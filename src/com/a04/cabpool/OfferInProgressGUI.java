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
	private String cabID;
	private ParseObject offer, filter, cab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_in_progress);

		cancelOfferButton = (Button) findViewById(R.id.cancelOfferButton);

		currentUser = ParseUser.getCurrentUser();

		// get current user's offer so offer info can be displayed
		cabID = currentUser.getString("currentCabId");
		Log.d("cancelOffer", cabID + "");
		ParseObject filter = currentUser.getParseObject("filter");
		saveFilter(filter);
		
		// set offering status in current user to false
		cancelOfferButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(getFilter() != null){
					currentUser.put("offering", false);
					currentUser.remove("currentCabId");
					try {
						currentUser.save();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						Log.d("debug", "removesaveUser: " + e1.getLocalizedMessage());
					}
					
					// find parse cab object whose id matches scanned cabID
					ParseQuery<ParseObject> cabQuery = ParseQuery.getQuery("Cab");
					cabQuery.whereEqualTo("cabID", cabID);
					cabQuery.findInBackground(new FindCallback<ParseObject>(){

						@Override
						public void done(List<ParseObject> cabsList,
								ParseException e) {
							// TODO Auto-generated method stub
							if(e == null){
								// note that no error does not imply a cab was found
								if(cabsList.isEmpty() == false) {
									
									//Why are you still saving cab?
									ParseObject cab = cabsList.get(0);
									Log.d("cabid", "Cab " + cabID + " found!");
									
									saveCab(cab);
									
									// update strict filters for cab
									ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
									userQuery.whereEqualTo("currentCabId", cabID);
									userQuery.findInBackground(new FindCallback<ParseUser>(){

										@Override
										public void done(
												List<ParseUser> usersList,
												ParseException e) {
											// TODO Auto-generated method stub
											if(e == null){
												if(usersList.isEmpty() == true){
													Log.d("debug", "empty");
													getCab().put("isOffering", true);
													getCab().put("minRating", -1);
													getCab().put("maxPassengers", -1);
													getCab().put("numPassengers", 0);
												} else {
													Log.d("debug", "not empty");
													int minRating, maxPassengers, numPassengers;
													int cabMinRating, cabMaxPassengers, cabNumPassengers;
													String gender;
													getCab().put("isOffering", true);
													for(ParseUser user : usersList){
														Log.d("debug", "usersList size: " + usersList.size());
														try {
															getCab().put("minRating", -1);
															getCab().put("maxPassengers", -1);
					
															Log.d("debug", user.getString("name"));
															ParseObject userFilter = user.getParseObject("filter");
															Log.d("debug", "stage 1");
															minRating = userFilter.fetchIfNeeded().getInt("minRating");
															Log.d("debug", "stage 2");
															maxPassengers = userFilter.fetchIfNeeded().getInt("maxPassengers");
															Log.d("debug", "stage 3");
															cabMinRating = getCab().getInt("minRating");
															Log.d("debug", "stage 4");
															cabMaxPassengers = getCab().getInt("maxPassengers");
															Log.d("debug", "stage 5");
															// set strict minRating
															if(minRating > cabMinRating || cabMinRating == -1){
																getCab().put("minRating", minRating);
																Log.d("debug", "stage 5 if");
															}
															Log.d("debug", "stage 6");
															// set strict maxPassengers
															if(maxPassengers < cabMaxPassengers || cabMaxPassengers == -1){
																getCab().put("maxPassengers", maxPassengers);
																Log.d("debug", "stage 6 if");
															}
															Log.d("debug", "stage 7");
															
															// ensure all users are in offering mode
															if(user.getBoolean("offering") == false){
																getCab().put("isOffering", false);
																Log.d("debug", "stage 7 if");
															}
															if(user.getString("gender").equalsIgnoreCase("male")){
																
															}
															Log.d("debug", "user...minRating: " + minRating
																	+ " maxPassengers: " + maxPassengers);
															Log.d("debug", "cab...minRating: " + getCab().getInt("minRating")
																	+ " maxPassengers: " + getCab().getInt("maxPassengers"));
															Log.d("debug", "stage 8");
														} catch (ParseException pe){
															Log.d("debug", pe.getLocalizedMessage());
														}

														
													}

												}
												Log.d("debug", "saving..minRating: " + getCab().getInt("minRating")
														+ " maxPassengers: " + getCab().getInt("maxPassengers"));
												getCab().put("numPassengers", usersList.size());
												getCab().saveInBackground();
												
												// remove filter
												getFilter().deleteInBackground();
												
												// remove data from current user
												currentUser.remove("filter");
												currentUser.remove("destination");
												currentUser.saveInBackground();
											} else {
												// error
												Log.d("error", e.getLocalizedMessage());
											}
										}
										
									});


								} else {
									Toast.makeText(OfferInProgressGUI.this,
											"Cab not found",
											Toast.LENGTH_SHORT).show();
								}
							
							} else {
								Toast.makeText(OfferInProgressGUI.this,
										e.getLocalizedMessage(),
										Toast.LENGTH_SHORT).show();
							}
						}
						
					});
					
					finish();
				} else {
					Toast.makeText(OfferInProgressGUI.this,
							"Filter not found", Toast.LENGTH_SHORT).show();
				}
				
				
				
				
/*				if(getFilter() != null && getOffer() != null){
					// delete filter associated with offer
					getFilter().deleteInBackground();
					
					// delete offer itself
					getOffer().deleteInBackground();
					
					currentUser.put("offering", false);
					currentUser.saveInBackground();

					finish();
				} else {
					Toast.makeText(OfferInProgressGUI.this,
							"Offer or Filtern is null", Toast.LENGTH_SHORT).show();
				}*/

			}
		});

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ParseQuery<ParseUser> requesterQuery = ParseUser.getQuery();
		requesterQuery.whereEqualTo("requesting", true);
		requesterQuery.whereEqualTo("currentCabId", cabID);
		
		requesterQuery.findInBackground(new FindCallback<ParseUser>(){

			@Override
			public void done(List<ParseUser> requestersList, ParseException e) {
				// TODO Auto-generated method stub
				if(e == null){
					if(requestersList.isEmpty() == false) {
						ParseUser requester = requestersList.get(0);
						Log.d("debug", "requester: " + requester.getString("name"));
					} else {
						Log.d("debug", "requestersList is empty");
					}
				} else {
					Log.d("debug", "requesterQuery: " + e.getLocalizedMessage());
				}
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
	private void saveCab(ParseObject cab){
		this.cab = cab;
	}
	
	private ParseObject getCab(){
		return this.cab;
	}
}
