package com.cydeo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.*;

public class API_Utils {

    Gson gson = new Gson();

    private String baseUrl = null;
    private String endPoint = null;
    private JsonObject responseBody = null;
    private Response response = null;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public JsonObject getResponseBody() {
        return responseBody;
    }

    public Response getResponse() {
        return response;
    }

    public void Get() {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON).
                        when()
                .get(baseUrl + endPoint);
        responseBody = gson.fromJson(response.getBody().asString(), JsonObject.class);
    }

    public void GetByQueryParam(String key, String value) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam(key, value).
                        when()
                .get(baseUrl + endPoint);
        responseBody = gson.fromJson(response.getBody().asString(), JsonObject.class);
    }

    public void Post() {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON).
                        when()
                .post(baseUrl + endPoint);
        responseBody = gson.fromJson(response.getBody().asString(), JsonObject.class);
    }

    public void Post(JsonObject payload) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload.toString()).
                        when()
                .post(baseUrl + endPoint);
        responseBody = gson.fromJson(response.getBody().asString(), JsonObject.class);
    }

    public void Patch(JsonObject payload) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload.toString()).
                        when()
                .patch(baseUrl + endPoint);
        responseBody = gson.fromJson(response.getBody().asString(), JsonObject.class);
    }

    public void Delete() {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON).
                        when()
                .delete(baseUrl + endPoint);
    }
}
