package com.rokejits.android.tool.data.xml;

import java.util.Vector;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XmlAttributeHolder {
  private String tag;
  private Node node;
	
  public XmlAttributeHolder(String tag){
    this.tag = tag;	  
  }
  
  public final String getTag(){
    return tag;	  
  }
  
  public void setNode(Node node){
    this.node = node;	  
  }
  
  public Node getNode(){
   return node;	  
  }  
  
  public String getValue(){
    if(node != null){
      int type = node.getNodeType();
      switch(type){
        case Node.ELEMENT_NODE:
          return node.getChildNodes().item(0).getNodeValue();	
        case Node.TEXT_NODE:
          return node.getNodeValue();
      }
    }	  
    return null;
  }
  
  public NodeList getChildList(){
    if(node == null)
      return null;
    return node.getChildNodes();
  }
  
  public Node getChild(String tag){
    if(node == null)
      return null;
    NodeList childList = node.getChildNodes();
    for(int i = 0;i < childList.getLength();i++){
      Node child = childList.item(i);
      if(child.getNodeName().equals(tag)){
        return child;	  
      }
    }
    return null;
  }
  
  public int getChildIndex(String tag){      
    NodeList childList = node.getChildNodes();
    for(int i = 0;i < childList.getLength();i++){
      Node child = childList.item(i);
      if(child.getNodeName().equals(tag)){
        return i;	  
      }
    }
    return -1;
  }
  
  public boolean isHasChildList(){
    if(node == null)
      return false;
    return node.getChildNodes().getLength() > 0;
  }
  
  public boolean isHas(String tag){	
    if(node == null)
      return false;
    
    NodeList childList = node.getChildNodes();    
    for(int i = 0;i < childList.getLength();i++){
      Node child = childList.item(i);      
      if(child.getNodeName().equals(tag)){
        return true;	  
      }
    }
    return false;
  }
  
  public <T extends XmlAttributeHolder> Vector<T> extractChild(T dataHolder){
    XmlAttributeReader<T> reader = new XmlAttributeReader<T>();
    reader.setDataHolder(dataHolder);
    reader.extractXmlNode(getChildList());    
    return reader.getAllData();
  }
  
  public int getAttributeStringAsInt(String attrName){
    return getAttributeStringAsInt(attrName, -1);	  
  }
  
  public int getAttributeStringAsInt(String attrName, int defaultValue){
    String value = getAttributeString(attrName);
    if(value == null){
      return defaultValue;	
    }    
    return Integer.parseInt(value);
  }
  
  public String getAttributeString(String attrName){
    NamedNodeMap attrs = getAttribute();
    if(attrs == null)
      return null;
    Node item = attrs.getNamedItem(attrName);
    if(item == null)
      return null;
    return item.getNodeValue();    
  }
  
  private NamedNodeMap getAttribute(){
    if(node == null)
      return null;
    NamedNodeMap attrs = node.getAttributes();
    if(attrs == null)
      return null;
    return attrs;
  }
	
  public abstract XmlAttributeHolder getDataHolder();
}
