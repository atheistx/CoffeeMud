package com.planet_ink.coffee_mud.Items.Armor;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;

public class LeatherBracers extends Armor
{
	public LeatherBracers()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a pair of leather bracers";
		displayText="a pair of leather bracers are here.";
		description="Strong enough to protect your forearms against the strongest of feathers...";
		properWornBitmap=Item.ON_LEFT_WRIST | Item.ON_RIGHT_WRIST | Item.ON_ARMS;
		wornLogicalAnd=true;
		baseEnvStats().setArmor(1);
		baseEnvStats().setWeight(5);
		baseEnvStats().setAbility(0);
		baseGoldValue=5;
		recoverEnvStats();
		material=Armor.LEATHER;
	}
	public Environmental newInstance()
	{
		return new LeatherBracers();
	}
	
}
