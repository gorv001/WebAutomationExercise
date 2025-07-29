import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.lang.reflect.Method;

public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected static ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void setUp(Method method) {
        WebDriverManager.chromedriver().setup();

        // Setup ChromeOptions for CI/CD (headless)
        ChromeOptions options = new ChromeOptions();
        if (System.getenv("CI") != null) {  // Only for CI environments like GitHub Actions
            options.addArguments("--headless=new", "--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.automationexercise.com/");

        test = extent.createTest(method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (test != null) {
            switch (result.getStatus()) {
                case ITestResult.FAILURE:
                    test.fail(result.getThrowable());
                    break;
                case ITestResult.SUCCESS:
                    test.pass("Test passed");
                    break;
                case ITestResult.SKIP:
                    test.skip("Test skipped: " + result.getThrowable());
                    break;
            }
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    }
}
