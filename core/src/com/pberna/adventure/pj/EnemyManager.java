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

import com.pberna.engine.localization.Localization;

public class EnemyManager implements IEnemyManager {
    private static IEnemyManager instance = new EnemyManager();

    private EnemyManager() {

    }

    public static IEnemyManager getInstance() {
        return instance;
    }

    @Override
    public Enemy getById(int id) {
        switch(id) {
            case 1:
                return getSkeleton();
            case 2:
                return getGolem();
        }

        return null;
    }

    private static Enemy getSkeleton() {
        Enemy enemy = new Enemy();
        enemy.setId(1);
        enemy.setName(Localization.getInstance().getTranslation("Enemies", "skeletonName"));
        enemy.setImageName("skeleton");
        enemy.setAttackValue(5);
        enemy.setDefenseValue(4);
        enemy.setInitiativeValue(6);
        enemy.setMagicValue(1);
        enemy.setMaximumLifePoints(12);
        enemy.setCurrentLifePoints(12);
        enemy.setMaximumPowerPoints(0);
        enemy.setCurrentPowerPoints(0);
        enemy.setCurrentLuckPoints(0);
        enemy.setMaximumLuckPoints(0);

        return enemy;
    }

    private static Enemy getGolem() {
        Enemy enemy = new Enemy();
        enemy.setId(2);
        enemy.setName(Localization.getInstance().getTranslation("Enemies", "golemName"));
        enemy.setImageName("golem");
        enemy.setAttackValue(8);
        enemy.setDefenseValue(8);
        enemy.setInitiativeValue(8);
        enemy.setMagicValue(5);
        enemy.setMaximumLifePoints(35);
        enemy.setCurrentLifePoints(35);
        enemy.setMaximumPowerPoints(0);
        enemy.setCurrentPowerPoints(0);
        enemy.setCurrentLuckPoints(0);
        enemy.setMaximumLuckPoints(0);

        return enemy;
    }
}
