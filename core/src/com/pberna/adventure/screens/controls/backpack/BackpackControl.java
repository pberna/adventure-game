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

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.pberna.adventure.items.Item;
import com.pberna.engine.controls2D.ExtendedImageButton;

import java.util.ArrayList;

public class BackpackControl extends HorizontalGroup {
    public static final int MinimumItemSlots = 3;

    private ArrayList<ItemSlotControl> itemSlots;

    public BackpackControl() {
        buildControl();
    }

    private void buildControl() {
        //initialize item slots
        itemSlots = new ArrayList<ItemSlotControl>();
        for(int i = 0; i < MinimumItemSlots; i++) {
            ItemSlotControl slot = createItemSlotControl();
            addActor(slot);
            itemSlots.add(slot);
        }
        pack();
    }

    private ItemSlotControl createItemSlotControl() {
        return new ItemSlotControl();
    }

    public void addItemButton(ExtendedImageButton<Item> itemButton) {
        int emptySlotIndex = -1;
        for(int i = 0; i < itemSlots.size(); i++) {
            if(itemSlots.get(i).getItemButton() == null) {
                emptySlotIndex = i;
                break;
            }
        }

        if(emptySlotIndex >= 0) {
            itemSlots.get(emptySlotIndex).setItemButton(itemButton);
        } else {
            ItemSlotControl newItemSlot = createItemSlotControl();
            newItemSlot.setItemButton(itemButton);
            addActor(newItemSlot);
            itemSlots.add(newItemSlot);
        }
    }

    public ExtendedImageButton<Item> takeSelectedItemButton() {
        int selectedSlotIndex = -1;
        for(int i = 0; i < itemSlots.size(); i++) {
            if (itemSlots.get(i).isSelected()) {
                selectedSlotIndex = i;
                break;
            }
        }

        if(selectedSlotIndex < 0) {
            return null;
        }

        ExtendedImageButton<Item> selectedItemButton = itemSlots.get(selectedSlotIndex).getItemButton();
        itemSlots.get(selectedSlotIndex).setItemButton(null);

        for(int i = selectedSlotIndex; i < itemSlots.size() - 1; i++) {
            ExtendedImageButton<Item> itemButton = itemSlots.get(i + 1).getItemButton();
            itemSlots.get(i + 1).setItemButton(null);
            itemSlots.get(i).setItemButton(itemButton);
        }

        if(itemSlots.size() > MinimumItemSlots) {
            ItemSlotControl slotToDelete = itemSlots.get(itemSlots.size() - 1);
            removeActor(slotToDelete);
            itemSlots.remove(slotToDelete);
        }

        return selectedItemButton;
    }

    public void removeAllItems() {
        for(ItemSlotControl itemSlot : itemSlots) {
            itemSlot.setItemButton(null);
            itemSlot.setSelected(false);
        }

        int totalItemSlots = itemSlots.size();
        for(int i = MinimumItemSlots; i < totalItemSlots; i++) {
            itemSlots.get(MinimumItemSlots).remove();
            itemSlots.remove(MinimumItemSlots);
        }
        pack();
    }

    public void setSelectedItemButton(ExtendedImageButton<Item> itemButton, boolean selected) {
        for(ItemSlotControl slot: itemSlots) {
            if(slot.getItemButton() == itemButton) {
                slot.setSelected(selected);
            } else {
                slot.setSelected(false);
            }
        }
    }
}
