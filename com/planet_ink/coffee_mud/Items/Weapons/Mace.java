package com.planet_ink.coffee_mud.Items.Weapons;
import com.planet_ink.coffee_mud.interfaces.*;

public class Mace extends Weapon
{
	public Mace()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a rather large mace";
		displayText="a heavy mace is found in the center of the room.";
		miscText="";
		description="It's metallic and quite hard..";
		baseEnvStats().setAbility(0);
		baseEnvStats().setLevel(0);
		baseEnvStats.setWeight(10);
		baseEnvStats().setAttackAdjustment(0);
		baseEnvStats().setDamage(7);
		baseGoldValue=8;
		recoverEnvStats();
		weaponType=TYPE_BASHING;
		weaponClassification=Weapon.CLASS_BLUNT;
	}
	
	public Environmental newInstance()
	{
		return new Mace();
	}
}
