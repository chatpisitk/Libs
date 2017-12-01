package com.rokejits.android.tool.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

public class BitmapEffectUtils {
  
  public static Bitmap convert2Sepia(Bitmap bmp_original) {		
	return convert2Sepia(bmp_original, 255, 0.35, 0.25, 0.15);
  }
	
  public static Bitmap convert2Sepia(Bitmap bmp_original, int depth,
			                       double Red, double Green, double Blue) {	
    // image size
  	int width = bmp_original.getWidth();
  	int height = bmp_original.getHeight();
  	// create output bitmap
 	Bitmap bmpSephia = Bitmap.createBitmap(width, height, bmp_original.getConfig());
	// constant grayscal
 	// color information
 	int A, R, G, B;
	int pixel;
	// scan through all pixels
 	for(int x = 0; x < width; ++x) {
	  for(int y = 0; y < height; ++y) {
	    // get pixel color
	    pixel = bmp_original.getPixel(x, y);
	    // get color on each channel
	    A = Color.alpha(pixel);
	  	R = Color.red(pixel);
		G = Color.green(pixel);
		B = Color.blue(pixel);
		// apply grayscale sample
		B = G = R = (int)(Red * R + Green * G + Blue * B);
 		// apply intensity level for sepid-toning on each channel
		R += (depth * Red);
	 	if(R > 255) { 
	      R = 255; 
	    }	 	 
		G += (depth * Green);
	 	if(G > 255) { G = 255; }
		 
	 	B += (depth * Blue);
		if(B > 255) { B = 255; }

		// set new pixel color to output image
		bmpSephia.setPixel(x, y, Color.argb(A, R, G, B));
	  }
	}	
 	 
 	return bmpSephia;
      
  }
	
  public static Bitmap convert2GreyScale(Bitmap bmp_original) {
    int width;
	int height;
				
	width = bmp_original.getWidth();
	height = bmp_original.getHeight();
				
	Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	Canvas c = new Canvas(bmpGrayscale);
	Paint paint = new Paint();
			    
	ColorMatrix cm = new ColorMatrix();
	cm.setSaturation(0);
			    
	ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	paint.setColorFilter(f);
	c.drawBitmap(bmp_original, 0, 0, paint);
			    
	return bmpGrayscale;  
  }
	
  public static Bitmap convert2Negative(Bitmap bmp_original) {
    int width;
	int height;
				
	width = bmp_original.getWidth();
	height = bmp_original.getHeight();
				
	Bitmap bmpnegative = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    Canvas c = new Canvas(bmpnegative);
			    
    Rect srcRect = new Rect(-(-50), -(-50), bmp_original.getWidth(), bmp_original.getHeight());
    Rect destRect = new Rect(srcRect);
    destRect.offset(-50, -50);
			    
    c.drawBitmap(bmp_original, srcRect, destRect, null);
    return bmpnegative; 
  }
	
  public static Bitmap convert2Reflection(Bitmap bmp_original, int reflectionH) {
    int width;
	int height;
				
	width = bmp_original.getWidth();
    height = bmp_original.getHeight();
				
	Bitmap reflection = Bitmap.createBitmap(bmp_original.getWidth(), reflectionH, Bitmap.Config.ARGB_8888);
	Bitmap blurryBitmap = Bitmap.createBitmap(bmp_original, 0, bmp_original.getHeight() - reflectionH, bmp_original.getWidth(), reflectionH);
				
	blurryBitmap = Bitmap.createScaledBitmap(Bitmap.createScaledBitmap(blurryBitmap,blurryBitmap.getWidth() / 2, blurryBitmap.getHeight() / 2, true), blurryBitmap.getWidth(), blurryBitmap.getHeight(), true);
    BitmapShader bitmapShader = new BitmapShader(blurryBitmap, TileMode.CLAMP, TileMode.CLAMP);
				
	Matrix invertMatrix = new Matrix();
    invertMatrix.setScale(1f, -1f);
    invertMatrix.preTranslate(0, -reflectionH);
    bitmapShader.setLocalMatrix(invertMatrix);
		        
    Shader alphaGradient = new LinearGradient(0, 0, 0, reflectionH, 0x80ffffff, 0x00000000, TileMode.CLAMP);
    ComposeShader compositor = new ComposeShader(bitmapShader, alphaGradient, PorterDuff.Mode.DST_IN);
		        
    Paint reflectionPaint = new Paint();
    reflectionPaint.setShader(compositor);
		        
    Canvas canvas = new Canvas(reflection);
    canvas.drawRect(0, 0, reflection.getWidth(), reflection.getHeight(), reflectionPaint);
		        
    return blurryBitmap;
  }
	
  public static Bitmap saturationScale(Bitmap bmp_original, float scale) {				
    int width;
	int height;
				
	width = bmp_original.getWidth();
    height = bmp_original.getHeight();
				
	Bitmap bmpSaturationscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    Canvas c = new Canvas(bmpSaturationscale);
    Paint paint = new Paint();
			    
    ColorMatrix cm = new ColorMatrix();
    cm.setSaturation(scale);
			    
    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
    paint.setColorFilter(f);
    c.drawBitmap(bmp_original, 0, 0, paint);
			    
    return bmpSaturationscale;  
  }
	
  public static Bitmap contrastScale(Bitmap bmp_original,float contrast) {
    int width;
	int height;
				
    float con = contrast ;
    if(con > 180) con = 0.0f;
            
    float angle = con / 180.f;
	            
    float scale = angle + 1.f;
	float translate = (-.5f * scale + .5f) * 255.f;
				
	width = bmp_original.getWidth();
	height = bmp_original.getHeight();
				
	Bitmap bmpContrastScale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bmpContrastScale);
    Paint paint = new Paint();
			    
    c.drawColor(Color.WHITE);
    paint.setColorFilter(null);
    c.drawBitmap(bmp_original, 0, 0, paint);
			    
    ColorMatrix cm = new ColorMatrix();
			    
    //Set Contrast
    cm.set(new float[] {
  	  scale, 0, 0, 0, translate, //Red
	  0, scale, 0, 0, translate, //Green
	  0, 0, scale, 0, translate, //Blue
	  0, 0, 0, 1, 0 //Alpha
    });
			    
    paint.setColorFilter(new ColorMatrixColorFilter(cm));
    c.drawBitmap(bmpContrastScale, 0, 0, paint);
			    
    //Contrast Scale Only
    cm.set(new float[] {
      scale, 0, 0, 0, 0,
      0, scale, 0, 0, 0,
      0, 0, scale, 0, 0,
      0, 0, 0, 1, 0 
   	});
			    
	paint.setColorFilter(new ColorMatrixColorFilter(cm));
	c.drawBitmap(bmpContrastScale, 0, 0, paint);
			    
	//Contrast Translate Only
	cm.set(new float[] {
	  1, 0, 0, 0, translate,
	  0, 1, 0, 0, translate,
	  0, 0, 1, 0, translate,
	  0, 0, 0, 1, 0 
    });
			    
    paint.setColorFilter(new ColorMatrixColorFilter(cm));
    c.drawBitmap(bmpContrastScale, 0, 0, paint);
			    
	return bmpContrastScale;    
  }
	
  public static Bitmap doBrightness(Bitmap src, int value) {
    // image size
	int width = src.getWidth();
	int height = src.getHeight();
	// create output bitmap
	Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	// color information
	int A, R, G, B;
	int pixel;
		
	// scan through all pixels
	for(int x = 0; x < width; ++x) {
	  for(int y = 0; y < height; ++y) {
	    // get pixel color
						
		pixel = src.getPixel(x, y);
		A = Color.alpha(pixel);
		R = Color.red(pixel);
		G = Color.green(pixel);
		B = Color.blue(pixel);
		
		// increase/decrease each channel
		R += value;
		if(R > 255) { R = 255; }
		else if(R < 0) { R = 0; }
		
		G += value;
		if(G > 255) { G = 255; }
		else if(G < 0) { G = 0; }
		
		B += value;
		if(B > 255) { B = 255; }
		else if(B < 0) { B = 0; }
	
		// apply new pixel color to output bitmap
		bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	  }
  	}

	return bmOut;	
  }
	
  public static Bitmap doGamma(Bitmap src, double red, double green, double blue) {
    // create output image
	Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
	// get image size
	int width = src.getWidth();
	int height = src.getHeight();
	// color information
	int A, R, G, B;
	int pixel;
	// constant value curve
	final int    MAX_SIZE = 256;
	final double MAX_VALUE_DBL = 255.0;
	final int    MAX_VALUE_INT = 255;
	final double REVERSE = 1.0;
		
	// gamma arrays
	int[] gammaR = new int[MAX_SIZE];
	int[] gammaG = new int[MAX_SIZE];
	int[] gammaB = new int[MAX_SIZE];
		
	// setting values for every gamma channels
	for(int i = 0; i < MAX_SIZE; ++i) {
      gammaR[i] = (int)Math.min(MAX_VALUE_INT,
						    (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
	  gammaG[i] = (int)Math.min(MAX_VALUE_INT,
		  	  		        (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
	  gammaB[i] = (int)Math.min(MAX_VALUE_INT,
							(int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
	}
		
	// apply gamma table
	for(int x = 0; x < width; ++x) {
	  for(int y = 0; y < height; ++y) {
		// get pixel color
		pixel = src.getPixel(x, y);
		A = Color.alpha(pixel);
		// look up gamma
		R = gammaR[Color.red(pixel)];
		G = gammaG[Color.green(pixel)];
		B = gammaB[Color.blue(pixel)];
		// set new color to output bitmap
		bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	  }
	}
		
	return bmOut;
  }


	
  public static Bitmap applyMask(Bitmap bmp_original, Bitmap bmp_mask) {
	int width;
	int height;
				
	width = bmp_original.getWidth();
	height = bmp_original.getHeight();
				
    //Merge Mask Layer into OriginalBitmap
	//Resize MaskLayer to Original_Bitmap's Size
    Bitmap mask = Bitmap.createScaledBitmap(bmp_mask, width, height, true);
	//===========================================
				
	Bitmap applyMask = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    Canvas c = new Canvas(applyMask);
    Paint paint = new Paint();
			    
    ColorMatrix cm = new ColorMatrix();
    cm.setSaturation(3);
		    
    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
    paint.setColorFilter(f);
			    
    c.drawBitmap(bmp_original, 0, 0, paint);
    c.drawBitmap(mask, 0, 0, paint);
    //==============================================
			    
    //Change Contrast
    float contrast = 10 + 2;
    if(contrast > 180) contrast = 0.0f;
	            
    float angle = contrast / 180.f;
	            
    float scale = angle + 1.f;
	float translate = (-.5f * scale + .5f) * 255.f;
				
	//Set Contrast
	cm.set(new float[] {
	  scale, 0, 0, 0, translate, //Red
	  0, scale, 0, 0, translate, //Green
	  0, 0, scale, 0, translate, //Blue
	  0, 0, 0, 1, 0 //Alpha
	});
			    
	paint.setColorFilter(new ColorMatrixColorFilter(cm));
	c.drawBitmap(applyMask, 0, 0, paint);
			    
	//Contrast Scale Only
	cm.set(new float[] {
	  scale, 0, 0, 0, 0,
	  0, scale, 0, 0, 0,
	  0, 0, scale, 0, 0,
	  0, 0, 0, 1, 0 
	});
			    
	paint.setColorFilter(new ColorMatrixColorFilter(cm));
	c.drawBitmap(applyMask, 0, 0, paint);
			    
	//Contrast Translate Only
	cm.set(new float[] {
	  1, 0, 0, 0, translate,
	  0, 1, 0, 0, translate,
	  0, 0, 1, 0, translate,
	  0, 0, 0, 1, 0 
	});
			    
    paint.setColorFilter(new ColorMatrixColorFilter(cm));
    c.drawBitmap(applyMask, 0, 0, paint);
	return applyMask;  
  }
	
  public static Bitmap convert2Filter(Bitmap src, double red, double green, double blue) {

	// image size
	int width = src.getWidth();
	int height = src.getHeight();
	// create output bitmap
	Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	// color information
	int A, R, G, B;
	int pixel;
		
	// scan through all pixels
	for(int x = 0; x < width; ++x) {
      for(int y = 0; y < height; ++y) {
		// get pixel color
		pixel = src.getPixel(x, y);
		// apply filtering on each channel R, G, B
		A = Color.alpha(pixel);
		R = (int)(Color.red(pixel) * red);
		G = (int)(Color.green(pixel) * green);
		B = (int)(Color.blue(pixel) * blue);
	    // set new color pixel to output bitmap
		bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	  }
	}
    return bmOut;
  }
}
