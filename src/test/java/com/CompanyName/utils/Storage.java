package com.CompanyName.utils;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class Storage {

    private static Storage storage;
    private HashMap<String, JsonObject> payloads = new HashMap<>();

    private Storage() {
        storage = this;
    }

    public static Storage getInstance() {
        return storage == null
                ? new Storage()
                : storage;
    }

    public static void reset() {
        storage = new Storage();
    }

    public void savePayloadByName(String name, JsonObject payload) {
        payloads.put(name, payload);
    }

    public JsonObject getPayloadByName(String name) {
        return payloads.get(name);
    }
}
