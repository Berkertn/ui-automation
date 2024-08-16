package ui.helpers.selenium;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.testng.Assert;
import ui.helpers.yaml.ConfigReader;

import static ui.utils.LogUtil.LOG;

import java.time.Duration;
import java.util.Optional;

public class DriverManager {

    private static ThreadLocal<WebDriver> driverTL = new ThreadLocal<>();

    public static ThreadLocal<String> baseUrlTL = new ThreadLocal<>();
    @Getter
    @Setter
    private static int implicitlyWaitSeconds = 10;
    @Getter
    @Setter
    private static boolean headless;

    public static void setDriverTL(String testBrowser, String headless) {
        setHeadless(Boolean.parseBoolean(headless));
        setDriverTL(testBrowser);
    }

    public static void setDriverTL(String testBrowser) {
        Optional<WebDriver> driverOpt = Optional.ofNullable(getDriverTL());
        if (driverOpt.isPresent()) {
            LOG.error("Driver is already set, can not set again!");
            return;
        }
        switch (testBrowser.toLowerCase()) {
            case "chrome": {
                WebDriverManager.chromedriver().setup();
                LOG.info("Chrome driver set up started");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                if (headless) {
                    chromeOptions.addArguments("--headless");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                }
                driverSet(chromeOptions);
                LOG.info("Chrome set up completed successfully");
                break;
            }
            case "firefox": {
                LOG.info("Firefox driver set up started");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                    LOG.info("Firefox set as headless");
                }
                driverSet(firefoxOptions);
                driverTL.get().manage().window().fullscreen();
                LOG.info("Firefox set up completed successfully");
                break;
            }
            case "edge": {
                WebDriverManager.edgedriver().setup();
                LOG.info("Edge driver set up started");
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                driverSet(edgeOptions);
                driverTL.get().manage().window().maximize();
                LOG.info("Edge set up completed successfully");
                break;
            }
            default: {
                LOG.error("Error: Unidentified Browser! Please select chrome, firefox or edge");
            }
        }
        driverTL.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitlyWaitSeconds));
    }

    public static WebDriver getDriverTL() {
        try {
            return driverTL.get();

        } catch (Exception e) {
            LOG.error("Driver is null, can not get driver!");
            return null;
        }
    }

    public static void quitDriver() {
        if (driverTL != null) {
            try {
                driverTL.get().quit();
            } catch (Exception e) {
                LOG.error("Error quitting driver: {}", e.getMessage());
            } finally {
                driverTL.remove();  // Bu, ThreadLocal'i sıfırlar
            }
        } else {
            LOG.error("Driver is already null, can not quit!");
        }
    }

    public static String getSelectAllCommand() {
        return Keys.chord(System.getProperty("os.name").toLowerCase().contains("mac") ? Keys.COMMAND : Keys.CONTROL, "a");
    }

    public static void driverSet(AbstractDriverOptions<?> options) {
        WebDriver driver = null;
        if (options instanceof ChromeOptions) {
            try {
                driver = new ChromeDriver((ChromeOptions) options);
            } catch (Exception e) {
                LOG.error("Error setting up Chrome driver: {}", e.getMessage());
                Assert.fail("Error setting up Chrome driver: " + e.getMessage());
            }
        } else if (options instanceof FirefoxOptions) {
            try {
                driver = new FirefoxDriver((FirefoxOptions) options);
            } catch (Exception e) {
                LOG.error("Error setting up Firefox driver: {}", e.getMessage());
                Assert.fail("Error setting up Firefox driver: " + e.getMessage());
            }
        } else if (options instanceof EdgeOptions) {
            try {
                driver = new EdgeDriver((EdgeOptions) options);
            } catch (Exception e) {
                LOG.error("Error setting up Edge driver: {}", e.getMessage());
                Assert.fail("Error setting up Edge driver: " + e.getMessage());
            }
        } else {
            LOG.error("Unsupported browser options provided.");
            Assert.fail("Unsupported browser options provided.");
        }

        if (driver == null) {
            LOG.error("Driver could not be initialized, it is null.");
            Assert.fail("Driver could not be initialized.");
        } else {
            driverTL.set(driver);
        }
    }

    public static void setBaseUrlTL(String baseUrl) {
        baseUrlTL.set(baseUrl);
    }

    public static String getBaseUrlTL() {
        Optional<String> baseUrlOpt = Optional.ofNullable(baseUrlTL.get());
        if (baseUrlOpt.isEmpty()) {
            LOG.error("Base URL is null, baseUrl set default");
            setBaseUrlTL(ConfigReader.getConfig("baseURL"));
            return baseUrlTL.get();
        }
        return baseUrlTL.get();
    }
}