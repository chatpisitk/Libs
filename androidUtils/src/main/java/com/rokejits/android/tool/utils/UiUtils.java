package com.rokejits.android.tool.utils;

import android.content.Context;
import android.util.TypedValue;

public class UiUtils {
  public static float convertDpiToPixel(Context context, int dp){
    return applyDimension(context, TypedValue.COMPLEX_UNIT_DIP, dp);  
  }
  
  public static float getSPToPixel(Context context, int sp){
    return applyDimension(context, TypedValue.COMPLEX_UNIT_SP, sp);	  
  }
  
  public static final float applyDimension(Context context, int unit, int value){
    return TypedValue.applyDimension(unit, value, context.getResources().getDisplayMetrics()); 	  
  }
}
