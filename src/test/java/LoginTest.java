
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;


public class LoginTest extends BaseTest {


    private static final Logger LOGGER = LogManager.getLogger(LoginTest.class);
    private final By signUpLoginButton = By.xpath("//a[@href='/login']");



    @Test(enabled = true, dataProvider = "registrationData",dataProviderClass = TestData.class)
    public void testRegistration(String username,String email, String password) {
        LOGGER.info("TestCase 01: First Test Case is Started");
        test.info("Starting Registration test");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.registerNewUser(username, email,password);
        By logoutButton = By.xpath("//a[@href='/logout']");
        WebElement logoutButtonElement = driver.findElement(logoutButton);
        Assert.assertTrue(logoutButtonElement.isDisplayed());
        LOGGER.info("TestCase 01: First Test Case is Finished");
        test.pass("Registration was successful");
    }

    @Test(enabled = true, dataProvider = "registrationData",dataProviderClass = TestData.class )
    public void testLogIn(String username,String email, String password) {
        LOGGER.info("TestCase 02: LogoIn Test Case is Started");
        test.info("Starting LogIn test");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.logIn("user996@test.com", "password123");
        By logoutButton = By.xpath("//a[@href='/logout']");
        WebElement logoutButtonElement = driver.findElement(logoutButton);
        Assert.assertTrue(logoutButtonElement.isDisplayed());
        LOGGER.info("TestCase 02: LogIn Test Case is Finished");
        test.pass("LogIn was successful");
    }


    @Test(enabled = true,dataProvider = "registrationData",dataProviderClass = TestData.class )
    public void testLogInWithInCorrectCredentials(String username,String email, String password) {
        LOGGER.info("TestCase 03: LogoIn with incorrect credentials Test Case is Started");
        test.info("Starting LogInWithInCorrectCredentials test");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.logIn("user996@test.com", "123111");
        By errorMessage = By.xpath("//p[contains(text(),'password is incorrect')]");
        WebElement errorMessageElement = driver.findElement(errorMessage);
        Assert.assertTrue(errorMessageElement.isDisplayed());
        LOGGER.info("TestCase 03: LogoIn with incorrect credentials Test Case is Passed");
        test.pass("LogInWithInCorrectCredentials was successful");
    }


    @Test(enabled = true,dataProvider = "registrationData",dataProviderClass = TestData.class )
    public void testLogOut(String username,String email, String password) {
        LOGGER.info("TestCase 04: Logout Test Case is Started");
        test.info("Starting LogOut test");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.logIn("user996@test.com", "password123");
        loginPage.logout();
        WebElement signUpButton = driver.findElement(signUpLoginButton);
        Assert.assertTrue(signUpButton.isDisplayed());
        LOGGER.info("TestCase 04: Logout Test Case is Finished");
        test.pass("LogOut was successful");
    }


}
