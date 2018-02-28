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

package com.pberna.adventure;

import java.util.ArrayList;
import com.pberna.adventure.items.Item;
import com.pberna.adventure.items.manager.ItemManager;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Gender;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.pj.race.Human;
import com.pberna.adventure.spells.Spell;

public class TestingHelper {

	private TestingHelper() {
		
	}
	
	public static Character createTestingCharacter()
	{
		Character character = new Character();

		character.setName("Pedro");
		character.setGender(Gender.Male);
		character.setPortraitImageName("man2");
		character.setRace(new Human());
		character.setAttributes(getTestingAttributes());
		character.setSkills(getTestingSkills());
		character.setSpells(getTestingSpells(character.getBaseSkillValue(Skill.IdMagic) ));
		character.setCurrentLifePoints(character.getMaximumLifePoints() - 5);
		character.setCurrentPowerPoints(character.getMaximumPowerPoints() - 4);
		character.setCurrentLuckPoints(character.getMaximumLuckPoints() - 3);

		for(int i = 1;;i++) {
			Item item = ItemManager.getInstance().getItem(i);
			if(item == null) {
				break;
			}
			character.getBackpack().addItem(item);
		}

		return character;
	}

	private static ArrayList<Attribute> getTestingAttributes() {
		ArrayList<Attribute> attributes = Attribute.getAttributes();

		int attributeValue = Attribute.TotalAttributePointsPerPj / attributes.size();
		for(Attribute attribute: attributes) {
			attribute.setValue(attributeValue);
		}
		return attributes;
	}

	private static ArrayList<Skill> getTestingSkills() {
		ArrayList<Skill> skills = Skill.getSkills();

		int skillValue = Skill.TotalSkillPointsPerPj / skills.size();
		for(Skill skill: skills) {
			skill.setValue(skillValue);
			if(skill.getId() == Skill.IdMagic) {
				skill.setValue(50);
			}
			if(skill.getId() == Skill.IdFighting) {
				skill.setValue(50);
			}
		}
		return skills;
	}
	
	private static ArrayList<Spell> getTestingSpells(int magicValue) {
		ArrayList<Spell> spells = new ArrayList<Spell>();
		
		for(Spell spell: Spell.getSpells()) {
			if(spells.size() < magicValue)
			{
				spells.add(spell);
			}
			else
			{
				break;
			}
		}
		
		return spells;
	}
}
