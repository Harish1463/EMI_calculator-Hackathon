package com.loanemi.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.loanemi.entity.JsonData;
import com.loanemi.utils.ConfigReader;
import com.loanemi.utils.JsonUtil;
import com.loanemi.utils.WaitUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Page Object Model for Loan Calculator tab.
 * Covers EMI, Loan Amount, Tenure, and Interest Rate calculators with slider and form field validations.
 */
public class LoanCalculatorPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger("LoanCalculatorLogger");

    // Load test data from JSON using property key
    private String testData = ConfigReader.getProperty("testCase3To10");
    private String jsonFilePath = ConfigReader.getProperty("jsonFilePath");
    JsonData testCase = JsonUtil.getTestCase(jsonFilePath, testData);

    /**
     * Constructor initializes PageFactory elements and logs instantiation
     */
    public LoanCalculatorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("Initialized LoanCalculatorPage with testData key '{}'", testData);
    }

    // ========== Navigation Menu ==========
    // Main dropdown menu for accessing loan calculators
    @FindBy(xpath = "//a[@title='Loan Calculators, Widgets & Rates']")
    private WebElement loanCalculatorMenu;

    // Sub-tab specifically for Loan Calculator suite
    @FindBy(xpath = "//ul[@class='dropdown-menu show']//a[text()='Loan Calculator']")
    private WebElement loanCalculatorTab;

    // ========== Calculator Tabs ==========
    // EMI Calculation tab
    @FindBy(id = "emi-calc")
    private WebElement emiCalculatorTab;

    // Principal Loan Amount calculation tab
    @FindBy(id = "loan-amount-calc")
    private WebElement loanAmountCalculatorTab;

    // Loan Tenure calculation tab
    @FindBy(id = "loan-tenure-calc")
    private WebElement loanTenureCalculatorTab;
    
    // Interest Rate calculation tab
    @FindBy(id = "interest-rate-calc")
    private WebElement interestRateCalculatorTab;

    // ========== Input Fields ==========
    // Amount of loan (₹)
    @FindBy(id = "loanamount")
    private WebElement loanAmountInput;

    // Interest rate in %
    @FindBy(id = "loaninterest")
    private WebElement interestRateInput;

    // Tenure period (years/months)
    @FindBy(id = "loanterm")
    private WebElement loanTenureInput;

    // Processing fees in %
    @FindBy(id = "loanfees")
    private WebElement loanFeesInput;

    // EMI input (used in reverse calculations)
    @FindBy(id = "loanemi")
    private WebElement loanEmiInput;

    // ========== Sliders ==========
    // Slider for loan amount field
    @FindBy(id = "loanamountslider")
    private WebElement loanAmountSlider;

    // Slider for interest rate field
    @FindBy(id = "loaninterestslider")
    private WebElement interestSlider;

    // Slider for loan tenure
    @FindBy(id = "loantermslider")
    private WebElement tenureSlider;

    // Slider for loan processing fees
    @FindBy(id = "loanfeesslider")
    private WebElement feesSlider;

    // Slider for EMI input
    @FindBy(id = "loanemislider")
    private WebElement loanEmiSlider;

    // ========== Tenure Toggle ==========
    // Toggle to switch tenure units to years
    @FindBy(xpath = "//input[@id='loanyears']/parent::label")
    private WebElement tenureYearsRadio;

    // Toggle to switch tenure units to months
    @FindBy(xpath = "//input[@id='loanmonths']/parent::label")
    private WebElement tenureMonthsRadio;

    // ========== EMI Scheme Toggle ==========
    // EMI payment in advance option
    @FindBy(xpath = "//input[@id='emiadvance']/parent::label")
    private WebElement emiAdvanceRadio;

    // EMI payment in arrears option
    @FindBy(xpath = "//input[@id='emiarrears']/parent::label")
    private WebElement emiArrearsRadio;
    
    // ========== Summary Results ==========
    // Summary of calculated EMI
    @FindBy(xpath = "//div[@id='loansummary-emi']//p")
    private WebElement loanEmiSummary;

    // Summary of principal loan amount
    @FindBy(xpath = "//div[@id='loansummary-loanamount']//p")
    private WebElement principalLoanAmountSummary;

    // Summary of loan tenure
    @FindBy(xpath = "//div[@id='loansummary-tenure']//p")
    private WebElement loanTenureSummary;

    // Summary of interest rate
    @FindBy(xpath = "//div[@id='loansummary-interestrate']//p")
    private WebElement interestRateSummary;

    // Summary of calculated APR
    @FindBy(xpath = "//div[@id='loansummary-apr']//p")
    private WebElement loanAprSummary;

    // Summary of total interest payable
    @FindBy(xpath = "//div[@id='loansummary-totalinterest']//p")
    private WebElement totalInterestSummary;

    // Summary of total payment (principal + interest + fees)
    @FindBy(xpath = "//div[@id='loansummary-totalamount']//p")
    private WebElement totalPaymentSummary;


    /**
     * Navigates to the Loan Calculator section from the main menu.
     * Triggers tab display and prepares the page for validation interactions.
     */
    public void navigateToLoanCalculator() {
        logger.info("Navigating to Loan Calculator tab");
        WaitUtil.waitForClickability(driver, loanCalculatorMenu).click(); // Open calculator dropdown
        logger.debug("Clicked on Loan Calculator menu");
        WaitUtil.waitForClickability(driver, loanCalculatorTab).click();  // Select Loan Calculator tab
        logger.debug("Clicked on Loan Calculator tab");
        
    }

    /**
     * Validates the EMI Calculator UI and its computed summary.
     * Also selects EMI scheme toggle and logs expected vs actual comparison.
     */
    public void validateEMICalculatorUI() {
        logger.info("Validating EMI Calculator UI");
        WaitUtil.waitForClickability(driver, emiCalculatorTab).click(); // Activate EMI tab
        logger.debug("Clicked on EMI Calculator tab");
        
     // Uncomment to enable detailed field-slider validation
//        validateCalculatorUI(
//            loanAmountInput, interestRateInput, loanTenureInput, loanFeesInput, null,
//            loanAmountSlider, interestSlider, tenureSlider, feesSlider, null,
//            true
//        );

        WaitUtil.waitForClickability(driver, emiArrearsRadio).click(); // Select 'EMI in Arrears'
        logger.debug("Selected 'EMI in Arrears' option");

        logger.info("Validating loan summary for EMI Calculator, expected EMI: ₹{}", testCase.getExpectedEmi());
        validateLoanSummary("EMI Calculator", Double.parseDouble(testCase.getExpectedEmi())); // Custom summary check

        logger.debug("Loan EMI fetched: {}", loanEmiSummary.getText());
    }

    /**
     * Validates the Loan Amount Calculator module.
     * It checks field visibility and loan summary output against expectations.
     */
    public void validateLoanAmountCalculatorUI() {
        logger.info("Validating Loan Amount Calculator UI");
        WaitUtil.waitForClickability(driver, loanAmountCalculatorTab).click();
        logger.debug("Clicked on Loan Amount Calculator tab");

     // Uncomment to enable detailed field-slider validation
//        validateCalculatorUI(
//            null, interestRateInput, loanTenureInput, loanFeesInput, loanEmiInput,
//            null, interestSlider, tenureSlider, feesSlider, loanEmiSlider,
//            true
//        );

        WaitUtil.waitForClickability(driver, emiArrearsRadio).click();
        logger.debug("Selected 'EMI in Arrears' option");

        logger.info("Validating loan summary for Loan Amount Calculator, expected amount: ₹{}", testCase.getExpectedAmount());
        validateLoanSummary("Loan Amount Calculator", Double.parseDouble(testCase.getExpectedAmount()));

        logger.debug("Principal Loan Amount fetched: {}", principalLoanAmountSummary.getText());
    }

    /**
     * Validates the Loan Tenure Calculator module.
     * Confirms summary values match expectations from test data.
     */
    public void validateLoanTenureCalculatorUI() {
        logger.info("Validating Loan Tenure Calculator UI");
        WaitUtil.waitForClickability(driver, loanTenureCalculatorTab).click();
        logger.debug("Clicked on Loan Tenure Calculator tab");

     // Uncomment to enable detailed field-slider validation
//        validateCalculatorUI(
//            loanAmountInput, interestRateInput, null, loanFeesInput, loanEmiInput,
//            loanAmountSlider, interestSlider, null, feesSlider, loanEmiSlider,
//            false
//        );

        WaitUtil.waitForClickability(driver, emiArrearsRadio).click();
        logger.debug("Selected 'EMI in Arrears' option");

        logger.info("Validating loan summary for Loan Tenure Calculator, expected tenure: {} months", testCase.getExpectedTenure());
        validateLoanSummary("Loan Tenure Calculator", Double.parseDouble(testCase.getExpectedTenure()));

        logger.debug("Loan Tenure fetched: {}", loanTenureSummary.getText());
    }

    /**
     * Validates the Interest Rate Calculator module.
     * Checks computed interest output against expected value from test data.
     */
    public void validateInterestRateCalculatorUI() {
        logger.info("Validating Interest Rate Calculator UI");
        WaitUtil.waitForClickability(driver, interestRateCalculatorTab).click();
        logger.debug("Clicked on Interest Rate Calculator tab");

     // Uncomment to enable detailed field-slider validation
//        validateCalculatorUI(
//            loanAmountInput, null, loanTenureInput, loanFeesInput, loanEmiInput,
//            loanAmountSlider, null, tenureSlider, feesSlider, loanEmiSlider,
//            true
//        );

        WaitUtil.waitForClickability(driver, emiArrearsRadio).click();
        logger.debug("Selected 'EMI in Arrears' option");

        logger.info("Validating loan summary for Interest Rate Calculator, expected rate: {}%", testCase.getExpectedRate());
        validateLoanSummary("Interest Rate Calculator", Double.parseDouble(testCase.getExpectedRate()));

        logger.debug("Loan Interest Rate fetched: {}", interestRateSummary.getText());
    }


    /**
     * Validates field-to-slider synchronization for EMI Calculator.
     * Includes loan amount, interest rate, tenure, and fees.
     */
    public void emiFieldValidation() {
        logger.info("Validating field and slider mappings for EMI Calculator");
        // WaitUtil.waitForClickability(driver, emiCalculatorTab).click();
        // Directly invokes UI validation without extra wait since tab is pre-clicked elsewhere
        validateCalculatorUI(
            loanAmountInput, interestRateInput, loanTenureInput, loanFeesInput, null,
            loanAmountSlider, interestSlider, tenureSlider, feesSlider, null,
            true
        );
    }

    /**
     * Validates field-to-slider synchronization for Loan Amount Calculator.
     * Activates tab, then validates interest, tenure, fees, and EMI input.
     */
    public void loanAmountFieldValidation() {
        logger.info("Validating field and slider mappings for Loan Amount Calculator");

        // Required only for activating the tab once before validating
        WaitUtil.waitForClickability(driver, loanAmountCalculatorTab).click();
        logger.debug("Clicked on Loan Amount Calculator tab");

        validateCalculatorUI(
            null, interestRateInput, loanTenureInput, loanFeesInput, loanEmiInput,
            null, interestSlider, tenureSlider, feesSlider, loanEmiSlider,
            true
        );
    }

    /**
     * Validates field-to-slider synchronization for Loan Tenure Calculator.
     * Covers amount, interest, fees, and EMI components.
     */
    public void loanTenureFieldValidation() {
        logger.info("Validating field and slider mappings for Loan Tenure Calculator");

        // Tab activation is required before accessing input fields
        WaitUtil.waitForClickability(driver, loanTenureCalculatorTab).click();
        logger.debug("Clicked on Loan Tenure Calculator tab");

        validateCalculatorUI(
            loanAmountInput, interestRateInput, null, loanFeesInput, loanEmiInput,
            loanAmountSlider, interestSlider, null, feesSlider, loanEmiSlider,
            false
        );
    }

    /**
     * Validates field-to-slider synchronization for Interest Rate Calculator.
     * Covers loan amount, tenure, fees, and EMI fields.
     */
    public void interestRateFieldValidation() {
        logger.info("Validating field and slider mappings for Interest Rate Calculator");

        // Needed to load dynamic content of Interest Rate tab
        WaitUtil.waitForClickability(driver, interestRateCalculatorTab).click();
        logger.debug("Clicked on Interest Rate Calculator tab");

        validateCalculatorUI(
            loanAmountInput, null, loanTenureInput, loanFeesInput, loanEmiInput,
            loanAmountSlider, null, tenureSlider, feesSlider, loanEmiSlider,
            true
        );
    }


    /**
     * Reusable validator that sets input fields, verifies slider mappings,
     * and toggles EMI and tenure options based on test case configuration.
     *
     * @param amountInput        Loan amount input field
     * @param interestInput      Interest rate input field
     * @param tenureInput        Tenure input field (years/months)
     * @param feesInput          Processing fees input field
     * @param emiInput           EMI input field (used for reverse calculations)
     * @param amountSlider       Loan amount slider
     * @param interestSlider     Interest rate slider
     * @param tenureSlider       Tenure slider
     * @param feesSlider         Fees slider
     * @param emiSlider          EMI slider
     * @param includeTenureToggle Indicates whether tenure toggle radio buttons should be validated
     */
    private void validateCalculatorUI(
            WebElement amountInput, WebElement interestInput, WebElement tenureInput, WebElement feesInput, WebElement emiInput,
            WebElement amountSlider, WebElement interestSlider, WebElement tenureSlider, WebElement feesSlider, WebElement emiSlider,
            boolean includeTenureToggle
    ) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        logger.info("Starting UI validation for current loan calculator tab");

        try {
            // Input field population using JavaScript injection for clean, fast entry
            if (amountInput != null) {
                js.executeScript("arguments[0].value='" + testCase.getAmount() + "';", amountInput);
                logger.debug("Set Loan Amount input: ₹{}", testCase.getAmount());
            }

            if (interestInput != null) {
                js.executeScript("arguments[0].value='" + testCase.getRate() + "';", interestInput);
                logger.debug("Set Interest Rate input: {}%", testCase.getRate());
            }

            if (tenureInput != null) {
                js.executeScript("arguments[0].value='" + testCase.getTenure() + "';", tenureInput);
                logger.debug("Set Tenure input: {} year(s)", testCase.getTenure());
            }

            if (feesInput != null) {
                js.executeScript("arguments[0].value='" + testCase.getFees() + "';", feesInput);
                logger.debug("Set Fees input: ₹{}", testCase.getFees());
            }

            if (emiInput != null && emiInput.isDisplayed()) {
                js.executeScript("arguments[0].value='" + testCase.getEmi() + "';", emiInput);
                logger.debug("Set EMI input: ₹{}", testCase.getEmi());
            }

        } catch (Exception e) {
            logger.error("Input fields not interactable: {}", e.getMessage());
            js.executeScript("arguments[0].scrollIntoView({block: 'end'});", feesSlider);
            Assert.fail("Failed to interact with input fields: " + e.getMessage());
        }

        // Slider validations for mapped inputs
        if (amountSlider != null) {
            logger.debug("Validating Amount slider position");
            validateSliderPosition(amountInput, amountSlider, Double.parseDouble(testCase.getMaxAmount()), testCase.getAmountName());
        }

        if (interestSlider != null) {
            logger.debug("Validating Interest slider position");
            validateSliderPosition(interestInput, interestSlider, Double.parseDouble(testCase.getMaxRate()), testCase.getRateName());
        }

        if (tenureSlider != null) {
            logger.debug("Validating Tenure slider position");
            validateSliderPosition(tenureInput, tenureSlider, Double.parseDouble(testCase.getMaxTenure()), testCase.getTenureName());
        }

        if (feesSlider != null) {
            logger.debug("Validating Fees slider position");
            validateSliderPosition(feesInput, feesSlider, Double.parseDouble(testCase.getMaxFees()), testCase.getFeesName());
        }

        if (emiSlider != null) {
            logger.debug("Validating EMI slider position");
            validateSliderPosition(emiInput, emiSlider, Double.parseDouble(testCase.getMaxEmi()), testCase.getEmiName());
        }

        // Tenure radio button toggle validation (if applicable)
        if (includeTenureToggle) {
            try {
                if (!tenureMonthsRadio.isSelected()) {
                    js.executeScript("arguments[0].click();", tenureMonthsRadio);
                    logger.debug("Selected Tenure as Months");
                }
                if (!tenureYearsRadio.isSelected()) {
                    js.executeScript("arguments[0].click();", tenureYearsRadio);
                    logger.debug("Selected Tenure as Years");
                }
            } catch (Exception e) {
                logger.error("Tenure toggle not interactable: {}", e.getMessage());
                js.executeScript("arguments[0].scrollIntoView({block: 'end'});", feesSlider);
                Assert.fail("Failed to toggle tenure radio buttons: " + e.getMessage());
            }
        }

        // EMI scheme toggle validation
        try {
            if (!emiAdvanceRadio.isSelected()) {
                emiAdvanceRadio.click();
                logger.debug("Selected EMI Advance option");
            }
            if (!emiArrearsRadio.isSelected()) {
                emiArrearsRadio.click();
                logger.debug("Selected EMI Arrears option");
            }
        } catch (Exception e) {
            logger.error("EMI toggle not interactable: {}", e.getMessage());
            js.executeScript("arguments[0].scrollIntoView({block: 'end'});", emiArrearsRadio);
            Assert.fail("Failed to toggle EMI radio buttons: " + e.getMessage());
        }

        logger.info("UI validation completed for current calculator tab");
    }


    /**
     * Validates whether a slider's handle aligns with the expected percentage position
     * based on the entered input value relative to the slider’s maximum supported value.
     *
     * If the deviation exceeds a defined threshold (10%), it fails the assertion.
     *
     * @param inputField Field whose value drives slider position
     * @param slider The corresponding slider WebElement
     * @param maxValue Maximum range value for the slider
     * @param fieldName Label used for logging context
     */
    private void validateSliderPosition(WebElement inputField, WebElement slider, double maxValue, String fieldName) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            logger.info("Validating slider position for field: {}", fieldName);

            // Scroll the slider into view to ensure visibility during test run
            js.executeScript("arguments[0].scrollIntoView(true);", slider);

            // Extract numeric value from input field, remove any formatting
            String rawValue = inputField.getAttribute("value").replaceAll("[^0-9.]", "").replace(",", "");
            double enteredValue = Double.parseDouble(rawValue);

            // Calculate expected slider position percentage based on value
            double expectedPercent = (enteredValue / maxValue) * 100;

            // Locate slider handle and get its 'style' attribute, which contains position
            WebElement handle = slider.findElement(By.className("ui-slider-handle"));
            String style = handle.getAttribute("style");
            double actualPercent = Double.parseDouble(style.replaceAll("[^0-9.]", ""));

            // Log expected and actual slider percentages
            logger.debug("{} slider → Entered: {}, Max: {}, Expected Position: ~{}%, Actual Position: {}%",
                         fieldName, enteredValue, maxValue, expectedPercent, actualPercent);

            // Acceptable tolerance for minor differences due to rendering
            double tolerance = 1.0;

            if (Math.abs(expectedPercent - actualPercent) <= tolerance) {
                logger.info("{} slider positioned correctly at ~{}%", fieldName, actualPercent);
            } else {
                logger.warn("{} slider mismatch: expected ~{}% but found {}%", fieldName, expectedPercent, actualPercent);
                if (Math.abs(expectedPercent - actualPercent) >= 10) {
                    logger.error("{} slider exceeds tolerance. Triggering failure.", fieldName);
                    Assert.fail(fieldName + " slider mismatch: expected ~" + expectedPercent + "% but found " + actualPercent + "%");
                }
            }

        } catch (Exception e) {
            logger.error("Exception during slider validation for {}: {}", fieldName, e.getMessage());

            // Scroll relevant area into view before failing
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'end'});", feesSlider);

            Assert.fail("Failed to validate " + fieldName + " slider: " + e.getMessage());
        }
    }

    
    /**
     * Validates loan summary result value against expected value for a given calculator type.
     * Compares parsed UI text with test data value and logs detailed result for audit/debug.
     *
     * @param calculatorType Identifies which calculator's result to validate
     * @param expectedValue The expected numeric value to verify against
     */
    private void validateLoanSummary(String calculatorType, double expectedValue) {
        try {
            logger.info("Validating loan summary for calculator: {}", calculatorType);

            double actualValue = 0;
            String label = calculatorType;

            // Retrieve actual value based on calculator type
            if (calculatorType.contains("EMI")) {
                actualValue = Double.parseDouble(loanEmiSummary.getText().replaceAll("[^\\d.]", ""));
            } else if (calculatorType.contains("Loan Amount")) {
                actualValue = Double.parseDouble(principalLoanAmountSummary.getText().replaceAll("[^\\d.]", ""));
            } else if (calculatorType.contains("Loan Tenure")) {
                actualValue = Double.parseDouble(loanTenureSummary.getText().replaceAll("[^\\d.]", ""));
            } else if (calculatorType.contains("Interest Rate")) {
                actualValue = Double.parseDouble(interestRateSummary.getText().replaceAll("[^\\d.]", ""));
            }

            logger.debug("Expected {} value: {}, Actual value: {}", label, expectedValue, actualValue);

            // Validate within ±1 tolerance to accommodate minor rounding differences
            if (Math.abs(expectedValue - actualValue) <= 1) {
                logger.info("{} validated successfully: Expected ≈ {}, Actual = {}", label, expectedValue, actualValue);
            } else {
                logger.warn("{} mismatch: Expected = {}, Actual = {}", label, expectedValue, actualValue);

                // Scroll error context into view and trigger failure
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start'});", emiArrearsRadio);
                logger.error("{} validation failed. Triggering assertion failure.", label);

                Assert.fail(label + " validation failed.");
            }

        } catch (Exception e) {
            logger.error("Exception during {} summary validation: {}", calculatorType, e.getMessage());

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start'});", emiArrearsRadio);
            Assert.fail("Could not validate summary field: " + e.getMessage());
        }
    }

}
