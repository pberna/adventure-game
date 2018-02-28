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

package com.tests.integration;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pberna.adventure.pj.Enemy;
import com.pberna.adventure.pj.EnemyManager;
import com.pberna.adventure.pj.IEnemyManager;
import com.pberna.engine.assets.AssetRepository;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class EnemyManagerIntegrationTests {

    private static final int InitialNumberOfEnemies = 30;
    private IEnemyManager _enemyManager;
    private AssetRepository _assetRepository;

    public EnemyManagerIntegrationTests() {

        _assetRepository = AssetRepository.getInstance();

        //System under test
        _enemyManager = EnemyManager.getInstance();
    }

    @Test
    public void getById_AllEnemies_AreWellformed () {

        //Arrange
        ArrayList<Enemy> listEnemies = new ArrayList<>(InitialNumberOfEnemies);

        //Act
        for(int i = 1;; i++) {
            Enemy enemy = _enemyManager.getById(i);
            if(enemy != null) {
                listEnemies.add(enemy);
            } else {
                break;
            }
        }

        //Assert
        for(Enemy enemy: listEnemies) {
            Assert.assertNotNull(enemy);
            Assert.assertNotNull(enemy.getName());
            Assert.assertNotEquals(Constants.NoText, enemy.getName());
            try {
                TextureRegion textureRegion = _assetRepository.getTextureRegion("enemies", enemy.getImageName());
                Assert.assertNotNull(textureRegion);
            } catch (Exception ex) {
                Assert.fail("Could not get image '" + enemy.getImageName() + "' for enemy " + enemy.getName());
            }
        }
    }
}
