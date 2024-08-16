package ui.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getExtentReportInstance() {
        if (extent == null) {
            createExtentReportInstance(String.format("test-output/reports/UI-Automation-Test-Report-%s.html", LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"))));
        }
        return extent;
    }

    public static ExtentReports createExtentReportInstance(String fileName) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Automation Test Run Result");
        sparkReporter.config().setReportName("UI Test Report");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Tester", "Berk");
        extent.setSystemInfo("Environment", "UAT");

        return extent;
    }
}
