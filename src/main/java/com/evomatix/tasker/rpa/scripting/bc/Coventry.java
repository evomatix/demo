package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.rpa.scripting.pages.CoventryApplication;
import com.evomatix.tasker.rpa.scripting.pages.CoventryLogin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Coventry {

    public static void coventry_Login(ExecutionHandler handler, String userName, String password) {
        handler.open(new CoventryLogin().getUrl(), 3000);

        boolean isHomePage = handler.checkElementPresent(CoventryApplication.lnk_UserName,5);
        if(!isHomePage){
            boolean isLoginPage = handler.checkElementPresent(CoventryLogin.txt_UserName,2);
            if(isLoginPage){
                handler.type(CoventryLogin.txt_UserName, userName);
                handler.type(CoventryLogin.txt_Password, password);
                handler.pause(2000);
                handler.click(CoventryLogin.btn_Login);
                handler.pause(15000);
                isHomePage = handler.checkElementPresent(CoventryApplication.lnk_UserName);
                if(!isHomePage){
                    throw new ExecutionInterruptedException("Unable to login to Coventry portal","Failed - Unable to login to Coventry");
                }
            }
        }




    }


    public static void coventry_FindTheOffer(ExecutionHandler handler, String studentName,String courseName, String id) {
        handler.click(CoventryApplication.lnk_Application);

        boolean isStudentFoundWithID=false;

        if(!(id==null || id.equals(""))){
            handler.writeToReport("App id is provided in Excel - Checking for possible matches with Name and ID");
            isStudentFoundWithID=handler.checkElementPresent(CoventryApplication.lnk_StudentNameWithID, Map.of("idf_StudentName_Lower",studentName.toLowerCase(),"idf_AppID",id));

            if(isStudentFoundWithID){
                String application= handler.getText(CoventryApplication.lnk_StudentNameWithID, Map.of("idf_StudentName_Lower",studentName.toLowerCase(),"idf_AppID",id));
                handler.writeToReport(application);
                handler.click(CoventryApplication.lnk_StudentNameWithID, Map.of("idf_StudentName_Lower",studentName.toLowerCase(),"idf_AppID",id));
            }else{
                handler.writeToReport("No Application found with Name and ID - Checking for possible matches only with Name ");
            }

        }else{
            handler.writeToReport("App id is not provided in Excel - Checking for possible matches with Name");
        }

        if(!isStudentFoundWithID){

            //waiting until loads
            handler.checkElementPresent(CoventryApplication.lnk_StudentNames, Map.of("idf_StudentName_Upper", studentName.toUpperCase(),"idf_StudentName_Camel", Utils.convertToTitleCaseIteratingChars(studentName),"idf_StudentName_Lower",studentName.toLowerCase()));

            int count =  handler.getElementCount(CoventryApplication.lnk_StudentNames, Map.of("idf_StudentName_Upper", studentName.toUpperCase(),"idf_StudentName_Camel", Utils.convertToTitleCaseIteratingChars(studentName),"idf_StudentName_Lower",studentName.toLowerCase()));
            handler.writeToReport("No of Applications found in Coventry Portal : ["+count+"]");

            if(count>1){
                handler.writeToReport("Multiple Applications ("+count+") are found in Coventry Portal");
                List<ObjectLocator> applications = handler.getElements(CoventryApplication.lnk_StudentNames, Map.of("idf_StudentName_Upper", studentName.toUpperCase(), "idf_StudentName_Camel", Utils.convertToTitleCaseIteratingChars(studentName), "idf_StudentName_Lower", studentName.toLowerCase()));
                boolean isNotFound=true;
                List<String> unmatched = new ArrayList<String>();
                for (ObjectLocator applicationEntry:applications) {
                   String application = handler.getText(applicationEntry);
                   String coventryCourseTitle = application.split("/")[1].trim();
                    if(courseName.contains(coventryCourseTitle)){
                        handler.click(applicationEntry);
                        handler.writeToReport("Found matching Application "+application);
                        isNotFound=false;
                        break;
                    }else{
                        unmatched.add("["+application+"]");
                    }
                }
                if(isNotFound){
                    throw new ExecutionInterruptedException("No matching applications Found in coventry portal "+ String.join(",", unmatched),"Failed - Multiple Applications, No Matching Course found in Coventry");
                }

            }else if(count==0){
                throw new ExecutionInterruptedException("Student not found in Coventry portal", "Failed - Student not found in Coventry");
            }

            String application= handler.getText(CoventryApplication.lnk_StudentName, Map.of("idf_StudentName_Upper", studentName.toUpperCase(),"idf_StudentName_Camel", Utils.convertToTitleCaseIteratingChars(studentName),"idf_StudentName_Lower",studentName.toLowerCase()));
            String coventryCourseTitle = application.split("/")[1].trim();
            if(courseName.contains(coventryCourseTitle)){
                handler.click(CoventryApplication.lnk_StudentName, Map.of("idf_StudentName_Upper", studentName.toUpperCase(),"idf_StudentName_Camel", Utils.convertToTitleCaseIteratingChars(studentName),"idf_StudentName_Lower",studentName.toLowerCase()));
                handler.writeToReport(application);
            }else{
                throw new ExecutionInterruptedException("The expected course name ["+courseName+"] did not not matched with the Student record found in Coventry portal with course ["+application.split("/")[1].trim()+"] - [matching method: includes]", "Failed - No Matching Course found in Coventry");
            }
        }
    }




    public static String coventry_DownloadTheOffer(ExecutionHandler handler) {

        String  currentWindow = handler.getCurrentWindowTitle();

        try{
            handler.click(CoventryApplication.btn_DownloadTheOffer);
        }catch (Exception e){
            throw new ExecutionInterruptedException("Offer not found in Coventry portal","Offer Not Found");
        }


        String file = handler.waitUntilDonwloadCompleted();
        handler.switchToWindowByTitle(currentWindow);
       // handler.closeAllOtherTabs();
        return file;
    }

    public static void coventry_Logout(ExecutionHandler handler) {
        handler.open(new CoventryLogin().getUrl(), 3000);
        handler.click(CoventryApplication.lnk_UserName);
        handler.click(CoventryApplication.lnk_Logout);
        handler.pause(1000);

    }
}
