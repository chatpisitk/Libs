package com.rokejits.android.tool.connection2;

import java.io.InputStream;

public interface IConnectionDescriptor {
  public static final long UNKNOWN_LENGTH		= -1;	
	
	
  public long getLength();	    
  public boolean isSuccess();
  public InputStream getInputStream();	    
  public int getErrorCode();	    
  public String getError();
  public IConnectionDescriptor deepCopy();
  
}
