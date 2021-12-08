package com.cydeo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.*;

public class API_Utils {

    Gson gson = new Gson();

    String baseUrl = null;
    String endPoint = null;
    JsonObject responseBody = null;
    JsonArray responseBodies = null;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public JsonObject getResponseBody() {
        return responseBody;
    }

    public void Post(JsonObject payload) {
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(payload.toString()).
                        when()
                        .post(baseUrl + endPoint).prettyPeek();
        Assert.assertEquals(response.getStatusCode(), 201);
        responseBody = gson.fromJson(response.getBody().asString(), JsonObject.class);
    }
}
