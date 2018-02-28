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

package com.pberna.adventure.items.manager;

import com.pberna.adventure.items.*;
import com.pberna.engine.localization.Localization;

public class ItemManager implements IItemManager {

    private static final ItemManager instance  = new ItemManager();

    private ItemManager() {

    }

    public static ItemManager getInstance() {
        return instance;
    }

    @Override
    public Item getItem(int itemId) {
        switch (itemId) {
            case 1:
                return getBootsItem();
            case 2:
                return getLifePotionItem();
            case 3:
                return getKeyItem();
            case 4:
                return getSwordItem();
            default:
                return null;
        }
    }

    private static Boots getBootsItem() {
        Boots boots = new Boots();

        boots.setId(1);
        boots.setName(Localization.getInstance().getTranslation("Items", "greenBootsName"));
        boots.setDescription(Localization.getInstance().getTranslation("Items", "greenBootsDescription"));
        boots.setImageName("boots1");
        boots.setDefenseModifier(1);

        return boots;
    }

    private static RecoveryItem getLifePotionItem() {
        RecoveryItem lifePotion = new RecoveryItem();

        lifePotion.setId(2);
        lifePotion.setName(Localization.getInstance().getTranslation("Items", "lifePotionName"));
        lifePotion.setDescription(Localization.getInstance().getTranslation("Items", "lifePotionDescription"));
        lifePotion.setEffectDescription(Localization.getInstance().getTranslation("Items", "lifePotionEffectDescription"));
        lifePotion.setLifePointsRecoveryFull();
        lifePotion.setImageName("life_potion");

        return lifePotion;
    }

    private static Item getKeyItem() {
        Item keyItem = new Item();

        keyItem.setId(3);
        keyItem.setName(Localization.getInstance().getTranslation("Items", "jailersKeyName"));
        keyItem.setDescription(Localization.getInstance().getTranslation("Items", "jailersKeyDescription"));
        keyItem.setImageName("key");

        return keyItem;
    }

    private static Weapon1Hand getSwordItem() {
        Weapon1Hand sword = new Weapon1Hand();

        sword.setId(4);
        sword.setName(Localization.getInstance().getTranslation("Items", "jailersShortSwordName"));
        sword.setDescription(Localization.getInstance().getTranslation("Items", "jailersShortSwordDescription"));
        sword.setImageName("jailer_sword");
        sword.setAttackModifier(1);

        return sword;
    }
}
