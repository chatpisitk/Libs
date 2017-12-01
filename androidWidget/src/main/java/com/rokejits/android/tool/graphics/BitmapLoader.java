package com.rokejits.android.tool.graphics;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.cache.CacheConnection;
import com.rokejits.android.tool.connection2.ConnectionListener;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.file.AssetConnection;
import com.rokejits.android.tool.connection2.file.FileConnection;
import com.rokejits.android.tool.connection2.internet.InternetConnection;
import com.rokejits.android.tool.connection2.internet.httpclient.HttpGetConnection;
import com.rokejits.android.tool.utils.BitmapUtils;
import com.rokejits.android.tool.utils.HttpUtils;

public class BitmapLoader implements ConnectionListener {

  
  private BitmapDownloaderListener listener;
  private boolean isFinish = false, isLoading = false;
  private int expectWidth = 0, expectHeight = 0;
  private IConnection2 iConnection2, activeConnection;
  private Context context;
  
  public static final IConnection2 createConnection(Context context, String url){
    if(url != null){
      if(HttpUtils.isHttpConnection(url)){
        CacheConnection cacheConnection = new CacheConnection(String.valueOf(url.hashCode()), 
        		                            new HttpGetConnection(context, url));
        return cacheConnection;
  	  }else if(url.startsWith("/")){
       return new FileConnection(url);
      }else{
        return new AssetConnection(context, url);    		  
      }	
    }	
    return null;
  }
  
  public BitmapLoader(Context context, String url, BitmapDownloaderListener listener){
	this(context, createConnection(context, url), listener);    
  }
  
  public BitmapLoader(Context context, IConnection2 iConnection2, BitmapDownloaderListener listener){
	this.context = context;
    this.listener = listener;    
    this.iConnection2 = iConnection2;
  }
  
  public void setTimeout(int timeout){
    if(iConnection2 != null){
      if(iConnection2 instanceof InternetConnection){
        InternetConnection internetConnection = (InternetConnection) iConnection2;	
        internetConnection.setTimeout(timeout);
      }else if(iConnection2 instanceof CacheConnection){
        CacheConnection cacheConnection = (CacheConnection) iConnection2;
        cacheConnection.setTimeout(timeout);
      }
    }
      
  }
  
  public void setSize(int width, int height){
    this.expectWidth = width;
    this.expectHeight = height;
  }
  
  public void load(){
    load(false);	  
  }
  
  public boolean isFinish(){
    return isFinish;	  
  }
  
  public void load(boolean needReload){
	if(needReload){
	  isFinish = false;
	  isLoading = false;
	}
	if(isFinish || isLoading)
	  return;
	if(iConnection2 == null)
	  return;
	
	isLoading = true;
	
	activeConnection = iConnection2.deepCopy();
	activeConnection.setConnectionListener(this);
	activeConnection.connect();
		  
  }
  
  public void stopLoad(){
	isFinish = false;
	isLoading = false;
	if(activeConnection != null)
  	  activeConnection.stopConnect();  
  }
  
  
  public interface BitmapDownloaderListener{
    public void onBitmapLoaded(String url, Bitmap bitmap);	  
  }


  @Override
  public void onConnected(IConnection2 iConnection2, InputStream in) {	  
	Log.d("bitmap loader onConnected in = "+in);		
	Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(in, expectWidth, expectHeight);
	Log.d("bitmap loader onConnected bitmap = "+bitmap);
	isFinish = true;	
	isLoading = false;
	if(bitmap == null && activeConnection != null){
	  if(activeConnection instanceof CacheConnection){
	    CacheConnection cacheConnection = (CacheConnection) activeConnection;	  
	    cacheConnection.deleteCache();
	  }	
	}
    listener.onBitmapLoaded(iConnection2.getUrl(), bitmap);
  }
  
  @Override
  public void onConnectFailed(IConnection2 iConnection2, int errorCode, String error) {
	Log.d("bitmap loader onConnectFailed1 = "+errorCode);
	Log.d("bitmap loader onConnectFailed2 = "+error);
	isFinish = false;
	isLoading = false;
    listener.onBitmapLoaded(null, null);   	
	
  }
  
}
