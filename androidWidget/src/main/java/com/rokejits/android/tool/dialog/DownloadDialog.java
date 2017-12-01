package com.rokejits.android.tool.dialog;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.rokejits.android.tool.connection2.ConnectionListener;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.ProgressDownloader;
import com.rokejits.android.tool.connection2.ProgressDownloader.OnProgressListener;
import com.rokejits.android.tool.os.UIHandler;

public class DownloadDialog extends ProgressDialog{

  private IConnection2 iConnection;
  private OnDismissListener onDismissListener;
  private OnDownloadListener onDownloadListener;
  
  private ProgressDownloader downloader; 
  
  public static final DownloadDialog create(Context context, OnDownloadListener onDownloadListener){
    return create(context, 0, onDownloadListener);	  
  }
  
  public static final DownloadDialog create(Context context, int theme, OnDownloadListener onDownloadListener){
    DownloadDialog downloadDialog = null;
    if(theme > 0){
      downloadDialog = new DownloadDialog(context, theme); 	
    }else{
      downloadDialog = new DownloadDialog(context);	
    }    
    
    downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    
    downloadDialog.onDownloadListener = onDownloadListener;
    
    return downloadDialog;
  }
	
	
	
  private DownloadDialog(Context context) {
	super(context);
	super.setOnDismissListener(mOnDismissListener);
  }	
	
  private DownloadDialog(Context context, int theme) {
	super(context, theme);
	super.setOnDismissListener(mOnDismissListener);
  } 
  
  @Override
  public void setOnDismissListener(OnDismissListener listener) {
    this.onDismissListener = listener;
  }
  
  public DownloadDialog download(IConnection2 iConnection){
    this.iConnection = iConnection;	   
    iConnection.setConnectionListener(connectionListener);
    iConnection.connect();
    setIndeterminate(true);
    if(!isShowing())
      show();
    return this;
  }
  
  
  private ConnectionListener connectionListener = new ConnectionListener() {
	
	@Override
	public void onConnected(IConnection2 iConnection2, final InputStream in) {
	  new UIHandler().post(new Runnable() {		
	    @Override
	    public void run() {
	      OutputStream os = onDownloadListener.onDownloadConnected(DownloadDialog.this, iConnection);	
	      if(os == null){
	    	iConnection.stopConnect();
	        dismiss();
	        return;
	      }
	      downloader = new ProgressDownloader(in, os, iConnection.getConnectionDescription().getLength());
	  	  downloader.setOnProgressListener(new OnProgressListener() {		
	  		@Override
	  		public void onProgressUpdate(long currentLoad, long maxLength) {
	  	      if(isIndeterminate()){
	  	        setMax((int)maxLength);	  
	  	        setIndeterminate(false);
	  	      }		  
	  		  setProgress((int)currentLoad);
	  		}
	  		
	  		@Override
	  		public void onFinishLoad(boolean isSuccess) {
	  		  if(onDownloadListener != null)
	  		    onDownloadListener.onDownloadSuccess();
	  		  dismiss();
	  		}
	  	  });
	  	  downloader.execute();
	    }
	  });
	  
	}
	
	@Override
	public void onConnectFailed(IConnection2 iConnection2, int errorCode, String error) {
	  if(onDownloadListener != null)
	    onDownloadListener.onDownloadFailed(errorCode, error);
	  dismiss();		
	}
  };
  
  private OnDismissListener mOnDismissListener = new OnDismissListener() {
	
	@Override
	public void onDismiss(DialogInterface dialog) {
	  if(downloader != null){
	    downloader.setOnProgressListener(null);
	  }
	  if(iConnection != null)
	    iConnection.stopConnect();
	  if(onDismissListener != null)
	    onDismissListener.onDismiss(dialog);
	}
  };
  
  public interface OnDownloadListener{
	public OutputStream onDownloadConnected(DownloadDialog downloadDialog, IConnection2 iConnection2);
    public void onDownloadSuccess();	  
    public void onDownloadFailed(int errorCode, String error);
  }

}
