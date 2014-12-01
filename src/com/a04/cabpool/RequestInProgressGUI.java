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

	// QR code scanner
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanResult != null) {
			// handle scan result
			// Toast.makeText(RequestCabGUI.this, "success",
			// Toast.LENGTH_SHORT).show();
			// Parse scan result
			// Use regex to parse contents
			Pattern pattern = Pattern.compile("Contents: ");
			Matcher matcher = pattern.matcher(scanResult.toString());
			matcher.find();
			int a = matcher.end();
			pattern = Pattern.compile("Raw bytes:");
			matcher = pattern.matcher(scanResult.toString());
			matcher.find();
			int b = matcher.start();
			cabID = scanResult.toString().substring(a, b);
			// Toast.makeText(RequestCabGUI.this, cabID,
			// Toast.LENGTH_SHORT).show();

			// Verify cabID
		}
		// else continue with any other code you need in the method

	}
}
