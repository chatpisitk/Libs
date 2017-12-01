package com.rokejits.android.tool.camera;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera.CameraInfo;

import com.rokejits.android.tool.utils.BitmapUtils;

public class CameraUtils {

  public static final Bitmap convertToJpeg(byte[] data, 
		                                   Point cameraResolution, 
		                                   int previewFormat, 
		                                   int cameraFacing, 
		                                   int cameraDisplayOrientationDegree){	  	
    YuvImage yuvImage = new YuvImage(data, previewFormat, cameraResolution.x, cameraResolution.y, null);     
    data = null;
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();  	
    yuvImage.compressToJpeg(new Rect(0, 0, cameraResolution.x, cameraResolution.y), 
   		                    100, 
      		                bOut);
    yuvImage = null;      
    data = bOut.toByteArray();
    bOut = null; 
    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
    data = null;     
    if(cameraFacing == CameraInfo.CAMERA_FACING_BACK)
      bitmap = BitmapUtils.rotate(bitmap, cameraDisplayOrientationDegree);
    else{
      bitmap = BitmapUtils.rotate(bitmap, cameraDisplayOrientationDegree - 180);
      bitmap = BitmapUtils.flipImage(bitmap, BitmapUtils.FLIP_HORIZONTAL);
    }
    
    return bitmap;
  }	
}
