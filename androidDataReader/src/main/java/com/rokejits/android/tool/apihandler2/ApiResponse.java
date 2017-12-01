package com.rokejits.android.tool.apihandler2;

import com.rokejits.android.tool.data.DataReader;

public class ApiResponse {

  private Object source;
  private GsonResponse gsonResponse;
  private DataReader dataReader;
	
  protected ApiResponse(Object source, DataReader reader){
    this.source = source;
    this.dataReader = reader;
  }  
  
  public void setResponse(GsonResponse response){
    this.gsonResponse = response;	  
  } 
  
  public boolean isSource(Object source){
    return this.source.equals(source);	  
  }
  
  public boolean isSuccess(){
	if(gsonResponse != null)
      return gsonResponse.isSuccess();	  
	return false;
  }
  
  public GsonResponse getResponse(){
    return gsonResponse;	  
  }
  
}
