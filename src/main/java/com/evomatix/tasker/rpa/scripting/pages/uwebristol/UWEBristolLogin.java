package com.evomatix.tasker.rpa.scripting.pages.uwebristol;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;

public class UWEBristolLogin extends Page {

    public UWEBristolLogin(){
        this.setUrl("https://welcome.uwe.ac.uk/urd/sits.urd/run/siw_lgn");
    }

    public static ObjectLocator txt_UserID = new ObjectLocator("txt_UserID","//input[@id='MUA_CODE.DUMMY.MENSYS']", LocatorType.XPATH);

    public static ObjectLocator txt_Password = new ObjectLocator("txt_Password","//input[@id='PASSWORD.DUMMY.MENSYS']", LocatorType.XPATH);

    public static ObjectLocator btn_Login = new ObjectLocator("btn_Login","//input[@type='submit']", LocatorType.XPATH);

    public static ObjectLocator btn_AccountCircle = new ObjectLocator("btn_AccountCircle","//span[@class='glyphicon glyphicon-user']", LocatorType.XPATH);

    public static ObjectLocator btn_Logout = new ObjectLocator("btn_Logout","//span/a[contains(text(),'Logout')]", LocatorType.XPATH);


}
