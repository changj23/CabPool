package com.a04.cabpool;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class MainMenuGUI extends AbstractGUIActivity {

	private Button requestCabButton;
	private Button offerCabButton;
	private TextView message;
	private boolean offering;
	private boolean requesting;
	private Button arrivedButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		requestCabButton = (Button) findViewById(R.id.requestCab);
		offerCabButton = (Button) findViewById(R.id.offerCab);
		arrivedButton = (Button) findViewById(R.id.arrived);
		message = (TextView) findViewById(R.id.message);

		message.setText("Hello, "
				+ ParseUser.getCurrentUser().getString("name") + "!");

		// determine if current user is anonymous
		if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
			// if user is anonymous, send user to login page
			Intent i = new Intent(MainMenuGUI.this, LoginGUI.class);
			startActivity(i);

			finish();
		} else {
			// if user is not anonymous
			// get current user data from parse.com
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				// let logged in users stay

				// enable or disable offer/request buttons depending on the
				// state of the user
				refresh();

				// TEMPORARY HARD CODED CABID
				// currentUser.put("currentCabId", "12345");
				// currentUser.saveInBackground();

			} else {
				// send user to login page if currentUser doesn't exist
				Intent i = new Intent(MainMenuGUI.this, LoginGUI.class);
				startActivity(i);

				finish();
			}
		}

		// request cab button handler
		requestCabButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getRequesting() == true) {
					Intent intent = new Intent(MainMenuGUI.this,
							ListOffersGUI.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainMenuGUI.this,
							RequestCabGUI.class);
					startActivity(intent);
				}
				// finish();
			}
		});

		// offer cab button handler
		offerCabButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getOffering() == true) {
					Intent intent = new Intent(MainMenuGUI.this,
							OfferInProgressGUI.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(MainMenuGUI.this,
							OfferCabGUI.class);
					startActivity(intent);
				}

				// finish();
			}
		});
	}

	private void setRequesting(boolean requesting) {
		this.requesting = requesting;
	}

	private boolean getRequesting() {
		return this.requesting;
	}

	private void setOffering(boolean offering) {
		this.offering = offering;
	}

	private boolean getOffering() {
		return this.offering;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		refresh();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {
		ParseUser currentUser = ParseUser.getCurrentUser();

		offering = currentUser.getBoolean("offering");
		requesting = currentUser.getBoolean("requesting");


//		 if (offering == true) {
//		 offerCabButton.setText("Resume Offer In Progress");
//		 requestCabButton.setText("Cannot request - Offer in progress");
//		 requestCabButton.setEnabled(false); } else {
//		 offerCabButton.setText("Offer Cab");
//		 requestCabButton.setEnabled(true); }
//		  
//		 if (requesting == true) {
//		 requestCabButton.setText("Resume Request In Progress");
//		 offerCabButton.setText("Cannot offer - Request in progress");
//		 offerCabButton.setEnabled(false); } else {
//		 requestCabButton.setText("Request Cab");
//		 offerCabButton.setEnabled(true); }
		 
	}
}
