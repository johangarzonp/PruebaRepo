package com.cotech.taxislibres;

import com.cotech.taxislibres.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GCMMessageView extends Activity {
	String message;
	TextView txtmsg;
	private Context context;
	private TaxisLi appState;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		appState = ((TaxisLi) context);
		setContentView(R.layout.messageview);
 
		if(appState.isPushPendiente()){
			finish();
		}else{
			
		
		// Retrive the data from GCMIntentService.java
		Intent i = getIntent();
 
		message = i.getStringExtra("message");
 
		// Locate the TextView
		txtmsg = (TextView) findViewById(R.id.message);
 
		// Set the data into TextView
		txtmsg.setText(message);
		}
	}
}