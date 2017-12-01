package com.rokejits.android.tool.connection2.internet;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

public interface IHttpHeader {
  public static final String CONTENT_TYPE							= HTTP.CONTENT_TYPE;
  public static final String CONTENT_LENGTH							= HTTP.CONTENT_LEN;
  public static final String IF_MODIFIED_SINCE						= "If-Modified-Since";
  public static final String LAST_MODIFIED							= "Last-Modified ";  
  
  public static final String CONTENT_TYPE_APPLICATION_FORM			= URLEncodedUtils.CONTENT_TYPE;
  public static final String CONTENT_TYPE_APPLICATION_JSON			= "application/json";
  public static final String CONTENT_TYPE_OCTET_STREAM				= HTTP.OCTET_STREAM_TYPE;
  public static final String CONTENT_TYPE_PLAIN_TEXT				= HTTP.PLAIN_TEXT_TYPE;
  
  
  
}
