package com.evomatix.tasker.framework.locator;

import org.openqa.selenium.By;

import java.util.Map;

import static java.util.Objects.nonNull;

public class ObjectLocator {

    public String name;

    public String locator;

    private String resolvedLocator;

    private Map<String,String> parameters;

    public LocatorType locatorType;

    public ObjectLocator(String name, String locator, LocatorType locatorType){

        this.name=name;
        this.locator=locator;
        this.locatorType=locatorType;

    }

    /**
     *
     * @return
     */
    public By getResolvedLocator(){

        if(nonNull(parameters)){
            resolvedLocator = new String(this.locator);

            parameters.forEach((key,value)->{

                String param = "#{{"+key+"}}#";

                if(resolvedLocator.contains(param)){
                    resolvedLocator= resolvedLocator.replace(param,value);
                }else{
                    throw new RuntimeException("Parameter ["+key+"] is not found in the locator ["+locator+"] of element ["+name+"]");
                }

            });

            return this.getBy(resolvedLocator);
        }else{
            return this.getBy(locator);
        }
    }


    private By getBy(String resolvedLocator){
        switch (this.locatorType){

            case XPATH:
                return By.xpath(resolvedLocator);
            case  CSS:
                return By.cssSelector(resolvedLocator);
            default:
                throw new RuntimeException("Unsupported Locator Type");
        }
    }


    public void clearParameters() {
         this.parameters=null;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

}
