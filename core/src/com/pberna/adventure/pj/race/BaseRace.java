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

package com.pberna.adventure.pj.race;

import java.util.ArrayList;

import com.pberna.adventure.pj.Skill;

public abstract class BaseRace {
	
	public static final int IdRaceHuman = 1;
	public static final int IdRaceElf = 2;
	public static final int IdRaceDwarf = 3;
	public static final int IdRaceHalfling = 4;
	
	public abstract int getId();
	
	public abstract String getName();
	
	public abstract String getNameMale();
	
	public abstract String getNameFemale();
	
	public abstract String getDescription();
		
	public abstract String getImageName();
	
	public abstract int getAttributeModifier(int idAttribute);
	
	public abstract int getSkillModifier(int idSkill);
	
	public int getMinimumMagic() {		
		return Skill.MinimumGenericValue;
	}
	
	public int getMaximumMagic() {
		return Skill.MaximumGenericValue;
	}	
	
	public static ArrayList<BaseRace> getAvailableRaces(){
		ArrayList<BaseRace> races = new ArrayList<BaseRace>();
		
		//add all available races
		races.add(new Human());
		races.add(new Elf());
		races.add(new Dwarf());
		races.add(new Halfling());
		
		return races;
	}
}
