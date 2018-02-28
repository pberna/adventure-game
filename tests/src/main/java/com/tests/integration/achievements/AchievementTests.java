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

package com.tests.integration.achievements;


import com.pberna.engine.achievements.achievement.Achievement;
import com.pberna.engine.achievements.achievement.AchievementManager;
import com.pberna.engine.achievements.achievement.IAchievementManager;
import com.pberna.engine.achievements.achievementAction.AchievementAction;
import com.pberna.engine.achievements.achievementAction.AchievementActionManager;
import com.pberna.engine.achievements.achievementAction.IAchievementActionManager;
import com.pberna.engine.achievements.playerAction.IPlayerActionManager;
import com.pberna.engine.achievements.playerAction.PlayerActionManager;
import com.tests.integration.Constants;
import com.tests.util.DatabaseHelper;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AchievementTests {

    private IAchievementManager achievementManager;
    private IPlayerActionManager playerActionManager;
    private IAchievementActionManager achievementActionManager;

    private static final String ActionPrefix = "AchievementAction";

    public AchievementTests() {
        achievementManager = AchievementManager.getInstance();
        playerActionManager = PlayerActionManager.getInstance();
        achievementActionManager = AchievementActionManager.getInstance();
    }

    private ArrayList<Achievement> achievementAreUnlocked = null;

    @Test
    public void getAchievementsShouldBeUnlocked_AllActionsIncrementalDone_ItShouldBeUnlocked() {
        //Arrange
        final int adventureId = 2;
        final int numActions = 3;
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        DatabaseHelper.TruncateTable(Constants.AchievementActionTableName);
        DatabaseHelper.TruncateTable(Constants.AchievementTableName);
        achievementManager.refreshCache();
        achievementActionManager.refreshCache();
        playerActionManager.refreshCache();

        Achievement achievement = createIncrementalAchievement(numActions, adventureId);
        achievementAreUnlocked = null;

        achievementManager.addListener(unlockedAchievements -> achievementAreUnlocked = unlockedAchievements);

        //Act

        for(int i = 0; i < numActions; i++) {
            playerActionManager.registerAction(ActionPrefix + String.valueOf(i));
        }

        //Assert
        Assert.assertNotNull(achievementAreUnlocked);
        Assert.assertEquals(1, achievementAreUnlocked.size());
        Assert.assertEquals(achievement.getId(), achievementAreUnlocked.get(0).getId());
        Assert.assertEquals(0, achievementManager.getAchievementsShouldBeUnlocked().size());
    }

    private Achievement createIncrementalAchievement(int numActions, int adventureId) {
        return createAchievement(numActions,adventureId, true);
    }

    private Achievement createNotIncrementalAchievement(int numActions, int adventureId) {
       return createAchievement(numActions,adventureId, false);
    }

    private Achievement createAchievement(int numActions, int adventureId, boolean incremental) {
        Achievement achievement = new Achievement();
        achievement.setId(1);
        achievement.setAdventureId(adventureId);
        achievement.setIncremental(incremental);
        achievement.setUnlocked(false);
        achievement.setImageName("");
        achievement.setNameKey("");
        achievement.setDescriptionKey("");
        achievement.setHidden(false);
        achievementManager.add(achievement);

        for(int i = 0; i < numActions; i++) {
            AchievementAction achievementAction = new AchievementAction(
                    ActionPrefix + String.valueOf(i), achievement.getId());
            achievementActionManager.add(achievementAction);
        }
        return achievement;
    }

    @Test
    public void getAchievementsShouldBeUnlocked_NotAllActionsIncrementalDone_ItShouldNotBeUnlocked() {
        //Arrange
        final int adventureId = 2;
        final int numActions = 3;
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        DatabaseHelper.TruncateTable(Constants.AchievementActionTableName);
        DatabaseHelper.TruncateTable(Constants.AchievementTableName);
        achievementManager.refreshCache();
        achievementActionManager.refreshCache();
        playerActionManager.refreshCache();

        createIncrementalAchievement(numActions, adventureId);
        achievementAreUnlocked = null;

        achievementManager.addListener(unlockedAchievements -> achievementAreUnlocked = unlockedAchievements);

        //Act
        for(int i = 0; i < numActions - 1; i++) {
            playerActionManager.registerAction(ActionPrefix + String.valueOf(i));
        }

        //Assert
        Assert.assertNull(achievementAreUnlocked);
        Assert.assertEquals(0, achievementManager.getAchievementsShouldBeUnlocked().size());
    }

    @Test
    public void getAchievementsShouldBeUnlocked_AnyActionNotIncrementalDone_ItShouldBeUnlocked() {
        //Arrange
        final int adventureId = 2;
        final int numActions = 3;
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        DatabaseHelper.TruncateTable(Constants.AchievementActionTableName);
        DatabaseHelper.TruncateTable(Constants.AchievementTableName);
        achievementManager.refreshCache();
        achievementActionManager.refreshCache();
        playerActionManager.refreshCache();

        Achievement achievement = createNotIncrementalAchievement(numActions, adventureId);
        achievementAreUnlocked = null;

        achievementManager.addListener(unlockedAchievements -> achievementAreUnlocked = unlockedAchievements);

        //Act
        playerActionManager.registerAction(ActionPrefix + String.valueOf(0));

        //Assert
        Assert.assertNotNull(achievementAreUnlocked);
        Assert.assertEquals(1, achievementAreUnlocked.size());
        Assert.assertEquals(achievement.getId(), achievementAreUnlocked.get(0).getId());
        Assert.assertEquals(0, achievementManager.getAchievementsShouldBeUnlocked().size());
    }

    @Test
    public void getAchievementsShouldBeUnlocked_NotAnyActionNotIncrementalDone_ItShouldNotBeUnlocked() {
        //Arrange
        final int adventureId = 2;
        final int numActions = 3;
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        DatabaseHelper.TruncateTable(Constants.AchievementActionTableName);
        DatabaseHelper.TruncateTable(Constants.AchievementTableName);
        achievementManager.refreshCache();
        achievementActionManager.refreshCache();
        playerActionManager.refreshCache();

        createNotIncrementalAchievement(numActions, adventureId);
        achievementAreUnlocked = null;

        achievementManager.addListener(unlockedAchievements -> achievementAreUnlocked = unlockedAchievements);

        //Act
        playerActionManager.registerAction(ActionPrefix + String.valueOf(numActions));

        //Assert
        Assert.assertNull(achievementAreUnlocked);
        Assert.assertEquals(0, achievementManager.getAchievementsShouldBeUnlocked().size());
    }
}
