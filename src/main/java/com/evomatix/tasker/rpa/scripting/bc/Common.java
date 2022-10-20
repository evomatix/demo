package com.evomatix.tasker.rpa.scripting.bc;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.rpa.scripting.pages.AdventusApplication;
import com.evomatix.tasker.rpa.scripting.pages.AdventusDocuments;
import com.evomatix.tasker.framework.fileops.SimplePDFReader;
import com.evomatix.tasker.rpa.scripting.pages.AdventusLogin;
import com.evomatix.tasker.rpa.scripting.pages.AdventusShow;
import com.evomatix.tasker.rpa.scripting.pages.AdventusStudentStatus;
import com.evomatix.tasker.rpa.scripting.pages.CoventryApplication;
import com.evomatix.tasker.rpa.scripting.pages.CoventryLogin;

public class Common {

	public static void coventry_Login(ExecutionHandler handler, String userName, String password) {
		handler.open(new CoventryLogin().getUrl(), 3000);
		handler.type(CoventryLogin.txt_UserName, userName);
		handler.type(CoventryLogin.txt_Password, password);
		handler.click(CoventryLogin.btn_Login);
		handler.pause(15000);
	}

	public static String coventry_DownloadTheOffer(ExecutionHandler handler, String studentID) {
		handler.click(CoventryApplication.lnk_Application);
		handler.click(CoventryApplication.lnk_StudentName, Map.of("idf_StudentID", studentID));
		handler.click(CoventryApplication.btn_DownloadTheOffer);
		String file = handler.waitUntilDonwloadCompleted();
		return file;
	}

	public static void adventus_Login(ExecutionHandler handler, String email, String password) {
		handler.open(new AdventusLogin().getUrl(), 3000);
		handler.type(AdventusLogin.txt_Email, email);
		handler.type(AdventusLogin.txt_Password, password);
		handler.click(AdventusLogin.btn_Login);
	}

	public static void adventus_Logout(ExecutionHandler handler) {
		handler.click(AdventusLogin.btn_AccountCircle);
		handler.click(AdventusLogin.btn_Logout);

	}

	public static void adventus_SearchStudent(ExecutionHandler handler, String searchText) {
		handler.type(AdventusStudentStatus.txt_Search, searchText);
		handler.pause(5000);
	}

	public static String adventus_GetStudentName(ExecutionHandler handler, String studentID) {
		String studentName = handler.getText(AdventusStudentStatus.lnk_StudentNameByID,
				Map.of("idf_StudentID", studentID));
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
		handler.click(AdventusStudentStatus.lnk_StudentName,Map.of("idf_StudentID", studentID, "idf_StudentName", studentname));
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
		handler.click(AdventusDocuments.dd_AddDocumentsValue, Map.of("idf_Value", offerType));
		handler.click(AdventusDocuments.btn_Add);
		handler.click(AdventusDocuments.btn_Upload);
		handler.handleFileUpload(AdventusDocuments.btn_Upload, filePath);
	}

	public static void adventus_SendMessage(ExecutionHandler handler, String offerType, String courceName) {
		handler.click(AdventusStudentStatus.lnk_StudentInformation);
		handler.click(AdventusStudentStatus.lnk_ActivityOverview);
		handler.type(AdventusShow.txt_MessageBox, "Dear Partner, Please be informed that we have received a "+ offerType + " from Coventry University for " + courceName+ " and the same has been attached under offer & confirmation documents. We would request you to fulfil all the conditions as mentioned in the offer letter and pay the initial deposit as per the requirement from the university. Some universities require the deposit before the university credibility interview and some require deposit only after clearing the credibility interview. Therefore if there are conditions to successfully pass the credibility interview then we would advise you not to pay a deposit till the credibility interview is cleared. It is a mandate that the student should undergo a mock interview with us before going for a university credibility interview. Thank you");
		handler.click(AdventusShow.btn_Send);
	}
	
	public static void adventus_EditApplication(ExecutionHandler handler, String studentID) {
		handler.click(AdventusStudentStatus.lnk_Application);
		handler.click(AdventusApplication.btn_Edit);
		handler.click(AdventusApplication.btn_EditInstitutionStudentId);
		handler.type(AdventusApplication.txt_InstitutionStudentId, studentID);
		handler.click(AdventusApplication.btn_EditInstitutionStudentId);
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
			handler.fail("Student id not Found in the downloaded PDF");
		}

		return studentID;
    }
    
    public static String adventus_RenameDownloadedFile(ExecutionHandler handler, String filePath, String prefix) {

        File file = new File(filePath);
        File updatedFile = new File(file.getParentFile().getAbsolutePath()+File.separator+prefix+"_"+file.getName());
        boolean success = file.renameTo(updatedFile);
        if (!success) {
            throw new RuntimeException("FAILED to rename "+file.getName());
        } else {
            System.out.println(updatedFile.getAbsolutePath());
           return updatedFile.getAbsolutePath();
        }
    }


}
