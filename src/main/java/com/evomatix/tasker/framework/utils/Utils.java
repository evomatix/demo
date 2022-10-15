package com.evomatix.tasker.framework.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String now(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = localDateTime.format(dateFormat);
        formattedDate= formattedDate.replace(" ","_");
        formattedDate=formattedDate.replace(":","_");
        formattedDate=formattedDate.replace("-","_");
        return formattedDate;
    }
}
