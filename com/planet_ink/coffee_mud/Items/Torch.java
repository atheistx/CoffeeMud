package com.planet_ink.coffee_mud.Items;


import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.application.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import java.util.*;

public class Torch extends LightSource
{
	public Torch()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a torch";
		displayText="a small straw torch sits here.";
		description="It looks like it is lightly covered in oil near the end.";
		durationTicks=30;
		
		material=Item.WOODEN;
		destroyedWhenBurnedOut=true;
		baseGoldValue=1;
	}
	public Environmental newInstance()
	{
		return new Torch();
	}

	
}
