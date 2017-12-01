package com.rokejits.android.tool.data;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataUtils {
  
  public static <T extends DataHolder> Vector<T> extractJson(Object obj, String tag, T dataHolder){
	if(obj == null || (!(obj instanceof JSONArray) && !(obj instanceof JSONObject)))
	  return null;
    DataReaderOption dOption = new DataReaderOption();
    dOption.setDataHolder(dataHolder);	    
    JSONObject jObj = new JSONObject();	    
    try {
	  jObj.put(tag, obj);
	  DataReader<T> reader = new DataReader<T>(dOption);
	  reader.extractJSONObject(jObj, null);
	  return reader.getAllData();
	} catch (JSONException e) {
	  e.printStackTrace();
	}    
    return null;   
  } 
  
}
