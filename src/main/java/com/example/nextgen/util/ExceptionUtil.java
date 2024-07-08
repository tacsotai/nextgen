package com.example.nextgen.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ExceptionUtil {

	public List<String> toErrMsgList(Exception e) {
		var errMsgList = new ArrayList<String>();
		errMsgList.add(e.getLocalizedMessage());
		for (StackTraceElement element : e.getStackTrace()) {
			errMsgList.add(element.toString());
		}
		return errMsgList;
	}
}
