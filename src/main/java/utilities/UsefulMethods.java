package utilities;

import extensions.Verifications;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class UsefulMethods {

    public final Verifications verifications;

    public UsefulMethods(Verifications verifications) {
        this.verifications = verifications;
    }


    public void uploadFileWithRobot(String imagePath) {
        StringSelection stringSelection = new StringSelection(imagePath);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        Robot robot = null;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.out.println("Upload attachment failed! " + e);
        }
        assert robot != null;
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(200);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public String getData(String Configuration) {
        try {
            DocumentBuilder dBuilder;
            Document doc = null;
            File fXmlFile = new File("./Configuration/DataConfig.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
            if (doc == null) {
                throw new IllegalStateException("Failed to parse configuration file");
            }
            doc.getDocumentElement().normalize();
            String value = doc.getElementsByTagName(Configuration).item(0).getTextContent();

            if (value == null || value.isEmpty()) {
                throw new IllegalStateException("Configuration value for " + Configuration + " not found");
            }
            return value;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read configuration: " + e.getMessage(), e);
        }
    }

    public String getProjectPath() {
        return System.getProperty("user.dir");
    }

    public LocalDateTime getPCDateTime(String dateFormat) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current PC time and date is " + dtf.format(now));
        return now;
    }

    public String calculateTimeDifference(LocalDateTime baseTime, LocalDateTime newTime) {
        String timeDifferenceInMilliseconds = String.valueOf(ChronoUnit.MILLIS.between(baseTime, newTime));
        System.out.println("The time difference in milliseconds is " + timeDifferenceInMilliseconds);
        return timeDifferenceInMilliseconds;
    }

    public String[] reverseArray(String[] array) {
        int left = 0;               // Index of the leftmost element
        int right = array.length - 1;  // Index of the rightmost element
        // Swap elements from the beginning and end towards the middle
        while (left < right) {
            // Swap array[left] and array[right]
            String temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            // Move the indices toward the middle
            left++;
            right--;
        }
        return array;
    }

    //Excel related actions

    public void readEntireXlsFile(String xlsExcelFilePath, int sheetIndex) {
        try {
            FileInputStream fis = new FileInputStream(xlsExcelFilePath);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (row != null) {
                    for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                        HSSFCell cell = row.getCell(columnIndex);
                        if (cell != null) {
                            if (cell.getCellType() == CellType.STRING) {
                                System.out.print(cell.getStringCellValue() + "\t");
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                System.out.print(cell.getNumericCellValue() + "\t");
                            }
                        }
                    }
                    System.out.println();
                }
            }
            fis.close();
            workbook.close();
        } catch (IOException e) {
            System.out.println("Reading file failed! " + e);
        }
    }


    public void getAndVerifyNumOfRowsAndColumnInXlsExcel(String xlsExcelFilePath, int sheetIndex, int expectedNumOfRows, int expectedNumOfColumn) throws IOException {
        FileInputStream fis = new FileInputStream(xlsExcelFilePath);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        int actualNumOfRows = sheet.getLastRowNum() + 1;
        int actualNumOfColumns = sheet.getRow(1).getLastCellNum();
        assertEquals(actualNumOfRows, expectedNumOfRows);
        assertEquals(actualNumOfColumns, expectedNumOfColumn);
    }


    public void printAndVerifyTextInSpecificXlsExcelColumn(String xlsPath, int sheetIndex, int columnIndex, String[] xlsExpectedColumnText) throws IOException, InterruptedException {
        FileInputStream fis = new FileInputStream(xlsPath);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        List<String> columnText = new ArrayList<>();
        for (Row row : sheet) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                String cellValue = "";
                if (cell.getCellType() == CellType.STRING) {
                    cellValue = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                System.out.println("Row " + row.getRowNum() + ", Column " + columnIndex + ": " + cellValue);
                columnText.add(columnIndex, cellValue);
            }
        }
        workbook.close();
        fis.close();
        Collections.reverse(columnText);
        System.out.println(columnText);
        verifications.verifyCorrectTextInEachStringListIndex(columnText, xlsExpectedColumnText);
    }


    public XSSFWorkbook readEntireXlsxExcel(String xlsxExcelFilePath, int sheetIndex) throws IOException {
        FileInputStream InputStream = new FileInputStream(xlsxExcelFilePath);
        XSSFWorkbook Workbook = new XSSFWorkbook(InputStream);
        XSSFSheet Sheet = Workbook.getSheetAt(sheetIndex);
        int Rows = Sheet.getLastRowNum();
        int Columns = Sheet.getRow(1).getLastCellNum();
        for (int r = 0; r <= Rows; r++) {
            XSSFRow Row = Sheet.getRow(r);
            for (int c = 0; c < Columns; c++) {
                XSSFCell Cell = Row.getCell(c);
                switch (Cell.getCellType()) {
                    case STRING -> System.out.print(Cell.getStringCellValue());
                    case NUMERIC -> System.out.print(Cell.getNumericCellValue());
                    case BOOLEAN -> System.out.print(Cell.getBooleanCellValue());
                }
                System.out.print(" | ");
            }
            System.out.println();
        }
        return Workbook;
    }

    public XSSFWorkbook readEntireXlsxExcelIterator(String excelFilePath, int sheetIndex) throws IOException {
        FileInputStream InputStream = new FileInputStream(excelFilePath);
        XSSFWorkbook Workbook = new XSSFWorkbook(InputStream);
        XSSFSheet Sheet = Workbook.getSheetAt(sheetIndex);
        for (Row cells : Sheet) {
            XSSFRow row = (XSSFRow) cells;
            Iterator<Cell> CellIterator = row.cellIterator();
            while (CellIterator.hasNext()) {
                XSSFCell cell = (XSSFCell) CellIterator.next();
                switch (cell.getCellType()) {
                    case STRING -> cell.getStringCellValue();
                    case NUMERIC -> cell.getNumericCellValue();
                    case BOOLEAN -> cell.getBooleanCellValue();
                }
                // System.out.println(" | ");
            }
            //System.out.println();
        }
        return Workbook;
    }


    public void getAndVerifyNumOfRowsAndColumnInXlsxExcel(String xlsxExcelFilePath, int sheetIndex, int expectedNumOfRows, int expectedNumOfColumn) throws IOException {
        FileInputStream InputStream = new FileInputStream(xlsxExcelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(InputStream);
        XSSFSheet Sheet = workbook.getSheetAt(sheetIndex);
        int actualNumOfRows = Sheet.getLastRowNum() + 1;
        int actualNumOfColumns = Sheet.getRow(1).getLastCellNum();
        assertEquals(actualNumOfRows, expectedNumOfRows);
        assertEquals(actualNumOfColumns, expectedNumOfColumn);
    }


    public void getAndVerifyTextOfASpecificXlsxExcelRow(String xlsxPath, int sheetIndex, int rowIndex, boolean flip, String[] xlsxExpectedRowText) throws IOException, InterruptedException {
        FileInputStream fileInputStream = new FileInputStream(xlsxPath);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        String[] actualRowValues = new String[workbook.getSheetAt(sheetIndex).getRow(rowIndex).getLastCellNum()];
        List<String> actualRowText = new ArrayList<>();
        for (int c = 0; c < workbook.getSheetAt(sheetIndex).getRow(rowIndex).getLastCellNum(); c++) {
            actualRowValues[c] = workbook.getSheetAt(sheetIndex).getRow(rowIndex).getCell(c).toString();
            System.out.println("The value of cell " + c + " of row " + rowIndex + " is:" + " " + actualRowValues[c]);
            actualRowText.add(rowIndex, actualRowValues[c]);
        }
        workbook.close();
        fileInputStream.close();
        System.out.println();
        if (flip) Collections.reverse(actualRowText);
        verifications.verifyCorrectTextInEachStringListIndex(actualRowText, xlsxExpectedRowText);
    }


    public void printAndVerifyTextInSpecificXlsxExcelColumn(String xlsxPath, int sheetIndex, int columnIndex, String[] xlsxExpectedColumnText) throws IOException, InterruptedException {
        FileInputStream fileInputStream = new FileInputStream(xlsxPath);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        List<String> columnText = new ArrayList<>();
        for (Row row : sheet) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                String cellValue = "";
                if (cell.getCellType() == CellType.STRING) {
                    cellValue = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                System.out.println("Row " + row.getRowNum() + ", Column " + columnIndex + ": " + cellValue);
                columnText.add(columnIndex, cellValue);
            }
        }
        workbook.close();
        fileInputStream.close();
        Collections.reverse(columnText);
        System.out.println(columnText);
        verifications.verifyCorrectTextInEachStringListIndex(columnText, xlsxExpectedColumnText);
    }

    //Word related actions
    public String readEntireDocxDocumentAndGetText(String wordDocPath) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(wordDocPath))) {
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            String docxText = xwpfWordExtractor.getText();
            System.out.println(docxText);
            // find number of words in the document
            long count = Arrays.stream(docxText.split("\\s+")).count();
            System.out.println("Total words: " + count);
            return docxText;
        }
    }


    public void verifySpecificTextExistsInDocx(String wordDocPath, String searchedText) throws IOException {
        String doxText = readEntireDocxDocumentAndGetText(wordDocPath);
        assertTrue(doxText.contains(searchedText));
    }

    public void readEntireDocDocumentAndVerifySpecificTextExist(String wordDocPath, String searchedText) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(wordDocPath);
            HWPFDocument document = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(document);
            String[] paragraphs = extractor.getParagraphText();
            for (String paragraph : paragraphs) {
                System.out.println(paragraph);
            }
            String docText = extractor.getText();
            assertTrue(docText.contains(searchedText));
            extractor.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Reading document failed! " + e);
        }
    }

    public String[] getWordDocumentHeader(String path) throws IOException {
        XWPFDocument doc = new XWPFDocument(new FileInputStream(path));
        List<XWPFHeader> HeaderList = doc.getHeaderList();
        String[] arr = new String[HeaderList.size()];
        for (int i = 0; i < HeaderList.size(); i++) {
            arr[i] = (HeaderList.get(i).getText());
            System.out.println("Header's Text is: " + HeaderList.get(i).getText());
        }
        return arr;
    }

    public String[] getWordDocumentFooter(String path) throws IOException {
        XWPFDocument doc = new XWPFDocument(new FileInputStream(path));
        List<XWPFFooter> FooterList = doc.getFooterList();
        String[] arr = new String[FooterList.size()];
        for (int i = 0; i < FooterList.size(); i++) {
            arr[i] = (FooterList.get(i).getText());
            System.out.println("Footer's Text is: " + FooterList.get(i).getText());
        }
        return arr;
    }

    public String[] getWordDocumentElements(String path) throws IOException {
        XWPFDocument doc = new XWPFDocument(new FileInputStream(path));
        List<IBodyElement> bodyElements = doc.getBodyElements();
        String[] elementsText = new String[bodyElements.size()];

        for (int i = 0; i < bodyElements.size(); i++) {
            System.out.println("Body part type is: " + bodyElements.get(i).getClass());
            if (bodyElements.get(i) instanceof XWPFParagraph) {
                XWPFParagraph Paragraph = (XWPFParagraph) bodyElements.get(i);
                System.out.println(Paragraph.getText());
                elementsText[i] = Paragraph.getText();
            } else if (bodyElements.get(i) instanceof XWPFTable) {
                XWPFTable table = (XWPFTable) bodyElements.get(i);
                System.out.println("table text " + table.getText());
                elementsText[i] = table.getText();
                List<XWPFTableRow> rows = table.getRows();
                for (XWPFTableRow row : rows) {
                    List<XWPFTableCell> tableCells = row.getTableCells();
                    System.out.println("New row with cells: " + row.getTableCells().size());
                    for (XWPFTableCell tableCell : tableCells)
                        System.out.println(tableCell.getText());
                }
            }
        }
        return elementsText;
    }

    public static int getPageCount(PDDocument doc) {
        //get the total number of pages in the pdf document
        int pageCount = doc.getNumberOfPages();
        return pageCount;
    }

    public static String readFile(String filePath) throws IOException {
        File file = new File(filePath);
        String fileContent = FileUtils.readFileToString(file, "UTF-8");
        System.out.println(fileContent);
        return fileContent;
    }

    public static void deleteFile(String filePath) throws IOException {
        Path path = FileSystems.getDefault().getPath(filePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException x) {
            System.err.println(x);
        }
        boolean isEmpty = isEmpty(path);
        if (isEmpty) System.out.println("Directory is empty");
    }

    public static boolean isEmpty(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
                return !directory.iterator().hasNext();
            }
        }
        return false;
    }

    public static void verifyNumOfPagesInPDFDocument(String pdfPath, int expectedNumOfPages) throws Exception {
        File file = new File(pdfPath);
        PDDocument pdfDocument = Loader.loadPDF(new RandomAccessReadBufferedFile(file));
        System.out.println("Num of pages in selected PDF document is " + pdfDocument.getNumberOfPages());
        //compareIntValues(pdfDocument.getNumberOfPages(), expectedNumOfPages);
    }

    public void convertEntirePDFToImages(String pdfPath, String convertedImagesPath, String extension) {
        File file = new File(pdfPath);
        try {
            PDDocument pdfDocument = Loader.loadPDF(new RandomAccessReadBufferedFile(file));
            if (pdfDocument.isEncrypted()) System.out.println("PDF file is encrypted - the test would fail");
            else {
                PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
                for (int page = 0; page < pdfDocument.getNumberOfPages(); ++page) {
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                    ImageIO.write(bim, "PNG", new File(convertedImagesPath + "_" + page + "." + extension));
                }
                pdfDocument.close();
            }
        } catch (IOException e) {
            System.out.println("Converting the entire PDF page to an image failed! " + e);
        }
    }

    public void convertSpecificPDFPageToImages(String pdfPath, int pageNumber, String convertedImagePath, String extension) {
        File file = new File(pdfPath);
        try {
            PDDocument pdfDocument = Loader.loadPDF(new RandomAccessReadBufferedFile(file));
            if (pdfDocument.isEncrypted()) System.out.println("PDF file is encrypted - the test would fail");
            else {
                PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pageNumber, 300, ImageType.RGB);
                ImageIO.write(bim, extension, new File(convertedImagePath + "Page" + pageNumber + "." + extension));
                pdfDocument.close();
            }
        } catch (IOException e) {
            System.out.println("Converting the PDF page to an image failed! " + e);
        }
    }

    //Originally included file chooser that allows specifying file name and path, but didn't work so removed
    public static void saveDocument() {
        try {
            // Simulate pressing Ctrl+S
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_S);
            robot.keyRelease(KeyEvent.VK_S);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            Thread.sleep(1000);
            // Simulate pressing Enter to save the file
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception e) {
            System.out.println("Saving document failed " + e);
        }
    }

    public void modifyExistingFile(String oldFilePath, String newFilePath) {
        File oldFile = new File(oldFilePath);
        File newFile = new File(newFilePath);
        try {
            FileUtils.moveFile(oldFile, newFile);
            System.out.println("File modified successfully.");
        } catch (IOException e) {
            System.err.println("Failed to rename the file: " + e.getMessage());
        }
    }

    public boolean checkIfFileExists(String filePath) {
        File file = FileUtils.getFile(filePath);
        boolean existence;
        if (file != null && file.exists()) {
            System.out.println("File exists.");
            existence = true;
        } else {
            System.out.println("File does not exist.");
            existence = false;
        }
        return existence;
    }

    public void verifyFileExists(String filePath) {
        String verdict = String.valueOf(checkIfFileExists(filePath));
        assertEquals(verdict, "true");
    }

    public void verifyFileDoesNotExist(String filePath) {
        String verdict = String.valueOf(checkIfFileExists(filePath));
        assertEquals(verdict, "false");
    }

    //The folder itself remain
    public void deleteAllFilesInAGivenPath(String folderPath) throws InterruptedException {
        File f = new File(folderPath);
        String[] s = f.list();
        if (s != null && s.length == 0) System.out.println("The folder is already empty - nothing to delete");
        else if (s != null) {
            System.out.println("The folder initially contained " + s.length + " files");
            for (String s1 : s) {
                File f1 = new File(f, s1);
                System.out.println(f1);
            }
            for (String s1 : s) {
                File f1 = new File(f, s1);
                f1.delete();
                Thread.sleep(1000);
            }
            String[] s1 = f.list();
            if (s1 != null) {
                assertEquals(s1.length, 0);
                System.out.println("All files were successfully deleted - folder is empty");
            }
        }
    }

    public void deleteEntireFolderWithItsContent(String[] args) {
        // Specify the path to the folder you want to delete
        String folderPath = "C:\\path\\to\\your\\folder";

        // Create a File object for the folder
        File folder = new File(folderPath);

        // Check if the folder exists
        if (folder.exists()) {
            // Call the deleteFolder method to delete the folder and its contents
            if (deleteFolder(folder)) {
                System.out.println("Folder deleted successfully.");
            } else {
                System.err.println("Failed to delete folder.");
            }
        } else {
            System.err.println("Folder does not exist.");
        }
    }

    public boolean deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Recursively delete files and sub-folders
                    if (!deleteFolder(file)) {
                        return false;
                    }
                }
            }
        }
        // Delete the folder itself
        return folder.delete();
    }
}