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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        // determine if current user is anonymous
        if(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())){
        	// if user is anonymous, send user to login page
        	Intent i = new Intent(MainMenuGUI.this, LoginGUI.class);
        	startActivity(i);
        	
        	finish();        	
        } else {
        	// if user is not anonymous
        	// get current user data from parse.com
        	ParseUser currentUser = ParseUser.getCurrentUser();
        	if(currentUser != null){
        		// let logged in users stay
        		
        	} else {
        		// send user to login page if currentUser doesn't exist
            	Intent i = new Intent(MainMenuGUI.this, LoginGUI.class);
            	startActivity(i);
            	
            	finish(); 
        	}
        }
        
        requestCabButton = (Button) findViewById(R.id.requestCab);
        offerCabButton = (Button) findViewById(R.id.offerCab);
        message = (TextView) findViewById(R.id.message);
        
        message.setText("Logged in as " + ParseUser.getCurrentUser().getUsername());
        
        // request cab button handler
        requestCabButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainMenuGUI.this, RequestCabGUI.class);
				startActivity(intent);
				//finish();
			}
		});
        
        // offer cab button handler
        offerCabButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainMenuGUI.this, OfferCabGUI.class);
				startActivity(intent);
				//finish();
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
