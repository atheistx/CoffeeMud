package com.planet_ink.coffee_mud.MOBS;

import com.planet_ink.coffee_mud.interfaces.Environmental;

public class GreenDragon extends Dragon
{
	
	public GreenDragon()
	{
		// ===== call the super class constructor
		super(GREEN);
	}
	
	public Environmental newInstance()
	{
		// ===== Hatch one!
		return new GreenDragon();
	}
}
