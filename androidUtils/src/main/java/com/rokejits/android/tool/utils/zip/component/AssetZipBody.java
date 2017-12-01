package com.rokejits.android.tool.utils.zip.component;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class AssetZipBody implements IZipBody{
  private long length = 0;
  private String name;
  private Context context;
	
  public AssetZipBody(Context context, String name) throws IOException{  
    length = context.getResources().getAssets().openFd(name).getLength();	
    this.name = name;
    this.context = context;
  }

  @Override
  public InputStream getEntity() throws IOException {	
	return context.getResources().getAssets().open(name);
  }

  @Override
  public String getName() {
	return null;
  }

  @Override
  public long getLength() {
	
	return length;
  }

}
