package com.cotech.taxislibres.activities;

import com.cotech.taxislibres.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class AScanQr extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanning);
		
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			startActivityForResult(intent, 0);

		    //Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		    //intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
		   // startActivityForResult(intent, 0);
			
		    Log.i( "AScanQr", "SCAN_MODE");

		} catch (Exception e) {
			e.printStackTrace();
//		    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
//		    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
//		    startActivity(marketIntent);
//		    Log.i( "AScanQr", "marketIntent");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {           
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == 0) {

	        if (resultCode == RESULT_OK) {
	            String contents = data.getStringExtra("SCAN_RESULT");
	            Log.i( "AScanQr", "Info del QR: "+ contents);
	        }
	        if(resultCode == RESULT_CANCELED){
	            //handle cancel
	        	Log.i( "AScanQr", "Error: "+ RESULT_CANCELED);
	        }
	    }
	}
}
