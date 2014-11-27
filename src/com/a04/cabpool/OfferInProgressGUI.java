package com.a04.cabpool;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;


public class OfferInProgressGUI extends AbstractGUIActivity {

	private Button cancelOfferButton;
	private ParseUser currentUser;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_in_progress);
        
        cancelOfferButton = (Button) findViewById(R.id.cancelOfferButton);
        
        currentUser = ParseUser.getCurrentUser();
        
        // set offering status in current user to false
        cancelOfferButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentUser.put("offering", false);
				currentUser.saveInBackground();
				finish();
			}
		});
        
    }
}
