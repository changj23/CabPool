package com.a04.cabpool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginGUI extends Activity {

	private Button loginButton;
	private TextView registerButton;
	private EditText usernameInput;
	private EditText passwordInput;
	private TextView message;
	private String username;
	private String password;
	private TextView forgot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// initializing all widgets
		loginButton = (Button) findViewById(R.id.loginButton);
		registerButton = (TextView) findViewById(R.id.registerButton);
		usernameInput = (EditText) findViewById(R.id.username);
		passwordInput = (EditText) findViewById(R.id.password);
		message = (TextView) findViewById(R.id.message);
		forgot = (TextView) findViewById(R.id.forgot_password);

		// FOR DEBUGGING: set username and password to Khedri
		usernameInput.setText("Khedri");
		passwordInput.setText("Khedri");

		// login button pressed
		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				username = usernameInput.getText().toString();
				password = passwordInput.getText().toString();

				// login
				ParseUser.logInInBackground(username, password,
						new LogInCallback() {

							@Override
							public void done(ParseUser user, ParseException e) {
								// TODO Auto-generated method stub
								if (user != null) {
									// if user exists and is authenticated
									// send them to main menu gui
									Intent intent = new Intent(LoginGUI.this,
											MainMenuGUI.class);
									startActivity(intent);
									// show login successful toast
									Toast.makeText(getApplicationContext(),
											"Login successful!",
											Toast.LENGTH_SHORT).show();
									finish();
								} else {
									message.setText("Login failed");
									Toast.makeText(getApplicationContext(),
											e.getLocalizedMessage(),
											Toast.LENGTH_SHORT).show();
								}
							}

						});
			}

		});

		registerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginGUI.this, RegistrationGUI.class);
				startActivity(intent);
				finish();
			}

		});

		forgot.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginGUI.this,
						ForgotPasswordGUI.class);
				startActivity(intent);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_not_logged_in, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
