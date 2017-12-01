package com.rokejits.android.tool.connection2.internet.httpurlconnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.IConnectionDescriptor;
import com.rokejits.android.tool.connection2.internet.InternetConnection;

public abstract class HttpURLConnection extends InternetConnection{

  private Hashtable<String, String> requestProperties;	
  private HttpURLConnectionDescriptor conDesc;
	
  public HttpURLConnection(Context context, String url) {
	super(context, url);	 
	setHttpURLConnectionDescriptor(new HttpURLConnectionDescriptor());
  }  
  
  public void setHttpURLConnectionDescriptor(HttpURLConnectionDescriptor conDesc){
    this.conDesc = conDesc;	  
    conDesc.setMyHttpUrlConnection(this);
  }  
  
  @Override
  protected IConnectionDescriptor doInternetConnect(String urlString) {
	//Log.e("HttpUrlConnection doInternetConnect = "+urlString);
	if(conDesc == null)  
	  setHttpURLConnectionDescriptor(new HttpURLConnectionDescriptor());	
	
	try {
	  SSLContext context = null;
	  HostnameVerifier hostnameVerifier = null;
	  if(urlString.startsWith(HTTPS_PROTOCOL)){
		Log.e("HttpUrlConnection HTTPSSSSSSSSSSSSSSSSS2222");
	    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//	    String algorithm = TrustManagerFactory.getDefaultAlgorithm();
//	    TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
//	    tmf.init(keyStore);
	    TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        
	    context = SSLContext.getInstance("TLS");
	    context.init(null, new TrustManager[] { tm }, null);
	   // context.init(null, tmf.getTrustManagers(), null);  	
	    hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;	    
	  }
	  Log.e("HttpUrlConnection connect to = " + urlString);
	  URL url = new URL(urlString);	
	  java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
	  conn.setRequestMethod(getRequestMethod());
	  conn.setConnectTimeout(getTimeout());
	  conn.setReadTimeout(getTimeout());  
	  
	  
	  if(requestProperties != null){
		Enumeration<String> keys = requestProperties.keys();
		while(keys.hasMoreElements()){
		  String key = keys.nextElement();
		  conn.addRequestProperty(key, requestProperties.get(key));
		}	    
	  }
	  
	  
	  if(context != null){
	    ((HttpsURLConnection) conn).setSSLSocketFactory(context.getSocketFactory());
	    ((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
	  }  
	  
	  conDesc.setHttpURLConnection(conn);	  
	  doHttpConnect(conn);
	  conDesc.onSetHttpURLConnection(conn);
	  
	  int resCode = conDesc.getResponseCode(); 
	  
	  if(resCode == java.net.HttpURLConnection.HTTP_MOVED_TEMP ||
		 resCode == java.net.HttpURLConnection.HTTP_MOVED_PERM){
	    HttpURLConnectionDescriptor reDirectCondesc = handlerRedirect(conn);
	    if(reDirectCondesc != null)
	      conDesc = reDirectCondesc;
	  }
	} catch (MalformedURLException e){
	  e.printStackTrace();
	  Log.e("HttpURLConnection error MalformedURLException = "+e.toString());
      conDesc.setError(MULFORMED_URL_ERROR, e.toString());	
    } catch (SocketTimeoutException e){
      e.printStackTrace();
      Log.e("HttpURLConnection error SocketTimeoutException = "+e.toString());	
	  conDesc.setError(INTERNET_TIMEOUT_ERROR, e.toString());	
	} catch(Exception e){
	  e.printStackTrace();
      Log.e("HttpURLConnection error Exception = "+e.toString());	
  	  conDesc.setError(INTERNET_ERROR, e.toString());	
	}
	return conDesc;
  } 
  
  protected HttpURLConnectionDescriptor handlerRedirect(java.net.HttpURLConnection conn){
    String redirectUrl = conn.getHeaderField("Location");
	    
    Log.e("HttpURLConnection redirectUrl = " + redirectUrl);
	if(redirectUrl != null){
      HttpURLGetConnection httpURLGetConnection = new HttpURLGetConnection(getContext(), redirectUrl);
      return (HttpURLConnectionDescriptor) httpURLGetConnection.doInternetConnect(redirectUrl);
    }       
	
	return null;
  }
  
  public void putRequestProperty(String field, String value){ 
    if(requestProperties == null)
      requestProperties = new Hashtable<String, String>();
    requestProperties.put(field, value);
  } 
  
  public Hashtable<String, String> getRequestProperties(){
    return requestProperties;	  
  }
  
  @Override
  protected final HttpURLConnection onDeepCopy() {
	HttpURLConnection httpURLConnection = doDeepCopy();
	if(httpURLConnection != null){
	  HttpURLConnectionDescriptor httpURLConnectionDescriptor = (HttpURLConnectionDescriptor) getConnectionDescription();
	  if(httpURLConnectionDescriptor != null)
	    httpURLConnection.setHttpURLConnectionDescriptor(httpURLConnectionDescriptor.deepCopy());
	  
	  if(requestProperties != null){
	    Enumeration<String> keys = requestProperties.keys();
	    while(keys.hasMoreElements()){
	      String key = keys.nextElement();
	      httpURLConnection.putRequestProperty(key, requestProperties.get(key)); 
	    }
	  }
	}
	return httpURLConnection;
  }
  
  protected abstract HttpURLConnection doDeepCopy();
  protected abstract String getRequestMethod();
  protected abstract void doHttpConnect(java.net.HttpURLConnection connection)  throws IOException ;  
  
  
  
}
