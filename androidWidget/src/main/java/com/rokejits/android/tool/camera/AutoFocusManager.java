/*
 * Copyright (C) 2012 ZXing authors
 * Copyright 2012 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rokejits.android.tool.camera;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.Log;

import com.rokejits.android.tool.os.UIHandler;

public final class AutoFocusManager implements Camera.AutoFocusCallback {

  private static final String TAG = AutoFocusManager.class.getSimpleName();

  private static final long AUTO_FOCUS_INTERVAL_MS = 3500L;
  private static final Collection<String> FOCUS_MODES_CALLING_AF;
  static {
    FOCUS_MODES_CALLING_AF = new ArrayList<String>(2);
    FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_AUTO);    
  }

  private boolean active = true;  
  private final boolean useAutoFocus;
  private final Camera camera;
  private final Timer timer;
  private TimerTask outstandingTask;
  private Vector<OnAutoFocusListener> onAutoFocusListeners;
  private boolean isSupporAutoFocusArea = false;
  private CameraManager cameraManager;
  private Context context;

  @SuppressLint("NewApi")
  AutoFocusManager(Context context, Camera camera, CameraManager cameraManager) {
	this.context = context;
    this.camera = camera;
    this.cameraManager = cameraManager;
    timer = new Timer(true);
    
    
    
    
    Camera.Parameters param = camera.getParameters();
    
    isSupporAutoFocusArea = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    
    if(isSupporAutoFocusArea)
      isSupporAutoFocusArea = param.getMaxNumFocusAreas() > 0;
    
    //param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
    camera.setParameters(param);
    String currentFocusMode = camera.getParameters().getFocusMode();
    useAutoFocus =
        CameraSetting.getInstance().isEnableAutoFocus() &&
        FOCUS_MODES_CALLING_AF.contains(currentFocusMode);
    Log.i(TAG, "Current focus mode '" + currentFocusMode + "'; use auto focus? " + useAutoFocus);    
    checkAndStart();
  }
  
  public boolean isActive(){
    return active;	  
  }
  
  public void stopAutoFocus(){
	if(!active)
	  return;
    stop();
    active = false;
  }
  
  public void startAutoFocus(){
    if(active)
      return;
    active = true;
    checkAndStart();
  }
  
  public void addOnAutoFocusListener(OnAutoFocusListener onAutoFocusListener){
    if(onAutoFocusListener == null)
      return;
    if(onAutoFocusListeners == null)
      onAutoFocusListeners = new Vector<OnAutoFocusListener>();
    if(onAutoFocusListeners.contains(onAutoFocusListener))
      return;
    onAutoFocusListeners.add(onAutoFocusListener);
  }  
  
  public void removeOnAutoFocusListener(OnAutoFocusListener onAutoFocusListener){
    if(onAutoFocusListeners == null)
      return;
    onAutoFocusListeners.remove(onAutoFocusListener);
  }
  
  public void removeAllOnAutoFocusListener(){
    if(onAutoFocusListeners == null)
      return;
    onAutoFocusListeners.removeAllElements();	  
  }

  @Override
  public synchronized void onAutoFocus(boolean success, Camera theCamera) {
    if (active) {
      outstandingTask = new TimerTask() {
        @Override
        public void run() {
          checkAndStart();
        }
      };
      timer.schedule(outstandingTask, AUTO_FOCUS_INTERVAL_MS);
      if(onAutoFocusListeners != null && onAutoFocusListeners.size() > 0){
        for(OnAutoFocusListener onAutoFocusListener : onAutoFocusListeners){
          onAutoFocusListener.onAutoFocus(success);	
        }	
      }
    }    
    
  }

  void checkAndStart() {
  	if (useAutoFocus) {
  	  //active = true;
      start(null);
    }
  }

  @SuppressLint("NewApi")
  private synchronized void start(RectF focusArea) {	  
    try {
      Camera.Parameters param = camera.getParameters();
      if(!isSupporAutoFocusArea){
        focusArea = null;	  
      }
      
      CameraConfigurationManager cameraConfigurationManager = cameraManager.getCameraConfigurationManager();
	  if(focusArea == null){	      
	    Point screenResolution = cameraConfigurationManager.getRealResolution();	      
	    focusArea = new RectF(0, 0, screenResolution.x, screenResolution.y);
	  }
      
	  if(isSupporAutoFocusArea){   
		
	    if(onAutoFocusListeners != null && onAutoFocusListeners.size() > 0){ 			
  	      if(isSupporAutoFocusArea){
  		    //Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//  		    int screenWidth, screenHeight;
//  		    if(android.os.Build.VERSION.SDK_INT < 13){
//  		      screenWidth = display.getWidth();
//  		      screenHeight = display.getHeight();
//  		    }else{
//  		      Point screenSize = new Point();        
//  		      display.getSize(screenSize);      
//  		      screenWidth = screenSize.x;
//  		      screenHeight = screenSize.y;      
//  		    }	
  		    
  	    	Point screenResolution = cameraConfigurationManager.getRealResolution();  
  	    	int screenWidth = screenResolution.x;
  	    	int screenHeight = screenResolution.y;
  	    	
  		    Rect area = new Rect((int)((focusArea.left * 2000) / screenWidth) - 1000, 
  		      	                 (int)((focusArea.top * 2000) / screenHeight) - 1000, 
  		    		             (int)((focusArea.right * 2000) / screenWidth) - 1000, 
  		    		             (int)((focusArea.bottom * 2000) / screenHeight) - 1000);
//  		      Log.e(TAG, "screenWidth = " + screenWidth);
//		      Log.e(TAG, "screenHeight = " + screenHeight);
//  		      Log.e(TAG, "focusArea.left = " + focusArea.left);
//		      Log.e(TAG, "focusArea.right = " + focusArea.right);
//		      Log.e(TAG, "focusArea.top = " + focusArea.top);
//		      Log.e(TAG, "focusArea.bottom = " + focusArea.bottom);
//  		      Log.e(TAG, "area.left = " + area.left);
//  		      Log.e(TAG, "area.right = " + area.right);
//  		      Log.e(TAG, "area.top = " + area.top);
//  		      Log.e(TAG, "area.bottom = " + area.bottom);
  		    param.setFocusAreas(Arrays.asList(new Camera.Area(area, 1000)));
  		    camera.setParameters(param);
  		  }
  		
  		 
	    }
      }
	  if(param.getFocusMode().equals(Camera.Parameters.FOCUS_MODE_AUTO)){
	    if(focusArea != null && onAutoFocusListeners != null){
	      PointF point = new PointF(focusArea.left + ((focusArea.right - focusArea.left) / 2), 
	  	  		                    focusArea.top + (focusArea.bottom - focusArea.top) / 2);	      
	      for(OnAutoFocusListener onAutoFocusListener : onAutoFocusListeners){
	        onAutoFocusListener.onStartAutoFocus(point);	
	      }
	    }
	    camera.autoFocus(this);
	  }
	} catch (RuntimeException re) {
      // Have heard RuntimeException reported in Android 4.0.x+; continue?
	  Log.w(TAG, "Unexpected exception while focusing", re);
  	  new UIHandler().post(new Runnable() {
			
		@Override
		public void run() {
		  onAutoFocus(false, camera);				
		}
	  });
	    
	}
  }
  
  

  /**
   * Performs a manual auto-focus after the given delay.
   * @param delay Time to wait before auto-focusing, in milliseconds
   */
  synchronized void start(long delay) {  	
    start(delay, null);
  }
  
  synchronized void start(long delay, RectF focusArea) {
	stop();
	outstandingTask = new AutoFocusTimerTask(focusArea);
  	timer.schedule(outstandingTask, delay);
  }

  synchronized void stop() {
    if (useAutoFocus) {
      camera.cancelAutoFocus();
    }
    if (outstandingTask != null) {
      outstandingTask.cancel();      
      outstandingTask = null;
    }  
  }
  
  class AutoFocusTimerTask extends TimerTask{
    private RectF focusArea;
	
    public AutoFocusTimerTask(RectF focusArea) { 
      this.focusArea = focusArea;
	}
	  
    @Override
    public void run() {
      //manual = true; 			
	  start(focusArea);
    }	  
  }
  
  public interface OnAutoFocusListener{
    public void onStartAutoFocus(PointF center);
    public void onAutoFocus(boolean success);
  }

}
