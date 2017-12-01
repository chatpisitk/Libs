package com.rokejits.android.tool.camera;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.rokejits.android.tool.data.SaveData;

public class CameraSetting extends SaveData{

  private static final String SAVE_NAME		= "camera_setting_0";	
	
  
  private static final String KEY_TOGGLE_LIGHT 	= "KEY_TOGGLE_LIGHT";
  private static final String KEY_AUTO_FOCUS 	= "KEY_AUTO_FOCUS";
  private static final String KEY_REVERSE_IMAGE = "KEY_REVERSE_IMAGE";

  private static CameraSetting self;
  
  public static final void newInstance(Context context){
    if(self == null)
      self = new CameraSetting(context);
  }
  
  public static final CameraSetting getInstance(){
    return self;	  
  }
  
  private CameraSetting(Context context) {
	super(context, SAVE_NAME, Activity.MODE_PRIVATE);	
  }

  
  public boolean isReverseImage(){
    return getBoolean(KEY_REVERSE_IMAGE, false);	  
  }
  
  public void setReverseImage(boolean reverse){
    Editor editor = edit();
    editor.putBoolean(KEY_REVERSE_IMAGE, reverse);
    editor.commit();
  }
	  
  public boolean isEnableAutoFocus(){
    return getBoolean(KEY_AUTO_FOCUS, true);	  
  } 
	  
  public void enableAutoFocus(boolean enable){
    Editor editor = edit();
    edit().putBoolean(KEY_AUTO_FOCUS, enable);
    editor.commit();  
  }
  
  public boolean isEnableTorch(){
    return getBoolean(KEY_TOGGLE_LIGHT, false);	  
  }
	  
  public void enableTorch(boolean enable){
    Editor editor = edit();
    edit().putBoolean(KEY_TOGGLE_LIGHT, enable);
    editor.commit();
  }
}
