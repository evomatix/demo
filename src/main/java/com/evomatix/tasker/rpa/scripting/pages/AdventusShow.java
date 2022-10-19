package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class AdventusShow extends Page {

    public AdventusShow(){
        this.setUrl("https://app.adventus.io");
    }

    public static ObjectLocator txt_MessageBox = new ObjectLocator("txt_MessageBox","//textarea[@id='message-box']", LocatorType.XPATH);
    public static ObjectLocator btn_Send = new ObjectLocator("btn_Send","//button/span[text()='Send']", LocatorType.XPATH);

    
    

}



