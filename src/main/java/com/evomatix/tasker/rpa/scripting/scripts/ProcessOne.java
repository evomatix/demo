package com.evomatix.tasker.rpa.scripting.scripts;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.framework.fileops.ExcelManager;
import com.evomatix.tasker.framework.reporting.LogType;
import com.evomatix.tasker.rpa.scripting.bc.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;

import java.util.List;
import java.util.Map;

public class ProcessOne {

	public static void partOne(ExecutionHandler handler) {

		ExcelManager excelDataSource = handler.fileManager.getExcelManager();
		excelDataSource.openWorkBook(handler.getConfiguration("EXCEL_FILE"),"Data");
		List<Map<String, Object>> data = excelDataSource.readExcel();
		int rowNumber =0;
		int executedRecords = 0;

		for (Map<String, Object> row : data) {
			rowNumber++;
			try {

				//1st check
				if(Utils.isEligible(handler,row)){
					executedRecords++;
					String outcome =ProcessOne.coventryProcess(handler,row, String.valueOf(row.get("Student ID")).split("\\.")[0]);
					ExcelOps.updateExcelOutcome(handler,excelDataSource,rowNumber,outcome);
				}

			} catch (ExecutionInterruptedException e) {
				ExcelOps.updateExcelError(handler,excelDataSource,rowNumber,e.type);
				handler.log(LogType.FAIL,"FAIL",e.getMessage());
				handler.log(LogType.FAIL,"TECH TRACE",ExceptionUtils.getStackTrace(e));
			} catch (Exception e){
				ExcelOps.updateExcelError(handler,excelDataSource,rowNumber,"Failed - Refer Execution Report");
				handler.log(LogType.FAIL,"FAIL",e.getMessage());
				handler.log(LogType.FAIL,"TECH TRACE",ExceptionUtils.getStackTrace(e));
			}
		}

	if(executedRecords>0){
		try {
			Adventus.adventus_Logout(handler);
			Coventry.coventry_Logout(handler);
		}catch (Exception e){
			handler.writeToReport("Final logout is not Successful");
		}
	}

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

			 throw new ExecutionInterruptedException("Unable to retrieve student name form Adventus portal","Failed - Unable to get Adventus Student Name ",e);
		}


		//step 02
		Coventry.coventry_Login(handler, handler.getConfiguration("COVENTRY_USERNAME"),handler.getConfiguration("COVENTRY_PASSWORD"));
		String pdfFile = null;
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

		}catch (ExecutionInterruptedException e){
			throw e;
		}catch (Exception e){
			throw new ExecutionInterruptedException(e.getMessage(),"Failed - Refer Execution Report",e);
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
		}catch (ExecutionInterruptedException e){
			throw e;
		}catch (Exception e){
			throw new ExecutionInterruptedException(e.getMessage(),"Failed - Refer Execution Report",e);
		}finally {
			Utils.cleanupFile(handler,updatedPdfFile);
		}


		return offerType;
	}

}
