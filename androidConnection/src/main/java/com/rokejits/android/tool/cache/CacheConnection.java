package com.rokejits.android.tool.cache;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.connection2.Connection2;
import com.rokejits.android.tool.connection2.ConnectionListener;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.file.FileConnection;
import com.rokejits.android.tool.connection2.file.FileConnection.FileConnectionDescriptor;
import com.rokejits.android.tool.connection2.internet.InternetConnection;
import com.rokejits.android.tool.utils.HttpUtils;
import com.rokejits.android.tool.utils.TimeUtils;

public class CacheConnection extends Connection2{ 
  
  public static final int CREATE_CACHE_FILE_ERROR		= 0x56874;
  public static final int DELETE_CACHE_FILE_ERROR		= CREATE_CACHE_FILE_ERROR + 1;
  public static final int CACHE_FILE_ERROR				= DELETE_CACHE_FILE_ERROR + 1;
	
  private IConnection2 iConnection;  
  private CacheConnectDescriptor cacheConnectDescriptor;
  private int lifeTime = TimeUtils.DAY;
  private boolean forceRefresh;
  
  public static final String getCacheId(String url, Hashtable<String, String> param){
    return getCacheId(url + HttpUtils.getParam(param, false, false));	  
  }
  
  public static final String getCacheId(String url){
    return String.valueOf(url.hashCode());	  
  }  
  
  public static final String getCacheFileName(String url){
    return getCacheId(url) + ".cache";	  
  }
  
  public CacheConnection(String cacheId, IConnection2 iConnection) {
	super(cacheId);	
	this.iConnection = iConnection;	
	iConnection.setConnectionListener(connectionListener);
  }  
  
  protected IConnection2 getConnection(){
    return iConnection;	  
  }
  
  public void setForceRefresh(boolean forceRefresh){
    this.forceRefresh = forceRefresh;	  
  }
  
  public boolean isForceRefresh(){
    return forceRefresh;	  
  }
  
  public void setLifeTime(int lifeTime){
    this.lifeTime = lifeTime;	  
  }
  
  public void setTimeout(int timeout){
    if(iConnection != null && iConnection instanceof InternetConnection){
      InternetConnection internetConnection = (InternetConnection) iConnection;
      internetConnection.setTimeout(timeout);
    }	  
  }
  
  protected File getCacheFile(){
    CacheManager cacheManager = CacheManager.getInstance();
    if(cacheManager.getCacheFolder() != null){
      return new File(cacheManager.getCacheFolder(), getUrl() + ".cache");      
    }  	 
    
    return null;
  }
  
  public boolean deleteCache(){    
	File cacheFile = getCacheFile();
	if(cacheFile != null && cacheFile.exists())	
	  return cacheFile.delete();
	return false;
  }
  
  @Override
  public CacheConnectDescriptor getConnectionDescription() {
	return cacheConnectDescriptor;
  }

  @Override
  protected CacheConnectDescriptor doConnect(String cacheId) {	
	cacheConnectDescriptor = new CacheConnectDescriptor();	
	iConnection.setErrorHandler(getErrorHandler());
	CacheManager cacheManager = CacheManager.getInstance();
	
	
	Log.e("cacheManager.isUseCache() = "+ cacheManager.isUseCache());
	Log.e("cacheManager.getCacheFolder() = "+ cacheManager.getCacheFolder());
	
	if(cacheManager.isUseCache() && 
	   cacheManager.getCacheFolder() != null && 
	   iConnection instanceof InternetConnection){	
	  File cacheFile = getCacheFile();	
	  Log.e("cacheFile = "+ cacheFile.getAbsolutePath());
	  Log.e("cacheFile.exists() = "+ cacheFile.exists());
	  if(cacheFile.exists()){
	    FileConnection fileConnection = new FileConnection(cacheFile.getAbsolutePath());
	    fileConnection.immediateConnect();
	    FileConnectionDescriptor fileConnectionDescriptor = (FileConnectionDescriptor) fileConnection.getConnectionDescription();
	    if(fileConnectionDescriptor.isSuccess()){	     
	      InputStream is = fileConnectionDescriptor.getInputStream();	
	      DataInputStream dIn = new DataInputStream(is);	      
		  try {
		    long cacheTime = dIn.readLong();
		    long lifeTime = dIn.readLong();
		    long currentTime = System.currentTimeMillis();
		    boolean isExpire = currentTime > cacheTime + lifeTime;
		    Log.e("currentTime = "+ currentTime);
		    Log.e("cacheTime = "+ cacheTime);
		    Log.e("lifeTime = "+ lifeTime);
		    Log.e("isExpire = "+ isExpire);
		    
		    cacheConnectDescriptor.setCacheConnectionDescriptor(fileConnectionDescriptor, isExpire);
		    cacheConnectDescriptor.setInputStream(is);
		    cacheConnectDescriptor.setLastUpdateDate(cacheTime);		    
		    if(!forceRefresh && !isExpire)
		      return cacheConnectDescriptor;
		  } catch (IOException e) {
			e.printStackTrace();
		  }		  
	    }	  
	  }	  	  
	}
	
	
    iConnection.connect(); 
	
//	iConnection.immediateConnect();
//	IConnectionDescriptor iConnectionDescriptor = iConnection.getConnectionDescription();
//	cacheConnectDescriptor.setConnectionDescriptor(iConnectionDescriptor);
//	if(iConnectionDescriptor.isSuccess()){
//	  cacheConnectDescriptor.setInputStream(saveCache(iConnectionDescriptor.getInputStream()));	
//	}else{
//	  cacheConnectDescriptor.setError(iConnectionDescriptor.getErrorCode(), iConnectionDescriptor.getError());	
//	}
	
	
	return null;
  }
  
  @Override
  public void stopConnect() {
	iConnection.stopConnect();
	super.stopConnect();
  }
  
  private InputStream saveCache(InputStream in){
    CacheManager cacheManager = CacheManager.getInstance();  
    if(cacheManager.isUseCache() && cacheManager.getCacheFolder() != null){
      File cacheFile = getCacheFile();
      if(CacheUtil.saveCache(cacheFile, in, lifeTime) == -1){
    	try {
    	  DataInputStream dIn = new DataInputStream(new FileInputStream(cacheFile));
    	  long cacheTime = dIn.readLong();
    	  dIn.readLong();
    	  cacheConnectDescriptor.setLastUpdateDate(cacheTime);
		  return dIn;
		} catch (FileNotFoundException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	   
	  }
    }    
    return in;
  }
  
  protected void onConnected(IConnection2 iConnection2, InputStream in){
    cacheConnectDescriptor.setConnectionDescriptor(iConnection.getConnectionDescription());
    cacheConnectDescriptor.setInputStream(saveCache(in));  

    ConnectionListener connectionListener = getConnectionListener();
    if(connectionListener != null)
      connectionListener.onConnected(CacheConnection.this, cacheConnectDescriptor.getInputStream());	  
  }
  
  @Override
  protected CacheConnection onDeepCopy() {
	CacheConnection cacheConnection = new CacheConnection(getUrl(), iConnection.deepCopy());
	cacheConnection.setLifeTime(lifeTime);
	return cacheConnection;
  } 
  
  protected void onConnectFailed(IConnection2 iConnection2, int errorCode, String error){
    cacheConnectDescriptor.setConnectionDescriptor(iConnection.getConnectionDescription());	
    cacheConnectDescriptor.setError(errorCode, error);	  
    ConnectionListener connectionListener = getConnectionListener();
    if(connectionListener != null)
      connectionListener.onConnectFailed(CacheConnection.this, errorCode, error);  
  }
  
  
  private ConnectionListener connectionListener = new ConnectionListener() {
	
	@Override
	public void onConnected(IConnection2 iConnection2, InputStream in) {
	  CacheConnection.this.onConnected(iConnection2, in);	    			  	
	}
	
	@Override
	public void onConnectFailed(IConnection2 iConnection2, int errorCode, String error) {	  
	  CacheConnection.this.onConnectFailed(iConnection2, errorCode, error);  
	}
  };


  
  
  
}
