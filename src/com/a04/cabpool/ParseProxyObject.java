package com.a04.cabpool;

import java.io.Serializable;

import com.parse.ParseObject;

public class ParseProxyObject implements Serializable {
	
	ParseObject object;
	
	public ParseProxyObject(ParseObject object){
		this.object = object;
	}
	
	public ParseObject getObject(){
		return this.object;
	}
	
	public void setObject(ParseObject object){
		this.object = object;
	}
	
}
