package com.planet_ink.coffee_mud.Abilities.Prayers;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import com.planet_ink.coffee_mud.utils.*;
import java.util.*;

public class Prayer_Thunderbolt extends Prayer
{
	public String ID() { return "Prayer_Thunderbolt"; }
	public String name(){ return "Thunderbolt";}
	public int quality(){ return MALICIOUS;}
	public int holyQuality(){ return HOLY_NEUTRAL;}
	public Environmental newInstance(){	return new Prayer_Thunderbolt();}

	public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto)
	{
		MOB target=this.getTarget(mob,commands,givenTarget);
		if(target==null) return false;

		if(!super.invoke(mob,commands,givenTarget,auto))
			return false;

		boolean success=profficiencyCheck(0,auto);

		if(success)
		{
			// it worked, so build a copy of this ability,
			// and add it to the affects list of the
			// affected MOB.  Then tell everyone else
			// what happened.
			Prayer_Thunderbolt newOne=(Prayer_Thunderbolt)this.copyOf();
			FullMsg msg=new FullMsg(mob,target,newOne,affectType(auto),auto?"<T-NAME> is filled with a holy charge!":"^S<S-NAME> invoke(s) the power of <S-HIS-HER> god against <T-NAMESELF>!^?");
			if(mob.location().okAffect(msg))
			{
				mob.location().send(mob,msg);
				if(!msg.wasModified())
				{
					int harming=Dice.roll(3,adjustedLevel(mob),15);
					ExternalPlay.postDamage(mob,target,this,harming,Affect.MASK_GENERAL|Affect.TYP_CAST_SPELL,Weapon.TYPE_STRIKING,"^SThe STRIKE of the gods <DAMAGE> <T-NAME>!^?");
				}
			}
		}
		else
			return maliciousFizzle(mob,target,"<S-NAME> invoke(s) <S-HIS-HER> god, but nothing happens.");


		// return whether it worked
		return success;
	}
}
