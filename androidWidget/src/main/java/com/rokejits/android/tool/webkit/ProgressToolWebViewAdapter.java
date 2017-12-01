package com.rokejits.android.tool.webkit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ProgressToolWebViewAdapter extends ToolWebViewAdapter{
  private int progress = 0;
  private int color = Color.GREEN;
  private WebChromeClient pWebChromeClient;	
  private WebViewClient pWebViewClient;  
  private boolean enableProgressBar = false;
  
  
  
  public ProgressToolWebViewAdapter() {	
    this(null, null);	
  }
  
  public ProgressToolWebViewAdapter(WebChromeClient pWebChromeClient, WebViewClient pWebViewClient) {
    this.pWebChromeClient = pWebChromeClient;
    this.pWebViewClient = pWebViewClient;
    enableProgressBar(true);	  
  }
  
  public final void enableProgressBar(boolean enable){
    enableProgressBar = enable;	   
  }
  
  public void setProgressBarColor(int color){
    this.color = color;
    if(progressPaint != null){
      progressPaint.setColor(color);     
    }
  }
  
  public final boolean isEnableProgressBar(){
    return enableProgressBar;	  
  } 

  @Override
  protected WebChromeClient getWebChromeClient() {
	if(pWebChromeClient == null)
	  pWebChromeClient = new ProgressWebChromeClient();
	return pWebChromeClient;
  }

  @Override
  protected WebViewClient getWebViewClient() {
	if(pWebViewClient == null)
	  pWebViewClient = new WebViewClient();
	return pWebViewClient;
  }
  
  
  private Paint progressPaint;
  
  @Override
  public void onDispatchDraw(ToolWebView view, Canvas canvas) {
    if(progressPaint == null){
      progressPaint = new Paint();
	  progressPaint.setColor(color);
	  progressPaint.setStyle(Style.FILL);
	  progressPaint.setAntiAlias(true);		    
	}
    float right = (float)(progress * view.getWidth()) / 100 ;
    canvas.drawRect(0, 0, right, 5, progressPaint);
	if(progress > 0){
      int offset = 6;
	  canvas.drawOval(new RectF(right - offset, -offset, right + offset , offset), progressPaint);
	}
  }



public class ProgressWebChromeClient extends WebChromeClient{
    @Override
	public void onProgressChanged(WebView view, int newProgress) {		
	  progress = newProgress;	
	  if(progress == 100)
	    progress = 0;
	  view.invalidate();
	}	  
  }
  
 
 
}
