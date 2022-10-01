package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.rpa.scripting.pages.GoogleHome;

public class Common {

    public static void clickIamFeelingLucky(ExecutionHandler handler){
        handler.open(new GoogleHome().getUrl(),3000);
        handler.click(GoogleHome.txt_IFL);
      //Parameterized  UI Object Sample
      //handler.click(GoogleHome.btn_Search,new LocatorParam().add("name","btnI").build());
        handler.pause(5000);


    }
}
