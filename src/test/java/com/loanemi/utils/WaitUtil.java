package com.loanemi.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for managing Selenium wait strategies.
 * Centralizes reusable methods for implicit and explicit waits,
 * improving readability and reducing flaky tests.
 */
public class WaitUtil {

    private static final Logger logger = LogManager.getLogger(WaitUtil.class);

    // Default timeout duration used across explicit waits
    private static final int DEFAULT_TIMEOUT = 10;

    /**
     * Applies a global implicit wait to the WebDriver session.
     * Used to define default polling interval for locating elements.
     *
     * @param driver  WebDriver instance
     * @param seconds Duration for wait in seconds
     */
    public static void applyImplicitWait(WebDriver driver, long seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
        logger.info("Implicit wait applied for {} seconds", seconds);
    }

    /**
     * Waits until the specified WebElement is visible in the DOM.
     *
     * @param driver  WebDriver instance
     * @param element Target WebElement to monitor
     * @return        WebElement once visible
     */
    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        logger.debug("Waiting for visibility of WebElement: {}", element);
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until an element located by a specific locator becomes visible.
     *
     * @param driver  WebDriver instance
     * @param locator Locator representing the target element
     * @return        WebElement once visible
     */
    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        logger.debug("Waiting for visibility of element located by: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the specified WebElement is clickable.
     * Useful before executing click actions.
     *
     * @param driver  WebDriver instance
     * @param element Target WebElement to wait for
     * @return        WebElement once clickable
     */
    public static WebElement waitForClickability(WebDriver driver, WebElement element) {
        logger.debug("Waiting for clickability of WebElement: {}", element);
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until an element located by a specific locator becomes clickable.
     *
     * @param driver  WebDriver instance
     * @param locator Locator of the element
     * @return        WebElement once clickable
     */
    public static WebElement waitForClickability(WebDriver driver, By locator) {
        logger.debug("Waiting for clickability of element located by: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }
}
