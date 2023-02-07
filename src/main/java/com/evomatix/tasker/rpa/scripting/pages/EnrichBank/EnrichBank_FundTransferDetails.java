package com.evomatix.tasker.rpa.scripting.pages.EnrichBank;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class EnrichBank_FundTransferDetails extends Page {

    public EnrichBank_FundTransferDetails(){
        this.setUrl("http://localhost:8080/home.html?uname=admin&psw=admin&remember=on");
    }

    public static ObjectLocator lbl_FundTransferDetails = new ObjectLocator("lbl_FundTransferDetails","//h2[text()='Fund Transfer - Details']", LocatorType.XPATH);
    public static ObjectLocator lbl_FundTransferConfirmation = new ObjectLocator("lbl_FundTransferConfirmation","//h2[text()='Fund Transfer - Confirmation']", LocatorType.XPATH);
    public static ObjectLocator txt_Name = new ObjectLocator("txt_Name","//label/b[text()='Name']/following::input[1]", LocatorType.XPATH);
    public static ObjectLocator txt_SourceAccountNo = new ObjectLocator("txt_SourceAccountNo","//label/b[text()='Source Account No']/following::input[1]", LocatorType.XPATH);
    public static ObjectLocator txt_TargetAccountNo = new ObjectLocator("txt_TargetAccountNo","//label/b[text()='Target Account No']/following::input[1]", LocatorType.XPATH);
    public static ObjectLocator txt_Amount = new ObjectLocator("txt_Amount","//label/b[text()='Amount(USD)']/following::input[1]", LocatorType.XPATH);
    public static ObjectLocator txt_TransactionID = new ObjectLocator("txt_TransactionID","//label/b[text()='Transaction ID']/following::input[1]", LocatorType.XPATH);
    public static ObjectLocator txt_DateTime = new ObjectLocator("txt_TransactionID","//label/b[text()='Date & Time']/following::input[1]", LocatorType.XPATH);
    public static ObjectLocator btn_Transfer = new ObjectLocator("btn_Transfer","//button[text()='Transfer']", LocatorType.XPATH);
    public static ObjectLocator btn_ConfirmTransfer = new ObjectLocator("btn_ConfirmTransfer","//button[text()='Confirm Transfer']", LocatorType.XPATH);
    public static ObjectLocator btn_DownloadReceipt = new ObjectLocator("btn_ConfirmTransfer","//button[text()='Download Receipt']", LocatorType.XPATH);

}



