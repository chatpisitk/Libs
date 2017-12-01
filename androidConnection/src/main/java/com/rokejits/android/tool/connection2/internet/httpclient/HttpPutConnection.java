package com.rokejits.android.tool.connection2.internet.httpclient;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.rokejits.android.tool.utils.HttpUtils;

public class HttpPutConnection extends HttpClientConnection{
  private Hashtable<String, String> param;
  public HttpPutConnection(Context context, String url, Hashtable<String, String> param) {
	super(context, url);
	this.param = param;
  }

  @Override
  protected HttpUriRequest getRequest(String url) {
	HttpPut httpPut = new HttpPut(url);
	try {
	  httpPut.setEntity(new StringEntity(HttpUtils.getParam(param, true)));
	} catch (UnsupportedEncodingException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	}
	return httpPut;
  }

  @Override
  protected HttpPutConnection doDeepCopy() {
	return new HttpPutConnection(getContext(), getUrl(), param);
  }
  
}
