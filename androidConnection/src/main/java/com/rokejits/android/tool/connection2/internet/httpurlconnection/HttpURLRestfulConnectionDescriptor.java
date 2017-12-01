package com.rokejits.android.tool.connection2.internet.httpurlconnection;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.connection2.internet.InternetConnection;

public class HttpURLRestfulConnectionDescriptor extends HttpURLConnectionDescriptor{
	
  protected void onSetHttpURLConnection(java.net.HttpURLConnection httpURLConnection){
   try {
     int resCode = httpURLConnection.getResponseCode();
     Log.e("HttpUrlConnection responseFrom = " + getMyHttpUrlConnection().getUrl());
     Log.e("HttpUrlConnection resCode = " + resCode);
     
     setResponseCode(resCode);
     setLength(httpURLConnection.getContentLength());
     setInputStream(httpURLConnection.getInputStream());	 
     		
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
}
