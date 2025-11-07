package com.napier.devops;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class WebServerIntegrationTest {

    @BeforeAll
    static void setup() throws Exception {
        // Configure RestAssured base URI
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;

        // Start the web server (runs Spark)
        new com.napier.devops.www.WebServer().start();

        // Spark needs a moment to initialize
        Thread.sleep(2000);
    }

    @Test
    void testRootEndpoint() {
        get("/")
                .then()
                .statusCode(200)
                .contentType("text/html")
                .body(containsString("<!DOCTYPE html>"))
                .body(containsString("<title>World Reporting App</title>"));
    }

    @Test
    void testCountriesDefault() {
        get("/reports/countries")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", not(empty()));
    }

    @Test
    void testCitiesByContinent() {
        given()
                .queryParam("scope", "continent")
                .queryParam("name", "Asia")
                .when()
                .get("/reports/cities")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", not(empty()));
    }

    @Test
    void testPopulationsGlobal() {
        get("/reports/populations")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("globalPopulation", greaterThan(0L))
                .body("data", not(empty()));
    }

    @Test
    void testLanguagesDefault() {
        get("/reports/languages")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", not(empty()));
    }

    @Test
    void testLookupContinents() {
        get("/lookups/continents")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", not(empty()));
    }

    @Test
    void testLookupDistricts_missingParam() {
        get("/lookups/districts")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing country parameter"));
    }
}
