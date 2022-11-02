package com.evomatix.tasker.rpa.scripting.pages;

import com.evomatix.tasker.framework.locator.LocatorType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.model.Page;


public class AdventusDocuments extends Page {

    public AdventusDocuments(){
        this.setUrl("https://app.adventus.io");
    }

    public static ObjectLocator lnk_Documents = new ObjectLocator("lnk_Documents","(//h6[@class='accordion__title accordion__title_full_width'])[last()]", LocatorType.XPATH);
    public static ObjectLocator btn_Add = new ObjectLocator("btn_Add","(//button/span[text()='Add'])[8]", LocatorType.XPATH);
    public static ObjectLocator dd_AddDocuments = new ObjectLocator("dd_AddDocuments","(//h6[@class='accordion__title accordion__title_full_width'])[last()]/following::span[text()='Add another document']", LocatorType.XPATH);
    public static ObjectLocator btn_Upload = new ObjectLocator("btn_Upload","(//button/i[@class='material-icons adv-icon adv-icon--regular adv-icon--spacing-none pointer action-icon' and text()='upload'])[last()]", LocatorType.XPATH);
    public static ObjectLocator dd_AddDocumentsValue = new ObjectLocator("dd_AddDocumentsValue","(//li[text()='#{{idf_Value}}#'])[last()]", LocatorType.XPATH);
    public static ObjectLocator dd_AddAdditionalDocumentsLink = new ObjectLocator("dd_AddAdditionalDocumentsLink","//div[@class='student-document-list__row']/following::p[text()='#{{idf_Value}}#'][last()]//following::i[text()='more_vert'][1]", LocatorType.XPATH);
    public static ObjectLocator dd_AddAdditionalDocuments = new ObjectLocator("dd_AddAdditionalDocuments","//div[@class='student-document-list__row']/following::p[text()='#{{idf_Value}}#'][last()]//following::i[text()='more_vert'][1]/following::li[text()='Upload additional files'][1]", LocatorType.XPATH);

}



