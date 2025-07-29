import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework", "Selenium TestNG");
            extent.setSystemInfo("Tester", "Gourav Kumar");
            extent.setSystemInfo("App Name", "AutomationExercise");
            extent.setSystemInfo("ReportsTo", "Manish Goyal");
        }
        return extent;
    }
}
