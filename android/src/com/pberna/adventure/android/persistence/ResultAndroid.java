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

import android.database.Cursor;

import com.pberna.engine.persistence.Result;

public class ResultAndroid implements Result {
    Cursor cursor;

    public ResultAndroid(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public boolean isEmpty() {
        return cursor.getCount() == 0;
    }

    @Override
    public boolean moveToNext() {
        return cursor.moveToNext();
    }

    @Override
    public int getColumnIndex(String name) {
        return cursor.getColumnIndex(name);
    }

    @Override
    public float getFloat(int columnIndex) {
        return cursor.getFloat(columnIndex);
    }

    @Override
    public String getString(int columnIndex) {
        return cursor.getString(columnIndex);
    }

    @Override
    public int getInt(int columnIndex) {
        return cursor.getInt(columnIndex);
    }

    @Override
    public long getLong(int columnIndex) {
        return cursor.getLong(columnIndex);
    }
}
