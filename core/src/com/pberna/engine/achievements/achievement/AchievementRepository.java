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

import com.pberna.engine.logging.Logger;
import com.pberna.engine.persistence.Database;
import com.pberna.engine.persistence.IRepository;
import com.pberna.engine.persistence.PersistenceHelper;
import com.pberna.engine.persistence.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

class AchievementRepository implements IRepository<Achievement>{

    AchievementRepository() {

    }

    @Override
    public Collection<Achievement> findAll() {
        String query = "SELECT * FROM '" + Achievement.TableName + "'";
        Result queryResult = Database.getInstance().query(query);

        return PersistenceHelper.fillEntitiesList(queryResult, Achievement.class);
    }

    @Override
    public Achievement add(Achievement entity) {
        String insertQuery = PersistenceHelper.getInsertStatement(entity, Achievement.class);

        Logger.getInstance().addLogInfo(Logger.TagSql, "Insert: " + insertQuery);
        Database.getInstance().execute(insertQuery);

        return entity;
    }

    @Override
    public void delete(Achievement entity) {

    }

    @Override
    public void update(Achievement entity) {

    }

    @Override
    public Achievement findById(int id) {
        String query = "SELECT * FROM '" + Achievement.TableName + "' WHERE " +
                Achievement.IdColumnName + " = " + String.valueOf(id);
        Result queryResult = Database.getInstance().query(query);

        ArrayList<Achievement> achievements = PersistenceHelper.fillEntitiesList(queryResult, Achievement.class);
        return achievements.size() > 0 ? achievements.get(0) : null;
    }

    public ArrayList<Achievement> getAchievements(boolean unlocked) {
        String query = "SELECT * FROM '" + Achievement.TableName + "' WHERE " +
                Achievement.UnlockedColumnName + " = " + String.valueOf(unlocked ? 1 : 0);

        Result queryResult = Database.getInstance().query(query);
        return PersistenceHelper.fillEntitiesList(queryResult, Achievement.class);
    }

    public void changelockAchievement(int achievementId, boolean unlocked, Date unlockDate) {
        String updateQuery = "UPDATE '" + Achievement.TableName + "' SET " +
                Achievement.UnlockedColumnName + " = " + String.valueOf(unlocked ? 1 : 0) +
                ", " + Achievement.UnlockDateColumnName + " = " +
                String.valueOf(unlockDate.getTime()) + " WHERE " + Achievement.IdColumnName +
                " = " + String.valueOf(achievementId);

        Database.getInstance().executeUpdate(updateQuery);
    }
}
