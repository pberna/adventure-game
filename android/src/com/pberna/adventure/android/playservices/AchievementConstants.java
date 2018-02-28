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

package com.pberna.adventure.android.playservices;

public class AchievementConstants {

    public static String getGooglePlayAchievementId(int achivementId) {
        switch (achivementId) {
            case 1:
                return "AchiveId1";
            case 2:
                return "AchiveId2";
            case 3:
                return "AchiveId3";
            case 4:
                return "AchiveId4";
            case 5:
                return "AchiveId5";
            case 6:
                return "AchiveId6";
            case 7:
                return "AchiveId7";
            case 8:
                return "AchiveId8";
            case 9:
                return "AchiveId9";
            case 10:
                return "AchiveId10";
            case 11:
                return "AchiveId11";
            case 12:
                return "AchiveId12";
            case 13:
                return "AchiveId13";
            case 14:
                return "AchiveId14";
        }

        return "";
    }
}
