package com.evomatix.tasker.rpa.scripting.pages.EnrichBank;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class EnrichBank_Login extends Page {

    public EnrichBank_Login(){
        this.setUrl("http://localhost:8080/index.html");
    }

    public static ObjectLocator txt_UserName = new ObjectLocator("txt_UserName","//input[@name='uname']", LocatorType.XPATH);

    public static ObjectLocator txt_Password = new ObjectLocator("txt_Password","//input[@name='psw']", LocatorType.XPATH);

    public static ObjectLocator btn_Login = new ObjectLocator("btn_Login","//button[text()='Login']", LocatorType.XPATH);

    public static ObjectLocator btn_Cancel = new ObjectLocator("btn_Cancel","//button[text()='Cancel']", LocatorType.XPATH);

    public static ObjectLocator chk_RememberMe = new ObjectLocator("chk_RememberMe","//label/input[@name='remember']", LocatorType.XPATH);
  

}



