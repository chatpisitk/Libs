package com.rokejits.android.tool.webkit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ToolWebView extends WebView{

  private ToolWebViewAdapter adapter;	
  
	
  public ToolWebView(Context context) {
	super(context);	
  }	
	
  public ToolWebView(Context context, AttributeSet attrs) {
	super(context, attrs);	
  } 

  public final void setAdapter(ToolWebViewAdapter adapter){	
    this.adapter = adapter;	  
    adapter.setToolWebView(this);
    WebChromeClient webChromeClient = adapter.getWebChromeClient();
    WebViewClient webViewClient = adapter.getWebViewClient();
    
    if(webChromeClient != null)
      super.setWebChromeClient(webChromeClient);    
    
    if(webViewClient != null)
      super.setWebViewClient(webViewClient);
  } 
  
  @Override
  public final void setWebChromeClient(WebChromeClient client) {}

  @Override
  public final void setWebViewClient(WebViewClient client) {}

  
  @Override
  protected void dispatchDraw(Canvas canvas) {  
	super.dispatchDraw(canvas);
	if(adapter != null){
	  adapter.dispatchDraw(canvas);	    
	}
  }

  
  
  
  
  

}
