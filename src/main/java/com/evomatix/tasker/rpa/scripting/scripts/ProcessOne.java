package com.evomatix.tasker.rpa.scripting.scripts;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.ExcelManager;
import com.evomatix.tasker.framework.reporting.LogType;
import com.evomatix.tasker.rpa.scripting.bc.Common;
import com.evomatix.tasker.rpa.scripting.bc.ExcelOps;

import java.util.List;
import java.util.Map;

public class ProcessOne {

	public static void partOne(ExecutionHandler handler) {

		ExcelManager excelDataSource = handler.fileManager.getExcelManager();
		excelDataSource.openWorkBook(handler.getConfiguration("EXCEL_FILE"),"Data");
		List<Map<String, Object>> data = excelDataSource.readExcel();
		int rowNumber =0;

		for (Map<String, Object> row : data) {
			rowNumber++;
			try {

				//1st check
				if(String.valueOf(row.get("Insto Name")).equals(handler.getConfiguration("UNIVERSITY_NAME"))
						&& (row.get("Checked Date")==null || String.valueOf(row.get("Checked Date")).trim().equals(""))){
					String outcome =ProcessOne.coventryProcess(handler,row);
				//	ExcelOps.updateExcelOutcome(handler,excelDataSource,rowNumber,outcome);
				}

			} catch (Exception e) {
			//	ExcelOps.updateExcelError(handler,excelDataSource,rowNumber,"Offer Not Found");
				e.printStackTrace();

				handler.log(LogType.FAIL,"FAIL",e.getMessage());
			}
		}

	}



	public static String coventryProcess(ExecutionHandler handler, Map<String, Object> row){
		String studentID = String.valueOf(row.get("Student ID")).split("\\.")[0];
		handler.reporter.startProcess("Student : "+studentID);

		//step 01
	/**	Common.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
		String studentName;
		try{

			Common.adventus_SearchStudent(handler,studentID);
			studentName = Common.adventus_GetStudentName(handler,studentID);
			handler.writeToReport("Student Name :"+studentName);
		}catch (Exception e){
			Common.adventus_Logout(handler); throw e;
		}
		Common.adventus_Logout(handler);

		//step 02
		Common.coventry_Login(handler, handler.getConfiguration("COVENTRY_USERNAME"),handler.getConfiguration("COVENTRY_PASSWORD"));
		String pdfFile;
		String pdfStudentID;
		String offerType;
		try{
			pdfFile = Common.coventry_DownloadTheOffer(handler, studentName);
			pdfFile=Common.adventus_RenameDownloadedFile(handler,pdfFile,handler.getConfiguration("ADVENTUS_OFFERTYPE"));
			pdfStudentID = Common.adventus_getStudentIDFromPDF(handler, pdfFile);
			handler.writeToReport("Extracted student ID :"+pdfStudentID);
			offerType =Common.adventus_getOfferType(handler, pdfFile);
			handler.writeToReport("Offer Type :"+offerType);
		}catch (Exception e){
			Common.coventry_Logout(handler); throw e;
		}
		Common.coventry_Logout(handler);
**/
		//step 03
		Common.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
		try{

		//	Common.adventus_UploadOfferLetter(handler, studentID, studentName,offerType, pdfFile);
			Common.adventus_UploadOfferLetter(handler, studentID, "Manir Hossain","Conditional Offer", "/home/rpa-poc/Downloads/Manir Hossain 13332686 EECT009 1_2023 Offer Letter.docx.pdf");
		//	Common.adventus_SendMessage(handler, "Offer Type", "Cource Name");
		//	Common.adventus_EditApplication(handler, pdfStudentID);
		}catch (Exception e){
			Common.adventus_Logout(handler); throw e;
		}
		Common.adventus_Logout(handler);

		return null;//offerType;
	}

}
