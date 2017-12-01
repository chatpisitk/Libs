package com.rokejits.android.tool.connection2;

import com.rokejits.android.tool.connection2.handler.ErrorHandler;

public interface IConnection2 {
 
  public String getUrl();	
  public void setConnectionListener(ConnectionListener listener);
  public ConnectionListener getConnectionListener();
  public void setErrorHandler(ErrorHandler errorHandler);
  public ErrorHandler getErrorHandler();
  public IConnectionDescriptor getConnectionDescription();
  public void connect();
  public void stopConnect();
  public void immediateConnect();
  public IConnection2 deepCopy();
    
  
}
