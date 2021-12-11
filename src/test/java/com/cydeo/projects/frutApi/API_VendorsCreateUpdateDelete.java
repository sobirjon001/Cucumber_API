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

public class API_VendorsCreateUpdateDelete implements Util_stuff {

    Useful_Utils useful_utils = new Useful_Utils();

    final String vendorsEndPoint = "/vendors/";
    final String vendorsByIdEndPoint = "/vendors/{id}";

    @Given("I get all vendors and save payload by name {string}")
    public void I_get_all_vendors_and_save_payload_by_name(String name) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(vendorsEndPoint);
        api.Get();

        JsonObject vendorsArray = new JsonObject();
        vendorsArray.add("vendors", new JsonArray());

        int pages = useful_utils.getNumberOfPagedBasedOnCountAndLimit(
                api.getResponseBody().getAsJsonObject("meta").get("count").getAsString(),
                api.getResponseBody().getAsJsonObject("meta").get("limit").getAsString()
        );
        int page = 0;
        while (page < pages) {
            api.GetByQueryParam("page", String.valueOf(++page));
            JsonArray vendors = api.getResponseBody().getAsJsonArray("vendors");
            vendorsArray.getAsJsonArray("vendors").addAll(vendors);
        }
        for (int i = 0; i < vendorsArray.getAsJsonArray("vendors").size(); i++) {
            vendorsArray.getAsJsonArray("vendors").get(i).getAsJsonObject().addProperty("vendorId",
                    useful_utils.getVendorIdFromURL(vendorsArray.getAsJsonArray("vendors").get(i)
                            .getAsJsonObject().get("vendor_url").getAsString()));
        }
        System.out.println("vendorsArray = " + vendorsArray);
        stg.savePayloadByName(name, vendorsArray);
    }

    @Given("I create new vendor by name {string} with data")
    public void I_create_new_vendor_by_name_with_data(String name, Map<String, String> data) {
        JsonObject vendor = new JsonObject();
        for (String key : data.keySet()) {
            vendor.addProperty(key, useful_utils.defineValue(data.get(key)));
        }

        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(vendorsEndPoint);
        api.Post(vendor);
        String vendorId = useful_utils.getVendorIdFromURL(
                api.getResponseBody().get("vendor_url").getAsString());

        api.getResponseBody().addProperty("vendorId", vendorId);
        stg.savePayloadByName(name, api.getResponseBody());
        System.out.println("vendorId = " + vendorId);
    }

    @Then("I validate vendor by vendorId {string} created")
    public void I_validate_vendor_by_id_created(String vendorIdPath) {
        Assert.assertTrue(vendorIdPath + " is not found! - FAIL!",
                doesVendorIdExist(vendorIdPath));
        System.out.println(vendorIdPath + " is fount! - PASS!");
    }

    @Then("I validate vendor by vendorId {string} does not exist")
    public void I_validate_vendor_by_id_does_not_exist(String vendorIdPath) {
        Assert.assertFalse(vendorIdPath + " is found! - FAIL!",
                doesVendorIdExist(vendorIdPath));
        System.out.println(vendorIdPath + " is not fount! - PASS!");
    }

    public boolean doesVendorIdExist(String vendorIdPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(vendorsEndPoint);
        api.Get();

        int pages = useful_utils.getNumberOfPagedBasedOnCountAndLimit(
                api.getResponseBody().getAsJsonObject("meta").get("count").getAsString(),
                api.getResponseBody().getAsJsonObject("meta").get("limit").getAsString()
        );
        boolean found = false;
        int page = 0;
        while (page < pages) {
            api.GetByQueryParam("page", String.valueOf(++page));
            JsonArray vendors = api.getResponseBody().get("vendors").getAsJsonArray();
            for (int i = 0; i < vendors.size(); i++) {
                String actualId = useful_utils.getVendorIdFromURL(
                        vendors.get(i).getAsJsonObject().get("vendor_url").getAsString());
                if (actualId.equals(useful_utils.defineValue(vendorIdPath))) {
                    System.out.println("We found our id, it is " + actualId);
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    @When("I update vendor by vendorId {string} and save with name {string} with data")
    public void I_update_vendor_by_id_and_save_with_name_with_data(String vendorIdPath, String name, Map<String, String> data) {
        JsonObject vendor = new JsonObject();
        for (String key : data.keySet()) {
            vendor.addProperty(key, data.get(key));
        }
        String vendorId = useful_utils.defineValue(vendorIdPath);
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(vendorsByIdEndPoint.replace("{id}", vendorId));
        api.Patch(vendor);

        api.getResponseBody().addProperty("vendorId", vendorId);
        stg.savePayloadByName(name, api.getResponseBody());
    }

    @Then("I validate vendor by vendorId {string} created/updated with data")
    public void I_validate_vendor_by_id_created_with_data(String vendorIdPath, Map<String, String> data) {
        I_validate_vendor_by_id_created(vendorIdPath);
        JsonArray vendors = api.getResponseBody().get("vendors").getAsJsonArray();
        for (int i = 0; i < vendors.size(); i++) {
            if (useful_utils.getVendorIdFromURL(vendors.get(i).getAsJsonObject().get("vendor_url").getAsString())
                    .equals(useful_utils.defineValue(vendorIdPath))) {
                for (String key : data.keySet()) {
                    Assert.assertEquals(key + " mismatch! - FAIL!",
                            useful_utils.defineValue(data.get(key)),
                            vendors.get(i).getAsJsonObject().get(key).getAsString());
                    System.out.println(key + " matches! - PASS!");
                }
                break;
            }
        }
    }

    @When("I get vendor by vendorId {string} and save by {string}")
    public void I_get_vendor_by_id(String vendorIdPath, String name) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(vendorsByIdEndPoint.replace("{id}", useful_utils.defineValue(vendorIdPath)));
        api.Get();

        api.getResponseBody().addProperty("vendorId",
                useful_utils.getVendorIdFromURL(api.getResponseBody().get("vendor_url").getAsString()));
        stg.savePayloadByName(name, api.getResponseBody());
    }

    @When("I delete a vendor by vendorId {string}")
    public void I_delete_a_vendor_by_vendorId(String vendorIdPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(vendorsByIdEndPoint.replace("{id}", useful_utils.defineValue(vendorIdPath)));
        api.Delete();
    }
}
