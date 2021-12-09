package com.cydeo.utils;

public class Useful_Utils implements Util_stuff {


    public String defineValue(String input) {
        if (input.contains("::")) {
            switch (input.substring(0, input.indexOf("::"))) {
                case "firstname":
                    return faker.name().firstName();
                case "lastname":
                    return faker.name().lastName();
            }
        } else if (input.contains(".")) {
            return stg.getPayloadByName(input.split("\\.")[0])
                    .get(input.split("\\.")[1]).getAsString();
        }
        return null;
    }
}
