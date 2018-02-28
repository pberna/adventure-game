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

package com.pberna.engine.persistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Database implements Disposable{
    protected String databaseName = "database";
    protected int databaseVersion = 1;
    private static Database instance = null;
    private boolean executeOnCreate = false;
    private boolean executeOnUpdate = false;
    private int oldDatabaseVersion = 1;

    public static Database getInstance (){
        return instance;
    }

    public static void setInstance(Database newInstance) {
        instance = newInstance;
    }

    protected void setExecuteOnCreate(boolean executeOnCreate) {
        this.executeOnCreate = executeOnCreate;
    }

    protected void setExecuteOnUpdate(boolean executeOnUpdate, int oldDatabaseVersion) {
        this.executeOnUpdate = executeOnUpdate;
        this.oldDatabaseVersion = oldDatabaseVersion;
    }

    public abstract void execute(String sql);
    public abstract int executeUpdate(String sql);
    public abstract Result query(String sql);

    public int getLastId() {
        Result result = query("SELECT last_insert_rowid() AS LAST_ID");
        if(!result.isEmpty()) {
            result.moveToNext();
            return result.getInt(result.getColumnIndex("LAST_ID"));
        }
        return 0;
    }

    class SqlScript {
        public String filePath;
        public int version;

        public SqlScript(String filePath, int version) {
            this.filePath = filePath;
            this.version = version;
        }
    }

    public void onCreate() {
        if(executeOnCreate) {
            ArrayList<SqlScript> scripts = loadScriptsList("data/scripts.xml");

            for (SqlScript script : scripts) {
                executeSqlScript(script);
            }
        }
    }

    public void onUpgrade(){
        if(executeOnUpdate) {
            ArrayList<SqlScript> scripts = loadScriptsList("data/scripts.xml");

            for (SqlScript script : scripts) {
                if (script.version > oldDatabaseVersion) {
                    executeSqlScript(script);
                }
            }
        }
    }

    private ArrayList<SqlScript> loadScriptsList(String xmlFilePath) {
        XmlReader xmlReader = new XmlReader();
        ArrayList<SqlScript> scriptsList = new ArrayList<SqlScript>(databaseVersion);

        try
        {
            XmlReader.Element root = xmlReader.parse(Gdx.files.internal(xmlFilePath));
            if(root.getName().equals("scripts"))
            {
                scriptsList = loadScriptChildrenNodes(root);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return scriptsList;
    }

    private ArrayList<SqlScript> loadScriptChildrenNodes(XmlReader.Element node) {
        ArrayList<SqlScript> scriptsList = new ArrayList<SqlScript>(databaseVersion);
        for (int i = 0; i < node.getChildCount(); i++) {
            try {
                XmlReader.Element childNode = node.getChild(i);

                if (childNode.getName().equals("script")) {
                    String filePath = childNode.getText();
                    int version = Integer.parseInt(childNode.getAttribute("version", "1"));
                    scriptsList.add(new SqlScript(filePath, version));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return scriptsList;
    }

    private void executeSqlScript(SqlScript script) {
        FileHandle file = Gdx.files.internal(script.filePath);
        String textContent = file.readString();

        for(String sentenceText: textContent.split(";")) {
            if(!sentenceText.equals("") && sentenceText.length() >= 2) {
                execute(sentenceText);
            }
        }
    }

    @Override
    public void dispose() {
        instance = null;
    }
}
