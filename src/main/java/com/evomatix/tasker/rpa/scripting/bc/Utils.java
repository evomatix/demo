package com.evomatix.tasker.rpa.scripting.bc;

import com.evomatix.tasker.framework.engine.ExecutionHandler;

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
                handler.switchWindow(currentWindow);
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

}
