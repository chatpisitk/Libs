package com.rokejits.android.tool.camera;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.rokejits.android.tool.widgets.R;

public class CameraView extends RelativeLayout implements Callback{ 

  private static final String TAG = CameraView.class.getName();	
	
  private FocusFrame focusFrame;
  private CameraManager cameraManager;
  private SurfaceView surfaceView;
  private SurfaceHolder surfaceHolder;
	  
  private boolean hasSurface = false, isCanTakePhoto = false;;	
  private OnCameraViewListener onCameraViewListener;
  private int activeCameraId, backCameraId = -1, frontCameraId = -1;
	
  public CameraView(Context context, AttributeSet attrs) {
	super(context, attrs);
	cameraManager = new CameraManager(context);
	
	Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    int cameraCount = Camera.getNumberOfCameras();
	for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
	  Camera.getCameraInfo(camIdx, cameraInfo);
	  if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
	    frontCameraId = camIdx;	  
	  }else if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
	    backCameraId = camIdx;	  
	  }
	}
	
	activeCameraId = backCameraId;
	if(activeCameraId == -1)
	  activeCameraId = frontCameraId;
	
  }
  
  public void startAutoFocus(){
    cameraManager.startAutoFocus();
    if(focusFrame != null)
      focusFrame.startAutoFocus();
  }
  
  public void stopAutoFocus(){
    cameraManager.stopAutoFocus();
    if(focusFrame != null)
      focusFrame.stopAutoFocus();    
  } 
  
  
  public boolean isFrontCameraSupported(){
    return frontCameraId != -1;	  
  }
  
  public boolean isBackCameraSupported(){
    return backCameraId != -1;	  
  }
  
  public boolean swapCamera(){
    if(activeCameraId == backCameraId){
      if(frontCameraId != -1){	
        openFrontCamera();
        return true;
      }
    }else{
      openBackCamera();
      return true;
    }	  
    return false;
  }
  
  public boolean isHasFrontCamera(){
    return frontCameraId != -1;	  
  }
  
  public int getActiveCameraId(){
    return activeCameraId;	  
  }
  
  public CameraInfo getActiveCameraInfo(){
    CameraInfo cameraInfo = new CameraInfo();
    Camera.getCameraInfo(activeCameraId, cameraInfo);
    return cameraInfo;
  }
  
  public void openFrontCamera(){
    openCamera(frontCameraId);  	  
  }
  
  public void openBackCamera(){
    openCamera(backCameraId);	  
  }
  
  private void openCamera(int camId){
    if(camId == -1 || activeCameraId == camId)
      return;
    activeCameraId = camId;
    if(hasSurface){
      closeDriver();      
      initCamera(surfaceHolder);
    }
  }
  
  public void closeDriver(){
	if(focusFrame != null)
	  focusFrame.onCloseDriver();
	cameraManager.stopPreview();
    cameraManager.closeDriver();	  
    isCanTakePhoto = false;
  }
  
  
  
  public void setOnCameraViewManager(OnCameraViewListener onCameraViewManager) {
	this.onCameraViewListener = onCameraViewManager;
  }
  
  public boolean takePhoto(PreviewCallback previewCallback){
	if(!isCanTakePhoto)
	  return false;	
	if(cameraManager != null)
      cameraManager.takePhoto(previewCallback);
	return true;
  }
  
  public void stopPreview(){
    if(cameraManager != null)	  
      cameraManager.stopPreview();	
  }
  
  public CameraManager getCameraManager(){
    return cameraManager;	  
  }

  public void onResume() {		
    // Set up the camera preview surface.
    surfaceView = (SurfaceView) findViewById(R.id.camera_view_layout_surfaceview_preview);
    surfaceHolder = surfaceView.getHolder();
    if (!hasSurface) {
      surfaceHolder.addCallback(this);
      
     // surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }else{
      initCamera(activeCameraId, surfaceHolder);	
    }
  }  
  
  public void onPause() { 
	    
    // Stop using the camera, to avoid conflicting with other camera-based apps
	
	closeDriver();   
	if (!hasSurface) {
      if(surfaceHolder != null)
        surfaceHolder.removeCallback(this);
    }
    
  }
  
  private void updateError(String error){
    if(onCameraViewListener != null)	  
      onCameraViewListener.onInitCameraFailed(error);
  }
  
  /** Initializes the camera and starts the handler to begin previewing. */
  private void initCamera(SurfaceHolder surfaceHolder) {
    initCamera(activeCameraId, surfaceHolder);	  
  }
  
  private void initCamera(int cameraId, SurfaceHolder surfaceHolder) {
    Log.d(TAG, "initCamera()");
    if (surfaceHolder == null) {
      throw new IllegalStateException("No SurfaceHolder provided");
    }
    try {

      // Open and initialize the camera
      cameraManager.openDriver(cameraId, surfaceHolder);
      cameraManager.startPreview();
      focusFrame = (FocusFrame) findViewById(R.id.camera_view_layout_focusframe);
      focusFrame.setCameraManager(cameraManager);
      isCanTakePhoto = true;
      if(onCameraViewListener != null)
        onCameraViewListener.onInitCameraSuccess(cameraManager);
    } catch (IOException ioe) {
      ioe.printStackTrace();
      updateError("Could not initialize camera. Please try restarting device.");
    } catch (RuntimeException e) {
      // Barcode Scanner has seen crashes in the wild of this variety:
      // java.?lang.?RuntimeException: Fail to connect to camera service
      e.printStackTrace();
      updateError("Could not initialize camera. Please try restarting device.");
    }   
  }  

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    Log.d(TAG, "surfaceCreated()");
	    
    if (holder == null) {
      Log.e(TAG, "surfaceCreated gave us a null surface");
    }
	    
    // Only initialize the camera if the OCR engine is ready to go.
    if (!hasSurface) {
      Log.d(TAG, "surfaceCreated(): calling initCamera()...");
      initCamera(holder);
    }
    hasSurface = true;	
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
		int height) {
	
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    hasSurface = false;	
  }
  
  public interface OnCameraViewListener{
    public void onInitCameraFailed(String error);
    public void onInitCameraSuccess(CameraManager cameraManager);
	  
  }
 
  
}
