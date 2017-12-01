package com.rokejits.android.tool.cache;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.rokejits.android.tool.connection2.ConnectionListener;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.utils.IOUtils;

public class ApiCacheConnection extends CacheConnection {
   
  private ApiCacheConnectAdapter apiCacheConnectAdapter;	
	
  public ApiCacheConnection(String cacheId, 
		                    IConnection2 iConnection, 
		                    ApiCacheConnectAdapter apiCacheConnectAdapter) {
	super(cacheId, iConnection);
	this.apiCacheConnectAdapter = apiCacheConnectAdapter;
  }

  @Override
  protected CacheConnectDescriptor doConnect(String cacheId) {
    CacheConnectDescriptor cacheConnectDescriptor = super.doConnect(cacheId);
    if(cacheConnectDescriptor == null && !isForceRefresh()){
      cacheConnectDescriptor = getConnectionDescription();  
      if(!cacheConnectDescriptor.isHasCache())
        cacheConnectDescriptor = null;
    }
    return cacheConnectDescriptor;
  }
  
  @Override
  protected void onConnected(IConnection2 iConnection2, InputStream in) {
	byte[] data = IOUtils.readByteArrayFromInputStream(in);	
	if(apiCacheConnectAdapter.isSuccess(iConnection2, new ByteArrayInputStream(data))){
	  super.onConnected(iConnection2, new ByteArrayInputStream(data));	  	
	}else{
	  ConnectionListener connectionListener = getConnectionListener();
	  if(connectionListener != null)
	    connectionListener.onConnected(ApiCacheConnection.this, new ByteArrayInputStream(data));  
	}	
  }
  
  @Override
  protected CacheConnection onDeepCopy() {
    return new ApiCacheConnection(getUrl(), getConnection().deepCopy(), apiCacheConnectAdapter);	
  }
  
  public interface ApiCacheConnectAdapter{
    public boolean isSuccess(IConnection2 iConnection2, InputStream in);	  
  }
  
  
  
}
