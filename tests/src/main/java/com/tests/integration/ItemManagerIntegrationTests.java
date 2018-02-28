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

package com.tests.integration;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pberna.adventure.items.Item;
import com.pberna.adventure.items.ItemUsable;
import com.pberna.adventure.items.manager.IItemManager;
import com.pberna.adventure.items.manager.ItemManager;
import com.pberna.engine.assets.AssetRepository;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ItemManagerIntegrationTests {

    private static final int InitialNumberOfItems = 30;
    private IItemManager _itemManager;
    private AssetRepository _assetRepository;

    public ItemManagerIntegrationTests() {

        _assetRepository = AssetRepository.getInstance();

        //System under test
        _itemManager = ItemManager.getInstance();
    }

    @Test
    public void getItem_AllItems_AreWellformed () {

        //Arrange
        ArrayList<Item> listItems = new ArrayList<>(InitialNumberOfItems);

        //Act
        for(int i = 1;; i++) {
            Item item = _itemManager.getItem(i);
            if(item != null) {
                listItems.add(item);
            } else {
                break;
            }
        }

        //Assert
        for(Item item: listItems) {
            Assert.assertNotNull(item);
            Assert.assertNotNull(item.getName());
            Assert.assertNotEquals(Constants.NoText, item.getName());
            Assert.assertNotNull(item.getDescription());
            Assert.assertNotEquals(Constants.NoText, item.getDescription());
            if(item instanceof ItemUsable) {
                Assert.assertNotNull(((ItemUsable) item).getEffectDescription() );
                Assert.assertNotEquals(Constants.NoText, ((ItemUsable) item).getEffectDescription() );
            }

            try {
                TextureRegion textureRegion = _assetRepository.getTextureRegion("items", item.getImageName());
                Assert.assertNotNull(textureRegion);
            } catch (Exception ex) {
                Assert.fail("Could not get image '" + item.getImageName() + "' for item " + item.getName());
            }
        }

    }
}
