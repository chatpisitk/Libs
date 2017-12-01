package com.rokejits.android.tool.io;

import java.io.IOException;
import java.io.InputStream;

public class XmlStreamReader {
  
  
  private InputStream is;
  public XmlStreamReader(InputStream is){
    this.is = is;	  
  }
  
  public void close(){	  
    try {
      if(is != null)
	    is.close();
      is = null;
	} catch (IOException e) {}
		
  }
  
  public String next() throws IOException{
    return next(false);	  
  }
  
  public String skip() throws IOException{
    return next(true);	  
  }
  
  private String next(boolean skip) throws IOException{
	if(is == null)
	  return null;
    StringBuffer buffer = new StringBuffer();    
    char c = (char) is.read();
    //DebugScreen.addLabel("c0 = "+c);
    while(c != '>' && c != -1){
     	
      buffer.append(c); 
      c = (char) is.read();
      /*try{
        Thread.sleep(50);	  
      }catch(Exception e){}*/
      //DebugScreen.addLabel("c = "+c);
    }
    //DebugScreen.addLabel("c2 = "+c);
    if(c != -1){
      String tag = buffer.append(c).toString();
      if(!skip){       
        if(tag.startsWith("<") && !tag.startsWith("</") && !tag.endsWith("/>")){
          int index = tag.indexOf(' ');
          if(index == -1)
            index = tag.indexOf('>');
          String tagName = tag.substring(1, index);
          buffer.append(findEndTag(tagName));
        }
      }
    	
    }else{
      is = null;	  
    }
    
    return buffer.toString();  	  
  }
  
  private String findEndTag(String tagName) throws IOException{
	
    StringBuffer buffer = new StringBuffer();
    String next;
    while(!(next = next()).endsWith("</"+tagName+">")){
      buffer.append(next);      
    }
    buffer.append(next);
    return buffer.toString();
  }
  
  
  
}
