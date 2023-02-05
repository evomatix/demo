package com.evomatix.tasker.rpa.scripting.scripts.processes;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.rpa.scripting.bc.Adventus;
import com.evomatix.tasker.rpa.scripting.bc.Greenwich;
import com.evomatix.tasker.rpa.scripting.bc.UWEBristol;
import com.evomatix.tasker.rpa.scripting.bc.Utils;
import com.evomatix.tasker.rpa.scripting.domain.UniversityOffer;
import com.evomatix.tasker.rpa.scripting.mappings.GreenwichMappings;
import com.evomatix.tasker.rpa.scripting.mappings.UWEBristolMappings;
import com.evomatix.tasker.rpa.scripting.pages.advantus.AdventusApplication;
import com.evomatix.tasker.rpa.scripting.pages.advantus.AdventusStudentStatus;

public class UWEBristolProcess {

    public static String UWEBristolProcess(ExecutionHandler handler, String studentID, String courseName,boolean isPK){

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
            handler.click(AdventusStudentStatus.lnk_Application);
            Adventus.viewApplication(handler, studentID, courseName, "University of the West of England Bristol");
            appID=handler.getText(AdventusApplication.txt_InstitutionApplicationId).replace("\n","").replace("edit","");
            handler.writeToReport("App ID :"+appID);
        }catch (Exception e){

            throw new ExecutionInterruptedException("Unable to retrieve student name form Adventus portal","Failed - Unable to get Adventus Student Name ",e);
        }

        //Switching Accounts

        String username =isPK ? handler.getConfiguration("BRISTOL_PK_USERNAME") : handler.getConfiguration("BRISTOL_IN_USERNAME");
        String password =isPK ?handler.getConfiguration("BRISTOL_PK_PASSWORD"): handler.getConfiguration("BRISTOL_IN_PASSWORD");



        //step 02
        UWEBristol.login(handler,username ,password);
        UniversityOffer offer= new UniversityOffer();
        String offerType=null;
        try{

            UWEBristol.searchStudent(handler,appID);
            offer = UWEBristol.processApplication(handler,appID,studentName);

            handler.writeToReport("PDF File :"+offer.getPdfPath());

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
            offerType = UWEBristolMappings.getAdventusDocumentMapping(offer.getDecision());
            if(offerType!=null){
                Adventus.uploadOfferLetter(handler, studentID, studentName,offerType, offer.getUpdatedPDFPath());
            }

            Adventus.editApplication(handler, studentID,courseName,offerType, "University of the West of England Bristol");

            String message = UWEBristolMappings.getAdventusMessageMapping(offer.getDecision());
            if(message!=null){
                message.replace("$University","University of UWE Bristol");
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
