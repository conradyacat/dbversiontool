package com.cyacat.engine.runner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Conrad Yacat on 8/5/2014.
 */
public interface ScriptRunner {

    int run(String filePath) throws IOException;

    List<String> getExpectedParameters();

    void setParameters(Map<String, String> parameters);
}
