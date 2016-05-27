package com.cotech.taxislibres.services;


import java.util.Locale;

import com.cotech.taxislibres.C;
import com.cotech.taxislibres.TaxisLi;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;
import android.widget.Toast;



public class ServiceTTS extends Service implements TextToSpeech.OnInitListener{

	private String TAG = "RLPlusSTextTo";
	protected String module = C.TEXT_TO_SPEECH;
	private TextToSpeech tts;
    private String spokenText = "TAXIS LIBRES LOSNUMERO 1";
    private IntentFilter filter;
    
    private TaxisLi appState;
    
    
    public final BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Integer cmd = intent.getIntExtra("CMD", 0);
			switch(cmd){
			case(C.HABLAR):
				if(appState.isQuieroEscuchar()){
					Log.i(TAG, "Deberia Hablar.........");
					String text = intent.getStringExtra("TEXTHABLA");
					String minusculas= text.toLowerCase();	//Cambia el String a minusculas
					//tts.speak(text, TextToSpeech.QUEUE_ADD,null);
					tts.speak(minusculas, TextToSpeech.QUEUE_ADD,null);
				}else{
					Log.i(TAG, "No Deberia Hablar.........");
				}
				break;
			}
			
		}
	};
    
    
    
    

    @Override
    public void onCreate() {
    	Context context = getApplicationContext();
    	appState = ((TaxisLi) context);
        
    	Log.i(TAG, "onCreate");
    	tts = new TextToSpeech(this, this);  
        
        filter = new IntentFilter();
		filter.addAction(module);
		try{
			registerReceiver(receiver, filter);
		}catch (Exception e){
			e.printStackTrace();
		}
		
        // This is a good place to set spokenText
    }

    @Override
    public void onInit(int status) {
    	Log.i(TAG, "onInit");
    	if (status == TextToSpeech.SUCCESS) {  
    				
    		Locale loc = new Locale ("es", "ES");
            int result = tts.setLanguage(loc);
      
    	    if (result == TextToSpeech.LANG_MISSING_DATA  
    	            || result == TextToSpeech.LANG_NOT_SUPPORTED) {  
    	        Log.e("TTS", "This Language is not supported");  
    	    } else {  
    	        
    	    }  
    	  
    	} else {  
    	    Log.e("TTS", "Initilization Failed!");  
    	}  
    }

    public void onUtteranceCompleted(String uttId) {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}