package com.cyacat.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Conrad Yacat on 11/11/2014.
 */
public final class ParameterHelper {

    public static Map<String, String> parse(String[] args) {
        Map<String, String> parameters = new HashMap<>(args.length);
        for (int i = 0; i < args.length; i++) {
            String[] parts = args[i].split("=");
            parameters.put(parts[0], parts[1]);
        }

        return parameters;
    }

    public static ParameterValidationResult validate(List<String> expectedParameters, Map<String, String> parameters) {
        ParameterValidationResult result = new ParameterValidationResult();
        for (String p : expectedParameters) {
            if (!parameters.containsKey(p)) {
                result.addInvalid(p);
            }
        }

        return result;
    }
}
