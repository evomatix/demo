package com.evomatix.tasker.framework.engine;

import com.evomatix.tasker.framework.fileops.FileManager;
import com.evomatix.tasker.framework.reporting.LogType;
import com.evomatix.tasker.framework.locator.ObjectLocator;
import com.evomatix.tasker.framework.reporting.ReportHandler;
import com.evomatix.tasker.framework.utils.PropertiesLoader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;


public class ExecutionHandler implements AutoCloseable {

    private Properties configs;

    private Properties settings;

    public FileManager fileManager;

    public ExecutionHandler(){
        fileManager = new FileManager();
        this.setup();
        reporter.initReporting();
    }

    private ReportHandler reporter = new ReportHandler();


    private WebDriver driver;


    public void setup(){

        this.loadProps();
        this.driver = WebDriverManager.chromedriver().create();
    }

    private void loadProps(){

        String resourcePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String configPath = resourcePath+"config.properties";
        configPath = configPath.substring(1,configPath.length());
        configs = PropertiesLoader.loadProperties(configPath);
        String settingsPath = resourcePath+"settings.properties";
        settingsPath = settingsPath.substring(1,settingsPath.length());
        settings = PropertiesLoader.loadProperties(settingsPath);
    }

    public String getConfiguration(String config){

        if(configs.containsKey(config)){
            return (String) configs.get(config);
        }else{
            throw  new RuntimeException("Config ["+config+"] is not found");
        }
    }


  public void open(String url, long timeout){
        this.driver.navigate().to(url);
        this.log(LogType.PASS,"Open",url);
        this.pause(timeout);

  }

    public void click(ObjectLocator locator){
        WebElement element= this.findElement(locator);
           try{
               element.click();
           }catch (JavascriptException e){
               JavascriptExecutor executor = (JavascriptExecutor)driver;
               executor.executeScript("arguments[0].click();", element);
           }
            this.log(LogType.PASS,"Click","Clicked on Element ["+locator.name+"]");
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
        this.log(LogType.PASS,"Type","Typed on Element ["+locator.name+"]");
    }

    public void type(ObjectLocator locator, Map<String,String> locatorParams,String text){
        locator.setParameters(locatorParams);
        this.type(locator,text);
    }

    public void select(ObjectLocator locator,String value){
         Select select=  new Select(this.findElement(locator));
         select.selectByValue(value);
         this.log(LogType.PASS,"Select","Selected Value ["+value+"] on Element ["+locator.name+"]");
    }

    public void select(ObjectLocator locator,Map<String,String> locatorParams,String value){
        locator.setParameters(locatorParams);
        this.select(locator,value);
    }

    public String getText(ObjectLocator locator){

        String txt = this.findElement(locator).getText();
      this.log(LogType.PASS,"GetText","Read value ["+txt+"] from Element ["+locator.name+"]");

        return txt;
    }
    
    public String getText(ObjectLocator locator,Map<String,String> locatorParams){
        locator.setParameters(locatorParams);
        return this.findElement(locator).getText();
}


    public boolean checkElementPresent(ObjectLocator locator){
        try{
          this.findElement(locator);
          this.log(LogType.PASS,"checkElementPresent","Element ["+locator.name+"] is present");
          return  true;


        }catch (Exception e){
            this.log(LogType.PASS,"checkElementPresent","Element ["+locator.name+"] is not present");
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

        int retry = settings.contains("find.element.retry") ? Integer.parseInt((String) settings.get("find.element.retry") ):10;
        int counter = retry;
        long retryInterval = settings.contains("find.element.retry.interval") ? Integer.parseInt((String) settings.get("find.element.retry.interval") ):1000;;
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
                    driver.wait(retryInterval);
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


    public void log(LogType logType,String message,String details){
        this.log(logType,message,details,false);

    }

    public void log(LogType logType,String message,String details ,boolean captureScreenshot){
        System.out.println(logType.toString()+" - "+message+" - "+details);
        reporter.log(logType,message,details);

    }


    @Override
    public void close() throws Exception {


        reporter.endReporting();
        if(this.driver!=null){
            this.driver.close();
            this.driver.quit();
        }

       // System.exit(0);

    }
}
