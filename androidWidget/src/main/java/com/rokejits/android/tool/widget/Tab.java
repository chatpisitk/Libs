package com.rokejits.android.tool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class Tab extends LinearLayout{

  private TabAdapter adapter;
  private OnViewClickListener onViewClickListener = new OnViewClickListener();
  private OnTabChangeListener onTabChangeListener;
  private View currentSelectedView;
	
  public Tab(Context context) {
	super(context);	
  }

  public Tab(Context context, AttributeSet attrs) {
	super(context, attrs);	
  }
  
  public TabAdapter getTabAdapter(){
    return adapter;	  
  }
  
  public void setTabAdapter(TabAdapter adapter){
    setTabAdapter(adapter, 0);	  
  }
  
  public void setOnTabChangedListener(OnTabChangeListener onTabChangeListener){
    this.onTabChangeListener = onTabChangeListener;	  
  }
  
  public void setTabAdapter(TabAdapter adapter, int selected){	
    this.adapter = adapter;
    removeAllViews();
    currentSelectedView = null;
    View selectView = null;
    for(int i = 0;i < adapter.getCount();i++){      
      View view = adapter.getView(this, i);
      if(i == selected)
        selectView = view;
      view.setOnClickListener(onViewClickListener);
      addView(view);
    }
    if(selectView != null)
      onViewClickListener.onClick(selectView);
  }
  
  public void unSelectAll(){
	if(adapter == null)
	  return;
	currentSelectedView = null;
    for(int i = 0;i < getChildCount();i++){
      adapter.onViewUnSelected(getChildAt(i), i);    	
    }	  
  }
  
  
  public void setCurrentTab(int tab){
    setCurrentTab(tab, false);
  }
  
  public void setCurrentTab(int tab, boolean forceUpdate){
    View view = getChildAt(tab);
    setCurrentTab(view, forceUpdate);
  }
  
  public int getCurrentTab(){
	if(currentSelectedView == null)
	  return -1;
    return indexOfChild(currentSelectedView);	  
  }
  
  private void setCurrentTab(View v, boolean forceUpdate){    
	if(forceUpdate){
	  if(currentSelectedView != null){
	    adapter.onViewUnSelected(currentSelectedView, indexOfChild(currentSelectedView));	  
	  }
	  currentSelectedView = null;
	}
    if(currentSelectedView != null && currentSelectedView.equals(v))
      return;
    adapter.onViewSelected(v, indexOfChild(v));      
    if(currentSelectedView != null){
      adapter.onViewUnSelected(currentSelectedView, indexOfChild(currentSelectedView));	  
    }
    currentSelectedView = v;
		  
    if(onTabChangeListener != null){
	  onTabChangeListener.onTabChanged(indexOfChild(v));	  
    }	  
  }
  
  
  class OnViewClickListener implements OnClickListener{

	@Override
	public void onClick(View v) {
	  setCurrentTab(v, false);
	}
	  
  }
  
  public interface OnTabChangeListener{
    public void onTabChanged(int selectedTab);	  
  }
  

}
