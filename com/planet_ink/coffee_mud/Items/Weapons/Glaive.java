package com.planet_ink.coffee_mud.Items.Weapons;
import com.planet_ink.coffee_mud.interfaces.*;

public class Glaive extends Weapon
{
	public Glaive()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a heavy glaive";
		displayText="a glaive leans against the wall.";
		miscText="";
		description="A long blade on a pole.";
		baseEnvStats().setAbility(0);
		baseEnvStats().setLevel(0);
		baseEnvStats.setWeight(8);
		baseEnvStats().setAttackAdjustment(0);
		baseEnvStats().setDamage(6);
		weaponType=TYPE_SLASHING;
		baseGoldValue=6;
		recoverEnvStats();
		wornLogicalAnd=true;
		properWornBitmap=Item.HELD|Item.WIELD;
		weaponClassification=Weapon.CLASS_POLEARM;
	}

	
	public Environmental newInstance()
	{
		return new Glaive();
	}

}
