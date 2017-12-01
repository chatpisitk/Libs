package com.rokejits.android.tool.connection2.internet;

import android.content.Context;

import com.rokejits.android.tool.connection2.Connection2;
import com.rokejits.android.tool.connection2.IConnectionDescriptor;
import com.rokejits.android.tool.utils.HttpUtils;
import com.rokejits.android.tool.utils.StringUtils;
import com.rokejits.android.tool.utils.TimeUtils;

import org.apache.http.protocol.HTTP;

import java.util.List;
import java.util.Map;

public abstract class InternetConnection extends Connection2{ 
  public static final String HTTP_PROTOCOL = "http://";	
  public static final String HTTPS_PROTOCOL = "https://";		
	
  public static final int NO_INTERNET_CONNECTION 				    = 0xffff00;
  public static final int INTERNET_RESPONSE_ERROR                   = NO_INTERNET_CONNECTION + 1;
  public static final int INTERNET_ERROR  							= INTERNET_RESPONSE_ERROR + 1;	
  public static final int INTERNET_TIMEOUT_ERROR  					= INTERNET_ERROR + 1;  
  public static final int MULFORMED_URL_ERROR  						= INTERNET_TIMEOUT_ERROR + 1;
  public static final String NO_INTERNET_CONNECTION_STRING  		= "No network connection";
  
  
  private int timeout = 30 * TimeUtils.SEC;
  private Context context;
  public InternetConnection(Context context, String url) {
	super(url);
	this.context = context;
  }
  
  protected Context getContext(){
    return context;	  
  }
  
  public void setTimeout(int timeout){
    this.timeout = timeout;	  
  }
  
  public int getTimeout(){  
    return timeout;	  
  }  
  
  public Map<String, List<String>> getAllRequestHeader(){
	InternetConnectionDescriptor internetConnectionDescriptor = getConnectionDescription();
	if(internetConnectionDescriptor != null)
	  return internetConnectionDescriptor.getAllRequestHeader();
    return null;
  }	
  
  public List<String> getRequestHeaders(String field){
    InternetConnectionDescriptor internetConnectionDescriptor = getConnectionDescription();
	if(internetConnectionDescriptor != null)
	  return internetConnectionDescriptor.getRequestHeaders(field);
    return null;  
  }
  
  public Map<String, List<String>> getAllResponseHeader(){
    InternetConnectionDescriptor internetConnectionDescriptor = getConnectionDescription();
	if(internetConnectionDescriptor != null)
	  return internetConnectionDescriptor.getAllResponseHeader();
    return null;	  
  }	
  
  public List<String> getResponseHeaders(String field){
    InternetConnectionDescriptor internetConnectionDescriptor = getConnectionDescription();
	if(internetConnectionDescriptor != null)
	  return internetConnectionDescriptor.getResponseHeaders(field);
    return null;   
  } 
  
  public String getRequestHeader(String field){
    List<String> results = getRequestHeaders(field);
    if(results != null && results.size() > 0){
      return results.get(0);	
    }
    return null;
  }
  
  public String getResponseHeader(String field){
    List<String> results = getResponseHeaders(field);
    if(results != null && results.size() > 0){
      return results.get(0);	
    }
    return null;	  
  }
  
  public long getContentLength(){		
    return StringUtils.getStringAsLong(getResponseHeader(HTTP.CONTENT_LEN), -1);	  
  }
  
  public long getIfModifiedSince(){
    return StringUtils.getStringAsLong(getResponseHeader(IHttpHeader.IF_MODIFIED_SINCE), -1);  
  }
  
  public long getLastModified(){
    return StringUtils.getStringAsLong(getResponseHeader(IHttpHeader.LAST_MODIFIED), -1);	  
  }
  
  
  @Override
  public InternetConnectionDescriptor getConnectionDescription() {
	IConnectionDescriptor iConnectionDescriptor = super.getConnectionDescription();
	if(iConnectionDescriptor != null){
	  return (InternetConnectionDescriptor) iConnectionDescriptor;	
	}
	return null;
  }
  
  
  
  @Override
  protected IConnectionDescriptor doConnect(String url) {
	if(!HttpUtils.isHasNetworkConnection(context)){   	  
   	  return new MyInternetConnectionDescriptor(NO_INTERNET_CONNECTION, NO_INTERNET_CONNECTION_STRING);
    }
	
	return doInternetConnect(onEncodeUrl(url)); 
  }
  
  protected String onEncodeUrl(String url){
    //return HttpUtils.URLEncode2(url);	
	return url;	
  }
  
  protected abstract IConnectionDescriptor doInternetConnect(String url);

  public class MyInternetConnectionDescriptor extends InternetConnectionDescriptor{

    public MyInternetConnectionDescriptor(){
      super();
    }

    public MyInternetConnectionDescriptor(int errorCode, String error){
      super(errorCode, error);
    }
    @Override
    public Map<String, List<String>> getAllRequestHeader() {
      return null;
    }

    @Override
    public List<String> getRequestHeaders(String field) {
      return null;
    }

    @Override
    public Map<String, List<String>> getAllResponseHeader() {
      return null;
    }

    @Override
    public List<String> getResponseHeaders(String field) {
      return null;
    }


    @Override
    public IConnectionDescriptor deepCopy() {
      return new MyInternetConnectionDescriptor(getErrorCode(), getError());
    }
  }

}
