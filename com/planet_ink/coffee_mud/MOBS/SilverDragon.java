package com.planet_ink.coffee_mud.MOBS;

import com.planet_ink.coffee_mud.interfaces.Environmental;

public class SilverDragon extends Dragon
{
	
	public SilverDragon()
	{
		// ===== call the super class constructor 
		super(SILVER);
	}
	
	public Environmental newInstance()
	{
		// ===== Hatch one!
		return new SilverDragon();
	}
}
