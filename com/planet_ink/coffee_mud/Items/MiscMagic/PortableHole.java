package com.planet_ink.coffee_mud.Items.MiscMagic;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;


public class PortableHole extends BagOfHolding implements MiscMagic
{
	public PortableHole()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a small disk";
		displayText="a small black disk can be found up here.";
		description="It looks like a small disk.";
		secretIdentity="A Portable Hole";
		baseEnvStats().setLevel(1);
		capacity=200 * baseEnvStats().level();

		baseGoldValue=15000;
		baseEnvStats().setDisposition(baseEnvStats().disposition()|Sense.IS_BONUS);
		recoverEnvStats();



	}
	
	public Environmental newInstance()
	{
		return new PortableHole();
	}
	
}
