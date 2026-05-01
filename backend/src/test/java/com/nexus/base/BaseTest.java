package com.nexus.base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.util.Properties;

public class BaseTest {

    protected static Properties config = new Properties();

    @BeforeSuite
    public void setup() {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            config.load(fis);

            // Set the Global Base URI for Rest-Assured
            RestAssured.baseURI = config.getProperty("base.uri");
            RestAssured.filters(new io.restassured.filter.log.RequestLoggingFilter(),
                    new io.restassured.filter.log.ResponseLoggingFilter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
