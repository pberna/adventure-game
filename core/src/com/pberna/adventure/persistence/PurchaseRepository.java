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

import com.pberna.adventure.store.Purchase;
import com.pberna.engine.logging.Logger;
import com.pberna.engine.persistence.Database;
import com.pberna.engine.persistence.IRepository;
import com.pberna.engine.persistence.PersistenceHelper;
import com.pberna.engine.persistence.Result;

import java.util.Collection;

public class PurchaseRepository implements IRepository<Purchase> {

    public PurchaseRepository() {
    }

    @Override
    public Collection<Purchase> findAll() {
        String query = "SELECT * FROM '" + Purchase.TableName + "' " +
                " ORDER BY '" + Purchase.DateColumnName + "' DESC";
        Result queryResult = Database.getInstance().query(query);

        return PersistenceHelper.fillEntitiesList(queryResult, Purchase.class);
    }

    @Override
    public Purchase add(Purchase entity) {
        String insertQuery = PersistenceHelper.getInsertStatement(entity, Purchase.class);

        Logger.getInstance().addLogInfo(Logger.TagSql, "Insert: " + insertQuery);
        Database.getInstance().execute(insertQuery);

        return entity;
    }

    @Override
    public void delete(Purchase entity) {

    }

    @Override
    public void update(Purchase entity) {

    }

    @Override
    public Purchase findById(int id) {
        return null;
    }
}
