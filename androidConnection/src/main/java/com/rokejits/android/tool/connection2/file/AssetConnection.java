package com.rokejits.android.tool.connection2.file;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.IConnectionDescriptor;
import com.rokejits.android.tool.connection2.SetableConnectionDescriptor;

public class AssetConnection extends FileConnection{
  private Context context;
  public AssetConnection(Context context, String url) {
	super(url);	
	this.context = context;	
  }

  @Override
  protected IConnectionDescriptor doConnect(String url) {
	IConnectionDescriptor conDesc = new AssetConnectionDescriptor(context, url);	
	return conDesc;
  }

  @Override
  protected IConnection2 onDeepCopy() {
    return new AssetConnection(context, getUrl());	
  }
  
  public class AssetConnectionDescriptor extends SetableConnectionDescriptor{
    
    
    
    public AssetConnectionDescriptor(Context context, String fileName) {
      try {
        AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(fileName);
        setLength(assetFileDescriptor.getLength());
        setInputStream(assetFileDescriptor.createInputStream());
      } catch (IOException e) {    		
    	try {
	  	  setInputStream(context.getAssets().open(fileName));
		} catch (IOException e1) {			
		  e1.printStackTrace();
		  setError(FILE_NOT_FOUND_ERROR, e1.toString());
		}
      }    	
    }

	@Override
	public IConnectionDescriptor deepCopy() {
	  return new AssetConnectionDescriptor(context, getUrl());
	}	
	  
  }
  
}
