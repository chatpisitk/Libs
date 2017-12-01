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

public class MyCoverFlow extends MyHorizontalAdapterView{

  private int mViewWidth = -1;
  private int mOffsetX;
	
  private boolean onTouch = false, needCheckMiddle;
  private float degree = 60;
  private Camera mCamera;
  private Scroller mScroller;
  private int selectedIndex = -1;
  
  public MyCoverFlow(Context context) {
	super(context);
	init();
  }	
	
  public MyCoverFlow(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
  }
  
  private void init(){
    setStaticTransformationsEnabled(true);	  
    mCamera = new Camera();
    
  }
  
  private void updateOnItemClicked(View view, int position){
    OnItemClickListener mOnItemClicked = getOnItemClickListener();  
    if(mOnItemClicked != null)
      mOnItemClicked.onItemClick(this, view, position, getAdapter().getItemId(position));
  }
  
  @Override
  protected boolean onSingleTapConfirmed(MotionEvent e) {
	Scroller mScroller = getScroller();
	if(!mScroller.isFinished())
	  return true;
    for(int i = 0;i < getChildCount();i++){
      View child = getChildAt(i);
  	  if (isEventWithinView(e, child)) {  		  
	    int index = getLeftIndex() + i;
	    int childCount = getChildCount();	    
	    if(childCount == 1){
	      updateOnItemClicked(child, index);  	
	    }else if(childCount > 1){
	      if(childCount == 2){
	    	if((i == 0 && index == 0) || 
	    	   (i == 1 && index == getAdapter().getCount() - 1))
	          updateOnItemClicked(child, index);	
	    	else{
	    	  int offsetX = child.getLeft() - mOffsetX;
	    	  scrollBy(offsetX, true);
	    	}
	      }else if(childCount == 3){
	        if(i == 1)
		      updateOnItemClicked(child, index);	
		    else{
		      int offsetX = child.getLeft() - mOffsetX;
		      scrollBy(offsetX, true);	 
		    }
	      }	      
	    }	    
	    break;
	  }  		  	  
	}			
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
	  if(position == 0){	  
	    setLeftIndex(position);	  		
	    setScroll(-mOffsetX);
	  }else{	  
	    setLeftIndex(position - 1);	
	    setScroll(mOffsetX);
	  }
	  
	  doDataChanged();
	}
  }
  
  public int getCurrentSelected(){
    return selectedIndex;	  
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
	  int dX = left - mOffsetX;
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
	    int diff = Math.abs(mOffsetX - child.getLeft());     
	    if(diff < minDiff){
	      minDiff = diff;
	      index = i;	    
	      view = child;
	    }
	  }
	  
	  if(index != -1){
	    
	    int sIndex = -1;
	    if(childCount == 3){
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
	
  }
  
  @Override
  protected boolean getChildStaticTransformation(View child, Transformation t) {
	if(mCamera == null)
	  mCamera = new Camera();
    int x = child.getLeft();	  
    int leftX = -mOffsetX;
    int centerX = mOffsetX;
    int rightX = getWidth() / 2 + mOffsetX;
	
    int current;
    float startDegree, toDegree;
    boolean left = false;    
    
    if(x < leftX){    
      current = Math.abs(leftX - x);
      startDegree = degree;
      toDegree = degree * 2;
      left = true;
    }else if(x < centerX && x >= leftX){
      current = centerX - x;     
      startDegree = 0;
      toDegree = degree;
      left = true;
    }else if(x < rightX && x >= centerX){
      current = x - centerX;
      startDegree = 0;
      toDegree = degree;
      left = false;
    }else{
      current = x - rightX;	
      startDegree = degree;
      toDegree = degree * 2;
      left = false;
    }
    
    float percent = Math.abs((float)(current) / (float)mViewWidth);
    float diffDegree = toDegree - startDegree;
    float d = startDegree + (int) (diffDegree * percent);
     
    doChildTransformation(child, t, d, left);
       
	return true;
  }
  
  private void doChildTransformation(View child, Transformation t, float degree, boolean left){
	float d = degree;
	if(!left)
	  d = -degree;
    int childCenterX = child.getWidth()/2;
  	int childCenterY = child.getHeight()/2;  	 
  	  
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
  protected void onPreHandlerView() {
	boolean first = mViewWidth == -1;
	if(first){
      mViewWidth = getMeasuredWidth() / 2;
      mOffsetX = mViewWidth / 2;
      mScroller = getScroller();
      setSelection(0);
	}
  }
  
  @Override
  protected boolean isHitLeftEdge(int leftEdge, int index) {
    if(index == 0 && leftEdge >= mOffsetX){      
      return true;	
    }	
    return false;
  }
  
  @Override
  protected int getMinLeftEdge(int leftEdge, int index) {	
	return mOffsetX;
  }
  
  @Override
  protected boolean isHitRightEdge(int rightEdge, int index) {	
	if(index == getAdapter().getCount() - 1 && rightEdge <= getMeasuredWidth() - mOffsetX)
	  return true;
	return false;
  }
  
  @Override
  protected int getMaxRightEdge(int rightEdge, int index) {
	return getMeasuredWidth() - mOffsetX;
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
	child.measure(MeasureSpec.makeMeasureSpec(mViewWidth, MeasureSpec.EXACTLY), 
			      MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
  }  

}
