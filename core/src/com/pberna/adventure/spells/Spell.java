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

package com.pberna.adventure.spells;

import java.util.ArrayList;

import com.pberna.adventure.pj.Skill;
import com.pberna.engine.localization.Localization;

public class Spell {
	public static final int TotalNumberSpells = 11;
	
	public static final int IdElectricDischarge = 1;
	public static final int IdQuestion = 2;
	public static final int IdShield = 3;
	public static final int IdCheat = 4;
	public static final int IdExpertise = 5;
	public static final int IdIceRay= 6;
	public static final int IdMight = 7;
	public static final int IdFear = 8;
	public static final int IdFireRay = 9;
	public static final int IdLevitation = 10;
	public static final int IdHeal = 11;
	public static final int IdMagicRitual = 12;
	
	private int id;
	private String name;
	private String description;
	private int minimumMagicRequired;
	private int powerPointsNeeded;
	private boolean canBeCastedInCombat;
	private boolean canBeCastedInAdventure;	
	private String imageName; 
	
	public int getId() {
		return id;
	}
	
	public Spell setId(int id) {
		this.id = id;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public Spell setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Spell setDescription(String description) {
		this.description = description;
		return this;
	}

	public int getMinimumMagicRequired() {
		return minimumMagicRequired;
	}

	public Spell setMinimumMagicRequired(int minimumMagicRequired) {
		this.minimumMagicRequired = minimumMagicRequired;
		return this;
	}

	public int getPowerPointsNeeded() {
		return powerPointsNeeded;
	}

	public Spell setPowerPointsNeeded(int powerPointsNeeded) {
		this.powerPointsNeeded = powerPointsNeeded;
		return this;
	}

	public boolean canBeCastedInCombat() {
		return canBeCastedInCombat;
	}

	public Spell setCanBeCastedInCombat(boolean canBeCastedInCombat) {
		this.canBeCastedInCombat = canBeCastedInCombat;
		return this;
	}

	public boolean canBeCastedInAdventure() {
		return canBeCastedInAdventure;
	}

	public Spell setCanBeCastedInAdventure(boolean canBeCastedInAdventure) {
		this.canBeCastedInAdventure = canBeCastedInAdventure;
		return this;
	}

	public String getImageName() {
		return imageName;
	}

	public Spell setImageName(String imageName) {
		this.imageName = imageName;
		return this;
	}
	
	public static ArrayList<Spell> getSpells()
	{
		ArrayList<Spell> listSpells = new ArrayList<Spell>(TotalNumberSpells);
		
		//electric discharge
		listSpells.add(buildSpell(new AttackSpell().setDamageMultiplier(0.25f), IdElectricDischarge, "spellElectricDischargeName", 
				"spellElectricDischargeDescription", 1, 1, true, false, "spell_electric_discharge"));
		
		//question
		listSpells.add(buildSpell(new Spell(), IdQuestion, "spellQuestionName", "spellQuestionDescription", 
				1, 1, false, false, "spell_question"));						

		//shield
		listSpells.add(buildSpell(new EnhanceCombatStatsSpell().setDefenseModifier(1), IdShield, "spellShieldName", 
				"spellShieldDescription", 2, 2, true, true, "spell_shield"));						
		
		//cheat
		listSpells.add(buildSpell(new Spell(), IdCheat, "spellCheatName", "spellCheatDescription", 
				2, 2, false, false, "spell_cheat"));						

		//expertise
		listSpells.add(buildSpell(new EnhanceSkillSpell().addSkillModifier(Skill.IdStealth, 2), IdExpertise, 
				"spellExpertiseName", "spellExpertiseDescription", 2, 2, false, true, "spell_expertise"));						

		//ice ray
		listSpells.add(buildSpell(new AttackSpell().setDamageMultiplier(0.75f), IdIceRay, "spellIceRayName", 
				"spellIceRayDescription", 3, 3, true, false, "spell_ice_ray"));						

		//might
		listSpells.add(buildSpell(new EnhanceCombatStatsSpell().setAttackModifier(2), IdMight, "spellMightName", 
				"spellMightDescription", 3, 3, true, true, "spell_might"));						

		//weakness
		listSpells.add(buildSpell(new Spell(), IdFear, "spellFearName", "spellFearDescription",
				3, 3, false, false, "spell_weakness"));						

		//fire ray
		listSpells.add(buildSpell(new AttackSpell().setDamageMultiplier(1.0f), IdFireRay, "spellFireRayName", 
				"spellFireRayDescription", 4, 4, true, false, "spell_fire_ray"));						

		//levitation
		listSpells.add(buildSpell(new Spell(), IdLevitation, "spellLevitationName", "spellLevitationDescription", 
				4, 4, false, false, "spell_levitation"));						

		//heal
		listSpells.add(buildSpell(new HealSpell().setHealPercentage(1.0f), IdHeal, "spellHealName", 
				"spellHealDescription",	5, 5, true, true, "spell_healing"));

		//magic ritual
		listSpells.add(buildSpell(new EnhanceCombatStatsSpell().setMagicModifier(3).setDefenseModifier(1),
				IdMagicRitual, "spellMagicRitualName", "spellMagicRitualDescription", 6, 6,
				true, true, "magic_ritual"));

		return listSpells;
	}
	
	private static Spell buildSpell(Spell spell, int id, String spellName, String spellDescription, int minimumMagic,
			int powerPointsNeeded, boolean canBeCastedInCombat, boolean canBeCastedInAdventure, String imageName)
	{
		return spell
		.setId(id)
		.setName(Localization.getInstance().getTranslation("Spells", spellName))
		.setDescription(Localization.getInstance().getTranslation("Spells", spellDescription))
		.setMinimumMagicRequired(minimumMagic)
		.setPowerPointsNeeded(powerPointsNeeded)
		.setCanBeCastedInCombat(canBeCastedInCombat)
		.setCanBeCastedInAdventure(canBeCastedInAdventure)
		.setImageName(imageName);
	}
	
	public static Spell findSpells(ArrayList<Spell> listSpells, int idSpell) 
	{
		for(Spell spell: listSpells){
			if(spell.getId() == idSpell)
			{
				return spell;
			}
		}
		return null;
	}

	public static void orderSpellsById(ArrayList<Spell> listSpells) {
		for(int i = 0; i < listSpells.size(); i++) {
			for(int j = i + 1; j < listSpells.size(); j++) {
				if(listSpells.get(j).getId() < listSpells.get(i).getId()) {
					Spell temp = listSpells.get(i);
					listSpells.set(i, listSpells.get(j));
					listSpells.set(j, temp);
				}
			}
		}
	}
}
