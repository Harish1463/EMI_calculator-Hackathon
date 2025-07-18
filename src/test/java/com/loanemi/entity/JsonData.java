package com.loanemi.entity;

/**
 * JsonData serves as a data model class for retrieving structured values from input JSON test files.
 * It supports various test cases including:
 * - TC01: Basic EMI calculation
 * - TC02: Home loan EMI breakdown and extra parameters
 * - TC03–TC10: Dynamic UI field and slider validations across loan calculators
 * 
 * Note: Some variables are shared across scenarios but commented out to avoid field collisions 
 *       during deserialization. Re-enable if scenarios are refactored independently.
 */
public class JsonData {

    // ==== TC01: Basic Car Loan EMI Parameters ====
    private String amount;   // Loan amount
    private String rate;     // Interest rate
    private String years;    // Tenure in years

    public String getAmount() {
        return amount;
    }

    public String getRate() {
        return rate;
    }

    public String getYears() {
        return years;
    }

    // ==== TC02: Home Loan Parameters and Expected Values ====
    private String homeValue;         // Property value
    private String downPayment;       // Down payment percentage
    private String insurance;         // Loan insurance amount
    // private String amount;         // Loan amount (commented due to overlap with TC01)
    // private String rate;           // Interest rate (commented due to overlap with TC01)
    private String tenure;            // Loan tenure in years
    private String fees;              // Processing fees percentage
    private String oneTimeExpense;    // One-time expenses (₹)
    private String tax;               // Property tax percentage
    private String insurancePerYear;  // Annual home insurance percentage
    private String maintenance;       // Monthly maintenance cost
    private String monthly;           // Extra monthly payment
    private String yearly;            // Extra yearly payment
    private String quarterly;         // Extra quarterly payment
    private String oneTimeOnly;       // Extra one-time payment

    // Expected summary components from calculator (for validation)
    private String expectedEmi;
    private String expectedExtra;
    private String expectedTax;
    private String expectedInsurance;
    private String expectedMaintenance;
    private String expectedTotal;

    public String getHomeValue() { return homeValue; }
    public String getDownPayment() { return downPayment; }
    public String getInsurance() { return insurance; }
    public String getTenure() { return tenure; }
    public String getFees() { return fees; }
    public String getOneTimeExpense() { return oneTimeExpense; }
    public String getTax() { return tax; }
    public String getInsurancePerYear() { return insurancePerYear; }
    public String getMaintenance() { return maintenance; }
    public String getMonthly() { return monthly; }
    public String getYearly() { return yearly; }
    public String getQuarterly() { return quarterly; }
    public String getOneTimeOnly() { return oneTimeOnly; }

    public String getExpectedEmi() { return expectedEmi; }
    public String getExpectedExtra() { return expectedExtra; }
    public String getExpectedTax() { return expectedTax; }
    public String getExpectedInsurance() { return expectedInsurance; }
    public String getExpectedMaintenance() { return expectedMaintenance; }
    public String getExpectedTotal() { return expectedTotal; }

    // ==== TC03–TC10: UI Field & Slider Mapping Parameters ====
    // private String amount;     // Reused field from TC01 — comment to avoid conflict
    // private String rate;       // Reused field from TC01
    // private String tenure;     // Reused field from TC02
    // private String fees;       // Reused field from TC02

    private String emi;              // EMI input value
    private String maxAmount;        // Max value for amount slider
    private String maxRate;          // Max value for interest slider
    private String maxTenure;        // Max value for tenure slider
    private String maxFees;          // Max value for fees slider
    private String maxEmi;           // Max value for EMI slider

    private String amountName;       // Label or display name for amount field
    private String rateName;         // Label for interest field
    private String tenureName;       // Label for tenure field
    private String feesName;         // Label for fees field
    private String emiName;          // Label for EMI field

    // private String expectedEmi;   // Already defined in TC02
    private String expectedAmount;   // Expected value after calculation
    private String expectedTenure;   // Expected tenure output
    private String expectedRate;     // Expected interest rate output

    public String getEmi() { return emi; }
    public String getMaxAmount() { return maxAmount; }
    public String getMaxRate() { return maxRate; }
    public String getMaxTenure() { return maxTenure; }
    public String getMaxFees() { return maxFees; }
    public String getMaxEmi() { return maxEmi; }

    public String getAmountName() { return amountName; }
    public String getRateName() { return rateName; }
    public String getTenureName() { return tenureName; }
    public String getFeesName() { return feesName; }
    public String getEmiName() { return emiName; }

    public String getExpectedAmount() { return expectedAmount; }
    public String getExpectedTenure() { return expectedTenure; }
    public String getExpectedRate() { return expectedRate; }
}
