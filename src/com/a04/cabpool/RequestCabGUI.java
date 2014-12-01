package com.a04.cabpool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
		
		offersListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				String value = (String) adapter.getItemAtPosition(position);
				Toast.makeText(RequestCabGUI.this, value,
						Toast.LENGTH_SHORT).show();
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

	public class CabPoolLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			//location.getLatitude();
			//location.getLongitude();
			position = new LatLng(location.getLatitude(), location.getLongitude());
			
			/*String Text = "My current location is: " + "Latitude = "
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
