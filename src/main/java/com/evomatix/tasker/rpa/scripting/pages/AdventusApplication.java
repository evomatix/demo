package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class AdventusApplication extends Page {

    public AdventusApplication(){
        this.setUrl("https://app.adventus.io");
    }

    public static ObjectLocator btn_Edit = new ObjectLocator("btn_Edit","//button[text()='Edit']", LocatorType.XPATH);
    public static ObjectLocator lnk_StudentName = new ObjectLocator("lnk_StudentName","//tr/td/span[contains(text(),'#{{idf_StudentID}}#')]/following::a[contains(text(),'#{{idf_StudentName}}#')]", LocatorType.XPATH);
    public static ObjectLocator txt_Search = new ObjectLocator("txt_Search","//span[contains(text(),'Search')]/following-sibling::input[@type='search']", LocatorType.XPATH);
    public static ObjectLocator lnk_StudentNameByID = new ObjectLocator("lnk_StudentNameByID","//a[@href='/counsellor/student/#{{idf_StudentID}}#/show']", LocatorType.XPATH);
    public static ObjectLocator lnk_Application = new ObjectLocator("lnk_Application","//a/span[text()='Applications']", LocatorType.XPATH);

    
    

}



