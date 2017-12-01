package com.rokejits.android.tool.webkit;

import android.graphics.Canvas;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ToolWebViewAdapter {
  private ToolWebView webView;
  
  
	
  protected final void setToolWebView(ToolWebView webView){
    this.webView = webView;	
    settingWebView();
  }	
  
  
  
  protected WebChromeClient getWebChromeClient(){
    return null;	  
  }
  
  protected WebViewClient getWebViewClient(){
    return null;	  
  }
  
  protected void onSettingWebView(WebSettings webSetting){}  
  protected void onDispatchDraw(ToolWebView view, Canvas canvas){}
  
  private void settingWebView(){
    WebSettings webSetting = webView.getSettings();
	
    webSetting.setJavaScriptEnabled(true);		
	webSetting.setDefaultTextEncodingName("UTF-8");	

    onSettingWebView(webSetting);
    
	//webSetting.setPluginState(PluginState.ON);
	//webSetting.setGeolocationEnabled(true);
	//webSetting.setDomStorageEnabled(true);
	//webSetting.setDatabaseEnabled(true);
	//webSetting.setAllowFileAccess(true);
    //webSetting.setLoadWithOvesrviewMode(true);
	//webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
  }  
  
  public final void dispatchDraw(Canvas canvas){
	if(webView != null)
	  onDispatchDraw(webView, canvas);	  
  }
  
  
  
}
