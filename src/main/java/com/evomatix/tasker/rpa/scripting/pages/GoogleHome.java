package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class GoogleHome extends Page {

    public GoogleHome(){
        this.setUrl("https://google.com");
    }

    public static ObjectLocator txt_IFL = new ObjectLocator("txt_Login","html/body/div[1]/div[3]/form/div[1]/div[1]/div[3]/center/input[2]", LocatorType.XPATH);

    public static ObjectLocator btn_Search = new ObjectLocator("btn_IFL","//input[@name='#{{name}}#']", LocatorType.XPATH);



}



