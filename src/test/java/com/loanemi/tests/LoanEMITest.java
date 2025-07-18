package com.loanemi.tests;

import java.io.ByteArrayInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.loanemi.base.DriverSetup;
import com.loanemi.pages.CarLoanPage;
import com.loanemi.pages.CreditCardEMICalculatorPage;
import com.loanemi.pages.HomeLoanPage;
import com.loanemi.pages.LoanCalculatorPage;
import com.loanemi.rerun.RetryAnalyzer;
import com.loanemi.utils.AllureReportOpener;
import com.loanemi.utils.ConfigReader;
import com.loanemi.utils.ExtentReportUtil;
import com.loanemi.utils.ScreenshotUtil;

import io.qameta.allure.Allure;

/**
 * TestNG-based automation suite for EMI calculations.
 * Covers Car Loan, Home Loan, Credit Card EMI, and general Loan calculators
 * with field-level and summary-level validations.
 */
public class LoanEMITest {

    private static final Logger logger = LogManager.getLogger("ExecutionLogger");

    private String baseUrl = ConfigReader.getProperty("baseUrl");
    private WebDriver driver;

    /**
     * Initializes WebDriver before test suite begins.
     * Accepts 'browser' parameter from testng.xml or CLI.
     */
    @Parameters({"browser"})
    @BeforeClass
    public void setUp(String browser) {
        logger.info("TestNG: Initializing driver for browser: {}", browser);
        ExtentReportUtil.initReport();

        driver = DriverSetup.getDriver(browser);
        driver.get(baseUrl);

        logger.info("Navigated to base URL: {}", baseUrl);
    }

    // ================= Car Loan EMI Calculation =================

    @Test(priority = 1, retryAnalyzer = RetryAnalyzer.class)
    public void testCarLoanEMICalculation() {
        logger.info("TestNG: Running Car Loan EMI calculation test");
        ExtentReportUtil.createTest("Car Loan EMI Calculation")
            .log(Status.INFO, "Calculating EMI for ₹15L, 9.5%, 1 year");

        new CarLoanPage(driver).calculateCarLoan(); // Full flow including validation
    }

    // ================= Home Loan EMI Flow =================

    @Test(priority = 2, retryAnalyzer = RetryAnalyzer.class)
    public void testHomeLoanEMIDataExtraction() {
        logger.info("TestNG: Running Home Loan EMI data extraction test");
        ExtentReportUtil.createTest("Home Loan EMI Data Extraction")
            .log(Status.INFO, "Filling Home Loan form and extracting EMI summary + amortization table");

        new HomeLoanPage(driver).calculateAndExtractHomeLoanEMI(); // Includes Excel export
    }

    // ================= EMI Calculator =================

    @Test(priority = 3, retryAnalyzer = RetryAnalyzer.class)
    public void testOnlyEMICalculatorFields() {
        logger.info("TestNG: Validating EMI calculator fields and sliders only");
        ExtentReportUtil.createTest("EMI Calculator Field Validation Only")
            .log(Status.INFO, "Validating only EMI calculator fields and sliders");

        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.emiFieldValidation(); // Field-slider sync check
    }

    @Test(priority = 4, retryAnalyzer = RetryAnalyzer.class)
    public void testEMICalculatorUIValidation() {
        logger.info("TestNG: Validating EMI calculator summary");
        ExtentReportUtil.createTest("EMI Calculator UI Validation")
            .log(Status.INFO, "Validating EMI calculator fields and summary");

        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.validateEMICalculatorUI(); // Field + summary
    }

    // ================= Loan Amount Calculator =================

    @Test(priority = 5, retryAnalyzer = RetryAnalyzer.class)
    public void testOnlyLoanAmountCalculatorFields() {
        logger.info("TestNG: Validating Loan Amount calculator fields and sliders only");
        ExtentReportUtil.createTest("Loan Amount Calculator Field Validation Only")
            .log(Status.INFO, "Validating only Loan Amount calculator fields and sliders");

        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.loanAmountFieldValidation();
    }

    @Test(priority = 6, retryAnalyzer = RetryAnalyzer.class)
    public void testLoanAmountCalculatorUIValidation() {
        logger.info("TestNG: Validating Loan Amount calculator summary");
        ExtentReportUtil.createTest("Loan Amount Calculator UI Validation")
            .log(Status.INFO, "Validating Loan Amount calculator fields and summary");

        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.validateLoanAmountCalculatorUI();
    }

    // ================= Loan Tenure Calculator =================

    @Test(priority = 7, retryAnalyzer = RetryAnalyzer.class)
    public void testOnlyLoanTenureCalculatorFields() {
        logger.info("TestNG: Validating Loan Tenure calculator fields and sliders only");
        ExtentReportUtil.createTest("Loan Tenure Calculator Field Validation Only")
            .log(Status.INFO, "Validating only Loan Tenure calculator fields and sliders");

        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.loanTenureFieldValidation();
    }

    @Test(priority = 8, retryAnalyzer = RetryAnalyzer.class)
    public void testLoanTenureCalculatorUIValidation() {
        logger.info("TestNG: Validating Loan Tenure calculator summary");
        ExtentReportUtil.createTest("Loan Tenure Calculator UI Validation")
            .log(Status.INFO, "Validating Loan Tenure calculator fields and summary");

        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.validateLoanTenureCalculatorUI();
    }

    // ================= Interest Rate Calculator =================

    @Test(priority = 9, retryAnalyzer = RetryAnalyzer.class)
    public void testOnlyInterestRateCalculatorFields() {
        logger.info("TestNG: Validating Interest Rate calculator fields and sliders only");
        ExtentReportUtil.createTest("Interest Rate Calculator Field Validation Only")
            .log(Status.INFO, "Validating only Interest Rate calculator fields and sliders");

        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.interestRateFieldValidation();
    }

    @Test(priority = 10, retryAnalyzer = RetryAnalyzer.class)
    public void testInterestRateCalculatorUIValidation() {
        logger.info("TestNG: Validating Interest Rate calculator summary");
        ExtentReportUtil.createTest("Interest Rate Calculator UI Validation")
            .log(Status.INFO, "Validating Interest Rate calculator fields and summary");

        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.validateInterestRateCalculatorUI();
    }

    // ================= Credit Card EMI Calculator =================

    @Test(priority = 11, retryAnalyzer = RetryAnalyzer.class)
    public void testCreditCardEMICalculatorCalculation() {
        logger.info("TestNG: Running Credit Card EMI calculation test");
        ExtentReportUtil.createTest("Credit Card EMI Calculation")
            .log(Status.INFO, "Calculating EMI for credit card transaction");

        CreditCardEMICalculatorPage creditPage = new CreditCardEMICalculatorPage(driver);
        creditPage.navigateToCreditCardEMICalculator();

        String emi = creditPage.calculateAndFetchEMI();
//        System.out.println("EMI displayed: " + emi);
        ExtentReportUtil.getTest().log(Status.PASS, "EMI displayed as: " + emi);
    }

    // ================= Result Capture and Reporting =================

    /**
     * Captures result and logs to Extent and Allure depending on outcome.
     * On failure, captures screenshot and attaches it to reports.
     */
    @AfterMethod
    public void captureResult(ITestResult result) {
        String testName = result.getName();

        if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("TestNG: Test '{}' passed", testName);
            ExtentReportUtil.getTest().log(Status.PASS, testName + " passed");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("TestNG: Test '{}' failed", testName);
            ExtentReportUtil.getTest().log(Status.FAIL, testName + " failed");
            ExtentReportUtil.getTest().log(Status.FAIL, result.getThrowable());

            // Screenshot for Extent report
            String path = ScreenshotUtil.captureScreenshotWithPath(driver, testName);
            ExtentReportUtil.getTest().addScreenCaptureFromPath(path);

            // Screenshot for Allure
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on Failure", new ByteArrayInputStream(screenshot));
        } else {
            logger.warn("TestNG: Test '{}' skipped", testName);
            ExtentReportUtil.getTest().log(Status.SKIP, testName + " skipped");
        }
    }

    /**
     * Cleans up WebDriver and flushes Extent + launches Allure report.
     * Called once after all tests finish executing.
     */
    @AfterClass
    public void tearDown() {
        logger.info("TestNG: Quitting WebDriver and flushing ExtentReports");
        DriverSetup.quitDriver();

        logger.info("TestNg Suite completed. Preparing to launch Allure report...");
        AllureReportOpener.openAllureReport();
        logger.info("Allure report launcher invoked.");

        ExtentReportUtil.flushReport();
    }
}
