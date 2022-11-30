package com.evomatix.tasker.rpa.scripting.scripts;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.ExcelManager;
import com.evomatix.tasker.framework.reporting.LogType;
import com.evomatix.tasker.rpa.scripting.bc.*;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;

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
					String outcome =ProcessOne.coventryProcess(handler,row, String.valueOf(row.get("Student ID")).split("\\.")[0]);
					ExcelOps.updateExcelOutcome(handler,excelDataSource,rowNumber,outcome);
				}

			} catch (Exception e) {
				if(e.getMessage().startsWith("MSG:")){
					ExcelOps.updateExcelError(handler,excelDataSource,rowNumber,"Multiple Applications Found");
				}else{
					ExcelOps.updateExcelError(handler,excelDataSource,rowNumber,"Offer Not Found");
				}
				e.printStackTrace();

				handler.log(LogType.FAIL,"FAIL",e.getMessage());
			}
		}

		Adventus.adventus_Logout(handler);
		Coventry.coventry_Logout(handler);

	}



	public static String coventryProcess(ExecutionHandler handler, Map<String, Object> row, String studentID){

		handler.reporter.startProcess("Student : "+studentID);

		//step 01
		Adventus.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
		String studentName;
		try{
			Adventus.adventus_SearchStudent(handler,studentID);
			studentName = Adventus.adventus_GetStudentName(handler,studentID);
			handler.writeToReport("Student Name : "+studentName);
		}catch (Exception e){
			 throw new RuntimeException("Unable to retrieve student name form",e);
		}


		//step 02
		Coventry.coventry_Login(handler, handler.getConfiguration("COVENTRY_USERNAME"),handler.getConfiguration("COVENTRY_PASSWORD"));
		String pdfFile=null;
		String updatedPdfFile;
		String pdfStudentID;
		String offerType;
		String courseTitle;
		try{
			courseTitle=Coventry.coventry_FindTheOffer(handler,studentName);
			handler.writeToReport("Course Title :"+courseTitle);
			pdfFile = Coventry.coventry_DownloadTheOffer(handler);
			handler.writeToReport("PDF File :"+pdfFile);
			pdfStudentID = Adventus.adventus_getStudentIDFromPDF(handler, pdfFile);
			handler.writeToReport("Student ID from PDF :"+pdfStudentID);
			offerType =Adventus.adventus_getOfferType(handler, pdfFile);
			handler.writeToReport("Offer Type from PDF :"+offerType);
			updatedPdfFile=Adventus.adventus_RenameDownloadedFile(handler,pdfFile,offerType);
			handler.writeToReport("PDF File :"+updatedPdfFile);

		}catch (Exception e){
		//	Utils.switchBackToBaseWindow(handler,currentWindow);
			 throw e;
		}finally {
			Utils.cleanupFile(handler,pdfFile);
		}

		//step 03
		Adventus.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
		try{
			Adventus.adventus_UploadOfferLetter(handler, studentID, studentName,offerType, updatedPdfFile);
			Adventus.adventus_SendMessage(handler, offerType, courseTitle);
			Adventus.adventus_EditApplication(handler, pdfStudentID,courseTitle,offerType);
			Adventus.adventus_updateTask(handler,studentID,offerType);
		}catch (Exception e){
			 throw e;
		}finally {
			Utils.cleanupFile(handler,updatedPdfFile);
		}


		return offerType;
	}

}
