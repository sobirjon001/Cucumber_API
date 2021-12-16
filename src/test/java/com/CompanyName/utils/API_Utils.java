package com.CompanyName.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class API_Utils {

    Gson gson = new Gson();

    private String baseUrl = null;
    private String endPoint = null;
    private Response response = null;
    private String responseBodyAsString = null;
    private JsonObject responseBody = null;
    private JsonArray responseArray = null;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public JsonObject getResponseBody() {
        return responseBody;
    }

    public JsonArray getResponseArray() {
        return responseArray;
    }

    private void resolveBody() {
        responseBodyAsString = response.getBody().asPrettyString();
        if (responseBodyAsString.charAt(0) == '{') {
            responseBody = gson.fromJson(responseBodyAsString, JsonObject.class);
        } else if (responseBodyAsString.charAt(0) == '[') {
            responseArray = gson.fromJson(responseBodyAsString, JsonArray.class);
        }
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
        resolveBody();
    }

    public void GetByQueryParam(String key, String value) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam(key, value).
                        when()
                .get(baseUrl + endPoint);
        resolveBody();
    }

    public void Post() {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON).
                        when()
                .post(baseUrl + endPoint);
        resolveBody();
    }

    public void Post(JsonObject payload) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload.toString()).log().all().
                        when()
                .post(baseUrl + endPoint);
        resolveBody();
    }

    public void Put(JsonObject payload) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload.toString()).
                        when()
                .put(baseUrl + endPoint);
        resolveBody();
    }

    public void Patch(JsonObject payload) {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload.toString()).
                        when()
                .patch(baseUrl + endPoint);
        resolveBody();
    }

    public void Delete() {
        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON).
                        when()
                .delete(baseUrl + endPoint);
    }
}
