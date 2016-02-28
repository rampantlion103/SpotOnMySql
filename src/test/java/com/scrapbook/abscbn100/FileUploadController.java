package com.scrapbook.abscbn100;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/smv/spoton/result")
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @RequestMapping(value="/100", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/100", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
                                                 @RequestParam("file") MultipartFile file){

        log.info("fileName: "  + file.getOriginalFilename() +"; fileSize:" + file.getSize());

        if (name == null || name.trim().length() == 0) {
            name = file.getOriginalFilename();
        }

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded (" + file.getOriginalFilename() + ")!";
            } catch (Exception e) {
                return "You failed to upload (" + file.getOriginalFilename() + ") => " + e.getMessage();
            }
        } else {
            return "You failed to upload (" + file.getOriginalFilename() + ") because the file was empty.";
        }
    }


    @RequestMapping(value="/100/multipleSave", method=RequestMethod.POST )
    public @ResponseBody String multipleSave(@RequestParam("file") MultipartFile[] files){
        String fileName = null;
        long fileSize = 0;
        String msg = "";
        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    fileSize = files[i].getSize();
                    log.info("fileName: "  + fileName +"; fileSize:" + fileSize);
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName +" with the file size = " + fileSize  + " <br/>";
                } catch (Exception e) {
                    return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
            return msg;
        } else {
            return "Unable to upload. File is empty.";
        }
    }


    @RequestMapping(value="/100/multipleUpload")
    public String multiUpload(){
        return "multipleUpload";
    }


    @RequestMapping(value="/100/download", method=RequestMethod.POST )
    public @ResponseBody String multipleSaveDownload(@RequestParam("file") MultipartFile[] files){
        String fileName = null;
        long fileSize = 0;
        String msg = "";
        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    fileSize = files[i].getSize();
                    log.info("fileName: "  + fileName +"; fileSize:" + fileSize);
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName +" with the file size = " + fileSize  + " <br/>";
                } catch (Exception e) {
                    return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
            return msg;
        } else {
            return "Unable to upload. File is empty.";
        }
    }



/*
    @RequestMapping(value = "/files/{file_name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("file_name") String fileName) {
        return new FileSystemResource(myService.getFileFor(fileName));
    }
*/



    @RequestMapping(value="/100/downloadx", method=RequestMethod.POST )
    public void  multipleSaveDownloadResult(@RequestParam("file") MultipartFile[] files, HttpServletResponse response) throws IOException {
        String fileName = null;
        long fileSize = 0;
        String msg = "";


        String csvFileName = "books.csv";

        response.setContentType("text/csv");

        // creates mock data
        String headerKey = "Content-Disposition";
        //String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);

        String headerValue = String.format("attachment; filename=\"%s\"", files[0].getOriginalFilename());
        response.setHeader(headerKey, headerValue);




        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    fileSize = files[i].getSize();
                    log.info("fileName: "  + fileName +"; fileSize:" + fileSize);
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName +" with the file size = " + fileSize  + " <br/>";
                } catch (Exception e) {
                    //return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
            //return msg;
        }



        /*

        Book book1 = new Book("Effective Java", "Java Best Practices",
            "Joshua Bloch", "Addision-Wesley", "0321356683", "05/08/2008",
            38);

        Book book2 = new Book("Head First Java", "Java for Beginners",
            "Kathy Sierra & Bert Bates", "O'Reilly Media", "0321356683",
            "02/09/2005", 30);

        Book book3 = new Book("Thinking in Java", "Java Core In-depth",
            "Bruce Eckel", "Prentice Hall", "0131872486", "02/26/2006", 45);

        Book book4 = new Book("Java Generics and Collections",
            "Comprehensive guide to generics and collections",
            "Naftalin & Philip Wadler", "O'Reilly Media", "0596527756",
            "10/24/2006", 27);

        List<Book> listBooks = Arrays.asList(book1, book2, book3, book4);

        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
            CsvPreference.STANDARD_PREFERENCE);

        String[] header = { "Title", "Description", "Author", "Publisher",
            "isbn", "PublishedDate", "Price" };

        csvWriter.writeHeader(header);

        for (Book aBook : listBooks) {
            csvWriter.write(aBook, header);
        }

        csvWriter.close();


        */
    }


    @RequestMapping(value="/100/downloadxx", method=RequestMethod.POST )
    public void  multipleSaveDownloadResultxx(@RequestParam("file") MultipartFile[] files, HttpServletResponse response) throws IOException {
        String fileName = null;
        long fileSize = 0;
        String msg = "";


        String csvFileName = "books.csv";

        response.setContentType("text/csv");

        // creates mock data
        String headerKey = "Content-Disposition";
        //String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);

        String headerValue = String.format("attachment; filename=\"%s\"", files[0].getOriginalFilename());
        response.setHeader(headerKey, headerValue);




        if (files != null && files.length >0) {
            for(int i =0 ;i< files.length; i++){
                try {
                    fileName = files[i].getOriginalFilename();
                    fileSize = files[i].getSize();
                    log.info("fileName: "  + fileName +"; fileSize:" + fileSize);
                    byte[] bytes = files[i].getBytes();
                    BufferedOutputStream buffStream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                    buffStream.write(bytes);
                    buffStream.close();
                    msg += "You have successfully uploaded " + fileName +" with the file size = " + fileSize  + " <br/>";
                } catch (Exception e) {
                    //return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
                }
            }
            //return msg;
        }



        /*

        Book book1 = new Book("Effective Java", "Java Best Practices",
            "Joshua Bloch", "Addision-Wesley", "0321356683", "05/08/2008",
            38);

        Book book2 = new Book("Head First Java", "Java for Beginners",
            "Kathy Sierra & Bert Bates", "O'Reilly Media", "0321356683",
            "02/09/2005", 30);

        Book book3 = new Book("Thinking in Java", "Java Core In-depth",
            "Bruce Eckel", "Prentice Hall", "0131872486", "02/26/2006", 45);

        Book book4 = new Book("Java Generics and Collections",
            "Comprehensive guide to generics and collections",
            "Naftalin & Philip Wadler", "O'Reilly Media", "0596527756",
            "10/24/2006", 27);

        List<Book> listBooks = Arrays.asList(book1, book2, book3, book4);

        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
            CsvPreference.STANDARD_PREFERENCE);

        String[] header = { "Title", "Description", "Author", "Publisher",
            "isbn", "PublishedDate", "Price" };

        csvWriter.writeHeader(header);

        for (Book aBook : listBooks) {
            csvWriter.write(aBook, header);
        }

        csvWriter.close();


        */
    }


}
