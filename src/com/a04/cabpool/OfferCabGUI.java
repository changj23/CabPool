package com.a04.cabpool;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class OfferCabGUI extends AbstractGUIActivity {

	private ParseUser currentUser;
	
	private Spinner genderSpinner;
	private NumberPicker ratingNumberPicker, maxPassNumberPicker;
	private Button createOfferButton, destinationSearch;
	private String gender;
	private int minRating, maxPassengers;
	private ParseObject filter, offer, locationClass, cab;
	private TextView destinationText;
	private ParseGeoPoint destinationPosition;

	private String cabID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer);

		// for QR code scanner
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
		Toast.makeText(getApplicationContext(), "Scan the Cab's QR Code", Toast.LENGTH_LONG).show();
		
		createOfferButton = (Button) findViewById(R.id.createOffer);
		genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
		ratingNumberPicker = (NumberPicker) findViewById(R.id.ratingNumberPicker);
		maxPassNumberPicker = (NumberPicker) findViewById(R.id.maxPassNumberPicker);
		destinationSearch = (Button) findViewById(R.id.destinationSearchButton);
		destinationText = (TextView) findViewById(R.id.destination);
		
		if(destinationText.getText().toString().equals("")){
			destinationText.setText("Please select a destination.");
		}
		
		currentUser = ParseUser.getCurrentUser();

		// config for rating number picker
		ratingNumberPicker.setMaxValue(5);
		ratingNumberPicker.setMinValue(0);
		ratingNumberPicker.setValue(3);

		// disable constant looping of values in numberPicker
		ratingNumberPicker.setWrapSelectorWheel(false);

		// disable soft keyboard on press
		ratingNumberPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		// config for max passenger number picker
		maxPassNumberPicker.setMaxValue(3);
		maxPassNumberPicker.setMinValue(0);
		maxPassNumberPicker.setValue(2);

		// disable constant looping of values in numberPicker
		maxPassNumberPicker.setWrapSelectorWheel(false);

		// disable soft keyboard on press
		maxPassNumberPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		
		// open maps activity for destination search
		destinationSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OfferCabGUI.this, MapsActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		

		// create offer button click handler
		createOfferButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender = String.valueOf(genderSpinner.getSelectedItem());
				minRating = ratingNumberPicker.getValue();
				maxPassengers = maxPassNumberPicker.getValue();
				

				// create new filter
				ParseObject filter = new ParseObject("Filters");
				filter.put("minRating", minRating);
				filter.put("gender", gender);
				filter.put("maxPassengers", maxPassengers);
				filter.put("filterType", "offer");
				saveFilter(filter);
				filter.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							// successfully saved filter object
							// Toast.makeText(OfferCabGUI.this,
							// "Successfully saved filters",
							// Toast.LENGTH_SHORT).show();

							// create Offer object and relate it to the filters
							// object
							
							currentUser.put("offering", true);
							currentUser.put("currentCabId", cabID);
							currentUser.put("filter", getFilter());		
							currentUser.put("destination", getLocationObject());
							currentUser.saveInBackground();
							// find parse cab object whose id matches scanned cabID
							ParseQuery<ParseObject> cabQuery = ParseQuery.getQuery("Cab");
							cabQuery.whereEqualTo("cabID", cabID);
							cabQuery.findInBackground(new FindCallback<ParseObject>(){

								@Override
								public void done(List<ParseObject> cabsList,
										ParseException e) {
									// TODO Auto-generated method stub
									if(e == null){
										// note that no error does not imply a cab was found
										if(cabsList.isEmpty() == false) {
											ParseObject cab = cabsList.get(0);
											Log.d("cabid", "Cab " + cabID + " found!");
											// set strict filters for cab
											
											// set cab rating to be max(filterMinRating, cabMinRating)
											int cabMinRating = cab.getInt("minRating");
											
											if(cabMinRating == -1 || minRating > cabMinRating){
												cab.put("minRating", minRating);
											}
											
											// set cab max passengers to be min(filterMaxPassengers, cabMaxPassengers)
											int cabMaxPassengers = cab.getInt("maxPassengers");
											if(cabMaxPassengers == -1 || maxPassengers < cabMaxPassengers){
												cab.put("maxPassengers", maxPassengers);
											}
											
											// set gender
											
											saveCab(cab);
											
											// check if all passengers are in "offering" mode
											ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
											userQuery.whereEqualTo("currentCabId", cabID);
											userQuery.findInBackground(new FindCallback<ParseUser>(){

												@Override
												public void done(
														List<ParseUser> usersList,
														ParseException e) {
													// TODO Auto-generated method stub
													if(e == null){
														//
														if(usersList.isEmpty() == true){
															getCab().put("isOffering", true);
														} else {
															getCab().put("isOffering", true);
															for(ParseObject user : usersList){
																if(user.getBoolean("offering") == false){
																	getCab().put("isOffering", false);
																}
															}
														}
														
														// add to number of passengers
														getCab().put("numPassengers", getCab().getInt("numPassengers") + 1);
														getCab().saveInBackground();
													} else {
														// error
														Log.d("error", e.getLocalizedMessage());
													}
												}
												
											});
										} else {
											Toast.makeText(OfferCabGUI.this,
													"Cab not found",
													Toast.LENGTH_SHORT).show();
										}									
									} else {
										Toast.makeText(OfferCabGUI.this,
												e.getLocalizedMessage(),
												Toast.LENGTH_SHORT).show();
									}
								}
								
							});
							
							// go to offer in progress gui
							Intent intent = new Intent(
									OfferCabGUI.this,
									OfferInProgressGUI.class);
							startActivity(intent);

							// finish activity so the user can't
							// come back here
							finish();
							
						} else {
							Toast.makeText(OfferCabGUI.this,
									e.getLocalizedMessage(), Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
			}
		});
	}
	
	public void onStart(){
		super.onStart();
	}

	// QR code scanner
	// Duplicated from RequestCabGUI
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		Log.d("debug", "onActivityResult");
		
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanResult != null) {
			// handle scan result
			cabID = scanResult.getContents();
			// Toast.makeText(OfferCabGUI.this, cabID,
			// Toast.LENGTH_LONG).show();
			if (cabID == null) {
				Toast.makeText(OfferCabGUI.this, "Scan Cancelled",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(OfferCabGUI.this, MainMenuGUI.class);
				startActivity(i);
				finish();
			}
			// Verify cabID
		}
		
		if(requestCode == 1){
			Log.d("debug", "requestCode == 1");
			if(resultCode == RESULT_OK){
				Log.d("debug", "resultCode = OK");
				String destinationAddress = intent.getStringExtra("destinationAddress");
				Double destinationLat = intent.getDoubleExtra("destinationPosLat", 0);
				Double destinationLong = intent.getDoubleExtra("destinationPosLong", 0);
				Log.d("destination", destinationAddress+":("+destinationLat+","+destinationLong+")");
				destinationPosition = new ParseGeoPoint(destinationLat, destinationLong);
				
				destinationText.setText(destinationAddress);
				
				// store address and destination geopoint into Location class
				ParseObject locationClass = new ParseObject("Location");
				locationClass.put("geopoint", destinationPosition);
				locationClass.put("locationName", destinationAddress);
				
				//locationClass.saveInBackground();
				saveLocationObject(locationClass);
				
				
			}
			if(resultCode == RESULT_CANCELED){
				
			}
		}
	}

	// allows accessing filter in "done" callback function
	private void saveFilter(ParseObject filter) {
		this.filter = filter;
	}

	private ParseObject getFilter() {
		return this.filter;
	}
	
	private void saveLocationObject(ParseObject locationClass){
		this.locationClass = locationClass;
	}
	
	private ParseObject getLocationObject(){
		return this.locationClass;
	}
	
	private void saveCab(ParseObject cab){
		this.cab = cab;
	}
	
	private ParseObject getCab(){
		return this.cab;
	}
}
