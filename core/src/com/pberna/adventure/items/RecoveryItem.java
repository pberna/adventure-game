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

import com.pberna.adventure.pj.Character;

public class RecoveryItem extends ItemUsable {

    private int lifePointsRecovery;
    private int powerPointsRecovery;
    private int luckPointsRecovery;

    private static final int MaxRecovery = 100000;

    public int getLifePointsRecovery() {
        return lifePointsRecovery;
    }

    public void setLifePointsRecovery(int lifePoinsRecovery) {
        this.lifePointsRecovery = lifePoinsRecovery;
    }

    public void setLifePointsRecoveryFull() {
        this.lifePointsRecovery = MaxRecovery;
    }

    public int getPowerPointsRecovery() {
        return powerPointsRecovery;
    }

    public void setPowerPointsRecovery(int powerPointsRecovery) {
        this.powerPointsRecovery = powerPointsRecovery;
    }

    public void setPowerPointsRecoveryFull() {
        this.powerPointsRecovery = MaxRecovery;
    }

    public int getLuckPointsRecovery() {
        return luckPointsRecovery;
    }

    public void setLuckPointsRecovery(int luckPointsRecovery) {
        this.luckPointsRecovery = luckPointsRecovery;
    }

    public void setLuckPointsRecoveryFull() {
        this.luckPointsRecovery = MaxRecovery;
    }

    @Override
    public void useItem(Character character) {
        if(lifePointsRecovery != 0) {
            character.setCurrentLifePoints(character.getCurrentLifePoints() +  lifePointsRecovery);
        }
        if(powerPointsRecovery != 0) {
            character.setCurrentPowerPoints(character.getCurrentPowerPoints() + powerPointsRecovery);
        }
        if(luckPointsRecovery != 0) {
            character.setCurrentLuckPoints(character.getCurrentLuckPoints() + luckPointsRecovery);
        }
    }
}
