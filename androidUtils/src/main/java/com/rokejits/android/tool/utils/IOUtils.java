package com.rokejits.android.tool.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class IOUtils {

  public static String readStringFromInputStream(InputStream in){
    byte[] data = null;
    String result = null;
	if(in == null)
	  return null;
	data = readByteArrayFromInputStream(in);
	if(data == null)
	  return null;
	try {
	  result = new String(data,"UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return result;	
  }
 
	  
  public static byte[] readByteArrayFromInputStream(InputStream in){
    byte[] result = null;
	if(in == null)
	  return null;
	try{
	  ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	  byte[] data = new byte[1024];      
	  int chunk = 0;
	  while ( -1 != (chunk = in.read(data)) ) {
	    buffer.write(data, 0, chunk);
	  }
	  result = buffer.toByteArray();
    }catch(Exception e){
      System.out.println("read byte array from inputstream error :" +e.toString());
      result = null;
    }finally{
      try {
        in.close();
      } catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	  }	
    }
    return result;
  }
}
