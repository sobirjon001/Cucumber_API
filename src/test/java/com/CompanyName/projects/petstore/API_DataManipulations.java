package com.CompanyName.projects.petstore;

import com.CompanyName.utils.ConfigurationReader;
import com.CompanyName.utils.Useful_Utils;
import com.CompanyName.utils.Util_stuff;
import com.google.gson.JsonObject;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class API_DataManipulations implements Util_stuff {

    Useful_Utils useful_utils = new Useful_Utils();

    private final String userEndPoint = "/user";
    private final String userByUsernameEndPoint = "/user/{username}";

    @When("I wait for {int} minutes")
    public void I_wait_for_minutes(int minutes) {
        try {
            Thread.sleep(1000L * minutes);
        } catch (Exception e) {
        }
    }

    @When("I create new user saving payload with name {string} with data")
    public void I_create_new_user_saving_payload_with_name_with_data(String name, Map<String, String> data) {
        JsonObject user = new JsonObject();
        for (String key : data.keySet()) {
            user.addProperty(key, useful_utils.defineValue(data.get(key)));
        }

        api.setBaseUrl(ConfigurationReader.getProperty("petStoreAPIBaseUrl"));
        api.setEndPoint(userEndPoint);
        api.Post(user);
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
        System.out.println("user = " + user);
        stg.savePayloadByName(name, user);
    }

    public JsonObject getUserByUsername(String usernamePath) {
        api.setBaseUrl(ConfigurationReader.getProperty("petStoreAPIBaseUrl"));
        api.setEndPoint(userByUsernameEndPoint.replace("{username}",
                useful_utils.defineValue(usernamePath)));
        api.Get();

        return api.getResponseBody();
    }

    public boolean doesUserByUsernameExist(String usernamePath) {
        getUserByUsername(usernamePath);
        return api.getResponse().getStatusCode() == 200;
    }

    @Then("I validate user by username {string} created")
    public void I_validate_user_by_username_created(String usernamePath) {
        Assert.assertTrue("username by " + usernamePath + " doesn't exist! - FAIL!",
                doesUserByUsernameExist(usernamePath));
        System.out.println("username by " + usernamePath + " created! - PASS!");
    }

    @Then("I validate user by username {string} does not exist")
    public void I_validate_user_by_username_does_not_exist(String usernamePath) {
        Assert.assertFalse("username by " + usernamePath + " exist! - FAIL!",
                doesUserByUsernameExist(usernamePath));
        Assert.assertEquals("Unexpected status code!", 404, api.getResponse().getStatusCode());
        System.out.println("username by " + usernamePath + " doesn't exist! - PASS!");
    }

    @Then("I validate user by username {string} created/updated with data")
    public void I_validate_user_by_username_created_with_data(String usernamePath, Map<String, String> data) {
        JsonObject actualUser = getUserByUsername(usernamePath);
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        for (String key : data.keySet()) {
            Assert.assertEquals(key + " mismatch! - FAIL!",
                    useful_utils.defineValue(data.get(key)), actualUser.get(key).getAsString());
            System.out.println(key + " matches! - PASS!");
        }
    }

    @When("I update user by username {string} and save with name {string} with data")
    public void I_update_user_by_username_and_save_with_name_with_data(String usernamePath, String name, Map<String, String> data) {
        JsonObject user = new JsonObject();
        for (String key : data.keySet()) {
            user.addProperty(key, useful_utils.defineValue(data.get(key)));
        }

        api.setBaseUrl(ConfigurationReader.getProperty("petStoreAPIBaseUrl"));
        api.setEndPoint(userByUsernameEndPoint.replace("{username}",
                useful_utils.defineValue(usernamePath)));
        api.Put(user);
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        stg.savePayloadByName(name, user);
    }

    @When("I delete a user by username {string}")
    public void I_delete_a_user_by_username(String usernamePath) {
        api.setBaseUrl(ConfigurationReader.getProperty("petStoreAPIBaseUrl"));
        api.setEndPoint(userByUsernameEndPoint.replace("{username}",
                useful_utils.defineValue(usernamePath)));
        api.Delete();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
    }
}
