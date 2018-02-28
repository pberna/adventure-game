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

package com.pberna.adventure.screens.controls.backpack;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.pberna.adventure.items.Item;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;

public class ItemSlotControl extends Stack {

    private Image backgroundImage;
    private ExtendedImageButton<Item> itemButton;
    private Image selectedBorder;

    public ItemSlotControl() {
        buildControl();
    }

    private void buildControl() {
        //backgroundImage
        backgroundImage = new Image(AssetRepository.getInstance().getTextureRegion("inventory", "item_slot"));
        add(backgroundImage);

        //itemButton
        itemButton = null;

        //selectedBorder
        selectedBorder = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "selected_border_item"));
        selectedBorder.setVisible(false);
        selectedBorder.setTouchable(Touchable.disabled);
        add(selectedBorder);
    }


    public ExtendedImageButton<Item> getItemButton() {
        return itemButton;
    }

    public void setItemButton(ExtendedImageButton<Item> itemButton) {
        if(this.itemButton != null) {
            this.itemButton.remove();
        }
        this.itemButton = itemButton;

        if(this.itemButton != null) {
            addActorAfter(backgroundImage, this.itemButton);
        }
        setSelected(false);
    }

    public void setSelected(boolean selected) {
        selectedBorder.setVisible(selected);
    }

    public boolean isSelected() {
        return selectedBorder.isVisible();
    }
}

