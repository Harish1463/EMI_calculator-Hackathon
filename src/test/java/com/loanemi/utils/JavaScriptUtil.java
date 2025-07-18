package com.loanemi.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for performing JavaScript-based operations within Selenium.
 * Useful for interacting with input fields that are dynamically controlled or masked.
 */
public class JavaScriptUtil {

    private static final Logger logger = LogManager.getLogger(JavaScriptUtil.class);
    private static JavascriptExecutor js;

    /**
     * Sets a value inside a form field using direct JavaScript manipulation.
     * Also dispatches typical DOM events to simulate manual input (input, change, blur).
     *
     * @param driver  WebDriver instance to cast as JavascriptExecutor
     * @param element WebElement representing the input field
     * @param value   String value to be injected into the field
     */
    public static void setInputValue(WebDriver driver, WebElement element, String value) {
        try {
            js = (JavascriptExecutor) driver;
            js.executeScript(
                "arguments[0].value='" + value + "';" +
                "arguments[0].dispatchEvent(new Event('input'));" +
                "arguments[0].dispatchEvent(new Event('change'));" +
                "arguments[0].dispatchEvent(new Event('blur'));",
                element
            );
            logger.info("JavaScript setInputValue triggered on element with value: '{}'", value);
        } catch (Exception e) {
            logger.error("JavaScript setInputValue failed for value '{}'", value, e);
        }
    }
}
