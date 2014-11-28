package com.a04.cabpool;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class RequestCabGUI extends AbstractGUIActivity {
	
	private ParseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);

		// for QR code scanner
/*		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();*/
		
		currentUser = ParseUser.getCurrentUser();
		
		// query for a list of offers
		ParseQuery<ParseObject> offerQuery = ParseQuery.getQuery("Offer");
		// ADD GEOLCOATION QUERY HERE
		
		offerQuery.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> offersList, ParseException e) {
				// TODO Auto-generated method stub
				if(e == null){
					if(offersList.isEmpty() == false){
						Toast.makeText(RequestCabGUI.this, offersList.get(0).getString("cabId"), Toast.LENGTH_SHORT).show();
						// list out all the offers found
						for (ParseObject offer : offersList){
							Log.d("offersFound", offer.getString("cabId"));
						}
					} else {
						Toast.makeText(RequestCabGUI.this, "No offers found", Toast.LENGTH_SHORT).show();
						
					}

				} else {
					Toast.makeText(RequestCabGUI.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});

	}

/*	// QR code scanner
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		
		if (scanResult != null) {
			// handle scan result
			Toast.makeText(RequestCabGUI.this, "success", Toast.LENGTH_SHORT).show();
		}
		// else continue with any other code you need in the method
		
	}*/
}
