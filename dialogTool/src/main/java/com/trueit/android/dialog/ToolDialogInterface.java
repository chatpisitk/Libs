package com.trueit.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Looper;

import com.rokejits.android.tool.os.UIHandler;
import com.rokejits.android.tool.os.UIHandler.UIHandlerListener;



public abstract class ToolDialogInterface implements UIHandlerListener { 
  private static final int SHOW_DIALOG 		= 0;
  private static final int DISMISS_DIALOG 	= 1;
  private UIHandler uiHandler;
  private Dialog dialog;
  protected Context context;
  private OnDismissListener onDismissListener;
  private OnCancelListener onCancelListener;
  private boolean isCancelable, isCanceledOnTouchOutside;
  protected static int dialogChoiceBg;
  protected static int bgResId; 
  protected static int textColor = Color.WHITE; 
  
  public ToolDialogInterface(Context context){
	this.context = context;
    uiHandler = new UIHandler(Looper.getMainLooper());
    uiHandler.setUIHandlerListener(this);
  }
  
  public static final void setResource(int dialogChoiceBg, int bgResId, int textColor){ 
    ToolDialogInterface.dialogChoiceBg = dialogChoiceBg;
    ToolDialogInterface.bgResId = bgResId;
    ToolDialogInterface.textColor = textColor;
  }
  
  protected boolean isSetResource(){
    return dialogChoiceBg > 0 && bgResId > 0;	  
  }
  
  protected boolean isCanceledOnTouchOutside(){
    return isCanceledOnTouchOutside;	  
  }
  
  public void setCanceledOnTouchOutside(boolean flag){
	isCanceledOnTouchOutside = flag;
    if(dialog != null)
      dialog.setCanceledOnTouchOutside(flag);
  }
  
  public void setCancelable(boolean flag){
    isCancelable = flag;
    if(dialog != null)
      dialog.setCancelable(flag);
  }
  
  public boolean isCancelable(){ 
    return isCancelable;	  
  } 
 
  public OnDismissListener getOnDismissListener(){
    return onDismissListener;	  
  }
  
  public void setOnDismissListener(OnDismissListener onDismissListener) {
	this.onDismissListener = onDismissListener;
  }
  
  public OnCancelListener getOnCancelListener() {
	return onCancelListener;
  }
  
  public void setOnCancelListener(OnCancelListener onCancelListener) {
	this.onCancelListener = onCancelListener;
  }
  
  public void show(){
    uiHandler.sendMessage(SHOW_DIALOG);	  
  }
  
  public void dismiss(){
    uiHandler.sendMessage(DISMISS_DIALOG);	  
  }

  protected abstract Dialog createDialog();
  
  protected void onShowDialog(){}
  protected void onDismissDialog(){}
  
  @Override
  public final void onUpdateUI(int source) {
    switch(source){
      case SHOW_DIALOG:
        if(dialog == null){
          dialog = createDialog();
          dialog.setOnDismissListener(onDismissListener2);  
          dialog.setOnCancelListener(onCancelListener2);

        }
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        if(!dialog.isShowing()){
          dialog.show();	
          onShowDialog();
        }
      break;
      case DISMISS_DIALOG:
        if(dialog != null && dialog.isShowing()){
          dialog.dismiss();
          onDismissDialog();
        }	  
      break;
    }	
	
  }
  

  private OnDismissListener onDismissListener2 = new OnDismissListener() {	
	@Override
	public void onDismiss(DialogInterface dialog) {
	  if(onDismissListener != null)	
	    onDismissListener.onDismiss(dialog);	
	}
  };
  
  private OnCancelListener onCancelListener2 = new OnCancelListener() {
	
	@Override
	public void onCancel(DialogInterface dialog) {
	  if(onCancelListener != null)
	    onCancelListener.onCancel(dialog);
	}
  };

   
  
}
