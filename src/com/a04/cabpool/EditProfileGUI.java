package com.a04.cabpool;

import com.a04.cabpool.R;
import com.a04.cabpool.R.id;
import com.a04.cabpool.R.layout;
import com.a04.cabpool.R.menu;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EditProfileGUI extends AbstractGUIActivity {
	
	private ParseUser currentUser;
	private TextView username;
	private TextView name;
	private EditText email;
	private EditText creditCard;
	private EditText expiryDate;
	private Button submit;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
           
        username = (TextView) findViewById(R.id.username);
        name = (TextView) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        creditCard = (EditText) findViewById(R.id.credit_card_num);
        expiryDate = (EditText) findViewById(R.id.expiry_date);
        submit = (Button) findViewById(R.id.button_submit);
        
        currentUser = ParseUser.getCurrentUser();
                
        username.setText("Username: "+ currentUser.getUsername());
        name.setText("Name: " + currentUser.getString("name"));
        email.setText(currentUser.getEmail());
        creditCard.setText(currentUser.getString("cardNum"));
        expiryDate.setText(currentUser.getDate("expDate").toString());
        
        
        
        submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email_str = email.getText().toString();
				String credit_str = creditCard.getText().toString();
				
				currentUser.setEmail(email_str);
				currentUser.put("cardNum", credit_str);
				
				
				currentUser.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							// successfully saved offer object
							Toast.makeText(
									EditProfileGUI.this,
									"Changes saved!",
									Toast.LENGTH_SHORT).show();

							// set current user in "offering" state
							
							// go to offer in progress gui
							Intent intent = new Intent(
									EditProfileGUI.this,
									MainMenuGUI.class);
							startActivity(intent);

							// finish activity so the user can't
							// come back here
							finish();
						} else {
							Toast.makeText(EditProfileGUI.this,
									e.getLocalizedMessage(),
									Toast.LENGTH_SHORT).show();
						}
					}

				});
			}
		});

	}
					
				
//				Intent intent = new Intent(EditProfileGUI.this, MainMenuGUI.class);
//				startActivity(intent);
//				Toast.makeText(getApplicationContext(), "Changes saved!", Toast.LENGTH_SHORT).show();
//				finish();
			
	

			

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
