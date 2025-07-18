package com.loanemi.base;

import com.loanemi.utils.ConfigReader;
import com.loanemi.utils.ScreenshotUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.URL;
import java.net.MalformedURLException;

/**
 * DriverSetup is a central class for initializing WebDriver instances
 * across different browsers and execution modes (local or remote/grid).
 * It supports thread-safe WebDriver management using ThreadLocal.
 */
public class DriverSetup {

    private static final Logger logger = LogManager.getLogger("DriverLogger");

    // Thread-local WebDriver to support parallel execution and thread isolation
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    /**
     * Public accessor for obtaining the WebDriver instance.
     * Determines mode of execution from config: 'local' or 'remote'.
     * 
     * @param browser Browser name (chrome, edge, firefox)
     * @return WebDriver instance for current thread
     */
    public static WebDriver getDriver(String browser) {
        if (driver.get() == null) {
            String mode = ConfigReader.getProperty("executionMode").trim().toLowerCase();
            System.out.println(mode); // Debug print (optional)

            if (mode.equals("remote")) {
                logger.info("Execution mode: REMOTE via Selenium Grid");
                driver.set(setupRemoteDriver(browser));
            } else {
                logger.info("Execution mode: LOCAL");
                driver.set(setupLocalDriver(browser));
            }

            // Maximize browser window after launching
            driver.get().manage().window().maximize();
        }

        return driver.get();
    }

    /**
     * Launches browser locally without Grid, based on requested browser type.
     * 
     * @param browser Browser name
     * @return Local WebDriver instance
     */
    private static WebDriver setupLocalDriver(String browser) {
        WebDriver localDriver;

        switch (browser.toLowerCase()) {
            case "chrome":
                localDriver = new ChromeDriver();
                logger.info("Local Chrome browser launched");
                break;
            case "edge":
            case "microsoftedge":
                localDriver = new EdgeDriver();
                logger.info("Local Edge browser launched");
                break;
            case "firefox":
                localDriver = new FirefoxDriver();
                logger.info("Local Firefox browser launched");
                break;
            default:
                logger.warn("Unknown browser '{}', defaulting to Chrome", browser);
                localDriver = new ChromeDriver();
        }

        return localDriver;
    }

    /**
     * Sets up WebDriver for remote execution using Selenium Grid.
     * Reads grid URL and target platform from config.properties.
     * 
     * @param browser Browser name
     * @return Remote WebDriver instance
     */
    private static WebDriver setupRemoteDriver(String browser) {
        String gridUrl = ConfigReader.getProperty("seleniumGridUrl");
        String platformName = ConfigReader.getProperty("platformType").toLowerCase();
        Platform platform = getPlatform(platformName);

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setPlatform(platform);

        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    cap.setBrowserName("chrome");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.merge(cap);
                    logger.info("Launching Remote Chrome with Platform: {}", platform);
                    return new RemoteWebDriver(new URL(gridUrl), chromeOptions);

                case "edge":
                case "microsoftedge":
                    cap.setBrowserName("MicrosoftEdge");
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.merge(cap);
                    logger.info("Launching Remote Edge with Platform: {}", platform);
                    return new RemoteWebDriver(new URL(gridUrl), edgeOptions);

                case "firefox":
                    cap.setBrowserName("firefox");
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.merge(cap);
                    logger.info("Launching Remote Firefox with Platform: {}", platform);
                    return new RemoteWebDriver(new URL(gridUrl), firefoxOptions);

                default:
                    logger.error("Unsupported browser specified for remote: {}", browser);
                    throw new IllegalArgumentException("Unsupported remote browser: " + browser);
            }
        } catch (MalformedURLException e) {
            logger.error("Malformed Selenium Grid URL: {}", gridUrl, e);
            throw new RuntimeException("Failed to connect to Selenium Grid", e);
        }
    }

    /**
     * Utility method to map string to Selenium Platform enum.
     * Supports 'windows', 'mac', 'linux', and 'any'.
     * 
     * @param name Platform name in lowercase
     * @return Corresponding Platform enum
     */
    private static Platform getPlatform(String name) {
        switch (name.toLowerCase()) {
            case "windows":
                return Platform.WINDOWS;
            case "mac":
                return Platform.MAC;
            case "linux":
                return Platform.LINUX;
            case "any":
            default:
                return Platform.ANY;
        }
    }

    /**
     * Gracefully shuts down the WebDriver and removes thread-local reference.
     * Captures screenshot before closure to aid reporting/debugging.
     */
    public static void quitDriver() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            ScreenshotUtil.captureScreenshot(currentDriver, "Before_Browser_Close");
            currentDriver.quit();
            logger.info("Driver session closed and removed from thread");
            driver.remove();
        } else {
            logger.warn("No active driver found to shut down");
        }
    }
}
