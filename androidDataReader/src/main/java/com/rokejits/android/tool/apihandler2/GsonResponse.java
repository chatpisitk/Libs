package com.rokejits.android.tool.apihandler2;

import android.content.Context;

public abstract class GsonResponse {
  
  private Context context;
  
  public void setContext(Context context){
    this.context = context;	  
  }
  
  public Context getContext(){
    return context;	  
  }	
	
  public abstract boolean isSuccess();	
  public abstract String getError();
  
}
