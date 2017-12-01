package com.rokejits.android.tool.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;

import com.rokejits.android.tool.Log;

public class BitmapUtils {
  public static final int FLIP_VERTICAL = 1;
  public static final int FLIP_HORIZONTAL = 2;
	
  public static Bitmap flipImage(Bitmap src, int type) {
    // create new matrix for transformation
    Matrix matrix = new Matrix();
	// if vertical
	if(type == FLIP_VERTICAL) {
	// y = y * -1
	  matrix.preScale(1.0f, -1.0f);
	}	// if horizonal
	else if(type == FLIP_HORIZONTAL) {
	   // x = x * -1
      matrix.preScale(-1.0f, 1.0f);
	   // unknown type
    } else {
	  return null;
	}
		 
    // return transformed image
    return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
  }	
	
  public static Bitmap cropCenter(Bitmap bitmap){
	if(bitmap.getWidth() < bitmap.getHeight()){
	  bitmap = Bitmap.createBitmap(bitmap,
		    		     0, 
		    		     bitmap.getHeight()/2 - bitmap.getWidth()/2,
		    		     bitmap.getWidth(),
		    		     bitmap.getWidth());
    }else{
      bitmap = Bitmap.createBitmap(bitmap, 
		    			 bitmap.getWidth()/2 - bitmap.getHeight()/2,
		    		     0,
		    		     bitmap.getHeight(), 
		    		     bitmap.getHeight());
	}
	 
    return bitmap;
  }	
  
//  public static final Bitmap getRoundedBitmap(Bitmap bitmap, float radius){
//	if(bitmap == null)
//	  return null;
//    Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
//	Canvas canvas = new Canvas(result);
//    BitmapShader shader;
//    shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//
//    Paint paint = new Paint();
//    paint.setAntiAlias(true);
//    paint.setShader(shader);
//
//	RectF rect = new RectF(5.0f, 5.0f, result.getWidth() - 5, result.getHeight() - 5);
//
//	// rect contains the bounds of the shape
//	// radius is the radius in pixels of the rounded corners
//	// paint contains the shader that will texture the shape
//	canvas.drawRoundRect(rect, radius, radius, paint);	  
//	
//	return result;
//  }
	
  public static final Bitmap crop(Bitmap bitmap, Rect cropArea){
    return Bitmap.createBitmap(bitmap, cropArea.left, cropArea.top, cropArea.width(), cropArea.height());	  
  }
  
  
  public static Bitmap getBitmapFromUri(Context context, Uri uri) throws FileNotFoundException{    
	return getBitmapFromUri(context, uri, -1, -1);       
  }	
  
  public static Bitmap getBitmapFromUri(Context context, Uri uri, int reqWidth, int reqHeight) throws FileNotFoundException{
	Bitmap bitmap = null;
    InputStream imageStream;      
	imageStream = context.getContentResolver().openInputStream(uri);
	if(reqWidth == -1 || reqHeight == -1) 
	  bitmap = BitmapFactory.decodeStream(imageStream);
	else
	  bitmap = decodeSampledBitmapFromResource(imageStream, reqWidth, reqHeight);
	try {
	  if(imageStream != null)
	    imageStream.close();
	} catch (IOException e) {	  
	  e.printStackTrace();
	}
	return bitmap;
  }
  
  public static byte[] getByteArrayFromBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int quality){
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	bitmap.compress(format, quality, bos);
	return bos.toByteArray();	  
  }	
  public static Bitmap checkBitmapForFitSize(Bitmap bitmap,int expectW,int expectH){
    return checkBitmapForFitSize(bitmap, expectW, expectH, false);	  
  }
  public static Bitmap checkBitmapForFitSize(Bitmap bitmap,int expectW,int expectH, boolean fit){		
	  int width = bitmap.getWidth();
      int height = bitmap.getHeight();
      int newWidth = 0;
      int newHeight = 0;
      
      if(fit && width == expectW && height == expectH){
        return bitmap;	  
      }
      
      if(width > height && !fit){
      	newWidth = expectW;
      	newHeight = (newWidth*height)/width;
      }
      else if(width < height && !fit){
      	newHeight = expectH;
      	newWidth = (newHeight*width)/height;
      }else{
      	newWidth = expectW;
      	newHeight = expectH;
      }
      
      if(newWidth <= 0)
        newWidth = width;
      if(newHeight <= 0)
        newHeight = height;
      
      float scaleWidth = ((float) newWidth) / width;
      float scaleHeight = ((float) newHeight) / height; 
      /*Log.d("checkBitmapForFitSize bitmap.getWidth()= "+bitmap.getWidth()); 	
      Log.d("checkBitmapForFitSize bitmap.getHeight()= "+bitmap.getHeight());
      Log.d("checkBitmapForFitSize scaleWidth= "+scaleWidth); 	
      Log.d("checkBitmapForFitSize scaleHeight= "+scaleHeight);
      Log.d("checkBitmapForFitSize newWidth= "+newWidth); 	
      Log.d("checkBitmapForFitSize newHeight= "+newHeight);*/
      
      return rescale(bitmap, scaleWidth, scaleHeight);	
  }
  
  public static Bitmap decodeSampledBitmapFromResource(InputStream in, int reqWidth, int reqHeight) {
    // First decode with inJustDecodeBounds=true to check dimensions	  
	byte[] datas = IOUtils.readByteArrayFromInputStream(in);
	if(datas == null)
	  return null;
	ByteArrayInputStream bIn = new ByteArrayInputStream(datas);
	
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(bIn, null, options);

    bIn = new ByteArrayInputStream(datas);
    
    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeStream(bIn, null, options);
  }
  
  public static Bitmap decodeSampledBitmapFromDrawable(Resources res, int drawableId, int reqWidth, int reqHeight) {
    // First decode with inJustDecodeBounds=true to check dimensions
	  
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(res, drawableId, options);
    
    
    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(res, drawableId, options);
  }
  
  public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;
    Log.e("reqWidth = " + reqWidth);
    //if((reqWidth != 0 && reqHeight != 0) && (reqWidth != -1 && reqHeight != -1)){
      if(reqWidth == -1)
        reqWidth = width;
      if(reqHeight == -1)
        reqHeight = height;
      if(reqWidth == 0){
        inSampleSize = Math.round((float) height / (float) reqHeight); 
      }else if(reqHeight == 0){
        inSampleSize = Math.round((float) width / (float) reqWidth);  
      }else if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
      }
    //}
    Log.e("inSampleSize = " + inSampleSize);  

  return inSampleSize;
}
  
  public static Bitmap rescale(Bitmap bitmap, float scaleWidth, float scaleHeight){
    Matrix matrix = new Matrix();
    matrix.postScale(scaleWidth, scaleHeight); 
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);  	  
  }
  
  public static Bitmap resize(Bitmap bitmap, int width, int height){
    return checkBitmapForFitSize(bitmap, width, height, true);	  
  }
  
//  public static Bitmap setBitmapOrientationPortrait(Context context, Uri uri){    
//	Bitmap img = null;	
//	try {
//	  img = Media.getBitmap(context.getContentResolver(), uri);	  
//	} catch (FileNotFoundException e) {
//	  e.printStackTrace();
//	} catch (IOException e) {
//	  e.printStackTrace();
//	}
//
//		
//	return setBitmapOrientationPortrait(context, uri, img);	  
//	  
//  }
  
  public static Bitmap setBitmapOrientationPortrait(String path){
    int exifOrientation = ExifInterface.ORIENTATION_UNDEFINED;
	try {	  
	  ExifInterface exif = new ExifInterface(path);
	  exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
	} catch (IOException e) {
	  e.printStackTrace();
	}	  
	try {
	  return rotateBitmap(
	  	       BitmapUtils.decodeSampledBitmapFromResource(
			     new FileInputStream(path), -1, -1), 
			   exifOrientation);
	} catch (FileNotFoundException e) {		
	  e.printStackTrace();
	}
	
	return null;
  }
  
  
  public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {    
    Matrix matrix = new Matrix();
    switch (orientation) {
      case ExifInterface.ORIENTATION_NORMAL:
        return bitmap;
      case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
        matrix.setScale(-1, 1);
      break;
      case ExifInterface.ORIENTATION_ROTATE_180:
        matrix.setRotate(180);
      break;
      case ExifInterface.ORIENTATION_FLIP_VERTICAL:
        matrix.setRotate(180);
        matrix.postScale(-1, 1);
      break;
      case ExifInterface.ORIENTATION_TRANSPOSE:
	    matrix.setRotate(90);
	    matrix.postScale(-1, 1);
	  break;
	  case ExifInterface.ORIENTATION_ROTATE_90:
	    matrix.setRotate(90);
	  break;
	  case ExifInterface.ORIENTATION_TRANSVERSE:
	    matrix.setRotate(-90);
	    matrix.postScale(-1, 1);
	  break;
	  case ExifInterface.ORIENTATION_ROTATE_270:
	    matrix.setRotate(-90);
	  break;
	  default:
	    return bitmap;
	}
    try {
      Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
      bitmap.recycle();
      return bmRotated;
    } catch (OutOfMemoryError e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static final Bitmap setBitmapOrientationPortrait(Bitmap img , String exifOrientation){
	//Log.e("exifOrientation = "+exifOrientation);
    if(img != null){	
	  Matrix mtx = new Matrix();
	  if(exifOrientation.equalsIgnoreCase("3")) {			
	    mtx.postRotate(180);
	    img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), mtx, true);
			
	  } else if(exifOrientation.equalsIgnoreCase("6")) {			
	    mtx.postRotate(90);
	    img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), mtx, true);
	  } else if(exifOrientation.equalsIgnoreCase("8")) {			
	    mtx.postRotate(-90);
	    img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), mtx, true);
	  }
	} 	
    return img;
  }
  
  public static final Bitmap rotate(Bitmap bitmap, int degree){
    if(bitmap != null){
      Matrix mtx = new Matrix();	
      mtx.postRotate(degree);
      return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
    }	  
    return null;
  }
  
  
}
