package com.planet_ink.coffee_mud.Items.MiscMagic;

import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.utils.*;
import com.planet_ink.coffee_mud.Items.*;
import com.planet_ink.coffee_mud.StdAffects.*;
import java.util.*;

public class RingOfMagicResistance extends Ring_Protection implements MiscMagic
{
	public RingOfMagicResistance()
	{
		super();
		this.baseEnvStats().setLevel(GOLD_RING_OPAL);
		this.recoverEnvStats();
	}

	public Environmental newInstance()
	{
		return new RingOfMagicResistance();
	}
}
