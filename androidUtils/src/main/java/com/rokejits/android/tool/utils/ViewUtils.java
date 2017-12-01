package com.rokejits.android.tool.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewUtils {


  public static final void ratio(View view, float ratio){
    Ratio r = new Ratio(view, ratio);	
    view.getViewTreeObserver().addOnGlobalLayoutListener(r);
  }	
	
  public static ViewGroup.LayoutParams getLayoutParams(ViewGroup viewGroup, int width, int height){   
    if(viewGroup instanceof AbsListView){
      return new AbsListView.LayoutParams(width, height);	
    }else if(viewGroup instanceof Gallery){
      return new Gallery.LayoutParams(width, height);	
    }else if(viewGroup instanceof WindowManager){
      return new WindowManager.LayoutParams(width, height);	
    }else if(viewGroup instanceof FrameLayout){
      return new FrameLayout.LayoutParams(width, height);	
    }else if(viewGroup instanceof LinearLayout){
      return new LinearLayout.LayoutParams(width, height);	
    }else if(viewGroup instanceof RadioGroup){
      return new RadioGroup.LayoutParams(width, height);	
    }else if(viewGroup instanceof RelativeLayout){
      return new RelativeLayout.LayoutParams(width, height);	
    }else if(viewGroup instanceof TableLayout){
      return new TableLayout.LayoutParams(width, height);	
    }else if(viewGroup instanceof TableRow){
      return new TableRow.LayoutParams(width, height);	
    }    
    return new ViewGroup.LayoutParams(width, height);	  
  }	
	
	
  public static int getDrawableIdentifier(Context context, String resName){
    return getIdentifier(context, resName, "drawable");  
  }
  
  public static int getStringIdentifier(Context context, String resName){
    return getIdentifier(context, resName, "string");  
  }
  
  public static int getIdentifier(Context context, String resName, String resType){
    return context.getResources().getIdentifier(resName, resType, context.getPackageName());	  
  }
  
  public static String getTextFromEditText(View root, int resId){
    return ((EditText)root.findViewById(resId)).getText().toString(); 	  
  }
	  
  public static void setTextToEditText(View root,int resId, String text){
    ((EditText)root.findViewById(resId)).setText(text);  	  
  }
  
  public static void setTextToTextView(View root,int resId, String text){
    ((TextView)root.findViewById(resId)).setText(text);  	  
  }
  
  public static void setTextToTextView(Activity root,int resId, String text){
    ((TextView)root.findViewById(resId)).setText(text);  	  
  }
	  
  public static boolean getBooleanFromCheckBox(View root,int resId){
    return ((CheckBox)root.findViewById(resId)).isChecked();    		
  }
  
  public static String getTextFromEditText(Activity root, int resId){
    return ((EditText)root.findViewById(resId)).getText().toString(); 	  
  }
		  
  public static void setTextToEditText(Activity root,int resId, String text){
    ((EditText)root.findViewById(resId)).setText(text);  	  
  }
	  
  public static boolean getBooleanFromCheckBox(Activity root,int resId){
    return ((CheckBox)root.findViewById(resId)).isChecked();    		
  }
  
  public static void setBooleanToCheckBox(Activity activity, int resId, boolean check){
    ((CheckBox)activity.findViewById(resId)).setChecked(check);	  
  }
  
  public static void setBooleanToCheckBox(View view, int resId, boolean check){
    ((CheckBox)view.findViewById(resId)).setChecked(check);	  
  }
  
  public static void setViewGone(View parent, int viewId){
    setViewGone(parent.findViewById(viewId));	  
  }
  
  public static void setViewVisible(View parent, int viewId){
    setViewVisible(parent.findViewById(viewId));	  
  }
  
  public static void setViewGone(View view){
    view.setVisibility(View.GONE);	  
  }
  
  public static void setViewVisible(View view){
    view.setVisibility(View.VISIBLE);	   
  }
  
  
  
}
