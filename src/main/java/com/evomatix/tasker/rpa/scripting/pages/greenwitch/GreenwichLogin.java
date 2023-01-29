package com.evomatix.tasker.rpa.scripting.pages.greenwitch;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class GreenwichLogin extends Page {

    public GreenwichLogin(){
        this.setUrl("https://greenwichapplications.gre.ac.uk/");
    }


    public static ObjectLocator txt_Username = new ObjectLocator("txt_Username","//input[@id='edit-name']", LocatorType.XPATH);

    public static ObjectLocator txt_Password = new ObjectLocator("txt_Password","//input[@id='edit-pass']", LocatorType.XPATH);

    public static ObjectLocator btn_Login = new ObjectLocator("btn_Login","//input[@id='edit-submit']", LocatorType.XPATH);

    public static ObjectLocator btn_AccountCircle = new ObjectLocator("btn_AccountCircle","//div[@id='topbar_hi_message']", LocatorType.XPATH);

    public static ObjectLocator btn_Logout = new ObjectLocator("btn_Logout","//div/a[contains(text(),'Log Out')]", LocatorType.XPATH);


}
