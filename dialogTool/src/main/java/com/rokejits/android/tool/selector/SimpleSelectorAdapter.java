package com.rokejits.android.tool.selector;

import com.rokejits.android.tool.utils.UiUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SimpleSelectorAdapter extends ListViewSelectorAdapter<String>{

  public SimpleSelectorAdapter(Context context, String[] datas) {
	super(context, datas);	
  }

  public SimpleSelectorAdapter(Context context, String[] datas, int index) {
	super(context, datas, index);	
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    TextView textView = (TextView) convertView;
	if(textView == null){
      Context context = getContext();				  
	  textView = new TextView(context);
	  textView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, ListView.LayoutParams.WRAP_CONTENT));
	  textView.setGravity(Gravity.CENTER);
	  textView.setBackgroundColor(Color.WHITE);
	  textView.setTextColor(Color.BLACK);
	  textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
	  textView.setTypeface(null, Typeface.BOLD);
	  textView.setPadding(0, 
		  	              (int)UiUtils.convertDpiToPixel(context, 10), 
				          0, 
	  			          (int)UiUtils.convertDpiToPixel(context, 10));		  	
	}
	textView.setText(getData(position));
	return textView;
  }

  @Override
  public long getItemId(int position) {
	
	return position;
  }

  @Override
  public Object getItem(int position) {
	
	return null;
  }
  
  
  

}
