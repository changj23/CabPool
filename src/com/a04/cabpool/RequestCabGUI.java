package com.a04.cabpool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class RequestCabGUI extends AbstractGUIActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);

		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		
		if (scanResult != null) {
			// handle scan result
			Toast.makeText(RequestCabGUI.this, "success", Toast.LENGTH_SHORT).show();
		}
		// else continue with any other code you need in the method
		
	}
}
