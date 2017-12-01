package com.rokejits.android.tool.data.loader;

public abstract class Loader implements Runnable{
  private LoaderListener loaderListener;
   	
  
  public Loader(LoaderListener loaderListener){
    this.loaderListener = loaderListener;	  
  }
  
  public void execute(){
    new Thread(this).start();	  
  }
  
  protected abstract void doLoad();
  
  @Override
  public void run() {
  	doLoad();
  	
  }
  
  protected void finishLoad(){
    loaderListener.onFinishLoad(this);	  
  }
  
  public interface LoaderListener{
    public void onFinishLoad(Loader loader);	    
  }

	
}
