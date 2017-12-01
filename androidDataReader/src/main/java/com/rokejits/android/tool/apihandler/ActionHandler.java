package com.rokejits.android.tool.apihandler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import android.content.Context;

import com.rokejits.android.tool.connection2.handler.ErrorHandler;
import com.rokejits.android.tool.data.DataReader;
import com.rokejits.android.tool.data.DataReaderListener;
import com.rokejits.android.tool.data.DataReaderOption;
import com.rokejits.android.tool.utils.HttpUtils;



public class ActionHandler {

  /*public static final String ACTION_SUCCESS = "success";
  public static final String ACTION_ERROR   = "error";*/
  
  private String[] successCode = new String[]{"200"};	
	
  /*private static final String AES_KEY = "F29326D840384A3E";
  private static final String AES_IV = "9876543210fedcba";*/
  private String responseCodeTag = "code";
  private String responseStatusTag = "msg";
  
  
  
  private Vector<ApiActionListener> listeners = new Vector<ApiActionListener>(); 
  private boolean ignoreResponseCode = false;
  private Context context;
  private ErrorHandler errorHandler;
  
  public ActionHandler(Context context){
    this.context = context;	  
  }
  
  protected Context getContext(){
    return context;	    
  }
  
  public void setErrorHandler(ErrorHandler errorHandler) {
	this.errorHandler = errorHandler;
  }
  
  public ErrorHandler getErrorHandler(){
    return errorHandler;	  
  }
  
  protected void setResponseCodeTag(String tag){
    responseCodeTag = tag;	  
  }
  
  protected void setResponseStatusTag(String tag){
    responseStatusTag = tag;	  
  }
  
  protected void setIgnoreResponseCode(boolean ignore){
    ignoreResponseCode = ignore;	  
  }
  
  protected DataReader request(Object source, String url){
    return request(source, new DataReaderOption(context, url));	  
  }
  
  protected DataReader request(Object source, DataReaderOption dOption){
    DataReader reader = new DataReader(dOption);
    request(source, reader);
    return reader;
  }
  
  protected void request(Object source, DataReader reader){
    new ApiCallBack(source, reader).execute();	  
  }
  
  protected String getUrlForGetMethod(String baseUrl, Hashtable<String, String> table){
    return getUrlForGetMethod(baseUrl, table, true);	  
  }
  
  protected String getUrlForGetMethod(String baseUrl, Hashtable<String, String> table, boolean isEncode){ 
    return baseUrl + HttpUtils.getParam(table, false, isEncode);	  
  }
  
  public void addSuccessCode(String code){
    String[] tmp = new String[successCode.length + 1];
    System.arraycopy(successCode, 0, tmp, 0, successCode.length);
    tmp[successCode.length] = code;
    successCode = tmp;
  }  
  
  public void addListener(ApiActionListener listener){
    if(listeners == null)
	  listeners = new Vector<ApiActionListener>();
    if(listener == null)
      return;
    if(listeners.contains(listener))
      return;
	listeners.addElement(listener);
  }
  public void removeAllListener(){
    if(listeners == null)
	  return;
	listeners.removeAllElements();	  
  }
  public void removeListener(ApiActionListener listener){
    if(listeners == null)
	  return;
	listeners.removeElement(listener);	  
  }
  
//  protected void actionSuccess(Object source,DataReader reader){
//    fireSuccess(source, reader , reader);	  
//  }
  
//  protected void actionFail(Object source,DataReader reader){
//	String status = reader.getTableData(responseStatusTag);
//    fireError(source, status, reader);	  
//  }
	
//  protected void fireAction(Object source,String action,Object data,DataReader returnData){
//    ApiAction apiAction = new ApiAction(source,action,data);    
//    apiAction.setReturnDataReader(returnData);
//    
//    if(!apiAction.isSuccess() && returnData != null){
//      apiAction.setErrorCode(returnData.getErrorCode());	
//    }
//    
//    Enumeration<ApiActionListener> list = listeners.elements();
//    while(list.hasMoreElements()){
//      ApiActionListener listener  = list.nextElement();	
//      listener.onAction(apiAction);
//    }
//	
//  }
  
  protected void onFirAction(ApiAction apiAction){}
  
  protected void fireAction(ApiAction apiAction){
    onFirAction(apiAction);
    Enumeration<ApiActionListener> list = listeners.elements();
    while(list.hasMoreElements()){
      ApiActionListener listener  = list.nextElement();	
      listener.onAction(apiAction);
    }
	
  }
  
//  protected void fireSuccess(Object source,DataReader returnData){
//    fireSuccess(source,"Success",returnData);	  
//  }
//  
//  protected void fireSuccess(Object source,Object data,DataReader returnData){
//    //fireAction(source,ApiActionListener.ACTION_SUCCESS, data,returnData);	
//    ApiAction apiAction = new ApiAction(source, ApiActionListener.ACTION_SUCCESS, data);    
//    apiAction.setReturnDataReader(returnData);
//    
//    fireAction(apiAction);
//    
//  }
  
//  protected void fireError(Object source,DataReader returnData){
//    fireError(source,"Error", returnData);	  
//  }
//  protected void fireError(Object source,Object data,DataReader returnData){
//    
//    ApiAction apiAction = new ApiAction(source, ApiActionListener.ACTION_ERROR, data);    
//    apiAction.setReturnDataReader(returnData);
//    
//    
//    if(!apiAction.isSuccess() && returnData != null){
//      apiAction.setErrorCode(returnData.getErrorCode());	
//    }
//    
//    fireAction(apiAction);
//    
//  }
  
  protected boolean isSuccess(String code){
	if(ignoreResponseCode)
	  return true;
	if(code == null)
	  return false;
    for(int i = 0;i < successCode.length;i++){
      if(code.equals(successCode[i]))
        return true;
    }
    return false;
  } 
  
  private class ApiCallBack implements DataReaderListener {
    private Object source;
    private DataReader reader;
	
	public ApiCallBack(Object source, DataReader dataReader){
	  this.reader = dataReader;
	  this.source = source;
	  reader.setListener(this);	  
	}
	
	public void execute(){
	  reader.read();	
	}

	@Override
	public void onFinish(DataReader reader) {	  
	  if(reader.getReadCode() == reader.READ_OK){		
		String statusCode = reader.getTableData(responseCodeTag); 
		String status = reader.getTableData(responseStatusTag);
	    if(isSuccess(statusCode)){
		  //actionSuccess(source,reader);	
	      ApiAction apiAction = new ApiAction(source, ApiActionListener.ACTION_SUCCESS, reader); 
	      apiAction.setStatusText(status);
	      fireAction(apiAction);
		}else{
			
		  
//		  fireError(source, status, reader);	
		  int errorCode = ApiActionListener.API_RESPONSE_ERROR;	
		  if(errorHandler != null)
		    status = errorHandler.handlerError(errorCode, status);
		  ApiAction apiAction = new ApiAction(source, ApiActionListener.ACTION_ERROR, status);    
		  apiAction.setReturnDataReader(reader);	
		  apiAction.setErrorCode(ApiActionListener.API_RESPONSE_ERROR);
		  fireAction(apiAction);
		}	 
	  }else{ 
	    String error = reader.getError();
	    
	    if(errorHandler != null)
	      error = errorHandler.handlerError(reader.getErrorCode(), error);
	    
	    ApiAction apiAction = new ApiAction(source, ApiActionListener.ACTION_ERROR, error);    
	    apiAction.setReturnDataReader(reader);    
	    apiAction.setErrorCode(reader.getErrorCode());	    
	    fireAction(apiAction);
	    
	  }		
	}

			
	
		  
  }	
}
