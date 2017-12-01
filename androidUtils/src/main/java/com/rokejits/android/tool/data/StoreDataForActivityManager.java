package com.rokejits.android.tool.data;

import java.util.Hashtable;

public class StoreDataForActivityManager {

  private static StoreDataForActivityManager self;
  private Hashtable<Integer, Object> storeData;
  
  public static StoreDataForActivityManager getInstance(){
    if(self == null){
      self = new StoreDataForActivityManager(); 	
    }    
    return self;
  }
  
  private StoreDataForActivityManager(){
    storeData = new Hashtable<Integer, Object>();    		
  }
  
  public void put(int id, Object data){
	if(data == null)
	  return;
    storeData.put(id, data);	  
  }
  
  public Object get(int id){
    return storeData.get(id);	  
  }
  
  public void remove(int id){
    storeData.remove(id);	  
  }
  
  public void clear(){
    storeData.clear();	  
  }
  
  public int getStoreSize(){
    return storeData.size();	  
  }
  
}
