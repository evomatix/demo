package com.evomatix.tasker.rpa.scripting.pages.greenwitch;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;

public class GreenwichApplicationView  extends Page {

    public GreenwichApplicationView(){
        this.setUrl("https://greenwichapplications.gre.ac.uk/search/record");
    }

    public static ObjectLocator btn_DownloadOffer = new ObjectLocator("btn_DownloadOffer","//input[@id='edit-print-offet-letter-link']", LocatorType.XPATH);

    public static ObjectLocator txt_Comment = new ObjectLocator("txt_Comment","//th[contains(text(),'Comment')]/../../../tbody/tr[1]/td[1]", LocatorType.XPATH);

    public static ObjectLocator txt_WarningMessage = new ObjectLocator("txt_WarningMessage","//div[@id='squeeze']//div[@class='messages warning']", LocatorType.XPATH);




}
