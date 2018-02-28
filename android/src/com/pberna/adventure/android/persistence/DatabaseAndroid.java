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


package com.pberna.adventure.android.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.pberna.adventure.persistence.AdventureDatabase;
import com.pberna.engine.persistence.Result;

public class DatabaseAndroid extends AdventureDatabase {
    protected SQLiteOpenHelper db_connection;
    protected SQLiteDatabase stmt;

    public DatabaseAndroid(Context context) {
        super();
        db_connection = new AndroidDB(context, databaseName, null, databaseVersion);
        stmt = db_connection.getWritableDatabase();
    }

    @Override
    public void execute(String sql){
        stmt.execSQL(sql);
    }

    @Override
    public int executeUpdate(String sql){
        stmt.execSQL(sql);
        SQLiteStatement tmp = stmt.compileStatement("SELECT CHANGES()");
        return (int) tmp.simpleQueryForLong();
    }

    @Override
    public Result query(String sql) {
        return new ResultAndroid(stmt.rawQuery(sql, null));
    }

    @Override
    public void dispose() {
        if(stmt != null) {
            stmt.close();
        }

        if(db_connection != null) {
            db_connection.close();
        }
        super.dispose();
    }

    class AndroidDB extends SQLiteOpenHelper {

        public AndroidDB(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            stmt = db;
            DatabaseAndroid.this.setExecuteOnCreate(true);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            stmt = db;
            DatabaseAndroid.this.setExecuteOnUpdate(true, oldVersion);
        }

    }

}
