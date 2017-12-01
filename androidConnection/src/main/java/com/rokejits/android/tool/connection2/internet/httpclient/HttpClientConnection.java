package com.rokejits.android.tool.connection2.internet.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyStore;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.IConnectionDescriptor;
import com.rokejits.android.tool.connection2.MySSLSocketFactory;
import com.rokejits.android.tool.connection2.internet.InternetConnection;

public abstract class HttpClientConnection extends InternetConnection{

  private Vector<Header> headerList;
  private Vector<Cookie> cookieList;
  private Hashtable<String, Object> httpParams;
  private CookieStore cookieStore;
  private HttpClientConnectionDescriptor httpClientConnectionDescriptor;	
  private boolean ignoreHandlerHttps = false;
  
  public HttpClientConnection(Context context, String url) {
	super(context, url);
	setHttpClientConnectionDescriptor(new HttpClientConnectionDescriptor());
  }
  
  public HttpClientConnection addHeader(String header, String value){   
    return addHeader(new BasicHeader(header, value));
  }
  
  public void setHttpClientConnectionDescriptor(HttpClientConnectionDescriptor httpClientConnectionDescriptor){
    this.httpClientConnectionDescriptor = httpClientConnectionDescriptor;	  
  }
  
  public void setParams(String key, Object value){
    if(httpParams == null)
      httpParams = new Hashtable<String, Object>();
    httpParams.put(key, value);
  }
  
  public void ignoreHandlerHttps(){
    ignoreHandlerHttps = true;	  
  }
  
  public HttpClientConnection addHeader(Header header){  
    if(headerList == null)
      headerList = new Vector<Header>();
    headerList.add(header);
    return this;
  }
  
  public void addCookie(Cookie cookie){
    if(cookieList == null)
      cookieList = new Vector<Cookie>();
    cookieList.add(cookie);
  }
  
  public Vector<Cookie> getCookieList(){
    return cookieList;	  
  }
  
  public void setCookieStore(CookieStore cookieStore){
    this.cookieStore = cookieStore;	  
  }  
  
  @Override
  protected final IConnectionDescriptor doInternetConnect(String url) {	
	InputStream is = null;
	HttpClientConnectionDescriptor conDesc = httpClientConnectionDescriptor;
	try {
	  AbstractHttpClient httpClient = getHttpClient();
	  if(httpParams != null){
	    Set<String> keys = httpParams.keySet();
	    for(String key : keys){
	      httpClient.getParams().setParameter(key, httpParams.get(key));	
	    }
	  }
	  
	  if(cookieList != null){
	    if(cookieStore == null)
	      cookieStore = httpClient.getCookieStore();
	    if(cookieStore == null){
	  	  cookieStore = new BasicCookieStore();
	  	  
	    }
	    
	    cookieStore.clear();
	    
	    for(Cookie c : cookieList){
	      cookieStore.addCookie(c);	
	    }	  
	  }
		  
	  if(cookieStore != null)
	    httpClient.setCookieStore(cookieStore);	  
	  HttpUriRequest request = getRequest(url);
	  
	  if(headerList != null){
	    for(Header header : headerList){
	      request.addHeader(header);	  
	    }	
	  }    	
	  conDesc.setHttpUriRequest(request);
	  
	  if(url.startsWith(HTTPS_PROTOCOL) && !ignoreHandlerHttps)
	    setHttps(url, httpClient);	
      HttpResponse res = httpClient.execute(request);
      conDesc.setHttpResponse(res);
      if(conDesc.getResponseCode() == HttpStatus.SC_MOVED_TEMPORARILY || 
         conDesc.getResponseCode() == HttpStatus.SC_MOVED_PERMANENTLY){
        HttpClientConnectionDescriptor reDirectDesc = handlerRedirect(res);
        if(reDirectDesc != null)
          conDesc = reDirectDesc;
      }
      
      
	} catch (MalformedURLException e){
	  Log.e("HttpClientConnection error url = " + url);
	  Log.e("HttpClientConnection error MalformedURLException = "+e.toString());
      conDesc.setError(MULFORMED_URL_ERROR, e.toString());	
    } catch (SocketTimeoutException e){
      Log.e("HttpClientConnection error url = " + url);
      Log.e("HttpClientConnection error SocketTimeoutException = "+e.toString());	
	  conDesc.setError(INTERNET_TIMEOUT_ERROR, e.toString());	
    } catch (ConnectTimeoutException e){
      Log.e("HttpClientConnection error url = " + url);
      Log.e("HttpClientConnection error ConnectTimeoutException = "+e.toString());	
	  conDesc.setError(INTERNET_TIMEOUT_ERROR, e.toString());			
    } catch(Exception e){
      Log.e("HttpClientConnection error url = " + url);
      Log.e("HttpClientConnection error Exception = "+e.toString());	
  	  conDesc.setError(INTERNET_ERROR, e.toString());	
	} finally {  		  
      try {
        if(is != null)	  
          is.close();
      } catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
      }	 
    }
	
	return conDesc;
  }
  
  protected HttpClientConnectionDescriptor handlerRedirect(HttpResponse httpResponse){
    Header[] locationList = httpResponse.getHeaders(HttpHeaders.LOCATION);	
    if(locationList != null && locationList.length > 0){
      String redirectUrl = locationList[0].getValue();
      if(redirectUrl != null){
        HttpGetConnection httpGetConnection = new HttpGetConnection(getContext(), redirectUrl);
        return (HttpClientConnectionDescriptor) httpGetConnection.doInternetConnect(redirectUrl);
      }
    }
    
    return null;
  }
  
  private void setHttps(String url, AbstractHttpClient client){
    try {
      Log.e("setHttps = " + url);
      URL urlSpec = new URL(url);
      int port = urlSpec.getPort();
      Log.e("port1 = " + port);
      if(port == -1)
        port = 443;
      Log.e("port2 = " + port);
  	  KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	  trustStore.load(null, null);
	  MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	  sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	  client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, port));	  
	} catch (Exception e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	}	
  }
  
  protected AbstractHttpClient getHttpClient(){
    HttpParams httpParameters = new BasicHttpParams();      
    // Set the timeout in milliseconds until a connection is established.
    // The default value is zero, that means the timeout is not used.      
    HttpConnectionParams.setConnectionTimeout(httpParameters, getTimeout());      
    // Set the default socket timeout (SO_TIMEOUT) 
    // in milliseconds which is the timeout for waiting for data.      
    HttpConnectionParams.setSoTimeout(httpParameters, getTimeout());  
    //httpParameters.setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
    //Log.e("get user agent = " + httpParameters.getParameter(CoreProtocolPNames.USER_AGENT));
    
    return new DefaultHttpClient(httpParameters);
  } 
  
  @Override
  protected IConnection2 onDeepCopy() {
    HttpClientConnection httpClientConnection = doDeepCopy();
    if(httpClientConnection != null){
      HttpClientConnectionDescriptor clientConnectionDescriptor = (HttpClientConnectionDescriptor) getConnectionDescription();
      if(clientConnectionDescriptor != null)
        httpClientConnection.setHttpClientConnectionDescriptor(clientConnectionDescriptor.deepCopy());
    
      httpClientConnection.setCookieStore(cookieStore);
    
      if(headerList != null){
        for(Header header : headerList){
          httpClientConnection.addHeader(header);	  
        }	
      }
    
      if(cookieList != null){
        for(Cookie cookie : cookieList){
          httpClientConnection.addCookie(cookie);	  
        }  	
      }
    }    
    
    
    return httpClientConnection;
  }
  
  protected abstract HttpClientConnection doDeepCopy();
  protected abstract HttpUriRequest getRequest(String url);
  
  
  

}
