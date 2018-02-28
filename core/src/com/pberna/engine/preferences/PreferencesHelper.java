/*
 *   Adventure Game, a digital gamebook written in java with Libgdx.
 *   Copyright (C) 2018 Pedro Berná
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *   Email contact: lomodastudios@gmail.com
 */

package com.pberna.engine.preferences;

import java.util.ArrayList;

import com.badlogic.gdx.Preferences;

public class PreferencesHelper {
	
	private PreferencesHelper(){
	
	}
	
	public static void putIntegerArrayList(Preferences preferences, 
			String key, ArrayList<Integer> values)
	{		
		String strValue = "";
		
		for(Integer value: values)
		{
			if(strValue == "")
			{
				strValue = value.toString();					
			}
			else
			{
				strValue += "," + value.toString();
			}
		}
		
		preferences.putString(key, strValue);
	}
	
	public static ArrayList<Integer> getIntegerArrayList(Preferences preferences, 
			String key)
    {
    	String strValue = preferences.getString(key, "");
    	
    	ArrayList<Integer> values = new ArrayList<Integer>();    	
    	for(String value: strValue.split(","))
    	{
    		if(value != "")
    			values.add(Integer.parseInt(value));
    	}
    	
    	return values;
    }
}
