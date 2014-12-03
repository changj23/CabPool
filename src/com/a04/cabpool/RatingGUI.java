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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class RatingGUI extends AbstractGUIActivity {
	
	private ParseUser currentUser;
	public float cabRatingFloat;
	public float cabRatingNumFloat;
	public Number oldRating;
	public Number cabRatingNum;
	public Number cabRating;
	public float oldRatingFloat;
	public boolean isRatePressed;
	public boolean isEnabled;
	public Button rateCabButton;
	public RatingBar cabRatingBar;
	public TextView Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        rateCabButton = (Button) findViewById(R.id.rateCabButton);
		Message = (TextView) findViewById(R.id.Message);
		cabRatingBar = (RatingBar) findViewById(R.id.cabRatingBar);
        
        cabRatingFloat=cabRatingBar.getRating();
        
        rateCabButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cabRatingNum != null) {
					oldRating = currentUser.getNumber("rating");
					cabRatingNum = currentUser.getNumber("ratingNum");
					oldRatingFloat = oldRating.floatValue();
					cabRatingNumFloat = cabRatingNum.floatValue();
					float num = cabRatingNumFloat * oldRatingFloat;
					num = num + cabRatingFloat;
					cabRatingNumFloat = cabRatingNumFloat + 1;
					cabRatingFloat = num / cabRatingNumFloat;
					currentUser.put("rating", cabRatingFloat);
					currentUser.put("ratingNum", cabRatingNumFloat);
					
				}else{
					cabRatingNumFloat=1;
					currentUser.put("rating", cabRatingFloat);
					currentUser.put("ratingNum", cabRatingNumFloat);
				}
				Intent intent = new Intent(RatingGUI.this,
						MainMenuGUI.class);
				startActivity(intent);
				finish();
				
			}
			
		});
        
        cabRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
    		public void onRatingChanged(RatingBar ratingBar, float rating,
    			boolean fromUser) {
     
    			cabRatingFloat=rating;
     
    		}
    	});
        
    }
}
