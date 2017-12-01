package com.rokejits.android.tool.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import com.rokejits.android.tool.utils.StringUtils;


public abstract class DataHolder{
	
  //public static final String FEED_NO_DATA = "NO_DATA"; 
	
  private String[] keys;
  protected Object[] datas;
  private String tagName;
  //private String type;  
  
  public DataHolder(String tagName, String... keys){
    setKeys(keys);
    this.tagName = tagName;
    
    //this.type = type;
  }
  
  protected void injectKeys(String... superKeys){
    if(superKeys != null){
      String[] keys = getKeys();
	  ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(keys));
	  boolean keyChange = false;
	  for(String key : superKeys){
	    if(!keyList.contains(key)){
	      keyList.add(key);	
	      keyChange = true;
	    }	  
	  }
	  if(keyChange){
	    keys = new String[keyList.size()];
	    for(int i = 0;i < keyList.size();i++){
	      keys[i] = keyList.get(i);	
	    }
	    setKeys(keys);
	  }
    }
  }
  
  public JSONObject toJson() throws JSONException{
    JSONObject jsonObject = new JSONObject();   
	    
    String[] keys = getKeys();
    for(String key : keys){
      Object obj = get(key);
      if(obj != null)
	    jsonObject.put(key, obj);	  
    }       
    return jsonObject;
    
  }
	  
  public String toJsonString() throws JSONException{
	JSONObject jObj = toJson();
	if(jObj != null)
	  return jObj.toString();
    return null;	  
  }
  
  public abstract DataHolder initDataHolder();  
  
  public void setKeys(String[] keys){
    this.keys = keys;
    datas = new Object[keys.length];
  }
  
  public final String[] getKeys(){
    return keys;	   
  }
  
  public final String getTagName(){
    return tagName;	  
  }
  
  public void remove(String key){
    int index = getKeyIndex(key);
	if(index == -1)
	  return;
	datas[index] = null;
  }
  
  /*public final String getType(){
    return type;	  
  }*/
  
  public final boolean isContain(String key){    	  
    return getKeyIndex(key) >= 0;
  }
  
  public final void put(String key,Object data){
	if(data == null)
	  return;
	int index = getKeyIndex(key);
	if(index == -1)
	  return;
	datas[index] = data;		
  }
  
  public final void put(String key,boolean value){
    put(key,new Boolean(value));	  
  }
  
  public final void put(String key,int data){
    put(key, new Integer(data));	  
  }
  
  public final void put(String key,long data){
    put(key, new Long(data));	  
  }
	  
	  
  public String getString(String key){  
	Object result = get(key);
	if(result instanceof String)
	  return (String) result;
    return null;
  }
  
  public final String getStringNotNull(String key){  
    String result = getString(key);	 
    if(result == null)
      result = "";
    return result;
  }  
  
  public final int getInt(String key, int defaultValue){
	if(get(key) == null)
	  return defaultValue;
    return ((Integer)get(key)).intValue();	  
  }
  
  public final long getLong(String key, long defValue){
	if(get(key) == null)
	  return defValue;
    return ((Long)get(key)).longValue();	  
  }
  
  public final double getDouble(String key, double defValue){
    if(get(key) == null)
      return defValue;
    return ((Double)get(key)).doubleValue();	  
  }
  
  public final float getFloat(String key, float defValue){
	if(get(key) == null)
	  return defValue;  
    return ((Float)get(key)).floatValue();	  
  }
  
  public final Float getStringAsFloat(String key){
    return getStringAsFloat(key, -1);	  
  }
  
  public final Float getStringAsFloat(String key, float defaultValue){
    String value = getString(key);
    if(value == null || value.length() == 0){
      return defaultValue;	
    }
    return Float.parseFloat(value);
  }
  
  public final Double getStringAsDouble(String key){
    return getStringAsDouble(key, -1);	  
  }
  
  public final Double getStringAsDouble(String key, double defaultValue){  
    String value = getString(key);
    if(value == null)
      return defaultValue;
	return Double.parseDouble(value);
  }
  
  public final long getStringAsLong(String key){  
    String value = getString(key);
    return Long.parseLong(value);
  }
  
  public final int getStringAsInt(String key){
    return getStringAsInt(key, -1);	  
  }
  
  public final int getStringAsInt(String key, int defaultValue){  
    String value = getString(key);
    if(value == null || value.length() == 0)
      return defaultValue;
    return Integer.parseInt(value);
 }
  
  public final boolean getBoolean(String key){	
	if(get(key) == null)
	  return false;
    return ((Boolean)get(key)).booleanValue();	  
  }
	  
  public final Object get(String key){
	int index = getKeyIndex(key);
	if(index == -1)
	  return null;
    return datas[index];	  
  }
	   

  protected int getKeyIndex(String key){	
    return StringUtils.getStingIndexInArray(key, keys);	  
  } 
  
  public void writeDataToIntent(Intent intent){
    for(int i = 0;i < keys.length;i++){
      String key = keys[i];
      Object obj = get(key);
      if(obj instanceof String){
        intent.putExtra(key, (String)obj);	  
      }else if(obj instanceof Integer){
        intent.putExtra(key, (Integer)obj);	  
      }else if(obj instanceof Long){
        intent.putExtra(key, (Long)obj);	  
      }else if(obj instanceof Double){
        intent.putExtra(key, (Double)obj);	  
      }else if(obj instanceof Float){
        intent.putExtra(key, (Float)obj);	  
      }else if(obj instanceof Boolean){
        intent.putExtra(key, (Boolean)obj);	  
      }      
    }	  
  }
  
  public void readDataFromIntent(Intent intent){
    for(int i = 0;i < keys.length;i++){
      String key = keys[i];
      if(intent.getExtras().containsKey(key)){
        put(key, intent.getExtras().get(key));	  
      }
    }	  
  }
  
  public <T extends DataHolder> Vector<T> extractChild(String childTag, String jsonTag, T dataHolder){
    return extractChildObject(jsonTag, get(childTag), dataHolder);    	  
  }  
  
  public <T extends DataHolder> Vector<T> extractChildObject(String jsonTag, Object obj, T dataHolder){
    if(obj == null || !(obj instanceof JSONObject || obj instanceof JSONArray))
      return null;
	JSONObject jsonObject = new JSONObject();
    try {
	  jsonObject.put(jsonTag, obj);
	} catch (JSONException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	}    
    return extractChildJSon(jsonObject, dataHolder);
		  
  }
  
  public <T extends DataHolder> Vector<T> extractChildJSon(JSONObject jsonObject, T dataHolder){
    DataReaderOption dOption = new DataReaderOption();
    dOption.setDataHolder(dataHolder);
    DataReader<T> reader = new DataReader<T>(dOption);
	reader.extractJSONObject(jsonObject, null);
	
	return reader.getAllData();
	  
  }
  
}
