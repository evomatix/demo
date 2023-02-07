package com.evomatix.tasker.rpa.scripting.scripts.processes;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;
import com.evomatix.tasker.framework.fileops.ExcelManager;
import com.evomatix.tasker.rpa.scripting.bc.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Processes {

	public static void tc_enrich_bank_transaction_validation(ExecutionHandler handler) {

		ExcelManager excelDataSource = handler.fileManager.getExcelManager();
		excelDataSource.openWorkBook(handler.getConfiguration("EXCEL_FILE"), "Test Data");
		List<Map<String, Object>> data = excelDataSource.readExcel();
		int rowNumber = 0;
		int executedRecords = 0;

		for (Map<String, Object> testdata : data) {
			rowNumber++;
			try {
				EnrichBank.login(handler, String.valueOf(testdata.get("user_name")), String.valueOf(testdata.get("password")));
				EnrichBank.verify_login(handler);
				EnrichBank.enter_fund_transfer_details(handler, String.valueOf(testdata.get("target_account_no")), String.valueOf(testdata.get("amount")));
				EnrichBank.confirm_fund_transfer_details(handler);


				String transaction_id = EnrichBank.get_transaction_id(handler);

				//Call API - get 4 parameters
				String url = "http://localhost:8080/funds/transfer/" + transaction_id;
				HttpResponse<String> response = handler.callWebRequest(url, "get", new ArrayList<>());
				Map<String, Object> result;

				try {
					result = new ObjectMapper().readValue(response.body(), HashMap.class);
				} catch (Exception e) {
					throw new RuntimeException("Error occurred while reading http response", e);
				}

				String name = (String) result.get("name");
				String sourceAccount = (String) result.get("sourceAccount");
				String target_account_no = (String) result.get("targetAccount");
				String amount = (String) result.get("amount");

				EnrichBank.download_fund_transfer_details_pdf(handler, name, sourceAccount, target_account_no, amount);

			} catch (RuntimeException e) {
				throw new RuntimeException("Error occurred while executing the test script", e);
			}
		}
	}
}
