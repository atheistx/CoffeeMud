package com.planet_ink.coffee_mud.Items;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;


public class SmallChest extends LockableContainer
{
	public SmallChest()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a small chest";
		displayText="a small wooden chest sits here.";
		description="It\\`s of solid wood construction with metal bracings.  The lid has a key hole.";
		capacity=25;
		material=Item.WOODEN;
		baseGoldValue=15;
		baseEnvStats().setWeight(25);
		recoverEnvStats();
	}
	
	public Environmental newInstance()
	{
		return new SmallChest();
	}

}
