import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = ((BaseTest) testClass).getDriver();
        String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());

        // Log failure and attach screenshot to report
        ExtentManager.getTest().fail("Test Failed",
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
        );
    }

    @Override
    public void onTestStart(ITestResult result) {
        String browser = (String) result.getAttribute("browser"); // ✅ Read browser from result
        if (browser == null) {
            browser = "unknown"; // fallback
        }
        ExtentManager.startTest(result.getMethod().getMethodName() + " - " + browser, browser);
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flushReports();
    }
}
