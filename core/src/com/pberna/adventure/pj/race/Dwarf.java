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

public class Dwarf extends BaseRace {

	public static final int MaximumMagic = 4;
	
	@Override
	public int getId() {
		return IdRaceDwarf;
	}	
	
	@Override
	public String getName() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "raceDwarf");
	}
	
	@Override
	public String getNameMale() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "raceDwarfMale");
	}
	
	@Override
	public String getNameFemale() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "raceDwarfFemale");
	}
	
	@Override
	public String getDescription() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "descriptionDwarf");
	}
	
	@Override
	public String getImageName() {
		return "dwarf_button";
	}
	
	@Override
	public int getAttributeModifier(int idAttribute) {
		switch(idAttribute){
			case Attribute.IdMight:
				return 2;
			case Attribute.IdAgility:
				return -1;
		}
		return 0;
	}

	@Override
	public int getSkillModifier(int idSkill) {
		switch(idSkill){
			case Skill.IdFighting:
				return 1;
			case Skill.IdStealth:
				return 1;
		}
		return 0;
	}	

	public int getMaximumMagic() {		
		return MaximumMagic;
	}	
}
