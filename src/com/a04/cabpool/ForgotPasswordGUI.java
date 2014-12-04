package com.a04.cabpool;

import java.util.List;

import com.a04.cabpool.R;
import com.a04.cabpool.R.id;
import com.a04.cabpool.R.layout;
import com.a04.cabpool.R.menu;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

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


public class ForgotPasswordGUI extends Activity {
	
	private EditText fg_emailInput;
	private String fg_email;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        fg_emailInput =(EditText) findViewById(R.id.forgot_email);

    }
    public void submit(View view) {
    	// TODO Auto-generated method stub
    	fg_email = fg_emailInput.getText().toString();
    	
    	ParseUser.requestPasswordResetInBackground(fg_email,
    			new RequestPasswordResetCallback() {
    		public void done(ParseException e) {
    			if (e == null) {
    				// An email was successfully sent with reset instructions.
    			} else {
    				// Something went wrong. Look at the ParseException to see what's up.
    			}
    		}
    	});
    }


}
