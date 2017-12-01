package com.rokejits.android.tool.graphics;

import android.graphics.Bitmap;
import android.os.Looper;
import android.widget.ImageView;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.graphics.BitmapLoader.BitmapDownloaderListener;
import com.rokejits.android.tool.os.UIHandler;
import com.rokejits.android.tool.os.UIHandler.UIHandlerListener;
import com.rokejits.android.tool.utils.BitmapUtils;

public class ImageViewBitmapLoader implements BitmapDownloaderListener, UIHandlerListener{
  private static final int BITMAP_LOADED = 0;
  
  private ImageView imageView;
  private BitmapLoader loader;
  private UIHandler uiHandler;
  private Bitmap bitmap;
  private int expectW = -1, expectH = -1;
  
  public ImageViewBitmapLoader(String url, ImageView imageView) {
	this.imageView = imageView;
	uiHandler = new UIHandler(Looper.getMainLooper());
	uiHandler.setUIHandlerListener(this);
    loader = new BitmapLoader(imageView.getContext(), url, this);
  }
  
  public void setExpectWidth(int width){
    expectW = width;	  
  }
	  
  public void setExpectHeight(int height){
    expectH = height;	  
  }
  
  public void load(){
    loader.load();	  
  }  

  @Override
  public void onUpdateUI(int source) {
    if(expectW != -1 && expectH != -1){
      imageView.setImageBitmap(BitmapUtils.resize(bitmap, expectW, expectH));	  
    }else if(expectW == -1 && expectH == -1){    	
      imageView.setImageBitmap(BitmapUtils.resize(bitmap, imageView.getWidth(), imageView.getHeight()));  
	}else{
	  int width = bitmap.getWidth();
	  int height = bitmap.getHeight();
	  float scale = 1;
	  if(expectW != -1){
		    	
	    scale = (float)expectW / (float)width;		      	
	  }else if(expectH != -1){
		scale = (float)expectH / (float)height;	
	  }
		    
      Log.d("scale = "+scale);
		    
      imageView.setImageBitmap(BitmapUtils.rescale(bitmap, scale, scale));
		    
		    
	}  	
	
  }

  @Override
  public void onBitmapLoaded(String url, Bitmap bitmap) {
    if(bitmap != null){
      this.bitmap = bitmap;
      uiHandler.sendMessage(BITMAP_LOADED);
    }
	
  }   

}
