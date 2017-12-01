package com.rokejits.android.tool.utils;

import android.graphics.Color;
/**
 * 
 * @author papontee
 *
 */
public class CircleImageOptions {	
  public float strokeWidth = 0;
  public int strokeColor;
  public int width;
  public boolean gradientEnable;
  public int startGradientColor;
  public int endGradientColor;
	
  public CircleImageOptions(){
    gradientEnable = false;
	width = 0;
	strokeWidth  = 0f;
	strokeColor = Color.DKGRAY;
	startGradientColor = 0x00FFFFFF;
	endGradientColor = 0xFF000000;
  }
}
