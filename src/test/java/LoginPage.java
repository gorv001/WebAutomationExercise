import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.Logger;





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

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void registerNewUser(String username, String email, String password) {
            LOGGER.info(" Starting registration for:"  + username);
        try {
            waitUntilVisible(signUpLoginButton).click();
            driver.findElement(signUpNameField).sendKeys(username);
            driver.findElement(emailInputField).sendKeys(email);
            driver.findElement(signUpButton).click();
            String formText = waitUntilVisible(formTextElement).getText();
            if (formText.contains("ENTER ACCOUNT INFORMATION")) {
                LOGGER.info("✅ Registration form loaded for: " + username);
            } else {
                LOGGER.info("❌ Registration form did not load.");
                return;
            }
            driver.findElement(titleButton).click();
            driver.findElement(passwordInput).sendKeys(password);
            new Select(driver.findElement(dayDropdown)).selectByVisibleText("14");
            new Select(driver.findElement(monthDropdown)).selectByValue("2");
            new Select(driver.findElement(yearsDropdown)).selectByValue("1996");
            waitUntilVisible(newsletterCheck).click();
            waitUntilVisible(OfferOptinCheck).click();
            waitUntilVisible(first_nameInput).sendKeys(username.split(" ")[0]);
            waitUntilVisible(last_nameInput).sendKeys("Kumar");
            waitUntilVisible(companyInput).sendKeys("One Guardian");
            waitUntilVisible(address1Input).sendKeys("417 Udyog Vihar");
            waitUntilVisible(address2Input).sendKeys("sector 20");
            waitUntilVisible(stateInput).sendKeys("Haryana");
            waitUntilVisible(cityInput).sendKeys("Gurugram");
            waitUntilVisible(zipcodeInput).sendKeys("122008");
            waitUntilVisible(mobile_numberInput).sendKeys("9729222533");
            waitUntilVisible(createAccountButton).click();
            String acountCreatedText = waitUntilVisible(acountCreatedButton).getText();
            if (acountCreatedText.contains("ACCOUNT CREATED!")) {
                LOGGER.info("🎉 Registration details entered successfully for: " + username);
            }else {
                LOGGER.info("Registration failed for: " + username);
            }
            waitUntilVisible(continueButton).click();

        } catch (Exception e) {
            LOGGER.info("❗ Error during registration: " + e.getMessage());
        }
    }
    public WebElement waitUntilVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollToElement(locator);
        return element;
    }
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }


    public void logout() {
        waitUntilVisible(logoutButton).click();
    }

    public void logIn(String email, String password) {
        try{
        LOGGER.info(" Starting putting the loginCredentials:"  + email);
        waitUntilVisible(signUpLoginButton).click();
        waitUntilVisible(loginEmailButton).sendKeys(email);
        waitUntilVisible(loginPasswordButton).sendKeys(password);
        waitUntilVisible(loginSubmitButton).click();
        }catch (Exception e) {
            LOGGER.info("Something went wrong...in login method");
        }

    }
}



