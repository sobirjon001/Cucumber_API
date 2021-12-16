package com.CompanyName.projects.semantecBits;

import com.CompanyName.utils.ConfigurationReader;
import com.CompanyName.utils.Storage;
import com.CompanyName.utils.Useful_Utils;
import com.CompanyName.utils.Util_stuff;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class API_stepDefinitions implements Util_stuff {

    private final String issues = "/issues";
    private final String issuesById = "/issues/{id}";

    Storage stg = Storage.getInstance();
    Useful_Utils useful_utils = new Useful_Utils();

    private JsonObject updateOrAddValuesToGivenJsonObgect(JsonObject json, Map<String, String> data) {
        for (String key : data.keySet()) {
            switch (key) {
                case "snowids":
                case "provider_types":
                    json.add(key, new JsonArray());
                    for (String eachElement : data.get(key).split(", ")) {
                        json.getAsJsonArray(key).add(eachElement);
                    }
                    break;
                case "date_reported":
                case "date_resolved":
                    json.addProperty(key, useful_utils.defineValue(data.get(key)));
                    break;
                default:
                    json.addProperty(key, data.get(key));
            }
        }
        return json;
    }

    @When("I create new issue and save payload by name {string} with data")
    public void I_create_new_issue_and_save_payload_by_name_with_data(String name, Map<String, String> data) {
        api.setBaseUrl(ConfigurationReader.getProperty("semanticBitsBaseUrl"));
        api.setEndPoint(issues);
        api.Post(updateOrAddValuesToGivenJsonObgect(new JsonObject(), data));
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        stg.savePayloadByName(name, api.getResponseArray().get(0).getAsJsonObject());
    }

    private JsonObject getIssueById(String issueIdPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("semanticBitsBaseUrl"));
        api.setEndPoint(issuesById.replace("{id}",
                useful_utils.defineValue(issueIdPath)));
        api.Get();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        return api.getResponseArray().get(0).getAsJsonObject();
    }

    private void commonValidationOfJsonObjectWithValues(JsonObject jsonToTest, Map<String, String> data) {
        for (String key : data.keySet()) {
            switch (key) {
                case "snowids":
                case "provider_types":
                    Assert.assertEquals(key + " mismatch! - FAIL!",
                            useful_utils.defineValue(data.get(key)),
                            jsonToTest.get(key).getAsJsonArray().toString());
                    break;
                default:
                    if (data.get(key).equals("null")) {
                        Assert.assertTrue(key + " is not null! - FAIL!",
                                jsonToTest.get(key).isJsonNull());
                    } else {
                        Assert.assertEquals(key + " mismatch! - FAIL!",
                                useful_utils.defineValue(data.get(key)),
                                jsonToTest.get(key).getAsString());
                    }
            }
        }
    }

    @Then("I validate the issue by id {string} with data")
    public void I_validate_the_issue_by_id_with_data(String issueIdPath, Map<String, String> data) {
        commonValidationOfJsonObjectWithValues(getIssueById(issueIdPath), data);
    }

    @When("I update the issue by id {string} and save by name {string} with data")
    public void I_update_the_issue_by_id_and_save_by_name_with_data(String issueIdPath, String name, Map<String, String> data) {
        api.setBaseUrl(ConfigurationReader.getProperty("semanticBitsBaseUrl"));
        api.setEndPoint(issuesById.replace("{id}",
                useful_utils.defineValue(issueIdPath)));
        api.Put(updateOrAddValuesToGivenJsonObgect(getIssueById(issueIdPath), data));
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        stg.savePayloadByName(name, api.getResponseArray().get(0).getAsJsonObject());
    }
}
