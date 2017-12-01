package com.rokejits.android.tool.data.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;


public class XmlAttributeReader<T extends XmlAttributeHolder>{

  private XmlAttributeReaderListener<T> listener;
  private T dataHolder;
  private Vector<T> dataHolderList;  
  
  public XmlAttributeReader(){
    dataHolderList = new Vector<T>();
  }  
  
  public void setDataHolder(T dataHolder){
    this.dataHolder = dataHolder;	  
  }
  
  public void setDataReaderListener(XmlAttributeReaderListener<T> listener){
    this.listener = listener;	  
  }
  
  public int getDataSize(){
    return dataHolderList.size();	  
  }
  
  public T getData(int index){
    return dataHolderList.get(index);	  
  }
  
  public Vector<T> getAllData(){
    return dataHolderList;	  
  }  
  
  private void updateError(int errorCode, String error){	
    if(listener != null)
      listener.onReadFailed(errorCode, error);
  }
  
  private void updateSuccess(){	
    if(listener != null)
      listener.onReadSuccess(this);
  }  
  
  public void readAsset(Context context, String filePath){
    try {
	  read(context.getAssets().open(filePath));
	} catch (IOException e) {
	  e.printStackTrace();
	}	  
  }
  
  public void read(File file){
    try {
	  read(new FileInputStream(file));
	} catch (FileNotFoundException e) {
	  e.printStackTrace();
	}	  
  }
  
  public void read(InputStream in) {    
	try {
	  extractXml(in);
	  updateSuccess();
	} catch (ParserConfigurationException e) {
	  updateError(XmlAttributeReaderListener.PARSER_CONFIGURATION_ERROR, e.toString());
	  e.printStackTrace();
	} catch (SAXException e) {
	  updateError(XmlAttributeReaderListener.SAX_ERROR, e.toString());
	  e.printStackTrace();
	} catch (IOException e) {
	  updateError(XmlAttributeReaderListener.IO_ERROR, e.toString());
	  e.printStackTrace();
	}
			
  }
  
  private void extractXml(InputStream in) throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder;
	docBuilder = docBuilderFactory.newDocumentBuilder();
	docBuilder.isValidating();
	Document xmlDoc = docBuilder.parse(in);
	xmlDoc.getDocumentElement().normalize();
	extractXmlNode(xmlDoc.getChildNodes());	
  }  
  
  public void extractXmlNode(NodeList list){
    for(int i = 0;i < list.getLength();i++){
      Node childNode = list.item(i);
      String childNodeName = childNode.getNodeName();      
      if(dataHolder != null && childNodeName.equals(dataHolder.getTag())){
        XmlAttributeHolder newHolder = dataHolder.getDataHolder();
        newHolder.setNode(childNode);
        dataHolderList.add((T) newHolder);
      }else{      
        NodeList childNodeList = childNode.getChildNodes();      
        extractXmlNode(childNodeList);
      }
      
    }
  } 
}
