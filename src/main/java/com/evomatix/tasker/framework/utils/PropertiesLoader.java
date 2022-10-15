package com.evomatix.tasker.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties loadProperties(String resourceFileName) {
        Properties configuration = new Properties();
       try{
           if(Files.exists(Paths.get(resourceFileName))){
               InputStream inputStream = new FileInputStream(resourceFileName);
               configuration.load(inputStream);
               inputStream.close();

           }
       }catch (Exception e){
           e.printStackTrace();
       }

        return configuration;
    }
}
