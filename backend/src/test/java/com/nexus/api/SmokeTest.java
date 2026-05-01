package com.nexus.api;

import static io.restassured.RestAssured.*;

import com.nexus.base.BaseTest;
import org.testng.annotations.Test; // Notice the change from JUnit to TestNG

public class SmokeTest extends BaseTest {

    @Test(description = "Verify the Infrastructure Baseline is responding")
    public void testEnvironmentIsUp() {
        given()
                .baseUri("http://localhost:8080")
                .when()
                .get("/")
                .then()
                .statusCode(200);
    }

    @Test(description = "NEX-101: Verify System Health Check")
    @io.qameta.allure.Severity(io.qameta.allure.SeverityLevel.BLOCKER)
    public void verifyPlatformIsLive() {
        // We are checking the Nginx container we set up in Docker yesterday

                when()
                .get("/")
                .then()
                .statusCode(200);

    }
}
