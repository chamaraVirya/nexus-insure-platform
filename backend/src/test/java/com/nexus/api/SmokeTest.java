package com.nexus.api;

import static io.restassured.RestAssured.*;
import org.testng.annotations.Test; // Notice the change from JUnit to TestNG

public class SmokeTest {

    @Test(description = "Verify the Infrastructure Baseline is responding")
    public void testEnvironmentIsUp() {
        given()
                .baseUri("http://localhost:8080")
                .when()
                .get("/")
                .then()
                .statusCode(200);
    }
}
