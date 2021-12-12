package com.cydeo.projects.frutApi;

import com.cydeo.utils.ConfigurationReader;
import com.cydeo.utils.Useful_Utils;
import com.cydeo.utils.Util_stuff;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class API_OrdersCreateUpdateDelete implements Util_stuff {

    final String ordersEndPoint = "/orders/";
    final String ordersByOrderIdEndPoint = "/orders/{orderId}";
    final String ordersActionsByIdEndPoint = "/orders/{orderId}/actions/";
    final String ordersByIdItemsEndPoint = "/orders/{orderId}/items/";
    final String ordersByIdItemsByIdEndPoint = "/orders/{orderId}/items/{itemId}";
    final String ordersForCustomerByIdEndPoint = "/customers/{id}/orders/";

    Useful_Utils useful_utils = new Useful_Utils();

    @When("I get all orders and save payload by name {string}")
    public void I_get_all_orders_and_save_payload_by_name(String name) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(ordersEndPoint);
        api.Get();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        JsonObject ordersArray = new JsonObject();
        ordersArray.add("orders", new JsonArray());

        int pages = useful_utils.getNumberOfPagedBasedOnCountAndLimit(
                api.getResponseBody().getAsJsonObject("meta").get("count").getAsString(),
                api.getResponseBody().getAsJsonObject("meta").get("limit").getAsString()
        );
        int page = 0;
        while (page < pages) {
            api.GetByQueryParam("page", String.valueOf(++page));
            Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
            JsonArray orders = api.getResponseBody().get("orders").getAsJsonArray();
            ordersArray.getAsJsonArray("orders").addAll(orders);
        }

        for (int i = 0; i < ordersArray.getAsJsonArray("orders").size(); i++) {
            ordersArray.getAsJsonArray("orders").get(i).getAsJsonObject().addProperty("orderId",
                    useful_utils.getOrderIdFromURL(ordersArray.getAsJsonArray("orders").get(i)
                            .getAsJsonObject().get("order_url").getAsString()));
        }
        stg.savePayloadByName(name, ordersArray);
    }

    @When("I create new order for customer by id {string} and save payload by name {string}")
    public void I_create_new_order_for_customer_by_id_and_save_payload_by_name(String idPath, String name) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(ordersForCustomerByIdEndPoint.replace("{id}",
                useful_utils.defineValue(idPath)));
        api.Post();
        Assert.assertEquals("Unexpected status code!", 201, api.getResponse().getStatusCode());

        String orderId = useful_utils.getOrderIdFromURL(api.getResponseBody().get("items_url").getAsString());
        api.getResponseBody().addProperty("orderId", orderId);
        stg.savePayloadByName(name, api.getResponseBody());
        System.out.println("Successfully created order by orderId " + orderId);
    }

    public JsonObject getOrderByOrderId(String orderIdPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(ordersByOrderIdEndPoint.replace("{orderId}",
                useful_utils.defineValue(orderIdPath)));
        api.Get();

        return api.getResponseBody();
    }

    public boolean doesOrderByOrderIdExist(String orderIdPath) {
        return !getOrderByOrderId(orderIdPath).has("error");
    }

    @Then("I validate order by orderId {string} created/updated with data")
    public void I_validate_order_by_orderId_created_with_data(String orderIdPath, Map<String, String> data) {
        JsonObject actualOrder = getOrderByOrderId(orderIdPath);
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        for (String key : data.keySet()) {
            String actualValue = "1";
            String expectedValue = "1";
            switch (key) {
                case "createdAt":
                case "updatedAt":
                    actualValue = actualOrder.get(key).getAsString().substring(0, 10);
                    break;
                case "actions":
                    actualValue = actualOrder.getAsJsonObject(key).has("purchase")
                            ? "purchase" : null;
                    break;
                default:
                    actualValue = actualOrder.get(key).getAsString();
            }
            expectedValue = useful_utils.defineValue(data.get(key));
            Assert.assertEquals(key + " mismatch! - FAIL!",expectedValue, actualValue);
            System.out.println(key + " match expected value of " + actualValue + " - PASS!");
        }
    }

    @Then("I verify order by orderId {string} created for customer by id {string}")
    public void I_verify_order_by_orderId_created_for_customer_by_id(String orderIdPath, String customerIdPath) {
        Assert.assertTrue(customerIdPath + " doesn't have order by " + orderIdPath + "! - FAIL!",
                doesOrderIdExistForCustomerId(customerIdPath, orderIdPath));
        System.out.println(customerIdPath + " have order by " + orderIdPath + "! - PASS!");
    }

    @Then("I verify order by orderId {string} does not exist for customer by id {string}")
    public void I_verify_order_by_orderId_does_not_exist_for_customer_by_id(String orderIdPath, String customerIdPath) {
        Assert.assertFalse(customerIdPath + " have order by " + orderIdPath + "! - FAIL!",
                doesOrderIdExistForCustomerId(customerIdPath, orderIdPath));
        System.out.println(customerIdPath + " doesn't have order by " + orderIdPath + "! - PASS!");
    }

    public boolean doesOrderIdExistForCustomerId(String customerIdPath, String orderIdPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(ordersForCustomerByIdEndPoint.replace("{id}",
                useful_utils.defineValue(customerIdPath)));
        api.Get();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        JsonArray orders = api.getResponseBody().getAsJsonArray("orders");
        for (JsonElement eachOrder : orders) {
            if (useful_utils.getOrderIdFromURL(eachOrder.getAsJsonObject().get("order_url").getAsString())
                    .equals(useful_utils.defineValue(orderIdPath))) {
                return true;
            }
        }
        return false;
    }

    @Then("I validate order by orderId {string} created")
    public void I_validate_order_by_orderId_created(String orderIdPath) {
        Assert.assertTrue(orderIdPath + " is not found! - FAIL!",
                doesOrderByOrderIdExist(orderIdPath));
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
        System.out.println(orderIdPath + " is fount! - PASS!");
    }

    @Then("I validate order by orderId {string} does not exist")
    public void I_validate_order_by_orderId_does_not_exist(String orderIdPath) {
        Assert.assertFalse(orderIdPath + " is found! - FAIL!",
                doesOrderByOrderIdExist(orderIdPath));
        Assert.assertEquals("Unexpected status code!", 404, api.getResponse().getStatusCode());
        System.out.println(orderIdPath + " is not fount! - PASS!");
    }

    @When("I delete order by orderId {string}")
    public void I_delete_order_by_orderId(String orderIdPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(ordersByOrderIdEndPoint.replace("{orderId}",
                useful_utils.defineValue(orderIdPath)));
        api.Delete();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
    }
}
