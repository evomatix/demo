package com.evomatix.tasker.rpa.scripting.scripts;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.ExcelManager;
import com.evomatix.tasker.framework.reporting.LogType;
import com.evomatix.tasker.rpa.scripting.bc.Common;

import java.util.List;
import java.util.Map;

public class ProcessOne {

	public static void partOne(ExecutionHandler handler) {

		ExcelManager excelDataSource = handler.fileManager.getExcelManager();
		excelDataSource.openWorkBook(handler.fileManager.getFileFromResource("Portal Check - RPA Pilot.xlsx"));
		List<Map<String, Object>> data = excelDataSource.readExcel("Lodgements");
		int count =0;
		int raw =0;
		for (Map<String, Object> row : data) {
			count++;
			try {

				if(String.valueOf(row.get("Insto Name")).equals(handler.getConfiguration("UNIVERSITY_NAME")) && String.valueOf(row.get("Execution Status")).equals("null")){
					raw++;
					handler.reporter.startProcess("["+raw+"] Student :"+  (String) row.get("Student Name") +" - "+(String) row.get("Student ID"));

					//step 01
					Common.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
					String studentName;
					try{
						Common.adventus_SearchStudent(handler, (String) row.get("Student ID"));
						studentName = Common.adventus_GetStudentName(handler, (String) row.get("Student ID"));
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

					//step 03
					Common.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
					try{
						Common.adventus_UploadOfferLetter(handler, (String) row.get("Student ID"), studentName,offerType, pdfFile);
						Common.adventus_SendMessage(handler, "Offer Type", "Cource Name");
						Common.adventus_EditApplication(handler, pdfStudentID);
					}catch (Exception e){
						Common.adventus_Logout(handler); throw e;
					}
					Common.adventus_Logout(handler);
				}

			} catch (Exception e) {
				e.printStackTrace();
				handler.log(LogType.FAIL,"FAIL",e.getMessage());
			}
		}

	}

}
