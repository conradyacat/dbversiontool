package com.cyacat.engine;

/**
 * Created by Conrad Yacat on 8/5/2014.
 */
public enum ScriptStatusType {
    Executing("Executing"),
    Ok("Ok"),
    Error("Error");

    private final String value;

    private ScriptStatusType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
