package com.automationexercise;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // ✅ Get browser from result attribute set in BaseTest
        String browser = (String) result.getAttribute("browser");
        if (browser == null) {
            browser = "chrome"; // default fallback
        }

        // ✅ Create ExtentTest ONLY here - not in BaseTest
        String testName = result.getMethod().getMethodName();
        ExtentManager.startTest(testName + " - " + browser, browser);

        // ✅ Add test info
        ExtentManager.getTest().info("Starting test: " + testName + " on " + browser);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().pass("Test passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = ((BaseTest) testClass).getDriver();

        if (driver != null) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());

            // Log failure and attach screenshot to report
            ExtentManager.getTest().fail("Test Failed: " + result.getThrowable().getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
            );
        } else {
            ExtentManager.getTest().fail("Test Failed: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().skip("Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flushReports();
    }
}