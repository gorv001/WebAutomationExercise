import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.Logger;


import java.io.File;
import java.time.Duration;
import java.util.Objects;

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
    private final By homeButton = By.xpath("//a[@style='color: orange;']");
    private final By newUserSignUpText = By.xpath("//h2[text()='New User Signup!']");
    private final By acountHolderName = By.xpath("//i[@class='fa fa-user']/../b");
    private final By deleteAcountButton = By.xpath("//a[contains(text(),' Delete Account')]");
    private final By deleteAcountText = By.xpath("//h2/b[contains(text(),'Account Deleted!')]");
    private final By deleteContinueButton = By.xpath("//a[@data-qa='continue-button']");
    private final By verifyLoginToYourAcountText = By.xpath("//div[@class='login-form']/h2");
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


    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void registerNewUser(String username, String email, String password) {
            LOGGER.info(" Starting registration for:"  + username);
        try {
            waitUntilVisible(signUpLoginButton).click();
            verifyNewUserSignUpPage();
            driver.findElement(signUpNameField).sendKeys(username);
            driver.findElement(emailInputField).sendKeys(email);
            driver.findElement(signUpButton).click();
            String formText = waitUntilVisible(formTextElement).getText();
            if (formText.contains("ENTER ACCOUNT INFORMATION")) {
                LOGGER.info("ENTER ACCOUNT INFORMATION is visible");
            } else {
                LOGGER.info("❌ ENTER ACCOUNT INFORMATION is visible");
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
                LOGGER.info("🎉 ACCOUNT IS CREATED! for: " + username);
            }else {
                LOGGER.info("❌ Acount creation failed for: " + username);
            }
            waitUntilVisible(continueButton).click();
           verifyUserLogedIn();
           deleteAcount();
           verifyAcountDeletedText();
           waitUntilVisible(deleteContinueButton).click();
        } catch (Exception e) {
            LOGGER.info("❗ Error during registration: " + e.getMessage());
        }
    }

    private void verifyUserLogedIn() {
        try {
            WebElement AccountHolderName= waitUntilVisible(acountHolderName);
            if(AccountHolderName.isDisplayed()){
                LOGGER.info(" Logged in as "+AccountHolderName.getText());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyAcountDeletedText() {
        try {
            if(driver.findElement(deleteAcountText).isDisplayed()){
                LOGGER.info("Account deleted text is visible");
            }else {
                LOGGER.info("Account deleted text is not visible");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteAcount() {
        try {
            WebElement deletebutton = driver.findElement(deleteAcountButton);
            if (deletebutton.isDisplayed()) {
                driver.findElement(deleteAcountButton).click();
            } else {
                LOGGER.info("Delete account button is not visible");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void verifyNewUserSignUpPage() {
        try {
            WebElement newUserSignUpButton = driver.findElement(newUserSignUpText);
            if(newUserSignUpButton.isDisplayed()){
                LOGGER.info("You are on registration page");
            }else{
                LOGGER.info("❌ You are not on registration page");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public WebElement waitUntilVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollToElement(locator);
        return element;
    }
    public void registerNewUser(String username, String email) {
        try {
            waitUntilVisible(signUpLoginButton).click();
            verifyNewUserSignUpPage();
            driver.findElement(signUpNameField).sendKeys(username);
            driver.findElement(emailInputField).sendKeys(email);
            driver.findElement(signUpButton).click();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        try{
        LOGGER.info(" Starting putting the loginCredentials:"  + email);
        verifyHomePage();
        waitUntilVisible(signUpLoginButton).click();
        verifyLoginToYourAcountText();
        waitUntilVisible(loginEmailButton).sendKeys(email);
        waitUntilVisible(loginPasswordButton).sendKeys(password);
        waitUntilVisible(loginSubmitButton).click();


        }catch (Exception e) {
            LOGGER.info("❌ Something went wrong...in login method");
        }

    }

    private void verifyLoginToYourAcountText() {
        try {
        WebElement verifylogintoyourAcountText = waitUntilVisible(verifyLoginToYourAcountText);
        if (verifylogintoyourAcountText.isDisplayed()) {
        LOGGER.info(" Login to your acount text is visible");
        }else {
            LOGGER.info("Login to your acount text is not visible");
        }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifyHomePage() {
        try {
            WebElement homePage = driver.findElement(homeButton);
            if (homePage.isDisplayed()) {
                LOGGER.info(" Home page is visible successfully");
                return true;

            } else {
                LOGGER.info("❌ Home page is not visible");
                return false;
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public void contactUsForm(String name,String email) {
        try {
            waitUntilVisible(contactUsButton).click();
            verifyContactUsPage();
            waitUntilVisible(contactUsNameInput).sendKeys(name);
            waitUntilVisible(contactUsEmailInput).sendKeys(email);
            waitUntilVisible(contactUsSubjectInput).sendKeys("For Testing purposes");
            waitUntilVisible(contactUsMessageInput).sendKeys("This field is for automation test script");
            File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("test.png")).getFile());
            waitUntilVisible(contactUsUploadFile).sendKeys(file.getAbsolutePath());
            waitUntilVisible(contactUsSubmit).click();
            Alert alert = driver.switchTo().alert();
            alert.accept();
           if(waitUntilVisible(contactUsSubmitSuccessMessage).isDisplayed()){
               LOGGER.info(" Contact us form is submitted successfully");
           }else{
               LOGGER.info(" Contact us form is not submited");
           }
           waitUntilVisible(contactUsHomeButton).click();
           verifyHomePage();

        } catch (Exception e) {
            throw new RuntimeException(e);
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
}



