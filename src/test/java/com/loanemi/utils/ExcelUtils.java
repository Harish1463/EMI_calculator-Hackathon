package com.loanemi.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Utility class for performing Excel operations.
 * Uses Apache POI to manipulate XLSX workbooks.
 */
public class ExcelUtils {

    private static final Logger logger = LogManager.getLogger("ExcelLogger");

    /**
     * Thread-safe method to set a single cell value in the specified sheet and file.
     * Automatically creates the sheet, row, or cell if it doesn't exist.
     *
     * @param filePath  Path to the target Excel file
     * @param sheetName Sheet to operate on
     * @param rowNum    Row number (0-based)
     * @param colNum    Column number (0-based)
     * @param value     String value to set
     */
    public static void setCellData(String filePath, String sheetName, int rowNum, int colNum, String value) {
        synchronized (filePath.intern()) { // Ensure thread-safe access per file
            logger.info("Attempting to write to Excel: {}, Sheet: {}, Row: {}, Column: {}, Value: {}",
                        filePath, sheetName, rowNum, colNum, value);

            try {
                File file = new File(filePath);
                Workbook workbook;
                Sheet sheet;

                // Load existing workbook or create new one
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    workbook = new XSSFWorkbook(fis);
                    fis.close();
                    logger.info("Existing Excel file found: {}", filePath);
                } else {
                    workbook = new XSSFWorkbook();
                    logger.info("Creating new Excel file: {}", filePath);
                }

                // Retrieve or create sheet
                sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    sheet = workbook.createSheet(sheetName);
                    logger.info("Created new sheet: {}", sheetName);
                }

                // Retrieve or create row
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    row = sheet.createRow(rowNum);
                    logger.debug("Created new row at index {}", rowNum);
                }

                // Retrieve or create cell
                Cell cell = row.getCell(colNum);
                if (cell == null) {
                    cell = row.createCell(colNum);
                    logger.debug("Created new cell at column {}", colNum);
                }

                // Set value and write to disk
                cell.setCellValue(value);
                logger.info("Successfully set cell value at row {}, column {}", rowNum, colNum);

                FileOutputStream fos = new FileOutputStream(filePath);
                workbook.write(fos);
                fos.close();
                workbook.close();

                logger.info("Excel write completed and file saved: {}", filePath);

            } catch (Exception e) {
                logger.error("Error writing to Excel file: {}", filePath, e);
            }
        }
    }

    /**
     * Clears the specified sheet by removing it and recreating it empty.
     * Useful for resetting data before a fresh test execution.
     *
     * @param filePath  Excel file path
     * @param sheetName Name of the sheet to clear
     */
    public static void clearSheet(String filePath, String sheetName) {
        synchronized (filePath.intern()) { // Thread safety per Excel file
            logger.info("Clearing sheet '{}' in file '{}'", sheetName, filePath);

            try {
                FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = WorkbookFactory.create(fis);
                fis.close();

                // Remove sheet if it exists
                int sheetIndex = workbook.getSheetIndex(sheetName);
                if (sheetIndex != -1) {
                    workbook.removeSheetAt(sheetIndex);
                    logger.info("Removed existing sheet: {}", sheetName);
                }

                // Recreate fresh sheet
                workbook.createSheet(sheetName);
                logger.info("Recreated fresh sheet: {}", sheetName);

                FileOutputStream fos = new FileOutputStream(filePath);
                workbook.write(fos);
                workbook.close();
                fos.close();

                logger.info("Sheet '{}' cleared and Excel file updated", sheetName);
            } catch (Exception e) {
                logger.error("Failed to clear sheet '{}' in file '{}'", sheetName, filePath, e);
            }
        }
    }
}
