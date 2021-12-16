package com.CompanyName.projects.frutApi;

import com.CompanyName.utils.ConfigurationReader;
import com.CompanyName.utils.Useful_Utils;
import com.CompanyName.utils.Util_stuff;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class API_CustomerCreateUpdateDelete implements Util_stuff {

    Useful_Utils useful_utils = new Useful_Utils();

    final String customersEndPoint = "/customers/";
    final String customersByIdEndPoint = "/customers/{id}";

    @Given("I created new customer by name {string} with data")
    public void I_created_new_customer_by_name_with_data(String name, Map<String, String> data) {
        JsonObject customer = new JsonObject();
        for (String key : data.keySet()) {
            customer.addProperty(key, useful_utils.defineValue(data.get(key)));
        }

        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(customersEndPoint);
        api.Post(customer);
        Assert.assertEquals("Unexpected status code!", 201, api.getResponse().getStatusCode());
        String id = useful_utils.getCustomerIdFromURL(
                api.getResponseBody().get("customer_url").getAsString());

        api.getResponseBody().addProperty("id", id);
        stg.savePayloadByName(name, api.getResponseBody());
        System.out.println("Created ne customer by id = " + id);
    }

    @When("I get all customers and save payload by name {string}")
    public void I_get_all_customers_and_save_payload_by_name(String name) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(customersEndPoint);
        api.Get();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        JsonObject customersArray = new JsonObject();
        customersArray.add("customers", new JsonArray());

        int pages = useful_utils.getNumberOfPagedBasedOnCountAndLimit(
                api.getResponseBody().getAsJsonObject("meta").get("count").getAsString(),
                api.getResponseBody().getAsJsonObject("meta").get("limit").getAsString()
        );
        int page = 0;
        while (page < pages) {
            api.GetByQueryParam("page", String.valueOf(++page));
            Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
            JsonArray customers = api.getResponseBody().get("customers").getAsJsonArray();
            customersArray.getAsJsonArray("customers").addAll(customers);
        }

        for (int i = 0; i < customersArray.getAsJsonArray("customers").size(); i++) {
            customersArray.getAsJsonArray("customers").get(i).getAsJsonObject().addProperty("id",
                    useful_utils.getCustomerIdFromURL(customersArray.getAsJsonArray("customers").get(i)
                            .getAsJsonObject().get("customer_url").getAsString()));
        }
        stg.savePayloadByName(name, customersArray);
    }

    @Then("I validate customer by id {string} created")
    public void I_validate_customer_by_id_created(String idPath) {
        Assert.assertTrue(idPath + " is not found! - FAIL!",
                doesCustomerIdExist(idPath));
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
        System.out.println(idPath + " is fount! - PASS!");
    }

    @Then("I validate customer by id {string} does not exist")
    public void I_validate_customer_by_id_does_not_exist(String idPath) {
        Assert.assertFalse(idPath + " is found! - FAIL!",
                doesCustomerIdExist(idPath));
        Assert.assertEquals("Unexpected status code!", 404, api.getResponse().getStatusCode());
        System.out.println(idPath + " is not fount! - PASS!");
    }

    public JsonObject I_get_customer_by_id(String idPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(customersByIdEndPoint.replace("{id}", useful_utils.defineValue(idPath)));
        api.Get();

        return api.getResponseBody();
    }

    public boolean doesCustomerIdExist(String idPath) {
        return !I_get_customer_by_id(idPath).has("error");
    }

    @When("I update customer by id {string} and save with name {string} with data")
    public void I_update_customer_by_id_and_save_with_name_with_data(String idPath, String name, Map<String, String> data) {
        JsonObject customer = new JsonObject();
        for (String key : data.keySet()) {
            customer.addProperty(key, useful_utils.defineValue(data.get(key)));
        }
        String id = useful_utils.defineValue(idPath);
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(customersByIdEndPoint.replace("{id}", id));
        api.Patch(customer);
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        api.getResponseBody().addProperty("id", id);
        stg.savePayloadByName(name, api.getResponseBody());
    }

    @Then("I validate customer by id {string} created/updated with data")
    public void I_validate_customer_by_id_created_with_data(String idPath, Map<String, String> data) {
        I_validate_customer_by_id_created(idPath);
        for (String key : data.keySet()) {
            Assert.assertEquals(key + " mismatch! - FAIL!",
                    useful_utils.defineValue(data.get(key)),
                    api.getResponseBody().get(key).getAsString());
            System.out.println(key + " matches! - PASS!");
        }
    }

    @When("I get customer by id {string} and save by name {string}")
    public void I_get_customer_by_id_and_save_by_name(String idPath, String name) {
        I_get_customer_by_id(idPath);
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        api.getResponseBody().addProperty("id",
                useful_utils.getCustomerIdFromURL(api.getResponseBody().get("orders_url").getAsString()));
        stg.savePayloadByName(name, api.getResponseBody());
    }

    @When("I delete a customer by id {string}")
    public void I_delete_a_customer_by_id(String idPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(customersByIdEndPoint.replace("{id}", useful_utils.defineValue(idPath)));
        api.Delete();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
    }
}
