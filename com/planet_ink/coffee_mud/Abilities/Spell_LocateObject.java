package com.planet_ink.coffee_mud.Abilities;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.application.*;
import com.planet_ink.coffee_mud.MOBS.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.*;
import com.planet_ink.coffee_mud.commands.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import java.util.*;

public class Spell_LocateObject extends Spell
	implements DivinationDevotion
{
	public Spell_LocateObject()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="Locate Object";

		canBeUninvoked=true;
		isAutoinvoked=false;

		baseEnvStats().setLevel(10);

		addQualifyingClass(new Mage().ID(),10);
		addQualifyingClass(new Ranger().ID(),baseEnvStats().level()+4);

		baseEnvStats().setAbility(0);
		uses=Integer.MAX_VALUE;
		recoverEnvStats();
	}

	public Environmental newInstance()
	{
		return new Spell_LocateObject();
	}

	public boolean invoke(MOB mob, Vector commands)
	{

		if(commands.size()<1)
		{
			mob.tell("Locate what?");
			return false;
		}
		String what=CommandProcessor.combine(commands,0);

		if(!super.invoke(mob,commands))
			return false;

		boolean success=profficiencyCheck(0);

		if(success)
		{
			FullMsg msg=new FullMsg(mob,null,this,Affect.SOUND_MAGIC,Affect.SOUND_MAGIC,Affect.SOUND_MAGIC,"<S-NAME> invoke(s) a divination, shouting '"+what+"'.");
			if(mob.location().okAffect(msg))
			{
				mob.location().send(mob,msg);
				for(int m=0;m<MUD.map.size();m++)
				{
					Room room=(Room)MUD.map.elementAt(m);
					Item item=room.fetchItem(null,what);
					if(item!=null)
						mob.tell(item.name()+" is in a place called '"+room.displayText()+"'.");
					for(int i=0;i<room.numInhabitants();i++)
					{
						MOB inhab=room.fetchInhabitant(i);
						item=inhab.fetchInventory(what);
						if(item!=null)
							mob.tell(item.name()+" is being carried by "+inhab.name()+" in a place called '"+room.displayText()+"'.");
					}
				}
			}

		}
		else
			beneficialFizzle(mob,null,"<S-NAME> invoke(s) a divination, shouting '"+what+"', but there is no answer.");


		// return whether it worked
		return success;
	}
}
