package com.cyacat.engine;

import com.cyacat.data.domain.DatabaseVersion;
import com.cyacat.data.domain.DatabaseVersionPK;
import com.cyacat.data.repository.DatabaseVersionRepository;
import com.cyacat.engine.runner.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Conrad Yacat on 8/4/2014.
 */
@Lazy
@Component
public class DatabaseVersioningEngine {

    private final DatabaseVersionRepository databaseVersionRepository;
    private final ScriptRunner scriptRunner;

    @Autowired
    public DatabaseVersioningEngine(DatabaseVersionRepository databaseVersionRepository, ScriptRunner scriptRunner) {
        this.databaseVersionRepository = databaseVersionRepository;
        this.scriptRunner = scriptRunner;
    }

    public boolean run(String targetDirectory) {
        // get latest database script
        DatabaseVersion latestDbVersion = this.databaseVersionRepository.findFirstByOrderByDateExecutedDesc();

        // set the default version if there are no DATABASE_VERSION records
        if (latestDbVersion == null) {
            DatabaseVersionPK pk = new DatabaseVersionPK(1, 0, 0, "");
            latestDbVersion = new DatabaseVersion();
            latestDbVersion.setDatabaseVersionPK(pk);
        }

        // get the version script files
        Collection<DatabaseVersion> version = getVersionScripts(targetDirectory + "\\02.version", latestDbVersion);

        if (!CollectionUtils.isEmpty(version)) {
            latestDbVersion = version.stream().findFirst().get();
        }

        // preversion script files
        Collection<DatabaseVersion> preVersion = getNonVersionScripts(targetDirectory + "\\01.preversion", latestDbVersion);

        if (!runScripts(preVersion)) {
            return false;
        }

        if (!runScripts(version)) {
            return false;
        }

        // dbobject script files
        Collection<DatabaseVersion> dbObjects = getNonVersionScripts(targetDirectory + "\\03.dbobjects", latestDbVersion);

        if (!runScripts(dbObjects)) {
            return false;
        }

        // postversion script files
        Collection<DatabaseVersion> postVersion = getNonVersionScripts(targetDirectory + "\\04.postversion", latestDbVersion);

        if (!runScripts(postVersion)) {
            return false;
        }

        return true;
    }

    private boolean runScripts(Collection<DatabaseVersion> dbVersions) {

        for (DatabaseVersion dbVersion : dbVersions) {

            dbVersion.setExecutedBy(System.getProperty("user.name"));
            this.databaseVersionRepository.save(dbVersion);

            // failed - stop execution on failure
            try {
                if (this.scriptRunner.run(dbVersion.getScriptInfo()) != 0) {
                    dbVersion.setStatus(ScriptStatusType.Error.toString());
                    this.databaseVersionRepository.save(dbVersion);
                    return false;
                }

                // successful
                dbVersion.setStatus(ScriptStatusType.Ok.toString());
            } catch (IOException e) {
                e.printStackTrace();
                dbVersion.setStatus(ScriptStatusType.Error.toString());
            }

            this.databaseVersionRepository.save(dbVersion);
        }

        return true;
    }

    private Collection<DatabaseVersion> getVersionScripts(String directory, DatabaseVersion latestDbVersion) {
        File root = new File(directory);
        List<File> directories = getDirectories(root);
        Map<DatabaseVersionPK, DatabaseVersion> dbVersions = new HashMap<DatabaseVersionPK, DatabaseVersion>();

        // get all the files if there are no major-directories
        if (CollectionUtils.isEmpty(directories)) {
            List<File> files = getFiles(root);
        } else {
            for (File majorDir : directories) {
                // get all the minor-directories
                List<File> subDirectories = getDirectories(majorDir);
                long major = Integer.parseInt(majorDir.getName());

                for (File minorDir : subDirectories) {
                    // get all the version script files
                    List<File> files = getFiles(minorDir);
                    long minor = Integer.parseInt(minorDir.getName());

                    // add the files into the list
                    for (File script : files) {
                        long version = Integer.parseInt(script.getName().split("\\.")[0]);
                        DatabaseVersionPK pk = new DatabaseVersionPK(major, minor, version, root.getName());

                        // check if the script will be included
                        if (!shouldInclude(latestDbVersion, pk)) {
                            continue;
                        }

                        // check for duplicates here
                        if (dbVersions.containsKey(pk)) {
                            throw new RuntimeException(script.getName() + " - Duplicate scripts detected. Please check the version numbers " + pk.toString());
                        }

                        DatabaseVersion dbVersion = new DatabaseVersion();
                        dbVersion.setDatabaseVersionPK(pk);
                        dbVersion.setScriptInfo(script.getAbsolutePath());
                        dbVersion.setStatus(ScriptStatusType.Executing.toString());
                        dbVersions.put(pk, dbVersion);
                    }
                }
            }
        }

        // sort the db versions
        List<DatabaseVersion> sortedDbVersions = dbVersions.values().stream().sorted().collect(Collectors.toList());

        return sortedDbVersions;
    }

    private Collection<DatabaseVersion> getNonVersionScripts(String directory, DatabaseVersion latestDbVersion) {
        File root = new File(directory);
        List<File> directories = getDirectories(root);
        Map<DatabaseVersionPK, DatabaseVersion> dbVersions = new HashMap<DatabaseVersionPK, DatabaseVersion>();
        DatabaseVersionPK latestPK = latestDbVersion.getDatabaseVersionPK();

        // get all the files if there are no major-directories
        if (CollectionUtils.isEmpty(directories)) {
            List<File> files = getFiles(root);

            // add the files into the list
            for (File script : files) {
                long version = Integer.parseInt(script.getName().split("\\.")[0]);
                DatabaseVersionPK pk = new DatabaseVersionPK(latestPK.getMajorRelease(), latestPK.getMinorRelease(), latestPK.getVersion(), root.getName());
                DatabaseVersion dbVersion = new DatabaseVersion();
                dbVersion.setDatabaseVersionPK(pk);
                dbVersion.setScriptInfo(script.getAbsolutePath());
                dbVersion.setStatus(ScriptStatusType.Executing.toString());
                dbVersions.put(pk, dbVersion);
            }
        } else {
            for (File majorDir : directories) {
                // get all the minor-directories
                List<File> subDirectories = getDirectories(majorDir);
                long major = Integer.parseInt(majorDir.getName());

                for (File minorDir : subDirectories) {
                    // get all the version script files
                    List<File> files = getFiles(minorDir);
                    long minor = Integer.parseInt(minorDir.getName());

                    // add the files into the list
                    for (File script : files) {
                        long version = Integer.parseInt(script.getName().split("\\.")[0]);
                        DatabaseVersionPK pk = new DatabaseVersionPK(latestPK.getMajorRelease(), latestPK.getMinorRelease(), latestPK.getVersion(), root.getName());
                        DatabaseVersion dbVersion = new DatabaseVersion();
                        dbVersion.setDatabaseVersionPK(pk);
                        dbVersion.setScriptInfo(script.getAbsolutePath());
                        dbVersion.setStatus(ScriptStatusType.Executing.toString());
                        dbVersions.put(pk, dbVersion);
                    }
                }
            }
        }

        // sort the db versions
        List<DatabaseVersion> sortedDbVersions = dbVersions.values().stream().sorted().collect(Collectors.toList());

        return sortedDbVersions;
    }

    private List<File> getFiles(File directory) {
        List<File> files = Arrays.asList(directory.listFiles()).stream().filter((x) -> x.isFile()).collect(Collectors.toList());
        return files;
    }

    private List<File> getDirectories(File directory) {
        List<File> directories = Arrays.asList(directory.listFiles()).stream().filter((x) -> x.isDirectory()).collect(Collectors.toList());
        return directories;
    }

    private boolean shouldInclude(DatabaseVersion latestDbVersion, DatabaseVersionPK toBeIncludedPK) {

        DatabaseVersionPK latestPK = latestDbVersion.getDatabaseVersionPK();

        if (latestPK.getMajorRelease() > toBeIncludedPK.getMajorRelease()) {
            return false;
        } else if (latestPK.getMajorRelease() == toBeIncludedPK.getMajorRelease() &&
                latestPK.getMinorRelease() > toBeIncludedPK.getMinorRelease()) {
            return false;
        } else if (latestPK.getMajorRelease() == toBeIncludedPK.getMajorRelease() &&
                latestPK.getMinorRelease() == toBeIncludedPK.getMinorRelease() &&
                latestPK.getVersion() > toBeIncludedPK.getVersion()) {
            return false;
        } else if (latestPK.getMajorRelease() == toBeIncludedPK.getMajorRelease() &&
                latestPK.getMinorRelease() == toBeIncludedPK.getMinorRelease() &&
                latestPK.getVersion() == toBeIncludedPK.getVersion() &&
                latestDbVersion.getStatus() != ScriptStatusType.Error.toString()) {
            return false;
        }

        return true;
    }
}
