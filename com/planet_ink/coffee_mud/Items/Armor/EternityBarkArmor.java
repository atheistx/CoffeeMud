package com.planet_ink.coffee_mud.Items.Armor;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;

public class EternityBarkArmor extends Armor
{
	public EternityBarkArmor()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a suit of Eternity Tree Bark Armor";
		displayText="a suit of Eternity tree bark armor sits here.";
		description="This suit of armor is made from the bark of the Fox god\\`s Eternity Tree(armor:  100 and as light as leather armor--wearable by theives)";
		properWornBitmap=Item.ON_TORSO | Item.ON_ARMS | Item.ON_LEGS;
		wornLogicalAnd=true;
		baseGoldValue+=25000;
		baseEnvStats().setArmor(100);
		baseEnvStats().setAbility(0);
		baseEnvStats().setWeight(15);
		baseEnvStats().setDisposition(baseEnvStats().disposition()|Sense.IS_BONUS);
		recoverEnvStats();
		material=Armor.LEATHER;
	}

	public Environmental newInstance()
	{
		return new EternityBarkArmor();
	}
}
