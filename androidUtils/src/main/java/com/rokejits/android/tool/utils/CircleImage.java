package com.rokejits.android.tool.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
/**
 * 
 * @author papontee
 *
 */
public class CircleImage {
	
	public static Bitmap getImage(Bitmap bitmap) {

		bitmap = BitmapUtils.cropCenter(bitmap);
	    
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		if(height < width)
			width = height;
		
	    Bitmap output = Bitmap.createBitmap(width,
	    		width, Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    
	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, width, width);

//	    paint.setStrokeWidth(5);
	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    canvas.drawCircle(width / 2, width / 2,
	    		width / 2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    bitmap.recycle();
	    
	    return output;
	}
	
	public static Bitmap getImage(Bitmap bitmap,CircleImageOptions options) 
	{	
		//crop image
		bitmap = BitmapUtils.cropCenter(bitmap);
	    
		int width = bitmap.getWidth();
		
	    Bitmap output = Bitmap.createBitmap(width,
	    		width, Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    
	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, width, width);

//	    paint.setStrokeWidth(5);
	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    canvas.drawCircle(width / 2, width / 2,
	    		width / 2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    
	    //gradient
	    if(options.gradientEnable)
	    {
	    	RadialGradient gradient = new RadialGradient(width / 2, width / 2, width, options.startGradientColor,
	                options.endGradientColor, android.graphics.Shader.TileMode.CLAMP);
	        Paint gradientPaint = new Paint();
	        gradientPaint.setDither(true);
	        gradientPaint.setShader(gradient);
//	        gradientPaint.setColor(0xFFFFFFFF);
//	        gradientPaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));

	        canvas.drawCircle(width / 2, width / 2, width / 2, gradientPaint);
	    }
	    
	    //stroke
	    if(options.strokeWidth > 0)
	    {
		    final Paint strokePaint = new Paint();
		    strokePaint.setAntiAlias(true);
		    strokePaint.setStyle(Style.STROKE);
		    strokePaint.setStrokeWidth(options.strokeWidth);
		    strokePaint.setColor(options.strokeColor);
		    
		    canvas.drawCircle(width / 2, width / 2,
		    		width / 2 - (options.strokeWidth /2), strokePaint);
	    }
	    
	    bitmap.recycle();
	    
	    return output;
	}	
}
