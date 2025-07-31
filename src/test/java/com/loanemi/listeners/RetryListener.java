package com.loanemi.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.loanemi.rerun.RetryAnalyzer;

/**
 * RetryListener is a TestNG listener class that dynamically applies retry logic
 * to test methods using the IAnnotationTransformer interface.
 * 
 * It automatically binds the custom RetryAnalyzer to every @Test method so that 
 * failed tests will be rerun based on configured retry conditions.
 */
public class RetryListener implements IAnnotationTransformer {

    private static final Logger logger = LogManager.getLogger(RetryListener.class);

    /**
     * This method is invoked by TestNG before the test method is run.
     * It attaches the RetryAnalyzer class to each test method dynamically.
     * 
     * @param annotation  The test annotation being processed
     * @param testClass   Class containing the test
     * @param constructor Constructor of the test class (can be null)
     * @param method      Specific test method to which retry logic is applied
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor constructor, Method method) {
        
        // Set RetryAnalyzer class to enable auto-rerun for failed tests
        annotation.setRetryAnalyzer(RetryAnalyzer.class);

        // Log the method that receives retry logic
        logger.debug("RetryAnalyzer applied to test method: {}.{}", 
                     method.getDeclaringClass().getSimpleName(), method.getName());
    }
}
