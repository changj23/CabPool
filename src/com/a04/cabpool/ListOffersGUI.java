package com.a04.cabpool;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ListOffersGUI extends AbstractGUIActivity {

	private ParseUser currentUser;
	private Button cancelRequestButton;
	private ParseObject filter;
	private ListView offersListView;
	private ArrayAdapter<String> adapter;
	private String cabID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_offers);

		offersListView = (ListView) findViewById(R.id.offersListView);
		cancelRequestButton = (Button) findViewById(R.id.cancelRequestButton);
		// this.adapter = adapter;

		currentUser = ParseUser.getCurrentUser();
		currentUser.put("rating", 3);
		currentUser.saveInBackground();

		// query for a list of offers
		ParseQuery<ParseObject> cabQuery = ParseQuery.getQuery("Cab");

		cabQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cabList, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {

					ArrayList<String> arrayList = new ArrayList<String>();
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							ListOffersGUI.this, R.layout.simplerow, arrayList);
					offersListView.setAdapter(adapter);

					// Toast.makeText(getApplicationContext(), "TEST1",
					// Toast.LENGTH_LONG).show();

					// list out all the offers found
					for (ParseObject cab : cabList) {
						// Only post offers
						if (cab.getBoolean("isOffering")) {
							Log.d("offersFound", cab.getString("cabID"));
							if (cab.getInt("maxPassengers") > cab
									.getInt("numPassengers")) {
								Log.d("Pasengers", cab.getString("cabID"));
								if (cab.getString("gender").equals("Either")
										|| cab.getString("gender")
												.equals(currentUser
														.getString("gender"))) {
									Log.d("gender", cab.getString("cabID"));
									Integer in1 = new Integer(cab
											.getInt("minRating"));
									Integer in2 = new Integer(currentUser
											.getInt("rating"));
									Log.d("minRating", in1.toString());
									Log.d("userRating", in2.toString());
									if (cab.getInt("minRating") <= currentUser
											.getInt("rating")) {
										Log.d("rating", cab.getString("cabID"));
										adapter.add("Cab ID: "
												+ cab.getString("cabID"));
									}
								}
							}
						}
					}
					if (adapter.isEmpty()) {
						Toast.makeText(ListOffersGUI.this,
								"No offers available", Toast.LENGTH_SHORT)
								.show();
					}

				} else {
					Toast.makeText(ListOffersGUI.this, e.getLocalizedMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}

		});

		offersListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String value = (String) adapter.getItemAtPosition(position);
				Toast.makeText(ListOffersGUI.this, value, Toast.LENGTH_SHORT)
						.show();

				// Find all users with cadID
				ParseQuery<ParseObject> offerersQuery = ParseQuery
						.getQuery("User");
				offerersQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> offerersList,
							ParseException e) {
						
						ArrayList<String> arrayList = new ArrayList<String>();
						for(ParseObject offerer : offerersList){
							if (offerer.get("currentCabId").equals(cabID)){							
								arrayList.add(offerer.getString("objecetID"));
								Toast.makeText(getApplicationContext(), offerer.getString("name"), Toast.LENGTH_SHORT).show();
							}
						}
						
						//For each offerer in the arrayList, send a parse notification asking them to accept/reject request
						
						
					}
				});
			}

		});

		cancelRequestButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
<<<<<<< HEAD
					// delete request filter
					saveFilter((ParseObject) currentUser.get("filter"));
					getFilter().deleteInBackground();

					currentUser.put("requesting", false);
					currentUser.remove("filter");
					currentUser.saveInBackground();	

					finish();			
=======
				// delete request filter
				saveFilter((ParseObject) currentUser.get("filter"));
				getFilter().deleteInBackground();

				currentUser.put("requesting", false);
				currentUser.remove("filter");
				currentUser.saveInBackground();
				Intent intent = new Intent(ListOffersGUI.this,
						MainMenuGUI.class);
				startActivity(intent);
				finish();
>>>>>>> 32a8b4b78dc41fd7965694a8b6c5817a51173bd5
			}
		});
	}

	private void saveFilter(ParseObject filter) {
		this.filter = filter;
	}

	private ParseObject getFilter() {
		return this.filter;
	}
}
