package com.cyacat.engine.runner;

import com.cyacat.data.domain.DatabaseVersion;

/**
 * Created by Conrad Yacat on 8/5/2014.
 */
public class SqlServerScriptRunner implements ScriptRunner {

    @Override
    public int run(String filePath) {

        return 1;
    }

    @Override
    public String[] getExpectedParameters() {
        String[] params = new String[4];
        params[0] = "db.server";
        params[1] = "db.uid";
        params[2] = "db.pwd";
        params[3] = "db.database";

        return params;
    }
}
