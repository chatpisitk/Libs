package com.rokejits.android.tool.view.adapterview;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.ListAdapter;
import android.widget.Scroller;

import com.rokejits.android.tool.os.UIHandler;

public class CubicPager extends MyHorizontalAdapterView{

  private Camera mCamera;	
  private boolean onTouch = false, needCheckMiddle;
  private Scroller mScroller;
  private int selectedIndex = -1;
	
  public CubicPager(Context context) {
	super(context);
	init();
  }	
	
  public CubicPager(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
  }
  
  private void init(){
    setStaticTransformationsEnabled(true);	  
    mCamera = new Camera();   
  }
  
  @Override
  protected void onPreHandlerView() {
	mScroller = getScroller();
  }
  
  @Override
  protected boolean onSingleTapConfirmed(MotionEvent e) {
	Scroller mScroller = getScroller();
	if(!mScroller.isFinished())
	  return true;
	
	int count = getChildCount();
	if(count == 0)
	  return false;
	if(count > 1)
	  return true;
	int position = getLeftIndex();
	OnItemClickListener mOnItemClicked = getOnItemClickListener();  
    if(mOnItemClicked != null)
      mOnItemClicked.onItemClick(this, getChildAt(0), position, getAdapter().getItemId(position));   		
    return true;	  
  }
  
  @Override
  public void setSelection(int position) {
	synchronized (this) {
	  ListAdapter adapter = getAdapter();
	  if(adapter == null)
	    return;
	  if(!mScroller.isFinished())
	    mScroller.forceFinished(true);
	  removeAllViewsInLayout();
	  int count = getChildCount();
	  if(count > 1){
	    if(position > 0)
	      position = position - 1;	 	  
	  }
	  setLeftIndex(position);
	  doDataChanged();
	  setScroll(0);	
	}    
  }
  
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
	boolean result = super.dispatchTouchEvent(ev);
	int action = ev.getAction();
	switch (action) {
	  case MotionEvent.ACTION_DOWN:
	    onTouch = true;
	    needCheckMiddle = false;
	  break;
	  case MotionEvent.ACTION_UP:
	  case MotionEvent.ACTION_OUTSIDE:
		if(onTouch && mScroller.isFinished()){	      
	      checkMiddle();
		}else{
		  needCheckMiddle = true;	
		}
		onTouch = false;
	  break;
	}
	return result;
  }
  
  private void checkMiddle(){
    new UIHandler().postDelayed(new Runnable() {
				
		@Override
		public void run() {
		 doCheckMiddle();
		}
	}, 100); 
  }
	  
  private void doCheckMiddle(){
    int scrollX = Integer.MAX_VALUE;
    for(int i = 0;i < getChildCount();i++){
      int left = getChildAt(i).getLeft();	 
	  int dX = left;
	  if(Math.abs(dX) < Math.abs(scrollX))
	    scrollX = dX;	
	}
    if(scrollX != 0)
      scrollBy(scrollX, true);	  
    
    needCheckMiddle = false;
  }
  
  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
	super.onLayout(changed, left, top, right, bottom);
	Scroller mScroller = getScroller();
	if(mScroller.isFinished()){
	  if(!onTouch && needCheckMiddle)
	    checkMiddle();
	}
	
	int childCount = getChildCount();
	OnItemSelectedListener onItemSelectedListener = getOnItemSelectedListener();
	if(childCount > 0){
	  int index = -1;
	  int minDiff = Integer.MAX_VALUE;
	  View view = null;
	  for(int i = 0;i < childCount;i++){
		View child = getChildAt(i);
	    int diff = Math.abs(child.getLeft());     
	    if(diff < minDiff){
	      minDiff = diff;
	      index = i;	    
	      view = child;
	    }
	  }
	  
	  if(index != -1){	    
	    int sIndex = -1;
	    if(childCount == 2){
	      sIndex = getLeftIndex() + index;	
	    }else{
	      if(index == 0)
	        sIndex = getLeftIndex();
	      else
	        sIndex = getLeftIndex() + index; 
	    }	
	    
	    int tmpIndex = selectedIndex;
	    selectedIndex = sIndex;
	    if(selectedIndex != tmpIndex && onItemSelectedListener != null)
	      onItemSelectedListener.onItemSelected(this, view, selectedIndex, getAdapter().getItemId(selectedIndex));	    
	  }	  
	}else{
	  selectedIndex = -1;
	  if(onItemSelectedListener != null)
	    onItemSelectedListener.onNothingSelected(this);
	}
	
	
	postInvalidate(0, 0, getMeasuredWidth(), getMeasuredHeight());
  }
  
  @Override
  protected boolean getChildStaticTransformation(View child, Transformation t) {
	if(mCamera == null)
	  mCamera = new Camera();
    int x = child.getLeft();    
    boolean left = indexOfChild(child) == 0;   
    
    float percent = Math.abs((float)(x)) / getWidth();
    float diffDegree = 90;
    float d = (int) (diffDegree * percent);
    
    doChildTransformation(child, t, d, left);
       
	return true;
  }
  
  private void doChildTransformation(View child, Transformation t, float degree, boolean left){
	//Log.e(left + "/" + degree);
	float d = degree;
	int childCenterX = 0;
  	int childCenterY = child.getHeight()/2;
	if(left){
	  d = -degree;
	  childCenterX = child.getWidth();
	}
      	 
  	  
  	Matrix matrix = t.getMatrix();	
  	mCamera.save();
  	//mCamera.rotateX(degree);
  	mCamera.rotateY(d);
  	//mCamera.rotateZ(-degree);
  	mCamera.getMatrix(matrix);
  	mCamera.restore();
  	matrix.preTranslate(-childCenterX, -childCenterY);
    matrix.postTranslate(childCenterX, childCenterY); 
  }
  
  @Override
  protected void positionView(int index, int leftEdge, View view) {
	super.positionView(index, leftEdge, view);
    
  }
  
  @Override
  protected void addLeftView(int index, int rightEdge, View view) {
	addAndMeasureChild(view, 0);	
	positionView(index, rightEdge - view.getMeasuredWidth(), view);
  }
  
  @Override
  protected void addRightView(int index, int leftEdge, View view) {
    addAndMeasureChild(view, -1);	
	positionView(index, leftEdge, view);
  }
  
  private void addAndMeasureChild(final View child, int viewPos) {
	LayoutParams params = child.getLayoutParams();
	if(params == null) {
	  params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
		
	addViewInLayout(child, viewPos, params, true);
	child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY), 
			      MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
  }  
  

}
