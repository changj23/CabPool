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
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
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

					// list out all the offers found
					for (ParseObject cab : cabList) {
						// Only post offers
						if (cab.getBoolean("isOffering")) {
							Log.d("offersFound", cab.getString("cabID"));
							if (cab.getInt("maxPassengers") > cab
									.getInt("numPassengers")) {
								if (cab.getString("gender").equals("Either")
										|| cab.getString("gender")
												.equals(currentUser
														.getString("gender"))) {
									if (cab.getInt("minRating") <= currentUser
											.getInt("rating")) {
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

				cabID = value.substring(8, 13);

				// Find all users with cabID				
				ParseQuery offerersQuery = ParseUser.getQuery();
				offerersQuery.whereEqualTo("currentCabId", cabID);
				
				ParseQuery pushQuery = ParseInstallation.getQuery();
				pushQuery.whereMatchesQuery("user", offerersQuery);
				ParsePush push = new ParsePush();
				Log.d("Push", "1");
				push.setQuery(pushQuery); 
				push.setMessage("Test");
				push.sendInBackground();
				
/*				offerersQuery.findInBackground(new FindCallback<ParseUser>() {
					@Override
					public void done(List<ParseUser> offerersList, ParseException e) {
						Log.d("debug", "1");
						ArrayList<String> arrayList = new ArrayList<String>();
						if (e == null) {
							//Integer i2 = new Integer(offerersList.size());
							//Log.d("debug", "List size: " + i2.toString());
							for (ParseUser offerer : offerersList) {
								Log.d("debug", "55");
								Log.d("debug", offerer.getString("name"));
								if (offerer.getBoolean("offering") == true) {
									Log.d("debug", offerer.getString("currentCabId"));
									if (offerer.getString("currentCabId").equals(cabID)) {
										Log.d("Test", "Offer");
										Log.d("Test", offerer.getString("objectId"));
										arrayList.add(offerer.getString("objectId"));
										Integer i = new Integer(arrayList.size());
										Log.d("Test", i.toString());
										Log.d("Test", offerer.getString("name"));
									}
								}
								Log.d("debug", "2");
								Log.d("debug", cabID);
								
								// For each offerer in the arrayList, send a push notification asking them to accept/reject request
								for(int i = 0; i < arrayList.size(); i ++){
									// create an acceptance object to track responses
									ParseObject acceptance = new ParseObject("Acceptance");
									acceptance.put("cabID",cabID);
									acceptance.put("offererID", arrayList.get(i));
									acceptance.saveInBackground();
								}								 
							}
						}
					}
				});*/
			}

		});

		cancelRequestButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// delete request filter
				saveFilter((ParseObject) currentUser.get("filter"));
				getFilter().deleteInBackground();

				currentUser.put("requesting", false);
				currentUser.remove("filter");
				currentUser.saveInBackground();

				finish();
				// delete request filter
				saveFilter((ParseObject) currentUser.get("filter"));
				getFilter().deleteInBackground();

				currentUser.put("requesting", false);
				currentUser.remove("filter");
				currentUser.saveInBackground();
				finish();
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
