package com.a04.cabpool;

import java.util.ArrayList;
import java.util.List;

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
	
	private LatLng position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);

		// ADD FILTERS N SHIT
		
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
