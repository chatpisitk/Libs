package com.rokejits.android.tool.ui.dialog.picturegetter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.rokejits.android.tool.selector.SelectorDialog;
import com.rokejits.android.tool.selector.SelectorDialogAdapter;
import com.rokejits.android.tool.selector.SelectorDialogAdapter.OnSelectorItemClickListener;
import com.rokejits.android.tool.ui.dialog.picturegetter.PictureGetter.PictureGetterListener;

public class PictureGetterDialog extends SelectorDialog{
  protected static final int CAPTURE 	= 0;
  protected static final int PICK 		= 1;
  
  private PictureGetter pictureGetter;
  private OnPictureGetterDialogItemClickListener onPictureGetterDialogItemClickListener;
  
  public PictureGetterDialog(Activity activity, PictureGetterListener listener) {
	super(activity);
	pictureGetter = new PictureGetter(activity, listener);
	setSelectorDialogAdapter(new PictureGetterSelectorAdapter(activity));
  }
  
  public void setOnPictureGetterDialogItemClickListener(
		OnPictureGetterDialogItemClickListener onPictureGetterDialogItemClickListener) {
	this.onPictureGetterDialogItemClickListener = onPictureGetterDialogItemClickListener;
  }
  
  public void setNeedCrop(PictureGetterCropOption cropOption){
    pictureGetter.setNeedCrop(cropOption);
  }
	  
  public void setRequireSize(int reqWidth, int reqHeight){
    pictureGetter.setRequireSize(reqWidth, reqHeight);
  }
	  
  public void setNeedRotate(boolean rotate){
	pictureGetter.setNeedRotate(rotate);	
  }  
  
  public boolean onActivityResult(int requestCode, int resultCode, Intent data){
    return pictureGetter.onActivityResult(requestCode, resultCode, data);
  }  
  
  @Override
  public void setSelectorDialogAdapter(SelectorDialogAdapter adapter){
	if(!(adapter instanceof PictureGetterSelectorAdapter)){
	  throw new RuntimeException("adaper must extends PictureGetterSelectorAdapter");	
	}
	super.setSelectorDialogAdapter(adapter);
	adapter.setOnSelectorItemClickListener(onSelectorItemClickListener);
  }
  
  private OnSelectorItemClickListener onSelectorItemClickListener = new OnSelectorItemClickListener() {	
	@Override
	public void onSelectorItemClicked(SelectorDialogAdapter selectorAdapter, View view, int position, long id) {
	  switch(position){
	    case CAPTURE:
	      if(onPictureGetterDialogItemClickListener == null || 
	         !onPictureGetterDialogItemClickListener.onCaptureClicked())
	        pictureGetter.capture();	
	    break;
	    case PICK:
	      if(onPictureGetterDialogItemClickListener == null || 
	         !onPictureGetterDialogItemClickListener.onPickFromGalleryClicked())
	      pictureGetter.pickFromGallery();
	    break;
	  }	
		
	}
  };	
  	
  public interface OnPictureGetterDialogItemClickListener{
    public boolean onCaptureClicked();
    public boolean onPickFromGalleryClicked();
  }
  
  
}
