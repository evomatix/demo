package com.evomatix.tasker.rpa.scripting.pages.greenwitch;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;

public class GreenwichApplicantSearch extends Page {

    public GreenwichApplicantSearch(){
        this.setUrl("https://greenwichapplications.gre.ac.uk/search/record");
    }


    public static ObjectLocator lnk_Applications = new ObjectLocator("lnk_Applications","//span[contains(text(),'Applications')]", LocatorType.XPATH);

    public static ObjectLocator lnk_ViewApplications = new ObjectLocator("lnk_ViewApplications","//li/a[contains(text(),'View Applications')]", LocatorType.XPATH);

    public static ObjectLocator txt_Search = new ObjectLocator("txt_Search","//input[@id='edit-name']", LocatorType.XPATH);

    public static ObjectLocator btn_Search = new ObjectLocator("btn_Search","//input[@id='student_search_records_submit_id']", LocatorType.XPATH);

    public static ObjectLocator txt_Status = new ObjectLocator("txt_Status","//th/a[contains(text(),'Status')]/../../../../tbody/tr/td/a", LocatorType.XPATH);


    //th[contains(text(),'Comment')]/../../../tbody/tr[1]/td[1]

}
