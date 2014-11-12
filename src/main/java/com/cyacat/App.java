package com.cyacat;

import com.cyacat.engine.DatabaseVersioningEngine;
import com.cyacat.engine.ParameterHelper;
import com.cyacat.engine.ParameterValidationResult;
import com.cyacat.engine.runner.ScriptRunner;
import com.cyacat.engine.runner.SqlServerScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Conrad Yacat on 8/4/2014.
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            LOG.error("Please provide arguments!");
        }

        // setup
        if (args[0].startsWith("setup")) {
            String[] parts = args[0].split("=");
            createScriptDirectoryStruture(parts[1]);
            return;
        }

        PropertiesFactoryBean propFact = new PropertiesFactoryBean();
        propFact.setLocation(new ClassPathResource("app.properties"));
        propFact.afterPropertiesSet();
        Properties properties = propFact.getObject();

        // TODO: use the db.type value to determine the script runner
        String dbType = properties.getProperty("db.type");

        // parse the arguments into script runner parameters
        Map<String, String> parameters = ParameterHelper.parse(args);

        ScriptRunner scriptRunner = new SqlServerScriptRunner();
        List<String> expectedParameters = scriptRunner.getExpectedParameters();
        expectedParameters.add("script.dir");

        // validate the expected parameters
        ParameterValidationResult paramResult = ParameterHelper.validate(expectedParameters, parameters);
        if (!paramResult.isValid()) {
            // TODO: log the invalid/missing parameters
            LOG.error("Parameter error");
        }

        scriptRunner.setParameters(parameters);

        // add the parameters to the Properties
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }

        propFact.setProperties(properties);

        GenericApplicationContext parentContext = new GenericApplicationContext();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)parentContext.getBeanFactory();
        beanFactory.registerSingleton("props", propFact);
        parentContext.refresh();

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] {"classpath:application-context.xml"}, parentContext);

        beanFactory.registerSingleton("ScriptRunner", scriptRunner);
        appContext.refresh();

        DatabaseVersioningEngine engine = appContext.getBean(DatabaseVersioningEngine.class);

        if (!engine.run(parameters.get("script.dir"))) {
            LOG.error("Error while executing scripts");
            return;
        }

        LOG.info("Script executed");
    }

    private static void createScriptDirectoryStruture(String rootDirPath) {

        String preVersionPath = rootDirPath + File.separator + "01.preversion";
        createDirectory(preVersionPath);

        String versionPath = rootDirPath + File.separator + "02.version";
        createDirectory(versionPath);

        String dbObjectsPath = rootDirPath + File.separator + "03.dbobjects";
        createDirectory(dbObjectsPath);

        String postVersionPath = rootDirPath + File.separator + "03.postversion";
        createDirectory(postVersionPath);
    }

    private static void createDirectory(String path) {

        File dir = new File(path);

        if (dir.exists()) {
            LOG.info(path + " already exists");
        }

        if (!dir.mkdirs()) {
            LOG.info("Failed to create " + path);
        }

        LOG.info(path + " created");
    }
}