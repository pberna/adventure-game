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

import com.pberna.engine.achievements.achievementAction.AchievementAction;
import com.pberna.engine.achievements.achievementAction.AchievementActionManager;
import com.pberna.engine.achievements.achievementAction.IAchievementActionManager;
import com.tests.integration.Constants;
import com.tests.util.DatabaseHelper;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;


public class AchievementActionTests {

    private IAchievementActionManager _achievementActionManager;

    public AchievementActionTests() {
        _achievementActionManager = AchievementActionManager.getInstance();
    }

    @Test
    public void getAchievementActions_getOnlyActionOfTheAchievement() {
        //Arrange
        DatabaseHelper.TruncateTable(Constants.AchievementActionTableName);
        _achievementActionManager.refreshCache();
        _achievementActionManager.add(new AchievementAction("AchievementAction1", 1));
        _achievementActionManager.add(new AchievementAction("AchievementAction2", 1));
        _achievementActionManager.add(new AchievementAction("AchievementAction3", 2));

        //Act
        ArrayList<AchievementAction> actions = _achievementActionManager.getAchievementActions(1);

        //Assert
        Assert.assertEquals(2, actions.size());
        Assert.assertEquals("AchievementAction1", actions.get(0).getActionId());
        Assert.assertEquals(1, actions.get(0).getAchievementId());
        Assert.assertEquals("AchievementAction2", actions.get(1).getActionId());
        Assert.assertEquals(1, actions.get(1).getAchievementId());
    }
}