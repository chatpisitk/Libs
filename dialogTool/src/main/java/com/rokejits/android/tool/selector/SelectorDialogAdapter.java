package com.rokejits.android.tool.selector;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class SelectorDialogAdapter {
  private SelectorDialog dialog;
  private OnSelectorItemClickListener onSelectorItemClickListener;
  private Context context;
  
  public SelectorDialogAdapter(Context context){
    this.context = context;	  
  }
  
  public Context getContext(){
    return context;	  
  }  
  
  protected void setSelectorDialog(SelectorDialog dialog) {
    this.dialog = dialog;	
  }
  
  protected void dismissDialog(){
    dialog.dismiss();	  
  }
  
  public void setOnSelectorItemClickListener(OnSelectorItemClickListener onSelectorItemClickListener){
    this.onSelectorItemClickListener = onSelectorItemClickListener;	  
  }
  
  protected void itemClicked(View view, int position, long id){
    if(onSelectorItemClickListener != null){
      onSelectorItemClickListener.onSelectorItemClicked(this, view, position, id);	
    }	  
  }
	
  public abstract View getTitleView(ViewGroup parent);
  public abstract View getBodyView(ViewGroup parent);
  
  public interface OnSelectorItemClickListener{
    public void onSelectorItemClicked(SelectorDialogAdapter selectorAdapter, View view, int position, long id);	  
  }
  
  
  	
}
