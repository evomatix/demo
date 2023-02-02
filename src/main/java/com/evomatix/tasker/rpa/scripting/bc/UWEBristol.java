package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.rpa.scripting.domain.UniversityOffer;
import com.evomatix.tasker.rpa.scripting.pages.conventry.CoventryLogin;
import com.evomatix.tasker.rpa.scripting.pages.greenwitch.GreenwichApplicationView;
import com.evomatix.tasker.rpa.scripting.pages.uwebristol.UWEBristolApplicantSearch;
import com.evomatix.tasker.rpa.scripting.pages.uwebristol.UWEBristolApplicantView;
import com.evomatix.tasker.rpa.scripting.pages.uwebristol.UWEBristolLogin;

import java.util.Locale;
import java.util.Map;

public class UWEBristol {

    public static void login(ExecutionHandler handler, String userName, String password) {
        handler.open(new UWEBristolLogin().getUrl(), 3000);

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

    public static void logout(ExecutionHandler handler) {
        handler.open(new UWEBristolLogin().getUrl(), 3000);
        handler.click(UWEBristolLogin.btn_AccountCircle);
        handler.click(UWEBristolLogin.btn_Logout);
        handler.pause(1000);

    }

    public static void searchStudent(ExecutionHandler handler,String institutionApplicationID){
        handler.click(UWEBristolApplicantSearch.lnk_ReviewApplication);
        handler.type(UWEBristolApplicantSearch.txt_Applicant_Number,institutionApplicationID);
        handler.click(UWEBristolApplicantSearch.btn_Search);
        boolean isStudentFound = handler.checkElementPresent(UWEBristolApplicantSearch.tr_SearchResultViewEdit, Map.of("idf_applicationID", institutionApplicationID));

        if(!isStudentFound){
            throw new ExecutionInterruptedException("Unable to find student record in UWE Bristol portal","Student Not Found");
        }



    }

    public static UniversityOffer processApplication(ExecutionHandler handler, String institutionApplicationID){

        UniversityOffer offer = new UniversityOffer();

        handler.click(UWEBristolApplicantSearch.tr_SearchResultViewEdit,Map.of("idf_applicationID", institutionApplicationID));

        String decision = handler.getText(UWEBristolApplicantSearch.txt_Decision).trim().toLowerCase();

        offer.setDecision(decision);

        String pdf=null;

        if(decision.equals("reject")){
            pdf= UWEBristol.downloadTheOffer( handler,"Emails","Reject Email With Feedback");

        }else if(decision.equals("conditional")||decision.equals("conditional firm accept")){
            pdf =UWEBristol.downloadTheOffer( handler,"Letters","International Conditional Offer");

        }else if(decision.equals("conditional firm accept unconditional firm accept")||decision.equals("unconditional firm accept")){
            pdf =UWEBristol.downloadTheOffer( handler,"Letters","International Unconditional Offer");

        }else if(decision.equals("Further Info Request") ){
            pdf = "";
        } else{
            throw new ExecutionInterruptedException("Student found but, Unable to find a decision for student in UWE Bristol portal","Failed - Cannot Locate a Decision");

        }

        offer.setPdfPath(pdf);

        return offer;



    }


    private static String downloadTheOffer(ExecutionHandler handler,String title, String entry) {

        String currentWindow = handler.getCurrentWindowTitle();

        String pdfFilePath=null;

        try {
            handler.click(UWEBristolApplicantView.lnk_AccordionEntry,Map.of("title", title));
            boolean linkFound = handler.checkElementPresent(UWEBristolApplicantView.lnk_AccordionEntryContent, Map.of("title", title, "entry", entry), 5);
            if (linkFound) {

                handler.click(UWEBristolApplicantView.lnk_AccordionEntryContent, Map.of("title", title, "entry", entry));
                handler.switchToNewlyOpenedTab();
                handler.exportPageAsPDF("/Users/vdhhewapathirana/Storage/Projects/evomatix/out/output.pdf");


            } else {
                throw new ExecutionInterruptedException("Student found but, Unable locate " + title + " with " + entry, "Failed - No " + title + " Found With Expected Title");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            handler.closeCurrentTab();
            handler.switchToWindowByTitle(currentWindow);
        }

        return pdfFilePath;
    }
}
