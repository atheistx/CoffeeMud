package com.planet_ink.coffee_mud.Items.Weapons;
import com.planet_ink.coffee_mud.interfaces.*;
import java.util.*;

public class Sword extends Weapon
{
	public Sword()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a sword";
		displayText="a rather plain looking sword leans against the wall.";
		miscText="";
		description="An plain sword.";
		baseEnvStats().setAbility(0);
		baseEnvStats().setLevel(0);
		baseEnvStats.setWeight(10);
		recoverEnvStats();
		baseEnvStats().setAttackAdjustment(0);
		baseEnvStats().setDamage(8);
		recoverEnvStats();
		weaponType=TYPE_SLASHING;
		weaponClassification=Weapon.CLASS_SWORD;
	}
	
	public Environmental newInstance()
	{
		Random randomizer = new Random(System.currentTimeMillis());
		int swordType = Math.abs(randomizer.nextInt() % 6);
		switch (swordType)
		{
			case 0:  return new Rapier();
			case 1:	 return new Katana();
			case 2:	 return new Longsword();
			case 3:	 return new Scimitar();
			case 4:	 return new Claymore();
			case 5:	 return new Shortsword();
			default: return new Sword();
				
		}

	}
	public void strike(MOB source, MOB target, boolean success)
	{
		super.strike(source, target, success);
	}
}
