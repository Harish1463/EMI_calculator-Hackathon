package com.loanemi.hooks;

import java.io.ByteArrayInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.aventstack.extentreports.Status;
import com.loanemi.base.DriverSetup;
import com.loanemi.utils.AllureReportOpener;
import com.loanemi.utils.ConfigReader;
import com.loanemi.utils.ExtentReportUtil;
import com.loanemi.utils.ScreenshotUtil;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

/**
 * CucumberHooks defines lifecycle operations for every Cucumber scenario.
 * It sets up browser drivers before each test and captures reports + screenshots after execution.
 */
public class CucumberHooks {

    // Thread-local reference to browser name for parallel-safe execution
    private static final ThreadLocal<String> threadBrowser = new ThreadLocal<>();

    private static final Logger logger = LogManager.getLogger("SetupLogger");

    // Base application URL loaded before each test
    private final String baseUrl = ConfigReader.getProperty("baseUrl");

    /**
     * Assigns browser name to current test thread
     * This allows browser-specific execution during parallel runs
     */
    public static void setBrowser(String browser) {
        threadBrowser.set(browser);
        logger.info("Browser parameter assigned to thread → {}", browser);
    }

    /**
     * Executes before every scenario
     * Initializes ExtentReport, launches WebDriver based on mode and navigates to app URL
     */
    @Before
    public void launchDriverAndInitReport(Scenario scenario) {
        String browser = threadBrowser.get();

        // Fallback to Chrome if browser is not explicitly set
        if (browser == null) {
            browser = "chrome";
            logger.warn("Thread-local browser not set, defaulting to Chrome");
        }

        String mode = ConfigReader.getProperty("executionMode").trim().toLowerCase();
        logger.info("Cucumber: Launching test in '{}' mode with browser '{}'", mode, browser);

        // Start new test in ExtentReports
        ExtentReportUtil.initReport();
        ExtentReportUtil.createTest(scenario.getName());

        // Launch browser via DriverSetup and open base URL
        WebDriver driver = DriverSetup.getDriver(browser);
        driver.get(baseUrl);
        logger.info("Navigated to base URL: {}", baseUrl);
    }

    /**
     * Executes after each scenario
     * Captures screenshot (if failed), closes browser, and flushes reports
     */
    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverSetup.getDriver(threadBrowser.get());
        String scenarioName = scenario.getName();

        if (scenario.isFailed()) {
            logger.warn("Cucumber: Test failed - '{}' | Capturing screenshot", scenarioName);
            
            // Log failure in ExtentReport
            ExtentReportUtil.getTest().log(Status.FAIL, scenarioName + " failed");
            ExtentReportUtil.getTest().log(Status.FAIL, "Failure details: " + scenario.getStatus());

            // Attach screenshot to Allure Report
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on Failure", new ByteArrayInputStream(screenshot));

            // Capture screenshot path for ExtentReports
            String path = ScreenshotUtil.captureScreenshotWithPath(driver, scenarioName);
            ExtentReportUtil.getTest().addScreenCaptureFromPath(path);
        } else {
            logger.info("Cucumber: Test completed - '{}'", scenarioName);
            ExtentReportUtil.getTest().log(Status.PASS, scenarioName + " passed");
        }

        // Safely close WebDriver
        try {
            DriverSetup.quitDriver();
        } catch (WebDriverException e) {
            logger.warn("Cucumber: WebDriver quit failed — session may already be invalid. {}", e.getMessage());
        }
        
        AllureReportOpener.openAllureReport();
        logger.info("Allure report launcher invoked.");
    }

    /**
     * Retrieves browser name used by current test thread
     * Useful for debugging and reporting
     */
    public static String getDriver() {
        return threadBrowser.get();
    }
}
