package com.a04.cabpool;

import java.util.regex.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseObject;

public class RequestInProgressGUI extends AbstractGUIActivity {

	private Button cabHereButton;

	private String cabID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_in_progress);

		cabHereButton = (Button) findViewById(R.id.cabHere);

		cabHereButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// for QR code scanner
				IntentIntegrator integrator = new IntentIntegrator(RequestInProgressGUI.this);
				integrator.initiateScan();
				//Toast.makeText(getApplicationContext(), "TEST", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onStart(){
		super.onStart();
		// Reset cabID
		cabID = "";
	}
	
	// QR code scanner
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanResult != null) {
			cabID = scanResult.getContents();
			if (scanResult.getContents() == ""){
				Intent i = new Intent(RequestInProgressGUI.this, RequestInProgressGUI.class);
				startActivity(i);
				// finish(); //?
			}
			 Toast.makeText(RequestInProgressGUI.this, cabID, Toast.LENGTH_SHORT).show();
			// Verify cabID
			
		} else {
			Toast.makeText(getApplicationContext(), "Scan Cancelled", Toast.LENGTH_SHORT).show();
		}
		

	}
}
