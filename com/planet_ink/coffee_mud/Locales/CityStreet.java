package com.planet_ink.coffee_mud.Locales;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.service.*;
import com.planet_ink.coffee_mud.MOBS.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import com.planet_ink.coffee_mud.telnet.*;
import com.planet_ink.coffee_mud.Exits.*;
import com.planet_ink.coffee_mud.utils.*;

import java.util.*;

public class CityStreet extends StdRoom
{
	public CityStreet()
	{
		super();
		myID=this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
		recoverEnvStats();
		domainType=Room.DOMAIN_OUTDOORS_CITY;
		domainCondition=Room.CONDITION_NORMAL;
	}
	public Environmental newInstance()
	{
		return new CityStreet();
	}
}
