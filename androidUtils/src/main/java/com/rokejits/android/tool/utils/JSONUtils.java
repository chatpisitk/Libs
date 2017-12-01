package com.rokejits.android.tool.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.rokejits.android.tool.data.DataHolder;

public class JSONUtils {


  public JSONArray parseToJSONArray(Object object){
    if(object != null && (object instanceof JSONObject || object instanceof JSONArray)){
      if(object instanceof JSONArray){
        return (JSONArray) object;	  
      }else{
       JSONArray jsonArray = new JSONArray();
       
       jsonArray.put(object);
       
       return jsonArray;
      }	
    }	  
    
    return null;
  }	
  	
//  public static final void readJsonObjectToDataHolder(JSONObject json, DataHolder dHolder) throws JSONException{
//    String[] keys = dHolder.getKeys();
//    for(int i  = 0;i < keys.length;i++){
//      String key = keys[i];
//      if(json.has(key)){
//        dHolder.put(key, json.get(key));	  
//      }	
//    }
//  }	
}
