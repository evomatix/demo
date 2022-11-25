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
    public static ObjectLocator lnk_StudentNameByID = new ObjectLocator("lnk_StudentNameByID","(//a[contains(@href,'/student/#{{idf_StudentID}}#/show')])[1]", LocatorType.XPATH);
    public static ObjectLocator lbl_StudentName = new ObjectLocator("lbl_StudentName","//a[@class='status dot']/..", LocatorType.XPATH);
    public static ObjectLocator lnk_Application = new ObjectLocator("lnk_Application","//a/span[text()='Applications']", LocatorType.XPATH);
    public static ObjectLocator lnk_SearchApply = new ObjectLocator("lnk_SearchApply","//div[text()='Search & Apply']", LocatorType.XPATH);
    public static ObjectLocator lnk_Documents= new ObjectLocator("lnk_Documents","//a/span[text()='Documents']", LocatorType.XPATH);
    public static ObjectLocator lnk_StudentInformation = new ObjectLocator("lnk_StudentInformation","//div[text()='Student Information']", LocatorType.XPATH);
    public static ObjectLocator lnk_ActivityOverview = new ObjectLocator("lnk_ActivityOverview","//a/span[text()='Activity & Overview']", LocatorType.XPATH);

  
  
  
    

}



