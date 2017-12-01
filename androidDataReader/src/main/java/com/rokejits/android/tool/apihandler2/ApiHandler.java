package com.rokejits.android.tool.apihandler2;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rokejits.android.tool.connection2.handler.ErrorHandler;
import com.rokejits.android.tool.data.DataReader;
import com.rokejits.android.tool.data.DataReaderListener;
import com.rokejits.android.tool.data.DataReaderOption;

public class ApiHandler {
  private Context context;
  private ErrorHandler errorHandler;
  private ApiHandlerListener apiHandlerListener;
  
  public ApiHandler(Context context){
    this.context = context;	  
  } 
  
  public void setApiHandlerListener(ApiHandlerListener apiHandlerListener) {
	this.apiHandlerListener = apiHandlerListener;
  }
  
  public void setErrorHandler(ErrorHandler errorHandler){
    this.errorHandler = errorHandler;	  
  }
  
  protected DataReader request(Object source, GsonResponse gsonResponse, String url){
    DataReaderOption dOption = new DataReaderOption(context, url);
    
    DataReader reader = new DataReader(dOption);
    
    
    return reader;
    
  }
  
  protected void request(Object source, GsonResponse gsonResponse, DataReader reader){
    new ApiRequest(source, gsonResponse, reader).request();	  
  }
  
  private void fireAction(ApiResponse apiResponse){
    if(apiHandlerListener != null){
      apiHandlerListener.onAction(apiResponse);	
    }	  
  }
  
  public interface ApiHandlerListener{
    public void onAction(ApiResponse apiResponse);	  
  }
  
  private class ApiRequest implements DataReaderListener{
    private Object source;
    private GsonResponse gsonResponse;
    private DataReader reader;
    
    public ApiRequest(Object source, GsonResponse gsonResponse, DataReader reader) {
	  this.source = source;
	  this.gsonResponse = gsonResponse;
	  this.reader = reader;	  
	  reader.setErrorHandler(errorHandler);
	  reader.setListener(this);
    }
    
    public void request(){
      reader.read();	
    }

	@Override
	public void onFinish(DataReader reader) {
	  ApiResponse apiResponse = new ApiResponse(source, reader);
  	  if(reader.getReadCode() == reader.READ_OK){
  		String response = reader.getResponse();  		
  		Gson gson = new GsonBuilder().create();  		
  		GsonResponse gResponse = gson.fromJson(response, gsonResponse.getClass());  
  		gResponse.setContext(context);
  	    apiResponse.setResponse(gResponse);  	    
  	  }
  	  
  	  fireAction(apiResponse);
			
	}
    
  }
  
}
