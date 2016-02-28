package com.scrapbook.abscbn100;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadFileWriteResultsXLSX {
    public static void main(String[] args) throws IOException {

        //Elapsed Time Start
        Instant start = Instant.now();
        System.out.println(start);


        //Reading XLXS File 1



        SpotsCheck spotCheck = new SpotsCheck();


        //Reading XLXS File 2
        FileInputStream fis = new FileInputStream(new File(System.getProperty("user.home"), "/ABS_CBN.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook (fis);
        XSSFSheet sheet = workbook.getSheet("Detail");
        Iterator ite = sheet.rowIterator();

        //Writing XLSX Result 1
        String excelFileNameResult1 =  System.getProperty("user.home") + "/testReadStudentsXResult1.xlsx";//name of excel file
        String sheetNameResult1 = "Matching";//name of sheet
        XSSFWorkbook wbResult1 = new XSSFWorkbook();
        XSSFSheet sheetResult1 = wbResult1.createSheet(sheetNameResult1);
        boolean showRowResult1 = false;
        List<Integer> removeRowsResult1 = new ArrayList<Integer>();
        int rowAdjResult1 = 0;

        //Writing XLSX Result 2
        String excelFileNameResult2 =  System.getProperty("user.home") + "/testReadStudentsXResult2.xlsx";//name of excel file
        String sheetNameResult2 = "NonMatching";//name of sheet
        XSSFWorkbook wbResult2 = new XSSFWorkbook();
        XSSFSheet sheetResult2 = wbResult2.createSheet(sheetNameResult2);
        boolean showRowResult2 = false;
        List<Integer> removeRowsResult2 = new ArrayList<Integer>();
        int rowAdjResult2 = 0;

        while(ite.hasNext()){
            Row row = (Row) ite.next();
            System.out.println("ROW: " + row.getRowNum());
            Iterator<Cell> cite = row.cellIterator();

            //Result1
            XSSFRow rowResult1 = sheetResult1.createRow((row.getRowNum() - rowAdjResult1) < 0 ? 0 : row.getRowNum() - rowAdjResult1);

            //Result2
            XSSFRow rowResult2 = sheetResult2.createRow((row.getRowNum() - rowAdjResult2) < 0 ? 0 : row.getRowNum() - rowAdjResult2);

            while(cite.hasNext()){
                Cell c = cite.next();

                //Result1
                XSSFCell cellResult1 = rowResult1.createCell(c.getColumnIndex() );
                cellResult1.setCellValue(c.toString());

                //Result2
                XSSFCell cellResult2 = rowResult2.createCell(c.getColumnIndex() );
                cellResult2.setCellValue(c.toString());



               // SpotsCheck spotCheck = new SpotsCheck();


                if(c.getColumnIndex() == 7) {
                    if(c.toString().equalsIgnoreCase("KRIS TV")) {
                        showRowResult1 = true;
                        showRowResult2 = false;
                    } else {
                        showRowResult1 = false;
                        showRowResult2 = true;
                    }
                }

                System.out.print(c.getColumnIndex() +":" + c.toString() +"  ");

            }

            if (row.getRowNum() <=1) {
                continue;
            }

            if (showRowResult1 == false) {
                System.out.println("RESULT1ROWINDEX:" + (row.getRowNum() - rowAdjResult1));
                deleteEmptyRows(sheetResult1,(row.getRowNum() - rowAdjResult1));
                rowAdjResult1++;
                showRowResult1 = true;
            }

            if (showRowResult2 == false) {
                System.out.println("RESULT1ROWINDEX:" + (row.getRowNum() - rowAdjResult2));
                deleteEmptyRows(sheetResult2,(row.getRowNum() - rowAdjResult2));
                rowAdjResult2++;
                showRowResult2 = true;
            }



            System.out.println();
        }

        //Reading
        fis.close();


        /* TESTING ONLY
        //Result1
        FileOutputStream fileOutResult1 = new FileOutputStream(excelFileNameResult1);
        //write this workbook to an Outputstream.
        wbResult1.write(fileOutResult1);
        fileOutResult1.flush();
        fileOutResult1.close();

        //Result2
        FileOutputStream fileOutResult2 = new FileOutputStream(excelFileNameResult2);
        //write this workbook to an Outputstream.
        wbResult2.write(fileOutResult2);
        fileOutResult2.flush();
        fileOutResult2.close();
        */

        //Elapsed Time End
        Instant end = Instant.now();
        System.out.println(end);
        Duration elapsed = Duration.between(start,end);
        System.out.println("Process Elapsed: " + elapsed);

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

}

//http://www.concretepage.com/apache-api/read-write-and-update-xlsx-using-poi-in-java
