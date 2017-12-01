package com.rokejits.android.tool.connection2.internet.httpurlconnection;

import java.io.IOException;

import android.content.Context;

public class HttpURLGetConnection extends HttpURLConnection{

  public HttpURLGetConnection(Context context, String url) {
	super(context, url);	
  }

  @Override
  protected String getRequestMethod() {	
	return "GET";
  }

  @Override
  protected void doHttpConnect(java.net.HttpURLConnection connection) throws IOException {
	connection.connect();	
  }

  @Override
  protected HttpURLConnection doDeepCopy() {
	return new HttpURLGetConnection(getContext(), getUrl());
  }

  
}
