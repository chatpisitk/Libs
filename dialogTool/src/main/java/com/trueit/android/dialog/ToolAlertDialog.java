package com.trueit.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ToolAlertDialog extends ToolDialogInterface {

  public static final int D_OK 			= 0;
  public static final int D_YES_NO 		= 1;
  public static final int D_OK_CANCEL   = 2;
  
//  public static final int OK 		= 0;
//  public static final int YES 		= 1;
//  public static final int NO 		= 2;
	
  
  private static final String[][] CHOICES = {
    {"OK"},	  
    {"Yes", "No"},
    {"OK", "Cancel"}
  };
  
  private String message;
  private String[] dialogChoices;
  private Button positiveBtn, neutralBtn, negativeBtn;
  private DialogInterface.OnClickListener listener;
  
  private Dialog aDialog;
  
  public static ToolAlertDialog alert(Context context, int resId){	  
    return alert(context, context.getString(resId));  	  
  }	
		
  public static ToolAlertDialog alert(Context context, String message){	  
    return show(context, message, D_OK, null);  	  
  }
		
  public static ToolAlertDialog ask(Context context, int resId, DialogInterface.OnClickListener listener){
    return ask(context, context.getString(resId), listener);	  
  }
	  
  public static ToolAlertDialog ask(Context context, String message, DialogInterface.OnClickListener listener){
    return show(context, message, D_YES_NO, listener);	  
  }
	  
  public static ToolAlertDialog askOk(Context context, int resId, DialogInterface.OnClickListener listener){
    return askOk(context, context.getString(resId), listener);	  
  }
	  
  public static ToolAlertDialog askOk(Context context, String message, DialogInterface.OnClickListener listener){
    return show(context, message, D_OK_CANCEL, listener);	  
  }
  
  public static ToolAlertDialog show(Context context, int resId, int dialogType, DialogInterface.OnClickListener listener){
    return show(context, context.getString(resId), dialogType, listener);   
  }
  
  public static ToolAlertDialog show(Context context, String message, int dialogType, DialogInterface.OnClickListener listener){
    return show(context, message, CHOICES[dialogType], listener);   
  }
  
  public static ToolAlertDialog show(Context context, int resId, String[] dialogChoices, DialogInterface.OnClickListener listener){
    return show(context, context.getString(resId), dialogChoices, listener);
  } 
  
  public static ToolAlertDialog show(Context context, String message, String[] dialogChoices, DialogInterface.OnClickListener listener){
    ToolAlertDialog dialog = new ToolAlertDialog(context, message, dialogChoices, listener);
	dialog.show(); 
	return dialog;
  }  
  protected ToolAlertDialog(Context context, String message, String[] dialogChoices, DialogInterface.OnClickListener listener) {
    super(context);	
    this.message = message;
    this.dialogChoices = dialogChoices;
    this.listener = listener;            
  }
  
  @Override
  protected Dialog createDialog() {
	if(aDialog == null){
	  if(!isSetResource())
	    initBasicDialog();
	  else
	    initCustomDialog();	
	  
	}
	return aDialog;
  }
  
  private void initCustomDialog(){

    aDialog = new Dialog(context);	
    aDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    aDialog.getWindow().getDecorView().setBackgroundResource(bgResId);
      
    aDialog.setContentView(R.layout.true_dialog_layout);
    TextView messageView = (TextView) aDialog.findViewById(R.id.true_dialog_layout_textview_message);
    positiveBtn = (Button) aDialog.findViewById(R.id.true_dialog_layout_button_positive);
    neutralBtn = (Button) aDialog.findViewById(R.id.true_dialog_layout_button_neutral);
    negativeBtn = (Button) aDialog.findViewById(R.id.true_dialog_layout_button_negative);
    messageView.setText(message);
    LinearLayout.LayoutParams params = null;
    LinearLayout.LayoutParams params2 = null;
    if(dialogChoices.length == 1){
      params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      params2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }else{
      params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
      params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      
    }
      
    aDialog.findViewById(R.id.true_dialog_layout_linearlayout_btn_body).setLayoutParams(params2);
    positiveBtn.setLayoutParams(params);
    neutralBtn.setLayoutParams(params);
    negativeBtn.setLayoutParams(params);
    
    messageView.setTextColor(textColor);
    positiveBtn.setTextColor(textColor);
    neutralBtn.setTextColor(textColor);
    negativeBtn.setTextColor(textColor);
      
    for(int i = 0;i < dialogChoices.length;i++){
      Button btn = null;
      if(i == 0){
        btn = positiveBtn;	
      }else if(i == 1){
        if(dialogChoices.length == 2){
          btn = negativeBtn;	  
        }else{
          btn = neutralBtn;  
        }  
      }else{
        btn = negativeBtn;	  	
      }
      btn.setLayoutParams(params);
      btn.setText(dialogChoices[i]);
      btn.setBackgroundResource(dialogChoiceBg);
      btn.setVisibility(View.VISIBLE);
      btn.setOnClickListener(btnOnClickListener);
    }
  }
  
  private void initBasicDialog(){ 
    AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);	  
    AlertDialog aDialog = aBuilder.create();
	  
    aDialog.setMessage(message);
	 
    for(int i = 0;i < dialogChoices.length && i < 3;i++){
      if(i == 0)
        aDialog.setButton(dialogChoices[0], listener);	
      if(i == 1)
    	aDialog.setButton2(dialogChoices[1], listener);
	  if(i == 2)
	    aDialog.setButton3(dialogChoices[2], listener);
	}
    this.aDialog = aDialog;
  }
  
  private View.OnClickListener btnOnClickListener = new OnClickListener() {		
	@Override
	public void onClick(View v) {
	  int which = -1;
	  if(v.equals(positiveBtn)){
	    which = Dialog.BUTTON_POSITIVE;  	  
	  }else if(v.equals(neutralBtn)){
	    which = Dialog.BUTTON_NEUTRAL;	  
	  }else{
		which = Dialog.BUTTON_NEGATIVE;  
	  }
	  if(listener != null)
	    listener.onClick(aDialog, which);
	  dismiss();
	}
  };
  
  
 /* private void setOnButtonClickListener(OnButtonClickListener listener){
    this.listener = listener;	  
  }

  @Override
  public final void onClick(DialogInterface dialog, int which) {
    if(listener != null){
      listener.onButtonClicked(which);	
    }  	
    dismiss();
  } */
  
  
  
  /*public Dialog(String message, int dialogType, String[] choice){
     	  
  }*/
  
  
}
