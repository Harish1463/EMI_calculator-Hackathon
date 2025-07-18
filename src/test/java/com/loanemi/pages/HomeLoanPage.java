package com.loanemi.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.loanemi.utils.ConfigReader;
import com.loanemi.utils.ExcelUtils;
import com.loanemi.utils.ExtentReportUtil;
import com.loanemi.utils.JavaScriptUtil;
import com.loanemi.entity.JsonData;
import com.loanemi.utils.JsonUtil;
import com.loanemi.utils.WaitUtil;

import java.util.List;

/**
 * Page Object class for Home Loan EMI Calculator.
 * Handles form interaction and prepayment inputs using test data,
 * and supports downstream result validation and Excel extraction.
 */
public class HomeLoanPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger("HomeLoanLogger");

    // Config-driven test data mapping
    private String testData = ConfigReader.getProperty("testCase2");
    private String jsonFilePath = ConfigReader.getProperty("jsonFilePath");
    JsonData testCase = JsonUtil.getTestCase(jsonFilePath, testData);

    private final String excelPath = ConfigReader.getProperty("excelPath");
    private final String LoanSheet = ConfigReader.getProperty("LoanSheet");
    private final String YearlyDataSheet = ConfigReader.getProperty("YearlyDataSheet");

    /**
     * Constructor: initializes page elements
     */
    public HomeLoanPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("Initialized HomeLoanPage with test data key '{}'", testData);
    }

    // ===== Navigation =====
    @FindBy(xpath = "//a[@title='Loan Calculators, Widgets & Rates']")
    private WebElement loanCalculatorMenu;

    @FindBy(xpath = "//ul[@class='dropdown-menu show']//a[text()='Home Loan EMI Calculator']")
    private WebElement homeLoanTab;

    // ===== Input Fields =====
    @FindBy(id = "homeprice") 
    private WebElement homeValueInput;
    @FindBy(id = "downpayment") 
    private WebElement downPaymentInput;
    @FindBy(id = "homeloaninsuranceamount") 
    private WebElement loanInsuranceInput;
    @FindBy(id = "homeloanamount") 
    private WebElement loanAmountInput;
    @FindBy(id = "homeloaninterest") 
    private WebElement interestRateInput;
    @FindBy(id = "homeloanterm") 
    private WebElement loanTenureInput;
    @FindBy(id = "loanfees") 
    private WebElement loanFeesInput;
    @FindBy(id = "onetimeexpenses") 
    private WebElement oneTimeExpensesInput;
    @FindBy(id = "propertytaxes") 
    private WebElement propertyTaxesInput;
    @FindBy(id = "homeinsurance") 
    private WebElement homeInsuranceInput;
    @FindBy(id = "maintenanceexpenses") 
    private WebElement maintenanceExpensesInput;

    // ===== Summary Output Sections =====
    @FindBy(id = "monthlyprincipalandinterestdef") 
    private WebElement principalAndInterest;
    @FindBy(id = "extradef") 
    private WebElement extraPayment;
    @FindBy(id = "monthlypropertytaxesdef") 
    private WebElement propertyTaxes;
    @FindBy(id = "monthlyhomeinsurancedef") 
    private WebElement homeInsurance;
    @FindBy(id = "monthlymaintenanceexpensesdef") 
    private WebElement maintenanceExpenses;
    @FindBy(id = "monthlypayment") 
    private WebElement totalMonthlyPayment;
    @FindBy(id = "downpaymentfeesonetimeexpensesdef") 
    private WebElement downPaymentFees;
    @FindBy(id = "totalprincipaldef") 
    private WebElement totalPrincipal;
    @FindBy(id = "totalextradef") 
    private WebElement totalPrepayments;
    @FindBy(id = "totalinterestdef") 
    private WebElement totalInterest;
    @FindBy(id = "totalinsurancemaintenanceandtaxesdef") 
    private WebElement totalTaxesInsuranceMaintenance;
    @FindBy(id = "totalpayment") 
    private WebElement totalOfAllPayments;

    // ===== Prepayment Section =====
    @FindBy(xpath = "//div[contains(@class,'addextrapayments')]//a[contains(@class,'hidden-ts') and contains(text(),'Add Prepayments')]")
    private WebElement addPrepaymentsButton;

    @FindBy(id = "extramonthly") private WebElement extraMonthlyInput;
    @FindBy(id = "extrayearly") private WebElement extraYearlyInput;
    @FindBy(id = "extraquarterly") private WebElement extraQuarterlyInput;
    @FindBy(id = "extraonetime") private WebElement extraOneTimeInput;

    // Misc
    @FindBy(xpath = "//td[@id='monthlyprincipalandinterestterm']")
    private WebElement forUpdateResults;

    @FindBy(xpath = "//tr[contains(@class,'yearlypaymentdetails')]")
    private List<WebElement> yearlyRows;

    /**
     * Fills the Home Loan EMI Calculator form with data from test case JSON
     */
    private void fillHomeLoanForm() {
        logger.info("Filling Home Loan form");

        try {
            // Assuming page content is stable once loaded — no extra waits needed unless dynamic
            JavaScriptUtil.setInputValue(driver, homeValueInput, testCase.getHomeValue());
            logger.debug("Home Value: ₹{}", testCase.getHomeValue());

            JavaScriptUtil.setInputValue(driver, downPaymentInput, testCase.getDownPayment());
            logger.debug("Down Payment (%): {}", testCase.getDownPayment());

            JavaScriptUtil.setInputValue(driver, loanInsuranceInput, testCase.getInsurance());
            logger.debug("Loan Insurance Amount: ₹{}", testCase.getInsurance());

            JavaScriptUtil.setInputValue(driver, loanAmountInput, testCase.getAmount());
            logger.debug("Loan Amount: ₹{}", testCase.getAmount());

            JavaScriptUtil.setInputValue(driver, interestRateInput, testCase.getRate());
            logger.debug("Interest Rate: {}%", testCase.getRate());

            JavaScriptUtil.setInputValue(driver, loanTenureInput, testCase.getTenure());
            logger.debug("Loan Tenure: {} years", testCase.getTenure());

            JavaScriptUtil.setInputValue(driver, loanFeesInput, testCase.getFees());
            logger.debug("Loan Fees (%): {}", testCase.getFees());

            JavaScriptUtil.setInputValue(driver, oneTimeExpensesInput, testCase.getOneTimeExpense());
            logger.debug("One-Time Expenses: ₹{}", testCase.getOneTimeExpense());

            JavaScriptUtil.setInputValue(driver, propertyTaxesInput, testCase.getTax());
            logger.debug("Property Taxes (%): {}", testCase.getTax());

            JavaScriptUtil.setInputValue(driver, homeInsuranceInput, testCase.getInsurancePerYear());
            logger.debug("Home Insurance (annual %): {}", testCase.getInsurancePerYear());

            JavaScriptUtil.setInputValue(driver, maintenanceExpensesInput, testCase.getMaintenance());
            logger.debug("Maintenance Expenses: ₹{}", testCase.getMaintenance());

            // Expand prepayment section — wait is required here to ensure DOM toggle
            WaitUtil.waitForClickability(driver, addPrepaymentsButton).click();
            logger.info("Clicked on 'Add Prepayments'");

            JavaScriptUtil.setInputValue(driver, extraMonthlyInput, testCase.getMonthly());
            logger.debug("Extra Monthly Payment: ₹{}", testCase.getMonthly());

            JavaScriptUtil.setInputValue(driver, extraYearlyInput, testCase.getYearly());
            logger.debug("Extra Yearly Payment: ₹{}", testCase.getYearly());

            JavaScriptUtil.setInputValue(driver, extraQuarterlyInput, testCase.getQuarterly());
            logger.debug("Extra Quarterly Payment: ₹{}", testCase.getQuarterly());

            JavaScriptUtil.setInputValue(driver, extraOneTimeInput, testCase.getOneTimeOnly());
            logger.debug("Extra One-Time Payment: ₹{}", testCase.getOneTimeOnly());

        } catch (Exception e) {
            logger.error("Exception while filling Home Loan form", e);
            ExtentReportUtil.getTest().log(Status.FAIL, "Exception during form fill: " + e.getMessage());
            Assert.fail("Form fill failed: " + e.getMessage());
        }
    }

    /**
     * Main test flow: navigates to Home Loan tab, fills form,
     * extracts EMI summary and schedule, performs monthly validation,
     * and exports data to Excel.
     */
    public void calculateAndExtractHomeLoanEMI() {
        logger.info("Navigating to Home Loan EMI Calculator");
        try {
            // Open calculator tab — needed for dynamic tab content injection
            WaitUtil.waitForClickability(driver, loanCalculatorMenu).click();
            logger.debug("Clicked on Loan Calculator Menu");

            WaitUtil.waitForClickability(driver, homeLoanTab).click();
            logger.debug("Clicked on Home Loan Tab");

            // One-time buffer before form interaction
            WaitUtil.applyImplicitWait(driver, 2);

            fillHomeLoanForm(); // previously defined
            logger.info("Home loan form filled successfully");

            // Wait removed — assuming form renders results instantly post fill
            logger.info("Starting summary extraction to Excel");
            extractSummaryToExcel();

            logger.info("Validating monthly summary");
            validateMonthlySummary(
                Double.parseDouble(testCase.getExpectedEmi()),
                Double.parseDouble(testCase.getExpectedExtra()),
                Double.parseDouble(testCase.getExpectedTax()),
                Double.parseDouble(testCase.getExpectedInsurance()),
                Double.parseDouble(testCase.getExpectedMaintenance()),
                Double.parseDouble(testCase.getExpectedTotal())
            );

            logger.info("Extracting yearly & monthly EMI schedule to Excel");
            extractYearlyAndMonthlyTableToExcel();

        } catch (Exception e) {
            logger.error("Exception during Home Loan EMI flow", e);
            ExtentReportUtil.getTest().log(Status.FAIL, "Failure during Home Loan EMI execution: " + e.getMessage());
            Assert.fail("Exception in Home Loan EMI flow: " + e.getMessage());
        }
    }

    
    /**
     * Extracts monthly and overall summary values from the Home Loan EMI Calculator
     * and writes them into a structured Excel sheet for reporting purposes.
     */
    private void extractSummaryToExcel() {
        int row = 0;
        logger.info("Extracting EMI summary to sheet '{}'", LoanSheet);

        try {
            // Clear the target sheet before writing fresh data
            ExcelUtils.clearSheet(excelPath, LoanSheet);

            // Add section header and spacing
            ExcelUtils.setCellData(excelPath, LoanSheet, row++, 0, "Home loan details");
            row++;

            // Prepare field names and corresponding values from the UI
            String[][] data = {
                {"Principal & Interest (EMI)", principalAndInterest.getText()},
                {"Monthly Extra Payment", extraPayment.getText()},
                {"Property Taxes", propertyTaxes.getText()},
                {"Home Insurance", homeInsurance.getText()},
                {"Maintenance Expenses", maintenanceExpenses.getText()},
                {"Total Monthly Payment", totalMonthlyPayment.getText()},
                {"Down Payment, Fees & One-time Expenses", downPaymentFees.getText()},
                {"Principal", totalPrincipal.getText()},
                {"Prepayments", totalPrepayments.getText()},
                {"Interest", totalInterest.getText()},
                {"Taxes, Insurance & Maintenance", totalTaxesInsuranceMaintenance.getText()},
                {"Total of All Payments", totalOfAllPayments.getText()}
            };

            // Write each data row into Excel with logging
            for (String[] entry : data) {
                ExcelUtils.setCellData(excelPath, LoanSheet, row++, 0, entry[0]); // Label
                ExcelUtils.setCellData(excelPath, LoanSheet, row - 1, 1, entry[1]); // Value
                logger.debug("{}: {}", entry[0], entry[1]);
            }

            logger.info("Summary data successfully written to Excel");

        } catch (Exception e) {
            logger.error("Error while writing summary to Excel", e);
        }
    }

    
    /**
     * Validates the monthly summary values displayed on the Home Loan EMI Calculator.
     * It compares actual UI values against expected values from test data and logs results.
     * Tolerates slight rounding variations (±₹1) due to calculation or rendering differences.
     *
     * @param expectedEMI           Expected monthly EMI
     * @param expectedExtra         Expected extra payment
     * @param expectedTax           Expected property tax
     * @param expectedInsurance     Expected home insurance
     * @param expectedMaintenance   Expected maintenance cost
     * @param expectedTotal         Expected total monthly payment
     */
    private void validateMonthlySummary(double expectedEMI, double expectedExtra, double expectedTax,
                                        double expectedInsurance, double expectedMaintenance, double expectedTotal) {

        logger.info("Starting monthly summary validation");

        // Use JavaScriptExecutor for fallback scroll (used later only on failure)
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Extract and parse actual values from page (numbers sanitized)
        double actualEMI = Double.parseDouble(principalAndInterest.getText().replaceAll("[^\\d.]", ""));
        double actualExtra = Double.parseDouble(extraPayment.getText().replaceAll("[^\\d.]", ""));
        double actualTax = Double.parseDouble(propertyTaxes.getText().replaceAll("[^\\d.]", ""));
        double actualInsurance = Double.parseDouble(homeInsurance.getText().replaceAll("[^\\d.]", ""));
        double actualMaintenance = Double.parseDouble(maintenanceExpenses.getText().replaceAll("[^\\d.]", ""));
        double actualTotal = Double.parseDouble(totalMonthlyPayment.getText().replaceAll("[^\\d.]", ""));

        // Round expected values to nearest rupee (optional: for easier numeric comparison)
        expectedEMI = Math.round(expectedEMI);
        expectedExtra = Math.round(expectedExtra);
        expectedTax = Math.round(expectedTax);
        expectedInsurance = Math.round(expectedInsurance);
        expectedMaintenance = Math.round(expectedMaintenance);
        expectedTotal = Math.round(expectedTotal);

        // Log comparison values for traceability
        logger.debug("Expected vs Actual breakdown:");
        logger.debug("EMI - Expected: ₹{}, Actual: ₹{}", expectedEMI, actualEMI);
        logger.debug("Extra - Expected: ₹{}, Actual: ₹{}", expectedExtra, actualExtra);
        logger.debug("Tax - Expected: ₹{}, Actual: ₹{}", expectedTax, actualTax);
        logger.debug("Insurance - Expected: ₹{}, Actual: ₹{}", expectedInsurance, actualInsurance);
        logger.debug("Maintenance - Expected: ₹{}, Actual: ₹{}", expectedMaintenance, actualMaintenance);
        logger.debug("Total Payment - Expected: ₹{}, Actual: ₹{}", expectedTotal, actualTotal);

        // Perform fuzzy match within ±₹1 tolerance
        boolean allMatch = Math.abs(expectedEMI - actualEMI) <= 1 &&
                           Math.abs(expectedExtra - actualExtra) <= 1 &&
                           Math.abs(expectedTax - actualTax) <= 1 &&
                           Math.abs(expectedInsurance - actualInsurance) <= 1 &&
                           Math.abs(expectedMaintenance - actualMaintenance) <= 1 &&
                           Math.abs(expectedTotal - actualTotal) <= 1;

        if (allMatch) {
            // If everything matches, log pass status
            ExtentReportUtil.getTest().log(Status.PASS, "Monthly summary values validated successfully.");
            logger.info("Monthly summary validation passed");
        } else {
            // Else log mismatches for each category
            ExtentReportUtil.getTest().log(Status.FAIL, "Monthly summary mismatch.");
            ExtentReportUtil.getTest().log(Status.FAIL, "Expected EMI: ₹" + expectedEMI + ", Actual: ₹" + actualEMI);
            ExtentReportUtil.getTest().log(Status.FAIL, "Expected Extra: ₹" + expectedExtra + ", Actual: ₹" + actualExtra);
            ExtentReportUtil.getTest().log(Status.FAIL, "Expected Tax: ₹" + expectedTax + ", Actual: ₹" + actualTax);
            ExtentReportUtil.getTest().log(Status.FAIL, "Expected Insurance: ₹" + expectedInsurance + ", Actual: ₹" + actualInsurance);
            ExtentReportUtil.getTest().log(Status.FAIL, "Expected Maintenance: ₹" + expectedMaintenance + ", Actual: ₹" + actualMaintenance);
            ExtentReportUtil.getTest().log(Status.FAIL, "Expected Total: ₹" + expectedTotal + ", Actual: ₹" + actualTotal);

            logger.error("Monthly summary mismatch detected");

            // Scroll to EMI field for screenshot clarity
            js.executeScript("arguments[0].scrollIntoView({block: 'start'});", principalAndInterest);

            Assert.fail("Monthly summary validation failed.");
        }
    }
    

    /**
     * Exports yearly and monthly EMI breakdown tables to Excel.
     * Dynamically expands year blocks and captures nested monthly data.
     */
    private void extractYearlyAndMonthlyTableToExcel() {
        int excelRow = 0;

        logger.info("Starting extraction of yearly & monthly EMI data to Excel");
        try {
            ExcelUtils.clearSheet(excelPath, YearlyDataSheet);
            logger.debug("Cleared sheet: {}", YearlyDataSheet);

            ExcelUtils.setCellData(excelPath, YearlyDataSheet, excelRow++, 0, "Home Loan Payment Schedule (Yearly + Monthly)");
            excelRow++;

            String[] headers = {
                "Year", "Principal (A)", "Interest (B)", "Taxes, Home Insurance & Maintenance (C)",
                "Total Payment (A + B + C)", "Balance", "Loan Paid To Date"
            };

            // Write table headers
            for (int col = 0; col < headers.length; col++) {
                ExcelUtils.setCellData(excelPath, YearlyDataSheet, excelRow, col, headers[col]);
            }
            excelRow++;

            // Iterate through yearly rows and nested monthly rows
            for (WebElement yearRow : yearlyRows) {
                List<WebElement> yearCols = yearRow.findElements(By.tagName("td"));
                for (int col = 0; col < yearCols.size(); col++) {
                    ExcelUtils.setCellData(excelPath, YearlyDataSheet, excelRow, col, yearCols.get(col).getText().trim());
                }
                excelRow++;
                logger.debug("Yearly row added: {}", yearCols.get(0).getText().trim());

                // Expand monthly details (simulate dropdown click)
                String yearId = yearCols.get(0).getAttribute("id");
                yearCols.get(0).click(); // toggle visible

                // Locate monthly container using dynamic ID
                String monthlyRowId = "month" + yearId;
                WebElement monthlyContainer = driver.findElement(By.id(monthlyRowId));
                List<WebElement> monthlyRows = monthlyContainer.findElements(By.xpath(".//table/tbody/tr"));

                for (WebElement monthRow : monthlyRows) {
                    List<WebElement> monthCols = monthRow.findElements(By.tagName("td"));
                    for (int col = 0; col < monthCols.size(); col++) {
                        ExcelUtils.setCellData(excelPath, YearlyDataSheet, excelRow, col, monthCols.get(col).getText().trim());
                    }
                    excelRow++;
                }

                logger.debug("Monthly rows for year '{}' exported", yearId);
            }

            logger.info("Yearly and monthly EMI data saved to: {}", excelPath);

        } catch (Exception e) {
            logger.error("Exception during EMI table extraction", e);
            ExtentReportUtil.getTest().log(Status.FAIL, "Failed to extract EMI table: " + e.getMessage());
            Assert.fail("EMI table export failed: " + e.getMessage());
        }
    }

}
