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

import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Skill;
import com.pberna.engine.localization.Localization;

public class Elf extends BaseRace{
	
	public static final int MinimumMagic = 4;
	
	@Override
	public int getId() {
		return IdRaceElf;
	}
	
	@Override
	public String getName() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "raceElf");
	}
	
	@Override
	public String getNameMale() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "raceElfMale");
	}
	
	@Override
	public String getNameFemale() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "raceElfFemale");
	}
	
	@Override
	public String getDescription() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "descriptionElf");
	}
	
	@Override
	public String getImageName() {
		return "elf_button";
	}
	
	@Override
	public int getAttributeModifier(int idAttribute) {
		switch(idAttribute){
			case Attribute.IdAgility:
				return 1;
			case Attribute.IdIntelligence:
				return 1;
			case Attribute.IdFortune:
				return -1;	
		}
		return 0;
	}

	@Override
	public int getSkillModifier(int idSkill) {
		switch(idSkill){
			case Skill.IdKnowledge:
				return 1;
			case Skill.IdMagic:
				return 1;
		}
		return 0;
	}	
	
	public int getMinimumMagic() {		
		return MinimumMagic;
	}
}
