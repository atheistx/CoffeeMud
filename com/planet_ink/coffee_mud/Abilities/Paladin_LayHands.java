package com.planet_ink.coffee_mud.Abilities;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import com.planet_ink.coffee_mud.CharClasses.*;
import java.util.*;

public class Paladin_LayHands extends StdAbility
{
	public Paladin_LayHands()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="Lay Hands";
		displayText="(in the holy dominion of the gods)";
		miscText="";
		triggerStrings.addElement("HANDS");

		malicious=false;
		baseEnvStats().setLevel(1);

		addQualifyingClass(new Paladin().ID(),1);

		canBeUninvoked=true;
		isAutoinvoked=false;
	}

	public int classificationCode()
	{
		return Ability.SKILL;
	}

	public Environmental newInstance()
	{
		return new Paladin_LayHands();
	}

	public boolean invoke(MOB mob, Vector commands)
	{
		if(!Sense.canPerformAction(mob))
			return false;

		if(mob.getAlignment()<500)
		{
			mob.tell("Your alignment has alienated your god from you.");
			return false;
		}

		if(mob.curState().getMana()==0)
		{
			mob.tell("You don't have enough mana to do that.");
			return false;
		}

		if(profficiency()<100)
		{
			if(Math.round(Util.div(mob.charStats().getIntelligence(),25)*100.0*Math.random())>50)
			   setProfficiency(profficiency()+1);
		}

		MOB target=this.getTarget(mob,commands);
		if(target==null) return false;

		boolean success=profficiencyCheck(0);

		if(success)
		{
			// it worked, so build a copy of this ability,
			// and add it to the affects list of the
			// affected MOB.  Then tell everyone else
			// what happened.
			FullMsg msg=new FullMsg(mob,target,this,Affect.SOUND_MAGIC,Affect.SOUND_MAGIC,Affect.SOUND_MAGIC,"<S-NAME> lay(s) <S-HIS-HER> healing hands onto <T-NAME>.");
			if(mob.location().okAffect(msg))
			{
				mob.location().send(mob,msg);
				mob.curState().adjMana(-1,mob.maxState());
				int healing=1+(int)Math.round(Util.div(mob.envStats().level(),10.0));
				target.curState().adjHitPoints(healing,target.maxState());
				target.tell("You feel a little better!");
			}
		}
		else
		{
			// it didn't work, but tell everyone you tried.
			FullMsg msg=new FullMsg(mob,target,this,Affect.SOUND_MAGIC,Affect.SOUND_MAGIC,Affect.SOUND_MAGIC,"<S-NAME> lay(s) <S-HIS-HER> healing hands onto <T-NAME>, but <S-HIS-HER> god does not heed.");
			if(mob.location().okAffect(msg))
				mob.location().send(mob,msg);
		}


		// return whether it worked
		return success;
	}

}
