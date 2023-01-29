package com.evomatix.tasker.rpa.scripting.pages.uwebristol;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;

public class UWEBristolHome extends Page {

    public UWEBristolHome(){
        this.setUrl("https://welcome.uwe.ac.uk/urd/sits.urd/run/siw_lgn");
    }

    public static ObjectLocator lnk_ReviewApplication = new ObjectLocator("lnk_ReviewApplication","//div/a[contains(text(),'Review Applicants')]", LocatorType.XPATH);



}
