package com.evomatix.tasker.framework.fileops;

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


    public ExcelManager getExcelManager(){
        return new ExcelManager();
    }
}
