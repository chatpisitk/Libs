package com.rokejits.android.tool.apihandler;

import com.rokejits.android.tool.data.DataReader;

public class ApiAction {

  private String action;
  
  private Object source = null;
  private Object data = null;
  private DataReader returnDataReader;
  private int errorCode;
  private String statusText;
  
  public ApiAction(Object source,String action,Object data){
	this.source = source;
    this.action = action;
    setData(data);
  }
  
  public void setData(Object data){
    this.data = data;	  
  }
  
  public void setStatusText(String statusText){
    this.statusText = statusText;	  
  }
  
  public String getStatusText(){
    return statusText;	  
  }
  
  public void setErrorCode(int errorCode){
    this.errorCode = errorCode;	   
  }
  
  public int getErrorCode(){
    return errorCode;	  
  }
  
  public Object getSource(){
    return source;	  
  }
  
  public String getAction(){
    return action;	  
  }
  
  public Object getData(){
    return data;	  
  }  
  
  public boolean isSource(Object s){
    return source.equals(s);	  
  }
  
  public boolean isSuccess(){
    return action.equals(ApiActionListener.ACTION_SUCCESS);	  
  }
  
  public void setReturnDataReader(DataReader reader){
    returnDataReader = reader;	  
  }
  
  public DataReader getReturnDataReader(){
    return returnDataReader;	  
  }
  
  public DataReader getDataReader(){
    return (DataReader) data;	  
  }
  
  public String getError(){
    return (String) data;	  
  }
    
}
