package com.planet_ink.coffee_mud.Items;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import java.util.*;

public class Ring extends StdItem
{

	public Ring()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="an ordinary ring";
		displayText="a nondescript ring sits here doing nothing.";
		description="It looks like a ring you wear on your fingers.";
		
		properWornBitmap=Item.ON_LEFT_FINGER | Item.ON_RIGHT_FINGER;
		wornLogicalAnd=false;
		baseGoldValue=50;
		recoverEnvStats();
	}
	public Environmental newInstance()
	{
		return new Ring();
	}
}
