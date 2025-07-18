package com.loanemi.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for capturing browser screenshots during test execution.
 * Saves timestamped PNG files in a dedicated /screenshots folder inside the project root.
 * Useful for visual debugging, test failure analysis, and report attachments.
 */
public class ScreenshotUtil {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);

    /**
     * Captures a screenshot of the current browser window and saves it without returning a path.
     * Filename includes timestamp to avoid collisions.
     *
     * @param driver         The active WebDriver instance
     * @param screenshotName The label for the screenshot (appended before timestamp)
     */
    public static void captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Take screenshot and obtain file reference
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            // Ensure screenshots directory exists
            String directoryPath = System.getProperty("user.dir") + "/screenshots";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
                logger.debug("Screenshot directory created at: {}", directoryPath);
            }

            // Generate unique filename using timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filePath = directoryPath + "/" + screenshotName + "_" + timeStamp + ".png";

            // Save screenshot to disk
            File dest = new File(filePath);
            FileUtils.copyFile(src, dest);

            logger.info("Screenshot captured and saved: {}", dest.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to capture screenshot '{}': {}", screenshotName, e.getMessage(), e);
        }
    }

    /**
     * Captures a screenshot and returns the full file path for use in reports or attachments.
     *
     * @param driver         The active WebDriver instance
     * @param screenshotName The label for the screenshot (appended before timestamp)
     * @return               The absolute file path of the saved screenshot
     */
    public static String captureScreenshotWithPath(WebDriver driver, String screenshotName) {
        String filePath = "";
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String directoryPath = System.getProperty("user.dir") + "/screenshots";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
                logger.debug("Screenshot directory created at: {}", directoryPath);
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            filePath = directoryPath + "/" + screenshotName + "_" + timeStamp + ".png";

            File dest = new File(filePath);
            FileUtils.copyFile(src, dest);

            logger.info("Screenshot with path captured: {}", dest.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to capture screenshot with path '{}': {}", screenshotName, e.getMessage(), e);
        }
        return filePath;
    }
}
