package com.evomatix.tasker.rpa.scripting.scripts;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.ExcelManager;
import com.evomatix.tasker.rpa.scripting.bc.Common;

import java.util.List;
import java.util.Map;

public class ProcessOne {

	public static void partOne(ExecutionHandler handler) {

		ExcelManager excelDataSource = handler.fileManager.getExcelManager();
		excelDataSource.openWorkBook(
				"C:\\Personnel\\Adventus\\Project\\tasker\\src\\main\\resources\\Portal Check - RPA Pilot.xlsx");
		List<Map<String, Object>> data = excelDataSource.readExcel("Lodgements");

		for (Map<String, Object> row : data) {
			try {
				Common.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
				Common.adventus_SearchStudent(handler, (String) row.get("Student ID"));
				String studentName = Common.adventus_GetStudentName(handler, (String) row.get("Student ID"));
				System.out.println(studentName);
				Common.coventry_Login(handler, handler.getConfiguration("COVENTRY_USERNAME"),handler.getConfiguration("COVENTRY_PASSWORD"));
				String pdfFile = Common.coventry_DownloadTheOffer(handler, studentName);
				Common.adventus_RenameDownloadedFile(handler);
				String pdfStudentID = Common.adventus_getStudentIDFromPDF(handler, pdfFile);
				System.out.println(pdfStudentID);
				Common.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"),handler.getConfiguration("ADVENTUS_PASSWORD"));
				Common.adventus_UploadOfferLetter(handler, (String) row.get("Student ID"), studentName,handler.getConfiguration("ADVENTUS_OFFERTYPE"));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
