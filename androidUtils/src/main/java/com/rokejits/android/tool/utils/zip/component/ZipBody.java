package com.rokejits.android.tool.utils.zip.component;
/**
 * Zip body
 * 
 * @param name ชื่อของไฟล์ใน zip หากต้องการเก็บเป็น folder ให้ใส่เป็น folder/file.ext
 * @param length ขนาด
 */
public abstract class ZipBody implements IZipBody{
  private String name;
  private long length;
  
  public ZipBody(String name, long length){
    this.name = name;
    this.length = length;
  }  
  
  @Override  
  public String getName() {
	return name;
  }
  
  @Override
  public long getLength() {
	return length;
  }
  
}
