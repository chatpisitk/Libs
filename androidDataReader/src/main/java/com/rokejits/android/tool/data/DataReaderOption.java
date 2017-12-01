package com.rokejits.android.tool.data;

import java.util.Hashtable;

import android.content.Context;

import com.rokejits.android.tool.connection2.Connection2;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.internet.InternetConnection;
import com.rokejits.android.tool.connection2.internet.httpclient.HttpGetConnection;
import com.rokejits.android.tool.connection2.internet.httpclient.HttpPostConnection;
import com.rokejits.android.tool.connection2.internet.httpclient.HttpPostMultiPartConnection;

public class DataReaderOption{
  private IConnection2 conn;
  private DataHolder dataHolder;
  private int format = DataReader.FORMAT_JSON;
	
  public DataReaderOption(){
	  
  }
  
  public DataReaderOption(Context context, String url){
    this(new HttpGetConnection(context, url));	
  }
    
  public DataReaderOption(Context context, String url, Hashtable<String, String> param){
    this(new HttpPostConnection(context, url, param));	
  }
    
  public DataReaderOption(Context context, String url, Hashtable<String, String> param, String name, String paramName, byte[] data){
    this(new HttpPostMultiPartConnection(context, url, param, name, paramName, data));
  }   
    
  public DataReaderOption(IConnection2 conn){
    this.conn = conn;	
  }
    
  public DataReaderOption setDataHolder(DataHolder dataHolder){
    this.dataHolder = dataHolder;
    return this;
  }
  
  public DataReaderOption setTimeout(int timeout){
	if(conn != null && conn instanceof InternetConnection)
	  ((InternetConnection)conn).setTimeout(timeout);
    return this;	  
  }
  
  
  public DataReaderOption setFormat(int format){
    this.format = format;
    return this;
  }
  
    
  public DataHolder getDataHolder(){
    return dataHolder;	
  }
    
  public int getFormat(){
    return format;	
  }
    
  public IConnection2 getConnection(){
    return conn;	
  }
    
    
}
