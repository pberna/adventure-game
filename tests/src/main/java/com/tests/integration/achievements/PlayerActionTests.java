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
import com.pberna.engine.achievements.playerAction.IPlayerActionManager;
import com.pberna.engine.achievements.playerAction.PlayerActionManager;
import com.tests.integration.Constants;
import com.tests.util.DatabaseHelper;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class PlayerActionTests {

    private IPlayerActionManager _playerActionManager;

    public PlayerActionTests() {
        _playerActionManager = PlayerActionManager.getInstance();
    }

    @Test
    public void registerAction_registerActionsFirstTime_ReturnsTrue () {
        //Arrange
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        _playerActionManager.refreshCache();
        String actionId ="TestAction1";

        //Act
        boolean isRegistered = _playerActionManager.registerAction(actionId);

        //Assert
        Assert.assertEquals(true, isRegistered);
    }

    @Test
    public void registerAction_registerActionsSecondTime_ReturnsFalse () {
        //Arrange
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        _playerActionManager.refreshCache();
        String actionId ="TestAction1";

        //Act
        boolean isRegisteredFirst = _playerActionManager.registerAction(actionId);
        boolean isRegisteredSecond = _playerActionManager.registerAction(actionId);

        //Assert
        Assert.assertEquals(true, isRegisteredFirst);
        Assert.assertEquals(false, isRegisteredSecond);
    }

    @Test
    public void areAllActionsRegistered_WhenAllActionAreRegistered_Returns_True () {
        //Arrange
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        _playerActionManager.refreshCache();
        _playerActionManager.registerAction("TestAction1");
        _playerActionManager.registerAction("TestAction2");

        ArrayList<AchievementAction> listActions = new ArrayList<>();
        listActions.add(new AchievementAction("TestAction1"));
        listActions.add(new AchievementAction("TestAction2"));

        //Act
        boolean areAllActionsRegistered = _playerActionManager.areAllActionsRegistered(listActions);

        //Assert
        Assert.assertEquals(true, areAllActionsRegistered);
    }

    @Test
    public void areAllActionsRegistered_WhenNptAllActionAreRegistered_Returns_False () {
        //Arrange
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        _playerActionManager.refreshCache();
        _playerActionManager.registerAction("TestAction1");
        _playerActionManager.registerAction("TestAction2");

        ArrayList<AchievementAction> listActions = new ArrayList<>();
        listActions.add(new AchievementAction("TestAction1"));
        listActions.add(new AchievementAction("TestAction2"));
        listActions.add(new AchievementAction("TestAction3"));

        //Act
        boolean areAllActionsRegistered = _playerActionManager.areAllActionsRegistered(listActions);

        //Assert
        Assert.assertEquals(false, areAllActionsRegistered);
    }

    @Test
    public void isAnyActionRegistered_WhenAnyActionIsRegistered_Returns_True () {
        //Arrange
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        _playerActionManager.refreshCache();
        _playerActionManager.registerAction("TestAction1");

        ArrayList<AchievementAction> listActions = new ArrayList<>();
        listActions.add(new AchievementAction("TestAction1"));
        listActions.add(new AchievementAction("TestAction2"));

        //Act
        boolean isAnyActionRegistered = _playerActionManager.isAnyActionRegistered(listActions);

        //Assert
        Assert.assertEquals(true, isAnyActionRegistered);
    }

    @Test
    public void isAnyActionRegistered_WhenNotAnyActionIsRegistered_Returns_False () {
        //Arrange
        DatabaseHelper.TruncateTable(Constants.PlayerActionTableName);
        _playerActionManager.refreshCache();
        _playerActionManager.registerAction("TestAction3");

        ArrayList<AchievementAction> listActions = new ArrayList<>();
        listActions.add(new AchievementAction("TestAction1"));
        listActions.add(new AchievementAction("TestAction2"));

        //Act
        boolean isAnyActionRegistered = _playerActionManager.isAnyActionRegistered(listActions);

        //Assert
        Assert.assertEquals(false, isAnyActionRegistered);
    }
}
