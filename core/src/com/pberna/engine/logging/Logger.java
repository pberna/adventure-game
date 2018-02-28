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

package com.pberna.engine.logging;

import com.badlogic.gdx.Gdx;

public class Logger {
    public static final String TagSql = "SQL";
    public static final String TagRendering = "CONT.REND.";
    public static final String TagPay = "PAY";

    private static Logger ourInstance = new Logger();
    private boolean logEnabled;

    public static Logger getInstance() {
        return ourInstance;
    }

    private Logger() {
        logEnabled = false;
    }

    public void addLogInfo(String tag, String message) {
        if(logEnabled) {
            Gdx.app.log(tag, message);
        }
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }
}
