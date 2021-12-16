package com.CompanyName.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Useful_Utils implements Util_stuff {

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String defineValue(String input) {
        if (input.contains("::")) {
            switch (input.substring(0, input.indexOf("::"))) {
                case "null":
                    return null;
                case "firstName":
                    return faker.name().firstName();
                case "lastName":
                    return faker.name().lastName();
                case "username":
                    return faker.name().username();
                case "email":
                    return faker.internet().emailAddress();
                case "password":
                    return faker.internet().password();
                case "phone":
                    return faker.phoneNumber().cellPhone();
                case "vendorName":
                    return faker.company().name();
                case "today":
                    return LocalDate.now(ZoneOffset.UTC).format(df);
                case "firstDayOfMonth":
                    return LocalDate.now(ZoneOffset.UTC).withDayOfMonth(1).format(df);
                case "lastDayOfMonth":
                    return LocalDate.now(ZoneOffset.UTC).withDayOfMonth(
                            LocalDate.now(ZoneOffset.UTC).lengthOfMonth()
                    ).format(df);
                case "uiid":
                    return String.valueOf(new Random().hashCode());
            }
        } else if (input.contains(":")) {
            return stg.getPayloadByName(input.split(":")[0])
                    .get(input.split(":")[1]).getAsString();
        } else if (input.contains("[")) {
            return stg.getPayloadByName(input.split("\\[")[0])
                    .get(input.split("\\[")[1]).getAsJsonArray().toString();
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

    public String getOrderIdFromURL(String order_url) {
        order_url = order_url.replace("/shop/orders/", "");
        StringBuilder orderId = new StringBuilder();
        for (Character character : order_url.toCharArray()) {
            if (Character.isDigit(character)) {
                orderId.append(character);
            } else {
                return orderId.toString();
            }
        }
        return orderId.toString();
    }

    public String getItemIdFromOrderURL(String order_url) {
        return order_url.substring(order_url.lastIndexOf("/") + 1);
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
