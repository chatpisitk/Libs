package com.rokejits.android.tool.apihandler3;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import android.content.Context;

import com.rokejits.android.tool.connection2.handler.ErrorHandler;
import com.rokejits.android.tool.data.DataReader;
import com.rokejits.android.tool.data.DataReaderListener;
import com.rokejits.android.tool.data.DataReaderOption;



public class ApiHandler {
  
  private Vector<ApiHandlerListener> listeners = new Vector<ApiHandlerListener>(); 
  private Context context;
  private ErrorHandler errorHandler;
  private ApiHandlerAdapter apiHandlerAdapter;
  
  public ApiHandler(Context context, ApiHandlerAdapter apiHandlerAdapter){
    this.context = context;
    this.apiHandlerAdapter = apiHandlerAdapter;
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
  
  public String getUrlForGetMethod(String baseApi, Hashtable<String, String> params){
    return apiHandlerAdapter.getUrlForGetMethod(baseApi, params);	  
  }
  
  protected DataReader<?> request(Object source, String url){
    return request(source, new DataReaderOption(context, url));	  
  }
  
  protected DataReader<?> request(Object source, DataReaderOption dOption){
	DataReader<?> reader = new DataReader(dOption);
    request(source, reader);
    return reader;
  }
  
  protected void request(Object source, DataReader<?> reader){
    new ApiCallBack(source, reader).execute();	  
  }  
  
  public void addListener(ApiHandlerListener listener){
    if(listeners == null)
	  listeners = new Vector<ApiHandlerListener>();
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
  public void removeListener(ApiHandlerListener listener){
    if(listeners == null)
	  return;
	listeners.removeElement(listener);	  
  } 
  
  protected void fireAction(ApiResponse apiResponse){    
    Enumeration<ApiHandlerListener> list = listeners.elements();
    while(list.hasMoreElements()){
      ApiHandlerListener listener  = list.nextElement();	
      listener.onAction(apiResponse);
    }	
  }  
  
  private class ApiCallBack implements DataReaderListener {
    private Object source;
    private DataReader<?> reader;
	
	public ApiCallBack(Object source, DataReader<?> dataReader){
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
	    boolean isSuccess = apiHandlerAdapter.isSuccess(reader);
	    String status = apiHandlerAdapter.getStatusText(reader);
	    if(isSuccess){
	      ApiResponse apiAction = new ApiResponse(source, ApiHandlerListener.ACTION_SUCCESS, reader); 
		  apiAction.setStatusText(status);
		  fireAction(apiAction);	
	    }else{
	      ApiResponse apiAction = new ApiResponse(source, ApiHandlerListener.ACTION_ERROR, status);
	      int errorCode = ApiHandlerListener.API_RESPONSE_ERROR;	
		  if(errorHandler != null)
		    status = errorHandler.handlerError(errorCode, status);
		  apiAction.setReturnDataReader(reader);	
		  apiAction.setErrorCode(ApiHandlerListener.API_RESPONSE_ERROR);
		  fireAction(apiAction);
	    }
//		ApiResponse apiResponse = apiHandlerAdapter.onApiCallback(reader);   
//		String statusCode = reader.getTableData(responseCodeTag); 
//		String status = reader.getTableData(responseStatusTag);
//	    if(isSuccess(statusCode)){
//		  //actionSuccess(source,reader);	
//	      ApiAction apiAction = new ApiAction(source, ApiActionListener.ACTION_SUCCESS, reader); 
//	      apiAction.setStatusText(status);
//	      fireAction(apiAction);
//		}else{
//			
//		  
////		  fireError(source, status, reader);	
//		  int errorCode = ApiActionListener.API_RESPONSE_ERROR;	
//		  if(errorHandler != null)
//		    status = errorHandler.handlerError(errorCode, status);
//		  ApiAction apiAction = new ApiAction(source, ApiActionListener.ACTION_ERROR, status);    
//		  apiAction.setReturnDataReader(reader);	
//		  apiAction.setErrorCode(ApiActionListener.API_RESPONSE_ERROR);
//		  fireAction(apiAction);
//		}	 
	  }else{ 
	    String error = reader.getError();
	    
	    if(errorHandler != null)
	      error = errorHandler.handlerError(reader.getErrorCode(), error);
	    
	    ApiResponse apiAction = new ApiResponse(source, ApiHandlerListener.ACTION_ERROR, error);    
	    apiAction.setReturnDataReader(reader);    
	    apiAction.setErrorCode(reader.getErrorCode());	    
	    fireAction(apiAction);
	    
	  }		
	}

			
	
		  
  }	
}
