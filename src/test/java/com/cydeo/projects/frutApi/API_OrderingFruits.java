package com.cydeo.projects.frutApi;

import com.cydeo.utils.ConfigurationReader;
import com.cydeo.utils.Util_stuff;
import com.google.gson.JsonObject;
import io.cucumber.java.en.Given;

import java.util.Map;

public class API_OrderingFruits implements Util_stuff {

    final String customers = "/customers/";

    @Given("I created new customer by name {string} with data")
    public void I_created_new_customer_by_name_with_data(String name, Map<String, String> data) {
        JsonObject customer = new JsonObject();
        for (String key : data.keySet()) {
            customer.addProperty(key, data.get(key));
        }

        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(customers);
        api.Post(customer);
        String id = api.getResponseBody().get("customer_url").getAsString();
        id = id.substring(id.lastIndexOf("/") + 1);

        customer.addProperty("id", id);
        stg.savePayloadByName(name, customer);
        System.out.println("id = " + id);
    }
}
