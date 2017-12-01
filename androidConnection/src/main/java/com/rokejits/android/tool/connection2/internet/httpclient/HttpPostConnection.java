package com.rokejits.android.tool.connection2.internet.httpclient;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class HttpPostConnection extends HttpClientConnection{
	
  private String postParam;
  private Hashtable<String, String> param;
  private HttpEntity httpEntity;
  
  public HttpPostConnection(Context context, String url) {
	super(context, url);	
  }
  
  public HttpPostConnection(Context context, String url, Hashtable<String, String> param) {
	super(context, url);
	this.param = param;
	
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);    
    Enumeration<String> keys = param.keys();
    while(keys.hasMoreElements()){
      String key = keys.nextElement();
      nameValuePairs.add(new BasicNameValuePair(key, param.get(key)));	  
    }      
    try {    
	  setEntity(new UrlEncodedFormEntity(nameValuePairs));
	} catch (UnsupportedEncodingException e1) {
	  e1.printStackTrace();
	}    
  } 
  
  public HttpPostConnection(Context context, String url, String post) {
	super(context, url);	
	this.postParam = post;
      setEntity(new StringEntity(postParam, HTTP.UTF_8));
  }
  
//  private void printHeader(Header...headers){
//	if(headers == null)
//	  return;
//    for(int i = 0;i < headers.length;i++){
//      Header header = headers[i];
//      if(header == null)
//       continue;
//      Log.e("Header name = " + header.getName());
//      Log.e("Header value = " + header.getValue());
//    }	  
//  }
  
  public Hashtable<String, String> getParams(){
    return param;	  
  }
  
  public String getPostEntity(){
    return postParam;	  
  }
  
  public void setEntity(HttpEntity httpEntity){
    this.httpEntity = httpEntity;	  
  }

  @Override
  protected HttpUriRequest getRequest(String url) {
    HttpPost httpPost = new HttpPost(url);         
    if(httpEntity != null){
     
	  httpPost.setEntity(httpEntity);
    }
	      
	return httpPost;
  }
  
  @Override
  protected HttpPostConnection doDeepCopy() {
	HttpPostConnection httpPostConnection = null;
	if(param != null)
	  httpPostConnection =  new HttpPostConnection(getContext(), getUrl(), param);
	else if(postParam != null)
	  httpPostConnection =  new HttpPostConnection(getContext(), getUrl(), postParam);
	
	if(httpEntity != null)
	  httpPostConnection.setEntity(httpEntity);
	
	return httpPostConnection;	  
	
  }

}
