package com.rokejits.android.tool.ui.dialog.picturegetter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.rokejits.android.tool.selector.SimpleSelectorAdapter;

public class PictureGetterSelectorAdapter extends SimpleSelectorAdapter{  
	  
  public PictureGetterSelectorAdapter(Context context) {
    super(context, new String[]{"Capture from camera", "Pick from Gallery"});  	
    setTitle("Select");
  }
    
  protected View onGetTitleView(View titleView, ViewGroup parent){
    return titleView; 	
  }
    
  protected View onGetView(View view, int position, View convertView, ViewGroup parent ){
    return view;	
  }
    
  @Override
  public final View getTitleView(ViewGroup parent) {
    View titleView = super.getTitleView(parent);
    titleView = onGetTitleView(titleView, parent);
    return titleView;
  }
    
  @Override
  public final View getView(int position, View convertView, ViewGroup parent) {
    View view = super.getView(position, convertView, parent);
    view = onGetView(view, position, convertView, parent);
    return view;
  }

	
	  
}