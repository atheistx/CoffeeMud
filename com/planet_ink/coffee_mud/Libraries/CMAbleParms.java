package com.planet_ink.coffee_mud.Libraries;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.AbilityMapper.AbilityMapping;
import com.planet_ink.coffee_mud.Libraries.interfaces.AbilityParameters.AbilityParmEditor;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

import java.util.*;

/*
   Copyright 2000-2008 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
public class CMAbleParms extends StdLibrary implements AbilityParameters
{
    public String ID(){return "CMAbleParms";}
    
    public CMAbleParms()
    {
        super();
    }
    
    public Vector getCodedSpells(String spells)
    {
        Vector spellsV=new Vector(); 
        if(spells.length()==0) return spellsV;
        if(spells.startsWith("*"))
        {
            spells=spells.substring(1);
            int x=spells.indexOf(";");
            if(x<0) x=spells.length();
            Ability A=CMClass.getAbility(spells.substring(0,x));
            if(A!=null)
            {
                if(x<spells.length())
                    A.setMiscText(spells.substring(x+1));
                spellsV.addElement(A);
                return spellsV;
            }
        }
        Vector V=CMParms.parseSemicolons(spells,true);
        Ability lastSpell=null;
        Ability A=null;
        for(int v=0;v<V.size();v++)
        {
            spells=(String)V.elementAt(v); 
            A=CMClass.getAbility(spells);
            if(A==null)
            {
                if(lastSpell!=null)
                    lastSpell.setMiscText(spells);
            }
            else
            {
                lastSpell=A;
                spellsV.addElement(A);
            }
        }
        return spellsV;
    }
    
    protected String parseLayers(short[] layerAtt, short[] clothingLayers, String misctype)
    {
        int colon=misctype.indexOf(":");
        if(colon>=0)
        {
            String layers=misctype.substring(0,colon).toUpperCase().trim();
            misctype=misctype.substring(colon+1).trim();
            if((layers.startsWith("MS"))
            ||(layers.startsWith("SM")))
            { layers=layers.substring(2); layerAtt[0]=Armor.LAYERMASK_MULTIWEAR|Armor.LAYERMASK_SEETHROUGH;}
            else
            if(layers.startsWith("M"))
            { layers=layers.substring(1); layerAtt[0]=Armor.LAYERMASK_MULTIWEAR;}
            else
            if(layers.startsWith("S"))
            { layers=layers.substring(1); layerAtt[0]=Armor.LAYERMASK_SEETHROUGH;}
            clothingLayers[0]=CMath.s_short(layers);
        }
        return misctype;
    }
    
    public void parseWearLocation(short[] layerAtt, short[] layers, long[] wornLoc, boolean[] logicalAnd, double[] hardBonus, String wearLocation)
    {
        if(layers != null)
        {
            layerAtt[0] = 0;
            layers[0] = 0;
            wearLocation=parseLayers(layerAtt,layers,wearLocation);
        }
        
        double hardnessMultiplier = hardBonus[0];
        wornLoc[0] = 0;
        hardBonus[0]=0.0;
        for(int wo=1;wo<Item.WORN_DESCS.length;wo++)
        {
            String WO=Item.WORN_DESCS[wo].toUpperCase();
            if(wearLocation.equalsIgnoreCase(WO))
            {
                hardBonus[0]+=Item.WORN_WEIGHTS[wo];
                wornLoc[0]=CMath.pow(2,wo-1);
                logicalAnd[0]=false;
            }
            else
            if((wearLocation.toUpperCase().indexOf(WO+"||")>=0)
            ||(wearLocation.toUpperCase().endsWith("||"+WO)))
            {
                if(hardBonus[0]==0.0)
                    hardBonus[0]+=Item.WORN_WEIGHTS[wo];
                wornLoc[0]=wornLoc[0]|CMath.pow(2,wo-1);
                logicalAnd[0]=false;
            }
            else
            if((wearLocation.toUpperCase().indexOf(WO+"&&")>=0)
            ||(wearLocation.toUpperCase().endsWith("&&"+WO)))
            {
                hardBonus[0]+=Item.WORN_WEIGHTS[wo];
                wornLoc[0]=wornLoc[0]|CMath.pow(2,wo-1);
                logicalAnd[0]=true;
            }
        }
        hardBonus[0]=(int)Math.round(hardBonus[0] * hardnessMultiplier);
    }

    public Vector parseRecipeFormatColumns(String recipeFormat) {
        char C = '\0';
        StringBuffer currentColumn = new StringBuffer("");
        Vector currentColumns = null;
        char[] recipeFmtC = recipeFormat.toCharArray();
        Vector columnsV = new Vector();
        for(int c=0;c<=recipeFmtC.length;c++) {
            if(c==recipeFmtC.length) {
                
                break;
            }
            C = recipeFmtC[c];
            if((C=='|')
            &&(c<(recipeFmtC.length-1))
            &&(recipeFmtC[c+1]=='|')
            &&(currentColumn.length()>0))
            {
                if(currentColumn.length()>0) {
                    if(currentColumns == null) {
                        currentColumns = new Vector();
                        columnsV.addElement(currentColumns);
                    }
                    currentColumns.addElement(currentColumn.toString());
                    currentColumn.setLength(0);
                }
                c++;
            }
            else
            if(Character.isLetter(C)||Character.isDigit(C)||(C=='_'))
                currentColumn.append(C);
            else
            {
                if(currentColumn.length()>0) {
                    if(currentColumns == null) {
                        currentColumns = new Vector();
                        columnsV.addElement(currentColumns);
                    }
                    currentColumns.addElement(currentColumn.toString());
                    currentColumn.setLength(0);
                }
                currentColumns = null;
                if((C=='.')
                &&(c<(recipeFmtC.length-2))
                &&(recipeFmtC[c+1]=='.')
                &&(recipeFmtC[c+2]=='.'))
                {
                    c+=2;
                    columnsV.addElement("...");
                }
                else
                if(columnsV.lastElement() instanceof String)
                    columnsV.setElementAt(((String)columnsV.lastElement())+C,columnsV.size()-1);
                else
                    columnsV.addElement(""+C);
            }
        }
        if(currentColumn.length()>0)
        {
            if(currentColumns == null) {
                currentColumns = new Vector();
                columnsV.addElement(currentColumns);
            }
            currentColumns.addElement(currentColumn.toString());
        }
        if((currentColumns != null) && (currentColumns.size()==0))
            columnsV.remove(currentColumns);
        return columnsV;
    }
    
    public String stripData(StringBuffer str, String div)
    {
        StringBuffer data = new StringBuffer("");
        while(str.length()>0)
        {
            if(str.length() < div.length())
                return null;
            for(int d=0;d<=div.length();d++)
            {
                if(d==div.length())
                {
                    str.delete(0,div.length());
                    return data.toString();
                } 
                else
                if(str.charAt(d)!=div.charAt(d))
                    break;
            }
            if(str.charAt(0)=='\n')
            {
                if(data.length()>0)
                    return data.toString();
                return null;
            }
            data.append(str.charAt(0));
            str.delete(0,1);
        }
        return null;
    }
    
    public void modifyRecipesList(MOB mob, String recipeFilename, String recipeFormat) 
    {
        Hashtable defaultFields = initDefaultFields();
        StringBuffer str=new CMFile(Resources.buildResourcePath("skills")+recipeFilename,null,true).text();
        Vector columnsV = parseRecipeFormatColumns(recipeFormat);
        Vector rowsV = new Vector();
        DVector dataRow = new DVector(2);
        Vector currCol = null;
        String lastDiv = null;
        int numberOfDataColumns = 0;
        for(int c = 0; c < columnsV.size(); c++)
            if(columnsV.elementAt(c) instanceof Vector)
                numberOfDataColumns++;
        while((str.length() > 0)
        &&(mob.session()!=null)
        &&(!mob.session().killFlag()))
        {
            for(int c = 0; c < columnsV.size(); c++)
            {
                String div = "\n\r";
                currCol = null;
                if(columnsV.elementAt(c) instanceof String)
                    stripData(str,(String)columnsV.elementAt(c));
                else
                if(columnsV.elementAt(c) instanceof Vector)
                {
                    currCol = (Vector)columnsV.elementAt(c);
                    if(c<columnsV.size()-1)
                    {
                        div = (String)columnsV.elementAt(c+1);
                        c++;
                    }
                }
                if(!div.equals("..."))
                {
                    lastDiv = div;
                    String data = null;
                        data = stripData(str,lastDiv);
                    if(data == null)
                        data = "";
                    dataRow.addElement(currCol,data);
                    currCol = null;
                } 
                else 
                {
                    String data = stripData(str,lastDiv);
                    if(data == null)
                        break;
                    dataRow.addElement(currCol,data);
                }
            }
            if(dataRow.size() != numberOfDataColumns) {
                Log.errOut("CMAbleParms","Unable to read file: "+recipeFilename+", row "+(rowsV.size()+1)+" has "+dataRow.size()+"/"+numberOfDataColumns);
                return;
            }
            rowsV.addElement(dataRow);
            dataRow = new DVector(2);
        }
        for(int r=0;r<rowsV.size();r++) {
            dataRow=(DVector)rowsV.elementAt(r);
            String classID = null;
            Item classModelI = null;
            boolean classIDRequired = false;
            for(int d=0;d<dataRow.size();d++)
                if(((Vector)dataRow.elementAt(d,1)).contains("ITEM_CLASS_ID"))
                    classID=(String)dataRow.elementAt(d,2);
                else
                if((!classIDRequired)&&(((Vector)dataRow.elementAt(d,1)).size()>1))
                    classIDRequired=true;
            if(classID==null)
            for(int d=0;d<dataRow.size();d++)
                if(((Vector)dataRow.elementAt(d,1)).contains("FOOD_DRINK"))
                    classID=(String)dataRow.elementAt(d,2);
            if((classID!=null)&&(classID.length()>0))
                if(classID.equalsIgnoreCase("FOOD"))
                    classModelI = CMClass.sampleItem("GenFood");
                else
                if(classID.equalsIgnoreCase("DRINK"))
                    classModelI = CMClass.sampleItem("GenDrink");
                else
                    classModelI = CMClass.sampleItem(classID);
            if(classModelI == null) {
                if(classIDRequired)
                {
                    Log.errOut("CMAbleParms","File: "+recipeFilename+", data row "+r+" discarded due to null/empty classID");
                    rowsV.removeElementAt(r);
                    r--;
                    continue;
                }
            } 
            boolean abortRow = false;
            for(int d=0;d<dataRow.size();d++) 
            {
                Vector colV=(Vector)dataRow.elementAt(d,1);
                if(colV.size()==1)
                {
                    AbilityParmEditor A = (AbilityParmEditor)defaultFields.get((String)colV.firstElement());
                    if((A == null)||(A.appliesToClass(classModelI)<0))
                        A = (AbilityParmEditor)defaultFields.get("N/A");
                    dataRow.setElementAt(d,1,A.ID());
                }
                else
                {
                    AbilityParmEditor applicableA = null;
                    for(int c=0;c<colV.size();c++)
                    {
                        AbilityParmEditor A = (AbilityParmEditor)defaultFields.get((String)colV.elementAt(c));
                        if(A==null) 
                        {
                            Log.errOut("CMAbleParms","File: "+recipeFilename+", col name "+((String)colV.elementAt(c))+" is not defined.");
                            return;
                        }
                        if((applicableA==null)
                        ||(A.appliesToClass(classModelI) > applicableA.appliesToClass(classModelI)))
                            applicableA = A;
                    }
                    if((applicableA == null)||(applicableA.appliesToClass(classModelI)<0))
                        applicableA = (AbilityParmEditor)defaultFields.get("N/A");
                    dataRow.setElementAt(d,1,applicableA.ID());
                }
                AbilityParmEditor A = (AbilityParmEditor)defaultFields.get((String)dataRow.elementAt(d,1));
                if(A==null)
                    Log.errOut("CMAbleParms","File: "+recipeFilename+", item id "+classID+" has no editor for "+((String)dataRow.elementAt(d,1)));
                else
                if(!A.confirmValue((String)dataRow.elementAt(d,2)))
                    Log.errOut("CMAbleParms","File: "+recipeFilename+", item id "+classID+" has bad data '"+((String)dataRow.elementAt(d,2))+"' for column "+((String)dataRow.elementAt(d,1))+" at row "+r);
            }
            if(abortRow)
                continue;
        }
        int[] lens = new int[numberOfDataColumns];
        String[] head = new String[numberOfDataColumns];
        for(int r=0;r<rowsV.size();r++) {
            dataRow=(DVector)rowsV.elementAt(r);
            for(int c=0;c<dataRow.size();c++)
            {
                AbilityParmEditor A = (AbilityParmEditor)defaultFields.get((String)dataRow.elementAt(c,1));
                if(A==null)
                    Log.errOut("CMAbleParms","Inexplicable lack of a column: "+((String)dataRow.elementAt(c,1)));
                if(head[c] == null)
                {
                    head[c] = A.colHeader();
                    lens[c]=head[c].length()+1;
                }
                else
                if((!head[c].startsWith("#"))
                &&(!head[c].equalsIgnoreCase(A.colHeader())))
                {
                    head[c]="#"+c;
                    lens[c]=head[c].length()+1;
                }
            }
        }
        int currLenTotal = 0;
        for(int l=0;l<lens.length;l++)
            currLenTotal+=lens[l];
        int curCol = 0;
        while(currLenTotal<74) {
            lens[curCol]++;
            currLenTotal++;
            curCol++;
            if(curCol >= lens.length)
                curCol = 0;
        }
        StringBuffer list=new StringBuffer("");
        list.append("### ");
        for(int l=0;l<lens.length;l++)
            list.append(CMStrings.padRight(head[l],lens[l])+" ");
        list.append("\n\r");
        for(int r=0;r<rowsV.size();r++) {
            dataRow=(DVector)rowsV.elementAt(r);
            list.append(CMStrings.padRight(""+r,3)+" ");
            for(int c=0;c<dataRow.size();c++)
                list.append(CMStrings.padRight((String)dataRow.elementAt(c,2),lens[c])+" ");
            list.append("\n\r");
        }
        //Log.sysOut("","\n\r" + list.toString());
        //TODO: resave the file.
        Log.sysOut("CMAbleParms","User: "+mob.Name()+" modified file "+recipeFilename);
        Resources.removeResource("PARSED: "+recipeFilename);
        
    }
    
    protected Hashtable initDefaultFields()
    {
        Vector V=CMParms.makeVector(new Object[] {
            new AbilityParmEditorImpl("SPELL_ID","Spell",PARMTYPE_CHOICES) {
                public void createChoices() { createChoices(CMClass.abilities());}
            },
            new AbilityParmEditorImpl("RESOURCE_NAME","Resource",PARMTYPE_CHOICES) {
                public void createChoices() { createChoices(RawMaterial.RESOURCE_DESCS);}
            },
            new AbilityParmEditorImpl("ITEM_NAME","Item Final Name",PARMTYPE_STRING){public void createChoices() {}}, 
            new AbilityParmEditorImpl("ITEM_LEVEL","Lvl",PARMTYPE_NUMBER){public void createChoices() {}},
            new AbilityParmEditorImpl("BUILD_TIME_TICKS","Time",PARMTYPE_NUMBER){public void createChoices() {}},
            new AbilityParmEditorImpl("AMOUNT_MATERIAL_REQUIRED","Amt",PARMTYPE_NUMBER){
                public void createChoices() {}
                public boolean confirmValue(String oldVal) { return true;}
            },
            new AbilityParmEditorImpl("ITEM_BASE_VALUE","Value",PARMTYPE_NUMBER){public void createChoices() {}},
            new AbilityParmEditorImpl("ITEM_CLASS_ID","Class ID",PARMTYPE_CHOICES) {
                public void createChoices() { 
                    Vector V  = new Vector();
                    V.addAll(CMParms.makeVector(CMClass.clanItems()));
                    V.addAll(CMParms.makeVector(CMClass.armor()));
                    V.addAll(CMParms.makeVector(CMClass.basicItems()));
                    V.addAll(CMParms.makeVector(CMClass.miscMagic()));
                    V.addAll(CMParms.makeVector(CMClass.miscTech()));
                    V.addAll(CMParms.makeVector(CMClass.weapons()));
                    Vector V2=new Vector();
                    Item I;
                    for(Enumeration e=V.elements();e.hasMoreElements();)
                    {
                        I=(Item)e.nextElement();
                        if(I.isGeneric())
                            V2.addElement(I);
                    }
                    createChoices(V2);
                }
            },
            new AbilityParmEditorImpl("CODED_WEAR_LOCATION","Wear Locs",PARMTYPE_SPECIAL) {
                public int appliesToClass(Object o) { return ((o instanceof Armor)||(o instanceof MusicalInstrument))?2:-1;}
                public void createChoices() {}
                public boolean confirmValue(String oldVal) { return oldVal.trim().length()>0;}
                public String commandLinePrompt(MOB mob, String oldVal, int[] showNumber, int showFlag) throws java.io.IOException
                {
                    short[] layerAtt = new short[1];
                    short[] layers = new short[1];
                    long[] wornLoc = new long[1];
                    boolean[] logicalAnd = new boolean[1];
                    double[] hardBonus=new double[1];
                    CMLib.ableParms().parseWearLocation(layerAtt,layers,wornLoc,logicalAnd,hardBonus,oldVal);
                    CMLib.genEd().wornLayer(mob,layerAtt,layers,++showNumber[0],showFlag);
                    CMLib.genEd().wornLocation(mob,wornLoc,logicalAnd,++showNumber[0],showFlag);
                    StringBuffer newVal = new StringBuffer("");
                    if((layerAtt[0]!=0)&&(layers[0]!=0))
                    {
                        if(CMath.bset(layerAtt[0],Armor.LAYERMASK_MULTIWEAR))
                            newVal.append('M');
                        if(CMath.bset(layerAtt[0],Armor.LAYERMASK_SEETHROUGH))
                            newVal.append('S');
                        newVal.append(':');
                    }
                    boolean needLink=false;
                    for(int wo=1;wo<Item.WORN_DESCS.length;wo++)
                    {
                        if(CMath.bset(wornLoc[0],CMath.pow(2,wo-1)))
                        {
                            if(needLink)
                                newVal.append(logicalAnd[0]?"&&":"||");
                            needLink = true;
                            newVal.append(Item.WORN_DESCS[wo].toUpperCase());
                        }
                    }
                    return newVal.toString();
                }
            },
            new AbilityParmEditorImpl("CONTAINER_CAPACITY","Cap.",PARMTYPE_NUMBER) {
                public int appliesToClass(Object o) { return (o instanceof Container)?1:-1;}
                public void createChoices() {}
            },
            new AbilityParmEditorImpl("BASE_ARMOR_AMOUNT","Arm.",PARMTYPE_NUMBER) {
                public int appliesToClass(Object o) { return (o instanceof Armor)?2:-1;}
                public void createChoices() {}
            },
            new AbilityParmEditorImpl("CONTAINER_TYPE","Typ.",PARMTYPE_MULTICHOICES) {
                public void createChoices() { createBinaryChoices(Container.CONTAIN_DESCS);}
                public int appliesToClass(Object o) { return (o instanceof Container)?1:-1;}
            },
            new AbilityParmEditorImpl("CODED_SPELL_LIST","Spells",PARMTYPE_SPECIAL) {
                public void createChoices() {}
                public boolean confirmValue(String oldVal) {
                    if(oldVal.length()==0) return true;
                    if(oldVal.charAt(0)=='*')
                        oldVal = oldVal.substring(1);
                    int x=oldVal.indexOf('(');
                    int y=oldVal.indexOf(';');
                    if((x<y)&&(x>0)) y=x;
                    if(y<0) 
                        return CMClass.getAbility(oldVal)!=null;
                    return CMClass.getAbility(oldVal.substring(0,y))!=null;
                }
                public String commandLinePrompt(MOB mob, String oldVal, int[] showNumber, int showFlag) throws java.io.IOException
                {
                    Vector spells=CMLib.ableParms().getCodedSpells(oldVal);
                    StringBuffer rawCheck = new StringBuffer("");
                    for(int s=0;s<spells.size();s++)
                        rawCheck.append(((Ability)spells.elementAt(s)).ID()).append(";").append(((Ability)spells.elementAt(s)).text()).append(";");
                    boolean okToProceed = true;
                    ++showNumber[0];
                    StringBuffer newVal = new StringBuffer("");
                    while(okToProceed) {
                        okToProceed = false;
                        CMLib.genEd().spells(mob,spells,showNumber[0],showFlag);
                        StringBuffer sameCheck = new StringBuffer("");
                        for(int s=0;s<spells.size();s++)
                            sameCheck.append(((Ability)spells.elementAt(s)).ID()).append(';').append(((Ability)spells.elementAt(s)).text()).append(';');
                        if(sameCheck.toString().equals(rawCheck.toString())) 
                            return oldVal;
                        newVal.setLength(0);
                        if(spells.size()==1)
                            newVal.append("*" + ((Ability)spells.firstElement()).ID() + ";" + ((Ability)spells.firstElement()).text());
                        else
                        if(spells.size()>1) {
                            for(int s=0;s<spells.size();s++)
                            {
                                String txt = ((Ability)spells.elementAt(s)).text().trim();
                                if((txt.indexOf(';')>=0)||(CMClass.getAbility(txt)!=null))
                                {
                                    mob.tell("You may not have more than one spell when one of the spells parameters is a spell id or a ; character.");
                                    okToProceed = true;
                                    break;
                                }
                                newVal.append(((Ability)spells.firstElement()).ID());
                                if(txt.length()>0)
                                    rawCheck.append(";" + ((Ability)spells.firstElement()).text());
                                if(s<(spells.size()-1))
                                    newVal.append(";");
                            }
                        }
                    }
                    return newVal.toString();
                }
            },
            new AbilityParmEditorImpl("BASE_DAMAGE","Dmg.",PARMTYPE_NUMBER) {
                public int appliesToClass(Object o) { return (o instanceof Weapon)?2:-1;}
                public void createChoices() {}
            },
            new AbilityParmEditorImpl("LID_LOCK","Lid.",PARMTYPE_CHOICES) {
                public int appliesToClass(Object o) { return (o instanceof Container)?1:-1;}
                public void createChoices() { createChoices(new String[]{"","LID","LOCK"});}
            },
            new AbilityParmEditorImpl("STATUE","Statue",PARMTYPE_CHOICES) {
                public void createChoices() { createChoices(new String[]{"","STATUE"});}
            },
            new AbilityParmEditorImpl("RIDE_BASIS","Ride",PARMTYPE_CHOICES) {
                public int appliesToClass(Object o) { return (o instanceof Rideable)?3:-1;}
                public void createChoices() { createChoices(new String[]{"","CHAIR","TABLE","LADDER","ENTER","BED"});}
            },
            new AbilityParmEditorImpl("LIQUID_CAPACITY","Liq.",PARMTYPE_NUMBER) {
                public int appliesToClass(Object o) { return (o instanceof Drink)?4:-1;}
                public void createChoices() {}
            },
            new AbilityParmEditorImpl("WEAPON_CLASS","WClas",PARMTYPE_CHOICES) {
                public int appliesToClass(Object o) { return (o instanceof Weapon)?2:-1;}
                public void createChoices() { createChoices(Weapon.CLASS_DESCS);}
            },
            new AbilityParmEditorImpl("SMOKE_FLAG","Smoke",PARMTYPE_CHOICES) {
                public int appliesToClass(Object o) { return (o instanceof Light)?5:-1;}
                public void createChoices() { createChoices(new String[]{"","SMOKE"});}
            },
            new AbilityParmEditorImpl("WEAPON_HANDS_REQUIRED","Hand",PARMTYPE_NUMBER) {
                public int appliesToClass(Object o) { return (o instanceof Weapon)?2:-1;}
                public void createChoices() {}
            },
            new AbilityParmEditorImpl("LIGHT_DURATION","Dur.",PARMTYPE_NUMBER) {
                public int appliesToClass(Object o) { return (o instanceof Light)?5:-1;}
                public void createChoices() {}
            },
            new AbilityParmEditorImpl("CLAN_ITEM_CODENUMBER","Typ.",PARMTYPE_CHOICES) {
                public int appliesToClass(Object o) { return (o instanceof ClanItem)?10:-1;}
                public void createChoices() { createNumberedChoices(ClanItem.CI_DESC);}
            },
            new AbilityParmEditorImpl("CLAN_EXPERIENCE_COST_AMOUNT","Exp",PARMTYPE_NUMBER) {public void createChoices() {}},
            new AbilityParmEditorImpl("CLAN_AREA_FLAG","Area",PARMTYPE_CHOICES) {
                public void createChoices() { createChoices(new String[]{"","AREA"});}
            },
            new AbilityParmEditorImpl("READABLE_TEXT","Read",PARMTYPE_STRINGORNULL) {public void createChoices() {}},
            new AbilityParmEditorImpl("REQUIRED_COMMON_SKILL_ID","Common Skill",PARMTYPE_CHOICES) {
                public void createChoices() {
                    Vector V  = new Vector();
                    Ability A = null;
                    for(Enumeration e=V.elements();e.hasMoreElements();)
                    {
                        A=(Ability)e.nextElement();
                        if((A.classificationCode() & Ability.ALL_ACODES) == Ability.ACODE_COMMON_SKILL)
                            V.addElement(A);
                    }
                    V.addElement("");
                    createChoices(V);
                }
            },
            new AbilityParmEditorImpl("FOOD_DRINK","Type",PARMTYPE_CHOICES) {
                public void createChoices() { createChoices(new String[]{"","FOOD","DRINK","GenPerfume"});}
            },
            new AbilityParmEditorImpl("SMELL_LIST","Smells",PARMTYPE_STRING) {
                public void createChoices() {}
                public int appliesToClass(Object o) { return (o instanceof Perfume)?5:-1;}
            },
            new AbilityParmEditorImpl("RESOURCE_OR_KEYWORD","Resource",PARMTYPE_SPECIAL) {
                public void createChoices() {}
                public boolean confirmValue(String oldVal) { return true;}
                public String commandLinePrompt(MOB mob, String oldVal, int[] showNumber, int showFlag) throws java.io.IOException
                {
                    ++showNumber[0];
                    boolean proceed = true;
                    String str = oldVal;
                    while(proceed)
                    {
                        proceed = false;
                        str=CMLib.genEd().prompt(mob,oldVal,showNumber[0],showFlag,prompt(),true,CMParms.toStringList(RawMaterial.RESOURCE_DESCS)).trim();
                        if(str.equals(oldVal)) return oldVal;
                        for(int r=0;r<RawMaterial.RESOURCE_DESCS.length;r++)
                            if(RawMaterial.RESOURCE_DESCS[r].equalsIgnoreCase(str))
                                str=(r>0)?RawMaterial.RESOURCE_DESCS[r]:"";
                        if(str.equals(oldVal)) return oldVal;
                        if(str.length()==0) return "";
                        boolean isResource = CMParms.contains(RawMaterial.RESOURCE_DESCS,str);
                        if((!isResource)&&(mob.session()!=null)&&(!mob.session().killFlag()))
                            if(!mob.session().confirm("You`ve entered a non-resource item keyword '"+str+"', ok (Y/n)?","Y"))
                                proceed = true;
                    }
                    return str;
                }
            },
            new AbilityParmEditorImpl("RESOURCE_NAME_OR_HERB_NAME","Resrc/Herb",PARMTYPE_SPECIAL) {
                public void createChoices() {}
                public boolean confirmValue(String oldVal) {
                    if(oldVal.trim().length()==0)
                        return true;
                    if(!oldVal.endsWith("$")) {
                        return CMParms.contains(RawMaterial.RESOURCE_DESCS,oldVal);
                    }
                    return true;
                }
                public String commandLinePrompt(MOB mob, String oldVal, int[] showNumber, int showFlag) throws java.io.IOException
                {
                    ++showNumber[0];
                    boolean proceed = true;
                    String str = oldVal;
                    while(proceed)
                    {
                        proceed = false;
                        if(oldVal.trim().endsWith("$")) oldVal=oldVal.trim().substring(0,oldVal.trim().length()-1);
                        str=CMLib.genEd().prompt(mob,oldVal,showNumber[0],showFlag,prompt(),true,CMParms.toStringList(RawMaterial.RESOURCE_DESCS)).trim();
                        if(str.equals(oldVal)) return oldVal;
                        for(int r=0;r<RawMaterial.RESOURCE_DESCS.length;r++)
                            if(RawMaterial.RESOURCE_DESCS[r].equalsIgnoreCase(str))
                                str=(r>0)?RawMaterial.RESOURCE_DESCS[r]:"";
                        if(str.equals(oldVal)) return oldVal;
                        if(str.length()==0) return "";
                        boolean isResource = CMParms.contains(RawMaterial.RESOURCE_DESCS,str);
                        if((!isResource)&&(mob.session()!=null)&&(!mob.session().killFlag()))
                        {
                            if(!mob.session().confirm("You`ve entered a non-resource item keyword '"+str+"', ok (Y/n)?","Y"))
                                proceed = true;
                            else
                                str=str+"$";
                        }
                    }
                    return str;
                }
            },
            new AbilityParmEditorImpl("AMMO_TYPE","Ammo",PARMTYPE_STRING) {
                public void createChoices() {}
                public int appliesToClass(Object o) { return ((o instanceof Weapon)||(o instanceof Ammunition))?2:-1;}
            },
            new AbilityParmEditorImpl("AMMO_CAPACITY","Ammo#",PARMTYPE_NUMBER) {
                public void createChoices() {}
                public int appliesToClass(Object o) { return ((o instanceof Weapon)||(o instanceof Ammunition))?2:-1;}
            },
            new AbilityParmEditorImpl("MAXIMUM_RANGE","Max",PARMTYPE_NUMBER) { public void createChoices() {} },
            new AbilityParmEditorImpl("RESOURCE_OR_MATERIAL","Rsc/Mat",PARMTYPE_CHOICES) {
                public void createChoices() {
                    Vector V=CMParms.makeVector(RawMaterial.RESOURCE_DESCS);
                    V.addAll(CMParms.makeVector(RawMaterial.MATERIAL_DESCS));
                    createChoices(V);
                }
            },
            new AbilityParmEditorImpl("OPTIONAL_RESOURCE_OR_MATERIAL","Rsc/Mat",PARMTYPE_CHOICES) {
                public void createChoices() {
                    Vector V=CMParms.makeVector(RawMaterial.RESOURCE_DESCS);
                    V.addAll(CMParms.makeVector(RawMaterial.MATERIAL_DESCS));
                    V.addElement("");
                    createChoices(V);
                }
            },
            new AbilityParmEditorImpl("HERB_NAME","Herb Final Name",PARMTYPE_STRING) {
                public void createChoices() {}
                public boolean confirmValue(String oldVal) {
                    if(oldVal.trim().length()==0)
                        return true;
                    if(!oldVal.endsWith("$")) return false;
                    return true;
                }
                public String commandLinePrompt(MOB mob, String oldVal, int[] showNumber, int showFlag) throws java.io.IOException
                {
                    if(oldVal.trim().endsWith("$")) oldVal=oldVal.trim().substring(0,oldVal.trim().length()-1);
                    super.commandLinePrompt(mob,oldVal,showNumber,showFlag);
                    if(oldVal.trim().length()>0)
                        return oldVal.trim()+"$";
                    return oldVal;
                }
            },
            new AbilityParmEditorImpl("RIDE_CAPACITY","Ridrs",PARMTYPE_NUMBER) {
                public void createChoices() {}
                public int appliesToClass(Object o) { return (o instanceof Rideable)?3:-1;}
            },
            new AbilityParmEditorImpl("METAL_OR_WOOD","Metal",PARMTYPE_CHOICES) {
                public void createChoices() { createChoices(new String[]{"METAL","WOOD"});}
            },
            new AbilityParmEditorImpl("OPTIONAL_RACE_ID","Race",PARMTYPE_SPECIAL) {
                public void createChoices() { 
                    createChoices(CMClass.races());
                    choices().addElement("","");
                    for(int x=0;x<choices().size();x++)
                        choices().setElementAt(x,1,((String)choices().elementAt(x,1)).toUpperCase());
                }
                public boolean confirmValue(String oldVal) {
                    if(oldVal.trim().length()==0)
                        return true;
                    Vector parsedVals = CMParms.parse(oldVal.toUpperCase());
                    for(int v=0;v<parsedVals.size();v++)
                        if(CMClass.getRace((String)parsedVals.elementAt(v))==null)
                            return false;
                    return true;
                }
                public String commandLinePrompt(MOB mob, String oldVal, int[] showNumber, int showFlag) throws java.io.IOException
                {
                    if((showFlag>0)&&(showFlag!=showNumber[0])) return oldVal;
                    String behave="NO";
                    String newVal = oldVal;
                    while((mob.session()!=null)&&(!mob.session().killFlag())&&(behave.length()>0))
                    {
                        mob.tell(showNumber+". "+prompt()+": '"+newVal+"'.");
                        if((showFlag!=showNumber[0])&&(showFlag>-999)) return newVal;
                        Vector parsedVals = CMParms.parse(newVal.toUpperCase());
                        behave=mob.session().prompt("Enter a race to add/remove (?)\n\r:","");
                        if(behave.length()>0)
                        {
                            if(behave.equalsIgnoreCase("?"))
                                mob.tell(CMLib.lister().reallyList(CMClass.races(),-1).toString());
                            else
                            {
                                if(parsedVals.contains(behave.toUpperCase().trim()))
                                {
                                    mob.tell("'"+behave+"' removed.");
                                    parsedVals.remove(behave.toUpperCase().trim());
                                    newVal = CMParms.combine(parsedVals,0);
                                }
                                else
                                {
                                    Race R=CMClass.getRace(behave);
                                    if(R!=null)
                                    {
                                        mob.tell(R.ID()+" added.");
                                        parsedVals.addElement(R.ID().toUpperCase());
                                        newVal = CMParms.combine(parsedVals,0);
                                    }
                                    else
                                    {
                                        mob.tell("'"+behave+"' is not a recognized race.  Try '?'.");
                                    }
                                }
                            }
                        }
                        else
                        if(oldVal.equalsIgnoreCase(newVal))
                            mob.tell("(no change)");
                    }
                    return newVal;
                }
            },
            new AbilityParmEditorImpl("INSTRUMENT_TYPE","Instrmnt",PARMTYPE_CHOICES) {
                public void createChoices() { createChoices(MusicalInstrument.TYPE_DESC); }
                public int appliesToClass(Object o) { return (o instanceof MusicalInstrument)?5:-1;}
            },
            new AbilityParmEditorImpl("STONE_FLAG","Stone",PARMTYPE_CHOICES) {
                public void createChoices() { createChoices(new String[]{"","STONE"});}
            },
            new AbilityParmEditorImpl("POSE_NAME","Pose Word",PARMTYPE_ONEWORD) {public void createChoices() {}},
            new AbilityParmEditorImpl("POSE_DESCRIPTION","Pose Description",PARMTYPE_STRING) {public void createChoices() {}},
            new AbilityParmEditorImpl("WOOD_METAL_CLOTH","",PARMTYPE_CHOICES) {
                public void createChoices() { createChoices(new String[]{"WOOD","METAL","CLOTH"});}
            },
            new AbilityParmEditorImpl("WEAPON_TYPE","W.Type",PARMTYPE_CHOICES) {
                public int appliesToClass(Object o) { return (o instanceof Weapon)?2:-1;}
                public void createChoices() { createChoices(Weapon.TYPE_DESCS);}
            },
            new AbilityParmEditorImpl("ATTACK_MODIFICATION","Att.",PARMTYPE_NUMBER) {
                public void createChoices() {}
                public int appliesToClass(Object o) { return (o instanceof Weapon)?2:-1;}
            },
            new AbilityParmEditorImpl("N/A","N/A",PARMTYPE_STRING) {
                public void createChoices() {}
                public int appliesToClass(Object o) { return -1;}
                public boolean confirmValue(String oldVal) { return oldVal.trim().length()==0||oldVal.equals("0");}
            },
            new AbilityParmEditorImpl("RESOURCE_NAME_AMOUNT_MATERIAL_REQUIRED","Resrc/Amt",PARMTYPE_SPECIAL) {
                public void createChoices() { 
                    createChoices(RawMaterial.RESOURCE_DESCS); 
                    choices().addElement("","");
                }
                public int appliesToClass(Object o) { return 0;}
                public boolean confirmValue(String oldVal) { 
                    if(oldVal.trim().length()==0) return true;
                    int x=oldVal.indexOf('/');
                    if(x<0) return false;
                    if(!choices().getDimensionVector(1).contains(oldVal.substring(0,x)))
                        return false;
                    if(!CMath.isInteger(oldVal.substring(x+1)))
                        return false;
                    return true;
                }
                public String commandLinePrompt(MOB mob, String oldVal, int[] showNumber, int showFlag) throws java.io.IOException
                {
                    int x=oldVal.indexOf('/');
                    String oldRsc = "";
                    int oldAmt = 0;
                    if(x>0) {
                        oldRsc = oldVal.substring(0,x);
                        oldAmt = CMath.s_int(oldVal.substring(x));
                    }
                    oldRsc = CMLib.genEd().prompt(mob,oldVal,++showNumber[0],showFlag,prompt(),choices());
                    if(oldRsc.length()>0)
                        return oldRsc+"/"+CMLib.genEd().prompt(mob,oldAmt,++showNumber[0],showFlag,prompt());
                    return "";
                }
            },
            
        });
        Hashtable H = new Hashtable();
        for(int v=0;v<V.size();v++) {
            AbilityParmEditor A = (AbilityParmEditor)V.elementAt(v);
            H.put(A.ID(),A);
        }
        return H;
    };
    
    public abstract class AbilityParmEditorImpl implements AbilityParmEditor 
    {
        private String ID;
        private DVector choices = null;
        private int fieldType;
        private String prompt = null;
        private String header = null;
        
        public AbilityParmEditorImpl(String fieldName, String shortHeader, int type) {
            ID=fieldName; 
            fieldType = type;
            header = shortHeader;
            prompt = CMStrings.capitalizeAndLower(CMStrings.replaceAll(ID,"_"," "));
            createChoices();
        }
        public String ID(){ return ID;}
        public int parmType(){ return fieldType;}
        public String prompt() { return prompt; }
        public String colHeader() { return header;}
        
        public boolean confirmValue(String oldVal)
        {
            boolean spaceOK = fieldType != PARMTYPE_ONEWORD;
            boolean emptyOK = false;
            switch(fieldType) {
                case PARMTYPE_STRINGORNULL:
                    emptyOK = true;
                case PARMTYPE_ONEWORD:
                case PARMTYPE_STRING:
                {
                    if((!spaceOK) && (oldVal.indexOf(' ') >= 0))
                        return false;
                    return (emptyOK)||(oldVal.trim().length()>0);
                }
                case PARMTYPE_NUMBER:
                    return CMath.isInteger(oldVal);
                case PARMTYPE_CHOICES:
                    if(!choices.getDimensionVector(1).contains(oldVal))
                        return choices.getDimensionVector(1).contains(oldVal.toUpperCase().trim());
                    return true;
                case PARMTYPE_MULTICHOICES:
                    return CMath.isInteger(oldVal);
            }
            return false;
        }
        public String commandLinePrompt(MOB mob, String oldVal, int[] showNumber, int showFlag)
            throws java.io.IOException
        {
            String str = null;
            boolean emptyOK = false;
            boolean spaceOK = fieldType != PARMTYPE_ONEWORD;
            switch(fieldType) {
                case PARMTYPE_STRINGORNULL:
                    emptyOK = true;
                case PARMTYPE_ONEWORD:
                case PARMTYPE_STRING:
                {
                    ++showNumber[0];
                    boolean proceed = true;
                    while(proceed) {
                        str = CMLib.genEd().prompt(mob,oldVal,showNumber[0],showFlag,prompt(),emptyOK).trim();
                        if((!spaceOK) && (str.indexOf(' ') >= 0))
                            mob.tell("Spaces are not allowed here.");
                        else
                            proceed=false;
                    }
                    break;
                }
                case PARMTYPE_NUMBER:
                    str = Integer.toString(CMLib.genEd().prompt(mob,Integer.parseInt(oldVal),++showNumber[0],showFlag,prompt()));
                    break;
                case PARMTYPE_CHOICES:
                    str = CMLib.genEd().prompt(mob,oldVal,++showNumber[0],showFlag,prompt(),choices);
                    break;
                case PARMTYPE_MULTICHOICES:
                    str = Integer.toString(CMLib.genEd().promptMulti(mob,Integer.parseInt(oldVal),++showNumber[0],showFlag,prompt(),choices));
                    break;
            }
            return str;
        }
        
        public abstract void createChoices(); 
        public DVector createChoices(Enumeration e) {
            if(choices != null) return choices;
            choices = new DVector(2);
            Object o = null;
            for(;e.hasMoreElements();) {
                o = e.nextElement();
                if(o instanceof String)
                    choices.addElement(o,CMStrings.capitalizeAndLower((String)o));
                else
                if(o instanceof Ability)
                    choices.addElement(((Ability)o).ID(),((Ability)o).name());
                else
                if(o instanceof Race)
                    choices.addElement(((Race)o).ID(),((Race)o).name());
                else
                if(o instanceof Environmental)
                    choices.addElement(((Environmental)o).ID(),((Environmental)o).ID());
            }
            return choices;
        }
        public DVector createChoices(Vector V) { return createChoices(V.elements());}
        public DVector createChoices(String[] S) { return createChoices(CMParms.makeVector(S).elements());}
        public DVector createBinaryChoices(String[] S) { 
            if(choices != null) return choices;
            choices = createChoices(CMParms.makeVector(S).elements());
            for(int i=0;i<choices.size();i++)
                if(i==0)
                    choices.setElementAt(i,1,Integer.toString(0));
                else
                    choices.setElementAt(i,1,Integer.toString(1<<(i-1)));
            return choices;
        }
        public DVector createNumberedChoices(String[] S) { 
            if(choices != null) return choices;
            choices = createChoices(CMParms.makeVector(S).elements());
            for(int i=0;i<choices.size();i++)
                choices.setElementAt(i,1,Integer.toString(i));
            return choices;
        }
        public DVector choices() { return choices; } 
        public int appliesToClass(Object o) { return o instanceof Item?0:-1;}
    }
}
