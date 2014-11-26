package com.a04.cabpool;

import com.a04.cabpool.R;
import com.a04.cabpool.R.id;
import com.a04.cabpool.R.layout;
import com.a04.cabpool.R.menu;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class AbstractGUIActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        } else if (id == R.id.edit_profile) {
        	Intent intent = new Intent(AbstractGUIActivity.this, EditProfileGUI.class);
        	startActivity(intent);
    	} else if (id == R.id.maps) {
    		Intent intent = new Intent(AbstractGUIActivity.this, MapsActivity.class);
    		startActivity(intent);
    	} else if (id == R.id.logout) {
        	ParseUser.logOut();
        	Intent intent = new Intent(AbstractGUIActivity.this, LoginGUI.class);
        	startActivity(intent);
        	finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
