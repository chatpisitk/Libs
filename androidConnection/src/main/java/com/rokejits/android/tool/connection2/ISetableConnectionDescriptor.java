package com.rokejits.android.tool.connection2;

import java.io.InputStream;

public interface ISetableConnectionDescriptor extends IConnectionDescriptor{

  public void setError(int errorCode, String error);
  public void setInputStream(InputStream in);
}
