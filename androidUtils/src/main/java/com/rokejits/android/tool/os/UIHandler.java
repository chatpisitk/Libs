package com.rokejits.android.tool.os;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class UIHandler extends Handler{
  public static final int UPDATE_UI = 0xf0f0f1;
  private static final String UPDATE_UI_KEY = "UPDATE_UI_KEY";
  private UIHandlerListener uiListener;
  
  
  public UIHandler(){
    super(Looper.getMainLooper());	  
  }
  
  public UIHandler(Looper looper){
    super(looper);	  
  }
  
  public void setUIHandlerListener(UIHandlerListener uiListener){
    this.uiListener = uiListener;	  
  }
  
  public Message sendUpdateUi(){
    return sendMessage(UPDATE_UI);	  
  }
  
  public Message sendMessage(int source){
	Bundle b = new Bundle();
	b.putInt(UPDATE_UI_KEY, source);
    Message msg = Message.obtain();
    msg.setData(b);
    sendMessage(msg); 
    return msg;
  }	
  
  protected boolean onHandlerMessage(Message message, int source){
    return false;
  }
	
  @Override
  public final void handleMessage(Message msg) {
	int source = msg.getData().getInt(UPDATE_UI_KEY);
    if(!onHandlerMessage(msg, source)){
      if(uiListener != null)
        uiListener.onUpdateUI(source);
    }	
  }
  
  public interface UIHandlerListener {  
    public void onUpdateUI(int source); 	
  }
}
