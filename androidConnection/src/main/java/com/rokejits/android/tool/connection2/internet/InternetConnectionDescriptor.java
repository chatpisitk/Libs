package com.rokejits.android.tool.connection2.internet;

import com.rokejits.android.tool.connection2.SetableConnectionDescriptor;

import java.util.List;
import java.util.Map;

public abstract class InternetConnectionDescriptor extends SetableConnectionDescriptor{

  public InternetConnectionDescriptor(){}

  public InternetConnectionDescriptor(int errorCode, String error){
    setError(errorCode, error);
  }

  public abstract Map<String, List<String>> getAllRequestHeader();
  public abstract List<String> getRequestHeaders(String field);
  public abstract Map<String, List<String>> getAllResponseHeader();	
  public abstract List<String> getResponseHeaders(String field);  
  
}
