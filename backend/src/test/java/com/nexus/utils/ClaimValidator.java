package com.nexus.utils;


import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class ClaimValidator {

    public static void assertSuccess(Response response, String expectedId) {
        response.then()
                .log().ifValidationFails() // Only logs if the test is about to turn Red
                .statusCode(201)
                .body("status", equalTo("Accepted"))
                .body("message", containsString(expectedId));
    }

    public static void assertRejection(Response response, String expectedErrorCode) {
        response.then()
                .log().ifValidationFails() // Forensic logging
                .statusCode(400)
                .body("error", equalTo(expectedErrorCode));
    }
}
