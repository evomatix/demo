package com.evomatix.tasker.rpa.scripting.scripts;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.framework.fileops.ExcelManager;
import com.evomatix.tasker.framework.reporting.LogType;
import com.evomatix.tasker.rpa.scripting.bc.*;
import com.evomatix.tasker.rpa.scripting.scripts.processes.CoventryProcess;
import com.evomatix.tasker.rpa.scripting.scripts.processes.GreenwichProcess;
import com.evomatix.tasker.rpa.scripting.scripts.processes.UWEBristolProcess;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;
import java.util.Map;

public class Processes {

	public static void coventryOfferCheck(ExecutionHandler handler) {

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
					String outcome = CoventryProcess.coventryProcess(handler, String.valueOf(row.get("Student ID")).split("\\.")[0], String.valueOf(row.get("Course Name")), String.valueOf(row.get("App ID")).split("\\.")[0]);
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
			Adventus.logout(handler);
			Coventry.coventry_Logout(handler);
		}catch (Exception e){
			handler.writeToReport("Final logout is not Successful");
		}
	}

	}




	public static void greenwichOfferCheck(ExecutionHandler handler) {

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
					String outcome = GreenwichProcess.GreenwichProcess(handler, String.valueOf(row.get("Student ID")).split("\\.")[0], String.valueOf(row.get("Course Name")));
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
				Adventus.logout(handler);
				Greenwich.logout(handler);
			}catch (Exception e){
				handler.writeToReport("Final logout is not Successful");
			}
		}

	}


	public static void UWEBristolOfferCheck(ExecutionHandler handler) {

		ExcelManager excelDataSource = handler.fileManager.getExcelManager();
		excelDataSource.openWorkBook(handler.getConfiguration("EXCEL_FILE"),"Combined Test Data - 27th Jan");
		List<Map<String, Object>> data = excelDataSource.readExcel();
		int rowNumber =0;
		int executedRecords = 0;

		for (Map<String, Object> row : data) {
			rowNumber++;
			try {

				//1st check
				if(Utils.isEligible(handler,row)){
					executedRecords++;
					String outcome = UWEBristolProcess.UWEBristolProcess(handler, String.valueOf(row.get("Student ID")).split("\\.")[0], String.valueOf(row.get("Course Name")));
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
				Adventus.logout(handler);
				Greenwich.logout(handler);
			}catch (Exception e){
				handler.writeToReport("Final logout is not Successful");
			}
		}

	}





}
