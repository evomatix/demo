package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class AdventusStudentStatus extends Page {

    public AdventusStudentStatus(){
        this.setUrl("https://app.adventus.io");
    }

    public static ObjectLocator lnk_AppStages = new ObjectLocator("lnk_AppStages","//a[text()='#{{idf_AppStage}}#']", LocatorType.XPATH);
    public static ObjectLocator lnk_StudentName = new ObjectLocator("lnk_StudentName","//tr/td/span[contains(text(),'#{{idf_StudentID}}#')]/following::a[contains(text(),'#{{idf_StudentName}}#')]", LocatorType.XPATH);
    public static ObjectLocator txt_Search = new ObjectLocator("txt_Search","//span[contains(text(),'Search')]/following-sibling::input[@type='search']", LocatorType.XPATH);
    public static ObjectLocator lnk_StudentNameByID = new ObjectLocator("lnk_StudentNameByID","//a[@href='/counsellor/student/#{{idf_StudentID}}#/show']", LocatorType.XPATH);


}



