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

public class EnhanceCombatStatsSpell extends Spell {
	private int attackModifier;
	private int defenseModifier;
	private int initiativeModifier;
	private int magicModifier;
	
	public int getAttackModifier() {
		return attackModifier;
	}
	public EnhanceCombatStatsSpell setAttackModifier(int attackModifier) {
		this.attackModifier = attackModifier;
		return this;
	}
	
	public int getDefenseModifier() {
		return defenseModifier;
	}
	public EnhanceCombatStatsSpell setDefenseModifier(int defenseModifier) {
		this.defenseModifier = defenseModifier;
		return this;
	}
	
	public int getInitiativeModifier() {
		return initiativeModifier;
	}
	public EnhanceCombatStatsSpell setInitiativeModifier(int initiativeModifier) {
		this.initiativeModifier = initiativeModifier;
		return this;
	}

	public int getMagicModifier() {
		return magicModifier;
	}

	public EnhanceCombatStatsSpell setMagicModifier(int magicModifier) {
		this.magicModifier = magicModifier;
		return this;
	}
}
