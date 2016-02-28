package com.smg.spoton.activity.spoton.abscbn100;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SpotsCheckXLSX {

    private static final Logger log = LoggerFactory.getLogger(SpotsCheckXLSX.class);

    public static Map<Integer, SpotsCheck> main(MultipartFile file) throws IOException {
        log.debug("##### SPOTSCHECK XLSX #######");

        Instant start = Instant.now();
        log.debug("STARTING TIME OF SPOTCHECKXLSX: " + String.valueOf(start));

        Map<Integer,SpotsCheck> spotsCheck = new HashMap<Integer, SpotsCheck>();
        int readDateCollumn = 10;
        int readTimeCollumn = 12;
        int readRateCollumn = 14;

       // FileInputStream fis = new FileInputStream(new File(System.getProperty("user.home"), "/Spot.xlsx"));
        Instant end;
        Duration elapsed;

        try (FileInputStream fis = (FileInputStream) file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet("Spots");
           if (sheet == null) throw new IOException("The \"Spots worksheet\" can't be found in \"File Upload 1: Kantar Data.\" Go Back and repeat the submission.");
            Iterator ite = sheet.rowIterator();

            report:
            while (ite.hasNext()) {
                Row row = (Row) ite.next();
                Iterator<Cell> cite = row.cellIterator();
                SpotsCheck spot = new SpotsCheck();

                while (cite.hasNext()) {
                    Cell c = cite.next();

                    //Finding and Setting the Collumn
                    if (row.getRowNum() == 0) {
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

                    //Read Date
                    if (c.getColumnIndex() == readDateCollumn) {
                        System.out.print(c.toString() + "  ");
                        spot.setDate(c.toString());
                    }

                    //Read Time
                    if (c.getColumnIndex() == readTimeCollumn) {
                        System.out.print(c.toString() + "  ");
                        spot.setTime(c.toString());
                    }

                    //Read Rate
                    if (c.getColumnIndex() == readRateCollumn) {
                        System.out.print(c.toString() + "  ");
                        spot.setRating(c.toString());
                    }

                }
                log.debug(spot.toString());
                spotsCheck.put(spot.hashCode(), spot);
            }

            log.debug("##### TESTING ######");
            // SpotsCheck spotCheck = new SpotsCheck("11/29/2015","22:57","6.15");
            SpotsCheck spotDetail = new SpotsCheck();
            spotDetail.setDate("11/29/2015");
            spotDetail.setTime("22:57:01");
            spotDetail.setRating("6.15");
            log.debug(spotDetail.toString());
            log.debug("ContainsKey: " + spotsCheck.containsKey(spotDetail.hashCode()));
            log.debug("ContainsValue: " + spotsCheck.containsValue(spotDetail));

            fis.close();
        }

        end = Instant.now();
        elapsed = Duration.between(start, end);
        log.debug("ELAPSED TIME OF SPOTSCHECKXLSX: " + elapsed);

        return spotsCheck;
    }
}
