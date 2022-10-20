package com.evomatix.tasker.framework.fileops;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {


    public void renameFile(String file, String newFileName){

        Path source = Paths.get(file);

        if(Files.exists(source)){

            try{
                Files.move(source, source.resolveSibling(newFileName));
            }catch (Exception e){
                throw new RuntimeException("Unable to Rename file",e);
            }

        }else{
            throw new RuntimeException("File not Exist");
        }

    }


    public String getFileFromResource(String fileName) {

       try{
           ClassLoader classLoader = getClass().getClassLoader();
           URL resource = classLoader.getResource(fileName);
           if (resource == null) {
               throw new IllegalArgumentException("file not found! " + fileName);
           } else {

               // failed if files have whitespaces or special characters
               //return new File(resource.getFile());

               return new File(resource.toURI()).getAbsolutePath();
           }
       }catch (Exception e){
           throw new RuntimeException("Unable to read file from resources folder",e);
       }

    }


    public ExcelManager getExcelManager(){
        return new ExcelManager();
    }

    public PDFManager getPDFManager(){
        return new PDFManager();
    }

}
