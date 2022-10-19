package com.evomatix.tasker.framework.fileops;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import technology.tabula.Rectangle;

/**
 * The Class ReadPdf.
 */
public class PDFReader {

    /** The Constant logger. */
    static final Logger logger = LoggerFactory.getLogger("ReadPdf");

    /** The file path. */
    private String filePath;

    /** The filter headers. */
    private String filterHeaders;

    /** The page number. */
    private String pageNumber;

    /** The filter coordinates. */
    private String filterCoordinates;

    /**
     * Instantiates a new read pdf.
     *
     * @param filePath the file path
     * @param filterHeaders the filter headers
     * @param pageNumber the page number
     * @param filterCoordinates the filter coordinates
     */
    public PDFReader(String filePath, String filterHeaders, String pageNumber, String filterCoordinates) {
        this.filePath = filePath;
        this.filterHeaders = filterHeaders;
        this.pageNumber = pageNumber;
        this.filterCoordinates = filterCoordinates;
    }

    /**
     * Prints the fields.
     *
     * @param pdfDocument the pdf document
     * @return the map
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Map<String, String> printFields(final PDDocument pdfDocument) throws IOException {
        Map<String, String> contentMap = new HashMap<String, String>();
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        List<PDField> fields = acroForm.getFields();
        logger.info(fields.size() + " top-level fields were found on the form");
        for (PDField field : fields) {
            contentMap = processField(field, "|--", field.getPartialName());
        }
        return contentMap;
    }

    /**
     * Process field.
     *
     * @param field the field
     * @param sLevel the s level
     * @param sParent the s parent
     * @return the map
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private Map<String, String> processField(final PDField field, final String sLevel, String sParent)
            throws IOException {
        Map<String, String> contentMap = new HashMap<String, String>();
        String partialName = field.getPartialName();
        if (field instanceof PDNonTerminalField) {
            if (!sParent.equals(field.getPartialName()) && partialName != null) {
                // if (partialName != null) { *violation Fix
                sParent = sParent + "." + partialName;
                // }
            }
            logger.info(sLevel + sParent);
            for (PDField child : ((PDNonTerminalField) field).getChildren()) {
                processField(child, "|  " + sLevel, sParent);
            }
        } else {
            String fieldValue = field.getValueAsString();
            StringBuilder outputString = new StringBuilder(sLevel);
            outputString.append(sParent);
            if (partialName != null) {
                outputString.append(".").append(partialName);
            }
            outputString.append(" = ").append(fieldValue);
            outputString.append(",  type=").append(field.getClass().getName());

            contentMap.put(partialName, fieldValue);
        }
        return contentMap;
    }

    /**
     * Gets the json map data.
     *
     * @param data the data
     * @return the json map data
     */
    public void getJsonMapData(final JSONArray data) {
        HashMap<String, String> mainData = new HashMap<String, String>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject parentOb = (JSONObject) data.get(i);
            Set<String> keys = parentOb.keySet();
            for (String key : keys) {
                if (parentOb.get(key).getClass() == JSONArray.class) {
                    getJsonMapData((JSONArray) parentOb.get(key));
                } else {
                    mainData.put(key, parentOb.get(key).toString());
                }
            }
        }
    }

    /**
     * Extract tables.
     *
     * @return the string
     * @throws Exception the exception
     */
    public String extractTables() throws Exception {
        String variable = null;
        checkNullOrEmpty(filePath);
        TabulaParserWorker worker = new TabulaParserWorker(new File(filePath));
        List<List<List<String>>> tableDataList = new ArrayList<List<List<String>>>();
        if (pageNumber == null || pageNumber.isEmpty()) {
            tableDataList = worker.extractTables();
        } else {
            if (filterCoordinates == null || filterCoordinates.isEmpty()) {
                tableDataList = worker.extractTables(Integer.parseInt(pageNumber));
            } else {
                String[] coords = filterCoordinates.split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int w = Integer.parseInt(coords[2]);
                int h = Integer.parseInt(coords[3]);
                Rectangle rect = new Rectangle(x, y, w, h);
                List<List<String>> tableData = worker.extractTables(Integer.parseInt(pageNumber), rect);
                tableDataList.add(tableData);
            }
        }
        if (filterHeaders != null && !filterHeaders.isEmpty()) {
            variable = filterTableDataList(tableDataList);
        } else {
            variable = new Gson().toJson(tableDataList);
        }

        return variable;
    }

    /**
     * Filter table data list.
     *
     * @param tableDataList the table data list
     * @return the string
     */
    private String filterTableDataList(List<List<List<String>>> tableDataList){
        List<List<List<String>>> filteredTableDataList = new ArrayList<List<List<String>>>();
        String[] filetrHeadersValues = filterHeaders.split(",");
        for (List<List<String>> table : tableDataList) {
            if (table.size() > 0) {
                List<String> headers = table.get(0);
                if (headers.containsAll(Arrays.asList(filetrHeadersValues))) {
                    filteredTableDataList.add(table);
                }
            }
        }
        return new Gson().toJson(filteredTableDataList);
    }

    /**
     * Check null or empty.
     *
     * @param value the value
     * @throws Exception the exception
     */
    public void checkNullOrEmpty(String value) throws Exception {
        if (value == null || value.isEmpty()) {
            throw new Exception("Illegal argument passed for the command : " + value);
        }
    }

    /**
     * Gets the file path.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     *
     * @param filePath the new file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the filter headers.
     *
     * @return the filter headers
     */
    public String getFilterHeaders() {
        return filterHeaders;
    }

    /**
     * Sets the filter headers.
     *
     * @param filterHeaders the new filter headers
     */
    public void setFilterHeaders(String filterHeaders) {
        this.filterHeaders = filterHeaders;
    }

    /**
     * Gets the page number.
     *
     * @return the page number
     */
    public String getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the page number.
     *
     * @param pageNumber the new page number
     */
    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Gets the filter coordinates.
     *
     * @return the filter coordinates
     */
    public String getFilterCoordinates() {
        return filterCoordinates;
    }

    /**
     * Sets the filter coordinates.
     *
     * @param filterCoordinates the new filter coordinates
     */
    public void setFilterCoordinates(String filterCoordinates) {
        this.filterCoordinates = filterCoordinates;
    }

}
