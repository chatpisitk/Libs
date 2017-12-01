package com.rokejits.android.tool.font;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {
  private static FontManager self;
  private Hashtable<String, Typeface> fontTypeFaceList;
  private Context context;
	  
  public static FontManager getInstance(){
    return self;	  
  }
	  
  public static void newInstance(Context context){
    if(self == null)
      self = new FontManager(context);
  }
	  
  private FontManager(Context context){
	this.context = context;
	fontTypeFaceList = new Hashtable<String, Typeface>();  
  }
	  
  public void loadFont(String fontName, String fontLocation){
    fontTypeFaceList.put(fontName, Typeface.createFromAsset(context.getAssets(), fontLocation));    
  }
  
  public Typeface getTypeface(String fontName){    
    return fontTypeFaceList.get(fontName);
  }  
 
}
