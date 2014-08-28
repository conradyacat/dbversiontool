package com.cyacat.engine.runner;

/**
 * Created by Conrad Yacat on 8/5/2014.
 */
public interface ScriptRunner {

    int run(String filePath);

    String[] getExpectedParameters();
}
