package com.automationexercise;

import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;


public class LoginTest extends BaseTest {


    private static final Logger LOGGER = LogManager.getLogger(LoginTest.class);
    private final By signUpLoginButton = By.xpath("//a[@href='/login']");
    private final By emailAlreadyExists = By.xpath("//p[contains(text(),'Email Address already exist!')]");



    @Test(
            enabled = true,
            groups = {"smoke"},
            priority = 0,
            dataProvider = "registrationData",
            dataProviderClass = TestData.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void testRegistration(String username, String email, String password) {
        LOGGER.info("========== 🚀 TestCase 01: Registration Test Started ==========");

        ExtentTest test = ExtentManager.getTest();
        test.info("🔍 Starting registration test for user: " + username);

        try {
            HomePage homePage = new HomePage(driver);
            LoginPage loginPage = new LoginPage(driver);

            homePage.verifyHomePage();
            test.info("✅ Verified Home Page loaded successfully");

            loginPage.registerNewUser(username, email, password);
            test.info("✅ User registered and account created: " + username);

            homePage.verifyHomePage();
            test.info("✅ Returned to Home Page after registration");

            test.pass("🎉 Registration flow completed successfully for: " + username);
            LOGGER.info("✅ TestCase 01: Registration Test Completed Successfully");
        } catch (Exception e) {
            test.fail("❌ Registration test failed: " + e.getMessage());
            LOGGER.error("❗ TestCase 01: Registration Test Failed", e);
            throw e; // Rethrow to respect retryAnalyzer
        } finally {
            LOGGER.info("===============================================================");
        }
    }



    @Test(
            enabled = true,
            groups = {"smoke"},
            priority = 1,
            dataProvider = "registrationData",
            dataProviderClass = TestData.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void testLogIn(String username, String email, String password) {
        LOGGER.info("========== 🔐 TestCase 02: Login Test Started ==========");

        ExtentTest test = ExtentManager.getTest();
        test.info("Starting login test for user: " + "user996@test.com");

        try {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.logIn("user996@test.com", "password123");
            test.info("✅ Login attempted with email: " + "user996@test.com");

            Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(), "Logout button is not visible. Login might have failed.");

            test.pass("🎉 Login successful for user: " + email);
            LOGGER.info("✅ TestCase 02: Login Test Passed for: " + email);
        } catch (Exception e) {
            test.fail("❌ Login failed for user: " + email + ". Error: " + e.getMessage());
            LOGGER.error("❗ TestCase 02: Login Test Failed", e);
            throw e; // Re-throw to allow retryAnalyzer to work
        } finally {
            LOGGER.info("=========================================================");
        }
    }



    @Test(
            enabled = true,
            groups = {"smoke"},
            priority = 2,
            dataProvider = "registrationData",
            dataProviderClass = TestData.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void testLogInWithInCorrectCredentials(String username, String email, String password) {
        LOGGER.info("========== 🔐 TestCase 03: Login with Invalid Credentials Started ==========");
        ExtentTest test = ExtentManager.getTest();
        test.info("Starting login with invalid credentials test for email: " + email);

        try {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.logIn(email, "invalidPassword123"); // Always test with wrong password

            // Wait for error message
            By errorMessage = By.xpath("//p[contains(text(),'password is incorrect')]");
            WebElement errorMessageElement = loginPage.waitUntilVisible(errorMessage);
            Assert.assertTrue(errorMessageElement.isDisplayed(), "❌ Error message is not displayed for incorrect login.");

            test.pass("✅ Error message is shown correctly for invalid credentials.");
            LOGGER.info("✅ TestCase 03: Login with Invalid Credentials Passed for: " + email);
        } catch (Exception e) {
            test.fail("❌ Login with invalid credentials failed unexpectedly. Error: " + e.getMessage());
            LOGGER.error("❗ TestCase 03: Login with Invalid Credentials Failed", e);
            throw e;
        } finally {
            LOGGER.info("=================================================================");
        }
    }



    @Test(
            enabled = true,
            groups = {"smoke"},
            priority = 3,
            dataProvider = "registrationData",
            dataProviderClass = TestData.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void testLogOut(String username, String email, String password) {
        LOGGER.info("========== 🔓 TestCase 04: Logout Test Started ==========");
        ExtentTest test = ExtentManager.getTest();
        test.info("Starting logout test for user: " + email);

        try {
            LoginPage loginPage = new LoginPage(driver);

            // Step 1: Log in
            loginPage.logIn("user996@test.com", "password123");

            // Step 2: Log out
            loginPage.logout();

            // Step 3: Verify redirection to login/signup page
            WebElement signUpButtonElement = loginPage.waitUntilVisible(signUpLoginButton);
            Assert.assertTrue(signUpButtonElement.isDisplayed(), "❌ Sign up button not visible after logout.");

            test.pass("✅ User was logged out successfully and redirected to login/signup screen.");
            LOGGER.info("✅ TestCase 04: Logout Test Passed for user: " + email);
        } catch (Exception e) {
            test.fail("❌ Logout failed due to an exception: " + e.getMessage());
            LOGGER.error("❗ TestCase 04: Logout Test Failed", e);
            throw e;
        } finally {
            LOGGER.info("==========================================================");
        }
    }


    @Test(
            enabled = true,
            groups = {"smoke"},
            priority = 4,
            dataProvider = "registrationData",
            dataProviderClass = TestData.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void registerUserWithExistingEmail(String username, String email, String password) {
        LOGGER.info("========== 🛑 TestCase 05: Register with Existing Email Started ==========");
        ExtentTest test = ExtentManager.getTest();
        test.info("Attempting to register user with already registered email: " + email);

        try {
            LoginPage loginPage = new LoginPage(driver);

            // Try to register with already existing email
            loginPage.registerNewUser("Gourav Kumar", "user996@test.com");  // use actual username & email from data provider

            WebElement errorMessageElement = loginPage.waitUntilVisible(emailAlreadyExists);
            Assert.assertTrue(errorMessageElement.isDisplayed(), "❌ Email already registered message not displayed");

            LOGGER.info("✅ Message displayed: " + errorMessageElement.getText());
            test.pass("🟢 Correct error shown for existing email: " + errorMessageElement.getText());

        } catch (Exception e) {
            test.fail("❌ Test failed due to exception: " + e.getMessage());
            LOGGER.error("❗ Exception during existing email registration test", e);
            throw e;
        } finally {
            LOGGER.info("========== 🛑 TestCase 05: Register with Existing Email Finished ==========");
        }
    }


    @Test(
            enabled = true,
            groups = {"smoke"},
            priority = 5,
            dataProvider = "registrationData",
            dataProviderClass = TestData.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void contactUsForm(String username, String email, String password) {
        LOGGER.info("========== 📩 Test Case 6: Contact Us Form Started ==========");
        ExtentTest test = ExtentManager.getTest();
        test.info("Starting Contact Us Form test with name: " + username + ", email: " + email);

        try {
            HomePage homePage = new HomePage(driver);

            // Verify Home Page is loaded before proceeding
            boolean isHome = homePage.verifyHomePage();
            Assert.assertTrue(isHome, "❌ Home page is not loaded before filling the form");
            test.pass("✅ Home page verified");

            // Fill Contact Us form
            homePage.contactUsForm(username, email);  // Assuming it fills name, email, message, and submits

            test.pass("✅ Contact form submitted successfully and confirmation message is displayed");

        } catch (Exception e) {
            LOGGER.error("❗ Exception occurred during Contact Us Form test", e);
            test.fail("❌ Contact Us Form test failed: " + e.getMessage());
            throw e;
        } finally {
            LOGGER.info("========== 📩 Test Case 6: Contact Us Form Finished ==========");
        }
    }


    @Test(
            enabled = true,
            groups = {"smoke"},
            priority = 5,
            dataProvider = "registrationData",
            dataProviderClass = TestData.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void verifyTestCasesPage(String username, String email, String password) {
        LOGGER.info("========== ✅ Test Case 7: Verify Test Cases Page Started ==========");
        ExtentTest test = ExtentManager.getTest(); // Get test instance for Extent report
        test.info("Starting Test Case 7: Navigating to Test Cases Page");

        try {
            HomePage homePage = new HomePage(driver);

            // Verify home page loads first
            boolean isHomeLoaded = homePage.verifyHomePage();
            Assert.assertTrue(isHomeLoaded, "❌ Home Page did not load successfully");
            test.pass("✅ Home page verified");

            // Navigate to Test Cases page and verify
            boolean isTestCasePageVisible = homePage.verifyTestCasePage();
            Assert.assertTrue(isTestCasePageVisible, "❌ Test Cases page is not displayed as expected");
            test.pass("✅ Test Cases Page displayed successfully");

        } catch (Exception e) {
            LOGGER.error("❗ Exception in Test Case 7: Verify Test Cases Page", e);
            test.fail("❌ Test Case failed due to exception: " + e.getMessage());
            throw e;
        } finally {
            LOGGER.info("========== ✅ Test Case 7: Verify Test Cases Page Finished ==========");
        }
    }


    @Test(
            enabled = true,
            groups = {"smoke"},
            priority = 5,
            dataProvider = "registrationData",
            dataProviderClass = TestData.class,
            retryAnalyzer = RetryAnalyzer.class
    )
    public void VerifyAllProductsAndProductDetailPage(String username, String email, String password) {
        LOGGER.info("========== ✅ Test Case 8: Verify All Products and Product Detail Page Started ==========");
        ExtentTest test = ExtentManager.getTest(); // Get the ExtentTest instance
        test.info("Starting Test Case 8: Navigating to All Products and verifying product detail");

        try {
            HomePage homePage = new HomePage(driver);

            // Step 1: Verify Home Page
            Assert.assertTrue(homePage.verifyHomePage(), "❌ Home page did not load properly");
            test.pass("✅ Home page verified");

            // Step 2: Navigate to All Products Page
            homePage.navigatedToAllProducts();
            test.pass("✅ Navigated to All Products page");

            // Step 3: Verify product list is visible
            Assert.assertTrue(homePage.verifyProductList(), "❌ Product list is not visible on All Products page");
            test.pass("✅ Product list is displayed");

            // Step 4: Click on a product and verify its details page
            Assert.assertTrue(homePage.viewProductDetails(), "❌ Failed to view product details");
            test.pass("✅ Product detail page opened");

            // Step 5: Verify product details content
            Assert.assertTrue(homePage.productDetailsVerified(), "❌ Product details are incomplete or missing");
            test.pass("✅ Product details are displayed correctly");

        } catch (Exception e) {
            LOGGER.error("❗ Exception in Test Case 8: Verify All Products and Product Detail Page", e);
            ExtentManager.getTest().fail("❌ Test Case failed due to exception: " + e.getMessage());
            throw e;
        } finally {
            LOGGER.info("========== ✅ Test Case 8: Verify All Products and Product Detail Page Finished ==========");
        }
    }



}
