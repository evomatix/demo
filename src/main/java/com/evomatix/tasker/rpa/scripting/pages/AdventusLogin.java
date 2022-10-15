package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class AdventusLogin extends Page {

    public AdventusLogin(){
        this.setUrl("https://app.adventus.io");
    }

    public static ObjectLocator txt_Email = new ObjectLocator("txt_Email","//input[@name='email']", LocatorType.XPATH);

    public static ObjectLocator txt_Password = new ObjectLocator("txt_Password","//input[@name='password']", LocatorType.XPATH);

    public static ObjectLocator btn_Login = new ObjectLocator("btn_Login","//button[contains(text(),'Log in')]", LocatorType.XPATH);


}



