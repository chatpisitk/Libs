package com.rokejits.android.tool;

public class Log {
  public static String TAG = "RokejitsX";
  public static boolean enabled = true;
  public static void d(String text){
	if(enabled)
      android.util.Log.d(TAG, text);	  
  }
  public static void e(String title){
    e(title, null);	  
  }
  
  public static void e(String title, Throwable e){
    if(enabled)
	  android.util.Log.e(TAG, title, e);	  
  }
  
}
