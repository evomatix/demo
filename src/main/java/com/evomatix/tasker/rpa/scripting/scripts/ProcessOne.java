package com.evomatix.tasker.rpa.scripting.scripts;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.ExcelManager;
import com.evomatix.tasker.rpa.scripting.bc.Common;

import java.util.List;
import java.util.Map;

public class ProcessOne {

	public static void partOne(ExecutionHandler handler) {

		ExcelManager excelDataSource = handler.fileManager.getExcelManager();
		excelDataSource.openWorkBook("/Users/vdhhewapathirana/Storage/Projects/evomatix/tasker/src/main/resources/Portal Check - RPA Pilot.xlsx");
		List<Map<String, Object>> data = excelDataSource.readExcel("Lodgements");

		for (Map<String, Object> row:data) {

			try{
				Common.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"), handler.getConfiguration("ADVENTUS_PASSWORD"));
				Common.adventus_SearchStudent(handler, (String) row.get("Student ID"));
				String studentName = Common.adventus_GetStudentName(handler, (String) row.get("Student ID"));
				System.out.println(studentName);
				Common.coventry_Login(handler, handler.getConfiguration("COVENTRY_USERNAME"), handler.getConfiguration("COVENTRY_PASSWORD"));
				String pdfFile =Common.coventry_DownloadTheOffer(handler, studentName);
				String pdfStudentID=Common.adventus_getStudentIDFromPDF(handler,pdfFile);
				System.out.println(pdfStudentID);

			}catch (Exception e){
				e.printStackTrace();
			}
		}
		

	}




	//Example data driven from Excel and Rename files
	//need to update params

	public static void parTwo(ExecutionHandler handler) {

		ExcelManager excelDataSource = handler.fileManager.getExcelManager();
		excelDataSource.openWorkBook("<excel_file_path>");
		List<Map<String, Object>> data = excelDataSource.readExcel("<sheetName>");

		for (Map<String, Object> row:data) {
			Common.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"), handler.getConfiguration("ADVENTUS_PASSWORD"));
			//acess data from sheet
			Common.adventus_SearchStudent(handler, (String) row.get("Student ID"));
			String studentName = Common.adventus_GetStudentName(handler, (String) row.get("Student ID"));
			System.out.println(studentName);
			Common.coventry_Login(handler, "jayatakker@adventus.io", "Adventus@123");
			Common.coventry_DownloadTheOffer(handler, studentName);
		}

			//rename file

			handler.fileManager.renameFile("c:/download/downloaded_pdf.pdf","new_name.pdf");






	}

}
