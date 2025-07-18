package com.loanemi.utils;

import java.io.File;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanemi.entity.JsonData;

/**
 * Utility class for reading structured test data from JSON files.
 * Supports loading individual test case objects mapped under unique keys.
 */
public class JsonUtil {

    private static final Logger logger = LogManager.getLogger(JsonUtil.class);

    /**
     * Reads a nested JSON object representing test cases using the specified file path and test case ID.
     *
     * Expected JSON structure:
     * {
     *   "testCases": {
     *     "TC01": { ... },
     *     "TC02": { ... }
     *   }
     * }
     *
     * @param filePath    Path to JSON file (e.g., src/test/resources/test-data/testcases.json)
     * @param testCaseId  Unique test case identifier (e.g., TC03)
     * @return JsonData object mapped to the given key, or null if not found
     */
    public static JsonData getTestCase(String filePath, String testCaseId) {
        try {
            logger.info("Attempting to read test case '{}' from file: {}", testCaseId, filePath);

            ObjectMapper mapper = new ObjectMapper();

            // Deserialize JSON as a nested map structure
            Map<String, Map<String, JsonData>> root = mapper.readValue(
                new File(filePath),
                new TypeReference<Map<String, Map<String, JsonData>>>() {}
            );

            // Safely retrieve target test case data
            JsonData data = root.get("testCases").get(testCaseId);

            if (data != null) {
                logger.info("Successfully retrieved test case '{}'", testCaseId);
            } else {
                logger.warn("Test case '{}' not found in JSON file '{}'", testCaseId, filePath);
            }

            return data;

        } catch (Exception e) {
            logger.error("Error reading test case '{}' from JSON file '{}'", testCaseId, filePath, e);
            return null;
        }
    }
}
