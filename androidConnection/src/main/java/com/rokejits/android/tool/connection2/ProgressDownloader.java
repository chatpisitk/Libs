package com.rokejits.android.tool.connection2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.AsyncTask;

public class ProgressDownloader extends AsyncTask<Void, Long, Boolean> {

  private static final int BUFFER_SIZE = 1024;	
	
  private InputStream is;
  private OutputStream os;  
  private long length;
  private OnProgressListener onProgressListener;
  
  private int bufferSize = BUFFER_SIZE;
  	
  public ProgressDownloader(InputStream is, OutputStream os, long length){
    this.is = is;
    this.os = os;
    this.length = length;
  }
  
  public void setBufferSize(int size){
    bufferSize = size;
    if(bufferSize <= 0)
      bufferSize = BUFFER_SIZE;
  } 
  
  public void setOnProgressListener(OnProgressListener onProgressListener) {
	this.onProgressListener = onProgressListener;
  }
  
  @Override
  protected Boolean doInBackground(Void... params) {
	byte[] buffers = new byte[bufferSize];
	int chunk = -1;
	long currentLoad = 0;
	try {
	  while((chunk = is.read(buffers)) != -1){
	    os.write(buffers, 0, chunk);
	    currentLoad = currentLoad + chunk;
	    publishProgress(currentLoad);
	  }  
	  return true;
	} catch (IOException e) {	  
	  e.printStackTrace();
	}finally{
	  try{
	    if(is != null)
	      is.close();
	    if(os != null)
	      os.close();
	  } catch (IOException e) {
		// TODO: handle exception
	  }
	}
	return false;
  }
  
  @Override
  protected void onProgressUpdate(Long... values) {
	if(onProgressListener != null)
	  onProgressListener.onProgressUpdate(values[0], length);
  }
  
  @Override
  protected void onPostExecute(Boolean result) {
	if(onProgressListener != null)
	  onProgressListener.onFinishLoad(result);
  }
  
  public interface OnProgressListener{
    public void onProgressUpdate(long currentLoad, long maxLength);
    public void onFinishLoad(boolean isSuccess);
  }	
  
  
}
