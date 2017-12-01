package com.rokejits.android.tool.view.adapterview;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.os.UIHandler;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

public class MyHorizontalAdapterView extends AdapterView<ListAdapter>{
 
	
  private int mLeftIndex, mRightIndex = -1;
  private int mScrollX, mLastScrollX;
  private boolean mDataChanged = false; 
  
  private ListAdapter mAdapter;
  
  private Scroller mScroller;
  private GestureDetector mGesture;
  
  
  private Queue<View> mRemovedViewQueue = new LinkedList<View>();
//  private OnItemSelectedListener mOnItemSelected;
//  private OnItemClickListener mOnItemClicked;
//  private OnItemLongClickListener mOnItemLongClicked;
  
  public MyHorizontalAdapterView(Context context) {
	super(context);
	init();
  }	
	
  public MyHorizontalAdapterView(Context context, AttributeSet attrs) {
	super(context, attrs);
	init();
  }
  
  private void init(){
	Context context = getContext();
    mScroller = new Scroller(context);
    mGesture = new GestureDetector(context, mGestureListener);
  }  
  
  protected Scroller getScroller(){
    return mScroller;	  
  }
  
  public int getLeftIndex(){
    return mLeftIndex;	  
  }
  
  protected void setLeftIndex(int index){
    mLeftIndex = index;
  }
  
//  @Override
//  public void setOnItemClickListener(OnItemClickListener listener) {
//    mOnItemClicked = listener;
//  }
//  
//  @Override
//  public void setOnItemSelectedListener(OnItemSelectedListener listener) {
//	mOnItemSelected = listener;
//  }
//  
//  public OnItemSelectedListener getOnItemSelectedListener(){
//    return mOnItemSelected;	  
//  }
//  
//  @Override
//  public void setOnItemLongClickListener(OnItemLongClickListener listener) {
//    mOnItemLongClicked = listener;
//  }

  @Override
  public void setAdapter(ListAdapter adapter) {
    if(mAdapter != null) {
  	  mAdapter.unregisterDataSetObserver(mDataObserver);
  	}
      
  	mAdapter = adapter;
  	if(mAdapter != null)
  	  mAdapter.registerDataSetObserver(mDataObserver);
  	reset();
  	
  }   
  
  @Override
  public ListAdapter getAdapter() {
	return mAdapter;
  }

  @Override
  public View getSelectedView() {
	return null;
  }
  
  @Override
  public void setSelection(int position) {	
	
  }
  
  protected synchronized void reset(){	    
	if(!mScroller.isFinished())  
	  mScroller.forceFinished(true);
	mLastScrollX = 0;
	mScrollX = 0;
	removeAllViewsInLayout();
    requestLayout();		  
  } 
  
  public void scrollBy(int x, boolean animate){
	synchronized (this) {
	  if(!mScroller.isFinished())
	    mScroller.forceFinished(true);
	  if(animate){
	    mLastScrollX = 0;
	    mScroller.startScroll(0, 0, x, 0);
	  }else{
		if(getChildCount() == 0){
		  mScrollX = -x;	
		}else{
		  View view = getChildAt(0);
		  mScrollX = view.getLeft() - x;
		}	    
	  }
	  requestLayout();	
	}	
  }
  
  protected void setScroll(int x){
    synchronized (this) {
      if(!mScroller.isFinished())
        mScroller.forceFinished(true);	
      if(getChildCount() == 0){
	    mScrollX = -x;	
	  }else{
	    View view = getChildAt(0);
	    mScrollX = x + view.getLeft();
	  }      
      requestLayout();      
	}	  
  }
  
  protected void onPreHandlerView(){}
  
  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);  
    synchronized (this) {
      if(mAdapter == null)
        return;           
      onPreHandlerView();
      if(!mScroller.isFinished()){
        mScroller.computeScrollOffset();
        int scrollX = mScroller.getCurrX();
        mScrollX -= scrollX - mLastScrollX;
        mLastScrollX = scrollX;
      }
    	    
      removeInVisibleView(mScrollX);
        
      int leftEdge = mScrollX;
      int rightEdge = mScrollX;
      if(getChildCount() > 0){
        View lastChild = getChildAt(getChildCount() - 1);
        leftEdge = getChildAt(0).getLeft() + mScrollX;	
        rightEdge = lastChild.getLeft() + lastChild.getMeasuredWidth() + mScrollX;
    	  
        if(isHitLeftEdge(leftEdge, mLeftIndex)){
      	  int oldLeftEdge = leftEdge;
    	  leftEdge = getMinLeftEdge(leftEdge, mLeftIndex);	
    	  rightEdge -= oldLeftEdge - leftEdge;
    	  mScroller.forceFinished(true);
        }else if(isHitRightEdge(rightEdge, mRightIndex)){
       	  int oldRightEdge = rightEdge;
    	  rightEdge = getMaxRightEdge(rightEdge, mRightIndex);
    	  leftEdge += rightEdge - oldRightEdge;
    	  mScroller.forceFinished(true);
    	}
      }      
    	    
 	  if(mDataChanged){
        rightEdge = leftEdge;
        mRightIndex = mLeftIndex - 1;
        if(mLeftIndex >= mAdapter.getCount()){
          mLeftIndex = 0;      
          mRightIndex = -1;    
          leftEdge = 0;
          rightEdge = 0;
        }      
    	      
        removeAllViewsInLayout();
        mDataChanged = false;	
      }      
    	    
      if(getChildCount() > 0){
        int index = mLeftIndex;
        int x = leftEdge;
        for(int i = 0;i < getChildCount();i++, index++){
          View view = getChildAt(i);
          positionView(index, x, view);
          x = view.getLeft() + view.getMeasuredWidth();
        }	
      }
    	    
      while(leftEdge > 0 && mLeftIndex > 0){  
        mLeftIndex--;
        View view = mAdapter.getView(mLeftIndex, mRemovedViewQueue.poll(), this);	
        addLeftView(mLeftIndex, leftEdge, view);
        leftEdge = view.getLeft();
    	      
       }
    	    
       while(rightEdge < getMeasuredWidth() && mRightIndex < mAdapter.getCount() - 1){    
         mRightIndex++;	
         View view = mAdapter.getView(mRightIndex, mRemovedViewQueue.poll(), this);	
         addRightView(mRightIndex, rightEdge, view);
         rightEdge = view.getLeft() + view.getMeasuredWidth();	 
    	     
       }    
    	    
       mScrollX = 0;
    	    
       if(!mScroller.isFinished()){
         post(new Runnable() {
    			
     	   @Override
      	   public void run() {
             requestLayout();			
      	   }
         });        
       }	
	 }
    
  }
  
  protected void onDataSetChanged(){}
  
  protected boolean isHitLeftEdge(int leftEdge, int index){
    if(index == 0 && leftEdge >= 0)
      return true;
    return false;
  }
  
  protected int getMinLeftEdge(int leftEdge, int index){
    return 0;	  
  }
  
  protected boolean isHitRightEdge(int rightEdge, int index){	
    if(index == mAdapter.getCount() - 1 && rightEdge <= getMeasuredWidth()){
      return true;	
    }	  
    return false;
  }
  
  protected int getMaxRightEdge(int rightEdge, int index){
    return getMeasuredWidth();	  
  }  
  
  protected void positionView(int index, int leftEdge, View view){
    view.layout(leftEdge, 0, leftEdge + view.getMeasuredWidth(), view.getMeasuredHeight());  	  
  }
  
  protected void addRightView(int index, int leftEdge, View view){
    addAndMeasureChild(view, -1);	    
    positionView(index, leftEdge, view);    
  }
  
  protected void addLeftView(int index, int rightEdge, View view){
    addAndMeasureChild(view, 0);
    
    int childWidth = view.getMeasuredWidth();
    int left = rightEdge - childWidth;    
    positionView(index, left, view);
  }  
  
  private void removeInVisibleView(int mScrollX){
    for(int i = 0;i < getChildCount();i++){
      View child = getChildAt(i);
      int left = child.getLeft() + mScrollX;
      int width = child.getMeasuredWidth();
      boolean remove = false;
      if(left >= getMeasuredWidth()){
    	//remove right
        remove = true;    
        mRightIndex--;
      }else if(left + width <= 0){
    	//remove left
        remove = true;
        mLeftIndex++;        
      }
      
      if(remove){
    	
        mRemovedViewQueue.offer(child);
        removeViewInLayout(child);
      }
    }    
  }  
  
  private void addAndMeasureChild(final View child, int viewPos) {
	LayoutParams params = child.getLayoutParams();
	if(params == null) {
	  params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	addViewInLayout(child, viewPos, params, true);
	child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST), 
			      MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
  }
  
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
	boolean handled = super.dispatchTouchEvent(ev);
	handled |= mGesture.onTouchEvent(ev);  
    return handled;
  }
  
  protected boolean isEventWithinView(MotionEvent e, View child) {
    Rect viewRect = new Rect();
    int[] childPosition = new int[2];
    child.getLocationOnScreen(childPosition);
    int left = childPosition[0];
    int right = left + child.getWidth();
    int top = childPosition[1];
    int bottom = top + child.getHeight();
    viewRect.set(left, top, right, bottom);
    return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
  }
  
  protected boolean onDown(MotionEvent e) {
	mScroller.forceFinished(true);
	return true;
  }
  
  protected void onLongPress(MotionEvent e){
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = getChildAt(i);
  	  if (isEventWithinView(e, child)) {
  		OnItemLongClickListener mOnItemLongClicked = getOnItemLongClickListener();
	    if (mOnItemLongClicked != null) {
	      int index = mLeftIndex + i;
	      mOnItemLongClicked.onItemLongClick(MyHorizontalAdapterView.this, child, index, mAdapter.getItemId(index));
	    }
	    break;
	  }

	} 
  }
  
  protected boolean onSingleTapUp(MotionEvent e){
    return false;	  
  }
  
  protected boolean onSingleTapConfirmed(MotionEvent e) {
    for(int i = 0;i < getChildCount();i++){
      View child = getChildAt(i);
  	  if (isEventWithinView(e, child)) {			
	    int index = getLeftIndex() + i;
	    OnItemClickListener mOnItemClicked = getOnItemClickListener();
	    OnItemSelectedListener mOnItemSelected = getOnItemSelectedListener();
	    if(mOnItemClicked != null){
  	      mOnItemClicked.onItemClick(MyHorizontalAdapterView.this, child, index, mAdapter.getItemId(index));
		}
				  		  
	    if(mOnItemSelected != null){
	      mOnItemSelected.onItemSelected(MyHorizontalAdapterView.this, child, index, mAdapter.getItemId( index));
	    }  
	    break;
	  }  		  
	  
	}			
    return true;	  
  }
  
  protected boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    synchronized(MyHorizontalAdapterView.this){     
      mScrollX = -(int)distanceX;     
    }
    requestLayout(); 
    return true;
  }
  
  protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    synchronized(MyHorizontalAdapterView.this){
      mLastScrollX = 0;
	  mScroller.fling(0, 0, (int)-velocityX, 0, -2000, 2000, 0, 0);
	}
	requestLayout();
    return true;	  
  }
  
  protected final void doDataChanged(){
    synchronized(MyHorizontalAdapterView.this){
	  onDataSetChanged();  
	  mDataChanged = true;
	}
	invalidate();
	requestLayout();	  
  }
  
  private DataSetObserver mDataObserver = new DataSetObserver() {

	@Override
	public void onChanged() {
  	  doDataChanged();  
	}

	@Override
	public void onInvalidated() {
	  reset();
	  invalidate();
	  requestLayout();
	}	
  };  
  
  private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {		
	  if(MyHorizontalAdapterView.this.onSingleTapUp(e))
	    return true;
	  return super.onSingleTapUp(e);
	}
	
	public boolean onSingleTapConfirmed(MotionEvent e) {
	  if(MyHorizontalAdapterView.this.onSingleTapConfirmed(e))
	    return true;	  
	  return false;
	  
	}
	
	@Override
	public void onShowPress(MotionEvent e) {}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	  if(MyHorizontalAdapterView.this.onScroll(e1, e2, distanceX, distanceY))
	    return true;	  
	  return false;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
	  MyHorizontalAdapterView.this.onLongPress(e);		
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	  if(MyHorizontalAdapterView.this.onFling(e1, e2, velocityX, velocityY))
	    return true;	  
	  return false;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
	  return MyHorizontalAdapterView.this.onDown(e);
	}
  };

}
