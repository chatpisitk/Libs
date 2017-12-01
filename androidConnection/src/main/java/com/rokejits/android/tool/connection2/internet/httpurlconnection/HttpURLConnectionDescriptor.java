package com.rokejits.android.tool.connection2.internet.httpurlconnection;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.connection2.IConnectionDescriptor;
import com.rokejits.android.tool.connection2.SetableConnectionDescriptor;
import com.rokejits.android.tool.connection2.internet.InternetConnection;
import com.rokejits.android.tool.connection2.internet.InternetConnectionDescriptor;

public class HttpURLConnectionDescriptor extends InternetConnectionDescriptor{
  private java.net.HttpURLConnection httpURLConnection;
  private HttpURLConnection mHttpURLConnection;
  private int resCode = -1;
	
    	
  protected void setMyHttpUrlConnection(HttpURLConnection httpURLConnection){
    mHttpURLConnection = httpURLConnection;	  
  }
  
  public HttpURLConnection getMyHttpUrlConnection(){
    return mHttpURLConnection;	  
  }
  
  protected final void setHttpURLConnection(java.net.HttpURLConnection httpURLConnection){
    this.httpURLConnection = httpURLConnection;
  }
	
  protected void onSetHttpURLConnection(java.net.HttpURLConnection httpURLConnection){
    try {
      resCode = httpURLConnection.getResponseCode();
      Log.e("HttpUrlConnection responseFrom = " + mHttpURLConnection.getUrl());
      Log.e("HttpUrlConnection resCode = " + resCode);
      if(resCode == java.net.HttpURLConnection.HTTP_OK ||
        resCode == java.net.HttpURLConnection.HTTP_CREATED){ 
        setLength(httpURLConnection.getContentLength());
  	    setInputStream(httpURLConnection.getInputStream());	 
  	    return;
	  }
	  setError(InternetConnection.INTERNET_RESPONSE_ERROR, "Listening - Other HTTP_RESPONSE_CODE " + resCode);  
		
	} catch (MalformedURLException e){
	  e.printStackTrace();
      Log.e("HttpURLConnection error MalformedURLException = "+e.toString());
	  setError(InternetConnection.MULFORMED_URL_ERROR, e.toString());	
	} catch (SocketTimeoutException e){
      e.printStackTrace();
      Log.e("HttpURLConnection error SocketTimeoutException = "+e.toString());	
  	  setError(InternetConnection.INTERNET_TIMEOUT_ERROR, e.toString());	
	} catch(Exception e){
      e.printStackTrace();
      Log.e("HttpURLConnection error Exception = "+e.toString());	
      setError(InternetConnection.INTERNET_ERROR, e.toString());	
	}	  	  
  }
  
	
  protected void setResponseCode(int responseCode){
    this.resCode = responseCode;	  
  }
  
  public int getResponseCode(){
    return resCode;	
  }
	
  public java.net.HttpURLConnection getHttpUrlConnection(){
    return httpURLConnection;	
  }

  @Override
  public Map<String, List<String>> getAllRequestHeader() {
	return httpURLConnection.getRequestProperties();
  }

  @Override
  public List<String> getRequestHeaders(String field) {
	return Arrays.asList(httpURLConnection.getRequestProperty(field));
  }

  @Override
  public Map<String, List<String>> getAllResponseHeader() {
    return httpURLConnection.getHeaderFields();
  }

  @Override
  public List<String> getResponseHeaders(String field) {
	return Arrays.asList(httpURLConnection.getHeaderField(field));
  }

  @Override
  public HttpURLConnectionDescriptor deepCopy() {
	return new HttpURLConnectionDescriptor();
  } 
	  
}
