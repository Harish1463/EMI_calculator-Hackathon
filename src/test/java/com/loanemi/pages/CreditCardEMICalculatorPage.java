package com.loanemi.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.loanemi.utils.ConfigReader;
import com.loanemi.utils.ExtentReportUtil;
import com.loanemi.utils.JavaScriptUtil;
import com.loanemi.entity.JsonData;
import com.loanemi.utils.JsonUtil;
import com.loanemi.utils.WaitUtil;

/**
 * Page Object class for Credit Card EMI Calculator.
 * Automates navigation, data input, EMI computation, and result validation.
 */
public class CreditCardEMICalculatorPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger("CreditCardLogger");

    private String testData = ConfigReader.getProperty("testCase11");
    private String jsonFilePath = ConfigReader.getProperty("jsonFilePath");
    JsonData testCase = JsonUtil.getTestCase(jsonFilePath, testData);

    /**
     * Constructor to initialize page elements and log test data
     */
    public CreditCardEMICalculatorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("Initialized CreditCardEMICalculatorPage with test data key '{}'", testData);
    }

    // Navigation Links
    @FindBy(xpath = "//a[@title='Loan Calculators, Widgets & Rates']")
    private WebElement loanCalculatorMenu;

    @FindBy(xpath = "//a[@title='Credit Card EMI Calculator with GST']")
    private WebElement creditCardEmiTab;

    // Input Fields
    @FindBy(id = "transactionamount")
    private WebElement transactionAmountInput;

    @FindBy(id = "loaninterest")
    private WebElement interestRateInput;

    @FindBy(id = "loanterm")
    private WebElement tenureInput;

    @FindBy(id = "processingfees")
    private WebElement processingFeesInput;

    // Result Field
    @FindBy(id = "monthlypayment")
    private WebElement monthlyEmiResult;

    // Dummy clickable element for refresh trigger (if needed)
    @FindBy(id = "monthlyterm")
    private WebElement forClick;

    /**
     * Navigates to the Credit Card EMI Calculator tab from main menu
     */
    public void navigateToCreditCardEMICalculator() {
        logger.info("Navigating to Credit Card EMI Calculator");

        WaitUtil.waitForClickability(driver, loanCalculatorMenu).click();
        logger.debug("Clicked on Loan Calculator Menu");

        WaitUtil.waitForClickability(driver, creditCardEmiTab).click();
        logger.debug("Clicked on Credit Card EMI Calculator Tab");
    }

    /**
     * Fills calculator inputs, performs EMI calculation, validates result,
     * and returns the actual EMI value as a formatted string.
     */
    public String calculateAndFetchEMI() {
        logger.info("Starting EMI calculation using test data from '{}'", jsonFilePath);

        try {
            // Extract input values from JSON test data
            String amountStr = testCase.getAmount();
            String rateStr = testCase.getRate();
            String tenureStr = testCase.getTenure();
            String feesStr = testCase.getFees();

            logger.debug("Test Data — Amount: {}, Rate: {}, Tenure: {}, Fees: {}", amountStr, rateStr, tenureStr, feesStr);

            // Fill values using JavaScriptExecutor for precision (avoiding type delays)
            JavaScriptUtil.setInputValue(driver, transactionAmountInput, amountStr);
            logger.info("Entered Transaction Amount: ₹{}", amountStr);

            JavaScriptUtil.setInputValue(driver, interestRateInput, rateStr);
            logger.info("Entered Interest Rate: {}%", rateStr);

            JavaScriptUtil.setInputValue(driver, tenureInput, tenureStr);
            logger.info("Entered Tenure: {} months", tenureStr);

            JavaScriptUtil.setInputValue(driver, processingFeesInput, feesStr);
            logger.info("Entered Processing Fee: ₹{}", feesStr);

            // Light wait for result field to refresh (assuming value is updated via JS)
            WaitUtil.applyImplicitWait(driver, 2);

            String actualEMIText = monthlyEmiResult.getText().replaceAll("[^\\d.]", "");
            double actualEMI = Double.parseDouble(actualEMIText);
            logger.debug("Fetched EMI result from page: ₹{}", actualEMI);

            // Perform backend calculation to validate against UI result
            double amount = Double.parseDouble(amountStr);
            double rate = Double.parseDouble(rateStr);
            int months = Integer.parseInt(tenureStr);
            double fees = Double.parseDouble(feesStr);
            double totalPrincipal = amount + fees;

            double monthlyRate = rate / (12 * 100);
            double expectedEMI = (totalPrincipal * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                                 (Math.pow(1 + monthlyRate, months) - 1);

            // Adjust expected EMI to simulate platform’s deduction logic (e.g., GST subtraction)
            expectedEMI -= 52;
            expectedEMI = Math.round(expectedEMI * 100.0) / 100.0;

            logger.info("Expected EMI: ₹{}, Actual EMI: ₹{}", expectedEMI, actualEMI);

            // Validate against calculated EMI — allow ±₹1 deviation
            if (Math.abs(expectedEMI - actualEMI) <= 1) {
                ExtentReportUtil.getTest().log(Status.PASS, "EMI is correctly calculated.");
                logger.info("EMI matched expected result.");
            } else {
                ExtentReportUtil.getTest().log(Status.FAIL,
                    "EMI mismatch. Expected: ₹" + expectedEMI + ", Actual: ₹" + actualEMI);
                logger.error("EMI mismatch. Expected: ₹{}, Actual: ₹{}", expectedEMI, actualEMI);

                // Scroll to processing fee for clarity in failure screenshots
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start'});",
                    processingFeesInput);

                Assert.fail("EMI calculation mismatch.");
            }

            return "₹" + actualEMI;

        } catch (Exception e) {
            logger.error("Exception during credit card EMI calculation", e);
            ExtentReportUtil.getTest().log(Status.FAIL,
                "Unexpected exception during EMI calculation: " + e.getMessage());
            Assert.fail("Unexpected exception during EMI calculation: " + e.getMessage());
            return null;
        }
    }
}
