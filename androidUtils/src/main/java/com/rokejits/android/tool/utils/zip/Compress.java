package com.rokejits.android.tool.utils.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.rokejits.android.tool.utils.zip.component.ByteArrayZipBody;
import com.rokejits.android.tool.utils.zip.component.IZipBody;
import com.rokejits.android.tool.utils.zip.component.ZipBody;

public class Compress extends AsyncTask<Void, Long, Boolean>{
  private static final int BUFFER = 2048; 
  
  private String _zipFile;
  private Vector<IZipBody> bodyList;
  private OnCompressListener compressListener;
  
  public Compress(String zipFile, IZipBody body){
    this._zipFile = zipFile;
    addBody(body); 
    ByteArrayZipBody bBody;
  }
  
  public void addBody(IZipBody body){
    if(bodyList == null)
      bodyList = new Vector<IZipBody>();
    bodyList.add(body);
  } 
  
  public void setCompressListener(OnCompressListener compressListener) {
	this.compressListener = compressListener;
  }
  
  @Override
  protected void onPreExecute() {
    long maxLength = 0;
    for(IZipBody body : bodyList){
      maxLength = maxLength + body.getLength();  	  
    }
    if(compressListener != null)
      compressListener.onInitialCompressing(maxLength);
  }
  
  @Override
  protected Boolean doInBackground(Void... params) {
    try  { 
      BufferedInputStream origin = null; 
      FileOutputStream dest = new FileOutputStream(_zipFile); 
	 
      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest)); 
 
      byte data[] = new byte[BUFFER]; 
	      
      long progress = 0;
	      
	      
      for(IZipBody body : bodyList){
    	Log.v("Compress", "Adding: " + body.getName());
        
        origin = new BufferedInputStream(body.getEntity(), BUFFER); 
        ZipEntry entry = new ZipEntry(body.getName()); 
        out.putNextEntry(entry); 
        int count; 
        while ((count = origin.read(data, 0, BUFFER)) != -1) { 
          out.write(data, 0, count); 
          progress = progress + count;
          if(compressListener != null)
            compressListener.onCompressing(progress);
        } 
        origin.close(); 
      }
      if(compressListener != null)
        compressListener.onCompressCompleted();     
      out.close(); 
    } catch(Exception e) { 
      e.printStackTrace(); 
      if(compressListener != null)
        compressListener.onCompressFailed(OnCompressListener.COMPRESS_ERROR, e.toString());
    } 
    return true;	  
  }
  
  public interface OnCompressListener{  
	public static final int COMPRESS_ERROR = 0xffffd1;  
	  
	public void onInitialCompressing(long maxLength);
    public void onCompressing(long progress);
    public void onCompressCompleted();	  
    public void onCompressFailed(int errorCode, String error);
  }
	
}
