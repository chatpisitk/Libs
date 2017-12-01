package com.rokejits.android.tool.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller { 

//  private int mDuration = 1500;
//
//  public FixedSpeedScroller(Context context) {
//    super(context);
//  }
//
//  public FixedSpeedScroller(Context context, Interpolator interpolator) {
//    super(context, interpolator);
//  }
//
//  public void setDuration(int duration){
//    this.mDuration = duration;
//  }
//
//  @Override
//  public void startScroll(int startX, int startY, int dx, int dy, int duration) {
//    // Ignore received duration, use fixed one instead
//    super.startScroll(startX, startY, dx, dy, mDuration);
//  }
//
//  @Override
//  public void startScroll(int startX, int startY, int dx, int dy) {
//    // Ignore received duration, use fixed one instead
//    super.startScroll(startX, startY, dx, dy, mDuration);
//  }

  private int mDuration = 1100;

  public FixedSpeedScroller(Context context) {
    super(context);
  }

  public FixedSpeedScroller(Context context, Interpolator interpolator) {
    super(context, interpolator);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
    super(context, interpolator, flywheel);
  }


  @Override
  public void startScroll(int startX, int startY, int dx, int dy, int duration) {
    // Ignore received duration, use fixed one instead
    super.startScroll(startX, startY, dx, dy, mDuration);
  }

  @Override
  public void startScroll(int startX, int startY, int dx, int dy) {
    // Ignore received duration, use fixed one instead
    super.startScroll(startX, startY, dx, dy, mDuration);
  }

}
