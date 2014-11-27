package com.a04.cabpool;

import com.a04.cabpool.R;
import com.a04.cabpool.R.id;
import com.a04.cabpool.R.layout;
import com.a04.cabpool.R.menu;
import com.parse.Parse;
import com.parse.ParseUser;

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
	
	private TextView username;
	private EditText password;
	private EditText email;
	private Button submit;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
       
        username = (TextView) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        submit = (Button) findViewById(R.id.button_submit);
                
        username.setText("Username: "+ParseUser.getCurrentUser().getUsername());
        email.setText(ParseUser.getCurrentUser().getEmail());
        
        submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditProfileGUI.this, MainMenuGUI.class);
				startActivity(intent);
				Toast.makeText(getApplicationContext(), "Changes saved!", Toast.LENGTH_SHORT).show();
				finish();
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
