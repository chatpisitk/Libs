package com.rokejits.android.tool.connection2.internet.httpclient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;

import com.rokejits.android.tool.connection2.IConnectionDescriptor;
import com.rokejits.android.tool.connection2.internet.InternetConnection;
import com.rokejits.android.tool.connection2.internet.InternetConnectionDescriptor;

public class HttpClientConnectionDescriptor extends InternetConnectionDescriptor{ 
  private HttpResponse httpResponse;	  
  private HttpUriRequest httpRequest;
  private int responseCode = -1;
	  
  public HttpClientConnectionDescriptor() {}
    
  public HttpClientConnectionDescriptor(HttpResponse httpResponse) {
    setHttpResponse(httpResponse);	
  }
  
  public int getResponseCode(){
    return responseCode;	  
  }
  
  protected void setResponseCode(int responseCode){
    this.responseCode = responseCode;	  
  }
   
  public void setHttpUriRequest(HttpUriRequest httpRequest){
    this.httpRequest = httpRequest;	  
  }
  
  public HttpUriRequest getHttpRequest(){
    return httpRequest;	  
  }
  
  public HttpResponse getHttpResponse(){
    return httpResponse;	
  }
  
  @Override
  public Map<String, List<String>> getAllRequestHeader() {
    return getAllHeader(httpRequest);	
  }

  @Override
  public List<String> getRequestHeaders(String field) {  	
  	return getHeaders(httpRequest, field);
  }

  @Override
  public Map<String, List<String>> getAllResponseHeader() {
    return getAllHeader(httpResponse);
  }

  @Override
  public List<String> getResponseHeaders(String field) {
  	return getHeaders(httpResponse, field);
  } 
  
  private Map<String, List<String>> getAllHeader(HttpMessage httpMessage){
    Hashtable<String, List<String>> result = null;
	if(httpMessage != null){  	 
  	  Header[] headers = httpMessage.getAllHeaders();
  	  if(headers != null && headers.length > 0){
  	    result = new Hashtable<String, List<String>>();
  	    for(int i = 0;i < headers.length;i++){
  	      Header header = headers[i];
  	      ArrayList<String> list = (ArrayList<String>) result.get(header.getName());
  	      if(list == null){
  	        list = new ArrayList<String>();
  	        result.put(header.getName(), list);
  	      }
  	      list.add(header.getValue());
  	    }
      }
	}
  	return result;  
  }
  
  private List<String> getHeaders(HttpMessage httpMessage, String name){
    ArrayList<String> result = null;
  	if(httpMessage != null){
  	  Header[] headers = httpMessage.getHeaders(name);	
  	  if(headers != null && headers.length > 0){
  	    result = new ArrayList<String>();
  	    for(int i = 0;i < headers.length;i++){
  	      Header header = headers[i];
  	      result.add(header.getValue());
  	    }
      }
  	}
  	return result;
  }
    
  public void setHttpResponse(HttpResponse httpResponse){
    this.httpResponse = httpResponse;
    
    onSetHttpResponse(httpResponse);       
  } 
    
  protected void onSetHttpResponse(HttpResponse httpResponse){
	responseCode = httpResponse.getStatusLine().getStatusCode(); 
    if(responseCode == HttpStatus.SC_OK ||
       responseCode == HttpStatus.SC_CREATED){  
      try {
        setLength(httpResponse.getEntity().getContentLength());
        setInputStream(httpResponse.getEntity().getContent());
	  } catch (Exception e) {
	    e.printStackTrace();
	    setError(InternetConnection.INTERNET_ERROR, e.toString());
	  }
  	}else{		  
      setError(InternetConnection.INTERNET_RESPONSE_ERROR, "Listening - Other HTTP_RESPONSE_CODE " + responseCode);	
  	}	   
  }

  @Override
  public HttpClientConnectionDescriptor deepCopy() {
	return new HttpClientConnectionDescriptor(getHttpResponse());
  }
}