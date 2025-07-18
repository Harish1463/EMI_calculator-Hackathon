package com.loanemi.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.loanemi.base.DriverSetup;
import com.loanemi.hooks.CucumberHooks;
import com.loanemi.pages.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

/**
 * Cucumber step definitions for EMI calculators including Car, Home,
 * Credit Card, and general Loan calculators.
 */
public class LoanSteps {

    private static final Logger logger = LogManager.getLogger("CucumberLogger");

    // WebDriver instance retrieved from Hooks via DriverSetup
    WebDriver driver = DriverSetup.getDriver(CucumberHooks.getDriver());

    // ======= Car Loan EMI Steps =======
    
    @Given("The user opens the EMI calculator site")
    public void openEmiCalculatorSite() {
        // Site already opened via Hooks setup
        logger.info("Cucumber: Launched EMI calculator site via Hooks");
    }

    @When("The user calculates the car loan EMI")
    public void calculateCarLoanEMI() {
        new CarLoanPage(driver).calculateCarLoan(); // Executes full calculation
        logger.info("Cucumber: Executed car loan EMI calculation");
    }

    @Then("The user should see the car loan EMI and interest")
    public void verifyCarLoanResults() {
        // Validation handled inside calculateCarLoan()
        logger.info("Cucumber: Car loan EMI and interest were validated within the page method");
    }

    // ======= Home Loan EMI Steps =======

    @When("The user calculates and extracts Home Loan EMI data")
    public void calculateHomeLoanEMI() {
        new HomeLoanPage(driver).calculateAndExtractHomeLoanEMI();
        logger.info("Cucumber: Executed home loan EMI extraction and summary");
    }

    @Then("The user should see the Home Loan summary and amortization table")
    public void verifyHomeLoanResults() {
        // Full validation and Excel export handled in HomeLoanPage
        logger.info("Cucumber: Home loan summary and amortization were validated within the page method");
    }

    // ======= EMI Calculator (General Loan) =======

    @When("The user validates EMI calculator fields and sliders only")
    public void validateEMIFieldsOnly() {
        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();        // Open tab
        page.emiFieldValidation();              // Validate field-slider mapping
        logger.info("Cucumber: EMI calculator fields and sliders validated");
    }

    @Then("The user should see validated EMI fields and sliders")
    public void verifyEMIFields() {
        logger.info("Cucumber: EMI fields and sliders were confirmed in previous step");
    }

    @When("The user validates EMI calculator summary")
    public void validateEMIUI() {
        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.validateEMICalculatorUI();         // Validate EMI computation output
        logger.info("Cucumber: EMI calculator summary validated");
    }

    @Then("The user should see the EMI result summary")
    public void verifyEMISummary() {
        logger.info("Cucumber: EMI result summary confirmed in preceding method");
    }

    // ======= Loan Amount Calculator =======

    @When("The user validates Loan Amount calculator fields and sliders only")
    public void validateLoanAmountFieldsOnly() {
        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.loanAmountFieldValidation();       // Validate Loan Amount fields and sliders
        logger.info("Cucumber: Loan Amount calculator fields and sliders validated");
    }

    @Then("The user should see validated Loan Amount fields and sliders")
    public void verifyLoanAmountFields() {
        logger.info("Cucumber: Loan Amount fields and sliders confirmed in previous step");
    }

    @When("The user validates Loan Amount calculator summary")
    public void validateLoanAmountUI() {
        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.validateLoanAmountCalculatorUI();  // Validate computed amount
        logger.info("Cucumber: Loan Amount calculator summary validated");
    }

    @Then("The user should see the Loan Amount result summary")
    public void verifyLoanAmountSummary() {
        logger.info("Cucumber: Loan Amount result summary verified during UI validation");
    }

    // ======= Loan Tenure Calculator =======

    @When("The user validates Loan Tenure calculator fields and sliders only")
    public void validateTenureFieldsOnly() {
        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.loanTenureFieldValidation();
        logger.info("Cucumber: Loan Tenure calculator fields and sliders validated");
    }

    @Then("The user should see validated Loan Tenure fields and sliders")
    public void verifyLoanTenureFields() {
        logger.info("Cucumber: Loan Tenure fields and sliders verified in previous step");
    }

    @When("The user validates Loan Tenure calculator summary")
    public void validateTenureUI() {
        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.validateLoanTenureCalculatorUI();  // Validate tenure result
        logger.info("Cucumber: Loan Tenure calculator summary validated");
    }

    @Then("The user should see the Loan Tenure result summary")
    public void verifyLoanTenureSummary() {
        logger.info("Cucumber: Loan Tenure result summary verified during UI validation");
    }

    // ======= Interest Rate Calculator =======

    @When("The user validates Interest Rate calculator fields and sliders only")
    public void validateInterestFieldsOnly() {
        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.interestRateFieldValidation();     // Validate interest slider and inputs
        logger.info("Cucumber: Interest Rate calculator fields and sliders validated");
    }

    @Then("The user should see validated Interest Rate fields and sliders")
    public void verifyInterestFields() {
        logger.info("Cucumber: Interest Rate fields and sliders verified in previous step");
    }

    @When("The user validates Interest Rate calculator summary")
    public void validateInterestUI() {
        LoanCalculatorPage page = new LoanCalculatorPage(driver);
        page.navigateToLoanCalculator();
        page.validateInterestRateCalculatorUI(); // Validate rate output
        logger.info("Cucumber: Interest Rate calculator summary validated");
    }

    @Then("The user should see the Interest Rate result summary")
    public void verifyInterestRateSummary() {
        logger.info("Cucumber: Interest Rate result summary confirmed during UI validation");
    }

    // ======= Credit Card EMI Calculator =======

    @When("The user calculates the credit card EMI")
    public void calculateCreditCardEMI() {
        CreditCardEMICalculatorPage creditPage = new CreditCardEMICalculatorPage(driver);
        creditPage.navigateToCreditCardEMICalculator(); // Open calculator tab
        creditPage.calculateAndFetchEMI();              // Fill inputs & validate
        logger.info("Cucumber: Executed credit card EMI calculation");
    }

    @Then("The user should see the credit card EMI value")
    public void verifyCreditCardEMIResult() {
        logger.info("Cucumber: Credit card EMI value confirmed in previous step");
    }
}
