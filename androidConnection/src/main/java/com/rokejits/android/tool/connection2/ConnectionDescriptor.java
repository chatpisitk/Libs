package com.rokejits.android.tool.connection2;

import java.io.InputStream;

public class ConnectionDescriptor extends SetableConnectionDescriptor{
  private InputStream in;
  private long length = -1;
   
//  public ConnectionDescriptor(InputStream in, long length){
//    this.in = in;
//    this.length = length;
//  }
//   
  public ConnectionDescriptor(){}
  
  public ConnectionDescriptor(int errorCode, String error){
    setError(errorCode, error);   	
  }    
    
  @Override
  public long getLength(){
    return length;	
  }
    
  @Override
  public boolean isSuccess(){
    return in != null;	
  }
    
  @Override
  public InputStream getInputStream(){
    return in; 	
  }

  @Override
  public IConnectionDescriptor deepCopy() {
	return new ConnectionDescriptor();
  }

  
}
