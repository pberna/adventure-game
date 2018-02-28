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

package com.pberna.adventure.screens.ingame.places;

import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Difficulty;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.pj.effects.Effect;
import com.pberna.adventure.places.CombatPlace;
import com.pberna.adventure.places.ItemUsePlaceToGo;
import com.pberna.adventure.places.SpellUsePlaceToGo;

public interface PlaceViewerEventsListener {
	void checkAttributePressed(Attribute attribute, Difficulty difficulty);
	void checkSkillPressed(Skill skill, Difficulty difficulty);
	void startCombat(CombatPlace combatPlace, boolean runAway);
	void moveToOtherPlace(int idPlace);
	void endAdventure();
	void allocateLuckPoints();
	void effectSuffered(Effect effect);
	void moveToOtherPlace(SpellUsePlaceToGo placeToGo);
	void moveToOtherPlace(ItemUsePlaceToGo placeToGo);
}
