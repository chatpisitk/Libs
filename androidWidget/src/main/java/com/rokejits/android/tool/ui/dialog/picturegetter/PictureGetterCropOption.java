package com.rokejits.android.tool.ui.dialog.picturegetter;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class PictureGetterCropOption {

  private static final String CROP_OUTPUT_WIDTH 		= "outputX";	
  private static final String CROP_OUTPUT_HEIGHT 		= "outputY";
  private static final String CROP_ASPECT_X	 		    = "aspectX";	
  private static final String CROP_ASPECT_Y 			= "aspectY";
  private static final String CROP_SCALE	 			= "scale";
  private static final String CROP_OUTPUT_URI 			= MediaStore.EXTRA_OUTPUT;
  private static final String CROP_OUTPUT_FORMAT		= "outputFormat";
  private static final String CROP_RETURN_DATA			= "return-data";
  
  
  
  private boolean removeUriAfterFinish = false;
  
  private Intent intent;
  
  public PictureGetterCropOption(int cropWidth, int cropHeight){
	intent = new Intent("com.android.camera.action.CROP");	
	setCropSize(cropWidth, cropHeight);   
    
  }  
  
  
  
  public int getCropWidth(){
    return intent.getIntExtra(CROP_OUTPUT_WIDTH, 0);	  
  }
  
  public int getCropHeight(){
    return intent.getIntExtra(CROP_OUTPUT_HEIGHT, 0);	  
  }
  
  
  public void removeUriAfterFinish(boolean remove){
    removeUriAfterFinish = remove;	  
  }
  
  public boolean isRemoveUriAfterFinish(){
    return removeUriAfterFinish;	  
  }
  /*public int getAspectX(){
    return aspectX;	  
  }
  
  public int getAspectY(){
    return aspectY;	  
  }
  
  public boolean needScale(){
    return scale;	  
  }*/
  public Intent getCropIntent(){
    return intent;	  
  } 
  
  public Uri getExtraOutputUri(){
    return (Uri) intent.getExtras().get(CROP_OUTPUT_URI);	  
  }
  
  public void setReturnDataInIntent(boolean returnData){
    intent.putExtra(CROP_RETURN_DATA, returnData);	  
    if(returnData){
      intent.removeExtra(CROP_OUTPUT_URI);	
    }
  }
  
  public void setCropSize(int width, int height){
    setCropWidth(width);
    setCropHeight(height);
  }
  
  public boolean isReturnDataInIntent(){	
    return intent.getBooleanExtra(CROP_RETURN_DATA, true);	  
  }
	  
  public void setExtraOutputUri(Uri uri){
    intent.putExtra(CROP_OUTPUT_URI, uri);	  
    setReturnDataInIntent(false);
  }
	  
  public void setOutputFormat(String format){
    intent.putExtra(CROP_OUTPUT_FORMAT, format);	  
  } 
  
  public void setAspect(int aspectX, int aspectY){
    setAspectX(aspectX);
    setAspectY(aspectY);
  }
  
  public void needScale(boolean needScale){
	intent.putExtra(CROP_SCALE, needScale);     	  
  }
  
  public void setCropWidth(int width) {
	if(width <= 0)
	  throw new PictureGetterCropException("Crop output width must be > 0");
	intent.putExtra(CROP_OUTPUT_WIDTH, width);	  
  }
	  
  public void setCropHeight(int height){
	if(height <= 0)
      throw new PictureGetterCropException("Crop output height must be > 0");  
	intent.putExtra(CROP_OUTPUT_HEIGHT, height);	  
  }
  
  public void setAspectX(int aspect){    	
    intent.putExtra(CROP_ASPECT_X, aspect);    
  }
  
  public void setAspectY(int aspect){
    intent.putExtra(CROP_ASPECT_Y, aspect);	  
  }
  
  
}
