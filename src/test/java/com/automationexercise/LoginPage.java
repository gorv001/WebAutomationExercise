package com.automationexercise;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.time.Duration;

public class LoginPage {

    private static final Logger LOGGER = (Logger) LogManager.getLogger(LoginPage.class);
    private final WebDriver driver;
    private WebDriverWait wait;
    private final By signUpLoginButton = By.xpath("//a[@href='/login']");
    private final By signUpNameField = By.xpath("//input[@data-qa='signup-name']");
    private final By emailInputField = By.xpath("//input[@data-qa='signup-email']");
    private final By signUpButton = By.xpath("//button[text()='Signup']");
    private final By formTextElement = By.xpath("(//h2[@class=\"title text-center\"])[1]");
    private final By titleButton = By.id("id_gender1");
    private final By passwordInput = By.id("password");
    private final By dayDropdown = By.id("days");
    private final By monthDropdown = By.id("months");
    private final By yearsDropdown = By.id("years");
    private final By newsletterCheck = By.id("newsletter");
    private final By OfferOptinCheck = By.id("optin");
    private final By first_nameInput = By.id("first_name");
    private final By last_nameInput = By.id("last_name");
    private final By companyInput = By.id("company");
    private final By address1Input = By.id("address1");
    private final By address2Input = By.id("address2");
    private final By countryDropdown = By.id("country");
    private final By stateInput = By.id("state");
    private final By cityInput = By.id("city");
    private final By zipcodeInput = By.id("zipcode");
    private final By mobile_numberInput = By.id("mobile_number");
    private final By createAccountButton = By.xpath("//button[text()='Create Account']");
    private final By acountCreatedButton = By.xpath("//h2[@class='title text-center']");
    private final By continueButton = By.xpath("//a[@data-qa='continue-button']");
    private final By loginEmailButton = By.name("email");
    private final By loginPasswordButton = By.name("password");
    private final By loginSubmitButton = By.xpath("//button[text()='Login']");
    private final By logoutButton = By.xpath("//a[@href='/logout']");
    private final By newUserSignUpText = By.xpath("//h2[text()='New User Signup!']");
    private final By acountHolderName = By.xpath("//i[@class='fa fa-user']/../b");
    private final By deleteAcountButton = By.xpath("//a[contains(text(),' Delete Account')]");
    private final By deleteAcountText = By.xpath("//h2/b[contains(text(),'Account Deleted!')]");
    private final By deleteContinueButton = By.xpath("//a[@data-qa='continue-button']");
    private final By verifyLoginToYourAcountText = By.xpath("//div[@class='login-form']/h2");



    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void registerNewUser(String username, String email, String password) {
        LOGGER.info("🚀 Starting registration for: {}", username);

        try {
            waitUntilVisible(signUpLoginButton).click();
            verifyNewUserSignUpPage();

            waitUntilVisible(signUpNameField).sendKeys(username);
            waitUntilVisible(emailInputField).sendKeys(email);
            waitUntilVisible(signUpButton).click();

            String formText = waitUntilVisible(formTextElement).getText().trim();
            if (formText.contains("ENTER ACCOUNT INFORMATION")) {
                LOGGER.info("✅ 'ENTER ACCOUNT INFORMATION' is visible");
            } else {
                LOGGER.error("❌ 'ENTER ACCOUNT INFORMATION' text not found. Halting registration.");
                return;
            }

            // Fill basic info
            waitUntilVisible(titleButton).click();
            waitUntilVisible(passwordInput).sendKeys(password);
            new Select(waitUntilVisible(dayDropdown)).selectByVisibleText("14");
            new Select(waitUntilVisible(monthDropdown)).selectByValue("2");
            new Select(waitUntilVisible(yearsDropdown)).selectByValue("1996");

            // Optional checkboxes
            waitUntilVisible(newsletterCheck).click();
            waitUntilVisible(OfferOptinCheck).click();

            // Address and contact info
            waitUntilVisible(first_nameInput).sendKeys(username.split(" ")[0]);
            waitUntilVisible(last_nameInput).sendKeys("Kumar");
            waitUntilVisible(companyInput).sendKeys("One Guardian");
            waitUntilVisible(address1Input).sendKeys("417 Udyog Vihar");
            waitUntilVisible(address2Input).sendKeys("Sector 20");
            waitUntilVisible(stateInput).sendKeys("Haryana");
            waitUntilVisible(cityInput).sendKeys("Gurugram");
            waitUntilVisible(zipcodeInput).sendKeys("122008");
            waitUntilVisible(mobile_numberInput).sendKeys("9729222533");

            waitUntilVisible(createAccountButton).click();

            String accountCreatedText = waitUntilVisible(acountCreatedButton).getText().trim();
            if (accountCreatedText.contains("ACCOUNT CREATED!")) {
                LOGGER.info("🎉 ACCOUNT CREATED successfully for: {}", username);
            } else {
                LOGGER.error("❌ Account creation failed for: {}", username);
                return;
            }

            waitUntilVisible(continueButton).click();

            // Post-registration verification and cleanup
            verifyUserLogedIn();
            deleteAcount();
            verifyAcountDeletedText();
            waitUntilVisible(deleteContinueButton).click();

        } catch (TimeoutException e) {
            LOGGER.error("⏳ Timeout occurred during registration: {}", e.getMessage());
            throw new RuntimeException("Timeout during user registration", e);
        } catch (Exception e) {
            LOGGER.error("❗ Unexpected error during registration for {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Unexpected error during registration", e);
        }
    }


    private void verifyUserLogedIn() {
        try {
            LOGGER.info("🔍 Verifying if user is logged in...");
            WebElement accountHolderNameElement = waitUntilVisible(acountHolderName);

            if (accountHolderNameElement.isDisplayed()) {
                String name = accountHolderNameElement.getText().trim();
                LOGGER.info("✅ Logged in as: " + name);
            } else {
                LOGGER.error("❌ Account holder name element is not visible.");
                throw new AssertionError("Account holder name is not displayed.");
            }

        } catch (TimeoutException e) {
            LOGGER.error("❌ Timeout while waiting for account holder name to appear", e);
            throw new RuntimeException("Timeout while verifying user login", e);
        } catch (Exception e) {
            LOGGER.error("❌ Unexpected error while verifying login", e);
            throw new RuntimeException("Unexpected error while verifying login", e);
        }
    }


    private void verifyAcountDeletedText() {
        try {
            LOGGER.info("🔍 Verifying 'Account Deleted' confirmation message...");
            WebElement deletedText = waitUntilVisible(deleteAcountText);

            if (deletedText.isDisplayed()) {
                LOGGER.info("✅ 'Account Deleted' text is visible.");
            } else {
                LOGGER.error("❌ 'Account Deleted' text is not visible.");
                throw new AssertionError("'Account Deleted' text not displayed");
            }

        } catch (TimeoutException e) {
            LOGGER.error("❌ Timeout waiting for 'Account Deleted' text", e);
            throw new RuntimeException("Timeout while verifying 'Account Deleted' text", e);
        } catch (Exception e) {
            LOGGER.error("❌ Unexpected error during account deletion confirmation", e);
            throw new RuntimeException("Unexpected error while verifying 'Account Deleted' text", e);
        }
    }


    private void deleteAcount() {
        try {
            LOGGER.info("🔍 Attempting to delete account...");
            WebElement deleteButton = waitUntilVisible(deleteAcountButton);

            if (deleteButton.isDisplayed()) {
                deleteButton.click();
                LOGGER.info("✅ Delete account button clicked successfully.");
            } else {
                LOGGER.error("❌ Delete account button is not visible.");
                throw new AssertionError("Delete account button not visible");
            }

        } catch (TimeoutException e) {
            LOGGER.error("❌ Timeout while waiting for Delete account button", e);
            throw new RuntimeException("Delete account button not visible due to timeout", e);
        } catch (Exception e) {
            LOGGER.error("❌ Unexpected error occurred while attempting to delete account", e);
            throw new RuntimeException("Unexpected error during account deletion", e);
        }
    }


    private void verifyNewUserSignUpPage() {
        try {
            LOGGER.info("🔍 Verifying if 'New User Signup' section is visible...");
            WebElement newUserSignUpTextElement = waitUntilVisible(newUserSignUpText);

            if (newUserSignUpTextElement.isDisplayed()) {
                LOGGER.info("✅ 'New User Signup' section is visible. You're on the registration page.");
            } else {
                LOGGER.error("❌ 'New User Signup' section is not displayed.");
                throw new AssertionError("New User Signup text is not visible");
            }
        } catch (TimeoutException e) {
            LOGGER.error("❌ Timeout while waiting for 'New User Signup' section to be visible", e);
            throw new RuntimeException("New User Signup section not visible due to timeout", e);
        } catch (Exception e) {
            LOGGER.error("❌ Unexpected error while verifying 'New User Signup' section", e);
            throw new RuntimeException("Error verifying New User Signup page", e);
        }
    }



    public WebElement waitUntilVisible(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            scrollToElement(locator);
            LOGGER.info("✅ Element is visible: {}", locator);
            return element;
        } catch (TimeoutException e) {
            LOGGER.error("❌ Timeout waiting for visibility of element: {}", locator);
            throw new RuntimeException("Element not visible: " + locator, e);
        } catch (Exception e) {
            LOGGER.error("❌ Unexpected error while waiting for element: {}", locator, e);
            throw new RuntimeException("Error during waitUntilVisible for: " + locator, e);
        }
    }

    public void registerNewUser(String username, String email) {
        try {
            LOGGER.info("🔐 Starting user registration with username: {} and email: {}", username, email);

            // Click the Sign Up/Login button
            WebElement signUpLoginBtn = waitUntilVisible(signUpLoginButton);
            Assert.assertTrue(signUpLoginBtn.isDisplayed(), "❌ SignUp/Login button not displayed");
            signUpLoginBtn.click();

            // Verify New User Sign Up section is visible
            verifyNewUserSignUpPage();

            // Enter user details
            WebElement nameField = waitUntilVisible(signUpNameField);
            WebElement emailField = waitUntilVisible(emailInputField);
            Assert.assertTrue(nameField.isDisplayed() && emailField.isDisplayed(), "❌ Name or Email field not displayed");

            nameField.sendKeys(username);
            emailField.sendKeys(email);
            LOGGER.info("✅ Entered name and email for sign-up");

            // Click Sign Up button
            WebElement signUpBtn = waitUntilVisible(signUpButton);
            Assert.assertTrue(signUpBtn.isDisplayed(), "❌ Sign Up button not displayed");
            signUpBtn.click();
            LOGGER.info("✅ Clicked on Sign Up button");

        } catch (TimeoutException e) {
            LOGGER.error("⏳ Timeout while registering new user: {}", e.getMessage());
            throw new RuntimeException("Timeout occurred during user registration", e);

        } catch (Exception e) {
            LOGGER.error("❌ Exception occurred while registering new user: {}", e.getMessage(), e);
            throw new RuntimeException("Error during new user registration", e);
        }
    }


    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }


    public void logout() {
        waitUntilVisible(logoutButton).click();
    }

    public void logIn(String email, String password) {
        try {
            LOGGER.info("🔐 Starting login process for user: " + email);

            // Verify home page
            HomePage homePage = new HomePage(driver);
            if (!homePage.verifyHomePage()) {
                LOGGER.error("❌ Home page not loaded. Aborting login.");
                ExtentManager.getTest().fail("❌ Home page not loaded. Cannot proceed with login.");
                return;
            }

            // Click on Sign Up / Login
            waitUntilVisible(signUpLoginButton).click();
            LOGGER.info("➡️ Clicked on Sign Up / Login button");

            // Verify login section is visible
            verifyLoginToYourAcountText();

            // Enter login credentials
            LOGGER.info("📝 Entering email and password...");
            waitUntilVisible(loginEmailButton).sendKeys(email);
            waitUntilVisible(loginPasswordButton).sendKeys(password);

            // Submit login
            waitUntilVisible(loginSubmitButton).click();
            LOGGER.info("✅ Submitted login form");


        } catch (Exception e) {
            LOGGER.error("❌ Exception occurred in logIn(): " + e.getMessage(), e);
            ExtentManager.getTest().fail("❌ Login failed: " + e.getMessage());
            throw new RuntimeException("Login failed", e);
        }
    }

    boolean isUserLoggedInSuccessfully() {
        try {
            LOGGER.info("🔎 Verifying if user is logged in successfully...");

            // Replace 'logoutButton' with the actual post-login element locator
            WebElement logoutElement = waitUntilVisible(logoutButton);  // timeout = 10 seconds

            if (logoutElement.isDisplayed()) {
                LOGGER.info("✅ User is logged in. Logout button is visible.");
                return true;
            } else {
                LOGGER.warn("⚠️ Logout button not displayed. Login may have failed.");
                return false;
            }

        } catch (Exception e) {
            LOGGER.error("❌ Login verification failed due to: " + e.getMessage());
            return false;
        }
    }



    private void verifyLoginToYourAcountText() {
        try {
            LOGGER.info("🔎 Verifying 'Login to your account' text visibility...");

            WebElement loginTextElement = waitUntilVisible(verifyLoginToYourAcountText); // timeout = 10 seconds

            if (loginTextElement != null && loginTextElement.isDisplayed()) {
                LOGGER.info("✅ 'Login to your account' text is visible.");
            } else {
                LOGGER.warn("⚠️ 'Login to your account' text is not visible or not displayed.");
                throw new AssertionError("'Login to your account' text is not visible.");
            }

        } catch (TimeoutException e) {
            LOGGER.error("❌ Timed out waiting for 'Login to your account' text to appear.", e);
            throw new RuntimeException("Login text not visible in expected time.");

        } catch (Exception e) {
            LOGGER.error("❌ Exception occurred while verifying login text: " + e.getMessage(), e);
            throw new RuntimeException("Failed to verify login text.", e);
        }
    }



}



