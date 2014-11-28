package com.a04.cabpool;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class OfferInProgressGUI extends AbstractGUIActivity {

	private Button cancelOfferButton;
	private ParseUser currentUser;
	private String cabId;
	private ParseObject offer;
	private ParseObject filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_in_progress);

		cancelOfferButton = (Button) findViewById(R.id.cancelOfferButton);

		currentUser = ParseUser.getCurrentUser();

		// get current user's offer so offer info can be displayed
		cabId = currentUser.getString("currentCabId");
		Log.d("cancelOffer", cabId + "");
		ParseQuery<ParseObject> offerQuery = ParseQuery.getQuery("Offer");
		offerQuery.whereEqualTo("cabId", cabId);
		offerQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> offersList, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					// no errors (doesn't imply an object was found)
					offer = offersList.get(0); // get offer
					filter = offer.getParseObject("filters"); // get associated filter
					
					Toast.makeText(OfferInProgressGUI.this, offer.getObjectId(), Toast.LENGTH_SHORT).show();
					
					saveFilter(filter);
					saveOffer(offer);
				} else {
					Toast.makeText(OfferInProgressGUI.this,
							e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				}
			}

		});

		// set offering status in current user to false
		cancelOfferButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// delete filter associated with offer
				getFilter().deleteInBackground();
				
				// delete offer itself
				getOffer().deleteInBackground();
				
				currentUser.put("offering", false);
				currentUser.saveInBackground();

				finish();
			}
		});

	}

	private void saveOffer(ParseObject offer) {
		this.offer = offer;
	}

	private ParseObject getOffer() {
		return this.offer;
	}

	private void saveFilter(ParseObject filter) {
		this.filter = filter;
	}

	private ParseObject getFilter() {
		return this.filter;
	}
}
