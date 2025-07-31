package com.loanemi.rerun;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer is used to automatically rerun failed TestNG tests.
 * It allows up to 'maxRetryCount' reattempts per failed test before marking it as final fail.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger("RetryLogger");

    // Current retry attempt count
    private int retryCount = 0;

    // Maximum number of retry attempts allowed
    private final int maxRetryCount = 3;

    /**
     * Invoked by TestNG when a test fails.
     * If retryCount < maxRetryCount, it logs the retry and returns true to trigger another execution.
     * Once maxRetryCount is reached, it logs a message and does not retry.
     *
     * @param result ITestResult representing the failed test
     * @return boolean true to retry, false to stop
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            logger.warn("Retrying test: '{}' | Attempt: {}/{}", result.getName(), retryCount, maxRetryCount);
            return true;
        } else {
            logger.info("Max retry limit reached for test: '{}'. No further attempts.", result.getName());
            return false;
        }
    }
}
