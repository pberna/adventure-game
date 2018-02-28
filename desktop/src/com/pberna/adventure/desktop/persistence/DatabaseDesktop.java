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

package com.pberna.adventure.desktop.persistence;

import com.pberna.adventure.persistence.AdventureDatabase;
import com.pberna.engine.persistence.Result;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseDesktop extends AdventureDatabase {
    protected Connection db_connection;
    protected Statement stmt;
    protected boolean noDatabase = false;

    public DatabaseDesktop(String databaseName) {
        super();
        this.databaseName = databaseName;
        initDatabase();
    }

    public DatabaseDesktop() {
        super();
        initDatabase();
    }

    private void initDatabase() {
        loadDatabase();
        if (isNewDatabase()) {
            setExecuteOnCreate(true);
            upgradeVersion();
        } else if (isVersionDifferent()) {
            setExecuteOnUpdate(true, getCurrentDatabaseVersion());
            upgradeVersion();
        }
    }

    public void execute(String sql) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int executeUpdate(String sql) {
        try {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Result query(String sql) {
        try {
            return new ResultDesktop(stmt.executeQuery(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadDatabase() {
        File file = new File(databaseName + ".db");
        if (!file.exists())
            noDatabase = true;
        try {
            Class.forName("org.sqlite.JDBC");
            db_connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName + ".db");
            stmt = db_connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void upgradeVersion() {
        execute("PRAGMA user_version=" + databaseVersion);
    }

    private boolean isNewDatabase() {
        return noDatabase;
    }

    private boolean isVersionDifferent() {
        Result q = query("PRAGMA user_version");
        return q.isEmpty() || (q.getInt(1) != databaseVersion);
    }

    private int getCurrentDatabaseVersion() {
        Result q = query("PRAGMA user_version");
        return q.isEmpty() ? 0 : q.getInt(1);
    }

    @Override
    public void dispose() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (db_connection != null) {
                db_connection.close();
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        super.dispose();
    }
}
