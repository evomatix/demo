package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.SimplePDFReader;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.rpa.scripting.pages.*;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Adventus {

    public static void adventus_Login(ExecutionHandler handler, String email, String password) {
        handler.open(new AdventusLogin().getUrl(), 3000);

        boolean isHomePage = handler.checkElementPresent(AdventusLogin.btn_AccountCircle,5);
        if(!isHomePage){
            boolean isLoginPage = handler.checkElementPresent(AdventusLogin.txt_Email,1);
            if(isLoginPage){
                handler.type(AdventusLogin.txt_Email, email);
                handler.type(AdventusLogin.txt_Password, password);
                handler.click(AdventusLogin.btn_Login);
                isHomePage = handler.checkElementPresent( AdventusLogin.btn_AccountCircle);

                if(!isHomePage){
                    throw new ExecutionInterruptedException("Unable to Login to Adventus Portal","Failed - Unable to log into Adventus");
                }
            }

        }
    }

    public static void adventus_Logout(ExecutionHandler handler) {
        handler.open(new AdventusLogin().getUrl(), 3000);
        handler.click(AdventusLogin.btn_AccountCircle);
        handler.click(AdventusLogin.btn_Logout);
        handler.pause(2000);

    }

    public static void adventus_SearchStudent(ExecutionHandler handler, String studentID) {
        //handler.type(AdventusStudentStatus.txt_Search, searchText);
        //handler.pause(5000);
        handler.open("https://app.adventus.io/counsellor/student/"+studentID+"/show",1000);
    }

    public static String adventus_GetStudentName(ExecutionHandler handler, String studentID) {
        //String studentName = handler.getText(AdventusStudentStatus.lnk_StudentNameByID, Map.of("idf_StudentID", studentID));
        String studentName = handler.getText(AdventusStudentStatus.lbl_StudentName);
        return studentName;
    }

    public static void adventus_ClickOnAppStage(ExecutionHandler handler, String appStage, String studentID,
                                                String studentname) {
        handler.click(AdventusStudentStatus.lnk_AppStages, Map.of("idf_AppStage", appStage));
        handler.pause(5000);
        handler.click(AdventusStudentStatus.lnk_StudentName,
                Map.of("idf_StudentID", studentID, "idf_StudentName", studentname));

    }

    public static void adventus_UploadOfferLetter(ExecutionHandler handler, String studentID, String studentname,String offerType, String filePath) {
        //handler.click(AdventusStudentStatus.lnk_StudentName,Map.of("idf_StudentID", studentID, "idf_StudentName", studentname));
        Adventus.adventus_SearchStudent(handler,studentID);
        handler.click(AdventusStudentStatus.lnk_SearchApply);
        handler.click(AdventusStudentStatus.lnk_Documents);
        handler.pause(5000);
        handler.scrollToBottom();
        handler.pause(5000);
        handler.checkElementPresent(AdventusDocuments.lnk_Documents);
        handler.click(AdventusDocuments.lnk_Documents);
        handler.pause(5000);
        handler.scrollToBottom();
        handler.pause(5000);
        handler.checkElementPresent(AdventusDocuments.dd_AddDocuments);
        handler.click(AdventusDocuments.dd_AddDocuments);
        boolean isOptionAvailable=handler.checkElementPresent(AdventusDocuments.dd_AddDocumentsValue, Map.of("idf_Value", offerType));

        if(isOptionAvailable){
            handler.click(AdventusDocuments.dd_AddDocumentsValue, Map.of("idf_Value", offerType));
            handler.click(AdventusDocuments.btn_Add);
            handler.scrollToBottom();
            handler.pause(5000);
            handler.handleFileUpload(AdventusDocuments.btn_Upload, filePath);

        }else{
            handler.click(AdventusDocuments.dd_AddAdditionalDocumentsLink, Map.of("idf_Value", offerType));
            handler.handleFileUpload(AdventusDocuments.dd_AddAdditionalDocuments, filePath, Map.of("idf_Value", offerType));

        }
    }

    public static void adventus_SendMessage(ExecutionHandler handler, String offerType, String courseName) {
        handler.click(AdventusStudentStatus.lnk_StudentInformation);
        handler.click(AdventusStudentStatus.lnk_ActivityOverview);
        handler.type(AdventusShow.txt_MessageBox, "Dear Partner, Please be informed that we have received a "+offerType+" from Coventry University for "+courseName+" and the same has been attached under offer & confirmation documents. We would request you to fulfil all the conditions (If Any) as mentioned in the offer letter and pay the initial deposit as per the requirement from the university. Some universities require the deposit before the university credibility interview and some require deposit only after clearing the credibility interview. Therefore if there are conditions to successfully pass the credibility interview then we would advise you not to pay a deposit till the credibility interview is cleared. It is a mandate that the student should undergo a mock interview with us before going for a university credibility interview. Thank you. - **Disclaimer - This is an automated message generated by Adventus.io. If you have any queries on the offer received please reach out to us. Thank you.");
        handler.click(AdventusShow.btn_Send);
    }


    public static void adventus_updateTask(ExecutionHandler handler, String studentID, String offerType) {
        if(offerType.equals("Full Offer")){
            Adventus.adventus_SearchStudent(handler,studentID);
            handler.click(AdventusStudentStatus.lnk_Task);
            boolean isEnabled = handler.isElementEnabled(AdventusStudentStatus.rdb_4_6);
            if(isEnabled){
                handler.click(AdventusStudentStatus.rdb_4_6);
                boolean isConfirmation = handler.checkElementPresent(AdventusStudentStatus.btn_Confirmation);
                if(isConfirmation){
                    handler.click(AdventusStudentStatus.btn_Confirmation);
                }
                handler.writeToReport("Offer Type is ["+offerType+"] task is updated");
            }
        }else{
            handler.writeToReport("Offer Type is ["+offerType+"] task will not be updated");
        }
    }



    public static void adventus_EditApplication(ExecutionHandler handler, String studentID, String courseTitle, String offerType) {
        handler.click(AdventusStudentStatus.lnk_Application);
        //wait till the element is loaded
        boolean appsAvailable=handler.checkElementPresent(AdventusApplication.btn_Edit);

        if(!appsAvailable){
            throw new ExecutionInterruptedException("No Applications are listed for the student ["+studentID+"] in Advantus portal","Failed - Unable to map offer to course");
        }else{
            handler.writeToReport("Checking for a Application with course title ["+courseTitle+"]");
            boolean isFound= handler.checkElementPresent(AdventusApplication.btn_EditWithCourse,Map.of("title", courseTitle));
            if(isFound){
                handler.click(AdventusApplication.btn_EditWithCourse, Map.of("title", courseTitle));
            }else{
                throw new ExecutionInterruptedException("Unable to Find relevant application for course ["+courseTitle+"] among available applications in advantus","Failed - Unable to map offer to course");
            }

        }

        handler.click(AdventusApplication.btn_EditInstitutionStudentId);
        handler.type(AdventusApplication.txt_InstitutionStudentId, studentID);
        handler.click(AdventusApplication.btn_EditInstitutionStudentId);
        handler.pause(3000);

        boolean isEnabled = handler.isElementEnabled(AdventusApplication.select_OfferType);
        if(isEnabled){

            if(offerType.equals("Full Offer") || offerType.equals("Conditional Offer")){
                handler.select(AdventusApplication.select_OfferType,offerType);
            }else{
                handler.select(AdventusApplication.select_OfferType,"Declined, with reason selected");
            }

            handler.click(AdventusApplication.btn_Update);

        }else{
            handler.writeToReport("Offer dropdown is disabled, skipping the step");
        }

    }



    public static String adventus_getStudentIDFromPDF(ExecutionHandler handler, String pdfFile) {

        SimplePDFReader reader = handler.fileManager.getPDFManager().getSimplePDFReader();
        List<String> lines = reader.extractLineContent(pdfFile);
        String studentID = null;
        for (String line : lines) {
            if (line.contains("Student ID:")) {
                studentID = line.split(":")[1].trim();
                break;
            }
        }
        if (studentID == null) {
            throw new ExecutionInterruptedException("Student id not Found in the downloaded PDF","Failed - Student ID Not Found in PDF");
        }

        return studentID;
    }




    public static String adventus_RenameDownloadedFile(ExecutionHandler handler, String filePath, String prefix) {

        File file = new File(filePath);
        File updatedFile = new File(file.getParentFile().getAbsolutePath()+File.separator+prefix+"_"+file.getName());
        boolean success = file.renameTo(updatedFile);
        if (!success) {
            throw new ExecutionInterruptedException("FAILED to rename downloaded pdf "+file.getName(),"Failed - Unable to Rename PDF File");
        } else {
            System.out.println(updatedFile.getAbsolutePath());
            return updatedFile.getAbsolutePath();
        }
    }


    public static String adventus_getOfferType(ExecutionHandler handler, String pdfFile) {

        SimplePDFReader reader = handler.fileManager.getPDFManager().getSimplePDFReader();
        List<String> lines = reader.extractLineContent(pdfFile);

        String offerType = null;
        for (String line : lines) {
            if (line.contains("Conditional Offer")) {
                offerType ="Conditional Offer";
                break;
            }else if (line.contains("Unconditional Offer")) {
                offerType ="Full Offer";
                break;
            }else if (line.contains("Rejected")) {
                offerType ="Application Outcome";
                break;
            }else if (line.contains("Withdrawn")) {
                offerType ="Application Outcome";
                break;
            }
        }
        if (offerType == null) {
            throw new ExecutionInterruptedException("Student id not Found in the downloaded PDF","Failed - Offer Type not found in PDF");
        }

        return offerType;
    }


}
