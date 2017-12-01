package com.rokejits.android.tool.utils.zip.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileZipBody extends ZipBody{
  private File file;
  public FileZipBody(String fileName, String filePath) throws FileZipBodyExeption{
	this(fileName, new File(filePath));
  }
  
  public FileZipBody(String fileName, File file) throws FileZipBodyExeption{
    super(fileName, file.length());
    this.file = file;
    if(file.isDirectory())
      throw new FileZipBodyExeption("Not a file!!"); 	
  }

  @Override
  public InputStream getEntity() throws FileNotFoundException {	
	return new FileInputStream(file);
  }  
  
  public class FileZipBodyExeption extends Exception{

	public FileZipBodyExeption() {
	  super();		
	}

	public FileZipBodyExeption(String detailMessage, Throwable throwable) {
	  super(detailMessage, throwable);
	}

	public FileZipBodyExeption(String detailMessage) {
	  super(detailMessage);
	}

	public FileZipBodyExeption(Throwable throwable) {
	  super(throwable);
	}
    	  
  }

}
