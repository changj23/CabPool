package com.a04.cabpool;

import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends AbstractGUIActivity {

	private GoogleMap map;
	private Location myLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_maps);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// enable "My Location" button on map
		map.setMyLocationEnabled(true);

		// geopoint, zoom
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.260879,
				-79.919225), 15));

		// other supported types include MAP_NORMAL, MAP_TYPE_TERRAIN,
		// MAP_TYPE_HYBRID
		// and MAP_TYPE_NONE
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//		map.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
//
//			@Override
//			public boolean onMyLocationButtonClick() {
//				// TODO Auto-generated method stub
//				myLocation = map.getMyLocation();
//				Toast toast = Toast
//						.makeText(
//								getApplicationContext(),
//								myLocation.getLatitude() + ", "
//										+ myLocation.getLongitude(),
//								Toast.LENGTH_SHORT);
//				toast.show();
//				// map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
//				// myLocation.getLatitude(), myLocation.getLongitude()),
//				// 15));
//
//				return true;
//			}
//		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
