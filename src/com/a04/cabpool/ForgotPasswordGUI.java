package com.a04.cabpool;

import java.util.List;

import com.a04.cabpool.R;
import com.a04.cabpool.R.id;
import com.a04.cabpool.R.layout;
import com.a04.cabpool.R.menu;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.Parse;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ForgotPasswordGUI extends AbstractGUIActivity {
	
	private TextView logIn;
	private EditText fg_emailInput;
	private String fg_email;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        fg_emailInput=(EditText) findViewById(R.id.forgot_email);
/*        logIn = (TextView) findViewById(R.id.text_LogIn);        
        logIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ForgotPasswordGUI.this, LoginGUI.class);
				startActivity(intent);
				finish();
				
			}
		});*/

    }
    public void submit(View view) {
    	// TODO Auto-generated method stub
    	fg_email = fg_emailInput.getText().toString();
    	
    	ParseUser.requestPasswordResetInBackground(fg_email,
    			new RequestPasswordResetCallback() {
    		public void done(ParseException e) {
    			
    			if (e == null) {
    	            Toast.makeText(getApplicationContext(),
    	                    "An email has been sent.",Toast.LENGTH_SHORT)
    	                    .show();
    				// An email was successfully sent with reset instructions.
    			}else if (e.getCode()==205){
    				Toast.makeText(getApplicationContext(),
							"Email was not found.", Toast.LENGTH_SHORT)
							.show();
    	            
    				// Something went wrong. Look at the ParseException to see what's up.
    			}else{
					int i = e.getCode();
					StringBuilder sb = new StringBuilder();
					sb.append("");
					sb.append(i);
					String strI = sb.toString();
					
					Log.d("exception", strI);
					
					Toast.makeText(getApplicationContext(),
							"Forgot Password Error", Toast.LENGTH_SHORT)
							.show();
    			}
    		}
    	});
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
