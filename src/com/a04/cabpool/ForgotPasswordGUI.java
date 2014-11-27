package com.a04.cabpool;

import com.a04.cabpool.R;
import com.a04.cabpool.R.id;
import com.a04.cabpool.R.layout;
import com.a04.cabpool.R.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ForgotPasswordGUI extends AbstractGUIActivity {
	
	private TextView logIn;
	private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        
        logIn = (TextView) findViewById(R.id.text_logIn);
        submit = (Button) findViewById(R.id.forgot_submit);
        
        logIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ForgotPasswordGUI.this, LoginGUI.class);
				startActivity(intent);
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
