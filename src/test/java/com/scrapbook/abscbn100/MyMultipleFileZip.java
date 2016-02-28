package com.scrapbook.abscbn100;

/**
 * Created by ronaldteodosio on 1/3/16.
 */


import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MyMultipleFileZip {










    public void zipFiles(List<String> files){

        LocalDateTime manilaDateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String manilaDateTimeText = manilaDateTime.format(formatter);
        final String zipDateTime =  manilaDateTimeText + ".zip";

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        try {
            fos = new FileOutputStream(System.getProperty("user.home") + "/spoton" + zipDateTime);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for(String filePath:files){
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                System.out.println("Zipping the file: "+input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4*1024];
                int size = 0;
                while((size = fis.read(tmp)) != -1){
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try{
                if(fos != null) fos.close();
            } catch(Exception ex){

            }
        }
    }

    public static void main(String a[]){

        LocalDateTime manilaDateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));
        System.out.println("MANILA Times:" + manilaDateTime);

        MyMultipleFileZip mfe = new MyMultipleFileZip();
        List<String> files = new ArrayList<String>();
        files.add(System.getProperty("user.home") + "/testReadStudentsXResult1.xlsx");
        files.add(System.getProperty("user.home") + "/testReadStudentsXResult2.xlsx");
        //files.add(System.getProperty("user.home") + "/port-chn.txt");
        mfe.zipFiles(files);
    }
}
//  See more at: http://www.java2novice.com/java-collections-and-util/zip/multiple/#sthash.5tS2ZQFw.dpuf
