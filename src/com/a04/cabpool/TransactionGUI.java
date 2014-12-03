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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class TransactionGUI extends AbstractGUIActivity {
	
	public boolean isPaymentComplete;
	public boolean isEnabled;
	public Button confirmButton;
	public TextView feeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        confirmButton = (Button) findViewById(R.id.confirmButton);
		feeText = (TextView) findViewById(R.id.feeText);
        
        //get fee
        //set text with fee
        String feeString= "Your fee is: ";
        
        feeText.setText(feeString);
        
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
