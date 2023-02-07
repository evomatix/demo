package com.evomatix.tasker.rpa.scripting.scripts.processes;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.rpa.scripting.bc.*;

public class Processes {

	public static void tc_enrich_bank_transaction_validation(ExecutionHandler handler){
		EnrichBank.login(handler,"","");
		EnrichBank.verify_login(handler);
		EnrichBank.enter_fund_transfer_details(handler,"","");
		//Call API - get following 4 parameters
		String name = ""; //API.name();
		String souorce_account_no = ""; //API.souorce_account_no();
		String target_account_no = ""; //API.target_account_no();
		String amount = ""; //API.amount();

		EnrichBank.confirm_fund_transfer_details(handler,name,souorce_account_no,target_account_no,amount);

		String transaction_id = EnrichBank.get_transaction_id(handler);

		EnrichBank.download_fund_transfer_details_pdf(handler);
		EnrichBank.logout();
	}

}
