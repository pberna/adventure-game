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

package com.pberna.adventure.games;

import com.badlogic.gdx.utils.TimeUtils;
import com.pberna.adventure.adventure.Adventure;
import com.pberna.adventure.persistence.StoredGameRepository;
import com.pberna.adventure.pj.Character;

import java.util.Collection;
import java.util.Date;

public class StoredGameManager implements IStoredGameManager{
    private static StoredGameManager ourInstance = new StoredGameManager();
    private StoredGameRepository repository;

    public static StoredGameManager getInstance() {
        return ourInstance;
    }

    private StoredGameManager() {
        repository = new StoredGameRepository();
    }

    @Override
    public StoredGame createStoredGame(Character character, Adventure adventure, int placeId,
                                       int pendingInventoryItemsCount, int score) {
        StoredGame storedGame = new StoredGame();
        storedGame.setPlaceId(placeId);
        storedGame.setCharacterJson(Character.getJsonFromCharacter(character));
        storedGame.setCreationDate(new Date(TimeUtils.millis()));
        storedGame.setProgressPercentage(0);
        storedGame.setCharacterName(character.getName());
        storedGame.setCurrentLifePoints(character.getCurrentLifePoints());
        storedGame.setMaximumLifePoints(character.getMaximumLifePoints());
        storedGame.setCurrentPowerPoints(character.getCurrentPowerPoints());
        storedGame.setMaximumPowerPoints(character.getMaximumPowerPoints());
        storedGame.setCharacterPortrait(character.getPortraitImageName());
        storedGame.setAdventureTitleTranslated(adventure.getTranslatedTitle());
        storedGame.setPendingInventoryItemsCount(pendingInventoryItemsCount);
        storedGame.setCurrentLuckPoints(character.getCurrentLuckPoints());
        storedGame.setMaximumLuckPoints(character.getMaximumLuckPoints());
        storedGame.setScore(score);

        return repository.add(storedGame);
    }

    @Override
    public Collection<StoredGame> getAllStoredGames(){
        return repository.findAll();
    }

    @Override
    public void deleteStoredGame(StoredGame storedGame) {
        if(storedGame != null) {
            repository.delete(storedGame);
        }
    }


}
