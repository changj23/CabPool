package com.a04.cabpool;

import java.util.Calendar;
import java.util.Date;

import com.a04.cabpool.R;
import com.a04.cabpool.R.id;
import com.a04.cabpool.R.layout;
import com.a04.cabpool.R.menu;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileGUI extends AbstractGUIActivity {

	private ParseUser currentUser;
	private TextView username;
	private TextView name;
	private EditText password;
	private EditText confirmPassword;
	private TextView birthDate;
	private int bYear;
	private int bMonth;
	private int bDay;
	private EditText email;
	private EditText creditCard;
	private EditText expiryDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	private Button submit;
	private Button delete;

	final Context context = this;
	static final int DATE_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		username = (TextView) findViewById(R.id.username);
		name = (TextView) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		confirmPassword = (EditText) findViewById(R.id.confirm);
		birthDate = (TextView) findViewById(R.id.birthday);
		creditCard = (EditText) findViewById(R.id.credit_card_num);
		expiryDate = (EditText) findViewById(R.id.expiry_date);
		submit = (Button) findViewById(R.id.button_submit);
		delete = (Button) findViewById(R.id.deleteButton);

		currentUser = ParseUser.getCurrentUser();

		username.setText(currentUser.getUsername());
		name.setText(currentUser.getString("name"));
		email.setText(currentUser.getEmail());

		// get the birth date
		bYear = currentUser.getDate("birthDate").getYear() + 1900;
		bMonth = currentUser.getDate("birthDate").getMonth() + 1;
		bDay = currentUser.getDate("birthDate").getDate();

		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append(currentUser.getDate("birthDate").getDate());
		Log.d("date", sb.toString());

		birthDate.setText(bMonth + "-" + bDay + "-" + bYear);

		creditCard.setText(currentUser.getString("cardNum"));

		// get the expiry date
		mYear = currentUser.getDate("expDate").getYear() + 1900; // add 1900 for
																	// gregorian
																	// calendar
		mMonth = currentUser.getDate("expDate").getMonth();
		mDay = currentUser.getDate("expDate").getDate();
		expiryDate.setText(mMonth + 1 + "-" + mDay + "-" + mYear);

		expiryDate.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
				return false;
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String emailInput = email.getText().toString();
				String creditInput = creditCard.getText().toString();
				String passwordInput = password.getText().toString();
				String confirmInput = confirmPassword.getText().toString();

				Date expiry = new Date(mYear - 1900, mMonth, mDay); // subtract
																	// 1900 for
																	// gregorian
																	// calendar

				if (emailInput.equals("") || creditInput.equals("")
						|| passwordInput.equals("") || confirmInput.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please enter all your information!",
							Toast.LENGTH_SHORT).show();
				}else if (passwordInput.equals(confirmInput) == false) {
					Toast.makeText(getApplicationContext(),
							"Passwords do not match",
							Toast.LENGTH_SHORT).show();
				}
				else {
					currentUser.setPassword(passwordInput);
					currentUser.setEmail(emailInput);
					currentUser.put("cardNum", creditInput);
					currentUser.put("expDate", expiry);
					currentUser.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if (e == null) {

								// successfully saved offer object
								Toast.makeText(getApplicationContext(),
										"Changes saved!", Toast.LENGTH_SHORT)
										.show();

								// set current user in "offering" state

								// go to offer in progress gui
								Intent intent = new Intent(EditProfileGUI.this,
										MainMenuGUI.class);
								startActivity(intent);

								// finish activity so the user can't
								// come back here
								finish();
							} else if (e.getCode() == 203) {
								Toast.makeText(
										getApplicationContext(),
										"An account with this email already exists",
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(EditProfileGUI.this,
										e.getLocalizedMessage(),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
				}

			}

		});

		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Confirm Delete Account");
				builder.setMessage("Are you sure you want to delete your account?");
				builder.setCancelable(true);
				builder.setPositiveButton("Confirm",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									currentUser.delete();
									ParseUser.logOut();
									Toast.makeText(getApplicationContext(),
											"Your account has been deleted",
											Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(
											EditProfileGUI.this, LoginGUI.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(intent);
									finish();

								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

				AlertDialog alert = builder.create();
				alert.show();

			}

		});
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	};

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private void updateDisplay() {
		expiryDate.setText(new StringBuilder().append(mMonth + 1).append("-")
				.append(mDay).append("-").append(mYear).append(" "));
	};

};

// the callback when the user sets the date in the dialog

// @Override
// public boolean onCreateOptionsMenu(Menu menu) {
// // Inflate the menu; this adds items to the action bar if it is present.
// getMenuInflater().inflate(R.menu.main, menu);
// return true;
// }

// @Override
// public boolean onOptionsItemSelected(MenuItem item) {
// // Handle action bar item clicks here. The action bar will
// // automatically handle clicks on the Home/Up button, so long
// // as you specify a parent activity in AndroidManifest.xml.
// int id = item.getItemId();
// if (id == R.id.action_settings) {
// return true;
// }
// return super.onOptionsItemSelected(item);
// }

