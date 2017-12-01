package com.rokejits.android.tool.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CacheUtil {  
	
  public static final int saveCache(File cacheFile, InputStream in, long lifeTime){
    DataOutputStream dOut = null;   
	try {
  	  if(cacheFile.exists())
	    if(!cacheFile.delete())
	      return CacheConnection.DELETE_CACHE_FILE_ERROR;
	  cacheFile.getParentFile().mkdirs();
	  if(!cacheFile.createNewFile())
	    return CacheConnection.CREATE_CACHE_FILE_ERROR;	  
	  dOut = new DataOutputStream(new FileOutputStream(cacheFile));
	  dOut.writeLong(System.currentTimeMillis());
	  dOut.writeLong(lifeTime);
			
	  byte[] buffer = new byte[1024];
	  int chuck = -1;			
	  while((chuck = in.read(buffer)) != -1){
	    dOut.write(buffer, 0, chuck);	
	  }
	  in.close();
	} catch (FileNotFoundException e) {		
  	  e.printStackTrace();
  	  return CacheConnection.CACHE_FILE_ERROR;
	} catch (IOException e) {
  	  e.printStackTrace();
	}finally{
	  try {
	    if(dOut != null){		      
		  dOut.close();			  
	    }      		      
      } catch (IOException e) {
	    e.printStackTrace();
	  }	
	}
	return -1;
  }	
  
  public static final boolean isCacheExpire(String cacheFilePath){
    File cacheFile = new File(cacheFilePath);
    boolean isExpire = true;
    if(cacheFile.exists()){
      DataInputStream dIn = null;
      FileInputStream fIn = null;
      try {
		fIn = new FileInputStream(cacheFile);
		dIn = new DataInputStream(fIn);
		long cacheTime = dIn.readLong();
	    long lifeTime = dIn.readLong();
	    long currentTime = System.currentTimeMillis();
	    isExpire = currentTime > cacheTime + lifeTime;
	  } catch (FileNotFoundException e) {
		e.printStackTrace();
	  } catch (IOException e) {
		e.printStackTrace();
	  } finally {
		try {
	      if(dIn != null)			
		    dIn.close();			
	      if(fIn != null)
	        fIn.close();
		} catch (IOException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		}
	  }  	
    }
    
    return isExpire;
  }
  	
}
