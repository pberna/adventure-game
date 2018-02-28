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

package com.pberna.engine.achievements.playerAction;


import com.pberna.engine.logging.Logger;
import com.pberna.engine.persistence.Database;
import com.pberna.engine.persistence.IRepository;
import com.pberna.engine.persistence.PersistenceHelper;
import com.pberna.engine.persistence.Result;

import java.util.ArrayList;
import java.util.Collection;

class PlayerActionRepository implements IRepository<PlayerAction> {

    PlayerActionRepository() {
    }

    @Override
    public Collection<PlayerAction> findAll() {
        String query = "SELECT * FROM '" + PlayerAction.TableName + "'";
        Result queryResult = Database.getInstance().query(query);

        return PersistenceHelper.fillEntitiesList(queryResult, PlayerAction.class);
    }

    @Override
    public PlayerAction add(PlayerAction entity) {
        String insertQuery = PersistenceHelper.getInsertStatement(entity, PlayerAction.class);

        Logger.getInstance().addLogInfo(Logger.TagSql, "Insert: " + insertQuery);
        Database.getInstance().execute(insertQuery);
        entity.setId(Database.getInstance().getLastId());

        return entity;
    }

    @Override
    public void delete(PlayerAction entity) {

    }

    @Override
    public void update(PlayerAction entity) {

    }

    @Override
    public PlayerAction findById(int id) {
        String query = "SELECT * FROM '" + PlayerAction.TableName + "' WHERE " +
                PlayerAction.IdColumnName + " = " + String.valueOf(id);
        Result queryResult = Database.getInstance().query(query);

        ArrayList<PlayerAction> playerActions = PersistenceHelper.fillEntitiesList(queryResult, PlayerAction.class);
        return playerActions.size() > 0 ? playerActions.get(0) : null;
    }
}
