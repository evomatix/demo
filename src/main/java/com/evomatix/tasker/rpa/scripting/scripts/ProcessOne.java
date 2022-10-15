package com.evomatix.tasker.rpa.scripting.scripts;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.rpa.scripting.bc.Common;

public class ProcessOne {

	public static void partOne(ExecutionHandler handler) {


		Common.adventus_Login(handler, "admissionstesting01@example.com", "RcY7CilamR8n8S7#wk");
		Common.adventus_SearchStudent(handler, "79456");
		String studentName = Common.adventus_GetStudentName(handler, "79456");
		System.out.println(studentName);
		Common.coventry_Login(handler, "jayatakker@adventus.io", "Adventus@123");
		//Common.coventry_DownloadTheOffer(handler, studentName);
		

	}

}
