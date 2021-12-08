package com.cydeo.utils;

import com.google.gson.JsonObject;

import java.util.Map;

public class Storage {

    public Storage() {
    }

    private Map<String, JsonObject> payloads;

    private Storage storage = null;

    public Storage getInstance() {
        return storage == null
                ? new Storage()
                : storage;
    }

    public void savePayloadByName(String name, JsonObject payload) {
        payloads.put(name, payload);
    }

    public JsonObject getPayloadByName(String name) {
        return payloads.get(name);
    }
}
