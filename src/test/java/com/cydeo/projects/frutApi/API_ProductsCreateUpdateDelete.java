package com.cydeo.projects.frutApi;

import com.cydeo.utils.ConfigurationReader;
import com.cydeo.utils.Useful_Utils;
import com.cydeo.utils.Util_stuff;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.cucumber.java.en.Given;

public class API_ProductsCreateUpdateDelete implements Util_stuff {

    Useful_Utils useful_utils = new Useful_Utils();

    final String productsEndPoint = "/products/";
    final String productsByIdEndPoint = "/products/{id}";

    @Given("I get all products and save payload by name {string}")
    public void I_get_all_products_and_save_payload_by_name(String name) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(productsEndPoint);
        api.Get();

        JsonObject productsArray = new JsonObject();
        productsArray.add("products", new JsonArray());

        int pages = useful_utils.getNumberOfPagedBasedOnCountAndLimit(
                api.getResponseBody().getAsJsonObject("meta").get("count").getAsString(),
                api.getResponseBody().getAsJsonObject("meta").get("limit").getAsString()
        );
        int page = 0;
        while (page < pages) {
            api.GetByQueryParam("page", String.valueOf(++page));
            JsonArray products = api.getResponseBody().get("products").getAsJsonArray();
            productsArray.getAsJsonArray("products").addAll(products);
        }
        System.out.println("productsArray = " + productsArray);
        stg.savePayloadByName(name, productsArray);
    }
}
