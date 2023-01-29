package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.rpa.scripting.domain.UniversityOffer;
import com.evomatix.tasker.rpa.scripting.pages.conventry.CoventryApplication;
import com.evomatix.tasker.rpa.scripting.pages.conventry.CoventryLogin;
import com.evomatix.tasker.rpa.scripting.pages.greenwitch.GreenwichApplicantSearch;
import com.evomatix.tasker.rpa.scripting.pages.greenwitch.GreenwichApplicationView;
import com.evomatix.tasker.rpa.scripting.pages.greenwitch.GreenwichLogin;
import com.evomatix.tasker.rpa.scripting.pages.uwebristol.UWEBristolApplicantSearch;
import com.evomatix.tasker.rpa.scripting.pages.uwebristol.UWEBristolLogin;

import java.util.HashMap;
import java.util.Map;

public class Greenwich {



    public static void login(ExecutionHandler handler, String userName, String password) {
        handler.open(new CoventryLogin().getUrl(), 3000);

        boolean isHomePage = handler.checkElementPresent(GreenwichLogin.btn_AccountCircle, 5);
        if (!isHomePage) {
            boolean isLoginPage = handler.checkElementPresent(GreenwichLogin.txt_Username, 2);
            if (isLoginPage) {
                handler.type(GreenwichLogin.txt_Username, userName);
                handler.type(GreenwichLogin.txt_Password, password);
                handler.pause(2000);
                handler.click(GreenwichLogin.btn_Login);
                handler.pause(15000);
                isHomePage = handler.checkElementPresent(GreenwichLogin.btn_AccountCircle);
                if (!isHomePage) {
                    throw new ExecutionInterruptedException("Unable to login to Greenwich portal", "Failed - Unable to login to UWE Bristol");
                }
            }
        }
    }

    public static void logout(ExecutionHandler handler) {
        handler.open(new GreenwichLogin().getUrl(), 3000);
        handler.click(GreenwichLogin.btn_AccountCircle);
        handler.click(GreenwichLogin.btn_Logout);
        handler.pause(1000);
    }

    public static void searchStudent(ExecutionHandler handler, String institutionApplicationID) {
        handler.open(new GreenwichApplicantSearch().getUrl(), 500);
        handler.type(GreenwichApplicantSearch.txt_Search, institutionApplicationID);
        handler.click(GreenwichApplicantSearch.btn_Search);
        boolean isStudentFound = handler.checkElementPresent(GreenwichApplicantSearch.txt_Status);

        if (!isStudentFound) {
            throw new ExecutionInterruptedException("Unable to find student record in Greenwich portal", "Student Not Found");
        }
    }

    public static UniversityOffer processApplication(ExecutionHandler handler) {

        UniversityOffer offer = new UniversityOffer();

        //getting offer
        String decision = handler.getText(GreenwichApplicantSearch.txt_Status).trim();
        offer.setDecision(decision);

        if (decision.equals("Pending") || decision.equals("Compliance Team")) {
            throw new ExecutionInterruptedException("Unable to find a decision for student in Greenwich portal", "Cannot Locate a Decision");
        } else if (decision.equals("Information required") || decision.equals("Rejected") || decision.equals("Pending (with faculty)") || decision.equals("Application cancelled")) {

            handler.click(GreenwichApplicantSearch.txt_Status);
            String comment = handler.getText(GreenwichApplicationView.txt_Comment);
            offer.setComment(comment);

        } else if (decision.equals("Conditional") || decision.equals("Unconditional (Academically)")) {

            handler.click(GreenwichApplicantSearch.txt_Status);
            String offerLetter = Greenwich.downloadTheOffer(handler);
            offer.setPdfPath(offerLetter);

        } else if (decision.equals("Incomplete")) {

            handler.click(GreenwichApplicantSearch.txt_Status);
            String comment = handler.getText(GreenwichApplicationView.txt_WarningMessage);
            offer.setComment(comment);

        } else {
            throw new ExecutionInterruptedException("Student found, but retrieved decision [" + decision + "] was unable to map with any existing categories; ", "Error - Check Log");
        }

        return offer;

    }


    private static String downloadTheOffer(ExecutionHandler handler) {

        String currentWindow = handler.getCurrentWindowTitle();
        try {
            handler.click(GreenwichApplicationView.btn_DownloadOffer);
        } catch (Exception e) {
            throw new ExecutionInterruptedException("Offer not found in Greenwich portal", "Offer Not Found");
        }
        String file = handler.waitUntilDonwloadCompleted();
        handler.switchToWindowByTitle(currentWindow);
        return file;
    }



}

