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

public class Human extends BaseRace{
	
	@Override
	public int getId() {
		return IdRaceHuman;
	}
	
	@Override
	public String getName() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "raceHuman");
	}	
	
	@Override
	public String getNameMale() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "raceHumanMale");
	}
	
	@Override
	public String getNameFemale() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "raceHumanFemale");
	}
	
	@Override
	public String getDescription() {
		return Localization.getInstance().getTranslation(
				"PjInformation", "descriptionHuman");
	}
	
	@Override
	public String getImageName() {
		return "human_button";
	}	
	
	@Override
	public int getAttributeModifier(int idAttribute) {
		switch(idAttribute){
			case Attribute.IdMight:
				return 1;				
		}
		return 0;
	}

	@Override
	public int getSkillModifier(int idSkill) {
		switch(idSkill){
			case Skill.IdFighting:
				return 1;
			case Skill.IdSocial:
				return 1;
		}
		return 0;
	}	

}
