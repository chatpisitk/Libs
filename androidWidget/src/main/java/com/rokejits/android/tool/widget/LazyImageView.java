package com.rokejits.android.tool.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.connection2.IConnection2;
import com.rokejits.android.tool.connection2.StopLoadListener;
import com.rokejits.android.tool.graphics.BitmapLoader;
import com.rokejits.android.tool.graphics.BitmapLoader.BitmapDownloaderListener;
import com.rokejits.android.tool.os.UIHandler;

public class LazyImageView extends ImageView implements BitmapDownloaderListener, StopLoadListener{ 	
  private BitmapLoader loader;  
  private int expectW = -1, expectH = -1;
  private LazyImageViewListener lListener;
  private boolean requestReload = false;
  private boolean isLoading, isFinish;
  
  public LazyImageView(Context context){
    super(context);   
  } 
  
  public LazyImageView(Context context, AttributeSet attrs){
    super(context, attrs);    
  } 
  
//  public LazyImageView(Context context, AttributeSet attrs, int defStyle){
//    super(context, attrs, defStyle);        
//  } 
  
  public void setLazyImageViewListener(LazyImageViewListener listener){
    lListener = listener;	  
  } 
  
  public boolean isLoading(){
    return isLoading;	  
  }
	  
  public boolean isFinish(){
    return isFinish;	  
  }
  
  public void setExpectWidth(int width){
    expectW = width;	  
  }
  
  public void setExpectHeight(int height){
    expectH = height;	  
  }
  
  /*public void setUrl(String url){
    setUrl(url, handler);	  
  }*/
  
  public void setUrl(String url){	
	stopLoad();
	if(url == null)
	  url = "";
    loader = new BitmapLoader(getContext(), url, this);	
  }
  
  public void setConnection(IConnection2 iConnection2){
	stopLoad();
    loader = new BitmapLoader(getContext(), iConnection2, this);	  
  }
  
  
  public void setTimeOut(int timeout){
    loader.setTimeout(timeout);	  
  }
  
  public void reload(){    	
    if(loader != null){
      stopLoad();
      requestReload = true;
      requestLayout();
    }
  }  
  
  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {	
	super.onLayout(changed, left, top, right, bottom);
	
	if(requestReload){
	  int width = getWidth();
	  int height = getHeight();
//      if(width == 0 || height == 0)
//        return;
	  isLoading = true;
	  isFinish = false;
	  
	  if(expectW == -1)
	    expectW = width;
	  if(expectH == -1)
	    expectH = height;	  
	  loader.setSize(expectW, expectH);
	  loader.load();
	  requestReload = false;	
	}
	
  }
  
  
  public void stopLoad(){
    if(loader != null){
      loader.stopLoad();	
    }	  
    isLoading = false;
  }
  
  private void updateListener(String url, Bitmap bitmap){
    if(lListener != null)
      lListener.onBitmapLoaded(this, url, bitmap);
  } 
  
  protected Bitmap onGetBitmap(Bitmap bitmap){
    return bitmap;
  }
  
  @Override
  public void onBitmapLoaded(final String url, Bitmap bitmap) {
	Log.d("onBitmapLoaded = "+bitmap);
	isLoading = false;
	final Bitmap tmp = onGetBitmap(bitmap);
    if(bitmap != null){
      isFinish = true; 
      new UIHandler().post(new Runnable() {		
		@Override
		public void run() {
		  setImageBitmap(tmp);
		  updateListener(url, tmp);			
		}
	  });	
    }else{
      updateListener(url, bitmap);	
    }	
	
  }

  public interface LazyImageViewListener{
    public void onBitmapLoaded(LazyImageView lView, String url, Bitmap bitmap);	   
  }

  @Override
  public void stopLoadAll() {
	stopLoad();
	
  } 
}
