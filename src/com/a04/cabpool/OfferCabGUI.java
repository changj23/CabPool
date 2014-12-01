package com.a04.cabpool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.a04.cabpool.MapsActivity.GeocoderTask;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class OfferCabGUI extends AbstractGUIActivity {

	private ParseUser currentUser;
	private Spinner genderSpinner;
	private NumberPicker ratingNumberPicker, maxPassNumberPicker;
	private Button createOfferButton, destinationSearch;
	private String gender;
	private int minRating, maxPassengers;
	private ParseObject filter, offer;
	

	private String cabID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer);

		// Reset cabID
		cabID = null;

		// for QR code scanner - Does the QR reader go back to this screen
		// after?
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();

		createOfferButton = (Button) findViewById(R.id.createOffer);
		genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
		ratingNumberPicker = (NumberPicker) findViewById(R.id.ratingNumberPicker);
		maxPassNumberPicker = (NumberPicker) findViewById(R.id.maxPassNumberPicker);
		destinationSearch = (Button) findViewById(R.id.destinationSearchButton);
		

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
				Intent intent = new Intent(OfferCabGUI.this, MapsActivity.class);
				startActivity(intent);
			}
		});
		

		// create offer button click handler
		createOfferButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender = String.valueOf(genderSpinner.getSelectedItem());
				minRating = ratingNumberPicker.getValue();
				maxPassengers = maxPassNumberPicker.getValue();

				// create new filter
				ParseObject filter = new ParseObject("Filters");
				filter.put("minRating", minRating);
				filter.put("gender", gender);
				filter.put("maxPassengers", maxPassengers);
				filter.put("filterType", "offer");
				saveFilter(filter);
				filter.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							// successfully saved filter object
							// Toast.makeText(OfferCabGUI.this,
							// "Successfully saved filters",
							// Toast.LENGTH_SHORT).show();

							// create Offer object and relate it to the filters
							// object
							ParseObject offer = new ParseObject("Offer");
							offer.put("filters", getFilter());
							offer.put("offerer", ParseUser.getCurrentUser());
							offer.put("valid", true);
							saveOffer(offer);

							offer.put("cabId", cabID);

							offer.saveInBackground(new SaveCallback() {

								@Override
								public void done(ParseException e) {
									// TODO Auto-generated method stub
									if (e == null) {
										// successfully saved offer object
										Toast.makeText(
												OfferCabGUI.this,
												"Successfully saved filter and offer",
												Toast.LENGTH_SHORT).show();

										// set current user in "offering" state
										currentUser.put("offering", true);
										currentUser.put("currentCabId", cabID);
										currentUser.saveInBackground();

										// go to offer in progress gui
										Intent intent = new Intent(
												OfferCabGUI.this,
												OfferInProgressGUI.class);
										startActivity(intent);

										// finish activity so the user can't
										// come back here
										finish();
									} else {
										Toast.makeText(OfferCabGUI.this,
												e.getLocalizedMessage(),
												Toast.LENGTH_SHORT).show();
									}
								}

							});
						} else {
							Toast.makeText(OfferCabGUI.this,
									e.getLocalizedMessage(), Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
			}
		});
	}

	// QR code scanner
	// Duplicated from RequestCabGUI
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanResult != null) {
			// handle scan result
			cabID = scanResult.getContents();
			// Toast.makeText(OfferCabGUI.this, cabID,
			// Toast.LENGTH_LONG).show();
			if (cabID == null) {
				Toast.makeText(OfferCabGUI.this, "Scan Cancelled",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(OfferCabGUI.this, MainMenuGUI.class);
				finish();
			}
			// Verify cabID
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
