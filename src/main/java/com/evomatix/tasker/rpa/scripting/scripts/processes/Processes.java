package com.evomatix.tasker.rpa.scripting.scripts.processes;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.ExcelManager;
import com.evomatix.tasker.rpa.scripting.bc.*;
import com.evomatix.tasker.rpa.scripting.pages.EnrichBank.EnrichBank_FundTransferDetails;


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

				//extracting data from UI

				String transactionId = EnrichBank.get_transaction_id(handler);

				HashMap<String,String> uiAttributes = new HashMap<>();
				uiAttributes.put("name",handler.getElementAttribute(EnrichBank_FundTransferDetails.txt_Name,"value"));
				uiAttributes.put( "sourceAccount",handler.getElementAttribute(EnrichBank_FundTransferDetails.txt_SourceAccountNo, "value"));
				uiAttributes.put("targetAccount", handler.getElementAttribute(EnrichBank_FundTransferDetails.txt_TargetAccountNo, "value"));
				uiAttributes.put("amount", handler.getElementAttribute(EnrichBank_FundTransferDetails.txt_Amount,"value"));

				//validations
				EnrichBank.validate_API(handler,transactionId,uiAttributes);
				EnrichBank.validate_MessageQueue(handler,transactionId,uiAttributes);
				EnrichBank.validate_PDF(handler,uiAttributes);

			} catch (RuntimeException e) {
				throw new RuntimeException("Error occurred while executing the test script", e);
			}
		}
	}
}
