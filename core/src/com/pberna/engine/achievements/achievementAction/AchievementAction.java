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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "AchievementAction")
public class AchievementAction {

    public static final String TableName = "AchievementAction";
    public static final String IdColumnName = "Id";
    public static final String ActionIdColumnName = "ActionId";
    public static final String AchievementIdColumnName = "AchievementId";

    @DatabaseField(generatedId = true, columnName = IdColumnName)
    private int id;
    @DatabaseField(columnName = ActionIdColumnName)
    private String actionId;
    @DatabaseField(columnName = AchievementIdColumnName)
    private int achievementId;

    public AchievementAction() {

    }

    public AchievementAction(String actionId) {
        this.actionId = actionId;
    }

    public AchievementAction(String actionId, int achievementId) {
        this.actionId = actionId;
        this.achievementId = achievementId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
