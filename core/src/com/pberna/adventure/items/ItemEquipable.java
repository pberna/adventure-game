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

package com.pberna.adventure.items;

import java.util.HashMap;

import com.pberna.adventure.pj.EEquipmentPosition;

public abstract class ItemEquipable extends Item {
		
	private boolean equipped;
	private EEquipmentPosition equipmentPosition;
	private HashMap<Integer, Integer> skillModifiers;
	private HashMap<Integer, Integer> attributeModifiers;
	
	public ItemEquipable() {
		super();
		this.equipped = false;
		this.equipmentPosition = null;
		this.skillModifiers = new HashMap<Integer, Integer>();
		this.attributeModifiers = new HashMap<Integer, Integer>();
	}	
	
	public abstract EEquipmentPosition[] getAllowedPositions();
		
	public boolean isEquipped() {
		return equipped;
	}
	
	public EEquipmentPosition getEquipmentPosition() {
		return equipmentPosition;
	}
		
	public boolean equip(EEquipmentPosition equipmentPosition) {
		boolean canEquip = false;
		for(EEquipmentPosition allowedPosition: getAllowedPositions()) {
			if(allowedPosition == equipmentPosition) {
				canEquip = true;
				break;
			}
		}
		
		if(!canEquip) {
			return false;
		}
		
		this.equipped = true;
		this.equipmentPosition = equipmentPosition;
		
		return true;
	}
	
	public void unEquip() {
		this.equipped = false;
		this.equipmentPosition = null;
	}
	
	private int attackModifier;
	private int defenseModifier;
	private int initiativeModifier;
	
	public int getAttackModifier() {
		return attackModifier;
	}
	public ItemEquipable setAttackModifier(int attackModifier) {
		this.attackModifier = attackModifier;
		return this;
	}
	
	public int getDefenseModifier() {
		return defenseModifier;
	}
	public ItemEquipable setDefenseModifier(int defenseModifier) {
		this.defenseModifier = defenseModifier;
		return this;
	}
	
	public int getInitiativeModifier() {
		return initiativeModifier;
	}
	public ItemEquipable setInitiativeModifier(int initiativeModifier) {
		this.initiativeModifier = initiativeModifier;
		return this;
	}
	
	public ItemEquipable addSkillModifier(int idSkill, int modifier) {
		skillModifiers.put(new Integer(idSkill), new Integer(modifier));
		return this;
	}
	
	public int getSkillModifier(int idSkill) {
		Integer idSkillInteger = new Integer(idSkill);
		
		if(skillModifiers.containsKey(idSkillInteger)) {
			return skillModifiers.get(idSkillInteger).intValue();
		}
		
		return 0;
	}	
	
	public boolean modifiesSkill(int idSkill) {
		return getSkillModifier(idSkill) != 0;
	}
	
	public ItemEquipable addAttributeModifier(int idAttribute, int modifier) {
		attributeModifiers.put(new Integer(idAttribute), new Integer(modifier));
		return this;
	}
	
	public int getAttributeModifier(int idAttribute) {
		Integer idAttributeInteger = new Integer(idAttribute);
		
		if(attributeModifiers.containsKey(idAttributeInteger)) {
			return attributeModifiers.get(idAttributeInteger).intValue();
		}
		
		return 0;
	}	
	
	public boolean modifiesAttribute(int idAttribute) {
		return getAttributeModifier(idAttribute) != 0;
	}
}
