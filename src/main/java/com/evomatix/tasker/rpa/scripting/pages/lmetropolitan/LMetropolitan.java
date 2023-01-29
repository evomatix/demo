package com.evomatix.tasker.rpa.scripting.pages.lmetropolitan;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;

public class LMetropolitan extends Page {

    public LMetropolitan(){
        this.setUrl("https://evision.londonmet.ac.uk/urd/sits.urd/run/siw_lgn");
    }

    public static ObjectLocator txt_Username = new ObjectLocator("txt_Username","//input[@id='MUA_CODE.DUMMY.MENSYS']']", LocatorType.XPATH);

    public static ObjectLocator txt_Password = new ObjectLocator("txt_Password","//input[@id='PASSWORD.DUMMY.MENSYS']", LocatorType.XPATH);

    public static ObjectLocator btn_Login = new ObjectLocator("btn_Login","//input[@type='submit']", LocatorType.XPATH);

    public static ObjectLocator btn_AccountCircle = new ObjectLocator("btn_AccountCircle","//div[@class='logged-in-msg']//a[contains(text(),'Logout')]", LocatorType.XPATH);

    public static ObjectLocator btn_Logout = new ObjectLocator("btn_Logout","//div[@class='logged-in-msg']//a[contains(text(),'Logout')]", LocatorType.XPATH);


}
