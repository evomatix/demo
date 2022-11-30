package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.rpa.scripting.pages.CoventryApplication;
import com.evomatix.tasker.rpa.scripting.pages.CoventryLogin;

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
                    throw new RuntimeException("Unable to login to Coventry portal");
                }
            }
        }




    }


    public static String coventry_FindTheOffer(ExecutionHandler handler, String studentName) {
        handler.click(CoventryApplication.lnk_Application);

        boolean isStudentFound= handler.checkElementPresent(CoventryApplication.lnk_StudentName, Map.of("idf_StudentName_Upper", studentName.toUpperCase(),"idf_StudentName_Camel", Utils.convertToTitleCaseIteratingChars(studentName),"idf_StudentName_Lower",studentName.toLowerCase()));

        if(!isStudentFound){
            throw new RuntimeException("Student not found in Coventry portal");
        }
        int count =  handler.getElementCount(CoventryApplication.lnk_StudentNames, Map.of("idf_StudentName_Upper", studentName.toUpperCase(),"idf_StudentName_Camel", Utils.convertToTitleCaseIteratingChars(studentName),"idf_StudentName_Lower",studentName.toLowerCase()));
        handler.writeToReport(" Applications count ["+count+"] in Coventry Portal");
        if(count>1){
            handler.writeToReport("Multiple Applications ("+count+") are found in Coventry Portal");
            throw new RuntimeException("MSG: Multiple Applications Found");
        }
        handler.click(CoventryApplication.lnk_StudentName, Map.of("idf_StudentName_Upper", studentName.toUpperCase(),"idf_StudentName_Camel", Utils.convertToTitleCaseIteratingChars(studentName),"idf_StudentName_Lower",studentName.toLowerCase()));
        String application= handler.getText(CoventryApplication.lnk_StudentName, Map.of("idf_StudentName_Upper", studentName.toUpperCase(),"idf_StudentName_Camel", Utils.convertToTitleCaseIteratingChars(studentName),"idf_StudentName_Lower",studentName.toLowerCase()));
        return application.split("/")[1].trim();
    }



    public static String coventry_DownloadTheOffer(ExecutionHandler handler) {

        String  currentWindow = handler.getCurrentWindowTitle();

        try{
            handler.click(CoventryApplication.btn_DownloadTheOffer);
        }catch (Exception e){
            throw new RuntimeException("Offer not found in Coventry portal",e);
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
