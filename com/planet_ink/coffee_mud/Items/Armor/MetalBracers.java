package com.planet_ink.coffee_mud.Items.Armor;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;

public class MetalBracers extends Armor
{
	public MetalBracers()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a pair of metal bracers";
		displayText="a pair of metal bracers lie here.";
		description="Good and solid protection for your arms.";
		properWornBitmap=Item.ON_LEFT_WRIST | Item.ON_RIGHT_WRIST | Item.ON_ARMS;
		wornLogicalAnd=true;
		baseEnvStats().setArmor(4);
		baseEnvStats().setWeight(10);
		baseEnvStats().setAbility(0);
		baseGoldValue=10;
		recoverEnvStats();
		material=Armor.METAL;
	}
	public Environmental newInstance()
	{
		return new MetalBracers();
	}
}
