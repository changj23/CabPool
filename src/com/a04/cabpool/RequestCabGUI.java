package com.a04.cabpool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.internal.ml;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class RequestCabGUI extends AbstractGUIActivity {

	private ParseUser currentUser;
	private ListView offersListView;
	private ArrayAdapter<String> adapter;
	
	private String cabID = "";
	private LatLng position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);

		offersListView = (ListView) findViewById(R.id.offersListView);

		// this.adapter = adapter;

		// for QR code scanner
		/*
		 * IntentIntegrator integrator = new IntentIntegrator(this);
		 * integrator.initiateScan();
		 */

		currentUser = ParseUser.getCurrentUser();

		// query for a list of offers
		ParseQuery<ParseObject> offerQuery = ParseQuery.getQuery("Offer");

		offerQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> offersList, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {

					ArrayList<String> arrayList = new ArrayList<String>();
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							RequestCabGUI.this, R.layout.simplerow, arrayList);
					offersListView.setAdapter(adapter);

					if (offersList.isEmpty() == false) {
						Toast.makeText(RequestCabGUI.this,
								offersList.get(0).getString("cabId"),
								Toast.LENGTH_SHORT).show();

						// list out all the offers found
						for (ParseObject offer : offersList) {
							Log.d("offersFound", offer.getString("cabId"));

							adapter.add("Cab id: " + offer.getString("cabId")
									+ "\nDestination: ");

						}
					} else {
						Toast.makeText(RequestCabGUI.this, "No offers found",
								Toast.LENGTH_SHORT).show();

					}

				} else {
					Toast.makeText(RequestCabGUI.this, e.getLocalizedMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ADD GEOLCOATION QUERY HERE
		LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocationListener loc = new CabPoolLocationListener();
		locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10,
				loc);
	}

	public ArrayAdapter getAdapter() {
		return this.adapter;
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

	public class CabPoolLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
//			location.getLatitude();
//			location.getLongitude();
			position = new LatLng(location.getLatitude(), location.getLongitude());
			
/*			String Text = "My current location is: " + "Latitude = "
					+ position.latitude + "Longitude = "
					+ position.longitude;
			Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT)
					.show();*/
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
}
