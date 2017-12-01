package com.rokejits.android.tool.selector;

import android.content.Context;

public abstract class ArraySelectorAdapter<T> extends SelectorDialogAdapter{
  private T[] datas;
  
  public ArraySelectorAdapter(Context context, T[] datas) {
	super(context);
	this.datas = datas;
	
  }
  
  public T getData(int position){
    return datas[position];	  
  }
  
  public T[] getDatas(){
    return datas;	  
  }
  
  public int getDataCount(){
    return datas.length;	  
  }
  

}
