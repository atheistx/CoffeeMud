package com.planet_ink.coffee_mud.Items.Armor;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;

public class StuddedLeatherArmor extends Armor
{
	public StuddedLeatherArmor()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="suit of studded leather armor";
		displayText="a suit of leather armor reinforced with decorative studs";
		description="A suit of studded leather armor including everything to protect the body, legs and arms.";
		properWornBitmap=Item.ON_TORSO | Item.ON_ARMS | Item.ON_LEGS;
		wornLogicalAnd=true;
		baseEnvStats().setArmor(22);
		baseEnvStats().setAbility(0);
		baseEnvStats().setWeight(35);
		baseGoldValue=40;
		recoverEnvStats();
		material=Armor.LEATHER;
	}
	public Environmental newInstance()
	{
		return new StuddedLeatherArmor();
	}
}
