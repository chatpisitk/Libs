package com.trueit.android.dialog;

import com.rokejits.android.tool.os.UIHandler;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.Window;
import android.widget.TextView;


public class ToolProgressDialog extends ToolDialogInterface {
  //private OnToolCancelListener cancelListener; 
  public static final int DIALOG_PROGRESS					= 0x14445;	
  public static final int DIALOG_DOWNLOAD_PROGRESS			= DIALOG_PROGRESS + 1;
  
  
  private String title, message;
  private boolean indeterminate;  
  private Dialog pDialog;
  private int max, progress;
  private int dialogType = DIALOG_PROGRESS;
  private UIHandler uiHandler;
  
  public static ToolProgressDialog show(Context context){
    return show(context, "Please wait", "Loading", true, false, null);   	  
  }
  
  public static ToolProgressDialog show(Context context, OnCancelListener listener){	
    return show(context, "Please wait", "Loading", true, true, listener);   	  
  } 
  
  public static ToolProgressDialog show(Context context, String title, String message){
    return show(context, title, message, false);   	  
  }
  
  public static ToolProgressDialog show(Context context, String title, String message, boolean indeterminate){
    return show(context, title, message, indeterminate, false);	  
  }
  
  public static ToolProgressDialog show(Context context, String title, String message, boolean indeterminate, boolean cancelable){
    return show(context, title, message, indeterminate, cancelable, null);	  
  }

  public static ToolProgressDialog show(Context context, String title, String message, boolean indeterminate, boolean cancelable, DialogInterface.OnCancelListener cancelListener){
    ToolProgressDialog tDialog = new ToolProgressDialog(context);
    tDialog.setTitle(title);
    tDialog.setMessage(message);
    tDialog.setIndeTerminate(indeterminate);
    tDialog.setCancelable(cancelable);
    tDialog.setOnCancelListener(cancelListener);
    tDialog.show();
    
    
    
    return tDialog;    
  } 
  
  private ToolProgressDialog(Context context){
	super(context);   
	uiHandler = new UIHandler();
  } 
  
  public void setDialogType(int type){
    this.dialogType = type;	  
  }
  
  public void setTitle(final String title){
    this.title = title;	  
    if(pDialog != null){
      uiHandler.post(new Runnable() {		
		@Override
		public void run() {
		  pDialog.setTitle(title);			
		}
	  });      
    }
  }
  
  public void setMessage(final String message){
    this.message = message;	  
    if(pDialog != null && pDialog instanceof ProgressDialog){
      uiHandler.post(new Runnable() {		
    	@Override
    	public void run() {
    	  ((ProgressDialog)pDialog).setMessage(message);			
    	}
      });	
    }
      	
      
  }
  
  public void setIndeTerminate(final boolean terminate){
    this.indeterminate = terminate;
    if(pDialog != null && pDialog instanceof ProgressDialog){
      
      uiHandler.post(new Runnable() {		
      	@Override
      	public void run() {
      	  ((ProgressDialog)pDialog).setIndeterminate(terminate);			
      	}
      });
    }
  } 
  
  public void setMax(final int max){
    this.max = max;
    if(pDialog != null && pDialog instanceof ProgressDialog){
      uiHandler.post(new Runnable() {		
       	@Override
       	public void run() {
       	  ((ProgressDialog)pDialog).setMax(max);			
       	}
      });	
    }
  }
  
  public void setProgress(final int value){
    this.progress = value;
    if(pDialog != null && pDialog instanceof ProgressDialog){
      uiHandler.post(new Runnable() {		
       	@Override
       	public void run() {
       	  ((ProgressDialog)pDialog).setProgress(value);			
       	}
      });	
    }
  }
  
  @Override
  public void setOnCancelListener(OnCancelListener onCancelListener){
    super.setOnCancelListener(onCancelListener);
    if(pDialog != null){
      pDialog.setOnCancelListener(onCancelListener);	
    }
  }
  
  @Override
  public void setOnDismissListener(OnDismissListener onDismissListener) {
    super.setOnDismissListener(onDismissListener);
    if(pDialog != null){
      pDialog.setOnDismissListener(onDismissListener);	
    }
  }

  @Override
  protected Dialog createDialog() {
    if(pDialog == null){
      switch(dialogType){
        case DIALOG_PROGRESS:
          if(!isSetResource())
            initBasicProgressDialog();
          else
          	initCustomProgressDialog();	
        break;
        case DIALOG_DOWNLOAD_PROGRESS:
          ProgressDialog pDialog = new ProgressDialog(context);
                    
          pDialog.setTitle(title);
          pDialog.setMessage(message);
          pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
          pDialog.setIndeterminate(indeterminate);
          pDialog.setMax(max);
          pDialog.setProgress(progress);
          pDialog.setCancelable(isCancelable());	
          pDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside());
          pDialog.setOnCancelListener(getOnCancelListener());
          pDialog.setOnDismissListener(getOnDismissListener());
          
          this.pDialog = pDialog;
          
        break;
      }	
      
    }  	  
	return pDialog;
  } 
  
  private void initBasicProgressDialog(){
    if(getOnCancelListener() != null)
      pDialog = ProgressDialog.show(context, title, message, indeterminate, isCancelable(), getOnCancelListener());
    else
      pDialog = ProgressDialog.show(context, title, message, indeterminate, isCancelable());  
  }
  
  private void initCustomProgressDialog(){
	pDialog = new Dialog(context);	
    pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    pDialog.getWindow().getDecorView().setBackgroundResource(bgResId);
	      
	pDialog.setContentView(R.layout.true_progress_dialog_layout);
	
	TextView titleView = (TextView) pDialog.findViewById(R.id.true_progress_dialog_layout_textview_title);
	TextView messageView = (TextView) pDialog.findViewById(R.id.true_progress_dialog_layout_textview_message);
	
	titleView.setText(title);
	messageView.setText(message);
	
	titleView.setTextColor(textColor);
	messageView.setTextColor(textColor);

	if(getOnCancelListener() != null)
	  pDialog.setOnCancelListener(getOnCancelListener());
	
  }
  
  
  
}
