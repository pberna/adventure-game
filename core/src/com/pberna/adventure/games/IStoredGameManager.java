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

import com.pberna.adventure.adventure.Adventure;
import com.pberna.adventure.pj.Character;

import java.util.Collection;

public interface IStoredGameManager {
    StoredGame createStoredGame(Character character, Adventure adventure, int placeId, int pendingInventoryItemsCount, int score);
    Collection<StoredGame> getAllStoredGames();
    void deleteStoredGame(StoredGame storedGame);
}
