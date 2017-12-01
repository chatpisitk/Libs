package com.rokejits.android.tool.cache;

import com.rokejits.android.tool.connection2.IConnectionDescriptor;
import com.rokejits.android.tool.connection2.SetableConnectionDescriptor;

public class CacheConnectDescriptor extends SetableConnectionDescriptor{  

  private IConnectionDescriptor cacheConnectionDescriptor, connectionDescriptor;
  private boolean isCache = false, isExpire = false;  
  private long lastUpdateDate;
    
  public boolean isExpire(){
    return isExpire;	  
  }
  
  public boolean isHasCache(){
    return cacheConnectionDescriptor != null;	  
  }
  
  public boolean isCache(){
    return isCache;	  
  }
  
  public long getLastUpdateDate(){
    return lastUpdateDate;	  
  }
  
  public void setLastUpdateDate(long lastUpdateDate){
    this.lastUpdateDate = lastUpdateDate;	  
  }
  
  public void setCacheConnectionDescriptor(IConnectionDescriptor iConnectionDescriptor, boolean isExpire){
	this.cacheConnectionDescriptor = iConnectionDescriptor; 
	isCache = true;
	this.isExpire = isExpire;
	setInputStream(cacheConnectionDescriptor.getInputStream());
  } 
  
  public void setConnectionDescriptor(IConnectionDescriptor iConnectionDescriptor){
	isCache = false;
	this.connectionDescriptor = iConnectionDescriptor;  
  }
  
  public IConnectionDescriptor getCacheConnectionDescriptor(){
    return cacheConnectionDescriptor;	  
  }
  
  public IConnectionDescriptor getConnectionDescriptor(){
    return connectionDescriptor;	  
  }

  @Override
  public IConnectionDescriptor deepCopy() {
	return new CacheConnectDescriptor();
  }  
  
}
