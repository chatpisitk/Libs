package com.rokejits.android.tool.cache;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.rokejits.android.tool.utils.FileUtil;
import com.rokejits.android.tool.utils.TimeUtils;

public class CacheManager {
  private static CacheManager self = null;
  private String cacheFolder;
  private boolean useCache = false;
  
  public static CacheManager getInstance(){
    if(self == null){
      self = new CacheManager();      
    }	  
    return self;
  }
  
  private CacheManager(){} 
  
  public void setCacheFolder(String cacheFolder){
    this.cacheFolder = cacheFolder;	  
  }
  
  public String getCacheFolder(){
    return cacheFolder;	  
  }
	
  public void useCache(boolean use){
    useCache = use;	  
  }
  
  public boolean isUseCache(){
    return useCache;	  
  }  
  
  public void clearExpireCache(){
    clearExpireCache(TimeUtils.DAY);	  
  }
  
  public void clearExpireCache(long expire){
    if(cacheFolder != null){
      File folder = new File(cacheFolder);
      if(folder.exists()){
        String[] cacheList = folder.list();
        if(cacheList != null){
          for(String cache : cacheList){
            File cacheFile = new File(cacheFolder, cache);
            DataInputStream dIn = null; 
            boolean needDel = false;
            try{
              dIn = new DataInputStream(new FileInputStream(cacheFile));
      	    
      		  long cacheTime = dIn.readLong();
      		  if(System.currentTimeMillis() > cacheTime + expire){
      			needDel = true;
      		  }
            	
            } catch (FileNotFoundException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
			} catch (IOException e) {
			  needDel = true;
			  e.printStackTrace();
			} finally {
              if(dIn != null)
				try {
					dIn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
      		if(needDel)
      		  FileUtil.deleteFile(cacheFile);      		
          }	
        }
      }
    }    
  }
  
  
  
}
