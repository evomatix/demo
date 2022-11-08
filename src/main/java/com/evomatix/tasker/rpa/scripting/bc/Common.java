package com.evomatix.tasker.rpa.scripting.bc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.ExcelManager;
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

	public static String coventry_DownloadTheOffer(ExecutionHandler handler, String studentName) {
		handler.click(CoventryApplication.lnk_Application);
		handler.click(CoventryApplication.lnk_StudentName, Map.of("idf_StudentName", studentName));
		String window =handler.getCurrentWindow();
		handler.click(CoventryApplication.btn_DownloadTheOffer);
		handler.pause(500);
		String file = handler.waitUntilDonwloadCompleted();
		handler.switchWindow(window);
		return file;
	}

	public static void coventry_Logout(ExecutionHandler handler) {
		handler.click(CoventryApplication.lnk_UserName);
		handler.click(CoventryApplication.lnk_Logout);

	}
	public static void adventus_Login(ExecutionHandler handler, String email, String password) {
		handler.open(new AdventusLogin().getUrl(), 3000);
		handler.type(AdventusLogin.txt_Email, email);
		handler.type(AdventusLogin.txt_Password, password);
		handler.click(AdventusLogin.btn_Login);
	}

	public static void adventus_Logout(ExecutionHandler handler) {
		handler.pause(2000);
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
		boolean isOptionAvailable=handler.checkElementPresent(AdventusDocuments.dd_AddDocumentsValue, Map.of("idf_Value", offerType));

		if(isOptionAvailable){
			handler.click(AdventusDocuments.btn_Add);
			handler.handleFileUpload(AdventusDocuments.btn_Upload, filePath);

		}else{
			handler.click(AdventusDocuments.dd_AddAdditionalDocumentsLink, Map.of("idf_Value", offerType));
			handler.handleFileUpload(AdventusDocuments.dd_AddAdditionalDocuments, filePath, Map.of("idf_Value", offerType));

		}
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
		handler.pause(15000);
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

	public static String adventus_getOfferType(ExecutionHandler handler, String pdfFile) {

		SimplePDFReader reader = handler.fileManager.getPDFManager().getSimplePDFReader();
		List<String> lines = reader.extractLineContent(pdfFile);
		String offerType = null;
		for (String line : lines) {
			if (line.contains("Conditional Offer")) {
				offerType ="Conditional Offer";
				break;
			}else if (line.contains("Unconditional Offer")) {
				offerType ="Conditional Offer";
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
			handler.fail("Student id not Found in the downloaded PDF");
		}

		return offerType;
	}


	public static void updateExcelError(ExcelManager excelDataSource, int row, String error){
		Calendar c= Calendar.getInstance();
		excelDataSource.writeExcel(row,14,Common.getToday());
		excelDataSource.writeExcel(row,15,error);
		excelDataSource.writeExcel(row,17,Common.getNextWorkingDay(3));
	}


	public static void updateExcelOutcome(ExcelManager excelDataSource, int row, String outcome){
		excelDataSource.writeExcel(row,14,Common.getToday());
		excelDataSource.writeExcel(row,15,outcome);
	}


	private static String getNextWorkingDay(int days){
		Date date=new Date();
		Calendar calendar = Calendar.getInstance();
		date=calendar.getTime();
		SimpleDateFormat s;
		s=new SimpleDateFormat("MM/dd/yy");

		System.out.println(s.format(date));

		for(int i=0;i<days;)
		{
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			//here even sat and sun are added
			//but at the end it goes to the correct week day.
			//because i is only increased if it is week day
			if(calendar.get(Calendar.DAY_OF_WEEK)<=5)
			{
				i++;
			}

		}
		date=calendar.getTime();
		s=new SimpleDateFormat("MMM dd, yyyy");
		return s.format(date);
	}

	private static String getToday(){

		Date date=new Date();
		SimpleDateFormat s =new SimpleDateFormat("MMM dd, yyyy");
		return s.format(date);
	}

}



