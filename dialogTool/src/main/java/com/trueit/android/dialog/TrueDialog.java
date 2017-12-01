package com.trueit.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class TrueDialog extends ToolDialogInterface{
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
	
	
  private String[] dialogChoices;
  private int[] dialogChoicesBg;
  private int bgResId;
  private String message;
  private DialogInterface.OnClickListener listener;	
  private Button positiveBtn, neutralBtn, negativeBtn;
  private Dialog dialog;
	
 
  
  public static TrueDialog alert(Context context, String message){	  
    return show(context, message, D_OK, null);  	  
  }
  
  public static TrueDialog alert(Context context, String message, int choiceBgResId, int bgResid){	  
    return show(context, message, D_OK, new int[]{choiceBgResId}, bgResid, null);  	  
  }
  
  public static TrueDialog ask(Context context, String message, DialogInterface.OnClickListener listener){
    return show(context, message, D_YES_NO, listener);	  
  }
  
  public static TrueDialog ask(Context context, String message, int[] dialogChoicesBg,int bgResId, DialogInterface.OnClickListener listener){
    return show(context, message, D_YES_NO, dialogChoicesBg, bgResId, listener);	  
  }
  
  public static TrueDialog askOk(Context context, String message, DialogInterface.OnClickListener listener){
    return show(context, message, D_OK_CANCEL, listener);	  
  }
  
  public static TrueDialog askOk(Context context, String message, int[] dialogChoicesBg,int bgResId, DialogInterface.OnClickListener listener){
    return show(context, message, D_OK_CANCEL, dialogChoicesBg, bgResId, listener);	  
  }
  
  public static TrueDialog show(Context context, String message, int dialogType, DialogInterface.OnClickListener listener){
    return show(context, message, CHOICES[dialogType], listener);   
  }
  
  public static TrueDialog show(Context context, String message, int dialogType, int[] dialogChoicesBg, int bgResId, DialogInterface.OnClickListener listener){
    return show(context, message, CHOICES[dialogType], dialogChoicesBg, bgResId, listener);   
  }
  
  public static TrueDialog show(Context context, String message, String[] dialogChoices, DialogInterface.OnClickListener listener){
	TrueDialog dialog = new TrueDialog(context, message, dialogChoices, null, 0, listener);
	dialog.show(); 
	return dialog;
  } 
  
  public static TrueDialog show(Context context, String message, String[] dialogChoices, int[] dialogChoicesBg, int bgResId, DialogInterface.OnClickListener listener){
	TrueDialog dialog = new TrueDialog(context, message, dialogChoices, dialogChoicesBg, bgResId, listener);
	dialog.show(); 
	return dialog;
  } 
  
  
  private TrueDialog(Context context, String message, 
		            String[] dialogChoices, int[] dialogChoicesBg, 
		            int bgResId, DialogInterface.OnClickListener listener) {
	super(context);
	this.message = message;
	this.dialogChoices = dialogChoices;
	this.dialogChoicesBg = dialogChoicesBg;
	this.bgResId = bgResId;
	this.listener = listener;
	
  }

  @Override
  protected Dialog createDialog() {
    if(bgResId > 0){
      dialog = new Dialog(context);	
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.getWindow().getDecorView().setBackgroundResource(bgResId);
      
      dialog.setContentView(R.layout.true_dialog_layout);
      TextView messageView = (TextView) dialog.findViewById(R.id.true_dialog_layout_textview_message);
      positiveBtn = (Button) dialog.findViewById(R.id.true_dialog_layout_button_positive);
      neutralBtn = (Button) dialog.findViewById(R.id.true_dialog_layout_button_neutral);
      negativeBtn = (Button) dialog.findViewById(R.id.true_dialog_layout_button_negative);
      messageView.setText(message);
      LinearLayout.LayoutParams params = null;
      if(dialogChoices.length == 1){
        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      }else{
    	params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
      }
      
      positiveBtn.setLayoutParams(params);
      neutralBtn.setLayoutParams(params);
      negativeBtn.setLayoutParams(params);
      
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
        btn.setBackgroundResource(dialogChoicesBg[i]);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(btnOnClickListener);
      }
      
    }else{
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
  	  dialog = aDialog;
    }    
    
	return dialog;
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
	    listener.onClick(dialog, which);
	  dismiss();
	}
  };
  
  
}
