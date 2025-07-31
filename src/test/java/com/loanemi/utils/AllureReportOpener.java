package com.loanemi.utils;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for generating and opening Allure reports automatically
 * after test suite execution. Assumes 'allure' CLI is available in system PATH.
 */
public class AllureReportOpener {

    private static final Logger logger = LogManager.getLogger("SetupLogger");
 // allurePath=C:\\Users\\2407257\\Downloads\\allure-2.34.1\\bin\\allure.bat
    private static final String allureExecutable = ConfigReader.getProperty("allurePath");
    
    
    /**
     * Generates Allure report from test result data and opens it in the default browser.
     * Uses ProcessBuilder to execute 'allure generate' and 'allure open' commands.
     * Logs status and handles exceptions gracefully.
     */
    public static void openAllureReport() {

        try {
            // Step 1: Generate the Allure report from results directory
            ProcessBuilder generate = new ProcessBuilder(
            	allureExecutable, "generate", "target/allure-results", "-o", "target/allure-report", "--clean"
            );
            generate.inheritIO(); // Shares console output with parent process
            logger.info("TestNG: Generating Allure report... {}", generate.command());

            Process genProcess = generate.start(); // Launch report generation
            genProcess.waitFor(); // Wait until report generation completes
            logger.info("TestNG: Allure report generated successfully");

            // Step 2: Open the generated report in default system browser
            ProcessBuilder open = new ProcessBuilder(
            	allureExecutable, "open", "target/allure-report"
            );
            open.inheritIO();
            logger.info("TestNG: Opening Allure report... {}", open.command());

            Process openProcess = open.start(); // Launch browser
            openProcess.waitFor(); // Block until open process completes
            logger.info("TestNG: Allure report opened successfully");

        } catch (IOException | InterruptedException e) {
            logger.error("TestNG: Failed to generate or open Allure report", e);

            // Preserve interrupt flag for graceful shutdown behavior
            Thread.currentThread().interrupt();
        }
    }
}
