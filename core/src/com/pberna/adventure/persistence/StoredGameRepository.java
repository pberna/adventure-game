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

package com.pberna.adventure.persistence;

import com.pberna.adventure.games.StoredGame;
import com.pberna.engine.logging.Logger;
import com.pberna.engine.persistence.Database;
import com.pberna.engine.persistence.IRepository;
import com.pberna.engine.persistence.PersistenceHelper;
import com.pberna.engine.persistence.Result;

import java.util.ArrayList;
import java.util.Collection;

public class StoredGameRepository implements IRepository<StoredGame> {

    public StoredGameRepository() {

    }

    @Override
    public Collection<StoredGame> findAll() {
        String query = "SELECT * FROM '" + StoredGame.TableName + "' ORDER BY " +
                StoredGame.CreationDataColumnName + " DESC";
        Result queryResult = Database.getInstance().query(query);

        return PersistenceHelper.fillEntitiesList(queryResult, StoredGame.class);
    }

    @Override
    public StoredGame add(StoredGame entity) {
        String insertQuery = PersistenceHelper.getInsertStatement(entity, StoredGame.class);
        Logger.getInstance().addLogInfo(Logger.TagSql, "Insert: " + insertQuery);

        Database.getInstance().execute(insertQuery);
        entity.setId(Database.getInstance().getLastId());

        return entity;
    }

    @Override
    public void delete(StoredGame entity) {
        String deleteQuery = "DELETE FROM '" + StoredGame.TableName + "' " +
                "WHERE " + StoredGame.IdColumnName + " = " + String.valueOf(entity.getId());
        Database.getInstance().execute(deleteQuery);
    }

    @Override
    public void update(StoredGame entity) {

    }

    @Override
    public StoredGame findById(int id) {
        String query = "SELECT * FROM '" + StoredGame.TableName + "' WHERE " +
                StoredGame.IdColumnName + " = " + String.valueOf(id);
        Result queryResult = Database.getInstance().query(query);

        ArrayList<StoredGame> storedGames = PersistenceHelper.fillEntitiesList(queryResult, StoredGame.class);
        return storedGames.size() > 0 ? storedGames.get(0) : null;

    }
}
