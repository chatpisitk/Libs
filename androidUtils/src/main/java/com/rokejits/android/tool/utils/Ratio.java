package com.rokejits.android.tool.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class Ratio implements OnGlobalLayoutListener{

  private View view;
  private float ratio = 0;	  
	  
  public Ratio(View view, float ratio){
    this.view = view;
	this.ratio = ratio;   
  }	
		
  @Override
  public void onGlobalLayout() {
    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);    
    if(ratio == 0)
      return;
    int width = view.getWidth();
    int height = view.getHeight();	  
    ViewGroup.LayoutParams params = view.getLayoutParams();
    if(ratio > 1){
      height = (int) (width / ratio);	  
    }else if(ratio > 0 && ratio < 1){
      width = (int) (height * ratio);	  
    }else if(ratio == 1){
      if(width < height){
        height = width;	  
      }else if(height < width){
        width = height;	  
      }	
    }
		  
    params.width = width;
    params.height = height;
    view.setLayoutParams(params);
    view.requestLayout();    
  }
}
