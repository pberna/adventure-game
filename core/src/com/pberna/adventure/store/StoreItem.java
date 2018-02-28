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

package com.pberna.adventure.store;

import com.pberna.adventure.dependencies.Settings;
import com.pberna.adventure.dependencies.StoreItemConfig;
import com.pberna.engine.localization.Localization;

import java.util.ArrayList;

public class StoreItem {
    public static final String RemoveAdvertisingId = "RemoveAdvertising";
    private static final String RemoveAdvertisingPrice = "1";

    private String id;
    private String title;
    private String description;
    private String imageName;
    private String price;
    private String platformStoreId;
    private boolean hasPurchased;

    public StoreItem() {
        id = "";
        title = "";
        description = "";
        imageName = "";
        hasPurchased = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public static ArrayList<StoreItem> getAllStoreItems() {
        ArrayList<StoreItem> storeItems = new ArrayList<StoreItem>();

        storeItems.add(getRemoveAdvertisingItem());
        assignPlatformInfoToStoreItems(storeItems);

        return storeItems;
    }

    private static StoreItem getRemoveAdvertisingItem() {
        return createStoreItem(RemoveAdvertisingId, Localization.getInstance().getTranslation("OutGame","removeAdvertisingTitle"),
                Localization.getInstance().getTranslation("OutGame", "removeAdvertisingDescription"),
                "remove_advertising", RemoveAdvertisingPrice);
    }

    private static StoreItem createStoreItem(String id, String title, String description, String imageName, String price) {
        StoreItem storeItem = new StoreItem();

        storeItem.setId(id);
        storeItem.setTitle(title);
        storeItem.setDescription(description);
        storeItem.setImageName(imageName);
        storeItem.setPrice(price);

        return storeItem;
    }

    private static void assignPlatformInfoToStoreItems(ArrayList<StoreItem> storeItems) {
        for(StoreItemConfig storeItemConfig : Settings.getInstance().getStoreItemConfigs()) {
            StoreItem storeItem = findStoreItem(storeItems, storeItemConfig.getStoreItemId());
            if(storeItem != null) {
                storeItem.setPlatformStoreId(storeItemConfig.getPlatformStoreId());
            }
        }
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public static StoreItem findStoreItem(ArrayList<StoreItem> storeItems, String id) {
        for(StoreItem storeItem : storeItems) {
            if(storeItem.getId().equals(id)) {
                return storeItem;
            }
        }
        return null;
    }

    public String getPlatformStoreId() {
        return platformStoreId;
    }

    public void setPlatformStoreId(String platformStoreId) {
        this.platformStoreId = platformStoreId;
    }

    public boolean hasPurchased() {
        return hasPurchased;
    }

    public void setHasPurchased(boolean hasPurchased) {
        this.hasPurchased = hasPurchased;
    }
}
