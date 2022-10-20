package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class AdventusDocuments extends Page {

    public AdventusDocuments(){
        this.setUrl("https://app.adventus.io");
    }

    public static ObjectLocator lnk_Documents = new ObjectLocator("lnk_Documents","(//div[@class='accordion'])[14]/div/h6", LocatorType.XPATH);
    public static ObjectLocator btn_Add = new ObjectLocator("btn_Add","(//button/span[text()='Add'])[8]", LocatorType.XPATH);
    public static ObjectLocator dd_AddDocuments = new ObjectLocator("dd_AddDocuments","(//ul[@class='inline-dropdown__items'])[10]", LocatorType.XPATH);
    public static ObjectLocator btn_Upload = new ObjectLocator("btn_Add","(//button/i[@class='material-icons adv-icon adv-icon--regular adv-icon--spacing-none pointer action-icon' and text()='upload'])[last()]", LocatorType.XPATH);

    
}



