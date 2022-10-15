package com.evomatix.tasker.framework.engine;

import com.evomatix.tasker.framework.model.logger.LogType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;


public class ExecutionHandler implements AutoCloseable {

    public ExecutionHandler(){
        this.setup();
    }


    private WebDriver driver;


    public void setup(){

        this.driver = WebDriverManager.chromedriver().create();
    }


  public void open(String url, long timeout){
        this.driver.navigate().to(url);
        this.pause(timeout);

  }

    public void click(ObjectLocator locator){
            this.findElement(locator).click();
    }

    public void click(ObjectLocator locator, Map<String,String> locatorParams){
        locator.setParameters(locatorParams);
        this.click(locator);
    }

    public void type(ObjectLocator locator,String text){
        WebElement webElement = this.findElement(locator);
        try{
            webElement.clear();
        }catch (Exception e){
            e.printStackTrace();
        }
        webElement.sendKeys(text);
    }

    public void type(ObjectLocator locator, Map<String,String> locatorParams,String text){
        locator.setParameters(locatorParams);
        this.type(locator,text);
    }

    public void select(ObjectLocator locator,String value){
         Select select=  new Select(this.findElement(locator));
         select.selectByValue(value);
    }

    public void select(ObjectLocator locator,Map<String,String> locatorParams,String value){
        locator.setParameters(locatorParams);
        this.select(locator,value);
    }

    public String getText(ObjectLocator locator){
            return this.findElement(locator).getText();
    }
    
    public String getText(ObjectLocator locator,Map<String,String> locatorParams){
        locator.setParameters(locatorParams);
        return this.findElement(locator).getText();
}


    public boolean checkElementPresent(ObjectLocator locator){
        try{
          this.findElement(locator);
          return  true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean checkElementPresent(ObjectLocator locator,Map<String,String> locatorParams){
       locator.setParameters(locatorParams);
       return this.checkElementPresent(locator);
    }


    public void pause(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private WebElement findElement(ObjectLocator element){

        int retry = 10;
        int counter = retry;
        long timeout = 1000;
        boolean elementNotPresent = true;

        do{

            WebElement webElement = driver.findElement(element.getResolvedLocator());


            if(webElement!=null){
                //scroll the element to view
                try{
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
                    //this make thing stable -> meantime slow
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                }

                return  webElement;
            }else{
                try {
                    driver.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(counter>0){
                counter--;
            }else{
                elementNotPresent=false;
            }

        }while (elementNotPresent);

        throw new RuntimeException("Element is not Found");



    }


    public void log(String message, LogType logType){
        ExecutionHandler.log(message,logType,false);
    }

    public static void log(String message, LogType logType,boolean captureScreenshot){
        System.out.println(message);

    }


    @Override
    public void close() throws Exception {



        if(this.driver!=null){
            this.driver.close();
            this.driver.quit();
        }

       // System.exit(0);

    }
}
