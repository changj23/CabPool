package com.a04.cabpool;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Offer")
public class Offer extends ParseObject {
		
	// Constructor
	public Offer(){
		
	}
	
	public String getCabID(){
		return getString("CabID");
	}
	
	public ParseGeoPoint getLocation(){
		return getParseGeoPoint("location");
	}
	
//	public ArrayList<String> getPassengerIDs(){
//		return getList("passengerIDs");
//	}
	
	public boolean isValid(){
		return getBoolean("valid");
	}
	
	public ParseObject getFilters(){
		return getParseObject("filters");
	}
	
	
}
