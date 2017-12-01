package com.rokejits.android.tool.connection2.internet.httpurlconnection;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

import android.content.Context;

import com.rokejits.android.tool.utils.HttpUtils;

public class HttpUrlPostConnection extends HttpURLConnection{
  private String postData;
  private Hashtable<String, String> param;
  
  public HttpUrlPostConnection(Context context, String url) {
	super(context, url);	
	
  }
  
  public HttpUrlPostConnection(Context context, String url, Hashtable<String, String> param) {
	super(context, url);
	this.param = param;
	if(param != null){
	  setPostEntity(HttpUtils.getParam(param, true, false));	
	}	
  }
  
  public Hashtable<String, String> getParams(){
    return param;	  
  }
  
  public void setPostEntity(String postData){
    this.postData = postData;	  
  }
  
  public String getPostEntity(){
    return postData;	  
  }
  
  @Override
  protected String getRequestMethod() {	
	return "POST";
  }
  @Override
  protected void doHttpConnect(java.net.HttpURLConnection connection) throws IOException {
	connection.setDoOutput(true);
    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());    
	writer.write(postData);
    writer.flush();	
  }

  @Override
  protected HttpURLConnection doDeepCopy() {
	return new HttpUrlPostConnection(getContext(), getUrl(), param);
  }

}
