package com.rokejits.android.tool.connection2.handler;

public class SimpleErrorHandler implements ErrorHandler{

  @Override
  public String handlerError(int errorCode, String errorMessage) {	
	return errorMessage;
  }

}
