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

package com.pberna.engine.achievements.playerAction;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.pberna.engine.achievements.achievementAction.AchievementAction;
import com.pberna.engine.achievements.Constants;
import com.pberna.engine.logging.Logger;

import java.util.ArrayList;
import java.util.Date;

public class PlayerActionManager implements IPlayerActionManager, Disposable {

    private static PlayerActionManager instance = null;

    private PlayerActionRepository repository;
    private ArrayList<PlayerActionEvents> listeners;
    private ArrayList<PlayerAction> playerActions;

    private PlayerActionManager() {
        repository = new PlayerActionRepository();
        listeners = new ArrayList<PlayerActionEvents>();
        playerActions = new ArrayList<PlayerAction>(repository.findAll());
    }

    public static PlayerActionManager getInstance() {
        if(instance == null) {
            instance = new PlayerActionManager();
        }
        return instance;
    }

    @Override
    public boolean registerAction(String actionId) {
        try {
            PlayerAction playerAction = new PlayerAction();
            playerAction.setActionId(actionId);
            playerAction.setCreationDate(new Date(TimeUtils.millis()));

            if(isActionInList(actionId)) {
                notifyPlayerActionDone(playerAction);
                return false;
            }

            repository.add(playerAction);
            playerActions.add(playerAction);

            notifyNewPlayerActionRegister(playerAction);

            return true;
        } catch (Exception ex) {
            Logger.getInstance().addLogInfo(Constants.TagAchievement, ex.getMessage());
            return false;
        }
    }

    private void notifyPlayerActionDone(PlayerAction playerAction) {
        for(PlayerActionEvents listener: listeners) {
            listener.playerActionDone(playerAction);
        }
    }

    private boolean isActionInList(String actionId) {
        for(PlayerAction playerAction: playerActions) {
            if(playerAction.getActionId().equals(actionId)) {
                return true;
            }
        }
        return false;
    }

    private void notifyNewPlayerActionRegister(PlayerAction playerAction) {
        for(PlayerActionEvents listener: listeners) {
            listener.newPlayerActionRegistered(playerAction);
        }
    }

    @Override
    public boolean areAllActionsRegistered(ArrayList<AchievementAction> actions) {
        try {
            for(AchievementAction achievementAction: actions) {
                boolean isRegistered = false;
                for(PlayerAction registeredAction: playerActions) {
                    if(registeredAction.getActionId().equals(achievementAction.getActionId())) {
                        isRegistered = true;
                        break;
                    }
                }
                if(!isRegistered) {
                    return false;
                }
            }
            return true;

        } catch (Exception ex) {
            Logger.getInstance().addLogInfo(Constants.TagAchievement, ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean isAnyActionRegistered(ArrayList<AchievementAction> actions) {
        try {
            for(AchievementAction achievementAction: actions) {
                for(PlayerAction registeredAction: playerActions) {
                    if(registeredAction.getActionId().equals(achievementAction.getActionId())) {
                        return true;
                    }
                }
            }
            return false;

        } catch (Exception ex) {
            Logger.getInstance().addLogInfo(Constants.TagAchievement, ex.getMessage());
            return false;
        }
    }

    @Override
    public void addListener(PlayerActionEvents listener) {
        listeners.add(listener);
    }

    @Override
    public void refreshCache() {
        playerActions = new ArrayList<PlayerAction>(repository.findAll());
    }

    @Override
    public int getPlayerActionsRegistered(ArrayList<AchievementAction> achievementActions) {
        int count = 0;
        try {
            for(AchievementAction achievementAction: achievementActions) {
                for(PlayerAction registeredAction: playerActions) {
                    if(registeredAction.getActionId().equals(achievementAction.getActionId())) {
                        count++;
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getInstance().addLogInfo(Constants.TagAchievement, ex.getMessage());
        }
        return count;
    }

    @Override
    public void dispose() {
        instance = null;
    }
}
