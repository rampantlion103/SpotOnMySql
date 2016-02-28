package com.scrapbook.abscbn100;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SpotsXLSX {


    //Date date
    //Time time
    //Rating rtg
    //hashcode

    public static void main(String[] args) throws IOException {

        Instant start = Instant.now();
        System.out.println(start);

        Map<Integer,SpotsCheck> spots = new HashMap<Integer, SpotsCheck>();
        int readDateCollumn = 9;
        int readTimeCollumn = 11;
        int readRateCollumn = 13;

        FileInputStream fis = new FileInputStream(new File(System.getProperty("user.home"), "/Spot.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook (fis);
        XSSFSheet sheet = workbook.getSheet("Spots");
        Iterator ite = sheet.rowIterator();
        report:
        while (ite.hasNext()) {
            Row row = (Row) ite.next();
            Iterator<Cell> cite = row.cellIterator();
            SpotsCheck spot = new SpotsCheck();
            while (cite.hasNext()){
                Cell c = cite.next();

               //System.out.print(c.toString() +"  "); //for debug

                //Finding and Setting the Collumn
                if (row.getRowNum() == 0) {
                    if (SpotsCheck.dateHeader.contains(c.toString().toUpperCase())) {
                        System.out.println("#### I found the date header at " + row.getRowNum() + ":" + c.getColumnIndex());
                        readDateCollumn = c.getColumnIndex();
                    }
                    if (SpotsCheck.timeHeader.contains(c.toString().toUpperCase())) {
                        System.out.println("#### I found the time header at " + row.getRowNum() + ":" + c.getColumnIndex());
                        readTimeCollumn = c.getColumnIndex();
                    }
                    if (SpotsCheck.ratingHeader.contains(c.toString().toUpperCase())) {
                        System.out.println("#### I found the rating header at " + row.getRowNum() + ":" + c.getColumnIndex());
                        readRateCollumn = c.getColumnIndex();
                    }
                }

                //if (row.getRowNum() > 3) break report; //for debug

                //Read Date
                if (c.getColumnIndex() == readDateCollumn) {
                    System.out.print(c.toString() +"  ");
                    spot.setDate(c.toString());
                }

                //Read Time
                if (c.getColumnIndex() == readTimeCollumn) {
                    System.out.print(c.toString() +"  ");
                    spot.setTime(c.toString());
                }

                //Read Rate
                if (c.getColumnIndex() == readRateCollumn) {
                    System.out.print(c.toString() +"  ");
                    spot.setRating(c.toString());
                }

               // System.out.print(c.toString() +"  ");
            }
            System.out.println();
            System.out.println(spot.toString());
            spots.put(spot.hashCode(),spot);
        }

        System.out.println("##### TESTING ######");
       // SpotsCheck spotCheck = new SpotsCheck("11/29/2015","22:57","6.15");
        SpotsCheck spotCheck = new SpotsCheck();
        spotCheck.setDate("11/29/2015");
        spotCheck.setTime("22:57:01");
        spotCheck.setRating("6.15");
        System.out.println(spotCheck.toString());
        System.out.println("ContainsKey: " + spots.containsKey(spotCheck.hashCode()));
        System.out.println("ContainsValue: " + spots.containsValue(spotCheck));

        spots.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));


        fis.close();

        //2015-12-27T02:58:21.797Z
        Instant end = Instant.now();
        System.out.println(end);
        //Elapsed: PT0.061S
        Duration elapsed = Duration.between(start, end);
        System.out.println("Elapsed: " + elapsed);



    }
}
