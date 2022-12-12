package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.ExcelManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExcelOps {

    public static void updateExcelError(ExecutionHandler handler, ExcelManager excelDataSource, int row, String error){
        Calendar c= Calendar.getInstance();
        excelDataSource.writeExcel(row,13,handler.getConfiguration("OFFICER_NAME"));
        excelDataSource.writeExcel(row,14,ExcelOps.getToday());
        excelDataSource.writeExcel(row,15,error);
        excelDataSource.writeExcel(row,17,ExcelOps.getNextWorkingDay(3));
    }


    public static void updateExcelOutcome(ExecutionHandler handler,ExcelManager excelDataSource, int row, String outcome){
        excelDataSource.writeExcel(row,13,handler.getConfiguration("OFFICER_NAME"));
        excelDataSource.writeExcel(row,14,ExcelOps.getToday());
        excelDataSource.writeExcel(row,15,outcome);
    }


    private static String getNextWorkingDay(int days){
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        date=calendar.getTime();
        SimpleDateFormat s;
        s=new SimpleDateFormat("dd/MM/yyyy");

        System.out.println(s.format(date));

        for(int i=0;i<days;)
        {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            //here even sat and sun are added
            //but at the end it goes to the correct week day.
            //because i is only increased if it is week day
            if(calendar.get(Calendar.DAY_OF_WEEK)<=5)
            {
                i++;
            }

        }
        date=calendar.getTime();
        s=new SimpleDateFormat("dd/MM/yyyy");
        return s.format(date);
    }

    private static String getToday(){

        Date date=new Date();
        SimpleDateFormat s =new SimpleDateFormat("dd/MM/yyyy");
        return s.format(date);
    }

}
