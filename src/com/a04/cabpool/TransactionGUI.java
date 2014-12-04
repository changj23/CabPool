package com.a04.cabpool;

import com.a04.cabpool.R;
import com.a04.cabpool.R.id;
import com.a04.cabpool.R.layout;
import com.a04.cabpool.R.menu;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class TransactionGUI extends AbstractGUIActivity {
	
	public boolean isPaymentComplete;
	public boolean isEnabled;
	public Button confirmButton;
	public TextView feeText;
	private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        
        confirmButton = (Button) findViewById(R.id.confirmButton);
		feeText = (TextView) findViewById(R.id.feeText);
        
        //get fee
        //set text with fee
		String cabID=currentUser.getString("currentCabID");
		ParseQuery<ParseObject> cabQuery = ParseQuery
				.getQuery("Cab");
		cabQuery.whereEqualTo("cabID", cabID);
		cabQuery.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> cabsList,
					ParseException e) {
				if (e==null){
					ParseObject cab= cabsList.get(0);
					int num=cab.getInt("numPassengers");
					long millisecs=currentUser.getLong("timeMillis");
					Calendar rightNow = Calendar.getInstance();
					long time=rightNow.getTimeInMillis();
					long diff = time-millisecs;
					diff=diff/60000;
					diff=diff/num;
					String feeString= "Your fee is: $"+diff;
			        feeText.setText(feeString);
				}else{
					long millisecs=currentUser.getLong("timeMillis");
					Calendar rightNow = Calendar.getInstance();
					long time=rightNow.getTimeInMillis();
					long diff = time-millisecs;
					diff=diff/60000;
					String feeString= "Your fee is: $"+diff;
			        feeText.setText(feeString);
				}
			}
		});
		
		
        
        
        confirmButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(TransactionGUI.this,
						RatingGUI.class);
				startActivity(intent);
				Toast.makeText(getApplicationContext(),
						"Transaction successful!",
						Toast.LENGTH_SHORT).show();
				finish();
				
			}
			
		});
    }
}
