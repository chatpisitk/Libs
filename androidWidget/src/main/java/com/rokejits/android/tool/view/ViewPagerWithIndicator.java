package com.rokejits.android.tool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class ViewPagerWithIndicator extends ViewPager{

  private IIndicator iIndicator;	
  private OnPageChangeListener myOnPageChangeListener;
	
  public ViewPagerWithIndicator(Context context) {
	super(context);
	init();
  }

  public ViewPagerWithIndicator(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
  }
  
  private void init(){
    super.setOnPageChangeListener(onPageChangeListener);	
    setIndicator(new DotIndicator());
  }
  
  @Override
  public void setOnPageChangeListener(OnPageChangeListener listener) {
	this.myOnPageChangeListener = listener;
  }
  
  
  public void setIndicator(IIndicator iIndicator) {
	this.iIndicator = iIndicator;
  }
 
  @Override
  protected void onDraw(Canvas canvas) {	
	super.onDraw(canvas);
	if(iIndicator != null && !isInEditMode())
	  iIndicator.onDrawIndicator(canvas, this);
  }
	
  private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
	
	@Override
	public void onPageSelected(int index) {
      if(myOnPageChangeListener != null)
	    myOnPageChangeListener.onPageSelected(index);
     
      postInvalidate();		
	}
	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	  if(myOnPageChangeListener != null)
	    myOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
	}	
	
	@Override
	public void onPageScrollStateChanged(int state) {
  	  if(myOnPageChangeListener != null)
	    myOnPageChangeListener.onPageScrollStateChanged(state);	
	}
};
  
	
  	
}
