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
            JsonArray vendors = api.getResponseBody().get("vendors").getAsJsonArray();
            vendorsArray.getAsJsonArray("vendors").addAll(vendors);
        }
        System.out.println("vendorsArray = " + vendorsArray);
        stg.savePayloadByName(name, vendorsArray);
    }

    @Given("I create new vendor by name {string} with data")
    public void I_create_new_vendor_by_name_with_data(String name, Map<String, String> data) {
        JsonObject vendor = new JsonObject();
        for (String key : data.keySet()) {
            vendor.addProperty(key, data.get(key));
        }

        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(vendorsEndPoint);
        api.Post(vendor);
        String vendorId = useful_utils.getVendorIdFromURL(
                api.getResponseBody().get("vendor_url").getAsString());

        api.getResponseBody().addProperty("vendorId", vendorId);
        stg.savePayloadByName(name, vendor);
        System.out.println("vendorId = " + vendorId);
    }

    @Then("I validate vendor by id {string} created")
    public void I_validate_vendor_by_id_created(String expectedVendorId) {
        Assert.assertTrue(expectedVendorId + " is not found! - FAIL!",
                doesVendorIdExist(expectedVendorId));
        System.out.println(expectedVendorId + " is fount! - PASS!");
    }

    @Then("I validate vendor by id {string} does not exist")
    public void I_validate_vendor_by_id_does_not_exist(String expectedVendorId) {
        Assert.assertFalse(expectedVendorId + " is found! - FAIL!",
                doesVendorIdExist(expectedVendorId));
        System.out.println(expectedVendorId + " is not fount! - PASS!");
    }

    public boolean doesVendorIdExist(String vendorId) {
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
                if (actualId.equals(useful_utils.defineValue(vendorId))) {
                    System.out.println("We found our id, it is " + actualId);
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    @When("I update vendor by name {string} by id {string} with data")
    public void I_update_vendor_by_id_with_data(String name, String vendorId, Map<String, String> data) {
        JsonObject vendor = new JsonObject();
        for (String key : data.keySet()) {
            vendor.addProperty(key, data.get(key));
        }
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(vendorsByIdEndPoint.replace("{id}", useful_utils.defineValue(vendorId)));
        api.Patch(vendor);

        api.getResponseBody().addProperty("vendorId", vendorId);
        stg.savePayloadByName(name, api.getResponseBody());
    }

    @Then("I validate vendor by id {string} created/updated with data")
    public void I_validate_vendor_by_id_created_with_data(String vendorId, Map<String, String> data) {
        I_validate_vendor_by_id_created(vendorId);
        JsonArray vendors = api.getResponseBody().get("vendors").getAsJsonArray();
        for (int i = 0; i < vendors.size(); i++) {
            if (useful_utils.getVendorIdFromURL(vendors.get(i).getAsJsonObject().get("vendor_url").getAsString())
                    .equals(useful_utils.defineValue(vendorId))) {
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
}
