package com.planet_ink.coffee_mud.Items.MiscMagic;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import com.planet_ink.coffee_mud.Items.*;
import java.util.*;

public class Wand_Nourishment extends Wand implements MiscMagic
{
	
	public Wand_Nourishment()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a wooden wand";
		displayText="a small wooden wand is here.";
		description="A wand made out of wood";
		secretIdentity="The wand of nourishment.  Hold the wand say \\`shazam\\` to it.";
		baseGoldValue=200;
		recoverEnvStats();
	}
	
	public Environmental newInstance()
	{
		return new Wand_Nourishment();
	}
	
	public void affect(Affect affect)
	{
		if(affect.amITarget(this))
		{
			MOB mob=affect.source();
			switch(affect.targetCode())
			{
			case Affect.SOUND_WORDS:
				if((mob.isMine(this))&&(!amWearingAt(Item.INVENTORY)))
					if(affect.targetMessage().toUpperCase().indexOf("'SHAZAM'")>=0)
						if(mob.curState().adjHunger(50,mob.maxState()))
							mob.tell("You are full.");
				break;
			default:
				break;
			}
		}
		super.affect(affect);
	}
}