package com.rokejits.android.tool.data;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.connection2.ConnectionListener;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.StopLoadListener;
import com.rokejits.android.tool.connection2.handler.ErrorHandler;
import com.rokejits.android.tool.os.UIHandler;
import com.rokejits.android.tool.utils.IOUtils;


public class DataReader<T extends DataHolder> implements ConnectionListener, StopLoadListener{
  
  
  public static final int FORMAT_XML  = 0;
  public static final int FORMAT_JSON = 1;
  
  public static final int READ_OK    = 200;
  public static final int READ_ERROR = -1;
  
  
  private DataReaderListener<T> listener;  
  private int readCode, errorCode;
  private String error;
  private Hashtable<String, String> table = new Hashtable<String, String>();
  private Vector<T> data = new Vector<T>();  
  private DataReaderOption dOption;  
  private ErrorHandler errorHandler;
  
  public DataReader(){
    this(new DataReaderOption());	  	  
  }
  
  public DataReader(DataReaderOption dOption){
    this.dOption = dOption;	  
  }  
  
  public DataReaderOption getDataReaderOption(){
    return dOption;	  
  }
  
  public void setErrorHandler(ErrorHandler errorHandler){
    this.errorHandler = errorHandler;	  
  }
  
  public void read(){
	IConnection2 conn = dOption.getConnection();
    conn.setConnectionListener(this);
    if(conn != null)
      conn.connect();    	  
  } 
  
  public void stopRead(){
	IConnection2 conn = dOption.getConnection();	
	if(conn != null)
	  conn.stopConnect();	  
  }
  
  public int getDataSize(){
    return data.size();
  }
  
  public Vector<T> getAllData(){
    if(data.size() == 0)
      return null;    
    return data;
  }
  
  /*public String getType(){
    return dataHolder.getType();	  
  }*/
  
  public T getData(int index){
    return (T) data.elementAt(index);	  
  }
  
  public String getTableData(String key){
    return table.get(key);	  
  }
  
  public Object getTable(String key){
    return table.get(key);	  
  }
  
  public boolean isTableDataContainKey(String key){
    return table.containsKey(key);	  
  }
  
  public void setListener(DataReaderListener<T> listener){
    this.listener = listener;	  
  }
  
  public void readFromInputStream(InputStream in){
    onConnected(null, in);	  
  } 
 
  public String getResponse(){
    return response;	  
  }
  
  private JSONObject jObj;
  private String response;
  public void extractJSON(DataHolder holder){
    dOption.setDataHolder(holder);
    data = new Vector<T>();
    extractJSONObject(jObj, null);
  }
  private void extractJSON(InputStream in) throws Exception{
	//Log.d("extractJSON");
	response = IOUtils.readStringFromInputStream(in);
	Log.d("extractJSON str = "+response);	
	if(response == null)
	  throw new Exception("null");
    jObj = new JSONObject(response);
    Log.d("jObj = "+jObj);
    extractJSONObject(jObj, null);
   
  }
  
  private void extractJSONDataNode(JSONArray jArray){
    for(int i = 0;i < jArray.length();i++){
      T holder = (T) dOption.getDataHolder().initDataHolder();
      try {
		JSONObject jObj = jArray.getJSONObject(i);
		extractJSONObject(jObj, holder);
	  } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
	  data.addElement(holder);
    }      
  }
  
  public void extractJSONObject(JSONObject jObj,DataHolder holder){
	  
    Iterator keys = jObj.keys();
    while(keys.hasNext()){
      String key = (String) keys.next(); 
      Log.d("key = "+key);
      Log.d("dOption.getDataHolder() = "+dOption.getDataHolder());
      
      try {
        if(dOption.getDataHolder() != null && holder == null && key.equals(dOption.getDataHolder().getTagName())){
          Log.d("extractJSONDataNode");
    	  Object obj = jObj.get(key);
    	  if(obj instanceof JSONArray){
    	    extractJSONDataNode(jObj.getJSONArray(key));	  
    	  }else if(obj instanceof JSONObject){
    	    Vector<JSONObject> v = new Vector<JSONObject>();
    	    v.add((JSONObject) obj);
    	    JSONArray jArray = new JSONArray(v);
    	    extractJSONDataNode(jArray);
    	  }
    	 
    	}else{  
		  Object obj = jObj.get(key);
		  extractJObject(key, obj, holder);
    	}  
		
		
	  } catch (JSONException e) {		
		e.printStackTrace();
	  }
    }
  }
  
  private void extractJObject(String key, Object obj, DataHolder holder){
    if(holder != null)
	  if(holder.isContain(key)){
	    putDataNode(key,obj,holder);
	    return;
	  }
		    
    if(obj instanceof JSONObject)
      extractJSONObject((JSONObject)obj, holder);
	else if(obj instanceof JSONArray)
	  extractJSONArray((JSONArray)obj, holder);
	else{
	  putDataNode(key,obj,holder);  	
	}	  
  }
  
  private void putDataNode(String key,Object o,DataHolder holder){
	//Log.d("put Data node = "+holder);
	if(holder != null)
	  if(!holder.isContain(key))
	    return;
	
    String put = null;
    if (o instanceof Byte) {
      put = ""+((Byte)o).byteValue();
    } else if (o instanceof Short) {
      put = ""+((Short)o).shortValue();
    } else if (o instanceof Integer) {
      put = ""+((Integer)o).intValue();
    } else if (o instanceof Long) {
      put = ""+((Long)o).longValue();
    } else if (o instanceof Float) {
      put = ""+((Float)o).floatValue();
    } else if (o instanceof Double) {
      put = ""+((Double)o).doubleValue();
    } else if (o instanceof String){
      put = (String) o; 	
    } else if (o instanceof Boolean){
      put = ""+((Boolean) o).booleanValue(); 	
    }
    Log.d("put Data node2 = "+key+"/"+put+"/"+holder);
    if(holder == null ){
      if(put != null)
        table.put(key, put);
    }else{
      if(put != null)
        holder.put(key, put);
      else
    	holder.put(key, o);  
    }
  }
  
  private void extractJSONArray(JSONArray jArray,DataHolder holder){
    for(int i = 0;i < jArray.length();i++){	     
	  try {
	    JSONObject jObj = jArray.getJSONObject(i);
		extractJSONObject(jObj, holder);
	  } catch (JSONException e) {
	    // TODO Auto-generated catch block
		e.printStackTrace();
	  }
    }	  
  }
  
  
  
  private void setError(int errorCode, String error){
	this.errorCode = errorCode;
    readCode = READ_ERROR;    
    if(errorHandler != null){
      error = errorHandler.handlerError(errorCode, error);	
    }    
    this.error = error;
  }
  
  public int getReadCode(){
    return readCode;	  
  }
  
  public int getErrorCode(){
    return errorCode;	  
  }
  
  public String getError(){
    return error;	  
  }
  private Document xmlDoc;
  public void extractXml(DataHolder holder){
    dOption.setDataHolder(holder);
    data = new Vector<T>();
    extractXmlNode(xmlDoc.getChildNodes());
  }
  
  private void extractXml(InputStream in) throws Exception{
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder;
	docBuilder = docBuilderFactory.newDocumentBuilder();
	docBuilder.isValidating();
	xmlDoc = docBuilder.parse(in);
	xmlDoc.getDocumentElement().normalize();
	extractXmlNode(xmlDoc.getChildNodes());	
  }
  
  public void extractXmlNode(NodeList list){
    for(int i = 0;i < list.getLength();i++){
      Node node = list.item(i);	
      String nodeName = node.getNodeName();
      //int nodeType = node.getNodeType();
      //Log.d("nodeName = "+nodeName);
      if(dOption.getDataHolder() != null && nodeName.equals(dOption.getDataHolder().getTagName())){
    	T holder = (T) dOption.getDataHolder().initDataHolder();
    	extractDataNode(node.getChildNodes(),holder);  
    	data.addElement(holder);
      }else{    	 
        NodeList childList = node.getChildNodes();
        
        if(childList.getLength() == 1){
          Node child = childList.item(0);
          int childType = child.getNodeType();
         
          if(childType == Node.TEXT_NODE){
        	//Log.d("table node value = "+child.getNodeValue());
            table.put(nodeName, child.getNodeValue());
          }else if(childType == Node.ELEMENT_NODE)
        	extractXmlNode(childList);	
        }else if(childList.getLength() > 1){
          extractXmlNode(childList);	
        }
      }
      
    }	  
  }
  
  private void extractDataNode(NodeList list,DataHolder holder){
    //DataNode xmlNode = new DataNode(dataHolder.getKeys(),dataHolder.getType());
    for(int i = 0;i < list.getLength();i++){
      Node node = list.item(i);	
      String nodeName = node.getNodeName();
      //int nodeType = node.getNodeType();
      //Log.d("data nodeName "+nodeName);
      if(holder.isContain(nodeName)){
    	  NodeList childList = node.getChildNodes();   
    	  //Log.d("childList.getLength() = "+childList.getLength());
          if(childList.getLength() == 1){
            Node child = childList.item(0);
            int childType = child.getNodeType();
            //Log.d("childType = "+childType);
            //Log.d("node.ELEMENT_NODE = "+node.ELEMENT_NODE);
           // Log.d("node.TEXT_NODE = "+node.TEXT_NODE);
            if(childType == Node.TEXT_NODE){
              String nodeValue = getNodeValue(node);
              //Log.d("data nodeValue "+nodeValue);
              holder.put(nodeName, nodeValue);
            }else if(childType == Node.ELEMENT_NODE){
              holder.put(nodeName, node);
            }else if(childType == Node.CDATA_SECTION_NODE){
              String nodeValue = getNodeValue(node);
             // Log.d("data nodeValue "+nodeValue);
              holder.put(nodeName, nodeValue);	
            }
          }else if(childList.getLength() > 1){
            holder.put(nodeName, node);	
          }  
    	/*int type = node.getNodeType();
    	if(type == node.TEXT_NODE){
    	  String nodeValue = getNodeValue(node);
    	  System.out.println("data nodeValue "+nodeValue);
    	  dataNode.put(nodeName, nodeValue);
    	}else if(type == node.ELEMENT_NODE){
    	  dataNode.put(nodeName, node);	
    	}*/
      }else{
        NodeList childList = node.getChildNodes();   
        //System.out.println("childList.getLength() = "+childList.getLength());
        if(childList.getLength() == 1){
          Node child = childList.item(0);
          int childType = child.getNodeType();
         /* System.out.println("childType = "+childType);
          System.out.println("node.ELEMENT_NODE = "+node.ELEMENT_NODE);
          System.out.println("node.TEXT_NODE = "+node.TEXT_NODE);*/
          if(childType == Node.ELEMENT_NODE)
          	extractDataNode(childList,holder);	
        }else if(childList.getLength() > 1){
          extractDataNode(childList,holder);	
        }
      }
    }
   
  }
  
  private String getNodeValue(Node node){
    NodeList list = node.getChildNodes();
    if(list.getLength() != 1)
      return null;
    return list.item(0).getNodeValue();
  } 
  
  private void updateListener(){
	new UIHandler().post(new Runnable() {
		
	  @Override
	  public void run() {
	    if(listener != null)
	      listener.onFinish(DataReader.this);
	  }
	});
    
  }
  
  @Override
  public void onConnected(IConnection2 iConnection2, InputStream in) {
    try{
	  readCode = READ_ERROR;      
      if(dOption.getFormat() == FORMAT_XML)
        extractXml(in);
      else
    	extractJSON(in);
      readCode = READ_OK;
	} catch (Exception e) {
	  setError(DataReaderListener.EXTRACT_DATA_ERROR, "Read error : "+e.toString());
	}finally{
	  updateListener();	
	}
  	
  }

  @Override
  public void onConnectFailed(IConnection2 iConnection2, int errorCode, String error) {
    readCode = READ_ERROR;    
	setError(errorCode, error);
	updateListener();
  	
  }

  @Override
  public void stopLoadAll() {
    stopRead();	
  }
  
  /*public void printTableData(){
    Enumeration<String> keys = table.keys();
    while(keys.hasMoreElements()){
      String key = keys.nextElement();
      Log.d("Table key = |"+key +"|");	
      Log.d("Table value = "+table.get(key));     
    }
  }*/

  
  



  
  
}
