package com.rokejits.android.tool.data.xml;

public interface XmlAttributeReaderListener<T extends XmlAttributeHolder>{
  public static final int PARSER_CONFIGURATION_ERROR = 0xffeed0;	
  public static final int SAX_ERROR 				 = PARSER_CONFIGURATION_ERROR + 1;
  public static final int IO_ERROR 					 = SAX_ERROR + 1;
	
	
  public void onReadFailed(int errorCode, String error);	  
  public void onReadSuccess(XmlAttributeReader<T> reader);
}
