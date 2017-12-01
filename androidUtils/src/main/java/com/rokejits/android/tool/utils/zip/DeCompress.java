package com.rokejits.android.tool.utils.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.os.AsyncTask;

import com.rokejits.android.tool.Log;

public class DeCompress extends AsyncTask<Void, Long, Boolean>{

  private String zipFile, destinationPath;
  private OnDeCompressListener onDeCompressListener;
  
  
  public DeCompress(String zipFile, String destinationPath){
    this.zipFile = zipFile;
    this.destinationPath = destinationPath;
  }
  
  public void setOnDeCompressListener(OnDeCompressListener onDeCompressListener) {
	this.onDeCompressListener = onDeCompressListener;
  }
  
  private void updateError(int errorCode, String error){
    if(onDeCompressListener != null){
      onDeCompressListener.onDeCompressFailed(errorCode, error);	
    }	  
  } 
  
  @Override
  protected void onPreExecute() {  
    ZipFile zFile;
	try {
	  zFile = new ZipFile(zipFile);	  
	  Enumeration zipEntities = zFile.entries();
	  long maxLength = 0;
	  while(zipEntities.hasMoreElements()){
	    ZipEntry zipEntry = (ZipEntry) zipEntities.nextElement();
		   
	    long uncompressedSizeOfEntry = zipEntry.getSize();
	    maxLength = maxLength + uncompressedSizeOfEntry;
	  }  
		  
		  
	  if(onDeCompressListener != null)
	    onDeCompressListener.onInitialDeCompressing(maxLength);
	} catch (IOException e) {
	  e.printStackTrace();
	  cancel(true);
	  updateError(OnDeCompressListener.DECOMPRESS_ERROR, "on Initial error : " + e.toString());
	}
     
	
  }
	
  @Override
  protected void onProgressUpdate(Long... values) { 
    if(onDeCompressListener != null)
      onDeCompressListener.onDeCompressing(values[0]);
  }
  
  @Override
  protected Boolean doInBackground(Void... voids) {
    ZipFile zFile = null;
       
    try { 
      zFile = new ZipFile(zipFile);
      long progress = 0;
      Enumeration zipEntities = zFile.entries();
      while (zipEntities.hasMoreElements()) {
    	ZipEntry zipEntry = (ZipEntry) zipEntities.nextElement();
        Log.d("Unzipping " + zipEntry.getName());    
        if(zipEntry.isDirectory()) { 
          _dirChecker(zipEntry.getName()); 
        } else { 
          String path[] = zipEntry.getName().split("/");
          if(path.length > 1){
            for(int i = 0; i < path.length - 1; i++){
              String pathToCreat = "";
        	  for(int j = 0; j < i; j++){
        	    pathToCreat += path [j] + "/";
        	  }
        	  pathToCreat += path[i];
        	  _dirChecker(pathToCreat);
            }
          }        	
          
          FileOutputStream fout = new FileOutputStream(new File(destinationPath, zipEntry.getName())); 
          InputStream zIn = zFile.getInputStream(zipEntry);
          int c=0;       
          byte[] buf = new byte[1024];
          
          while ((c = zIn.read(buf)) != -1) {   
          
            fout.write(buf, 0, c); 
          
            progress = c + progress;
          
            publishProgress(progress);
           
          } 
          zIn.close();
          fout.close(); 
         
        } 
         
       
      }    	
	} catch (Exception e) {
	  updateError(OnDeCompressListener.DECOMPRESS_ERROR, "DeCompressing failed : " + e.toString());
	  e.printStackTrace();
	  return false;
	}
	return true;
  }
  
  @Override
  protected void onPostExecute(Boolean result) {
	if(result && onDeCompressListener != null){
	  onDeCompressListener.onDeCompressCompleted();	
	}
  }
  
  private void _dirChecker(String dir) { 
    File f = new File(destinationPath, dir);	 
    if(!f.isDirectory()) { 
      f.mkdirs(); 
    } 
  }

  public interface OnDeCompressListener{  
	public static final int DECOMPRESS_ERROR = 0xffffd2;  
		  
	
	public void onInitialDeCompressing(long maxLength);
    public void onDeCompressing(long progress);
    public void onDeCompressCompleted();	  
    public void onDeCompressFailed(int errorCode, String error);
  }

}
