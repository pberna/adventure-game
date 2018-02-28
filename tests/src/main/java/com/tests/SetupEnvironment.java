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

package com.tests;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Disposable;
import com.pberna.adventure.dependencies.DependenciesContainer;
import com.pberna.adventure.dependencies.Settings;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.persistence.Database;
import com.pberna.engine.utils2D.graphics.PixmapHelper;

public class SetupEnvironment extends Game {

    public SetupEnvironment(DependenciesContainer dependenciesContainer) {
        Database.setInstance(dependenciesContainer.getDatabase());
        Settings.setInstance(dependenciesContainer.getSettings());
    }

    public void create () {
        AudioManager.getInstance().loadSettings();
        Localization.getInstance().loadSettings();

        if(Database.getInstance() != null) {
            Database.getInstance().onCreate();
            Database.getInstance().onUpgrade();
        }


        Localization.getInstance().readLocalizationDefinitionFromXml("data/i18N.xml");
        Localization.getInstance().loadAllBundles();

        AssetRepository.getInstance().readAssetsDefinitionFromXml("data/assets.xml");
        AssetRepository.getInstance().loadAssetsSynchronized();
    }

    @Override
    public void dispose() {
        super.dispose();

        disposeIfNotNull(AssetRepository.getInstance());
        disposeIfNotNull(Localization.getInstance());
        disposeIfNotNull(PixmapHelper.getInstance());
        disposeIfNotNull(Database.getInstance());
    }

    private void disposeIfNotNull(Disposable disposable) {
        if(disposable != null) {
            disposable.dispose();
        }
    }
}
