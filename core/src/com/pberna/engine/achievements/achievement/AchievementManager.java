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

package com.pberna.engine.achievements.achievement;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.pberna.engine.achievements.achievementAction.AchievementAction;
import com.pberna.engine.achievements.achievementAction.AchievementActionManager;
import com.pberna.engine.achievements.achievementAction.IAchievementActionManager;
import com.pberna.engine.achievements.playerAction.IPlayerActionManager;
import com.pberna.engine.achievements.playerAction.PlayerAction;
import com.pberna.engine.achievements.playerAction.PlayerActionEvents;
import com.pberna.engine.achievements.playerAction.PlayerActionManager;

import java.util.ArrayList;
import java.util.Date;

public class AchievementManager implements IAchievementManager, Disposable {
    private static AchievementManager instance = null;

    public static AchievementManager getInstance() {
        if(instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    private AchievementRepository repository;
    private IAchievementActionManager achievementActionManager;
    private IPlayerActionManager playerActionManager;
    private ArrayList<AchievementEventListener> listeners;
    private ArrayList<Achievement> achievements;
    private ArrayList<Achievement> lockedAchievements;

    private AchievementManager() {
        repository = new AchievementRepository();
        achievementActionManager = AchievementActionManager.getInstance();
        playerActionManager = PlayerActionManager.getInstance();
        playerActionManager.addListener(new PlayerActionEvents() {
            @Override
            public void newPlayerActionRegistered(PlayerAction playerAction) {
                checkNewAchievementsUnlocked();
            }

            @Override
            public void playerActionDone(PlayerAction playerAction) {

            }
        });
        listeners = new ArrayList<AchievementEventListener>();
        achievements = new ArrayList<Achievement>(repository.findAll());
        lockedAchievements = repository.getAchievements(false);
    }

    @Override
    public ArrayList<Achievement> getAllAchievements() {
        return achievements;
    }

    @Override
    public ArrayList<Achievement> getAllLockedAchievements() {
        return lockedAchievements;
    }

    @Override
    public ArrayList<Achievement> getAchievementsShouldBeUnlocked() {
        ArrayList<Achievement> listShouldBeUnlocked = new ArrayList<Achievement>();

        for (Achievement achievement: getAllLockedAchievements()) {
            ArrayList<AchievementAction> achievementActions = achievementActionManager.
                    getAchievementActions(achievement.getId());
            if(achievement.isIncremental()) {
                if (playerActionManager.areAllActionsRegistered(achievementActions)) {
                    listShouldBeUnlocked.add(achievement);
                }
            } else {
                if (playerActionManager.isAnyActionRegistered(achievementActions)) {
                    listShouldBeUnlocked.add(achievement);
                }
            }
        }

        return listShouldBeUnlocked;
    }

    @Override
    public void unlockAchievements(ArrayList<Achievement> achievements) {
        for(Achievement achievement: achievements) {
            Date date = new Date(TimeUtils.millis());
            repository.changelockAchievement(achievement.getId(), true, date);

            Achievement lockedAchievement = getLockedAchievementFromCache(achievement.getId());
            if(lockedAchievement != null) {
                lockedAchievements.remove(lockedAchievement);
            }

            Achievement achievementFromCache = getAchievementFromCache(achievement.getId());
            achievementFromCache.setUnlocked(true);
            achievementFromCache.setUnlockDate(date);
        }
    }

    private Achievement getLockedAchievementFromCache(int id) {
        for(Achievement lockedAchievement: lockedAchievements) {
            if(lockedAchievement.getId() == id) {
                return lockedAchievement;
            }
        }
        return null;
    }

    private Achievement getAchievementFromCache(int id) {
        for(Achievement lockedAchievement: achievements) {
            if(lockedAchievement.getId() == id) {
                return lockedAchievement;
            }
        }
        return null;
    }

    @Override
    public Achievement add(Achievement achievement) {
        Achievement createdAchievement = repository.add(achievement);
        achievements.add(createdAchievement);
        if(!achievement.isUnlocked()) {
            lockedAchievements.add(createdAchievement);
        }

        return createdAchievement;
    }

    @Override
    public void addListener(AchievementEventListener listener) {
        listeners.add(listener);
    }

    private void checkNewAchievementsUnlocked() {
        ArrayList<Achievement> achievementsShouldBeUnlocked = getAchievementsShouldBeUnlocked();

        if(achievementsShouldBeUnlocked.size() > 0) {
            unlockAchievements(achievementsShouldBeUnlocked);
            notifyUnlockedAchievements(achievementsShouldBeUnlocked);
        }
    }

    private void notifyUnlockedAchievements(ArrayList<Achievement> achievementsShouldBeUnlocked) {
        for(AchievementEventListener listener: listeners) {
            listener.achievementsUnlocked(achievementsShouldBeUnlocked);
        }
    }

    @Override
    public void dispose() {
        instance = null;
    }

    @Override
    public void refreshCache() {
        achievements = new ArrayList<Achievement>(repository.findAll());
        lockedAchievements = repository.getAchievements(false);
    }

    @Override
    public int getAchievementProgress(int achivementId) {
        ArrayList<AchievementAction> achievementActions = achievementActionManager.getAchievementActions(achivementId);
        int completedActions = playerActionManager.getPlayerActionsRegistered(achievementActions);

        return Math.round((float)completedActions / (float)achievementActions.size() * 100f);
    }

    @Override
    public ArrayList<Achievement> getAchievementsShouldBeIncrementedWhenRegisteringActions(String actionId){
        ArrayList<Achievement> achievements = new ArrayList<Achievement>();

        for (Achievement achievement: getAllLockedAchievements()) {
            if (achievement.isIncremental()) {
                ArrayList<AchievementAction> achievementActions = achievementActionManager.
                        getAchievementActions(achievement.getId());
                for (AchievementAction achievementAction : achievementActions) {
                    if (achievementAction.getActionId().equals(actionId)) {
                        achievements.add(achievement);
                        break;
                    }
                }
            }
        }
        return achievements;
    }
}
