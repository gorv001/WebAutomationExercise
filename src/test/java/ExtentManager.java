import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

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
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.loadXMLConfig("src/test/resources/extent-config.xml"); // Load theme and display settings

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
                .assignDevice(browser);
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
