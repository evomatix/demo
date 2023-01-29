package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.rpa.scripting.pages.conventry.CoventryLogin;
import com.evomatix.tasker.rpa.scripting.pages.uwebristol.UWEBristolApplicantSearch;
import com.evomatix.tasker.rpa.scripting.pages.uwebristol.UWEBristolLogin;

import java.util.Map;

public class UWEBristol {

    public static void UWEBristol_Login(ExecutionHandler handler, String userName, String password) {
        handler.open(new CoventryLogin().getUrl(), 3000);

        boolean isHomePage = handler.checkElementPresent(UWEBristolLogin.btn_AccountCircle,5);
        if(!isHomePage){
            boolean isLoginPage = handler.checkElementPresent(UWEBristolLogin.txt_UserID,2);
            if(isLoginPage){
                handler.type(UWEBristolLogin.txt_UserID, userName);
                handler.type(UWEBristolLogin.txt_Password, password);
                handler.pause(2000);
                handler.click(UWEBristolLogin.btn_Login);
                handler.pause(15000);
                isHomePage = handler.checkElementPresent(UWEBristolLogin.btn_AccountCircle);
                if(!isHomePage){
                    throw new ExecutionInterruptedException("Unable to login to UWE Bristol portal","Failed - Unable to login to UWE Bristol");
                }
            }
        }
    }

    public static void coventry_Logout(ExecutionHandler handler) {
        handler.open(new UWEBristolLogin().getUrl(), 3000);
        handler.click(UWEBristolLogin.btn_AccountCircle);
        handler.click(UWEBristolLogin.btn_Logout);
        handler.pause(1000);

    }

    public static void search_Student(ExecutionHandler handler,String institutionApplicationID){
        handler.click(UWEBristolApplicantSearch.lnk_ReviewApplication);
        handler.type(UWEBristolApplicantSearch.txt_Applicant_Number,institutionApplicationID);
        handler.click(UWEBristolApplicantSearch.btn_Search);
        boolean isStudentFound = handler.checkElementPresent(UWEBristolApplicantSearch.tr_SearchResultViewEdit, Map.of("idf_applicationID", institutionApplicationID));

        if(!isStudentFound){
            throw new ExecutionInterruptedException("Unable to find student record in UWE Bristol portal","Student Not Found");
        }

        handler.click(UWEBristolApplicantSearch.tr_SearchResultViewEdit,Map.of("idf_applicationID", institutionApplicationID));


    }

    public static void processApplication(ExecutionHandler handler){

        String decision = handler.getText(UWEBristolApplicantSearch.txt_Decision).trim();

        if(decision.equals("")){
            throw new ExecutionInterruptedException("Unable to find a decision for student in UWE Bristol portal","CANNOT LOCATE A DECISION");
        }



    }
}
