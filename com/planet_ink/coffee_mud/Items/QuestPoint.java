package com.planet_ink.coffee_mud.Items;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import java.util.*;

public class QuestPoint extends StdItem
{
	public QuestPoint()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		name="a quest point";
		displayText="someone has left a quest point here";
		myLocation=null;
		description="A shiny blue coin with magical script around the edges.";
		myUses=Integer.MAX_VALUE;
		myWornCode=0;
		miscText="";
		baseEnvStats.setWeight(0);
		capacity=0;
		isAContainer=false;
		recoverEnvStats();
	}
	public Environmental newInstance()
	{
		return new QuestPoint();
	}
	
	public void affect(Affect affect)
	{
		if(affect.amITarget(this))
		{
			MOB mob=affect.source();
			switch(affect.targetCode())
			{
			case Affect.HANDS_GET:
				{
				setLocation(null);
				destroyThis();
				if(!mob.isMine(this))
					mob.setQuestPoint(mob.getQuestPoint()+1);
				remove();
				mob.location().recoverRoomStats();
				return;
				}
			default:
				break;
			}
		}
		super.affect(affect);
	}
}
