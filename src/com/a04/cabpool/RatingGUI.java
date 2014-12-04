package com.a04.cabpool;

import java.util.List;
import java.lang.Number;
import java.lang.Float;

import com.a04.cabpool.R;
import com.a04.cabpool.R.id;
import com.a04.cabpool.R.layout;
import com.a04.cabpool.R.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class RatingGUI extends AbstractGUIActivity {

	private ParseUser currentUser;
	public float cabRatingFloat;
	public Button rateCabButton;
	public RatingBar cabRatingBar;
	public TextView Message;
	private String cabID;
	private ParseObject offer, filter, cab;
	public ParseObject userCab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating);
		
		/*ParseObject userCab = new ParseObject("Cab");
		userCab.put("minRating", -1);
		userCab.put("maxPassengers", 5);
		userCab.put("gender", "Male");
		userCab.put("isOffering", false);
		userCab.put("numPassengers", 2);
		userCab.put("cabID", "88888");*/
		

		rateCabButton = (Button) findViewById(R.id.rateCabButton);
		Message = (TextView) findViewById(R.id.Message);
		cabRatingBar = (RatingBar) findViewById(R.id.cabRatingBar);

		cabRatingFloat = cabRatingBar.getRating();

		currentUser = ParseUser.getCurrentUser();
		
		ParseObject filter = currentUser.getParseObject("filter");
		saveFilter(filter);

		rateCabButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				if (getFilter() != null) {
					currentUser.put("offering", false);
					currentUser.remove("currentCabId");

					// find parse cab object whose id matches scanned cabID
					ParseQuery<ParseObject> cabQuery = ParseQuery
							.getQuery("Cab");
					cabQuery.whereEqualTo("cabID", cabID);
					cabQuery.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> cabsList,
								ParseException e) {
							// TODO Auto-generated method stub
							if (e == null) {
								// note that no error does not imply a cab was
								// found
								if (cabsList.isEmpty() == false) {

									// Why are you still saving cab?
									ParseObject cab = cabsList.get(0);
									Log.d("cabid", "Cab " + cabID + " found!");

									saveCab(cab);
									
									Number cabRating=cab.getNumber("cabRating");
									Number cabRatingNum=cab.getNumber("cabRatingNum");
									if (cabRating!=null){
										float floatRating=cabRating.floatValue();
										float floatCabNum=cabRatingNum.floatValue();
										float intermediateNum= floatCabNum*floatRating;
										intermediateNum=intermediateNum+cabRatingFloat;
										floatCabNum=floatCabNum+1;
										floatRating=floatRating/floatCabNum;
										cab.put("cabRating", floatRating);
										cab.put("cabRatingNum", floatCabNum);
										
									}else{
										float floatRating=cabRatingFloat;
										cab.put("cabRating", floatRating);
										float floatCabNum=0;
										floatCabNum=floatCabNum+1;
										cab.put("cabRatingNum", floatCabNum);
									}
									
									Number personRating=currentUser.getNumber("rating");
									Number personRatingNum=currentUser.getNumber("ratingNum");
									if (personRating!=null){
										float floatRating=cabRating.floatValue();
										float floatCabNum=cabRatingNum.floatValue();
										float floatPersonRating=personRating.floatValue();
										float floatPersonNum=personRatingNum.floatValue();
										float intermediateNum= floatPersonNum*floatPersonRating;
										intermediateNum=intermediateNum+floatRating*floatCabNum;
										floatPersonNum=floatPersonNum+floatCabNum;
										floatPersonRating=floatPersonRating/floatPersonNum;
										currentUser.put("rating", floatPersonRating);
										currentUser.put("ratingNum", floatPersonNum);
									}else{
										float floatRating=cabRating.floatValue();
										float floatCabNum=cabRatingNum.floatValue();
										currentUser.put("rating", floatRating);
										currentUser.put("ratingNum", floatCabNum);
									}
									

									// update strict filters for cab
									ParseQuery<ParseUser> userQuery = ParseUser
											.getQuery();
									userQuery.whereEqualTo("cabID", cabID);
									userQuery
											.findInBackground(new FindCallback<ParseUser>() {

												@Override
												public void done(
														List<ParseUser> usersList,
														ParseException e) {
													// TODO Auto-generated
													// method stub
													if (e == null) {
														if (usersList.isEmpty() == true) {
															getCab().put(
																	"isOffering",
																	false);
															getCab().put(
																	"minRating",
																	-1);
															getCab().put(
																	"maxPassengers",
																	-1);
															getCab().put(
																	"numPassengers",
																	0);
														} else {
															int minRating, maxPassengers, numPassengers;
															int cabMinRating, cabMaxPassengers, cabNumPassengers;
															String gender;
															getCab().put(
																	"isOffering",
																	true);
															for (ParseUser user : usersList) {
																minRating = user
																		.getParseObject(
																				"filter")
																		.getInt("minRating");
																maxPassengers = user
																		.getParseObject(
																				"filter")
																		.getInt("maxPassengers");
																cabMinRating = getCab()
																		.getInt("minRating");
																cabMaxPassengers = getCab()
																		.getInt("maxPassengers");
																// set strict
																// minRating
																if (minRating > cabMinRating
																		|| cabMinRating == -1) {
																	getCab().put(
																			"minRating",
																			minRating);
																}

																// set strict
																// maxPassengers
																if (maxPassengers < cabMaxPassengers
																		|| cabMaxPassengers == -1) {
																	getCab().put(
																			"maxPassengers",
																			maxPassengers);
																}

																// ensure all
																// users are in
																// offering mode
																if (user.getBoolean("offering") == false) {
																	getCab().put(
																			"isOffering",
																			false);
																}

															}

														}

														getCab().put(
																"numPassengers",
																usersList
																		.size());
														getCab().saveInBackground();
													} else {
														// error
														Log.d("error",
																e.getLocalizedMessage());
													}
												}

											});

								} else {
									
								}

							} else {
								Toast.makeText(getApplicationContext(),
										e.getLocalizedMessage(),
										Toast.LENGTH_SHORT).show();
							}
						}

					});

					// remove filter
					getFilter().deleteInBackground();

					// remove data from current user
					currentUser.remove("filter");
					currentUser.remove("destination");
					currentUser.saveInBackground();

					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Filter not found",
							Toast.LENGTH_SHORT).show();
				}
				Intent intent = new Intent(RatingGUI.this, MainMenuGUI.class);
				startActivity(intent);
				Toast.makeText(
						getApplicationContext(),
						"Your rating has been submitted. Thank you for using CabPool!",
						Toast.LENGTH_SHORT).show();
				finish();

			}

		});

		cabRatingBar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {

						cabRatingFloat = rating;

					}
				});

	}

	private ParseObject getFilter() {
		return this.filter;
	}

	private void saveCab(ParseObject cab) {
		this.cab = cab;
	}

	private ParseObject getCab() {
		return this.cab;
	}
	private void saveFilter(ParseObject filter) {
		this.filter = filter;
	}
}
