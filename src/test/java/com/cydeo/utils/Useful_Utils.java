package com.cydeo.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Useful_Utils implements Util_stuff {


    public String defineValue(String input) {
        if (input.contains("::")) {
            switch (input.substring(0, input.indexOf("::"))) {
                case "firstname":
                    return faker.name().firstName();
                case "lastname":
                    return faker.name().lastName();
                case "vendorName":
                    return faker.company().name();
            }
        } else if (input.contains(":")) {
            return stg.getPayloadByName(input.split(":")[0])
                    .get(input.split(":")[1]).getAsString();
        }
        return input;
    }

    public String getCustomerIdFromURL(String customer_url) {
        customer_url = customer_url.replace("/shop/customers/", "");
        StringBuilder customerId = new StringBuilder();
        for (Character character : customer_url.toCharArray()) {
            if (Character.isDigit(character)) {
                customerId.append(character);
            } else {
                return customerId.toString();
            }
        }
        return customerId.toString();
    }

    public String getVendorIdFromURL(String vendor_url) {
        vendor_url = vendor_url.replace("/shop/vendors/", "");
        StringBuilder vendorId = new StringBuilder();
        for (Character character : vendor_url.toCharArray()) {
            if (Character.isDigit(character)) {
                vendorId.append(character);
            } else {
                return vendorId.toString();
            }
        }
        return vendorId.toString();
    }

    public String getProductIdFromURL(String product_url) {
        product_url = product_url.replace("/shop/products/", "");
        StringBuilder productId = new StringBuilder();
        for (Character character : product_url.toCharArray()) {
            if (Character.isDigit(character)) {
                productId.append(character);
            } else {
                return productId.toString();
            }
        }
        return productId.toString();
    }

    public String getCategoryIdFromURL(String category_url) {
        return category_url.replace("/shop/categories/", "");
    }

    public int getNumberOfPagedBasedOnCountAndLimit(int count, int limit) {
        int pages = 0;
        while (count > 0) {
            count = count - limit;
            pages++;
        }
        return pages;
    }

    public int getNumberOfPagedBasedOnCountAndLimit(String count, String limit) {
        return getNumberOfPagedBasedOnCountAndLimit(Integer.parseInt(count), Integer.parseInt(limit));
    }

    public List<String> getListOfUncontainedElementsInsideLists(List<String> mainList, List<String> expectedContainedList) {
        return mainList.stream().filter(p -> !expectedContainedList.contains(p)).collect(Collectors.toList());
    }

    public List<String> getListOfValuesByKeyInGivenJsonArray(String key, JsonArray jsonArray) {
        List<String> result = new ArrayList<>();
        for (JsonElement eachElement : jsonArray) {
            result.add(eachElement.getAsJsonObject().get(key).getAsString());
        }
        return result;
    }
}
