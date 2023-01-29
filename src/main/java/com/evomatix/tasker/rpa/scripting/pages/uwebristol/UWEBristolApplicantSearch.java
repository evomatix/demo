package com.evomatix.tasker.rpa.scripting.pages.uwebristol;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;

public class UWEBristolApplicantSearch extends Page {

    public UWEBristolApplicantSearch(){
        this.setUrl("https://welcome.uwe.ac.uk/urd/sits.urd/run/siw_lgn");
    }

    public static ObjectLocator lnk_ReviewApplication = new ObjectLocator("lnk_ReviewApplication","//div/a[contains(text(),'Review Applicants')]", LocatorType.XPATH);

    public static ObjectLocator txt_Applicant_Number = new ObjectLocator("txt_Applicant_Number","//input[@id='ANSWER.TTQ.MENSYS.1.']", LocatorType.XPATH);

    public static ObjectLocator btn_Search = new ObjectLocator("btn_Search","//input[@title='Next']", LocatorType.XPATH);

    public static ObjectLocator tr_SearchResultViewEdit = new ObjectLocator("tr_SearchResultViewEdit","//td[text()='#{{idf_applicationID}}#']//..//td//a[contains(text(),'View')]", LocatorType.XPATH);

    public static ObjectLocator txt_Decision = new ObjectLocator("txt_Decision","//th[contains(text(),'Decision')]//..//..//td[5]", LocatorType.XPATH);




}
