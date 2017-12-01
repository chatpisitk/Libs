package com.rokejits.android.tool.connection2;

import java.io.InputStream;

public abstract class SetableConnectionDescriptor implements ISetableConnectionDescriptor{  
  private int errorCode;
  private String error;
  private InputStream in;
  private long length = UNKNOWN_LENGTH;
  
  @Override
  public void setInputStream(InputStream in) {
    this.in = in;			
  }
  
  @Override
  public InputStream getInputStream() {
	return in;
  }
  
  @Override
  public boolean isSuccess() {
	return in != null;
  }
  
  @Override
  public void setError(int errorCode, String error) {
	this.errorCode = errorCode;
    this.error = error;		
  }
  
  @Override
  public String getError() {
	return error;
  } 
  
  @Override
  public int getErrorCode() {
	return errorCode;
  }

  protected void setLength(long length){
    this.length = length;	  
  }
  
  @Override
  public long getLength() {	
	return length;
  }  
}
