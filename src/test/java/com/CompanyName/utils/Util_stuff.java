package com.CompanyName.utils;

import com.github.javafaker.Faker;

public interface Util_stuff {

    API_Utils api = new API_Utils();
    Storage stg = Storage.getInstance();

    Faker faker = new Faker();
}
