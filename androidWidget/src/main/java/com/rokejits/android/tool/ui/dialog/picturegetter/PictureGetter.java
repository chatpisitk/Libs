package com.rokejits.android.tool.ui.dialog.picturegetter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.rokejits.android.tool.Log;
import com.rokejits.android.tool.data.StoreDataForActivityManager;
import com.rokejits.android.tool.os.UIHandler;
import com.rokejits.android.tool.utils.BitmapUtils;


public class PictureGetter {
  
  
  public static final int PICTUREGETTER_REQUEST_CAPTURE_IMAGE 	            	= 0x0AC1;
  public static final int PICTUREGETTER_REQUEST_PICK_IMAGE 	                	= 0x0AC2;  
  public static final int PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_CAMERA 	    	= 0x0AC3;
  public static final int PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_PICK_IMAGE 	    = 0x0AC4;
  public static final int PICTUREGETTER_REQUEST_ROTATE_IMAGE               		= 0x0AC5;
  
  
  private Uri mImageCaptureUri;
  private Activity activity;
  private Fragment fragment;
  private Context context;
  private PictureGetterListener pictureGetterListener;
  
  private boolean needRotate = false;
  private PictureGetterCropOption cropOption;
  private long captureTime;
  private int reqWidth = -1, reqHeight = -1;
  
  public PictureGetter(Activity activity, PictureGetterListener listener) {
    //super(activity, "Select", new String[]{"Capture from camera", "Pick from Gallery"});
    this.activity = activity;
    this.context = activity;
    pictureGetterListener = listener;	
  }
  
  public PictureGetter(Fragment fragment, PictureGetterListener listener) {
    //super(activity, "Select", new String[]{"Capture from camera", "Pick from Gallery"});
    this.fragment = fragment;
    this.context = fragment.getActivity();
    pictureGetterListener = listener;	
  }
  
  
  public void setNeedCrop(PictureGetterCropOption cropOption){
    this.cropOption = cropOption;    
    if(cropOption != null && cropOption.isReturnDataInIntent()){
      if(cropOption.getCropWidth() > 256 || cropOption.getCropHeight() > 256){
        throw new PictureGetterCropException("Crop size too big, Must be < 256x256 or set extra output uri in PictureGetterCropOption");	  
      }	
    }
  }
  
  public void setRequireSize(int reqWidth, int reqHeight){
    this.reqWidth = reqWidth;
    this.reqHeight = reqHeight;
  }
  
  public void setNeedRotate(boolean rotate){
    needRotate = rotate;	  
  }  
  
  private void startActivityForResult(Intent intent, int requestCode){
    if(activity != null){
      activity.startActivityForResult(intent, requestCode);	
    }else{
      fragment.startActivityForResult(intent, requestCode);	
    }	  
  }
  
  public boolean onActivityResult(final int requestCode, int resultCode, Intent data){
    Log.d("onActivityResult1 "+requestCode);
	Log.d("onActivityResult2 "+resultCode);
	Log.d("PICTUREGETTER_REQUEST_PICK_IMAGE "+PICTUREGETTER_REQUEST_PICK_IMAGE);
	Log.d("PICTUREGETTER_REQUEST_CAPTURE_IMAGE "+PICTUREGETTER_REQUEST_CAPTURE_IMAGE);
	Log.d("PICTUREGETTER_REQUEST_ROTATE_IMAGE "+PICTUREGETTER_REQUEST_ROTATE_IMAGE);
	Log.d("PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_CAMERA "+PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_CAMERA);
	Log.d("PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_PICK_IMAGE "+PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_PICK_IMAGE);
	switch(requestCode){
	  case PICTUREGETTER_REQUEST_PICK_IMAGE:	   
	  case PICTUREGETTER_REQUEST_CAPTURE_IMAGE:	 
	  case PICTUREGETTER_REQUEST_ROTATE_IMAGE:		
	  case PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_CAMERA:	   
	  case PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_PICK_IMAGE:
	    if(resultCode == Activity.RESULT_CANCELED){
	      onCancel();
		  return true;
		}
	  break;
	}
	
	//int rotation = -2;
	switch (requestCode) {
	  case PICTUREGETTER_REQUEST_PICK_IMAGE:
	    if(resultCode == Activity.RESULT_OK)
	      mImageCaptureUri = data.getData();	    
	  case PICTUREGETTER_REQUEST_CAPTURE_IMAGE:	  
		Bitmap bmp = null;		
		if(resultCode == Activity.RESULT_OK){
		  try {
		    bmp = BitmapUtils.getBitmapFromUri(context, mImageCaptureUri, reqWidth, reqHeight);
		  } catch (FileNotFoundException e) {		
			e.printStackTrace();
		  }
	      if(bmp != null && cropOption == null){
			if(needRotate){			
			  
			  int id = bmp.hashCode();
			  StoreDataForActivityManager.getInstance().put(id, bmp);
			  Intent intent = new Intent(context, RotateActivity.class);
			  intent.putExtra(RotateActivity.ORIGINAL_BITMAP, id);
			  startActivityForResult(intent, PICTUREGETTER_REQUEST_ROTATE_IMAGE);
			  
			}else{
			  pictureGetterListener.onGetPicture(mImageCaptureUri, bmp);			
			 
			}
			delete(mImageCaptureUri);
		  }else{
			new UIHandler().post(new Runnable() {
				
				@Override
				public void run() {
				  if(requestCode == PICTUREGETTER_REQUEST_PICK_IMAGE)
				    doCrop(PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_PICK_IMAGE);
				  else
				    doCrop(PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_CAMERA);	
					
				}
			});
			
		  }	  
		}		
	  return true;	 
	  case PICTUREGETTER_REQUEST_ROTATE_IMAGE:
		int id = data.getIntExtra(RotateActivity.ORIGINAL_BITMAP, -1);
		Bitmap bitmap = null;
		if(id != -1){
		  bitmap = (Bitmap) StoreDataForActivityManager.getInstance().get(id);		  
		}
		pictureGetterListener.onGetPicture(mImageCaptureUri, bitmap);			
		//delete(mImageCaptureUri);
	    return true;
	  case PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_CAMERA:	   
	  case PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_PICK_IMAGE:
	    
		if(resultCode == Activity.RESULT_OK){
		  Bitmap returnPhoto = null;
		  Uri returnUri = mImageCaptureUri;
	      if(cropOption.isReturnDataInIntent()){
	        Bundle extras = data.getExtras();
	        if (extras != null) {	        	
	          returnPhoto = extras.getParcelable("data");
	        }
	      }else{
	        returnUri = cropOption.getExtraOutputUri();	
	        try {
			  returnPhoto = BitmapUtils.getBitmapFromUri(context, returnUri, reqWidth, reqHeight);
			} catch (FileNotFoundException e) {				
			  e.printStackTrace();
			}
	      }
	      pictureGetterListener.onGetPicture(returnUri, returnPhoto);
	      //PictureGetterManager.getInstance().removePictureGetterOption(pictureGetterListener);
		
		  if(requestCode == PICTUREGETTER_REQUEST_CROP_IMAGE_FROM_CAMERA){
		    File f = new File(mImageCaptureUri.getPath());      
	        if (f.exists()) f.delete();
		  }
		  
		  if(cropOption.isRemoveUriAfterFinish()){
			Uri uri = cropOption.getExtraOutputUri();
			if(uri != null){
		      File f = new File(uri.getPath());      
		      Log.d("remove uri = "+uri.getPath());
		      Log.d("remove file = "+f.exists());
		      if (f.exists()) 
		        Log.d("remove = "+f.delete());
			}
		  }
		  
		  
	    }
      break;
    }
    return false;
  }
  
  private void delete(Uri uri){
	if(uri == null)
	  return;
    File f = new File(uri.getPath());      
    Log.d("remove uri = "+uri.getPath());
    Log.d("remove file = "+f.exists());
    if (f.exists()) 
      Log.d("remove = "+f.delete());	  
  }
  
  
  
  private void doCrop(final int requestCode) {
    final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
  	
  	Intent intent = cropOption.getCropIntent();
    intent.setType("image/*");      
    List<ResolveInfo> list = context.getPackageManager().queryIntentActivities( intent, 0 );
      
    int size = list.size();
      
    if (size == 0) {	        
      Toast.makeText(context, "Can not find image crop app", Toast.LENGTH_SHORT).show();
      	
      return;
    } else {
      intent.setData(mImageCaptureUri);     
          
      if (size == 1) {
    	
        Intent i = new Intent(intent);
	    ResolveInfo res	= list.get(0);
	        	
	    i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	        	
	    startActivityForResult(i, requestCode);
      } else {
	    for (ResolveInfo res : list) {
		  final CropOption co = new CropOption();
		        	
		  co.title = context.getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
		  co.icon = context.getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
		  co.appIntent = new Intent(intent);
		        	
		  co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
		        	
		  cropOptions.add(co);
		}
	        
		CropOptionAdapter adapter = new CropOptionAdapter(context, cropOptions);
		        
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setTitle("Choose Crop App");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
		  @Override
		  public void onClick( DialogInterface dialog, int item ) {
		    startActivityForResult( cropOptions.get(item).appIntent, requestCode);
		  }
		});
	        
		builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
		  @Override
		  public void onCancel( DialogInterface dialog ) {		               
		    if (mImageCaptureUri != null ) {
		      context.getContentResolver().delete(mImageCaptureUri, null, null );
		      mImageCaptureUri = null;
		    }
		  }
		});
		        
		AlertDialog alert = builder.create();
		        
		alert.show();
      }
    }
  }
  
  public void capture(){
    captureTime = System.currentTimeMillis();
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
			           "tmp_picturegetter_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);		
    intent.putExtra("return-data", true);
    startActivityForResult(intent, PICTUREGETTER_REQUEST_CAPTURE_IMAGE);  
  }
  
  public void pickFromGallery(){
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	intent.setType("image/*");
	startActivityForResult(intent,PICTUREGETTER_REQUEST_PICK_IMAGE);	    
  } 
  
  private void onCancel() {	
	pictureGetterListener.onGetPicture(null, null);
	//PictureGetterManager.getInstance().removePictureGetterOption(pictureGetterListener);
  }
  
  
  
  public interface PictureGetterListener{
    public void onGetPicture(Uri uri, Bitmap bitmap);    
  }
  
  
  
}
