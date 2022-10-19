package com.evomatix.tasker.framework.fileops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Rectangle;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.TextElement;
import technology.tabula.detectors.NurminenDetectionAlgorithm;
import technology.tabula.detectors.SpreadsheetDetectionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

/**
 * The Class TabulaParserWorker.
 */
@SuppressWarnings("rawtypes")
public class TabulaParserWorker {

    /** The Constant logger. */
    static final Logger logger = LoggerFactory.getLogger("TabulaParserWorker");

    /** The pdf file. */
    private File pdfFile;

    /**
     * Instantiates a new tabula parser worker.
     *
     * @param pdfFile the pdf file
     * @throws FileNotFoundException the file not found exception
     */
    public TabulaParserWorker(File pdfFile) throws FileNotFoundException {
        if (!pdfFile.exists()) {
            throw new FileNotFoundException("Cannot find the PDF file in location " + pdfFile.getAbsolutePath());
        }
        logger.info("Initialized PDF parser for the pdf file : " + pdfFile);
        this.pdfFile = pdfFile;
    }

    /**
     * Extract tables.
     *
     * @return the list
     * @throws InvalidPasswordException the invalid password exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public List<List<List<String>>> extractTables() throws InvalidPasswordException, IOException {
        List<List<List<String>>> tableArray = new ArrayList<List<List<String>>>();
        NurminenDetectionAlgorithm detectionAlgorithm = new NurminenDetectionAlgorithm();
        PageIterator pages = getPages();
        while (pages.hasNext()) {
            Page page = pages.next();
            logger.info("Parsing the page number : " + page.getPageNumber());
            List<Rectangle> tablesOnPage = detectionAlgorithm.detect(page);
            logger.info("Found " + tablesOnPage.size() + " tables on the current page");
            for (Rectangle rect : tablesOnPage) {
                Page p = page.getArea(rect);
                SpreadsheetExtractionAlgorithm bea = new SpreadsheetExtractionAlgorithm();
                Table table = bea.extract(p).get(0);
                List<List<String>> tableObj = getTableRows(table);
                tableArray.add(tableObj);
            }
        }
        return tableArray;
    }

    /**
     * Extract text.
     *
     * @param pageNumber the page number
     * @return the string
     * @throws InvalidPasswordException the invalid password exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String extractText(int pageNumber) throws InvalidPasswordException, IOException {
        Page page = getPage(pageNumber);
        List<TextElement> textelements = page.getText();
        StringBuilder textBuilder = new StringBuilder();
        float perviousY = 0;
        final double EPSILON = 0.001;

        for (TextElement elem : textelements) {
            if ( Math.abs( elem.y - perviousY) > EPSILON) {
                textBuilder.append("\n");
            }
            perviousY = elem.y;
            textBuilder.append(elem.getText());
        }
        return textBuilder.toString().trim();
    }

    public String extractText(int pageNumber, Rectangle rect) throws InvalidPasswordException, IOException {

        PDPage page = getPage(pageNumber).getPDPage();
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.addRegion("SelectedRange", rect.getBounds2D());
        stripper.extractRegions(page);
        String text = stripper.getTextForRegion("SelectedRange");

        return text;
    }

    /**
     * Gets the pages.
     *
     * @return the pages
     * @throws InvalidPasswordException the invalid password exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public PageIterator getPages() throws InvalidPasswordException, IOException {
        ObjectExtractor extractor = getObjectExtractor();
        PageIterator pages = extractor.extract();
        return pages;
    }

    /**
     * Gets the page.
     *
     * @param pageNumber the page number
     * @return the page
     * @throws InvalidPasswordException the invalid password exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Page getPage(int pageNumber) throws InvalidPasswordException, IOException {
        ObjectExtractor extractor = getObjectExtractor();
        logger.info("Parsing the page number : " + pageNumber);
        Page page = extractor.extract(pageNumber);
        return page;
    }

    /**
     * Gets the object extractor.
     *
     * @return the object extractor
     * @throws InvalidPasswordException the invalid password exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private ObjectExtractor getObjectExtractor() throws InvalidPasswordException, IOException {
        PDDocument pdfDocument = PDDocument.load(pdfFile);
        logger.info("Loaded the PDF file for extraction");
        return new ObjectExtractor(pdfDocument);
    }

    /**
     * Gets the table rows.
     *
     * @param table the table
     * @return the table rows
     */
    private List<List<String>> getTableRows(Table table) {
        List<List<String>> tableObj = new ArrayList<List<String>>();
        List<List<RectangularTextContainer>> tableRowsList = table.getRows();
        logger.info("Table has n number of rows. n=" + tableRowsList.size());
        for (int row = 0; row < tableRowsList.size(); row++) {
            List<String> rowObj = new ArrayList<String>();
            List<RectangularTextContainer> cols = tableRowsList.get(row);
            for (int col = 0; col < cols.size(); col++) {
                rowObj.add(cols.get(col).getText());
                System.out.print(cols.get(col).getText() + " | ");
            }
            tableObj.add(rowObj);
        }
        return tableObj;
    }

    /**
     * Extract tables.
     *
     * @param pageNumber the page number
     * @return the list
     * @throws InvalidPasswordException the invalid password exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public List<List<List<String>>> extractTables(int pageNumber) throws InvalidPasswordException, IOException {
        List<List<List<String>>> tableArray = new ArrayList<List<List<String>>>();
        SpreadsheetDetectionAlgorithm detectionAlgorithm = new SpreadsheetDetectionAlgorithm();
        Page page = getPage(pageNumber);
        List<Rectangle> tablesOnPage = detectionAlgorithm.detect(page);
        logger.info("Found " + tablesOnPage.size() + " tables on the current page");
        for (Rectangle rect : tablesOnPage) {
            Page p = page.getArea(rect);
            SpreadsheetExtractionAlgorithm bea = new SpreadsheetExtractionAlgorithm();
            Table table = bea.extract(p).get(0);
            List<List<String>> tableObj = getTableRows(table);
            tableArray.add(tableObj);
        }
        return tableArray;

    }

    /**
     * Extract tables.
     *
     * @param pageNumber the page number
     * @param rect the rect
     * @return the list
     * @throws InvalidPasswordException the invalid password exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public List<List<String>> extractTables(int pageNumber, Rectangle rect)
            throws InvalidPasswordException, IOException {
        ObjectExtractor extractor = getObjectExtractor();
        logger.info("Parsing the page number : " + pageNumber + " with rect : " + rect);
        Page page = extractor.extract(pageNumber);
        Page p = page.getArea(rect);
        SpreadsheetExtractionAlgorithm bea = new SpreadsheetExtractionAlgorithm();
        Table table = bea.extract(p).get(0);
        List<List<String>> tableObj = getTableRows(table);
        return tableObj;
    }

}