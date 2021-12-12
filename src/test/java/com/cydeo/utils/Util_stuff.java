package com.cydeo.utils;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface Util_stuff {

    API_Utils api = new API_Utils();
    Storage stg = Storage.getInstance();

    Faker faker = new Faker();
}
