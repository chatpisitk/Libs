package com.rokejits.android.tool.apihandler3;

public interface ApiHandlerListener {
	
  public static final int API_RESPONSE_ERROR = 0x220124;	
	
  public static final int ACTION_ERROR		= 0; 
  public static final int ACTION_SUCCESS	= ACTION_ERROR + 1;
	
  public void onAction(ApiResponse apiResponse);	
}
