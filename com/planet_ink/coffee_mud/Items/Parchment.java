package com.planet_ink.coffee_mud.Items;

import com.planet_ink.coffee_mud.interfaces.*;
public class Parchment extends GenReadable
{
	public Parchment()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a piece of parchment";
		displayText="a piece of parchment here.";
		description="looks kinda like a piece of paper";
	}
	
	public Environmental newInstance()
	{
		return new Parchment();
	}
	
}
