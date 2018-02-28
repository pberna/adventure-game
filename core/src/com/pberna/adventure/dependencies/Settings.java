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

package com.pberna.adventure.dependencies;

import java.util.ArrayList;

public class Settings {
    private boolean storeAvailable;
    private static Settings instance;
    private ArrayList<StoreItemConfig> storeItemConfigs;
    private IStoreQuerier storeQuerier;
    private boolean gplayAchievementsAvailable;
    private String versionName;
    private boolean leadeboardAvailable;

    public Settings() {
        storeAvailable = true;
        storeItemConfigs = new ArrayList<StoreItemConfig>();
        gplayAchievementsAvailable = false;
        versionName = "1.0";
        leadeboardAvailable = false;
    }

    public static Settings getInstance() {
        return instance;
    }

    public static void setInstance(Settings instance) {
        Settings.instance = instance;
    }

    public boolean isStoreAvailable() {
        return storeAvailable;
    }

    public void setStoreAvailable(boolean showStore) {
        this.storeAvailable = showStore;
    }

    public ArrayList<StoreItemConfig> getStoreItemConfigs() {
        return storeItemConfigs;
    }

    public void setStoreItemConfigs(ArrayList<StoreItemConfig> storeItemConfigs) {
        this.storeItemConfigs = storeItemConfigs;
    }

    public IStoreQuerier getStoreQuerier() {
        return storeQuerier;
    }

    public void setStoreQuerier(IStoreQuerier storeQuerier) {
        this.storeQuerier = storeQuerier;
    }

    public boolean isGplayAchievementsAvailable() {
        return gplayAchievementsAvailable;
    }

    public void setGplayAchievementsAvailable(boolean gplayAchievementsAvailable) {
        this.gplayAchievementsAvailable = gplayAchievementsAvailable;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isLeadeboardAvailable() {
        return leadeboardAvailable;
    }

    public void setLeadeboardAvailable(boolean leadeboardAvailable) {
        this.leadeboardAvailable = leadeboardAvailable;
    }
}
