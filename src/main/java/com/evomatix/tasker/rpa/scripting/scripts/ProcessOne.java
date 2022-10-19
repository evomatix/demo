package com.evomatix.tasker.rpa.scripting.scripts;

import com.evomatix.tasker.framework.engine.ExecutionHandler;
import com.evomatix.tasker.rpa.scripting.bc.Common;

public class ProcessOne {

	public static void partOne(ExecutionHandler handler) {


		Common.adventus_Login(handler, handler.getConfiguration("ADVENTUS_USERNAME"), handler.getConfiguration("ADVENTUS_PASSWORD"));
		Common.adventus_SearchStudent(handler, "79456");
		String studentName = Common.adventus_GetStudentName(handler, "79456");
		System.out.println(studentName);
		Common.coventry_Login(handler, handler.getConfiguration("COVENTRY_USERNAME"), handler.getConfiguration("COVENTRY_PASSWORD"));
		//Common.coventry_DownloadTheOffer(handler, studentName);
		

	}

}
