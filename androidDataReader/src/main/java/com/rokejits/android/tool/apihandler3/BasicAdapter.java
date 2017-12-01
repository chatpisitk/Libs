package com.rokejits.android.tool.apihandler3;

import java.util.Hashtable;

import com.rokejits.android.tool.utils.HttpUtils;

public abstract class BasicAdapter implements ApiHandlerAdapter{
  
  @Override
  public String getUrlForGetMethod(String baseUrl, Hashtable<String, String> table) {	
    return baseUrl + HttpUtils.getParam(table, false, true);
  }

}
