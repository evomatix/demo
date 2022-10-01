package com.evomatix.tasker.framework.locator;

import java.util.HashMap;
import java.util.Map;

public class LocatorParam {

    private  HashMap<String,String> map = new HashMap<>();

    public LocatorParam add(String param,String value){
       this.map.put(param,value);
        return this;
    }

    public Map build(){
       Map<String,String> temp = new HashMap(map);
       map.clear();
       return temp;
    }
}
