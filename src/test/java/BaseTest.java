import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.lang.reflect.Method;
import java.io.IOException;

public class BaseTest {
    protected WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeSuite
    public void setupReport() {
        ExtentManager.initReports();
    }

    @BeforeMethod
    @Parameters("browser")
    public void setUp(String browser, Method method,ITestResult result) {
        System.out.println("Running " + method.getName() + " on " + browser +
                " | Thread: " + Thread.currentThread().getId());

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if (System.getenv("CI") != null) {
                options.addArguments("--headless=new", "--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage");
            }
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (System.getenv("CI") != null) {
                options.addArguments("--headless");
            }
            driver = new FirefoxDriver(options);
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            driver = new EdgeDriver(options);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.get("https://www.automationexercise.com/");
        result.setAttribute("browser", browser);

        // Start Extent test here
        ExtentManager.startTest(method.getName(),browser);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        ExtentTest test = ExtentManager.getTest(); // Get test after startTest() is called

        if (test != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());

                test.fail("Test Failed: " + result.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.pass("Test passed");
            } else if (result.getStatus() == ITestResult.SKIP) {
                test.skip("Test skipped: " + result.getThrowable());
            }
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void tearDownReport() {
        ExtentManager.flushReports();
    }
}
