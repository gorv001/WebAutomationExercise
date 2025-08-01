package com.automationexercise;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class HomePage extends LoginPage {
    private static final Logger LOGGER = (Logger) LogManager.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;
    private final By homeButton = By.xpath("//a[@style='color: orange;']");
    private final By contactUsButton = By.xpath("//a[@href='/contact_us']");
    private final By getInTouchText = By.xpath("//div[@class='col-sm-12']/h2");
    private final By contactUsNameInput = By.xpath("//input[@placeholder='Name']");
    private final By contactUsEmailInput = By.xpath("//input[@name='email']");
    private final By contactUsSubjectInput = By.xpath("//input[@name='subject']");
    private final By contactUsMessageInput = By.id("message");
    private final By contactUsUploadFile = By.xpath("//input[@name='upload_file']");
    private final By contactUsSubmit = By.xpath("//input[@name='submit']");
    private final By contactUsSubmitSuccessMessage = By.xpath("//div[@class='status alert alert-success']");
    private final By contactUsHomeButton = By.xpath("//span[contains(text(),'Home')]");
    private final By testCaseButton = By.xpath("//button[contains(text(),'Test Cases')]");
    private final By testCaseText = By.xpath("//h2/b[contains(text(),'Test Cases')]");
    private final By productsButton = By.xpath("//a[@href='/products']");
    private final By productsList = By.xpath("//div[@class='col-sm-4']");
    private final By viewProductDetailButton = By.xpath("//a[@href='/product_details/1']");
    private final By productInformation = By.xpath("//div[@class='product-information']");
    private final By productName = By.xpath("//div[@class='product-information']/h2");
    private final By productCategory = By.xpath("(//div[@class='product-information']/p)[1]");
    private final By productPrice = By.xpath("//div[@class='product-information']/span/span");

    public HomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public boolean verifyHomePage() {
        try {
            LOGGER.info("🔍 Starting verification of the Home Page...");

            waitUntilVisible(homeButton); // Explicit wait for visibility
            WebElement homePage = driver.findElement(homeButton);

            Assert.assertTrue(homePage.isDisplayed(), "❌ Home page is not visible!");
            LOGGER.info("✅ Home page is visible successfully.");
            ExtentManager.getTest().pass("✅ Home page is visible successfully.");

            return true;

        } catch (Exception e) {
            LOGGER.error("❌ Exception occurred while verifying home page: " + e.getMessage(), e);
            ExtentManager.getTest().fail("❌ Exception while verifying Home Page: " + e.getMessage());
            return false;
        }
    }


    public void contactUsForm(String name, String email) {
        try {
            LOGGER.info("🔁 Starting Contact Us form submission...");

            // Navigate to Contact Us page
            waitUntilVisible(contactUsButton).click();
            verifyContactUsPage();

            // Fill the form fields
            LOGGER.info("📝 Filling in contact form details...");
            waitUntilVisible(contactUsNameInput).sendKeys(name);
            waitUntilVisible(contactUsEmailInput).sendKeys(email);
            waitUntilVisible(contactUsSubjectInput).sendKeys("For Testing purposes");
            waitUntilVisible(contactUsMessageInput).sendKeys("This field is for automation test script");

            // Upload a file
            File file = null;
            try {
                file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("test.png")).getFile());
                waitUntilVisible(contactUsUploadFile).sendKeys(file.getAbsolutePath());
                LOGGER.info("📎 File uploaded successfully: " + file.getName());
            } catch (NullPointerException npe) {
                LOGGER.error("❌ Upload file not found in resources!");
                ExtentManager.getTest().fail("❌ Upload file missing: test.png");
                return;
            }

            // Submit the form
            waitUntilVisible(contactUsSubmit).click();
            LOGGER.info("📤 Submitted the form, handling alert...");

            // Accept the alert
            Alert alert = driver.switchTo().alert();
            alert.accept();
            LOGGER.info("✅ Alert accepted.");

            // Check for success message
            if (waitUntilVisible(contactUsSubmitSuccessMessage).isDisplayed()) {
                LOGGER.info("🎉 Contact form submitted successfully!");
                ExtentManager.getTest().pass("🎉 Contact form submitted successfully!");
            } else {
                LOGGER.warn("⚠️ Submission success message not visible.");
                ExtentManager.getTest().fail("⚠️ Submission success message not visible.");
            }

            // Return to home page
            waitUntilVisible(contactUsHomeButton).click();
            verifyHomePage();

        } catch (Exception e) {
            LOGGER.error("❌ Exception in contactUsForm(): " + e.getMessage(), e);
            ExtentManager.getTest().fail("❌ Contact form submission failed: " + e.getMessage());
            throw new RuntimeException("Contact Us form submission failed", e);
        }
    }


    private void verifyContactUsPage() {
        WebElement getInTouchTextButton = driver.findElement(getInTouchText);
        if(getInTouchTextButton.isDisplayed()){
            LOGGER.info(" Contact us page is visible");
        }else {
            LOGGER.info(" Contact us page is not visible");
        }
    }

    public boolean verifyTestCasePage() {
        try {
            LOGGER.info(" Starting verifying test case page");
            waitUntilVisible(testCaseButton).click();
            WebElement testCaseTextButton = driver.findElement(testCaseText);
            if(testCaseTextButton.isDisplayed()){
                LOGGER.info(" TestCase page is visible");
                return  true;
            }else  {
                LOGGER.error(" TestCase page is not visible");
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void navigatedToAllProducts() {
        try {
            LOGGER.info("🧭 Navigating to All Products page...");

            // Click on the "Products" button after ensuring it's visible
            WebElement productsBtn = waitUntilVisible(productsButton);
            productsBtn.click();

            // Small wait to allow page load if needed (optional: use explicit wait for better handling)
            Thread.sleep(1000);

            String currentUrl = driver.getCurrentUrl();
            LOGGER.info("🔗 Current URL after navigation: " + currentUrl);

            // Assert page contains the expected URL segment
            Assert.assertTrue(currentUrl.contains("products"), "❌ Navigation to All Products page failed. URL: " + currentUrl);
            LOGGER.info("✅ Successfully navigated to All Products page.");

        } catch (NoSuchElementException e) {
            LOGGER.error("❌ Products button not found on the page.", e);
            Assert.fail("Products button not present: " + e.getMessage());

        } catch (AssertionError ae) {
            LOGGER.error("❌ URL validation failed after clicking Products button.", ae);
            throw ae;

        } catch (Exception e) {
            LOGGER.error("❌ Unexpected error during navigation to All Products.", e);
            throw new RuntimeException("Unexpected error while navigating to All Products page.", e);
        }
    }


    public boolean verifyProductList() {
        try {
            LOGGER.info("🔍 Verifying product list on the page...");

            List<WebElement> products = driver.findElements(productsList);

            if (products == null || products.isEmpty()) {
                LOGGER.error("❌ No products found on the page.");
                Assert.fail("Product list is empty or not present.");
                return false;
            }

            LOGGER.info("🛍️ Number of products found: " + products.size());

            for (int i = 0; i < products.size(); i++) {
                WebElement productItem = products.get(i);
                Assert.assertTrue(productItem.isDisplayed(), "❌ Product item at index " + i + " is not visible. Text: " + productItem.getText());
            }

            LOGGER.info("✅ All products in the list are visible.");
            return true;

        } catch (NoSuchElementException e) {
            LOGGER.error("❌ Element locator for product list failed.", e);
            Assert.fail("Locator for product list is incorrect or element missing: " + e.getMessage());
            return false;

        } catch (AssertionError ae) {
            LOGGER.error("❌ Assertion failed during product list verification.", ae);
            throw ae; // let test fail with proper assertion message

        } catch (Exception e) {
            LOGGER.error("❌ Unexpected exception during product list verification.", e);
            throw new RuntimeException("Unexpected error while verifying product list.", e);
        }
    }


    public boolean viewProductDetails() {
        try {
            LOGGER.info("🔍 Attempting to view product details...");

            // Wait for and click the 'View Product' button
            WebElement viewButton = waitUntilVisible(viewProductDetailButton);
            viewButton.click();
            LOGGER.info("🖱️ Clicked on 'View Product' button");

            // Wait for product detail section to appear
            WebElement productInfo = waitUntilVisible(productInformation);
            Assert.assertTrue(productInfo.isDisplayed(), "❌ Product detail page is not displayed");

            LOGGER.info("✅ Product detail page is successfully displayed.");
            return true;

        } catch (TimeoutException e) {
            LOGGER.error("❌ Timeout while waiting for product detail elements.", e);
            Assert.fail("Product detail elements not visible in expected time: " + e.getMessage());
            return false;

        } catch (NoSuchElementException e) {
            LOGGER.error("❌ Element not found during viewProductDetails.", e);
            Assert.fail("Required product detail element is missing: " + e.getMessage());
            return false;

        } catch (Exception e) {
            LOGGER.error("❌ Unexpected error in viewProductDetails()", e);
            throw new RuntimeException("Error while verifying product detail view", e);
        }
    }


    public boolean productDetailsVerified() {
        try {
            WebElement productNameElement = waitUntilVisible(productName);
            WebElement productPriceElement = waitUntilVisible(productPrice);
            WebElement productCategoryElement = waitUntilVisible(productCategory);

            String productName = productNameElement.getText().trim();
            String productPrice = productPriceElement.getText().trim();
            String productCategory = productCategoryElement.getText().trim();

            // Validate Product Name
            if (productName.isEmpty()) {
                LOGGER.error("❌ Product name is missing.");
                Assert.fail("Product name is empty.");
            }

            // Validate Product Price
            if (productPrice.isEmpty()) {
                LOGGER.error("❌ Product price is missing.");
                Assert.fail("Product price is empty.");
            }

            // Validate Product Category
            if (productCategory.isEmpty()) {
                LOGGER.error("❌ Product category is missing.");
                Assert.fail("Product category is empty.");
            }

            LOGGER.info("✅ Product Details Verified Successfully:");
            LOGGER.info("🛒 Name: " + productName + " | 💰 Price: " + productPrice + " | 📂 Category: " + productCategory);
            return true;

        } catch (NoSuchElementException e) {
            LOGGER.error("❌ One or more product detail elements not found.", e);
            Assert.fail("Product details elements missing: " + e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.error("❌ Unexpected error while verifying product details.", e);
            throw new RuntimeException("Product detail verification failed", e);
        }
    }

}
