package com.rokejits.android.tool.utils.zip.component;

import java.io.InputStream;

public interface IZipBody {

  /**
   * ชื่อ
   */	
  public String getName();
  
  /**
   * ข้อมูล
   */
  public InputStream getEntity() throws Exception;
  
  /**
   * ขนาด
   */
  public long getLength();
}
