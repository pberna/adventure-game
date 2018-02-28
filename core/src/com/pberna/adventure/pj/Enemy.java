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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pberna.adventure.spells.AttackSpell;
import com.pberna.adventure.spells.Spell;

import java.util.ArrayList;

public class Enemy {
	private int id;
	private String name;
	private String imageName;
	private int attackValue;
	private int defenseValue;
	private int magicValue;
	private int initiativeValue;
	private int currentLifePoints;
	private int maximumLifePoints;
	private int currentPowerPoints;
	private int maximumPowerPoints;
	private int currentLuckPoints;
	private int maximumLuckPoints;
	private ArrayList<Spell> spells;

	private TextureRegion textureImage;

	public Enemy() {
		spells = new ArrayList<Spell>();
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
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public int getAttackValue() {
		return attackValue;
	}
	public void setAttackValue(int attackValue) {
		this.attackValue = attackValue;
	}
	
	public int getDefenseValue() {
		return defenseValue;
	}
	public void setDefenseValue(int defenseValue) {
		this.defenseValue = defenseValue;
	}
	
	public int getMagicValue() {
		return magicValue;
	}
	public void setMagicValue(int magicValue) {
		this.magicValue = magicValue;
	}
	
	public int getCurrentLifePoints() {
		return currentLifePoints;
	}	
	public void setCurrentLifePoints(int currentLifePoints) {
		this.currentLifePoints = Math.min(Math.max(0, currentLifePoints), getMaximumLifePoints());
	}
	
	public int getMaximumLifePoints() {
		return maximumLifePoints;
	}
	public void setMaximumLifePoints(int maximumLifePoints) {
		this.maximumLifePoints = maximumLifePoints;
	}
	
	public int getCurrentPowerPoints() {
		return currentPowerPoints;
	}
	public void setCurrentPowerPoints(int currentPowerPoints) {
		this.currentPowerPoints = Math.min(Math.max(0, currentPowerPoints), getMaximumPowerPoints());
	}
	
	public int getMaximumPowerPoints() {
		return maximumPowerPoints;
	}
	public void setMaximumPowerPoints(int maximumPowerPoints) {
		this.maximumPowerPoints = maximumPowerPoints;
	}
	public int getInitiativeValue() {
		return initiativeValue;
	}
	public void setInitiativeValue(int initiativeValue) {
		this.initiativeValue = initiativeValue;
	}	
	
	public void applyDamage(int damage) {
		if(damage > 0) {
			setCurrentLifePoints(currentLifePoints - damage);
		}
	}

	public int getCurrentLuckPoints() {
		return currentLuckPoints;
	}

	public void setCurrentLuckPoints(int currentLuckPoints) {
		this.currentLuckPoints = currentLuckPoints;
	}

	public int getMaximumLuckPoints() {
		return maximumLuckPoints;
	}

	public void setMaximumLuckPoints(int maximumLuckPoints) {
		this.maximumLuckPoints = maximumLuckPoints;
	}

	public TextureRegion getTextureImage() {
		return textureImage;
	}

	public void setTextureImage(TextureRegion textureImage) {
		this.textureImage = textureImage;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public EnemyNextAction getNextAction () {
		for (Spell spell: spells) {
			if(spell instanceof AttackSpell) {
				AttackSpell attackSpell = (AttackSpell) spell;
				if(attackSpell.getPowerPointsNeeded() <= currentPowerPoints) {
					return EnemyNextAction.MagicAttack;
				}
			}
		}

		return EnemyNextAction.Attack;
	}

	public AttackSpell getAttackSpellToCast () {
		for (Spell spell: spells) {
			if(spell instanceof AttackSpell) {
				AttackSpell attackSpell = (AttackSpell) spell;
				if(attackSpell.getPowerPointsNeeded() <= currentPowerPoints) {
					return attackSpell;
				}
			}
		}

		return null;
	}
}
