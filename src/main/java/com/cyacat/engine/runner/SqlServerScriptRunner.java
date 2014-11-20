package com.cyacat.engine.runner;

import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Conrad Yacat on 8/5/2014.
 */
public class SqlServerScriptRunner implements ScriptRunner {

    private Properties parameters;

    @Override
    public int run(String filePath) throws IOException {

        String sqlCmdExePath = this.parameters.getProperty("sqlcmd.path");
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(sqlCmdExePath)) {
             sb.append("sqlcmd.exe");
        } else {
            sb.append(File.separator).append("sqlcmd.exe");
        }

        sb.append(" -b ");
        sb.append(" -S ").append(this.parameters.getProperty("db.server"));
        sb.append(" -E ");
        sb.append(" -d ").append(this.parameters.getProperty("db.name"));
        sb.append(" -U ").append(this.parameters.getProperty("db.user"));
        sb.append(" -P ").append(this.parameters.getProperty("db.pwd"));
        sb.append(" -i ").append(filePath);
        sb.append("\n\rRETURN %ERRORLEVEL%");

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter("dbscript.bat"));
            bw.write(sqlCmdExePath);
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        } finally {
            if (bw != null) {
                bw.flush();
                bw.close();
            }
        }

        try {
            Process process = Runtime.getRuntime().exec("cmd /c start dbscript.bat");
            return process.waitFor();
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return 1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public List<String> getExpectedParameters() {
        List<String> params = new ArrayList<>();
        params.add("db.server");
        params.add("db.user");
        params.add("db.pwd");
        params.add("db.name");
        params.add("sqlcmd.path");

        return params;
    }

    @Override
    public void setParameters(Properties parameters) {
        this.parameters = parameters;
    }
}
