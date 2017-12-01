package com.rokejits.android.tool.data;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveData {

  protected SharedPreferences sharedPreference;
  private Context context;
  
  protected SaveData(Context context, String saveName, int mode){
	this(context, context.getSharedPreferences(saveName, mode));	
  }  
  
  protected SaveData(Context context, SharedPreferences sharedPreference){
	this.context = context;  
    this.sharedPreference = sharedPreference;	  
  }
  
  protected Context getContext(){
    return context;	  
  }
  
  protected int getInt(String key, int defaultValue){ 
    return sharedPreference.getInt(key, defaultValue);	  
  }
  
  protected float getFloat(String key, float defaultValue){ 
    return sharedPreference.getFloat(key, defaultValue);	  
  }
  
  protected long getLong(String key, long defaultValue){ 
    return sharedPreference.getLong(key, defaultValue);	  
  }
  
  protected boolean getBoolean(String key, boolean defaultValue){ 
    return sharedPreference.getBoolean(key, defaultValue);	  
  }
  
  protected String getString(String key, String defaultValue){ 
    return sharedPreference.getString(key, defaultValue);	  
  }
  
  protected Map<String, ?> getAll() { 
    return sharedPreference.getAll();	
  }
  
  protected Editor edit() {
    return sharedPreference.edit();	
  }
  
//  protected void saveData(String key, int value){
//    Editor editor = sharedPreference.edit();
//    editor.putInt(key, value);
//    editor.commit();
//  }
//  
//  protected void saveData(String key, float value){
//    Editor editor = sharedPreference.edit();
//	editor.putFloat(key, value);
//	editor.commit();
//  }
//  
//  protected void saveData(String key, long value){
//    Editor editor = sharedPreference.edit();
//	editor.putLong(key, value);
//	editor.commit();
//  }
//  
//  protected void saveData(String key, boolean value){
//    Editor editor = sharedPreference.edit();
//	editor.putBoolean(key, value);
//	editor.commit();
//  }
//  
//  protected void saveData(String key, String value){
//    Editor editor = sharedPreference.edit();
//	editor.putString(key, value);
//	editor.commit();
//  }
}
