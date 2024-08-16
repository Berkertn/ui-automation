package ui.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;
import java.time.Duration;

import static ui.utils.ExtentManager.getExtentReportInstance;
import static ui.utils.LogUtil.LOG;
import static ui.helpers.selenium.DriverManager.*;

public abstract class BaseTest extends ActionMethods {

    protected static SoftAssert softAssertion;
    @Getter
    @Setter
    private static ExtentReports extent;
    @Getter
    @Setter
    private static ExtentTest test;

    //hooks
    @BeforeSuite
    void beforeSuite() {
        setExtent(getExtentReportInstance());
    }

    @Parameters({"baseURL"})
    @BeforeClass
    void setUp(@Optional String baseURL) {
        System.out.println("Before class " + getClass().getSimpleName());
        if (baseURL != null && !baseURL.isEmpty()) {
            LOG.info("Base URL is set to: {}", baseURL);
            setBaseUrlTL(baseURL);
        }
    }

    @Parameters({"browser", "headless"})
    @BeforeMethod
    void beforeMethod(@Optional("firefox") String browser, @Optional("true") String headless, Method method) {
        setDriverTL(browser, headless);
        setTest(getExtent().createTest(getClass().getSimpleName() + " - " + method.getName() + "-" + browser));
        getDriverTL().get(getBaseUrlTL());
        driver = getDriverTL();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        softAssertion = new SoftAssert();
    }

    @AfterMethod
    void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail("Test Failed: " + result.getThrowable());

            String pathAfterDir = takeScreenshot(result.getMethod().getMethodName(), result.getTestClass().getName());
            String path = ".." + "/screenshots/" + pathAfterDir;
            test.fail("Test Failed - Screenshot: " + result.getMethod().getMethodName())
                    .addScreenCaptureFromPath(path);
            LOG.debug("Taking screenshot for failed test: {}", result.getMethod().getMethodName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test Skipped: " + result.getThrowable());
        }
    }

    @AfterClass
    void tearDown() {
        System.out.println("After class");
        quitDriver();
    }

    @AfterSuite
    void afterSuite() {
        if (extent == null) {
            LOG.error("Extent report is null");
        } else {
            extent.flush();
        }
    }
}