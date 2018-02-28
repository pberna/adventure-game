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

import com.pberna.engine.logging.Logger;
import com.pberna.engine.persistence.Database;
import com.pberna.engine.persistence.IRepository;
import com.pberna.engine.persistence.PersistenceHelper;
import com.pberna.engine.persistence.Result;

import java.util.ArrayList;
import java.util.Collection;

class AchievementActionRepository implements IRepository<AchievementAction> {

    AchievementActionRepository() {

    }

    @Override
    public Collection<AchievementAction> findAll() {
        String query = "SELECT * FROM '" + AchievementAction.TableName + "'";
        Result queryResult = Database.getInstance().query(query);

        return PersistenceHelper.fillEntitiesList(queryResult, AchievementAction.class);
    }

    @Override
    public AchievementAction add(AchievementAction entity) {
        String insertQuery = PersistenceHelper.getInsertStatement(entity, AchievementAction.class);

        Logger.getInstance().addLogInfo(Logger.TagSql, "Insert: " + insertQuery);
        Database.getInstance().execute(insertQuery);

        return entity;
    }

    @Override
    public void delete(AchievementAction entity) {

    }

    @Override
    public void update(AchievementAction entity) {

    }

    @Override
    public AchievementAction findById(int id) {
        return null;
    }

    public ArrayList<AchievementAction> getAchievementActions(int achievementId) {
        String query = "SELECT * FROM '" + AchievementAction.TableName + "' WHERE " +
                AchievementAction.AchievementIdColumnName + " = " + String.valueOf(achievementId);
        Result queryResult = Database.getInstance().query(query);

        return PersistenceHelper.fillEntitiesList(queryResult, AchievementAction.class);
    }
}
