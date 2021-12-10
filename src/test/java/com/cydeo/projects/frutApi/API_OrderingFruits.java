package com.cydeo.projects.frutApi;

import com.cydeo.utils.ConfigurationReader;
import com.cydeo.utils.Useful_Utils;
import com.cydeo.utils.Util_stuff;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class API_OrderingFruits implements Util_stuff {

    Useful_Utils useful_utils = new Useful_Utils();

    final String customers = "/customers/";
    final String customersById = "/customers/{id}";

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

    @Then("I validate consumer id created by id {string}")
    public void I_validate_consumer_id_created_by_id(String expectedId) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(customers);
        api.Get();

        int count = Integer.parseInt(api.getResponseBody().get("meta").getAsJsonObject().get("count").getAsString());
        int limit = Integer.parseInt(api.getResponseBody().get("meta").getAsJsonObject().get("limit").getAsString());
        int pages = 0;
        while (count > 0) {
            count = count - limit;
            pages++;
        }
        boolean found = false;
        int page = 0;
        while (page < pages) {
            api.GetByQueryParam("page", String.valueOf(++page));
            JsonArray customers = api.getResponseBody().get("customers").getAsJsonArray();
            for (int i = 0; i < customers.size(); i++) {
                String actualId = customers.get(i).getAsJsonObject().get("customer_url").getAsString();
                actualId = actualId.substring(actualId.lastIndexOf("/") + 1);
                if (actualId.equals(useful_utils.defineValue(expectedId))) {
                    System.out.println("We found our id, it is " + actualId);
                    found = true;
                    break;
                }
            }
        }
        Assert.assertTrue(expectedId + " is not found! - FAIL!", found);
    }

    @When("I update customer by name {string} by id {string} with data")
    public void I_update_customer_by_id_with_data(String name, String id, Map<String, String> data) {
        JsonObject customer = new JsonObject();
        for (String key : data.keySet()) {
            customer.addProperty(key, data.get(key));
        }
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(customersById.replace("{id}", useful_utils.defineValue(id)));
        api.Patch(customer);

        api.getResponseBody().addProperty("id", id);
        stg.savePayloadByName(name, api.getResponseBody());
    }

    @Then("I validate consumer id created by id {string} with data")
    public void I_validate_consumer_id_created_by_id_with_data(String id, Map<String, String> data) {
        I_validate_consumer_id_created_by_id(id);
        JsonArray customers = api.getResponseBody().get("customers").getAsJsonArray();
        for (int i = 0; i < customers.size(); i++) {
            String _id = customers.get(i).getAsJsonObject().get("customer_url").getAsString();
            if (_id.substring(_id.lastIndexOf("/") + 1).equals(useful_utils.defineValue(id))) {
                for (String key : data.keySet()) {
                    Assert.assertEquals(key + " mismatch! - FAIL!",
                            useful_utils.defineValue(data.get(key)),
                            customers.get(i).getAsJsonObject().get(key).getAsString());
                    System.out.println(key + " matches! - PASS!");
                }
                break;
            }
        }
    }

    @When("I get customer by id {string}")
    public void I_get_customer_by_id(String id) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(customersById.replace("{id}", useful_utils.defineValue(id)));
        api.Get();
    }
}
