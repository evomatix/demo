package com.evomatix.tasker.rpa.scripting.pages.uwebristol;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;

public class UWEBristolApplicantView extends Page {

    public UWEBristolApplicantView(){
        this.setUrl("https://welcome.uwe.ac.uk/urd/sits.urd/run/siw_lgn");
    }

    public static ObjectLocator lnk_AccordionEntry = new ObjectLocator("lnk_AccordionEntry","//div[contains(text(),'#{{title}}#')]", LocatorType.XPATH);


    public static ObjectLocator lnk_AccordionEntryContent = new ObjectLocator("lnk_AccordionEntryContent","//div[contains(text(),'#{{title}}#')]/..//div//tr//td//a[contains(text(),'#{{entry}}#')]", LocatorType.XPATH);



}
