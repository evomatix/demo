package com.evomatix.tasker.rpa.scripting.bc;

import java.util.Map;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.rpa.scripting.pages.AdventusLogin;
import com.evomatix.tasker.rpa.scripting.pages.AdventusStudentStatus;
import com.evomatix.tasker.rpa.scripting.pages.CoventryApplication;
import com.evomatix.tasker.rpa.scripting.pages.CoventryLogin;
import com.evomatix.tasker.rpa.scripting.pages.GoogleHome;

public class Common {
	
    public static void coventry_Login(ExecutionHandler handler, String userName, String password){
        handler.open(new CoventryLogin().getUrl(),3000);
        handler.type(CoventryLogin.txt_UserName, userName);
        handler.type(CoventryLogin.txt_Password, password);
        handler.click(CoventryLogin.btn_Login);
        handler.pause(15000);
    }
    
    public static void coventry_DownloadTheOffer(ExecutionHandler handler, String studentID){
        handler.click(CoventryApplication.lnk_Application);
        handler.click(CoventryApplication.lnk_StudentName,Map.of("idf_StudentID",studentID));
        handler.click(CoventryApplication.btn_DownloadTheOffer);
        handler.waitUntilDonwloadCompleted();


    }

    public static void adventus_Login(ExecutionHandler handler, String email, String password){
        handler.open(new AdventusLogin().getUrl(),3000);
        handler.type(AdventusLogin.txt_Email, email);
        handler.type(AdventusLogin.txt_Password, password);
        handler.click(AdventusLogin.btn_Login);
    }
    
    public static void adventus_SearchStudent(ExecutionHandler handler, String searchText){
        handler.type(AdventusStudentStatus.txt_Search, searchText);
        handler.pause(5000);
    }
    
    public static String adventus_GetStudentName(ExecutionHandler handler, String studentID){
        String studentName = handler.getText(AdventusStudentStatus.lnk_StudentNameByID, Map.of("idf_StudentID",studentID));
        return studentName;
    }
    
    public static void adventus_ClickOnAppStage(ExecutionHandler handler, String appStage, String studentID, String studentname){
        handler.click(AdventusStudentStatus.lnk_AppStages,Map.of("idf_AppStage",appStage));
        handler.pause(5000);
        handler.click(AdventusStudentStatus.lnk_StudentName,Map.of("idf_StudentID",studentID,"idf_StudentName",studentname));

    }
    
    public static void adventus_UploadOfferLetter(ExecutionHandler handler, String studentID, String studentname){
        handler.click(AdventusStudentStatus.lnk_StudentName,Map.of("idf_StudentID",studentID,"idf_StudentName",studentname));
        handler.click(AdventusStudentStatus.lnk_Application);
        

    }
    
    
}
