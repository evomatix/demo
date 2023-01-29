package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.framework.exceptions.ExecutionInterruptedException;

import java.io.File;
import java.util.Map;

public class Utils {

    public static String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    public static void switchBackToBaseWindow(ExecutionHandler handler,String currentWindow){
        if(!currentWindow.equals("")){
            try{
                handler.switchToWindowByTitle(currentWindow);
               // handler.closeAllOtherTabs();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void cleanupFile(ExecutionHandler handler,String file){
        if(file!=null){
            try{
                handler.fileManager.deleteFile(file);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static boolean isEligible(ExecutionHandler handler,Map<String, Object> row){

        //fields
        String uniName = String.valueOf(row.get("Insto Name"));
        String uniConfig =  handler.getConfiguration("UNIVERSITY_NAME");
        Object checkDate = row.get("Checked Date")==null? "": String.valueOf(row.get("Checked Date")).trim();


        //condition
        //check the [uni name] is equals to the [uni filter] configuration and the check date is empty
        return (uniName.equals(uniConfig) && checkDate.equals(""));



    }



    public static String addPrefixToFileName(ExecutionHandler handler, String filePath, String prefix) {

        File file = new File(filePath);
        File updatedFile = new File(file.getParentFile().getAbsolutePath()+File.separator+prefix+"_"+file.getName());
        boolean success = file.renameTo(updatedFile);
        if (!success) {
            throw new ExecutionInterruptedException("FAILED to rename downloaded pdf "+file.getName(),"Failed - Unable to Rename PDF File");
        } else {
            System.out.println(updatedFile.getAbsolutePath());
            return updatedFile.getAbsolutePath();
        }
    }





}
