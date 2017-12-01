package com.rokejits.android.tool.jsonapihandler;

import java.io.InputStream;

import android.content.Context;

import com.rokejits.android.tool.connection2.ConnectionListener;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.handler.ErrorHandler;
import com.rokejits.android.tool.connection2.handler.SimpleErrorHandler;
import com.rokejits.android.tool.connection2.internet.InternetConnection;
import com.rokejits.android.tool.os.UIHandler;
import com.rokejits.android.tool.utils.IOUtils;

public abstract class JSONApiHandler {

  private Context context;
  private ErrorHandler errorHandler;
  private OnApiListener onApiListener;
  
  public JSONApiHandler(Context context){
    this.context = context;
    
    setErrorHandler(new SimpleErrorHandler());
  }
  
  public Context getContext(){
    return context;	  
  }
  
  public void setErrorHandler(ErrorHandler errorHandler){
    this.errorHandler = errorHandler;	  
  }
  
  public ErrorHandler getErrorHandler(){
    return errorHandler;	  
  }
  
  public void setOnApiListener(OnApiListener onApiListener) {
	this.onApiListener = onApiListener;
  }
  
  protected void request(Object source, IConnection2 iConnection2){
    new ApiRequest(source, iConnection2).request();	  
  }
  
  protected abstract ApiResponse getApiResponseForSource(Object source);
  
  private void fireAction(final ApiResponse apiResponse){
	new UIHandler().post(new Runnable() {		
  	  @Override
	  public void run() {
  		if(onApiListener != null){
  	      onApiListener.onAction(apiResponse);	
  	    }		
	  }
	});
      
  }
  
  private void fireError(ApiResponse apiResponse, int errorCode, String error){
    error = errorHandler.handlerError(errorCode, error);
    apiResponse.setError(errorCode, error);
    fireAction(apiResponse);
  }
  
  
  class ApiRequest implements ConnectionListener{    
    private ApiResponse apiResponse;    
    
    public ApiRequest(Object source, IConnection2 iConnection2) {	  
	  iConnection2.setErrorHandler(errorHandler);
	  iConnection2.setConnectionListener(this);
	  
	  apiResponse = getApiResponseForSource(source);
	  
	  apiResponse.setSource(source);
	  apiResponse.setConnection(iConnection2);	  
	}
    
    public void request(){
      apiResponse.getConnection().connect();	
    }

	@Override
	public void onConnected(IConnection2 iConnection2, InputStream in) {		
	  String response = IOUtils.readStringFromInputStream(in);
	  if(response == null){
	    fireError(apiResponse, InternetConnection.INTERNET_ERROR, null);	  
	  }else{
	    apiResponse.setResponse(response);
	    fireAction(apiResponse);
	  }
	}

	@Override
	public void onConnectFailed(IConnection2 iConnection2, int errorCode, String error) {
	  fireError(apiResponse, errorCode, error);	
	}	  
  }
  
  public interface OnApiListener{
	public static final int API_RESPONSE_ERROR = 0x320124;   
	  
    public void onAction(ApiResponse apiResponse);	  
  }
	
  public abstract class ApiResponse<T>{
    private IConnection2 iConnection2;
    private String response, error;
    private int errorCode;
    private Object source;   
    
    private void setSource(Object source){
      this.source = source; 	
    }
    
    public Object getSource(){
      return source;	
    }
    
    private void setConnection(IConnection2 iConnection2){
      this.iConnection2 = iConnection2;	
    }
    
    private void setResponse(String response){
      this.response = response;	
      onSetResponse(response);
    }   
    
    public void setError(int errorCode, String error){
      this.error = error;
      this.errorCode = errorCode;
    }
    
    public boolean isSource(Object source){
      return this.source.equals(source);	
    }
    
    public IConnection2 getConnection(){
      return iConnection2;	
    }
    
    public String getResponse(){
      return response;  	
    }       
    
    public int getErrorCode(){
      return errorCode;	
    }
    
    public String getError(){
      return error;	
    }
	   
    public abstract boolean isSuccess();
    public abstract T[] getDatas();
    public abstract void onSetResponse(String response);
  }
  
}
