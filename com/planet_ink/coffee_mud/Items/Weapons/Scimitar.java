package com.planet_ink.coffee_mud.Items.Weapons;
import com.planet_ink.coffee_mud.interfaces.*;

public class Scimitar extends Sword
{
	public Scimitar()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="an ornate scimitar";
		displayText="a rather ornate looking Scimitar leans against the wall.";
		miscText="";
		description="It has a metallic pommel, and a long curved blade.";
		baseEnvStats().setAbility(0);
		baseEnvStats().setLevel(0);
		baseEnvStats().setWeight(4);
		baseEnvStats().setAttackAdjustment(0);
		baseEnvStats().setDamage(8);
		baseGoldValue=15;
		recoverEnvStats();
		weaponType=TYPE_SLASHING;
	}
	
	public Environmental newInstance()
	{
		return new Scimitar();
	}
}
