/*
 *   Adventure Game, a digital gamebook written in java with Libgdx.
 *   Copyright (C) 2018 Pedro Berná
 *
 *   This code is based on the code from TheInvader360:
 *   https://github.com/TheInvader360/libgdx-gameservices-tutorial/
 */

package com.pberna.adventure.android.playservices;

import com.pberna.adventure.controllers.ITransitionCallback;

public interface ActionResolver {
    boolean getSignedInGPGS();
    void loginGPGS();
    void unlockAchievementGPGS(String achievementId);
    void getAchievementsGPGS();
    void incrementAchievementProgress(String achievementId, int increment);
    void showOrLoadInterstital(ITransitionCallback callback);
    void submitScoreGPGS(int score);
    void getLeaderboardGPGS();
}
