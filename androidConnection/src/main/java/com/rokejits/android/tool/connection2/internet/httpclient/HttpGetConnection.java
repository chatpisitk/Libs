package com.rokejits.android.tool.connection2.internet.httpclient;

import java.util.Hashtable;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.content.Context;

import com.rokejits.android.tool.utils.HttpUtils;

public class HttpGetConnection extends HttpClientConnection{

  public HttpGetConnection(Context context, String url) {
	super(context, url);	
  }
  
  public HttpGetConnection(Context context, String url, Hashtable<String, String> param) {
	super(context, url + HttpUtils.getParam(param, false));	
  }

  @Override
  protected HttpUriRequest getRequest(String url) {	
	HttpGet httpGet = new HttpGet(url);
	return httpGet;
  }

  @Override
  protected HttpGetConnection doDeepCopy() {
    return new HttpGetConnection(getContext(), getUrl());	
  }

}
