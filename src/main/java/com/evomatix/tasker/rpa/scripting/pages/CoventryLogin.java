package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class CoventryLogin extends Page {

    public CoventryLogin(){
        this.setUrl("https://partnerportal.coventry.ac.uk/s/");
    }

    public static ObjectLocator txt_UserName = new ObjectLocator("txt_UserName","//input[@name='username']", LocatorType.XPATH);

    public static ObjectLocator txt_Password = new ObjectLocator("txt_Password","//input[@name='password']", LocatorType.XPATH);

    public static ObjectLocator btn_Login = new ObjectLocator("btn_Login","//button/span[contains(text(),'Log in')]", LocatorType.XPATH);

}



