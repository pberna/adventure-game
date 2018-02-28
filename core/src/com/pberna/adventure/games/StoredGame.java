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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "StoredGame")
public class StoredGame {

    public static final String TableName = "StoredGame";
    public static final String IdColumnName = "Id";
    public static final String CreationDataColumnName = "CreationDate";

    @DatabaseField(generatedId = true, columnName = IdColumnName)
    private int id;
    @DatabaseField(columnName = "PlaceId")
    private int placeId;
    @DatabaseField(columnName = "CharacterJson")
    private String characterJson;
    @DatabaseField(columnName = CreationDataColumnName)
    private Date creationDate;
    @DatabaseField(columnName = "ProgressPercentage")
    private float progressPercentage;
    @DatabaseField(columnName = "CharacterName")
    private String characterName;
    @DatabaseField(columnName = "CurrentLifePoints")
    private int currentLifePoints;
    @DatabaseField(columnName = "MaximumLifePoints")
    private int maximumLifePoints;
    @DatabaseField(columnName = "CurrentPowerPoints")
    private int currentPowerPoints;
    @DatabaseField(columnName = "MaximumPowerPoints")
    private int maximumPowerPoints;
    @DatabaseField(columnName = "CharacterPortrait")
    private String characterPortrait;
    @DatabaseField(columnName = "AdventureTitleTranslated")
    private String adventureTitleTranslated;
    @DatabaseField(columnName = "PendingInventoryItemsCount")
    private int pendingInventoryItemsCount;
    @DatabaseField(columnName = "CurrentLuckPoints")
    private int currentLuckPoints;
    @DatabaseField(columnName = "MaximumLuckPoints")
    private int maximumLuckPoints;
    @DatabaseField(columnName = "Score")
    private int score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getCharacterJson() {
        return characterJson;
    }

    public void setCharacterJson(String characterJson) {
        this.characterJson = characterJson;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public float getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(float progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getCurrentLifePoints() {
        return currentLifePoints;
    }

    public void setCurrentLifePoints(int currentLifePoints) {
        this.currentLifePoints = currentLifePoints;
    }

    public int getMaximumLifePoints() {
        return maximumLifePoints;
    }

    public void setMaximumLifePoints(int maximumLifePoints) {
        this.maximumLifePoints = maximumLifePoints;
    }

    public int getCurrentPowerPoints() {
        return currentPowerPoints;
    }

    public void setCurrentPowerPoints(int currentPowerPoints) {
        this.currentPowerPoints = currentPowerPoints;
    }

    public int getMaximumPowerPoints() {
        return maximumPowerPoints;
    }

    public void setMaximumPowerPoints(int maximumPowerPoints) {
        this.maximumPowerPoints = maximumPowerPoints;
    }

    public String getCharacterPortrait() {
        return characterPortrait;
    }

    public void setCharacterPortrait(String characterPortrait) {
        this.characterPortrait = characterPortrait;
    }

    public String getAdventureTitleTranslated() {
        return adventureTitleTranslated;
    }

    public void setAdventureTitleTranslated(String adventureTitleTranslated) {
        this.adventureTitleTranslated = adventureTitleTranslated;
    }

    public int getPendingInventoryItemsCount() {
        return pendingInventoryItemsCount;
    }

    public void setPendingInventoryItemsCount(int pendingInventoryItemsCount) {
        this.pendingInventoryItemsCount = pendingInventoryItemsCount;
    }

    public int getCurrentLuckPoints() {
        return currentLuckPoints;
    }

    public void setCurrentLuckPoints(int currentLuckPoints) {
        this.currentLuckPoints = currentLuckPoints;
    }

    public int getMaximumLuckPoints() {
        return maximumLuckPoints;
    }

    public void setMaximumLuckPoints(int maximumLuckPoints) {
        this.maximumLuckPoints = maximumLuckPoints;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
