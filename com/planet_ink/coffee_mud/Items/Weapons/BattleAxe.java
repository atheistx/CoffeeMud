package com.planet_ink.coffee_mud.Items.Weapons;
import com.planet_ink.coffee_mud.interfaces.*;

public class BattleAxe extends Sword
{
	public BattleAxe()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a battle axe";
		displayText="a heavy battle axe sits here";
		miscText="";
		description="It has a stout pole, about 4 feet in length with a trumpet shaped blade.";
		baseEnvStats().setAbility(0);
		baseEnvStats().setLevel(0);
		baseEnvStats.setWeight(15);
		baseEnvStats().setAttackAdjustment(0);
		baseEnvStats().setDamage(8);
		baseGoldValue=35;
		wornLogicalAnd=true;
		properWornBitmap=Item.HELD|Item.WIELD;
		recoverEnvStats();
		weaponType=Weapon.TYPE_SLASHING;
		weaponClassification=Weapon.CLASS_AXE;
	}
	
	public Environmental newInstance()
	{
		return new BattleAxe();
	}
}
