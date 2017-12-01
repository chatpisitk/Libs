package com.rokejits.android.tool.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProgressOutputStream extends FilterOutputStream{
    
  private long transferred; 
  private OnTransferListener onTransferListener;
 
  public ProgressOutputStream(OutputStream out){
    super(out);	  
    this.transferred = 0;    
  }
  
  public void setOnTransferListener(OnTransferListener onTransferListener) {
	this.onTransferListener = onTransferListener;
  }
  
  private void updateTransfered(long transfered){
    if(onTransferListener != null){
      onTransferListener.onTransfered(transfered); 	
    }	  
  } 
  
  public void write(byte[] b, int off, int len) throws IOException{
    out.write(b, off, len);
    out.flush();
    transferred += len;
    updateTransfered(transferred);	  
  }
 
  public void write(int b) throws IOException{
    out.write(b);
    out.flush();
    transferred++;	  
    updateTransfered(transferred);
  }
  
  public interface OnTransferListener{
    public void onTransfered(long transfered);
  }
}
  
  
