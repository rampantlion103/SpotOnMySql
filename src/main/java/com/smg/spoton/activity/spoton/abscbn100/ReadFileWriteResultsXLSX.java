package com.smg.spoton.activity.spoton.abscbn100;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReadFileWriteResultsXLSX {

    private static final Logger log = LoggerFactory.getLogger(ReadFileWriteResultsXLSX.class);

    public static XSSFWorkbook[] main(MultipartFile file, String filterDate, String filterEndDate, Map<Integer, SpotsCheck> spotsCheck) throws IOException {

        log.debug("########  ReadFileWriteResultsXLSX ############");
        log.debug("StartDate: " + filterDate);
        log.debug("EndDate: " + filterEndDate);


        //Elapsed Time Start
        Instant start = Instant.now();
        log.debug(String.valueOf(start));

        //
        LocalDate spotDate = null;
        LocalDate startDate = null;
        LocalDate endDate = null;

        spotDate = convertStringDate("1/1/9999");
        startDate = convertStringDate(filterDate);
        endDate = convertStringDate(filterEndDate);

        //Reading XLXS File 1
        SpotsCheck spotDetail = new SpotsCheck();
        int readDateCollumn = 12;
        int readTimeCollumn = 13;
        int readRateCollumn = 19;

        String excelFileNameResult1;
        XSSFWorkbook wbResult1;

        String excelFileNameResult2;
        XSSFWorkbook wbResult2;

        XSSFWorkbook[] workbooks = new XSSFWorkbook[2];

        FileOutputStream fileOutResult1;
        FileOutputStream fileOutResult2;
        Instant end;
        Duration elapsed;

        try (FileInputStream fis = (FileInputStream) file.getInputStream()) {

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet("Detail");
            if (sheet == null) throw new IOException("The \"Detail worksheet\" can't be found in \"File Upload 2: ABS-CBN Report.\" Go Back and repeat the submission.");
            Iterator ite = sheet.rowIterator();

            //Writing XLSX Result 1
            excelFileNameResult1 = System.getProperty("user.home") + "/SpotCheckMatched.xlsx";
            String sheetNameResult1 = "Matching";//name of sheet
            wbResult1 = new XSSFWorkbook();
            XSSFSheet sheetResult1 = wbResult1.createSheet(sheetNameResult1);
            boolean showRowResult1 = false;
            List<Integer> removeRowsResult1 = new ArrayList<Integer>();
            int rowAdjResult1 = 0;

            //Writing XLSX Result 2
            excelFileNameResult2 = System.getProperty("user.home") + "/SpotCheckUnmatched.xlsx";
            String sheetNameResult2 = "NonMatching";//name of sheet
            wbResult2 = new XSSFWorkbook();
            XSSFSheet sheetResult2 = wbResult2.createSheet(sheetNameResult2);
            boolean showRowResult2 = false;
            List<Integer> removeRowsResult2 = new ArrayList<Integer>();
            int rowAdjResult2 = 0;

            while (ite.hasNext()) {
                Row row = (Row) ite.next();
                log.debug("ROW: " + row.getRowNum());
                Iterator<Cell> cite = row.cellIterator();

                //Result1
                XSSFRow rowResult1 = sheetResult1.createRow((row.getRowNum() - rowAdjResult1) < 0 ? 0 : row.getRowNum() - rowAdjResult1);

                //Result2
                XSSFRow rowResult2 = sheetResult2.createRow((row.getRowNum() - rowAdjResult2) < 0 ? 0 : row.getRowNum() - rowAdjResult2);

                while (cite.hasNext()) {
                    Cell c = cite.next();

                    //Finding and Setting the Collumn
                    if (row.getRowNum() == 1) {
                        if (SpotsCheck.dateHeader.contains(c.toString().toUpperCase())) {
                            log.debug("#### I found the date header at " + row.getRowNum() + ":" + c.getColumnIndex());
                            readDateCollumn = c.getColumnIndex();
                        }
                        if (SpotsCheck.timeHeader.contains(c.toString().toUpperCase())) {
                            log.debug("#### I found the time header at " + row.getRowNum() + ":" + c.getColumnIndex());
                            readTimeCollumn = c.getColumnIndex();
                        }
                        if (SpotsCheck.ratingHeader.contains(c.toString().toUpperCase())) {
                            log.debug("#### I found the rating header at " + row.getRowNum() + ":" + c.getColumnIndex());
                            readRateCollumn = c.getColumnIndex();
                        }
                    }

                    //if (row.getRowNum() > 3) break report; //for debug

                    //Read Date
                    if (c.getColumnIndex() == readDateCollumn) {
                        spotDetail.setDate(getCellValueAsString(c));
                    }

                    //Read Time
                    if (c.getColumnIndex() == readTimeCollumn) {
                        spotDetail.setTime(getCellValueAsString(c));
                    }

                    //Read Rate
                    if (c.getColumnIndex() == readRateCollumn) {
                        spotDetail.setRating(c.toString());
                    }


                    //Result1
                    XSSFCell cellResult1 = rowResult1.createCell(c.getColumnIndex());
                    cellResult1.setCellValue(c.toString());

                    //Result2
                    XSSFCell cellResult2 = rowResult2.createCell(c.getColumnIndex());
                    cellResult2.setCellValue(c.toString());

                    //Time Correction
                    //Read Time
                    if (c.getColumnIndex() == readTimeCollumn) {
                        //Result1
                        cellResult1 = rowResult1.createCell(c.getColumnIndex());
                        cellResult1.setCellValue(spotDetail.getTime()!=null?spotDetail.getTime().toString():"");

                        //Result2
                        cellResult2 = rowResult2.createCell(c.getColumnIndex());
                        cellResult2.setCellValue(spotDetail.getTime()!=null?spotDetail.getTime().toString():"");
                    }

                    if (!cite.hasNext()) {
                        if (spotsCheck.containsValue(spotDetail)) {
                            showRowResult1 = true;
                            showRowResult2 = false;
                        } else {
                            showRowResult1 = false;
                            showRowResult2 = true;
                        }

                        if (filterEndDate == null) {
                            log.debug("@@@@@showfilterdate: " + spotDetail.getDate() + "   " + spotDetail.getTime());
                            if (!spotDetail.getDate().equals(filterDate)) {
                                showRowResult1 = false;
                                showRowResult2 = false;
                            }
                        } else {
                            spotDate = convertStringDate(spotDetail.getDate());
                            if (startDate.isEqual(spotDate) || endDate.isEqual(spotDate) || (spotDate.isAfter(startDate) && spotDate.isBefore(endDate))) {
                                //System.out.println("TRUE");
                            } else {
                                showRowResult1 = false;
                                showRowResult2 = false;
                            }
                        }

                        log.debug(spotDetail.toString());
                    }

                    System.out.print(c.getColumnIndex() + ":" + c.toString() + "  ");

                }

                if (row.getRowNum() <= 1) {
                    continue;
                }

                if (showRowResult1 == false) {
                    log.debug("RESULT1ROWINDEX:" + (row.getRowNum() - rowAdjResult1));
                    deleteEmptyRows(sheetResult1, (row.getRowNum() - rowAdjResult1));
                    rowAdjResult1++;
                    showRowResult1 = true;
                }

                if (showRowResult2 == false) {
                    log.debug("RESULT1ROWINDEX:" + (row.getRowNum() - rowAdjResult2));
                    deleteEmptyRows(sheetResult2, (row.getRowNum() - rowAdjResult2));
                    rowAdjResult2++;
                    showRowResult2 = true;
                }

            }
            //Reading
            fis.close();
        }

        //Result1
        workbooks[0] =  wbResult1;

        //Result2
        workbooks[1] =  wbResult2;

        /* FOR TESTING ONLY
        //Result1
        fileOutResult1 = new FileOutputStream(excelFileNameResult1);
        //write this workbook to an Outputstream.
        wbResult1.write(fileOutResult1);
        fileOutResult1.flush();
        fileOutResult1.close();

        //Result2
        fileOutResult2 = new FileOutputStream(excelFileNameResult2);
        //write this workbook to an Outputstream.
        wbResult2.write(fileOutResult2);
        fileOutResult2.flush();
        fileOutResult2.close();
        */

        //Elapsed Time End
        end = Instant.now();
        log.debug(String.valueOf(end));
        elapsed = Duration.between(start, end);
        log.debug("Process Elapsed Time of ReadFileWriteResultsXLSX: " + elapsed);

        return workbooks;
    }

    static LocalDate convertStringDate (String date) {
        log.debug("Convert String Date: " + date);
        LocalDate retVal = null;
        final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");

        try {
            retVal = LocalDate.parse(date, DATE_FORMAT);
        } catch (java.time.format.DateTimeParseException e) {
            retVal = LocalDate.parse("1/1/9999", DATE_FORMAT);

        }
        return retVal;
    }



    public static void deleteEmptyRows(XSSFSheet sheet, List<Integer> rowIndexes){
        int minusAdj = 0;
        for(Integer rowIndex : rowIndexes) {
            deleteEmptyRows(sheet, rowIndex - minusAdj++);
        }

    }

    public static void deleteEmptyRows(XSSFSheet sheet, int rowIndex){
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }

    private static String getCellValueAsString(Cell poiCell) {
        if (poiCell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(poiCell)) {
            //get date
            Date date = poiCell.getDateCellValue();

            //set up formatters that will be used below
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatYearOnly = new SimpleDateFormat("yyyy");
            SimpleDateFormat formatMMDDYYYY = new SimpleDateFormat("M/d/yyyy");

        /*get date year.
        *"Time-only" values have date set to 31-Dec-1899 so if year is "1899"
        * you can assume it is a "time-only" value
        */
            String dateStamp = formatYearOnly.format(date);

            if (dateStamp.equals("1899") || dateStamp.equals("1900")){
                //Return "Time-only" value as String HH:mm:ss
                return formatTime.format(date);
            } else {
                //here you may have a date-only or date-time value

                //get time as String HH:mm:ss
                String timeStamp =formatTime.format(date);

                if (timeStamp.equals("00:00:00")){
                    //if time is 00:00:00 you can assume it is a date only value (but it could be midnight)
                    //In this case I'm fine with the default Cell.toString method (returning dd-MMM-yyyy in case of a date value)
                    //return poiCell.toString();
                    return formatMMDDYYYY.format(date);
                } else {
                    //return date-time value as "dd-MMM-yyyy HH:mm:ss"
                    return poiCell.toString()+" "+timeStamp;
                }
            }
        }
        //use the default Cell.toString method (returning "dd-MMM-yyyy" in case of a date value)
        return poiCell.toString();
    }

}

//http://www.concretepage.com/apache-api/read-write-and-update-xlsx-using-poi-in-java
