package com.cyacat.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Conrad Yacat on 11/11/2014.
 */
public final class ParameterValidationResult {

    private List<String> invalidList = new ArrayList<>();

    public boolean isValid() {
        return this.invalidList.size() == 0;
    }

    public void addInvalid(String parameter) {
        this.invalidList.add(parameter);
    }
}