package com.evomatix.tasker.framework.fileops;



import java.io.IOException;
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

/**
 * The Class ReadPdf.
 */
public class SimplePDFReader {

    /** The Constant logger. */
    static final Logger logger = LoggerFactory.getLogger("ReadPdf");

    /** The file path. */
    private String filePath;

    /** The page number. */
    private String pageNumber;

    /** The filter coordinates. */
    //private String filterCoordinates;

    /**
     * Instantiates a new read pdf.
     *
     * @param filePath the file path
     * @param pageNumber the page number

     */
    public SimplePDFReader(String filePath, String pageNumber) {
        this.filePath = filePath;
        this.pageNumber = pageNumber;
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
     * @param pageNumber
     *            the new page number
     */
    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

}
