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

package com.pberna.engine.achievements.achievement;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.pberna.engine.localization.Localization;

import java.util.Date;

@DatabaseTable(tableName = "Achievement")
public class Achievement {

    public static final String TableName = "Achievement";
    public static final String IdColumnName = "Id";
    public static final String AdventureIdColumnName = "AdventureId";
    public static final String UnlockedColumnName = "Unlocked";
    public static final String UnlockDateColumnName = "UnlockDate";

    @DatabaseField(id = true, columnName = IdColumnName)
    private int id;
    @DatabaseField(columnName = AdventureIdColumnName   )
    private int adventureId;
    private String externalId;
    @DatabaseField(columnName = "NameKey")
    private String nameKey;
    @DatabaseField(columnName = "DescriptionKey")
    private String descriptionKey;
    @DatabaseField(columnName = "ImageName")
    private String imageName;
    @DatabaseField(columnName = "Order")
    private int order;
    @DatabaseField(columnName = "Hidden")
    private boolean hidden;
    @DatabaseField(columnName = "Incremental")
    private boolean incremental;
    @DatabaseField(columnName = UnlockedColumnName)
    private boolean unlocked;
    @DatabaseField(columnName = UnlockDateColumnName)
    private Date unlockDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdventureId() {
        return adventureId;
    }

    public void setAdventureId(int adventureId) {
        this.adventureId = adventureId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isIncremental() {
        return incremental;
    }

    public void setIncremental(boolean incremental) {
        this.incremental = incremental;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public String getName() {
        return Localization.getInstance().getTranslation("Achievements", nameKey);
    }

    public String getDescription() {
        return Localization.getInstance().getTranslation("Achievements", descriptionKey);
    }

    public Date getUnlockDate() {
        return unlockDate;
    }

    public void setUnlockDate(Date unlockDate) {
        this.unlockDate = unlockDate;
    }
}
