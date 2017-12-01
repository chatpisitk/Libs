package com.rokejits.android.tool.utils.zip.component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * ByteArrayZipBody
 * 
 * @param name ชื่อของไฟล์ใน zip หากต้องการเก็บเป็น folder ให้ใส่เป็น folder/file.ext
 * @param length ขนาด
 */
public class ByteArrayZipBody extends ZipBody{
  private byte[] data;
  public ByteArrayZipBody(String name, byte[] data) {
	super(name, data.length);	
	this.data = data;
  }

  @Override
  public InputStream getEntity() {
	return new ByteArrayInputStream(data);
  }  
 
}
