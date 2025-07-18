package com.loanemi.utils;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for generating and opening Allure reports automatically
 * after test suite execution. Assumes 'allure' CLI is available in system PATH.
 */
public class AllureReportOpener {

    private static final Logger logger = LogManager.getLogger("SetupLogger");
    
    // allurePath=C:allure-2.34.1\\bin\\allure.bat
    private static final String allureExecutable = ConfigReader.getProperty("allurePath");

    /**
     * Generates Allure report from test result data and opens it in the default browser.
     * Uses ProcessBuilder to execute 'allure generate' and 'allure open' commands.
     * Logs status and handles exceptions gracefully.
     */
    public static void openAllureReport() {
    	if (allureExecutable == null || !new File(allureExecutable).exists()) {
            logger.error("Allure: Allure.bat file not found or 'allurePath' not set properly in config.properties: {}", allureExecutable);
            return;
        }
    	
        try {
            // Step 1: Generate the Allure report from results directory
            ProcessBuilder generate = new ProcessBuilder(
            	allureExecutable, "generate", "target/allure-results", "-o", "target/allure-report", "--clean"
            );
            generate.inheritIO(); // Shares console output with parent process
            logger.info("Allure: Generating Allure report... {}", generate.command());

            Process genProcess = generate.start(); // Launch report generation
            genProcess.waitFor(); // Wait until report generation completes
            logger.info("Allure: Allure report generated successfully");

            // Step 2: Open the generated report in default system browser
            ProcessBuilder open = new ProcessBuilder(
            	allureExecutable, "open", "target/allure-report"
            );
            open.inheritIO();
            logger.info("Allure: Opening Allure report... {}", open.command());

            Process openProcess = open.start(); // Launch browser
            openProcess.waitFor(); // Block until open process completes
            logger.info("Allure: Allure report opened successfully");

        } catch (IOException | InterruptedException e) {
            logger.error("Allure: Failed to generate or open Allure report", e);

            // Preserve interrupt flag for graceful shutdown behavior
            Thread.currentThread().interrupt();
        }
    }
}
