package com.a04.cabpool;

import java.util.Calendar;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class RequestCabGUI extends AbstractGUIActivity {

	private ParseUser currentUser;
	private Spinner genderSpinner;
	private NumberPicker ratingNumberPicker, maxPassNumberPicker;
	private Button createRequestButton, destinationSearch;
	private String gender;
	private int minRating, maxPassengers;
	private ParseObject filter, offer;
	private TextView destinationText;
	private ParseGeoPoint originPosition;
	private ParseGeoPoint destinationPosition;
	private LatLng position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);

		createRequestButton = (Button) findViewById(R.id.createRequest);
		genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
		ratingNumberPicker = (NumberPicker) findViewById(R.id.ratingNumberPicker);
		maxPassNumberPicker = (NumberPicker) findViewById(R.id.maxPassNumberPicker);
		destinationSearch = (Button) findViewById(R.id.destinationSearchButton);
		destinationText = (TextView) findViewById(R.id.destinationText);
		
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
				Intent intent = new Intent(RequestCabGUI.this, MapsActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		

		// create offer button click handler
		createRequestButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Calendar rightNow = Calendar.getInstance();
				long time=rightNow.getTimeInMillis();
				currentUser.put("timeMillis", time);
				
				//get current position
				LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
				LocationListener loc = new CabPoolLocationListener();
				locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
						loc);
				
				gender = String.valueOf(genderSpinner.getSelectedItem());
				minRating = ratingNumberPicker.getValue();
				maxPassengers = maxPassNumberPicker.getValue();		
				
				// create new filter
				ParseObject filter = new ParseObject("Filters");
				filter.put("minRating", minRating);
				filter.put("gender", gender);
				filter.put("maxPassengers", maxPassengers);
				filter.put("filterType", "request");
				saveFilter(filter);
				filter.saveInBackground(new SaveCallback() {					
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							
							currentUser.put("filter", getFilter());
							currentUser.put("origin", originPosition);
							currentUser.put("requesting", true);
							currentUser.saveInBackground();
							
							Intent intent = new Intent(RequestCabGUI.this, ListOffersGUI.class);
							startActivity(intent);
							finish();
						} else {
							Toast.makeText(RequestCabGUI.this,
									e.getLocalizedMessage(), Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		
		// Handle Map
		if(requestCode == 1){
			if(resultCode == RESULT_OK){
				
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
				locationClass.saveInBackground();
				
			}
			if(resultCode == RESULT_CANCELED){
				
			}
		}
	}

	public class CabPoolLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			//location.getLatitude();
			//location.getLongitude();
			originPosition = new ParseGeoPoint(location.getLatitude(),
					location.getLongitude());
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS ON", Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS OFF",
					Toast.LENGTH_LONG).show();

		}

	}
	
	// allows accessing filter in "done" callback function
	private void saveFilter(ParseObject filter) {
		this.filter = filter;
	}

	private ParseObject getFilter() {
		return this.filter;
	}

	private void saveOffer(ParseObject offer) {
		this.offer = offer;
	}

	private ParseObject getOffer() {
		return this.offer;
	}
}
