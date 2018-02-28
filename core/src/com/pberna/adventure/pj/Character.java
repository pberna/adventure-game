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

package com.pberna.adventure.pj;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.utils.Json;
import com.pberna.adventure.items.Item;
import com.pberna.adventure.items.ItemEquipable;
import com.pberna.adventure.items.manager.ItemManager;
import com.pberna.adventure.pj.race.BaseRace;
import com.pberna.adventure.pj.race.Dwarf;
import com.pberna.adventure.pj.race.Elf;
import com.pberna.adventure.pj.race.Halfling;
import com.pberna.adventure.pj.race.Human;
import com.pberna.adventure.spells.EnhanceCombatStatsSpell;
import com.pberna.adventure.spells.EnhanceSkillSpell;
import com.pberna.adventure.spells.Spell;
import com.pberna.engine.localization.ILocalizable;

public class Character implements ILocalizable {
	
	public static final int MaxCharacterNameLength = 12;
	public static final int LifePointsMultiplier = 3;
	public static final int PowerPointsMultiplier = 3;
	public static final int LuckPointsMultiplier = 3;
	
	private String name;
	private Gender gender;
	private String portraitImageName;
	private BaseRace race;
	private ArrayList<Attribute> attributes;
	private ArrayList<Skill> skills;
	private Backpack backpack;
	private int currentLifePoints;
	private int currentPowerPoints;
	private HashMap<EEquipmentPosition, ItemEquipable> equippedItems;
	private ArrayList<Spell> spells;
	private ArrayList<Spell> activeSpells;
	private int currentLuckPoints;
	 
	public Character()	{
		name = "";
		gender = Gender.NotSet;
		portraitImageName = "";
		setRace(null);
		attributes = Attribute.getAttributes();
		skills = Skill.getSkills();
		backpack = new Backpack();
		currentLifePoints = 1;
		currentPowerPoints = 0;
		equippedItems = new HashMap<EEquipmentPosition, ItemEquipable>(EEquipmentPosition.values().length);
		spells = new ArrayList<Spell>(Spell.TotalNumberSpells);
		activeSpells = new ArrayList<Spell>();
		currentLuckPoints = 0;
	}
	
	public String getName() { return name; }
	public void setName(String name) {
		this.name = name;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getPortraitImageName() {
		return portraitImageName;
	}
	public void setPortraitImageName(String portraitImageName) {
		this.portraitImageName = portraitImageName;
	}
	public BaseRace getRace() {
		return race;
	}
	public void setRace(BaseRace race) {
		this.race = race;
	}
	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}
	public ArrayList<Skill> getSkills() {
		return skills;
	}
	public void setSkills(ArrayList<Skill> skills) {
		this.skills = skills;
	}
	public Backpack getBackpack() {
		return backpack;
	}

	public void setBackpack(Backpack backpack) {
		this.backpack = backpack;
	}

	public int getCurrentLifePoints() {
		return currentLifePoints;
	}

	public void setCurrentLifePoints(int currentLifePoints) {
		this.currentLifePoints = Math.min(Math.max(0, currentLifePoints), getMaximumLifePoints());
	}
	
	public int getMaximumLifePoints() {
		return Math.max(1, getTotalAttributeValue(Attribute.IdMight) * LifePointsMultiplier);			
	}

	public int getCurrentPowerPoints() {
		return currentPowerPoints;
	}

	public void setCurrentPowerPoints(int currentPowerPoints) {
		this.currentPowerPoints = Math.min(Math.max(0, currentPowerPoints), getMaximumPowerPoints());
	}
	
	public int getMaximumPowerPoints() {
		return getTotalAttributeValue(Attribute.IdIntelligence) * PowerPointsMultiplier;			
	}

	public void resetAttributesValues() {
		for(Attribute attribute: attributes){
			attribute.setValue(Attribute.MinimumGenericValue);
		}		
	}	

	public void resetSkillValues() {
		for(Skill skill: skills){
			skill.setValue(Skill.MinimumGenericValue);
		}
	}

	public int getBaseAttributeValue(int idAttribute) {
		Attribute searchedAttribute = Attribute.findAttribute(attributes, idAttribute);
		
		if(searchedAttribute != null) {
			return searchedAttribute.getValue() + race.getAttributeModifier(idAttribute);
		}
		
		return 0;
	}
	
	public int getModifierAttributeValue(int idAttribute) {
		int modifier = 0;
		
		for(ItemEquipable item:equippedItems.values()) {
			if(item != null) {
				modifier += item.getAttributeModifier(idAttribute);
			}
		}
		
		return modifier;
	}
	
	public int getTotalAttributeValue(int idAttribute) {
		return getBaseAttributeValue(idAttribute) + getModifierAttributeValue(idAttribute);
	}

	public int getBaseSkillValue(int idSkill) {
		Skill searchedSkill = Skill.findSkill(skills, idSkill);
		
		if(searchedSkill != null) {
			return searchedSkill.getValue() + race.getSkillModifier(idSkill);
		}
		
		return 0;
	}
	
	public int getModifierSkillValue(int idSkill) {
		int modifier = 0;
		for(Spell spell:activeSpells) {
			if(spell instanceof EnhanceSkillSpell) {
				modifier += ((EnhanceSkillSpell)spell).getSkillModifier(idSkill);
			}
			if(idSkill == Skill.IdMagic && spell instanceof EnhanceCombatStatsSpell) {
				modifier += ((EnhanceCombatStatsSpell)spell).getMagicModifier();
			}
		}
		
		for(ItemEquipable item:equippedItems.values()) {
			if(item != null) {
				modifier += item.getSkillModifier(idSkill);
			}
		}
		
		return modifier;
	}
	
	public int getTotalSkillValue(int idSkill) {
		return getBaseSkillValue(idSkill) + getModifierSkillValue(idSkill);
	}
	
	public int getBaseAttackValue() {
		return getBaseSkillValue(Skill.IdFighting);
	}
	
	public int getModifierAttackValue() {
		int modifier = 0;
		for(Spell spell:activeSpells) {
			if(spell instanceof EnhanceCombatStatsSpell) {
				modifier += ((EnhanceCombatStatsSpell)spell).getAttackModifier();
			}
		}
		
		for(ItemEquipable item:equippedItems.values()) {
			if(item != null) {
				modifier += item.getAttackModifier();
			}
		}
		
		return modifier;
	}
	
	public int getTotalAttackValue() {
		return getBaseAttackValue() + getModifierAttackValue();
	}
	
	public int getBaseDefenseValue() {
		return getBaseSkillValue(Skill.IdFighting);
	}
	
	public int getModifierDefenseValue() {
		int modifier = 0;
		for(Spell spell:activeSpells) {
			if(spell instanceof EnhanceCombatStatsSpell) {
				modifier += ((EnhanceCombatStatsSpell)spell).getDefenseModifier();
			}
		}
		
		for(ItemEquipable item:equippedItems.values()) {
			if(item != null) {
				modifier += item.getDefenseModifier();
			}
		}
		
		return modifier;
	}
	
	public int getTotalDefenseValue() {
		return getBaseDefenseValue() + getModifierDefenseValue();
	}
	
	public int getModifierInitiativeValue() {
		int modifier = 0;
		for(ItemEquipable item:equippedItems.values()) {
			if(item != null) {
				modifier += item.getInitiativeModifier();
			}
		}
		return modifier;
	}
	
	public int getTotalInitiativeValue() {
		return getTotalAttributeValue(Attribute.IdAgility) + getModifierInitiativeValue();
	}
	
	public ItemEquipable getEquippedItem(EEquipmentPosition position) {
		if(equippedItems.containsKey(position)) {
			return equippedItems.get(position);
		}
		return null;
	}
	
	public boolean setEquippedItem(EEquipmentPosition position, ItemEquipable item) {
		if(item == null) {
			equippedItems.put(position, item);
			return false;
		}
		
		boolean equipped = item.equip(position);
		if(equipped) {
			equippedItems.put(position, item);
		}
		return equipped;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}
	
	public ArrayList<Spell> getCombatSpells() {
		ArrayList<Spell> combatSpells = new ArrayList<Spell>();
		
		for(Spell spell: this.spells) {
			if(spell.canBeCastedInCombat()) {
				combatSpells.add(spell);
			}
		}
		
		return combatSpells;
	}
	
	public void resetSpells (){
		spells.clear();
	}
	
	public boolean hasLearntSpell(Spell spell) {
		return (Spell.findSpells(spells, spell.getId()) != null); 
	}
	
	public boolean hasEnoughPowerPointsToCastSpell(Spell spell) {
		return getCurrentPowerPoints() >= spell.getPowerPointsNeeded();
	}
	
	public boolean canCastSpell(Spell spell) {
		return hasLearntSpell(spell) && hasEnoughPowerPointsToCastSpell(spell);
	}
	
	public boolean hasItem(Item item) {
		
		for(int i = 0 ; i < backpack.getItemCount(); i++) {
			if(backpack.getItem(i).getId() == item.getId()) {
				return true;
			}
		}

		for(EEquipmentPosition position: EEquipmentPosition.values()) {
			Item equippedItem = getEquippedItem(position);
			if(equippedItem != null && equippedItem.getId() == item.getId()) {
				return true;
			}
		}
		
		return false;
	}

	public boolean removeItem(int itemId) {
		if(!removeItemFromBackpack(itemId)) {
			return removeItemFromEquipped(itemId);
		}
		return true;
	}

	private boolean removeItemFromBackpack(int itemId) {
		int removeIndex = -1;
		for(int i = 0 ; i < backpack.getItemCount(); i++) {
			if(backpack.getItem(i).getId() == itemId) {
				removeIndex = i;
				break;
			}
		}

		if(removeIndex >= 0) {
			backpack.removeItem(removeIndex);
			return true;
		}
		return false;
	}

	private boolean removeItemFromEquipped(int itemId) {
		EEquipmentPosition removePosition = null;
		for(EEquipmentPosition position: EEquipmentPosition.values()) {
			Item equippedItem = getEquippedItem(position);
			if(equippedItem != null && equippedItem.getId() == itemId) {
				removePosition = position;
			}
		}
		if(removePosition != null) {
			equippedItems.remove(removePosition);
			return true;
		}
		return false;
	}
	
	public void applyDamage(int damage) {
		if(damage > 0) {
			setCurrentLifePoints(currentLifePoints - damage);
		}
	}
	
	public ArrayList<Spell> getActiveSpells() {
		return activeSpells;
	}
	
	public void addActiveSpell(Spell spell) {
		if(!(spell instanceof EnhanceCombatStatsSpell || spell instanceof EnhanceSkillSpell)) {
			return;
		}
		
		for(Spell activeSpell: activeSpells) {
			if(activeSpell.getId() == spell.getId()) {
				return;
			}
		}
		
		activeSpells.add(spell);
	}
	
	public void removeActiveSpell(int idSpell) {
		Spell spellToRemove = null;
		
		for(Spell activeSpell: activeSpells) {
			if(activeSpell.getId() == idSpell) {
				spellToRemove = activeSpell;
				break;
			}
		}
		
		if(spellToRemove != null) {
			activeSpells.remove(spellToRemove);
		}
	}
	
	public boolean hasActiveSpell(int idSpell) {
		return Spell.findSpells(activeSpells, idSpell) != null;
	}

	@Override
	public void refreshLocalizableItems() {
		localizeBaseRace();
		localizeAttributes();
		localizeSkills();
		backpack.refreshLocalizableItems();
		localizeEquippedItems();
		localizeSpells();
	}	

	private void localizeBaseRace() {
		if(race instanceof Human) {
			race = new Human();
		} else if(race instanceof Elf) {
			race = new Elf();
		} else if(race instanceof Dwarf) {
			race = new Dwarf();
		} else if(race instanceof Halfling) {
			race = new Halfling();
		}		
	}
	
	private void localizeAttributes() {
		ArrayList<Attribute> localizedAttributes = Attribute.getAttributes();
		
		for(Attribute characterAttribute: attributes) {
			Attribute localizedAttribute = Attribute.findAttribute(localizedAttributes, characterAttribute.getId());
			if(localizedAttribute != null) {
				characterAttribute.setName(localizedAttribute.getName());
				characterAttribute.setDescription(localizedAttribute.getDescription());
			}
		}
	}
	
	private void localizeSkills() {
		ArrayList<Skill> localizedSkills = Skill.getSkills();
		
		for(Skill characterSkill: skills) {
			Skill localizedSkill = Skill.findSkill(localizedSkills, characterSkill.getId());
			if(localizedSkill != null) {
				characterSkill.setName(localizedSkill.getName());
				characterSkill.setDescription(localizedSkill.getDescription());
			}
		}		
	}
	
	private void localizeEquippedItems() {
		for(EEquipmentPosition position: EEquipmentPosition.values()) {
			ItemEquipable item = equippedItems.get(position);
			if(item != null) {
				Item localizedItemNotCasted = ItemManager.getInstance().getItem(item.getId());
				ItemEquipable localizedItem = (localizedItemNotCasted instanceof ItemEquipable)
						? (ItemEquipable) localizedItemNotCasted : null;

				if(localizedItem != null) {
					localizedItem.equip(position);
					equippedItems.put(position, localizedItem);
				}
			}
		}
	}
	
	private void localizeSpells() {
		localizeSpellsList(spells);
		localizeSpellsList(activeSpells);
	}

	private void localizeSpellsList(ArrayList<Spell> spellsList) {
		ArrayList<Spell> localizedSpells = Spell.getSpells();
		
		ArrayList<Integer> listSpellsIds = new ArrayList<Integer>();
		for(Spell spell:spellsList) {
			listSpellsIds.add(spell.getId());
		}		
		spellsList.clear();
		
		for(Integer id:listSpellsIds) {
			Spell spell = Spell.findSpells(localizedSpells, id.intValue());
			if(spell != null) {
				spellsList.add(spell);
			}
		}
	}

	public static String getJsonFromCharacter(Character character) {
		Json json = new Json();
		return json.toJson(character);
	}

	public static Character getCharacterFromJson(String text) {
		Json json = new Json();
		Character character = json.fromJson(Character.class, text);

		HashMap<EEquipmentPosition, ItemEquipable> newEquippedItems = new HashMap<EEquipmentPosition, ItemEquipable>();
		HashMap castedHashMap = ((HashMap)(character.equippedItems));
		for(Object stringPosition:castedHashMap.keySet()) {
			ItemEquipable item = (ItemEquipable)castedHashMap.get(stringPosition);
			EEquipmentPosition position = EEquipmentPosition.valueOf(stringPosition.toString());
			if(item != null) {
				item.equip(position);
				newEquippedItems.put(position, item);
			}
		}
		character.equippedItems = newEquippedItems;

		return character;
	}

	public int getCurrentLuckPoints() {
		return currentLuckPoints;
	}

	public void setCurrentLuckPoints(int currentLuckPoints) {
		this.currentLuckPoints = Math.min(Math.max(0, currentLuckPoints), getMaximumLuckPoints());
	}

	public int getMaximumLuckPoints() {
		return getTotalAttributeValue(Attribute.IdFortune) * LuckPointsMultiplier;
	}
}
