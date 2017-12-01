package com.rokejits.android.tool.connection2;

import java.io.InputStream;

import com.rokejits.android.tool.connection2.handler.ErrorHandler;
import com.rokejits.android.tool.connection2.handler.SimpleErrorHandler;


public abstract class Connection2 implements IConnection2{ 
  
  private String url;
  private ConnectionListener listener;
  private ConnectThread connectThread;
  private ErrorHandler errorHandler;
  private IConnectionDescriptor conDesc;
  
  public Connection2(String url){
    this.url = url;	   
  } 
  
  @Override
  public String getUrl(){
    return url;	  
  }
  
  @Override
  public void setConnectionListener(ConnectionListener listener){
    this.listener = listener;	  
  }
  
  @Override
  public ConnectionListener getConnectionListener() {	
	return listener;
  }
  
  @Override
  public void setErrorHandler(ErrorHandler errorHandler){
    this.errorHandler = errorHandler;	  
  }
  
  @Override
  public ErrorHandler getErrorHandler(){
    return errorHandler;	  
  }
  
  @Override
  public IConnectionDescriptor getConnectionDescription(){
    return conDesc;	  
  }
  
  @Override
  public void connect(){
    if(connectThread != null){
      return;	
    }	  
    connectThread = new ConnectThread();
    connectThread.start();
  }
  
  @Override
  public void stopConnect(){
    listener = null;	  
  }
  
  @Override
  public void immediateConnect(){
    doConnect();	  
  }
  
  private void doConnect(){
    //Log.e("String connect to " + url);
    conDesc = doConnect(url);
    //Log.e("conDesc =  " + conDesc);
    if(conDesc == null)
      return;
   // Log.e("conDesc.isSuccess() =  " + conDesc.isSuccess());
    if(conDesc.isSuccess()){     
      updateSuccess(conDesc.getInputStream());     
    }else{      
      updateError(conDesc.getErrorCode(), conDesc.getError());      
    }	  
  }
  
  private void updateSuccess(InputStream in){
    if(listener != null){
      listener.onConnected(this, in);	
    }	  
  }
  
  private void updateError(int errorCode, String error){
    if(listener != null){
      if(errorHandler == null)
        setErrorHandler(new SimpleErrorHandler());
	  error = errorHandler.handlerError(errorCode, error);  
	  listener.onConnectFailed(this, errorCode, error);	
    }	  	  
  }  
  
  @Override
  public final IConnection2 deepCopy() {
	IConnection2 iConnection2 = onDeepCopy();
	if(iConnection2 != null){
	  iConnection2.setErrorHandler(getErrorHandler());	
	}
	return iConnection2;
  }
  
  protected abstract IConnection2 onDeepCopy();
  protected abstract IConnectionDescriptor doConnect(String url);  
  
  
  class ConnectThread extends Thread{    	  
    @Override
    public void run() {
      doConnect();
    }	  
  }
  
  
  
}
