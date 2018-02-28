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

package com.pberna.engine.score;

import com.pberna.engine.achievements.playerAction.IPlayerActionManager;
import com.pberna.engine.achievements.playerAction.PlayerAction;
import com.pberna.engine.achievements.playerAction.PlayerActionEvents;
import com.pberna.engine.achievements.playerAction.PlayerActionManager;

public abstract class ScoreManager {
    private int score;
    private IPlayerActionManager playerActionManager;

    public ScoreManager() {
        score = 0;
        playerActionManager = PlayerActionManager.getInstance();
        playerActionManager.addListener(new PlayerActionEvents() {
            @Override
            public void newPlayerActionRegistered(PlayerAction playerAction) {
                if(playerAction != null) {
                    checkScoreIncrease(playerAction.getActionId());
                }
            }

            @Override
            public void playerActionDone(PlayerAction playerAction) {
                if(playerAction != null) {
                    checkScoreIncrease(playerAction.getActionId());
                }
            }
        });
    }
    protected abstract void checkScoreIncrease(String actionId);

    public void resetScore() {
        score = 0;
    }

    public void resetScore(int score) {
        this.score = score;
    }

    protected void increaseScore(int increment) {
        score += increment;
    }

    public int getScore() {
        return score;
    }

    public abstract void submitScore();
}
