package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class AdventusApplication extends Page {

    public AdventusApplication(){
        this.setUrl("https://app.adventus.io");
    }

    public static ObjectLocator btn_Edit = new ObjectLocator("btn_Edit","//button[text()='Edit']", LocatorType.XPATH);
    public static ObjectLocator btn_EditWithCourse = new ObjectLocator("btn_EditWithCourse","//div[@class='row']//div[contains(text(),'#{{title}}#')]//..//..//..//..//p[contains(text(),'Coventry University')]//..//..//button", LocatorType.XPATH);
    public static ObjectLocator btn_EditInstitutionStudentId = new ObjectLocator("btn_EditInstitutionStudentId","//div[contains(text(),'Institution Student Id')]/following::i[1]", LocatorType.XPATH);
    public static ObjectLocator txt_InstitutionStudentId = new ObjectLocator("txt_InstitutionStudentId","//div[contains(text(),'Institution Student Id')]/following::input[1]", LocatorType.XPATH);
    public static ObjectLocator select_OfferType = new ObjectLocator("select_OfferType","//label[text()='Offer']/..//select", LocatorType.XPATH);


}



