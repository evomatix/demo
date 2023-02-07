package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.rpa.scripting.pages.EnrichBank.EnrichBank_FundTransferDetails;
import com.evomatix.tasker.rpa.scripting.pages.EnrichBank.EnrichBank_Login;

public class EnrichBank {

    public static void login(ExecutionHandler handler, String user_name, String password) {
        handler.open(new EnrichBank_Login().getUrl(),3000);
        handler.type(EnrichBank_Login.txt_UserName, user_name);
        handler.type(EnrichBank_Login.txt_Password, password);
        handler.click(EnrichBank_Login.btn_Login);
    }
    public static void verify_login(ExecutionHandler handler) {
        handler.checkElementPresent(EnrichBank_FundTransferDetails.lbl_FundTransferDetails);
    }
    public static void enter_fund_transfer_details(ExecutionHandler handler, String target_account_no, String amount) {
        handler.type(EnrichBank_FundTransferDetails.txt_TargetAccountNo, target_account_no);
        handler.type(EnrichBank_FundTransferDetails.txt_Amount, amount);
        handler.click(EnrichBank_FundTransferDetails.btn_Transfer);
        handler.checkElementPresent(EnrichBank_FundTransferDetails.lbl_FundTransferConfirmation);
    }

    public static void confirm_fund_transfer_details(ExecutionHandler handler, String name, String source_account_no,String target_account_no, String amount) {
        String name_ui = handler.getElementAttribute(EnrichBank_FundTransferDetails.txt_Name,"value");
        String source_account_no_ui = handler.getElementAttribute(EnrichBank_FundTransferDetails.txt_SourceAccountNo, "value");
        String target_account_no_ui = handler.getElementAttribute(EnrichBank_FundTransferDetails.txt_TargetAccountNo, "value");
        String amount_ui = handler.getElementAttribute(EnrichBank_FundTransferDetails.txt_Amount,"value");

        if(name.equalsIgnoreCase(name_ui)){
            //WriteToReport("Name from API call = ["+name+"] | Name from UI = ["+name_ui+"] - Passed");
        }
        else{
            //WriteToReport("Name from API call = ["+name+"] | Name from UI = ["+name_ui+"] - Failed");
        }

        if(source_account_no.equalsIgnoreCase(source_account_no_ui)){
            //WriteToReport("Source Account No. from API call = ["+source_account_no+"] | Source Account No. from UI = ["+source_account_no_ui+"] - Passed");
        }
        else{
            //WriteToReport("Source Account No. from API call = ["+source_account_no+"] | Source Account No. from UI = ["+source_account_no_ui+"] - Failed");
        }

        if(target_account_no.equalsIgnoreCase(target_account_no_ui)){
            //WriteToReport("Target Account No. from API call = ["+target_account_no+"] | Target Account No. from UI = ["+target_account_no_ui+"] - Passed");
        }
        else{
            //WriteToReport("Target Account No. from API call = ["+target_account_no+"] | Target Account No. from UI = ["+target_account_no_ui+"] - Failed");
        }

        if(amount.equalsIgnoreCase(amount_ui)){

            //WriteToReport("Amount from API call = ["+amount+"] | Amount from UI = ["+amount_ui+"] - Passed");
        }
        else{
            //WriteToReport("Amount from API call = ["+amount+"] | Amount from UI = ["+amount_ui+"] - Failed");
        }

        handler.click(EnrichBank_FundTransferDetails.btn_ConfirmTransfer);
    }

    public static String get_transaction_id(ExecutionHandler handler) {
        String transaction_id = handler.getText(EnrichBank_FundTransferDetails.txt_TransactionID);
        return transaction_id;
    }
    public static void download_fund_transfer_details_pdf(ExecutionHandler handler) {
        handler.click(EnrichBank_FundTransferDetails.btn_DownloadReceipt);
    }

    public static void logout(){

    }





}
