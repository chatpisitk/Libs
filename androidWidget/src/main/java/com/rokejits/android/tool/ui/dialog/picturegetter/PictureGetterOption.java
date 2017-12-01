package com.rokejits.android.tool.ui.dialog.picturegetter;

import android.content.Context;
import android.content.Intent;

import com.rokejits.android.tool.ui.dialog.picturegetter.PictureGetter.PictureGetterListener;


public class PictureGetterOption{
  public PictureGetterCropOption cropOption;
  public boolean needRotate;
  public PictureGetterListener listener;
  public int reqWidth = -1, reqHeight = -1;
  private Context context;
  
	    
  public PictureGetterOption(Context context, PictureGetterCropOption cropOption, boolean needRotate, PictureGetterListener listener){
	this.context = context;
    this.cropOption = cropOption;
    this.needRotate = needRotate;
    this.listener = listener;
  }
  
  public final void getPicture(){
    String id = PictureGetterManager.getInstance().addPictureGetterOption(this);
    Intent intent = new Intent(context, PictureGetterActivity.class);
    intent.putExtra(PictureGetterActivity.PICTURE_GETTER_OPTION_ID, id);
    context.startActivity(intent);
  }
	    
  public void setRequireSize(int reqWidth, int reqHeight){
    this.reqWidth = reqWidth;
    this.reqHeight = reqHeight;
  }  
  
  public PictureGetterCropOption getCropOption(){
    return cropOption;	
  }
	    
  public boolean isNeedRotate(){
    return needRotate;	
  }
	    
  public PictureGetterListener getPictureGetterListener(){
    return listener;	
  }
	    
	    
	    
	    
}

