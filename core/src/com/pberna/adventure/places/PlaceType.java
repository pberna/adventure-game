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

package com.pberna.adventure.places;

import java.util.ArrayList;

public class PlaceType {
	
	public static final int IdOptionChoosePlace = 1;
	public static final int IdCombatPlace = 2;
	public static final int IdSkillCheckPlace = 3;
	public static final int IdAttributeCheckPlace = 4;
	public static final int IdItemUsePlace = 5;
	public static final int IdSpellUsePlace = 6;
	public static final int IdEndAdventurePlace = 7;
	public static final int IdEffectPlace = 8;
	
	private int id;
	private String name;

	public PlaceType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static ArrayList<PlaceType> getAllPlaceTypes() {
		ArrayList<PlaceType> listPlaceTypes = new ArrayList<PlaceType>();
		
		listPlaceTypes.add(new PlaceType(IdOptionChoosePlace, "OptionChoosePlace"));
		listPlaceTypes.add(new PlaceType(IdCombatPlace, "CombatPlace"));
		listPlaceTypes.add(new PlaceType(IdSkillCheckPlace, "SkillCheckPlace"));
		listPlaceTypes.add(new PlaceType(IdAttributeCheckPlace, "AttributeCheckPlace"));
		listPlaceTypes.add(new PlaceType(IdItemUsePlace, "ItemUsePlace"));
		listPlaceTypes.add(new PlaceType(IdSpellUsePlace, "SpellUsePlace"));
		listPlaceTypes.add(new PlaceType(IdEndAdventurePlace, "EndAdventurePlace"));
		listPlaceTypes.add(new PlaceType(IdEffectPlace, "EffectPlace"));
		
		return listPlaceTypes;
	}
	
	public static PlaceType findPlaceType(ArrayList<PlaceType> listPlaceTypes, int idPlaceType) {
		for(PlaceType placetype:listPlaceTypes) {
			if(placetype.getId() == idPlaceType) {
				return placetype;
			}
		}
		return null;
	}
}
