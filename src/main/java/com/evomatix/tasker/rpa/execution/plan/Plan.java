package com.evomatix.tasker.rpa.execution.plan;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.rpa.scripting.scripts.processes.Processes;

public class Plan {


   public static void execute() throws Exception {

       try(ExecutionHandler handler = new ExecutionHandler()){
           //Call your scripts here
           Processes.tc_enrich_bank_transaction_validation(handler);


       }catch (Exception e){
           e.printStackTrace();
       }


   }


}
