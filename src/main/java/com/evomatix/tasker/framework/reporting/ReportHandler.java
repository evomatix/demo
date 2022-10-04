package com.evomatix.tasker.framework.reporting;

import com.evomatix.tasker.framework.utils.Utils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReportHandler {


    private ExtentReports extent;

    private ExtentTest test;

    Path reportPath;

    public void initReporting(){


        reportPath = Paths.get("./reports/ExecutionReport_"+ Utils.now()+".html");
        extent = new ExtentReports(reportPath.toString());
    }


    public void startProcess(String process){
        test = extent.startTest(process);
    }


    public void log(LogType logType,String stepName, String details){
       try{

           switch (logType){

               case FAIL:
                   test.log(LogStatus.FAIL,stepName ,details);
                   break;
               case PASS:
                   test.log(LogStatus.PASS,stepName ,details);
                   break;

               case START_PROCESS:
                   this.startProcess(stepName+" "+details);
                   break;

           }
       }catch (Exception e){
           if(test==null){
               this.startProcess("Process-"+Utils.now());
               this.log(logType,stepName,details);
           }else{
               e.printStackTrace();
           }
       }
    }


    public void endReporting(){
        extent.flush();
     //   this.cleanupReports();


    }


    public void cleanupReports(){
        try {
            String read = Files.readAllLines(this.reportPath).toString();
            read=read.replace("Test","Process");
            Files.write(this.reportPath, read.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
