package com.evomatix.tasker.rpa.scripting.scripts.processes;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.rpa.scripting.bc.Utils;
import com.evomatix.tasker.rpa.scripting.domain.UniversityOffer;
import com.evomatix.tasker.rpa.scripting.mappings.GreenwichMappings;
import com.evomatix.tasker.rpa.scripting.pages.advantus.AdventusApplication;

public class GreenwichProcess {

    public static String GreenwichProcess(ExecutionHandler handler, String studentID, String courseName, String universityName){

        handler.reporter.startProcess("Student : "+studentID);

        //step 01
        Adventus.login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
        String studentName;
        String appID=null;
        try{
            Adventus.searchStudent(handler,studentID);
            studentName = Adventus.getStudentName(handler,studentID);
            studentName=studentName.replace(".","").replace("\\.","").replace("-","").replace("_","").trim();
            handler.writeToReport("Student Name : "+studentName);
            handler.writeToReport("Course Title :"+courseName);
            //handler.click(AdventusStudentStatus.lnk_Application);
            Adventus.viewApplication(handler, studentID, courseName, universityName);
            appID=handler.getText(AdventusApplication.txt_InstitutionApplicationId).replace("\n","").replace("edit","");
            handler.writeToReport("App ID :"+appID);
        }catch (Exception e){

            throw new ExecutionInterruptedException("Unable to retrieve student name form Adventus portal","Failed - Unable to get Adventus Student Name ",e);
        }


        //step 02
        Greenwich.login(handler, handler.getConfiguration("GREENWICH_USERNAME"),handler.getConfiguration("GREENWICH_PASSWORD"));
        UniversityOffer offer= new UniversityOffer();
        String offerType=null;
        try{

            Greenwich.searchStudent(handler,appID);
            offer = Greenwich.processApplication(handler);
            String updatedPdfFile=Utils.addPrefixToFileName(handler,offer.getPdfPath(),offer.getDecision());
            offer.setPdfPath(updatedPdfFile);
            handler.writeToReport("PDF File :"+updatedPdfFile);

        }catch (ExecutionInterruptedException e){
            throw e;
        }catch (Exception e){
            throw new ExecutionInterruptedException(e.getMessage(),"Failed - Refer Execution Report",e);
        }finally {
            Utils.cleanupFile(handler,offer.getPdfPath());
        }

        //step 03
        Adventus.login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
        try{
            offerType = GreenwichMappings.getAdventusDocumentMapping(offer.getDecision());
            if(offerType!=null){
                Adventus.uploadOfferLetter(handler, studentID, studentName,offerType, offer.getUpdatedPDFPath());
            }

            Adventus.editApplication(handler,studentID, courseName,offerType, universityName);

            String message = GreenwichMappings.getAdventusMessageMapping(offer.getDecision());
            if(message!=null){
                message.replace("$University","University of Greenwich");
                message.replace("$Course",courseName);
                Adventus.sendMessage(handler, message);
            }
            Adventus.updateTask(handler,studentID,offerType);



        }catch (ExecutionInterruptedException e){
            throw e;
        }catch (Exception e){
            throw new ExecutionInterruptedException(e.getMessage(),"Failed - Refer Execution Report",e);
        }finally {
            Utils.cleanupFile(handler,offer.getUpdatedPDFPath());
        }



        return offerType;
    }


}
