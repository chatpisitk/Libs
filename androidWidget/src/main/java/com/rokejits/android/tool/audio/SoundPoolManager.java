package com.rokejits.android.tool.audio;

import java.util.Enumeration;
import java.util.Hashtable;

import com.rokejits.android.tool.Log;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundPoolManager {  
  
  private SoundPool soundPool;  
  private Hashtable<Integer, SoundPoolHolder> soundMap;  
  private Context context;  
  private int maxStream, streamType, srcQuality;
  
  public SoundPoolManager(Context context, int maxStream){
    this(context, maxStream, AudioManager.STREAM_MUSIC, 0);	  
    
  }
  
  public SoundPoolManager(Context context, int maxStream, int streamType, int srcQuality){
    this.context = context;   
    this.maxStream = maxStream;
    this.streamType = streamType;
    this.srcQuality = srcQuality;
    soundMap = new Hashtable<Integer, SoundPoolHolder>();    
  }   
  
  public boolean playSound(int id){	  
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	float volume = actualVolume / maxVolume;	  
    return playSound(id, volume);	  
  }
  
  public boolean playSound(int id, float volume){
    return playSound(id, volume, volume);	  
  }
  
  public boolean playSound(int id, float leftVolume, float rightVolume){
    return playSound(id, leftVolume, rightVolume, 1, 0, 1f); 	  
  }
  
  public boolean playSound(int id, float leftVolume, float rightVolume, int priority, int loop, float rate){
	Log.e("playSound soundPool = "+soundPool);
    if(soundPool != null){
      SoundPoolHolder sHolder = soundMap.get(id);      	  
      if(sHolder != null && sHolder.loaded){
        soundPool.play(sHolder.streamId, leftVolume, rightVolume, priority, loop, rate);	 
        return true;
      }
    }
    return false;
  }
  
  public void resumeSound(int id){
    if(soundPool != null){
      SoundPoolHolder sHolder = soundMap.get(id);
      if(sHolder != null && sHolder.loaded){
        soundPool.resume(sHolder.streamId);	  
      }	
    }	  
  }
  
  public void stopSound(int id){
    if(soundPool != null){
      SoundPoolHolder sHolder = soundMap.get(id);
      if(sHolder != null && sHolder.loaded){
        soundPool.stop(sHolder.streamId);	  
      }
    }	  
  }
  
  public boolean isLoaded(int id){
    SoundPoolHolder sHolder = soundMap.get(id);
    return sHolder != null && sHolder.loaded;
  }
  
  public boolean loadSound(int id){
	if(soundPool == null){ 
	  soundMap = new Hashtable<Integer, SoundPoolHolder>();
	  soundPool = new SoundPool(maxStream, streamType, srcQuality);
	  if(soundPool != null)
	    soundPool.setOnLoadCompleteListener(onLoadCompleteListener);
	}
	if(soundPool != null){	  
	  SoundPoolHolder sHolder = soundMap.get(id);
	  if(sHolder == null){	  
	    loadAndAddSound(id);
	    return true;
  	  }
	}
    return false;	  
  }
  
  public void unloadSound(int id){
	if(soundPool != null){	  
      SoundPoolHolder sHolder = soundMap.get(id);
      if(sHolder != null && sHolder.loaded){
        soundPool.unload(sHolder.streamId);
        soundMap.remove(id);
      }
	}
  }
  
  public void unLoadAll(){
	if(soundMap == null)
	  return;
    Enumeration<Integer> e = soundMap.keys();
    while(e.hasMoreElements()){
      unloadSound(e.nextElement());	
    }    
    soundMap.clear();
  }
  
  public void release(){
    if(soundPool != null){
      soundPool.release();	
      soundPool = null;
    }	  
  }
  
  private void loadAndAddSound(int id){	
    SoundPoolHolder sHolder = new SoundPoolHolder();
    soundMap.put(id, sHolder);
    sHolder.streamId = soundPool.load(context, id, 1);
    
  }
  
  class SoundPoolHolder{
    public int streamId;
    public boolean loaded = false;   
  }
  
  private OnLoadCompleteListener onLoadCompleteListener = new OnLoadCompleteListener() {
	
	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {	  
      if(status == 0){
    	Enumeration<SoundPoolHolder> e = soundMap.elements();
    	while(e.hasMoreElements()){
    	  SoundPoolHolder sHolder = e.nextElement();
    	  if(sHolder.streamId == sampleId){
    	    sHolder.loaded = true;
    	    break;
    	  }
    	}	    
	  }else{
	    soundMap.remove(sampleId);	  
	  }
		
	}
  };  
  
	
}
