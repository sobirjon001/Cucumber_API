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

import java.util.*;

public class API_ProductsCreateUpdateDelete implements Util_stuff {

    Useful_Utils useful_utils = new Useful_Utils();

    final String productsEndPoint = "/products/";
    final String productsByProductIdEndPoint = "/products/{productId}";
    final String categoriesEndPoint = "/categories/";
    final String categoriesByCategoryIdEndPoint = "/categories/{categoryId}";

    @Given("I get all products and save payload by name {string}")
    public void I_get_all_products_and_save_payload_by_name(String name) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(productsEndPoint);
        api.Get();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        JsonObject productsArray = new JsonObject();
        productsArray.add("products", new JsonArray());

        int pages = useful_utils.getNumberOfPagedBasedOnCountAndLimit(
                api.getResponseBody().getAsJsonObject("meta").get("count").getAsString(),
                api.getResponseBody().getAsJsonObject("meta").get("limit").getAsString()
        );
        int page = 0;
        while (page < pages) {
            api.GetByQueryParam("page", String.valueOf(++page));
            Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
            JsonArray products = api.getResponseBody().getAsJsonArray("products");
            productsArray.getAsJsonArray("products").addAll(products);
        }
        for (int i = 0; i < productsArray.getAsJsonArray("products").size(); i++) {
            productsArray.getAsJsonArray("products").get(i).getAsJsonObject().addProperty("productId",
                    useful_utils.getVendorIdFromURL(productsArray.getAsJsonArray("products").get(i)
                            .getAsJsonObject().get("product_url").getAsString()));
        }
        stg.savePayloadByName(name, productsArray);
    }

    @Given("I create new product by name {string} with data")
    public void I_create_new_product_by_name_with_data(String name, Map<String, String> data) {
        JsonObject product = new JsonObject();
        for (String key : data.keySet()) {
            product.addProperty(key, useful_utils.defineValue(data.get(key)));
        }

        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(productsEndPoint);
        api.Post(product);
        Assert.assertEquals("Unexpected status code!", 201, api.getResponse().getStatusCode());

        String productId = useful_utils.getProductIdFromURL(
                api.getResponseBody().get("product_url").getAsString());
        api.getResponseBody().addProperty("productId", productId);
        api.getResponseBody().addProperty("vendorId",
                useful_utils.getVendorIdFromURL(api.getResponseBody().get("vendor_url").getAsString()));
        api.getResponseBody().addProperty("categoryId",
                useful_utils.getCategoryIdFromURL(api.getResponseBody().get("category_url").getAsString()));

        stg.savePayloadByName(name, api.getResponseBody());
        System.out.println("productId = " + productId);
    }

    public JsonObject getProductByProductId(String productIdPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(productsByProductIdEndPoint.replace("{productId}",
                useful_utils.defineValue(productIdPath)));
        api.Get();

        return api.getResponseBody();
    }

    public boolean doesProductByProductIdExist(String productIdPath) {
        return !getProductByProductId(productIdPath).has("error");
    }

    @Then("I validate product by productId {string} created")
    public void I_validate_product_by_productId_created(String productIdPath) {
        Assert.assertTrue(productIdPath + " is not found! - FAIL!",
                doesProductByProductIdExist(productIdPath));
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
        System.out.println(productIdPath + " is fount! - PASS!");
    }

    @Then("I validate product by productId {string} does not exist")
    public void I_validate_product_by_productId_does_not_exist(String productIdPath) {
        Assert.assertFalse(productIdPath + " is found! - FAIL!",
                doesProductByProductIdExist(productIdPath));
        Assert.assertEquals("Unexpected status code!", 404, api.getResponse().getStatusCode());
        System.out.println(productIdPath + " is not fount! - PASS!");
    }

    @When("I update product by productId {string} and save with name {string} with data")
    public void I_update_product_by_productId_and_save_with_name_with_data(String productIdPath, String name, Map<String, String> data) {
        JsonObject product = new JsonObject();
        for (String key : data.keySet()) {
            product.addProperty(key, useful_utils.defineValue(data.get(key)));
        }
        String productId = useful_utils.defineValue(productIdPath);
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(productsByProductIdEndPoint.replace("{productId}", productId));
        api.Patch(product);
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        api.getResponseBody().addProperty("productId", productId);
        api.getResponseBody().addProperty("vendorId",
                useful_utils.getVendorIdFromURL(api.getResponseBody().get("vendor_url").getAsString()));
        api.getResponseBody().addProperty("categoryId",
                useful_utils.getCategoryIdFromURL(api.getResponseBody().get("category_url").getAsString()));
        stg.savePayloadByName(name, api.getResponseBody());
    }

    @Then("I validate product by productId {string} created/updated with data")
    public void I_validate_product_by_productId_created_with_data(String productIdPath, Map<String, String> data) {
        JsonObject products = getProductByProductId(productIdPath);
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
        for (String key : data.keySet()) {
            Assert.assertEquals(key + " mismatch! - FAIL!",
                    useful_utils.defineValue(data.get(key)),
                    products.get(key).getAsString());
            System.out.println(key + " matches! - PASS!");
        }
    }

    @When("I get product by productId {string} and save by {string}")
    public void I_get_product_by_productId(String productIdPath, String name) {
        getProductByProductId(productIdPath);
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        api.getResponseBody().addProperty("productId",
                useful_utils.getVendorIdFromURL(api.getResponseBody().get("product_url").getAsString()));
        stg.savePayloadByName(name, api.getResponseBody());
    }

    @When("I delete a product by productId {string}")
    public void I_delete_a_product_by_productId(String productIdPath) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(productsByProductIdEndPoint.replace("{productId}",
                useful_utils.defineValue(productIdPath)));
        api.Delete();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());
    }

    @Given("I get all categories and save payload by name {string}")
    public void I_get_all_categories_and_save_payload_by_name(String name) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(categoriesEndPoint);
        api.Get();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        JsonObject categoriesArray = new JsonObject();
        categoriesArray.add("categories", new JsonArray());
        JsonArray categories = api.getResponseBody().getAsJsonArray("categories");
        categoriesArray.getAsJsonArray("categories").addAll(categories);

        System.out.println("categoriesArray = " + categoriesArray);
        stg.savePayloadByName(name, categoriesArray);
    }

    @Then("I validate categories payload by name {string} has list")
    public void I_validate_categories_payload_by_name_has_list(String name, List<String> expectedCategories) {
        List<String> actualCategories = useful_utils.getListOfValuesByKeyInGivenJsonArray(
                "name", stg.getPayloadByName(name).getAsJsonArray("categories")
        );
        List<String> uncontainedExpected = useful_utils.getListOfUncontainedElementsInsideLists(expectedCategories, actualCategories);
        List<String> uncontainedActusl = useful_utils.getListOfUncontainedElementsInsideLists(actualCategories, expectedCategories);
        Assert.assertEquals("FAIL! - Actual categories don't have " + uncontainedExpected.toString(),
                0, uncontainedExpected.size());
        Assert.assertEquals("FAIL! - Actual categories have extra " + uncontainedActusl.toString(),
                0, uncontainedActusl.size());
        System.out.println("Actual categories are valid with expected list! - PASS!");
    }

    @When("I get all products by category {string} and save payload by name {string}")
    public void I_get_all_products_by_category_and_save_payload_by_name(String category, String name) {
        api.setBaseUrl(ConfigurationReader.getProperty("fruitAPIBaseUrl"));
        api.setEndPoint(categoriesByCategoryIdEndPoint.replace("{categoryId}",
                useful_utils.defineValue(category)));
        System.out.println("Fetching products by category " + category);
        api.Get();
        Assert.assertEquals("Unexpected status code!", 200, api.getResponse().getStatusCode());

        for (int i = 0; i < api.getResponseBody().getAsJsonArray("products").size(); i++) {
            api.getResponseBody().getAsJsonArray("products").get(i).getAsJsonObject().addProperty("productId",
                    useful_utils.getProductIdFromURL(api.getResponseBody().getAsJsonArray("products").get(i)
                            .getAsJsonObject().get("product_url").getAsString()));
        }
        stg.savePayloadByName(name, api.getResponseBody());
    }

    @Then("I validate products payload by name {string} contains coma separated list {string}")
    public void I_validate_products_payload_by_name_contains_coma_separated_list(String name, String comaSeparatedProductsNames) {
        List<String> expectedProducts = Arrays.asList(comaSeparatedProductsNames.split(", "));
        List<String> actualProducts = useful_utils.getListOfValuesByKeyInGivenJsonArray(
                "name", stg.getPayloadByName(name).getAsJsonArray("products")
        );
        List<String> uncontainedExpected = useful_utils.getListOfUncontainedElementsInsideLists(expectedProducts, actualProducts);
        Assert.assertEquals("FAIL! - Actual products don't have " + uncontainedExpected.toString(),
                0, uncontainedExpected.size());
        System.out.println("Actual products contain expected list in category " +
                stg.getPayloadByName(name).get("name").getAsString() + "! - PASS!");
    }
}
