package com.planet_ink.coffee_mud.Items.MiscMagic;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.Abilities.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import com.planet_ink.coffee_mud.Items.*;
import java.util.*;


public class ManualAdvancement extends StdItem implements MiscMagic
{
	public ManualAdvancement()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a book";
		baseEnvStats().setWeight(1);
		displayText="an ornately bound book sits here.";
		description="An ornately bound book filled with mystical symbols.";
		secretIdentity="The Manual of Advancement.";
		this.setUsesRemaining(5);
		baseGoldValue=10000;
		recoverEnvStats();
	}
	
	public Environmental newInstance()
	{
		return new ManualAdvancement();
	}
	
	public void affect(Affect affect)
	{
		if(affect.amITarget(this))
		{
			MOB mob=affect.source();
			switch(affect.targetCode())
			{
			case Affect.VISUAL_READ:
				if(mob.isMine(this))
				{
					if(mob.fetchAffect(new Spell_ReadMagic().ID())!=null)
					{
						if(this.usesRemaining()<=0)
							mob.tell("The markings have been read off the parchment, and are no longer discernable.");
						else
						{
							this.setUsesRemaining(this.usesRemaining()-1);
							mob.tell("The manual glows softly, enveloping you in it's wisdom.");
							mob.charStats().getMyClass().gainExperience(mob,null,mob.getExpNeededLevel()+1);
						}
					}
					else
						mob.tell("The markings look magical, and are unknown to you.");
				}
				return;
			default:
				break;
			}
		}
		super.affect(affect);
	}
	
}
