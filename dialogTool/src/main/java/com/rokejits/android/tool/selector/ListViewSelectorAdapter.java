package com.rokejits.android.tool.selector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import com.rokejits.android.tool.utils.UiUtils;


public abstract class ListViewSelectorAdapter<T> extends ArraySelectorAdapter<T>{

  private String title;	
  private TextView titleView;
  private ListView listView;
  private int currentIndex;
  private int styleResId;
  
  
  public ListViewSelectorAdapter(Context context, T[] datas){
	this(context, datas, 0);    
  }	
  
  public ListViewSelectorAdapter(Context context, T[] datas, int currentIndex){
	super(context, datas);    
	this.currentIndex = currentIndex;
  }	
  
  public abstract View getView(int position, View convertView, ViewGroup parent);
  public abstract long getItemId(int position);
  public abstract Object getItem(int position); 
  
  public void setTitle(String title){
	this.title = title;
    if(titleView != null){      	
      titleView.setText(title);      
      if(title != null && title.length() > 0){
    	titleView.setVisibility(View.VISIBLE);        
      }else{
    	titleView.setVisibility(View.GONE);        	  
      }
      
    }	  
  }  
  
  public String getTitle(){
    return title;	  
  }
  
  @Override
  public View getTitleView(ViewGroup parent) {
	if(titleView == null){
	  Context context = getContext();
	  titleView = new TextView(context);
	  titleView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
	  titleView.setGravity(Gravity.CENTER);
	  titleView.setBackgroundColor(Color.BLACK);
	  titleView.setTextColor(Color.WHITE);
	  titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
	  titleView.setTypeface(null, Typeface.BOLD);
	  titleView.setPadding(0, 
                          (int)UiUtils.convertDpiToPixel(context, 10), 
                           0, 
                          (int)UiUtils.convertDpiToPixel(context, 10));
	  setTitle(title);
	}
	return titleView;
  }

  @Override
  public View getBodyView(ViewGroup parent) {
	if(listView == null){
	  listView = new ListView(getContext());
	  listView.setLayoutParams(new LinearLayout.LayoutParams(ListView.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
	  listView.setAdapter(listViewAdapter);	  
	  listView.setSelection(currentIndex);
	  listView.setOnItemClickListener(listViewOnItemClickListener);  
	}
	return listView;
  }
  
  public void notifyDataSetChanged(){
    listViewAdapter.notifyDataSetChanged();	  
  }  
  
  private OnItemClickListener listViewOnItemClickListener = new OnItemClickListener() {
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {     
      itemClicked(view, position, id);      
      dismissDialog();    		     		
	}    	  
  };
  
	  
	  
  private BaseAdapter listViewAdapter = new BaseAdapter() {
	  
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	  return ListViewSelectorAdapter.this.getView(position, convertView, parent);
	}
	
	@Override
	public long getItemId(int position) {		
	  return ListViewSelectorAdapter.this.getItemId(position);
	}
	
	@Override
	public Object getItem(int position) {
	  return ListViewSelectorAdapter.this.getItem(position);
	}
	
	@Override
	public int getCount() {		
	  return getDataCount();
	}
  }; 

  public interface OnListViewSelectorAdapterItemSelectListener{
    public void onListViewSelectorItemSelected(ListViewSelectorAdapter<?> listViewAdapter, int position);	  
  }
  	
}
