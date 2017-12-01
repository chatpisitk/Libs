package com.rokejits.android.tool.connection2;

import java.io.InputStream;

public interface ConnectionListener{		
  public void onConnected(IConnection2 iConnection2, InputStream in);	  
  public void onConnectFailed(IConnection2 iConnection2, int errorCode, String error);
}
