package com.automationexercise;

import org.testng.annotations.DataProvider;

import java.util.Random;

public class TestData {
    public static  String email = "user" + new Random().nextInt(1000) + "@test.com";
    @DataProvider(name = "registrationData")
    public Object[][] getRegistrationData() {
        return new Object[][] {
                {"GouravKumar", email, "password123"},
        };
    }
}
