package com.loanemi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Utility class for initializing and managing ExtentReports.
 * Creates timestamped HTML reports with customizable themes and titles.
 */
public class ExtentReportUtil {

    private static final Logger logger = LogManager.getLogger(ExtentReportUtil.class);

    private static ExtentReports extent;
    private static ExtentTest test;
    private static ExtentSparkReporter spark;

    /**
     * Initializes and configures ExtentReports with timestamped file.
     */
    public static ExtentReports initReport() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = System.getProperty("user.dir") + "/reports/LoanEMI_Report_" + timeStamp + ".html";

        spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("Loan EMI Automation Report");
        spark.config().setDocumentTitle("Test Execution Report");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm:ss a '('zzz')'");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        logger.info("ExtentReports initialized successfully at path: {}", reportPath);
        return extent;
    }

    /**
     * Creates a new test entry in the report.
     */
    public static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        logger.info("ExtentTest created for scenario: {}", testName);
        return test;
    }

    /**
     * Retrieves the current ExtentTest instance.
     */
    public static ExtentTest getTest() {
        return test;
    }

    /**
     * Flushes report to disk if initialized.
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed and written to disk.");
        } else {
            logger.warn("Attempted to flush ExtentReports, but instance was null.");
        }
    }
}
