package com.a04.cabpool;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class ListOffersGUI extends AbstractGUIActivity {

	private ParseUser currentUser;
	private ListView offersListView;
	private ArrayAdapter<String> adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offers);
        
		//KEEP AS REFERENCE
        
		offersListView = (ListView) findViewById(R.id.offersListView);

		// this.adapter = adapter;

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
							ListOffersGUI.this, R.layout.simplerow, arrayList);
					offersListView.setAdapter(adapter);

					
					if (offersList.isEmpty() == false) {
						Toast.makeText(ListOffersGUI.this,
								offersList.get(0).getString("cabId"),
								Toast.LENGTH_SHORT).show();

						// list out all the offers found
						for (ParseObject offer : offersList) {
							Log.d("offersFound", offer.getString("cabId"));

							adapter.add("Cab id: " + offer.getString("cabId")
									+ "\nDestination: ");

						}
					} else {
						Toast.makeText(ListOffersGUI.this, "No offers found",
								Toast.LENGTH_SHORT).show();

					}

				} else {
					Toast.makeText(ListOffersGUI.this, e.getLocalizedMessage(),
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
				Toast.makeText(ListOffersGUI.this, value,
						Toast.LENGTH_SHORT).show();
			}
			
		});
    }

}

