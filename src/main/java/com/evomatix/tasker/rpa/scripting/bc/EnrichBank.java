package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.fileops.SimplePDFReader;
import com.evomatix.tasker.framework.mq.RabbitMQManager;
import com.evomatix.tasker.rpa.scripting.pages.EnrichBank.EnrichBank_FundTransferDetails;
import com.evomatix.tasker.rpa.scripting.pages.EnrichBank.EnrichBank_Login;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    }

    public static void confirm_fund_transfer_details(ExecutionHandler handler) {
        handler.checkElementPresent(EnrichBank_FundTransferDetails.lbl_FundTransferConfirmation);
        handler.click(EnrichBank_FundTransferDetails.btn_ConfirmTransfer);
    }

    public static String get_transaction_id(ExecutionHandler handler) {
        String transaction_id = handler.getText(EnrichBank_FundTransferDetails.txt_TransactionID);
        return transaction_id;
    }

    public static void validate_API(ExecutionHandler handler, String transactionId, HashMap<String,String> source ){
        String url = "http://localhost:8080/funds/transfer/" + transactionId;
        HttpResponse<String> response = handler.callWebRequest(url, "get", new ArrayList<>());
        Map<String, Object> result;

        try {
            result = new ObjectMapper().readValue(response.body(), HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while reading http response", e);
        }

        HashMap<String,String> apiData = new HashMap<>();

        apiData.put("name",(String) result.get("name"));
        apiData.put("sourceAccount",(String) result.get("sourceAccount"));
        apiData.put("targetAccount",(String) result.get("targetAccount"));
        apiData.put("amount",(String) result.get("amount"));
        EnrichBank.validateTransactionAttributes(handler, "web api call",source,apiData);
    }


    public static void validate_MessageQueue(ExecutionHandler handler, String transactionId, HashMap<String,String> source ){
        RabbitMQManager manager = handler.messageQueueManager.getRabbitMQManager();
        manager.createConnection("localhost", "enrichbank");
        String msg= manager.fetchMessages();
        Map<String, Object> result;

        try {
            result = new ObjectMapper().readValue(msg, HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while reading http response", e);
        }

        HashMap<String,String> apiData = new HashMap<>();

        apiData.put("name",(String) result.get("name"));
        apiData.put("sourceAccount",(String) result.get("sourceAccount"));
        apiData.put("targetAccount",(String) result.get("targetAccount"));
        apiData.put("amount",(String) result.get("amount"));
        EnrichBank.validateTransactionAttributes(handler, "message queue",source,apiData);
    }


    public static void validate_PDF(ExecutionHandler handler,HashMap<String,String> source){

        String pdfFile = EnrichBank.download_fund_transfer_details_pdf(handler);
        SimplePDFReader reader = handler.fileManager.getPDFManager().getSimplePDFReader();
        String pdfData =reader.readPDF(pdfFile);

        for (String attribute:source.keySet()) {
            if(pdfData.equalsIgnoreCase(source.get(attribute)))
                handler.writeToReport(attribute+" from UI = ["+source.get(attribute)+"] - is found in PDF - Passed");
            else
                handler.fail(attribute+" from UI = ["+source.get(attribute)+"] is found not in PDF - Failed");
        }

    }


    public static String download_fund_transfer_details_pdf(ExecutionHandler handler) {
        handler.click(EnrichBank_FundTransferDetails.btn_DownloadReceipt);
        return handler.waitUntilDonwloadCompleted();
    }


    public static void validateTransactionAttributes(ExecutionHandler handler, String channel, HashMap<String,String> source,HashMap<String,String> target){

        for (String attribute:source.keySet()) {
            if(source.get(attribute).equalsIgnoreCase(target.get(attribute)))
                handler.writeToReport(attribute+" from "+channel+" = ["+target.get(attribute)+"] | "+attribute+" from UI = ["+source.get(attribute)+"] - Passed");
            else
                handler.fail(attribute+" from "+channel+" = ["+target.get(attribute)+"] | "+attribute+" from UI = ["+source.get(attribute)+"] - Failed");
        }
    }



    public static void logout(){

    }



}
