package com.rokejits.android.tool.connection2.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.rokejits.android.tool.connection2.Connection2;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.IConnectionDescriptor;
import com.rokejits.android.tool.connection2.SetableConnectionDescriptor;

public class FileConnection extends Connection2{
  public static final int FILE_NOT_FOUND_ERROR = 0xeffff0;  
  public FileConnection(String url) {
	super(url);
  }

  @Override
  protected IConnectionDescriptor doConnect(String url) {
	IConnectionDescriptor conDesc = new FileConnectionDescriptor(url);
	return conDesc;
  }  
  
  @Override
  protected IConnection2 onDeepCopy() {
	return new FileConnection(getUrl());
  }
  
  public class FileConnectionDescriptor extends SetableConnectionDescriptor{
    private File file;    
    
    public FileConnectionDescriptor(String path) {
	  file = new File(path);
	  try {
		setLength(file.length());
	 	setInputStream(new FileInputStream(file));
	  } catch (FileNotFoundException e) {
		//e.printStackTrace();
		setError(FILE_NOT_FOUND_ERROR, e.toString());
	  }
	}
    
    public File getFile(){
      return file;	
    }

	@Override
	public IConnectionDescriptor deepCopy() {
	  return new FileConnectionDescriptor(getUrl());
	}
	  
	
	  
  }

}
