package com.a04.cabpool;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tyczj.mapnavigator.Navigator;

public class MapsActivity extends AbstractGUIActivity {

	private GoogleMap map;
	private Location myLocation;
	MarkerOptions markerOptions;
	LatLng latLng;
	private Button findDestinationButton;
	private EditText offerDestinationInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_maps);
		
		offerDestinationInput = (EditText) findViewById(R.id.offerDestination);
		findDestinationButton = (Button) findViewById(R.id.findDestinationButton);
		
		findDestinationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Getting reference to EditText to get the user input
				// location

				// Getting user input location
				String offerDestination = offerDestinationInput.getText().toString();

				if (offerDestination != null && !offerDestination.equals("")) {
					new GeocoderTask().execute(offerDestination);
				}
			}
		});

		// get my location
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = lm.getBestProvider(criteria, true);

		Location location = lm.getLastKnownLocation(bestProvider);

		if (location == null) {
			Log.d("provider", "Getting Network Provider");
			location = lm
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		//Geocoder geocoder = new Geocoder(getBaseContext());

		// make sure that location service is enabled
		if (location != null) {

			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			// enable "My Location" button on map
			map.setMyLocationEnabled(true);

			// geopoint, zoom
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					43.260879, -79.919225), 15));

			// other supported types include MAP_NORMAL, MAP_TYPE_TERRAIN,
			// MAP_TYPE_HYBRID
			// and MAP_TYPE_NONE

			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			// testing map navigation
			LatLng start = new LatLng(location.getLatitude(),
					location.getLongitude());
			LatLng end = new LatLng(43.260879, -79.919225);

			Navigator nav = new Navigator(map, start, end);
			nav.findDirections(true);
		} else {
			Toast.makeText(this, "GPS needs to be enabled", Toast.LENGTH_SHORT)
					.show();
		}

	}

	// An AsyncTask class for accessing the GeoCoding Web Service
	class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getBaseContext());
			List<Address> addresses = null;

			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getBaseContext(), "No Location found",
						Toast.LENGTH_SHORT).show();
			}

			// Clears all the existing markers on the map
			map.clear();

			// Adding Markers on Google Map for each matching address
			for (int i = 0; i < addresses.size(); i++) {

				Address address = (Address) addresses.get(i);

				// Creating an instance of GeoPoint, to display in Google Map
				latLng = new LatLng(address.getLatitude(),
						address.getLongitude());

				String addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address
								.getCountryName());

				markerOptions = new MarkerOptions();
				markerOptions.position(latLng);
				markerOptions.title(addressText);

				map.addMarker(markerOptions);

				// Locate the first location
				if (i == 0)
					map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			}
		}
	}
}
