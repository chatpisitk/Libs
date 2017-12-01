package com.rokejits.android.tool.connection2.internet.httpclient;

import android.content.Context;

import com.rokejits.android.tool.utils.HttpUtils;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.net.URI;
import java.util.Hashtable;

public class HttpDeleteConnection extends HttpClientConnection{

  protected Hashtable<String, String> param;		
	
  public HttpDeleteConnection(Context context, String url) {
	this(context, url, null);
  }

  public HttpDeleteConnection(Context context, String url, Hashtable<String, String> param) {
	super(context, url);	
	this.param = param;
  }
  
  @Override
  protected HttpUriRequest getRequest(String url) {
    HttpDelete httpDel = new HttpDelete(url);      
    
    if(param != null){
		httpDel.setEntity(new StringEntity(HttpUtils.getParam(param, true), "utf-8"));
	}
	return httpDel;
  }
  
  @Override
  protected HttpDeleteConnection doDeepCopy() {
    return new HttpDeleteConnection(getContext(), getUrl(), param);
  }
  
  class HttpDelete extends HttpEntityEnclosingRequestBase{
	public final static String METHOD_NAME = "DELETE";

	public HttpDelete(){
	  super(); 	
	}
	
	public HttpDelete(final URI uri){
	  super();
	  setURI(uri);
	}
	
	public HttpDelete(final String uri){
	  super();
	  setURI(URI.create(uri));
	}
	
	
	
	@Override
	public String getMethod() {
	  // TODO Auto-generated method stub
	  return METHOD_NAME;
	}
	  
  }

}
