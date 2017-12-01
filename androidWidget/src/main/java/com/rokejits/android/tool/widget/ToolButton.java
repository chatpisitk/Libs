package com.rokejits.android.tool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.rokejits.android.tool.font.FontManager;
import com.rokejits.android.tool.widgets.R;

public class ToolButton extends Button{
  
  public ToolButton(Context context) {
	super(context);
  }
	  
  public ToolButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	init(attrs);
  }
	  
  public ToolButton(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	init(attrs);
  }

  private void init(AttributeSet attrs){
    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ToolFont);
    if(a != null){
      String toolFont = a.getString(R.styleable.ToolFont_toolFont);
      if(toolFont != null){
        setCustomFont(toolFont);	  
      }
      a.recycle();  
    }     	 
  }
	  
  public void setCustomFont(String fontName){
    FontManager fontManager = FontManager.getInstance();
    if(fontManager != null){
      Typeface typeface = fontManager.getTypeface(fontName);
      if(typeface != null)
        setTypeface(typeface);
    }
  } 

}
