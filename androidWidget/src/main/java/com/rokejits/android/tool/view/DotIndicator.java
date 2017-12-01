package com.rokejits.android.tool.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class DotIndicator implements IIndicator{
  private int DOT_WITH = 5;
  private int DOT_SPACE = 5;
  
  private Paint paint;
  
  public DotIndicator(){
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Style.FILL);
  }
  
  @Override
  public void onDrawIndicator(Canvas canvas, ViewPager viewPager) {
    PagerAdapter pAdapter = viewPager.getAdapter();	
    if(pAdapter == null)
      return;
    int dotCount = pAdapter.getCount();
    int maxWidth = canvas.getWidth() - DOT_SPACE;
    int maxCount = maxWidth / (DOT_WITH + DOT_SPACE);    
    int currentIndex = viewPager.getCurrentItem();
    
    if(maxCount > dotCount)
      maxCount = dotCount;
    
    if(currentIndex > maxCount)
      currentIndex = maxCount;
    
    int dotWidth = (DOT_WITH * maxCount) + (DOT_SPACE * (maxCount - 1));
    int startX = viewPager.getScrollX() + canvas.getWidth()/2 - dotWidth/2;
    int y = canvas.getHeight() - DOT_WITH - DOT_SPACE;
    
    for(int i = 0;i < maxCount;i++){
      if(i != currentIndex){
        paint.setColor(0xff999999);	  
      }else{
        paint.setColor(Color.BLACK);	  
      }	
      
      canvas.drawCircle(startX, y, DOT_WITH, paint);
      startX = startX + DOT_WITH;
      if(i < maxCount - 1)
        startX = startX + (DOT_SPACE * 2);      
    }
    
    
  }

}
