package com.evomatix.tasker.framework.fileops;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelManager {



    private Workbook workbook;

    boolean open;

    public ExcelManager(){
        this.open=false;
    }


    public void openWorkBook(String filePath){
       try{
           FileInputStream file = new FileInputStream(new File(filePath));
           workbook = new XSSFWorkbook(file);
           open=true;

       }catch (Exception e){
           throw new RuntimeException("Error Occurred while reading Excel Workbook", e);
       }
    }


    public List<Map<String,Object>> readExcel(String sheetName) throws IOException {

        if(!open){
            throw new RuntimeException("No opened workbooks, Please open before reading");
        }

        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        Sheet sheet = workbook.getSheet(sheetName);

        boolean processHeaders = true;

        Map<Integer,String> headers = new HashMap<>();


        for (Row row : sheet) {

            if(processHeaders){

                int index = 0;
                for (Cell cell : row) {
                    headers.put(index,(String) processCell(cell));
                    index++;
                }
                processHeaders=false;

            }else{

                Map<String,Object>  rawData = new HashMap<>();
                int index = 0;
                for (Cell cell : row) {
                    if(headers.containsKey(index)){
                        rawData.put(headers.get(index),processCell(cell));
                    }
                    index++;
                }

                data.add(rawData);

            }

        }
        return data;
    }


    private Object processCell(Cell cell){
            switch (cell.getCellType()) {
                case STRING:
                return cell.getRichStringCellValue().getString();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue() ;
                    } else {
                        return cell.getNumericCellValue() ;
                    }

                case BOOLEAN:
                    return cell.getBooleanCellValue() ;

                case FORMULA:
                    return cell.getCellFormula() ;

                default:
                    return " ";

        }

    }




    public void closeWorkBook(){
      try{
          if (workbook != null){
              workbook.close();
          }
      }catch (Exception e){
         e.printStackTrace();
      }

    }
}
