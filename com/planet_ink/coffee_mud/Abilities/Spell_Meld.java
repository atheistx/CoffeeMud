package com.planet_ink.coffee_mud.Abilities;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.commands.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.Items.*;
import com.planet_ink.coffee_mud.Items.Armor.*;
import com.planet_ink.coffee_mud.Items.Weapons.*;
import com.planet_ink.coffee_mud.service.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.application.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import com.planet_ink.coffee_mud.CharClasses.*;
import java.util.*;

public class Spell_Meld extends Spell
	implements AlterationDevotion
{

	public Spell_Meld()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="Meld";

		// what the affected mob sees when they
		// bring up their affected list.
		displayText="(Meld)";


		canBeUninvoked=true;
		isAutoinvoked=false;

		baseEnvStats().setLevel(6);

		addQualifyingClass(new Mage().ID(),6);
		addQualifyingClass(new Ranger().ID(),baseEnvStats().level()+4);

		uses=Integer.MAX_VALUE;
		recoverEnvStats();
	}

	public Environmental newInstance()
	{
		return new Spell_Meld();
	}

	public boolean shinBone(Item one, Item two, long locationOne, long locationTwo)
	{
		if((one.canBeWornAt(locationOne)&&two.canBeWornAt(locationTwo))
		   &&(!one.canBeWornAt(locationTwo))
		   &&(!two.canBeWornAt(locationOne)))
			return true;
		return false;
	}
	public int heiarchy(int materialCode)
	{
		switch(materialCode)
		{
		case Armor.CLOTH:
			return 0;
		case Armor.LEATHER:
			return 1;
		case Armor.WOODEN:
			return 2;
		case Armor.METAL:
			return 3;
		case Armor.MITHRIL:
			return 4;
		default:
			return 5;
		}
	}


	public boolean invoke(MOB mob, Vector commands)
	{
		if(commands.size()<2)
		{
			mob.tell("Meld what and what else together?");
			return false;
		}
		Item itemOne=mob.fetchInventory((String)commands.elementAt(0));
		if((itemOne==null)||((itemOne!=null)&&(!Sense.canBeSeenBy(itemOne,mob))))
		{
			mob.tell("You don't seem to have a '"+((String)commands.elementAt(0))+"'.");
			return false;
		}
		Item itemTwo=mob.fetchInventory(CommandProcessor.combine(commands,1));
		if((itemTwo==null)||((itemTwo!=null)&&(!Sense.canBeSeenBy(itemTwo,mob))))
		{
			mob.tell("You don't seem to have a '"+CommandProcessor.combine(commands,1)+"'.");
			return false;
		}

		if((itemOne instanceof Armor)&&(itemTwo instanceof Armor))
		{
			if(shinBone(itemOne,itemTwo,Item.ON_HEAD,Item.ON_NECK)
			   ||shinBone(itemOne,itemTwo,Item.ON_HEAD,Item.ON_TORSO)
			   ||shinBone(itemOne,itemTwo,Item.ON_NECK,Item.ON_TORSO)
			   ||shinBone(itemOne,itemTwo,Item.ON_TORSO,Item.ON_ARMS)
			   ||shinBone(itemOne,itemTwo,Item.ON_ARMS,Item.ON_LEFT_WRIST)
			   ||shinBone(itemOne,itemTwo,Item.ON_ARMS,Item.ON_HANDS)
			   ||shinBone(itemOne,itemTwo,Item.ON_HANDS,Item.ON_LEFT_WRIST)
			   ||shinBone(itemOne,itemTwo,Item.ON_HANDS,Item.ON_RIGHT_FINGER)
			   ||shinBone(itemOne,itemTwo,Item.ON_TORSO,Item.ON_LEGS)
			   ||shinBone(itemOne,itemTwo,Item.ON_LEGS,Item.ON_FEET))
			{

			}
			else
			{
				mob.tell(itemOne.name()+" and "+itemTwo.name()+" aren't worn in compatible places, and thus can't be melded.");
				return false;
			}
		}
		else
		if((itemOne instanceof Weapon)||(itemTwo instanceof Weapon))
		{
			if(!itemOne.canBeWornAt(Item.HELD))
			{
				mob.tell(itemOne.name()+" can't be held, and thus can't be melded with "+itemTwo.name()+".");
				return false;
			}
			if(!itemTwo.canBeWornAt(Item.HELD))
			{
				mob.tell(itemTwo.name()+" can't be held, and thus can't be melded with "+itemOne.name()+".");
				return false;
			}
			if(itemOne.rawLogicalAnd())
			{
				mob.tell(itemOne.name()+" is two handed, and thus can't be melded with "+itemTwo.name()+".");
				return false;
			}
			if(itemTwo.rawLogicalAnd())
			{
				mob.tell(itemTwo.name()+" is two handed, and thus can't be melded with "+itemOne.name()+".");
				return false;
			}
		}
		else
		if((itemOne instanceof Container)&&(itemTwo instanceof Container))
		{

		}
		else
		{
			mob.tell("You can't meld those together.");
			return false;
		}


		// the invoke method for spells receives as
		// parameters the invoker, and the REMAINING
		// command line parameters, divided into words,
		// and added as String objects to a vector.
		if(!super.invoke(mob,commands))
			return false;

		boolean success=profficiencyCheck(0);

		if(success)
		{
			// it worked, so build a copy of this ability,
			// and add it to the affects list of the
			// affected MOB.  Then tell everyone else
			// what happened.
			invoker=mob;
			FullMsg msg=new FullMsg(mob,null,this,Affect.SOUND_MAGIC,Affect.SOUND_MAGIC,Affect.SOUND_MAGIC,"<S-NAME> meld(s) "+itemOne.name()+" and "+itemTwo.name()+".");
			if(mob.location().okAffect(msg))
			{
				mob.location().send(mob,msg);

				String itemOneName=itemOne.name();
				String itemTwoName=itemTwo.name();
				int x=itemOneName.indexOf("melded together");
				if(x>0) itemOneName=itemOneName.substring(0,x).trim();
				x=itemTwoName.indexOf("melded together");
				if(x>0) itemTwoName=itemTwoName.substring(0,x).trim();


				String newName=itemOneName+" and "+itemTwoName+" melded together";
				if((itemOne instanceof Armor)&&(itemTwo instanceof Armor))
				{
					int material=((Armor)itemOne).material();
					if(heiarchy(material)<heiarchy(((Armor)itemTwo).material()))
						material=((Armor)itemTwo).material();

					long wornLocation=itemOne.rawProperLocationBitmap()|itemTwo.rawProperLocationBitmap();
					if((wornLocation&Item.HELD)==(Item.HELD))
						wornLocation-=Item.HELD;
					if(((wornLocation&Item.ON_LEFT_FINGER)==(Item.ON_LEFT_FINGER))
					   &&((wornLocation&Item.ON_RIGHT_FINGER)==(Item.ON_RIGHT_FINGER)))
					{
						if(((wornLocation&Item.ON_LEFT_WRIST)==(Item.ON_LEFT_WRIST))
						&&((wornLocation&Item.ON_RIGHT_WRIST)==0))
						   wornLocation-=Item.ON_RIGHT_FINGER;
						else
						if(((wornLocation&Item.ON_RIGHT_WRIST)==(Item.ON_RIGHT_WRIST))
						&&((wornLocation&Item.ON_LEFT_WRIST)==0))
						   wornLocation-=Item.ON_LEFT_FINGER;
						else
						{
							if(Dice.rollPercentage()>50)
								wornLocation-=Item.ON_RIGHT_FINGER;
							else
								wornLocation-=Item.ON_LEFT_FINGER;
						}
					}

					if(((wornLocation&Item.ON_LEFT_WRIST)==(Item.ON_LEFT_WRIST))
					   &&((wornLocation&Item.ON_RIGHT_WRIST)==(Item.ON_RIGHT_WRIST)))
					{
						if(((wornLocation&Item.ON_LEFT_FINGER)==(Item.ON_LEFT_FINGER))
						&&((wornLocation&Item.ON_RIGHT_FINGER)==0))
						   wornLocation-=Item.ON_RIGHT_WRIST;
						else
						if(((wornLocation&Item.ON_RIGHT_FINGER)==(Item.ON_RIGHT_FINGER))
						&&((wornLocation&Item.ON_LEFT_FINGER)==0))
						   wornLocation-=Item.ON_LEFT_WRIST;
						else
						{
							if(Dice.rollPercentage()>50)
								wornLocation-=Item.ON_RIGHT_WRIST;
							else
								wornLocation-=Item.ON_LEFT_WRIST;
						}
					}


					GenArmor gc=new GenArmor(
											newName,
											newName+" sits here.",
											"It looks like someone melded "+itemOneName+" and "+itemTwoName,
											itemOne.secretIdentity()+", "+itemTwo.secretIdentity(),
											itemOne.value()+itemTwo.value(),
											material,
											itemOne.baseEnvStats().weight()+itemTwo.baseEnvStats().weight(),
											itemOne.baseEnvStats().armor()+itemTwo.baseEnvStats().armor(),
											itemOne.capacity()+itemTwo.capacity(),
											true,
											wornLocation);

					gc.baseEnvStats().setLevel(itemOne.baseEnvStats().level());
					if(itemTwo.baseEnvStats().level()>itemOne.baseEnvStats().level())
						gc.baseEnvStats().setLevel(itemTwo.baseEnvStats().level());
					gc.baseEnvStats().setAbility(itemOne.baseEnvStats().ability()+itemTwo.baseEnvStats().ability());
					gc.recoverEnvStats();
					mob.addInventory(gc);
				}
				else
				if((itemOne instanceof Weapon)||(itemTwo instanceof Weapon))
				{
					GenWeapon gc=new GenWeapon(
											newName,
											newName+" sits here.",
											"It looks like someone melded "+itemOneName+" and "+itemTwoName,
											itemOne.secretIdentity()+", "+itemTwo.secretIdentity(),
											itemOne.value()+itemTwo.value(),
											itemOne.baseEnvStats().weight()+itemTwo.baseEnvStats().weight(),
											itemOne.baseEnvStats().attackAdjustment()+itemTwo.baseEnvStats().attackAdjustment(),
											itemOne.baseEnvStats().damage()+itemTwo.baseEnvStats().damage(),
											((Weapon)itemOne).weaponType,
											((Weapon)itemTwo).weaponClassification,
											true);


					gc.baseEnvStats().setLevel(itemOne.baseEnvStats().level());
					if(itemTwo.baseEnvStats().level()>itemOne.baseEnvStats().level())
						gc.baseEnvStats().setLevel(itemTwo.baseEnvStats().level());
					gc.baseEnvStats().setAbility(itemOne.baseEnvStats().ability()+itemTwo.baseEnvStats().ability());
					gc.recoverEnvStats();
					mob.addInventory(gc);
				}
				else
				if((itemOne instanceof Container)&&(itemTwo instanceof Container))
				{
					boolean isLocked=((Container)itemOne).hasALock;
					String keyName=((Container)itemOne).keyName;
					if(!isLocked)
					{
						isLocked=((Container)itemTwo).hasALock;
						keyName=((Container)itemTwo).keyName;
					}
					GenContainer gc=new GenContainer(
													 newName,
													 newName+" sits here.",
													 "It looks like someone melded "+itemOneName+" and "+itemTwoName,
													 itemOne.isGettable()&&itemTwo.isGettable(),
													 itemOne.value()+itemTwo.value(),
													 itemOne.baseEnvStats().weight()+itemTwo.baseEnvStats().weight(),
													 ((Container)itemOne).capacity()+((Container)itemTwo).capacity(),
													 isLocked,
													 (((Container)itemOne).hasALid||((Container)itemTwo).hasALid),
													 false,
													 keyName);

					gc.baseEnvStats().setLevel(itemOne.baseEnvStats().level());
					if(itemTwo.baseEnvStats().level()>itemOne.baseEnvStats().level())
						gc.baseEnvStats().setLevel(itemTwo.baseEnvStats().level());
					gc.baseEnvStats().setAbility(itemOne.baseEnvStats().ability()+itemTwo.baseEnvStats().ability());
					gc.recoverEnvStats();
					mob.addInventory(gc);
				}
				itemOne.destroyThis();
				itemTwo.destroyThis();
				mob.location().recoverRoomStats();
			}
		}
		else
			return beneficialFizzle(mob,null,"<S-NAME> attempt(s) "+itemOne.name()+" and "+itemTwo.name()+", but fail(s).");

		// return whether it worked
		return success;
	}
}