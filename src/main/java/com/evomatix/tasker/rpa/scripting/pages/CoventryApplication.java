package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class CoventryApplication extends Page {

    public CoventryApplication(){
        this.setUrl("");
    }

    public static ObjectLocator lnk_Application = new ObjectLocator("lnk_Application","//a/span[text()='Application']", LocatorType.XPATH);
    public static ObjectLocator lnk_StudentName = new ObjectLocator("lnk_Studentname","//button[contains(text(),'#{{idf_StudentID}}#')]", LocatorType.XPATH);
    public static ObjectLocator btn_DownloadTheOffer = new ObjectLocator("btn_DownloadTheOffer","//button[text()='Download the offer']", LocatorType.XPATH);


}



