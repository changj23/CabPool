package com.a04.cabpool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationGUI extends Activity {
	
	ParseUser newUser;
	private Button register;
	private EditText nameInput;
	private EditText usernameInput;
	private EditText passwordInput;
	private EditText retypePassword;
	private EditText cardNumInput;
	private EditText birthDateInput;
	private EditText expDateInput;
	private EditText emailInput;
	private String name;
	private String username;
	private String password;
	private String passwordRetype;
	private String email;
	private String gender;
	private String cardNum;
	private Date birthDate;
	private int bYear;
	private int bMonth;
	private int bDay;
	private Date expDate;
	private int mYear;
	private int mMonth;
	private int mDay;

	static final int DATE_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID1 = 1;

	// Gender button set up:
	public void genderRadio(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.femaleButton:
			if (checked)
				gender = "female";
			break;
		case R.id.maleButton:
			if (checked)
				gender = "male";
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		// Locate EditTexts in main.xml
		nameInput = (EditText) findViewById(R.id.name);
		usernameInput = (EditText) findViewById(R.id.username);
		passwordInput = (EditText) findViewById(R.id.password);
		retypePassword = (EditText) findViewById(R.id.repeatPassword);
		emailInput = (EditText) findViewById(R.id.email);
		birthDateInput = (EditText) findViewById(R.id.birthday);
		cardNumInput = (EditText) findViewById(R.id.credit_card_num);
		expDateInput = (EditText) findViewById(R.id.expiry_date);
		

		// Locate Buttons in main.xml
		register = (Button) findViewById(R.id.registerButton);

		birthDateInput.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID1);
				return false;
			}
		});

		expDateInput.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
				return false;
			}
		});
		
		

		// Register Button Click Listener
		register.setOnClickListener(new OnClickListener() {

			public void onClick(View arg2) {
				// Retrieve the text entered from the EditText
				name = nameInput.getText().toString();
				username = usernameInput.getText().toString();
				password = passwordInput.getText().toString();
				passwordRetype = retypePassword.getText().toString();
				email = emailInput.getText().toString();
				cardNum = cardNumInput.getText().toString();
				expDate = new Date(mYear - 1900, mMonth, mDay);
				birthDate = new Date(bYear - 1900, bMonth, bDay);

				// Check to see if all fields are filled
				if (username.equals("") || password.equals("")
						|| email.equals("") || cardNum.equals("")
						|| name.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please fill in all your information!",
							Toast.LENGTH_LONG).show();

				} else if (false == (password.equals(passwordRetype))) {
					Toast.makeText(getApplicationContext(),
							"Passwords do not match", Toast.LENGTH_SHORT).show();
				} else {
					// Save new user data into Parse.com Data Storage
					newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					newUser.put("email", email);
					newUser.put("name", name);
					newUser.put("birthDate", birthDate);
					newUser.put("cardNum", cardNum);
					newUser.put("expDate", expDate);
					
					newUser.signUpInBackground(new SignUpCallback() {

						@Override
						public void done(ParseException e) {
							
							if (e == null) {
				
								// Show a simple Toast message upon successful registration
								Intent intent = new Intent(RegistrationGUI.this,
										LoginGUI.class);
								startActivity(intent);
								finish();
								// show login successful toast
								Toast.makeText(
										getApplicationContext(),
										"Successfully signed up! Please log in.",
										Toast.LENGTH_SHORT).show();
							} 
							else if (e.getCode() == 202) {
								Toast.makeText(getApplicationContext(),
										"Username is already taken", Toast.LENGTH_SHORT)
										.show();
							}
							else if (e.getCode() == 203) {
								Toast.makeText(getApplicationContext(),
										"An account with this email already exists", Toast.LENGTH_SHORT)
										.show();
								
							}
							else {
								int i = e.getCode();
								StringBuilder sb = new StringBuilder();
								sb.append("");
								sb.append(i);
								String strI = sb.toString();
								
								Log.d("exception", strI);
								
								Toast.makeText(getApplicationContext(),
										"Sign up Error", Toast.LENGTH_SHORT)
										.show();
							}

						}

					});
				}

			}

		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_DIALOG_ID:
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(this, mDateSetListener, year, month,
					day);
		case DATE_DIALOG_ID1:
			final Calendar c1 = Calendar.getInstance();
			int year1 = c1.get(Calendar.YEAR);
			int month1 = c1.get(Calendar.MONTH);
			int day1 = c1.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(this, bDateSetListener, year1, month1,
					day1);
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

	private DatePickerDialog.OnDateSetListener bDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			bYear = year;
			bMonth = monthOfYear;
			bDay = dayOfMonth;
			updateDisplay1();
		}
	};

	private void updateDisplay() {
		expDateInput.setText(new StringBuilder().append(mMonth + 1).append("-")
				.append(mDay).append("-").append(mYear).append(" "));
	};

	private void updateDisplay1() {
		birthDateInput
				.setText(new StringBuilder().append(bMonth + 1).append("-")
						.append(bDay).append("-").append(bYear).append(" "));
	};

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.menu_not_logged_in, menu);
	// return true;
	// }
	//
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
}
