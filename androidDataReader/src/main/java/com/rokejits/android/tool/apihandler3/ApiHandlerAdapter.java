package com.rokejits.android.tool.apihandler3;

import java.util.Hashtable;

import com.rokejits.android.tool.data.DataReader;

public interface ApiHandlerAdapter {

  public boolean isSuccess(DataReader<?> reader);	
  public String getStatusText(DataReader<?> reader);
  public String getUrlForGetMethod(String baseUrl, Hashtable<String, String> table);
  
}
