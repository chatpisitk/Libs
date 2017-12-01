package com.rokejits.android.tool.selector;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.trueit.android.dialog.R;
import com.trueit.android.dialog.ToolDialogInterface;

public class SelectorDialog extends ToolDialogInterface{
  private Dialog alertDialog;
  private SelectorDialogAdapter adapter;
  
  public SelectorDialog(Context context){
	super(context);
    alertDialog = new Dialog(context, android.R.style.Theme_Dialog);
    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    alertDialog.setContentView(R.layout.selector_layout);
  }
  
  public SelectorDialogAdapter getSelectorDialogAdapter(){
    return adapter;	  
  }
  
  public void setSelectorDialogAdapter(SelectorDialogAdapter adapter){
	this.adapter = adapter;
	adapter.setSelectorDialog(this);
    LinearLayout titleLayout = (LinearLayout) alertDialog.findViewById(R.id.selector_layout_linearlayout_title);	  
    LinearLayout bodyLayout = (LinearLayout) alertDialog.findViewById(R.id.selector_layout_linearlayout_body);
    
    titleLayout.removeAllViews();
    bodyLayout.removeAllViews();
    
    View getTitleView = adapter.getTitleView(titleLayout);
    View getBodyView = adapter.getBodyView(bodyLayout);
    
    if(getTitleView.getParent() != null){
      ((ViewGroup)getTitleView.getParent()).removeView(getTitleView);	
    }
    
    if(getBodyView.getParent() != null){
      ((ViewGroup)getBodyView.getParent()).removeView(getBodyView);	
    }
    
    titleLayout.addView(getTitleView);
    bodyLayout.addView(getBodyView);    
  }  
 
  @Override
  protected Dialog createDialog() {
	// TODO Auto-generated method stub
	return alertDialog;
  }
	
}
