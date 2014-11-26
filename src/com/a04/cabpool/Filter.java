package com.a04.cabpool;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Filters")

public class Filter extends ParseObject {
	
	// Constructor
	public Filter(){
		
	}
	
	public int getMaxPassengers(){
		return getInt("maxPassengers");
	}
	
	public int getDetourTime(){
		return getInt("detourTime");
	}
	
	public int getMinRating(){
		return getInt("minRating");
	}
}
