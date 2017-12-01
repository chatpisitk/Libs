package com.rokejits.android.tool.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


public class FileUtil {
	
  public static void galleryAddPic(Context context, String mCurrentPhotoPath) {
	  
    galleryAddPic(context, new File(mCurrentPhotoPath));
  }	
  
  public static void galleryAddPic(Context context, File pictureFile) {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);    
    Uri contentUri = Uri.fromFile(pictureFile);
    mediaScanIntent.setData(contentUri);
    context.sendBroadcast(mediaScanIntent);
    
    
    
  }
	
  public static final String getAppFolder(Context context){
    return context.getFilesDir ().getAbsolutePath(); 	  
  }
  
  public static final String getExternalAppFolder(Context context){
    return getExternalAbsolutePath() + "/Android/data/" + context.getPackageName();	  
  }
  
	
  public static final String getExternalAbsolutePath(){
    return Environment.getExternalStorageDirectory().getAbsolutePath(); 	  
  }	
	
  public static boolean isExternalPathExist(){
	final  String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    if(path == null){
      return false;
    }else{
      File f = new File(path);
      if(f.canRead()){
        return true;
      }else{
        return false;
      }
    }
  }
	
  public static boolean deleteFile(String filePath){
    return deleteFile(new File(filePath));	  
  }
  
  public static boolean deleteFile(File file){
    if(file.exists()){
      return file.delete();	
    }	  
    return false;
  }
  
  public static void saveFile(String filePath, byte[] data) throws IOException{
    saveFile(filePath, data, true);	  
  }	
  
  public static void saveFile(String filePath, byte[] data, int bufferSize) throws IOException{
    saveFile(filePath, data, true, bufferSize);	  
  }
	
  public static void saveFile(String filePath, byte[] data, boolean deleteIfExist) throws IOException{
    saveFile(filePath, data, deleteIfExist, -1);	  
  }
  
  public static void saveFile(String filePath, byte[] data, boolean deleteIfExist, int bufferSize) throws IOException{
	File file = new File(filePath);
	if(file.exists()){
	  if(deleteIfExist){ 
	    file.delete();	    
	  }	  
	}
	
	ByteArrayInputStream bInputStream = new ByteArrayInputStream(data);
	DataInputStream dInputStream = new DataInputStream(bInputStream);
	
	FileOutputStream fOut = new FileOutputStream(file);
	DataOutputStream dOut = new DataOutputStream(fOut);
	
	if(bufferSize > 0){
	  byte[] buffer = new byte[bufferSize];
	  int ch = -1;
	  while((ch = dInputStream.read(buffer)) != -1){
	    dOut.write(buffer, 0, ch);	
	  }
	}else{
	  dOut.write(data);
	  dOut.flush();
	}
	
	dOut.close();
	fOut.close();	   	  
  }	
  
  public static void saveFile(String filePath, InputStream in) throws IOException{
    saveFile(filePath, in, true);	  
  }
  
  public static void saveFile(String filePath, InputStream in, boolean deleteIfExist) throws IOException{
    File file = new File(filePath);
	if(file.exists()){
	  if(deleteIfExist){ 
	    file.delete();
	    file.createNewFile();
	  }	  
	}else{
	  file.createNewFile();
	}
	FileOutputStream fOut = new FileOutputStream(file);
 	DataOutputStream dOut = new DataOutputStream(fOut);
	
 	byte[] buf = new byte[1024];
 	int chuck = -1;
 	
 	while((chuck = in.read(buf)) != -1){
 	  dOut.write(buf, 0, chuck);	
 	}
	
	
	dOut.close();
	fOut.close();	   	    
  }
  
  public static byte[] readFile(String filePath) throws FileNotFoundException{
    return IOUtils.readByteArrayFromInputStream(openFile(filePath));	  
  }
  
  public static FileInputStream openFile(String filePath) throws FileNotFoundException{
    File file = new File(filePath);   
    return new FileInputStream(file);
        
  }
  
//  public static String getFilePathFromURI(Context context, Uri contentUri) {
//
//	  String fileName="unknown";//default fileName
//	    Uri filePathUri = contentUri;
//	    if (contentUri.getScheme().toString().compareTo("content")==0)
//	    {      
//	        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
//	        if (cursor.moveToFirst())
//	        {
//	            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
//	            filePathUri = Uri.parse(cursor.getString(column_index));
//	            fileName = filePathUri.getLastPathSegment().toString();
//	        }
//	    }
//	    else if (contentUri.getScheme().compareTo("file")==0)
//	    {
//	        fileName = filePathUri.getLastPathSegment().toString();
//	    }
//	    else
//	    {
//	        fileName = fileName+"_"+filePathUri.getLastPathSegment().toString();
//	    }
//	    return fileName;
//  }
}
