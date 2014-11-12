package com.cyacat.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

    public static void parseAndMerge(String[] args, Properties properties) {
        Map<String, String> parameters = parse(args);
        // add the parameters to the Properties
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
    }

    public static ParameterValidationResult validate(List<String> expectedParameters, Properties properties) {
        ParameterValidationResult result = new ParameterValidationResult();
        for (String key : expectedParameters) {
            if (!properties.containsKey(key)) {
                result.addInvalid(key);
            }
        }

        return result;
    }
}
