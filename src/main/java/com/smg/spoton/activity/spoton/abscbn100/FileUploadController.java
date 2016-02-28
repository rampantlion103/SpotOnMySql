package com.smg.spoton.activity.spoton.abscbn100;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping(value="/smg/result/abscbn100")
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @RequestMapping(value="/download", method=RequestMethod.POST )
    public void  downloadSpotOn(@RequestParam("file") MultipartFile[] files,
                                @RequestParam(value="date") String filterDate,
                                @RequestParam(value="enddate") String filterEndDate,
                                HttpServletResponse response) throws IOException {

        log.debug("*****Filtering Date: " + filterDate);
        log.debug("*****Filtering Date: " + filterEndDate);
        //Elapsed Time Start
        Instant start = Instant.now();
        log.debug("DOWNLOADSPOTON START TIME: " + String.valueOf(start));

        //#########################################################################
        //UPLOADING
        //#########################################################################
        String fileName = null;
        long fileSize = 0;
        String msg = "";

        if (files != null && files.length > 0) {
            for(int i =0; i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    fileSize = files[i].getSize();
                    log.info("fileName: "  + fileName +"; fileSize:" + fileSize);
                    byte[] bytes = files[i].getBytes();
                 //   BufferedOutputStream buffStream =
                 //   new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                 //   buffStream.write(bytes);
                 //   buffStream.close();
                    msg += "You have successfully uploaded " + fileName +" with the file size = " + fileSize  + " <br/>";
                } catch (Exception e) {
                    //return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
        }

        //#########################################################################
        // PROCESSING
        //#########################################################################
        // Testing: SpotsCheck Bean
        SpotsCheck.test();

        // Creating the SpotsCheck List from file[0]: Spot Worksheet
        Map<Integer,SpotsCheck> spotsCheck = null;
        if (files != null && files.length > 0 && files[0] != null) {
            spotsCheck = SpotsCheckXLSX.main(files[0]);
        }

        // Uploading the ABS CBN Excel file (text format only, no macros)
        // and spot checking with SpotCheck from Kantar Run
        XSSFWorkbook[] workbooks = new XSSFWorkbook[2];
        if (files != null && files.length > 1 && files[1] != null) {
            workbooks = ReadFileWriteResultsXLSX.main(files[1], filterDate, filterEndDate, spotsCheck);
        }

        //#########################################################################
        // DOWNLOADING
        //#########################################################################
        String resultName = files[1].getOriginalFilename().replaceAll(".xlsx","");
        String folderName = "SpotOnResult_" + resultName + ".zip";
        String fileName0 = resultName + "_MatchedSpots.xlsx";
        String fileName1 = resultName  + "_UnatchedSpots.xlsx";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", folderName);
        response.setContentType("application/zip");
        response.setHeader(headerKey, headerValue);

        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        out.putNextEntry(new ZipEntry(fileName0));
        ByteArrayOutputStream bos0 = new ByteArrayOutputStream();
        workbooks[0].write(bos0);
        bos0.writeTo(out);
        out.closeEntry();

        out.putNextEntry(new ZipEntry(fileName1));
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        workbooks[1].write(bos1);
        bos1.writeTo(out);
        out.closeEntry();

        // Add other entries as needed
        out.close();


        //#########################################################################
        //TO DO
        //#########################################################################
        /*
        check the
         */

        //Elapsed Time End
        Instant end = Instant.now();
        Duration elapsed = Duration.between(start,end);
        log.debug("DOWNLOADSPOTON ELAPSED TIME:" + elapsed);

    }

}
