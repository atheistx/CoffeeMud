package com.planet_ink.coffee_mud.Abilities;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.commands.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.application.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.service.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import com.planet_ink.coffee_mud.CharClasses.*;
import java.util.*;


public class Spell_ResistAcid extends Spell
	implements AbjurationDevotion
{

	public Spell_ResistAcid()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="Resist Acid";
		displayText="(Resist Acid)";
		miscText="";

		canBeUninvoked=true;
		isAutoinvoked=false;

		baseEnvStats().setLevel(5);

		addQualifyingClass(new Mage().ID(),5);
		addQualifyingClass(new Ranger().ID(),baseEnvStats().level()+4);

		baseEnvStats().setAbility(0);
		uses=Integer.MAX_VALUE;
		recoverEnvStats();
	}

	public Environmental newInstance()
	{
		return new Spell_ResistAcid();
	}

	public void unInvoke()
	{
		// undo the affects of this spell
		if((affected==null)||(!(affected instanceof MOB)))
			return;
		MOB mob=(MOB)affected;
		mob.tell("Your oily protection dries up.");

		super.unInvoke();

	}


	public void affect(Affect affect)
	{
		if((affected==null)||(!(affected instanceof MOB)))
			return;

		MOB mob=(MOB)affected;

		if(affect.amITarget(mob))
		{
			switch(affect.targetCode())
			{
			case Affect.STRIKE_ACID:
				if(Dice.rollPercentage()>35)
				{
					mob.location().show(mob,null,Affect.VISUAL_WNOISE,"The oily coating around <S-NAME> absorbs the acid blast.");
					affect.tagModified(true);
				}
				break;
			default:
				break;
			}
		}
	}


	public boolean invoke(MOB mob, Vector commands)
	{
		MOB target=getTarget(mob,commands);
		if(target==null)
			return false;

		if(!super.invoke(mob,commands))
			return false;

		boolean success=profficiencyCheck(0);

		FullMsg msg=new FullMsg(mob,target,this,Affect.SOUND_MAGIC,Affect.SOUND_MAGIC,Affect.VISUAL_WNOISE,"<S-NAME> invoke(s) a oily field of protection around <T-NAME>.");
		if(mob.location().okAffect(msg))
		{
			mob.location().send(mob,msg);
			beneficialAffect(mob,target,0);
		}
		else
			beneficialFizzle(mob,target,"<S-NAME> attempt(s) to invoke acid protection, but fail(s).");

		return success;
	}
}