package com.nexus.base;

import io.restassured.RestAssured;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected static Properties properties;
    public static String wiremockAdminUri; // Declared at class level

    @BeforeSuite
    public void setup() {

        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(fis);

            // Assigning variables from properties
            RestAssured.baseURI = properties.getProperty("base.uri");
            wiremockAdminUri = properties.getProperty("wiremock.admin.uri");

            System.out.println(">> System Bootstrapped");
            System.out.println(">> Target API: " + RestAssured.baseURI);
            System.out.println(">> WireMock Admin: " + wiremockAdminUri);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties. Check file path!");
        }
    }


    @AfterMethod
    public void verifyBackendJournal() {
        // This will run automatically after every single test method
        given()
                .baseUri(BaseTest.wiremockAdminUri)
                .when()
                .get("/__admin/requests")
                .then()
                .statusCode(200);

        System.out.println(">> Backend Integrity Check: PASSED");
    }
}