package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class AdventusDocuments extends Page {

    public AdventusDocuments(){
        this.setUrl("https://app.adventus.io");
    }

    public static ObjectLocator lnk_Documents = new ObjectLocator("lnk_Documents","//div/h6[contains(text(),'Documents')]", LocatorType.XPATH);
    public static ObjectLocator btn_Send = new ObjectLocator("btn_Send","//button/span[text()='Send']", LocatorType.XPATH);
    public static ObjectLocator dd_AddDocuments = new ObjectLocator("dd_AddDocuments","(//div/h6[contains(text(),'Documents')]/following::button/i[text()='arrow_drop_down'])[2]", LocatorType.XPATH);

    
    

}



