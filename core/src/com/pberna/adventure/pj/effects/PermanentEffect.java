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

package com.pberna.adventure.pj.effects;

import com.pberna.adventure.pj.Character;


public class PermanentEffect extends Effect {
    private int lifePointsAdjustment;
    private int powerPointsAdjustment;
    private int luckAdjustment;

    public PermanentEffect(int lifePointsAdjustment, int powerPointsAdjustment, int luckAdjustment) {
        this.lifePointsAdjustment = lifePointsAdjustment;
        this.powerPointsAdjustment = powerPointsAdjustment;
        this.luckAdjustment = luckAdjustment;
    }

    @Override
    public void applyTo(Character character) {
        character.setCurrentLifePoints(character.getCurrentLifePoints() + lifePointsAdjustment);
        character.setCurrentPowerPoints(character.getCurrentPowerPoints() + powerPointsAdjustment);
        character.setCurrentLuckPoints(character.getCurrentLuckPoints() + luckAdjustment);
    }

    public int getLifePointsAdjustment() {
        return this.lifePointsAdjustment;
    }

    public int getPowerPointsAdjustment() {
        return this.powerPointsAdjustment;
    }

    public int getLuckAdjustment() {
        return this.luckAdjustment;
    }
}
