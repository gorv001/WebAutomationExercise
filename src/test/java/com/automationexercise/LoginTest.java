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



    @Test(enabled = false, groups = {"smoke"},priority = 0, dataProvider = "registrationData",dataProviderClass = TestData.class,retryAnalyzer = RetryAnalyzer.class)
    public void testRegistration(String username, String email, String password) {
        LOGGER.info("===============================================");
        ExtentTest test = ExtentManager.getTest(); // Safely get the test instance
        LOGGER.info("TestCase 01: First Test Case is Started");
        test.info("Starting Registration test");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.verifyHomePage();
        loginPage.registerNewUser(username, email, password);
        Assert.assertTrue(loginPage.verifyHomePage(), "The registration is failed");

        LOGGER.info("TestCase 01: First Test Case is Finished");
        test.pass("Registration was successful");
        LOGGER.info("===============================================");
    }


    @Test(enabled = false,groups = {"smoke"},priority = 1, dataProvider = "registrationData",dataProviderClass = TestData.class,retryAnalyzer = RetryAnalyzer.class )
    public void testLogIn(String username,String email, String password) {
        LOGGER.info("===============================================");
        ExtentTest test = ExtentManager.getTest(); // Safely get the test instance
        LOGGER.info("TestCase 02: LogoIn Test Case is Started");
        test.info("Starting LogoIn test");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.logIn("user996@test.com", "password123");
        By logoutButton = By.xpath("//a[@href='/logout']");
        WebElement logoutButtonElement = driver.findElement(logoutButton);
        Assert.assertTrue(logoutButtonElement.isDisplayed());

        LOGGER.info("TestCase 02: LogIn Test Case is Finished");
        test.pass("LogIn was successful");
        LOGGER.info("===============================================");
    }


    @Test(enabled = false,groups = {"smoke"},priority = 2,dataProvider = "registrationData",dataProviderClass = TestData.class,retryAnalyzer = RetryAnalyzer.class )
    public void testLogInWithInCorrectCredentials(String username,String email, String password) {
        LOGGER.info("===============================================");
        ExtentTest test = ExtentManager.getTest(); // Safely get the test instance
        LOGGER.info("TestCase 03: LogoIn with incorrect credentials Test Case is Started");
        test.info("Starting LogoIn with incorrect credentials test");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.logIn("user996@test.com", "123111");
        By errorMessage = By.xpath("//p[contains(text(),'password is incorrect')]");
        WebElement errorMessageElement = driver.findElement(errorMessage);
        Assert.assertTrue(errorMessageElement.isDisplayed());

        LOGGER.info("TestCase 03: LogoIn with incorrect credentials Test Case is Passed");
        test.pass("LogInWithInCorrectCredentials was successful");
        LOGGER.info("===============================================");
    }


    @Test(enabled = false,groups = {"smoke"},priority = 3,dataProvider = "registrationData",dataProviderClass = TestData.class,retryAnalyzer = RetryAnalyzer.class )
    public void testLogOut(String username,String email, String password) {
        LOGGER.info("===============================================");
        ExtentTest test = ExtentManager.getTest(); // Safely get the test instance
        LOGGER.info("TestCase 04: Logout Test Case is Started");
        test.info("Starting LogOut test");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.logIn("user996@test.com", "password123");
        loginPage.logout();
        WebElement signUpButton = driver.findElement(signUpLoginButton);
        Assert.assertTrue(signUpButton.isDisplayed());

        LOGGER.info("TestCase 04: Logout Test Case is Finished");
        test.pass("LogOut was successful");
        LOGGER.info("===============================================");
    }

    @Test(groups = {"smoke"},priority = 4,dataProvider = "registrationData",dataProviderClass = TestData.class,retryAnalyzer = RetryAnalyzer.class )
    public void  RegisterUserWithExistingEmail(String username,String email, String password) {
        LOGGER.info("===============================================");
        ExtentTest test = ExtentManager.getTest(); // Safely get the test instance
        LOGGER.info("TestCase 05:  Register User with existing email Test Case is Started");
        test.info("Starting  Register User with existing email test");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.registerNewUser("Gourav","user996@test.com");
        WebElement signUpButton = driver.findElement(emailAlreadyExists);
        LOGGER.info(signUpButton.getText()+" Success- Your email is already registered");
        Assert.assertTrue(signUpButton.isDisplayed());

        LOGGER.info("TestCase 05:  Register User with existing email Test Case is Finished");
        test.pass("Register User with existing email was successful");
        LOGGER.info("===============================================");
    }

    @Test(enabled = false,groups = {"smoke"},priority = 5,dataProvider = "registrationData",dataProviderClass = TestData.class,retryAnalyzer = RetryAnalyzer.class )
    public void  contactUsForm(String username,String email, String password) {
        LOGGER.info("===============================================");
        ExtentTest test = ExtentManager.getTest(); // Safely get the test instance
        LOGGER.info("Test Case 6: Contact Us Form Test Case is Started");
        test.info("Starting  Test Case 6: Contact Us Form Test Case is Started");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.verifyHomePage();
        loginPage.contactUsForm(username,email);
        Assert.assertTrue(loginPage.verifyHomePage());

        LOGGER.info("Test Case 6: Contact Us Form Test Case is Finished");
        test.pass("Test Case 6: Contact Us Form Test Case is Finished");
        LOGGER.info("===============================================");
    }


}
