package com.cyacat.engine.runner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Conrad Yacat on 8/5/2014.
 */
public class SqlServerScriptRunner implements ScriptRunner {

    private Map<String, String> parameters;

    @Override
    public int run(String filePath) throws IOException {

        String sqlCmdExePath = this.parameters.get("sqlcmd.path") + File.separator + "sqlcmd.exe --";

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
        params.add("db.uid");
        params.add("db.pwd");
        params.add("db.database");
        params.add("sqlcmd.path");

        return params;
    }

    @Override
    public void setParameters(Map<String, String> parameters) {
        parameters = parameters;
    }
}
