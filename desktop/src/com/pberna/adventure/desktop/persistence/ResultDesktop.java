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

import com.pberna.engine.persistence.Result;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultDesktop implements Result {

    private ResultSet res;
    private boolean calledIsEmpty = false;

    public ResultDesktop(ResultSet res) {
        this.res = res;
    }

    @Override
    public boolean isEmpty() {
        try {
            if (res.getRow() == 0) {
                calledIsEmpty = true;
                return !res.next();
            }
            return res.getRow() == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean moveToNext() {
        try {
            if (calledIsEmpty) {
                calledIsEmpty = false;
                return true;
            } else
                return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getColumnIndex(String name) {
        try {
            return res.findColumn(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public float getFloat(int columnIndex) {
        try {
            return res.getFloat(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getString(int columnIndex) {
        try {
            return res.getString(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getInt(int columnIndex) {
        try {
            return res.getInt(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getLong(int columnIndex) {
        try {
            return res.getLong(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
