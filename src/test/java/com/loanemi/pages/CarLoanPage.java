package com.loanemi.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;
import com.loanemi.utils.ConfigReader;
import com.loanemi.utils.ExtentReportUtil;
import com.loanemi.entity.JsonData;
import com.loanemi.utils.JsonUtil;
import com.loanemi.utils.WaitUtil;

import org.testng.Assert;

/**
 * Page Object class for Car Loan EMI Calculator.
 * Inputs loan data, selects EMI scheme, and validates output against financial formula.
 */
public class CarLoanPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger("CarLoanLogger");

    private String testData = ConfigReader.getProperty("testCase1");
    private String jsonFilePath = ConfigReader.getProperty("jsonFilePath");
    JsonData testCase = JsonUtil.getTestCase(jsonFilePath, testData);

    /**
     * Constructor: Initializes page factory and prepares test data
     */
    public CarLoanPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("Initialized CarLoanPage with testData key '{}'", testData);
    }

    // UI elements from Car Loan tab
    @FindBy(id = "car-loan") 
    private WebElement carLoanTab;
    @FindBy(id = "loanamount") 
    private WebElement loanAmountInput;
    @FindBy(id = "loaninterest") 
    private WebElement interestRateInput;
    @FindBy(id = "loanterm") 
    private WebElement loanTenureInput;
    @FindBy(xpath = "//div[@id='emitotalinterest']/p/span") 
    private WebElement totalInterest;
    @FindBy(xpath = "//div[@id='emiamount']/p/span") 
    private WebElement emiAmount;
    @FindBy(xpath = "//*[@id=\"leschemewrapper\"]/div/div/div/div/label[2]") 
    private WebElement emiInArrears;
    @FindBy(xpath = "//input[@id='emiadvance']/parent::label") 
    private WebElement emiInAdvance;

    /**
     * Executes the full Car Loan EMI calculation flow and performs validation
     */
    public void calculateCarLoan() {

        logger.info("Starting Car Loan calculation using test data");

        int amount = Integer.parseInt(testCase.getAmount());
        double rate = Double.parseDouble(testCase.getRate());
        int years = Integer.parseInt(testCase.getYears());

        logger.debug("Test Inputs — Amount: ₹{}, Rate: {}%, Tenure: {} year(s)", amount, rate, years);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Activate Car Loan tab — essential page load trigger
            WaitUtil.waitForClickability(driver, carLoanTab).click();
            logger.info("Clicked on Car Loan tab");
            
            WaitUtil.applyImplicitWait(driver, 2);

            // Assuming tab content is loaded immediately — no repetitive waits required
            js.executeScript("arguments[0].value='" + amount + "';", loanAmountInput);
            logger.debug("Entered Loan Amount: ₹{}", amount);

            js.executeScript("arguments[0].value='" + rate + "';", interestRateInput);
            logger.debug("Entered Interest Rate: {}%", rate);

            js.executeScript("arguments[0].value='" + years + "';", loanTenureInput);
            logger.debug("Entered Loan Tenure: {} year(s)", years);

            // Select EMI mode: toggle both to trigger recalculation
            emiInAdvance.click();
            emiInArrears.click();
            logger.debug("Selected EMI Advance and then EMI Arrears options");

            // Light implicit wait for summary result section to refresh
            WaitUtil.applyImplicitWait(driver, 2);

            // Fetch and parse calculated EMI and interest values
            String actualEMIText = emiAmount.getText().replaceAll("[^\\d.]", "");
            String actualInterestText = totalInterest.getText().replaceAll("[^\\d.]", "");

            double actualEMI = Double.parseDouble(actualEMIText);
            double actualInterest = Double.parseDouble(actualInterestText);

            logger.debug("Fetched from page — EMI: ₹{}, Interest: ₹{}", actualEMI, actualInterest);

            // Compute expected EMI using formula
            double monthlyRate = rate / 12 / 100;
            int months = years * 12;

            double expectedEMI = (amount * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                                 (Math.pow(1 + monthlyRate, months) - 1);

            double totalPayable = expectedEMI * months;
            double expectedInterest = totalPayable - amount;

            // Round to 2 decimals
            expectedEMI = Math.round(expectedEMI * 100.0) / 100.0;
            expectedInterest = Math.round(expectedInterest * 100.0) / 100.0;

            logger.info("Expected EMI: ₹{}, Actual EMI: ₹{}", expectedEMI, actualEMI);
            logger.info("Expected Interest: ₹{}, Actual Interest: ₹{}", expectedInterest, actualInterest);

            // Tolerance check — allows ±₹1 deviation
            if (Math.abs(expectedEMI - actualEMI) <= 1 && Math.abs(expectedInterest - actualInterest) <= 1) {
                ExtentReportUtil.getTest().log(Status.PASS, "EMI and Interest are correctly calculated.");
                logger.info("Validation passed — EMI and Interest match expected values");
            } else {
                ExtentReportUtil.getTest().log(Status.FAIL, "EMI or Interest mismatch. Expected EMI: ₹" + expectedEMI +
                    ", Actual EMI: ₹" + actualEMI + " | Expected Interest: ₹" + expectedInterest +
                    ", Actual Interest: ₹" + actualInterest);
                logger.error("Validation failed — EMI or Interest mismatch detected");

                js.executeScript("arguments[0].scrollIntoView({block: 'start'});", emiInArrears);
                Assert.fail("EMI or Interest calculation mismatch.");
            }

        } catch (Exception e) {
            logger.error("Exception during car loan calculation", e);
            ExtentReportUtil.getTest().log(Status.FAIL, "Unexpected exception during EMI calculation: " + e.getMessage());
            Assert.fail("Unexpected exception during EMI calculation: " + e.getMessage());
        }
    }
}
