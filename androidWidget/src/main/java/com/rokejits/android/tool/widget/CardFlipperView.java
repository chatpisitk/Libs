package com.rokejits.android.tool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.rokejits.android.tool.animation.Flip3dAnimation;
import com.rokejits.android.tool.utils.TimeUtils;

public class CardFlipperView extends RelativeLayout{
  private int currentCardIndex = 0;
  private GestureDetector gestureDetector;
  private int duration = TimeUtils.SEC / 2;
  private boolean onAnimate = false;
	
  public CardFlipperView(Context context) {
	super(context);
	gestureDetector = new GestureDetector(onGestureDetector);
  }

  public CardFlipperView(Context context, AttributeSet attrs) {
	super(context, attrs);	
	gestureDetector = new GestureDetector(onGestureDetector);
  }
  
  public void setDuration(int duration){
    this.duration = duration;	  
  }
  
  public int getCurrentCardIndex(){
    return currentCardIndex;	  
  }
  
  @Override
  public boolean onTouchEvent(MotionEvent event) {	
	return gestureDetector.onTouchEvent(event);
  }
  
  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
	super.onLayout(changed, l, t, r, b);
	for(int i = 0; i < getChildCount();i++){
	  View v = getChildAt(i);
	  if(i == currentCardIndex){
	    v.setVisibility(View.VISIBLE);	  
	  }else{
		v.setVisibility(View.GONE);   
	  }
	}
  }
  
  public boolean flipLeftToRight(){
	if(onAnimate)
	  return false;
    View prevView = getChildAt(currentCardIndex);
    if(currentCardIndex == 0){
      currentCardIndex = getChildCount() - 1;	
    }else{
      currentCardIndex--;	
    }
    View nextView = getChildAt(currentCardIndex);
    Flip3dAnimation flipAnimation = new Flip3dAnimation(0, 90, prevView.getWidth()/2, prevView.getHeight()/2);
    flipAnimation.setInterpolator(new AccelerateInterpolator());
    flipAnimation.setDuration(duration);
    flipAnimation.setAnimationListener(new NextFlipAnimation(prevView, nextView, -90, 0));
    prevView.startAnimation(flipAnimation);
    onAnimate = true;
    return true;
  }
  
  public boolean flipRightToLeft(){
	if(onAnimate)
	  return false;
    View prevView = getChildAt(currentCardIndex);
    if(currentCardIndex == getChildCount() - 1){
      currentCardIndex = 0;	
    }else{
      currentCardIndex++;	
    }
    View nextView = getChildAt(currentCardIndex);
    Flip3dAnimation flipAnimation = new Flip3dAnimation(0, -90, prevView.getWidth()/2, prevView.getHeight()/2);
    flipAnimation.setInterpolator(new AccelerateInterpolator());
    flipAnimation.setDuration(duration);
    flipAnimation.setAnimationListener(new NextFlipAnimation(prevView, nextView, 90, 0));
    prevView.startAnimation(flipAnimation);
    onAnimate = true;
    return true;
  }
  
  private OnGestureListener onGestureDetector = new OnGestureListener() {	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
	  Log.e("CardFlipperView", "onSingleTapUp");	
	  return false;
	}
		
	@Override
	public void onShowPress(MotionEvent e) {Log.e("CardFragment", "onShowPress");}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	  Log.e("CardFlipperView", "onScroll");	
      return false; 
    }
		
	@Override
	public void onLongPress(MotionEvent e) {
	  Log.e("CardFlipperView", "onLongPress");	
	}
		
	@Override
	public boolean onDown(MotionEvent e) {
	  Log.e("CardFlipperView", "onDown");	
	  if(onAnimate)
	    return false;
      return true; 
	}
	
	@Override
	public boolean onFling(MotionEvent start, MotionEvent end, float velocityX, float velocityY) {
	  Log.e("CardFlipperView", "onFling");	  
	  if(end.getRawX() < start.getRawX()){
	    flipRightToLeft();       	   
	  }else if(end.getRawX() > start.getRawX()){
	    flipLeftToRight();	
	  }	  
	  return true;
	}
		
		
  };
  
  class NextFlipAnimation implements AnimationListener{
    private View prevView, nextView;
    private int startDegree, endDegree;
    
    public NextFlipAnimation(View prevView, View nextView, int startDegree, int endDegree){
      this.prevView = prevView;
      this.nextView = nextView;
      this.startDegree = startDegree;
      this.endDegree = endDegree;
    }
    
	@Override
	public void onAnimationEnd(Animation animation) { 
	  prevView.setVisibility(View.GONE);
	  nextView.setVisibility(View.VISIBLE);
	  Flip3dAnimation an1 = new Flip3dAnimation(startDegree, endDegree, nextView.getWidth()/2, nextView.getHeight()/2);
	  an1.setInterpolator(new DecelerateInterpolator());
	  an1.setDuration(duration);
	  an1.setAnimationListener(new AnimationListener() {		
		@Override
		public void onAnimationStart(Animation animation) {}		
		@Override
		public void onAnimationRepeat(Animation animation) {}
		
		@Override
		public void onAnimationEnd(Animation animation) {
		  onAnimate = false;			
		}
	  });
	  nextView.startAnimation(an1);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {}
	@Override
	public void onAnimationStart(Animation animation) {}
	  
  } 
  
  
}
