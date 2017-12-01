package com.rokejits.android.tool.connection2;

import java.util.Enumeration;
import java.util.Vector;

public class StopLoadManager {

  private Vector<StopLoadListener> stopLoadList;
  
  public StopLoadManager(){
    stopLoadList = new Vector<StopLoadListener>();	  
  }
  
  public void addStopLoadListener(StopLoadListener listener){
    if(listener == null || stopLoadList.contains(listener)){
      return;	
    }	  
    stopLoadList.add(listener);
  }
  
  public void removeStopLoadListener(StopLoadListener listener){
    stopLoadList.remove(listener);	  
  }
  
  public void removeAllStopLoadListener(){
    stopLoadList.removeAllElements();	  
  }
  
  public void stopLoadAll(){
    stopLoadAll(false);	  
  }  
  
  public void stopLoadAll(boolean removeAfterStop){ 
    Enumeration<StopLoadListener> e = stopLoadList.elements();
    while(e.hasMoreElements()){
      e.nextElement().stopLoadAll();	
    }
    if(removeAfterStop)
      removeAllStopLoadListener();
  }
}
