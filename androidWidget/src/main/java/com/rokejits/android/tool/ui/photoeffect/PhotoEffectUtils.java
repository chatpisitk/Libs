package com.rokejits.android.tool.ui.photoeffect;

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

public class PhotoEffectUtils {

  public static Bitmap convertToSepia(Bitmap bmp_original){	
	int width;
	int height;
	int r, g, b, c, gry;
	int depth = 20;
	
	width = bmp_original.getWidth();
	height = bmp_original.getHeight();
	
	Bitmap bmpSephia = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	Canvas canvas = new Canvas(bmpSephia);
	Paint paint = new Paint();
	   
	ColorMatrix cm = new ColorMatrix();
	cm.setScale(.3f, .3f, .3f, 1.0f);
	    
	ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	paint.setColorFilter(f);
	canvas.drawBitmap(bmp_original, 0, 0, paint);
	    
	for(int x=0; x < width; x++) {
	    	
	  for(int y=0; y < height; y++) {
	    		
	    c = bmp_original.getPixel(x, y);

	    r = Color.red(c);
	    g = Color.green(c);
	    b = Color.blue(c);

	    gry = (r + g + b) / 3;
	    r = g = b = gry;

	    r = r + (depth * 2);
	    g = g + depth;

	    if(r > 255) {
	      r = 255;
	    }
	            
	    if(g > 255) {
	      g = 255;
	    }
	            
	    bmpSephia.setPixel(x, y, Color.rgb(r, g, b));
	  }
	}
	    
	return bmpSephia;    
		  
	  
  }	
  
  public static Bitmap convertToGreyScale(Bitmap bmp_original) {	
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
  
  public static Bitmap convertToReflection(Bitmap bmp_original, int reflectionH) {	
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
      
    return reflection;
    
	
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
  
  public static Bitmap contrastScale(Bitmap bmp_original, float contrast) {
			
    int width;
	int height;
		
    float con = contrast + 2;
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
            }
      );
		    
	  paint.setColorFilter(new ColorMatrixColorFilter(cm));
	  c.drawBitmap(bmpContrastScale, 0, 0, paint);
		    
		    //Contrast Scale Only
		    cm.set(new float[] {
                 scale, 0, 0, 0, 0,
                 0, scale, 0, 0, 0,
                 0, 0, scale, 0, 0,
                 0, 0, 0, 1, 0 
		    	}
		    );
		    
		    paint.setColorFilter(new ColorMatrixColorFilter(cm));
		    c.drawBitmap(bmpContrastScale, 0, 0, paint);
		    
		    //Contrast Translate Only
		    cm.set(new float[] {
                 1, 0, 0, 0, translate,
                 0, 1, 0, 0, translate,
                 0, 0, 1, 0, translate,
                 0, 0, 0, 1, 0 
		    	}
		    );
		    
		    paint.setColorFilter(new ColorMatrixColorFilter(cm));
		    c.drawBitmap(bmpContrastScale, 0, 0, paint);
		    
		    return bmpContrastScale;
		
		  
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
          }
	    );
	    
	    paint.setColorFilter(new ColorMatrixColorFilter(cm));
	    c.drawBitmap(applyMask, 0, 0, paint);
	    
	    //Contrast Scale Only
	    cm.set(new float[] {
             scale, 0, 0, 0, 0,
             0, scale, 0, 0, 0,
             0, 0, scale, 0, 0,
             0, 0, 0, 1, 0 
	    	}
	    );
	    
	    paint.setColorFilter(new ColorMatrixColorFilter(cm));
	    c.drawBitmap(applyMask, 0, 0, paint);
	    
	    //Contrast Translate Only
	    cm.set(new float[] {
             1, 0, 0, 0, translate,
             0, 1, 0, 0, translate,
             0, 0, 1, 0, translate,
             0, 0, 0, 1, 0 
	    	}
	    );
	    
	    paint.setColorFilter(new ColorMatrixColorFilter(cm));
	    c.drawBitmap(applyMask, 0, 0, paint);
	    //==================================================
	    return applyMask;
	   
	
  }
  
  
  
}
