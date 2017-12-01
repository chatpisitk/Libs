package com.rokejits.android.tool.connection2.internet.httpclient;

import org.apache.http.HttpResponse;

import com.rokejits.android.tool.connection2.IConnectionDescriptor;
import com.rokejits.android.tool.connection2.internet.InternetConnection;

public class HttpClientRestfulConnectionDescriptor extends HttpClientConnectionDescriptor{
 
  public HttpClientRestfulConnectionDescriptor() {}
    
  public HttpClientRestfulConnectionDescriptor(HttpResponse httpResponse) {
    setHttpResponse(httpResponse);	
  }
  
  @Override
  protected void onSetHttpResponse(HttpResponse httpResponse) {
	int responseCode = httpResponse.getStatusLine().getStatusCode(); 
	setResponseCode(responseCode);
    try {
      setLength(httpResponse.getEntity().getContentLength());
      setInputStream(httpResponse.getEntity().getContent());
    } catch (Exception e) {
      e.printStackTrace();
      setError(InternetConnection.INTERNET_ERROR, e.toString());
    }
	 	   
  }
  
  @Override
  public HttpClientRestfulConnectionDescriptor deepCopy() {
	return new HttpClientRestfulConnectionDescriptor(getHttpResponse());
  }
}
