package com.automationexercise;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static void initReports() throws IOException {
        if (extent == null) {
            // Timestamp for unique report name
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.loadXMLConfig("src/test/resources/extent-config.xml"); // Load theme and display settings

            spark.config().setDocumentTitle("Automation Test Report");
            spark.config().setReportName("Web Automation Results");
            spark.config().setTheme(Theme.DARK); // ⭐ If you changed theme
            spark.config().setTimelineEnabled(true); // ⭐ Adds interactive timeline
            spark.config().setEncoding("utf-8");
            extent = new ExtentReports();
            extent.attachReporter(spark);

            // Add system/environment details
            extent.setSystemInfo("Framework", "Selenium + TestNG");
            extent.setSystemInfo("Tester", "Gourav Kumar");
            extent.setSystemInfo("Project", "AutomationExercise");
            extent.setSystemInfo("Reports To", "Manish Goyal");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Executed By", System.getProperty("user.name"));
        }
    }

    public static void startTest(String testName, String browser) {
        ExtentTest extentTest = extent.createTest(testName)
                .assignCategory(browser)
                .assignCategory("Smoke Test")  // Add test category
                .assignAuthor("Gourav Kumar");
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            if (System.getenv("CI") == null) { // Only open locally, not in CI
                try {
                    Desktop.getDesktop().browse(new File("test-output/ExtentReport.html").toURI());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
