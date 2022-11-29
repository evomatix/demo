package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class CoventryApplication extends Page {

    public CoventryApplication(){
        this.setUrl("");
    }

    public static ObjectLocator lnk_Application = new ObjectLocator("lnk_Application","//a/span[text()='Application']", LocatorType.XPATH);
  //  public static ObjectLocator lnk_StudentName = new ObjectLocator("lnk_Studentname","//button[starts-with(text(),'#{{idf_StudentName}}#')]", LocatorType.XPATH);
    public static ObjectLocator lnk_StudentName = new ObjectLocator("lnk_StudentName","(//button[starts-with(text(),'#{{idf_StudentName_Upper}}#') or starts-with(text(),'#{{idf_StudentName_Camel}}#') or  starts-with(text(),'#{{idf_StudentName_Lower}}#')])[1]", LocatorType.XPATH);
    public static ObjectLocator lnk_StudentNames = new ObjectLocator("lnk_StudentNames","(//button[starts-with(text(),'#{{idf_StudentName_Upper}}#') or starts-with(text(),'#{{idf_StudentName_Camel}}#') or  starts-with(text(),'#{{idf_StudentName_Lower}}#')])", LocatorType.XPATH);
    public static ObjectLocator btn_DownloadTheOffer = new ObjectLocator("btn_DownloadTheOffer","//button[text()='Download the offer']", LocatorType.XPATH);
    public static ObjectLocator lnk_UserName = new ObjectLocator("lnk_UserName","//a/span[@class='triggerDownArrow down-arrow']", LocatorType.XPATH);
    public static ObjectLocator lnk_Logout = new ObjectLocator("lnk_Logout","//a[text()='Logout']", LocatorType.XPATH);

    

}



