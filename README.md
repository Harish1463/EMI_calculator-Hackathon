# 📘 Find the Interest Amount for current year

## 🛠️ Tech Stack
- Java (JDK 17+)  
- Selenium WebDriver  
- TestNG  
- Maven  
- ExtentReports  
- Apache POI (Excel Integration)  
- Log4j2  
- GitHub  

---

## 🎯 Project Objective

Automate the end-to-end validation of a comprehensive EMI calculator platform which includes Car Loans, Home Loans, Credit Card EMIs, and advanced Loan Calculator tools. The project ensures data accuracy, UI integrity, and responsive behavior across multiple calculation modules. It was designed to simulate real-world testing workflows, extract financial data, and verify slider-field synchronization.

---

## 🧮 Key Scenarios Automated

1. Car Loan EMI Calculation
   - Input loan amount, interest rate, and tenure  
   - Assert calculated EMI and breakdown of monthly principal & interest

2. Home Loan EMI Extraction
   - Fill extensive input fields with dynamic data  
   - Extract EMI summary and year-on-year amortization data to Excel  
   - Validate monthly financial components including insurance and taxes

3. Credit Card EMI Calculation
   - Submit transactional inputs and calculate EMI with GST  
   - Compare result with expected EMI using financial formula

4. Loan Calculator Suite
   - EMI Calculator: Validate EMI output and summary  
   - Loan Amount Calculator: Calculate principal amount  
   - Loan Tenure Calculator: Determine correct repayment duration  
   - Interest Rate Calculator: Reverse-calculate rate using EMI and tenure  

5. Field Validation of Loan Calculator
   - Verify field and slider responsiveness  
   - Test toggle behavior (EMI Advance/Arrears, Tenure in Months/Years)  

---

## 🧪 Automation Capabilities

- Validation of transactions & do calculations;
- Extract table values & store in excel
- Filling data in screen & multiple UI validations
- Navigation from Menus
- Reusable methods
- Scrolling down in web page 

---

## 📸 Reporting

- Allure Reports integrated for visual test insights  
- Cucumber BDD offers step-wise clarity for test scenarios  
- Log files for detailed execution trace  

---

## 🧑‍💻 Contribution & Workflow

This was built as part of a team project during a hackathon. 
Contributions included:
- Designing test architecture  
- Implementing test cases  
- Debugging failed executions  
- Reporting test outcomes  

Roles included:  
- Architecture design and framework setup  
- Page object modeling and suite configuration  
- Test documentation: scenarios, cases, RTM  
- Debugging execution issues and validating test data  

---

## 🚀 How to Run Locally
 
1. Clone the repo: `git clone https://github.com/---------------------.git`  
2. Open the project in Eclipse or IntelliJ  
3. Install dependencies via Maven: `mvn clean install`  
4. Run tests using TestNG or Cucumber runner  
5. Generate reports: `mvn allure:serve`  