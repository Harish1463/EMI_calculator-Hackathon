package com.loanemi.runner;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.loanemi.hooks.CucumberHooks;
import com.loanemi.utils.AllureReportOpener;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Cucumber Test Runner class integrated with TestNG.
 * Executes feature files based on specified tags and generates
 * HTML, JSON, and JUnit-style reports.
 */
@CucumberOptions(
    // Path to feature files
    features = "src/test/resources/com/loanemi/features",

    // Package locations for step definitions and hooks
    glue = {"com.loanemi.stepdefs", "com.loanemi.hooks"},

    // Filters which scenarios to run based on tags
    tags = "@carpage or @creditpage or @homepage or @loanpage",

    // Report formats for various integrations
    plugin = {
        "pretty",
        "html:target/cucumber-reports/html-report.html",
        "json:target/cucumber-reports/report.json",
        "junit:target/cucumber-reports/report.xml"
    },

    // Cleans up output and removes ANSI coloring
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    private static final Logger logger = LogManager.getLogger(TestRunner.class);   
    
    /**
     * Assigns the browser value to current thread context before executing tests.
     * If no browser parameter is passed, defaults to 'chrome'.
     *
     * @param browser Browser name passed from testng.xml or CLI
     */
    @Parameters({"browser"})
    @BeforeClass
    public void setUp(@Optional("chrome") String browser) {
        CucumberHooks.setBrowser(browser);
        logger.info("TestNG @BeforeClass: Browser parameter assigned to thread → {}", browser);
    }
    
    /**
     * Executes once after the entire Cucumber suite finishes.
     * Triggers Allure report generation and opens it automatically.
     */
    @AfterSuite
    public void afterSuite() {
        logger.info("Cucumber Test Suite completed. Preparing to launch Allure report...");
        AllureReportOpener.openAllureReport();
        logger.info("Allure report launcher invoked.");
    }
}
