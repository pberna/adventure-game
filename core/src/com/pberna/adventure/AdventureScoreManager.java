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

package com.pberna.adventure;

import com.badlogic.gdx.utils.Disposable;
import com.pberna.engine.score.ScoreManager;

public class AdventureScoreManager extends ScoreManager implements Disposable {

    private AdventureScoreManagerEvents listener;
    private static AdventureScoreManager instance = null;

    public void setListener(AdventureScoreManagerEvents listener) {
        this.listener = listener;
    }

    public static AdventureScoreManager getInstance() {
        if(instance == null) {
            instance = new AdventureScoreManager();
        }
        return instance;
    }

    @Override
    public void dispose() {
        instance = null;
    }

    @Override
    protected void checkScoreIncrease(String actionId) {
        int increment = getIncrementScore(actionId);
        increaseScore(increment);
    }

    private int getIncrementScore(String actionId) {
        if(actionId.equals(AchievementsHelper.ExitJail)) {
            return 10;
        }
        if(actionId.startsWith("adventure.succes.")) {
            return 75;
        }
        if(actionId.equals(AchievementsHelper.WinGolem)) {
            return 300;
        }
        if(actionId.equals(AchievementsHelper.WinNoMelee)) {
            return 75;
        }
        if(actionId.equals(AchievementsHelper.WinSkeleton)) {
            return 125;
        }
        if(actionId.equals(AchievementsHelper.WinQuick)) {
            return 75;
        }
        if(actionId.startsWith("adventure.getitem.")) {
            return 75;
        }
        if(actionId.startsWith(AchievementsHelper.CastSpell1Prefix)) {
            return 25;
        }
        return 0;
    }

    @Override
    public void submitScore() {
        if(listener != null) {
            listener.submitScore(getScore());
        }
    }
}
