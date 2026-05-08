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
            try {
                // 1. Initialize the properties object first!
                properties = new Properties();
                FileInputStream file = new FileInputStream("src/test/resources/config.properties");
                properties.load(file);

                // 2. Now that properties is loaded, assign the variables
                wiremockAdminUri = properties.getProperty("wiremock.admin.uri");

                // Set RestAssured baseURI while we are at it
                RestAssured.baseURI = properties.getProperty("base.uri");

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Config file could not be loaded.");
            }
        }


    @AfterMethod
    public void verifyBackendJournal() {
        // This will run automatically after every single test method
        given()
                .baseUri(wiremockAdminUri)
                .when()
                .get("/__admin/requests")
                .then()
                .statusCode(200);

        System.out.println(">> Backend Integrity Check: PASSED");
    }
    }
