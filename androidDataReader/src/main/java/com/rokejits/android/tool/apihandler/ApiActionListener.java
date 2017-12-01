package com.rokejits.android.tool.apihandler;



public interface ApiActionListener {
	
  public static final int API_RESPONSE_ERROR = 0x220124;	
	
  public static final String ACTION_SUCCESS = "success";
  public static final String ACTION_ERROR   = "error";
  
  
  
  public void onAction(ApiAction event);
}
