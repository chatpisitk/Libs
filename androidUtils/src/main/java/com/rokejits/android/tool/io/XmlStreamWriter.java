package com.rokejits.android.tool.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class XmlStreamWriter {

  private OutputStream out;
  public XmlStreamWriter(OutputStream out){
    this.out = out;    
  }
  
  public void writeXml(Hashtable<String, String> xmlTable) throws IOException{
    XmlSerializer serializer = Xml.newSerializer();
	StringWriter writer = new StringWriter();
    Enumeration<String> keys = xmlTable.keys();
    
    try {
	  serializer.setOutput(writer);
	  serializer.startDocument("UTF-8", true);
	    
	  while(keys.hasMoreElements()){
	    String tag = keys.nextElement();
	    String value = xmlTable.get(tag);
	    writeTextTag(serializer, tag, value);  	
	  }	  
	  
	} catch (IllegalArgumentException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	} catch (IllegalStateException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	} catch (IOException e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	}  
    
    writeXml(writer.toString());
		  
  }

  private void writeTextTag(XmlSerializer serializer,String tagName, String text) throws IllegalArgumentException, IllegalStateException, IOException{
    serializer.startTag("", tagName);
    serializer.text(text);
    serializer.endTag("", tagName);
  }
  
  public void writeXml(String xmlString) throws IOException{
    out.write(xmlString.getBytes());	  
  }
  
  
  
}
