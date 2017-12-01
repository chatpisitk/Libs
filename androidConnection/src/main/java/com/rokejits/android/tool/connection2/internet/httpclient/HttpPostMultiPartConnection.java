package com.rokejits.android.tool.connection2.internet.httpclient;

import android.content.Context;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.io.ProgressOutputStream;
import com.rokejits.android.tool.io.ProgressOutputStream.OnTransferListener;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

public class HttpPostMultiPartConnection extends HttpClientConnection{
//  private String paramName, name, mediaMimeType;
//  private byte[] mediaBody;
//  private File fileBody;
  //private Vector<MyContentBody> mediaBodyList;
  private Hashtable<String, MyContentBody> contentBodyList;
  private Hashtable<String, String> param; 
  private OnProgressListener onProgressListener;
  //private long mediaContentLength;
  private MyHttpEntity myHttpEntity;
  
	
  public HttpPostMultiPartConnection(Context context, String url, Hashtable<String, String> param) {
	super(context, url);
	this.param = param;
    addParam(param);
  }
  
  public HttpPostMultiPartConnection(Context context, String url, Hashtable<String, String> param, String name, String paramName, byte[] data) {
	super(context, url);
	this.param = param;
    addParam(param);
	addMedia(paramName, name, data);
  }
  
  public HttpPostMultiPartConnection(Context context, String url, Hashtable<String, String> param, String name, String paramName, byte[] data, ContentType mimeType) {
	super(context, url);
	this.param = param;
    addParam(param);
	addMedia(paramName, name, mimeType, data);
  }
  
  public Hashtable<String, String> getParams(){
    return param;	  
  }

  private void addParam(Hashtable<String, String> params){
    if(params != null){
      Enumeration<String> keys = params.keys();
      while(keys.hasMoreElements()){
        String key = keys.nextElement();
        String value = params.get(key);

        StringBody stringBody = new StringBody(value, ContentType.create("text/plain", "utf-8"));
        addContentBody(key, stringBody, value.length());
      }
    }
  }
  
  public void addMedia(String paramName, String name, byte[] mediaBody){
    addMedia(paramName, paramName, null, mediaBody);  	  
  }

  public void addContentBody(String paramName, ContentBody contentBody, long length){
    if(contentBodyList == null)
      contentBodyList = new Hashtable<String, MyContentBody>();
    MyContentBody myContentBody = new MyContentBody();
    myContentBody.contentBody = contentBody;
    myContentBody.length = length;
    contentBodyList.put(paramName, myContentBody);
  }

  public void addMedia(String paramName, String name, ContentType mimeType, byte[] mediaBody){
    ByteArrayBody contentBody = new ByteArrayBody(mediaBody, mimeType, name);
    addContentBody(paramName, contentBody, mediaBody.length);
  }  

  public void addMedia(String paramName, File fileBody){
    addMedia(paramName, null, fileBody);    
  }
  
  public void addMedia(String paramName, String mimeType, File fileBody){
    addMedia(paramName, null, mimeType, fileBody);	  
  }
  
  public void addMedia(String paramName, String filename, String mimeType, File fileBody){  
    ContentType contentType = null;
    if(mimeType != null)
      contentType = ContentType.create(mimeType);
    addMedia(paramName, filename, contentType, fileBody);
  }
  
  public void addMedia(String paramName, String filename, ContentType mimeType, File fileBody){
    FileBody fileBody1 = new FileBody(fileBody, mimeType, filename);
    addContentBody(paramName, fileBody1, fileBody.length());
  } 
  
  @Override
  protected HttpUriRequest getRequest(String url) {	
    HttpPost postRequest = new HttpPost(url);      
    
    long contentLength = 0;
    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();   
    
    Log.e("contentBodyList = " + contentBodyList);
    if(contentBodyList != null){
      Enumeration<String> keys = contentBodyList.keys();
      while(keys.hasMoreElements()){
        String key = keys.nextElement();
        MyContentBody myContentBody = contentBodyList.get(key);

        multipartEntityBuilder.addPart(key, myContentBody.contentBody);
        contentLength = contentLength + myContentBody.length;
      }
    }
    
    myHttpEntity = new MyHttpEntity(multipartEntityBuilder.build(), contentLength);
    myHttpEntity.setProgressListener(onProgressListener);
    
    postRequest.setEntity(myHttpEntity);    
	return postRequest;
  }  
  
  public void setOnProgressListener(OnProgressListener onProgressListener) {
	this.onProgressListener = onProgressListener;
  }  
  
  @Override
  protected HttpPostMultiPartConnection doDeepCopy() {
    HttpPostMultiPartConnection httpPostMultiPartConnection = new HttpPostMultiPartConnection(getContext(), getUrl(), null);
    httpPostMultiPartConnection.param = getParams();
    if(contentBodyList != null){
      Enumeration<String> keys = contentBodyList.keys();
      while(keys.hasMoreElements()){
        String key = keys.nextElement();
        MyContentBody myContentBody = contentBodyList.get(key);
        httpPostMultiPartConnection.addContentBody(key, myContentBody.contentBody, myContentBody.length);
      }
    }
    return httpPostMultiPartConnection;
    
  }
  
  public interface OnProgressListener{
    public void onProgressed(long progress, long contentLength);	  
  }
  
  class MyContentBody{
    public ContentBody contentBody;
    public long length;
  }
  
  class MyHttpEntity implements HttpEntity, OnTransferListener{
	private HttpEntity pHttpEntity;  
	private OnProgressListener onProgressListener;
	private long contentLength;
    
	public MyHttpEntity(HttpEntity pHttpEntity, long contentLength){
      this.pHttpEntity = pHttpEntity;
      this.contentLength = contentLength;
    }
	
	public void setProgressListener(OnProgressListener onProgressListener) {
	  this.onProgressListener = onProgressListener;
	}
    
	@Override
	public void consumeContent() throws IOException {
	  pHttpEntity.consumeContent();	
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
	  return pHttpEntity.getContent();
	}

	@Override
	public Header getContentEncoding() {	  	
	  return pHttpEntity.getContentEncoding();
	}

	@Override
	public long getContentLength() {
	  return pHttpEntity.getContentLength();
	}

	@Override
	public Header getContentType() {
	  return pHttpEntity.getContentType();
	}

	@Override
	public boolean isChunked() {
	  return pHttpEntity.isChunked();
	}

	@Override
	public boolean isRepeatable() {
	  return pHttpEntity.isRepeatable();
	}

	@Override
	public boolean isStreaming() {
	  return pHttpEntity.isStreaming();
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
	  ProgressOutputStream pOut = new ProgressOutputStream(outstream);  	  
 	  pOut.setOnTransferListener(this);
 	  pHttpEntity.writeTo(pOut);
	}
	
	private void updateProgress(long progress, long contentLength){
      if(onProgressListener != null){
        onProgressListener.onProgressed(progress, contentLength);	
      }	  
    } 

	@Override
	public void onTransfered(long transfered) {
	  updateProgress(transfered, contentLength); 		
	}
	  
  }
  

}
