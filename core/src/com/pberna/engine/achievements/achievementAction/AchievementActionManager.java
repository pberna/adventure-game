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

package com.pberna.engine.achievements.achievementAction;

import com.badlogic.gdx.utils.Disposable;
import com.pberna.engine.achievements.achievement.AchievementManager;

import java.util.ArrayList;
import java.util.HashMap;

public class AchievementActionManager implements IAchievementActionManager, Disposable{

    private static AchievementActionManager instance = null;

    public static AchievementActionManager getInstance() {
        if(instance == null) {
            instance = new AchievementActionManager();
        }
        return instance;
    }

    private AchievementActionRepository repository;
    private HashMap<Integer, ArrayList<AchievementAction>> achievementActions;

    private AchievementActionManager() {
        repository = new AchievementActionRepository();
        achievementActions = new HashMap<Integer, ArrayList<AchievementAction>>();
    }

    @Override
    public ArrayList<AchievementAction> getAchievementActions(int achievementId) {
        Integer key = achievementId;
        if(achievementActions.containsKey(key)) {
            return achievementActions.get(key);
        }

        ArrayList<AchievementAction> actions = repository.getAchievementActions(achievementId);
        achievementActions.put(key, actions);

        return actions;
    }

    @Override
    public AchievementAction add(AchievementAction achievementAction) {
        AchievementAction action = repository.add(achievementAction);

        Integer key = achievementAction.getAchievementId();
        if(achievementActions.containsKey(key)) {
            achievementActions.get(key).add(action);
        }

        return action;
    }

    @Override
    public void refreshCache() {
        achievementActions.clear();
    }

    @Override
    public void dispose() {
        instance = null;
    }
}
