package com.rokejits.android.tool.ui.dialog.picturegetter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.rokejits.android.tool.ui.dialog.picturegetter.PictureGetter.PictureGetterListener;


public class PictureGetterActivity extends Activity implements PictureGetterListener {
  public static final String PICTURE_GETTER_OPTION_ID = "PICTURE_GETTER_OPTION_ID";
  private PictureGetterDialog picGetter;
  private PictureGetterOption option;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {	
	
	super.onCreate(savedInstanceState);
	setTheme(android.R.style.Theme_Translucent_NoTitleBar);
	String id = getIntent().getExtras().getString(PICTURE_GETTER_OPTION_ID);
	option = PictureGetterManager.getInstance().getPictureGetterOption(id);
	if(option == null){
	  finish();	
	} else {
	  picGetter = new PictureGetterDialog(this, this);
	  picGetter.setRequireSize(option.reqWidth, option.reqHeight);
	  PictureGetterCropOption cropOption = option.getCropOption();
	  if(cropOption != null)
	    picGetter.setNeedCrop(cropOption);
	  picGetter.setNeedRotate(option.isNeedRotate());
	  picGetter.show();
	}
  } 
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {	  
	picGetter.onActivityResult(requestCode, resultCode, data);	
	
  }

  @Override
  public void onGetPicture(Uri uri, Bitmap bitmap) {	
	option.getPictureGetterListener().onGetPicture(uri, bitmap);
	PictureGetterManager.getInstance().removePictureGetterOption(option);
    finish();	
  }

  	
}
