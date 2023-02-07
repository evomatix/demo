package com.evomatix.tasker.rpa.scripting.scripts.processes;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.rpa.scripting.bc.Utils;

public class CoventryProcess {

    public static String coventryProcess(ExecutionHandler handler, String studentID, String courseName, String appID){

        handler.reporter.startProcess("Student : "+studentID);

        //step 01
        Adventus.login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
        String studentName;
        try{
            Adventus.searchStudent(handler,studentID);
            studentName = Adventus.getStudentName(handler,studentID);
            studentName=studentName.replace(".","").replace("\\.","").replace("-","").replace("_","").trim();
            handler.writeToReport("Student Name : "+studentName);
            handler.writeToReport("App ID :"+appID);
        }catch (Exception e){

            throw new ExecutionInterruptedException("Unable to retrieve student name form Adventus portal","Failed - Unable to get Adventus Student Name ",e);
        }


        //step 02
        Coventry.coventry_Login(handler, handler.getConfiguration("COVENTRY_USERNAME"),handler.getConfiguration("COVENTRY_PASSWORD"));
        String pdfFile = null;
        String updatedPdfFile;
        String pdfStudentID;
        String offerType;
        try{
            handler.writeToReport("Course Title :"+courseName);

            Coventry.coventry_FindTheOffer(handler,studentName,courseName,appID);
            pdfFile = Coventry.coventry_DownloadTheOffer(handler);
            handler.writeToReport("PDF File :"+pdfFile);
            pdfStudentID = Adventus.adventus_getStudentIDFromPDF(handler, pdfFile);
            handler.writeToReport("Student ID from PDF :"+pdfStudentID);
            offerType =Adventus.adventus_getOfferType(handler, pdfFile);
            handler.writeToReport("Offer Type from PDF :"+offerType);
            updatedPdfFile=Adventus.adventus_RenameDownloadedFile(handler,pdfFile,offerType);
            handler.writeToReport("PDF File :"+updatedPdfFile);

        }catch (ExecutionInterruptedException e){
            throw e;
        }catch (Exception e){
            throw new ExecutionInterruptedException(e.getMessage(),"Failed - Refer Execution Report",e);
        }finally {
            Utils.cleanupFile(handler,pdfFile);
        }

        //step 03
        Adventus.login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
        try{
            Adventus.uploadOfferLetter(handler, studentID, studentName,offerType, updatedPdfFile);
            Adventus.sendMessage(handler, offerType, courseName);
            Adventus.editApplication(handler, pdfStudentID,courseName,offerType, "Coventry University");
            Adventus.updateTask(handler,studentID,offerType);
        }catch (ExecutionInterruptedException e){
            throw e;
        }catch (Exception e){
            throw new ExecutionInterruptedException(e.getMessage(),"Failed - Refer Execution Report",e);
        }finally {
            Utils.cleanupFile(handler,updatedPdfFile);
        }


        return offerType;
    }


}

