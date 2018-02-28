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

package com.pberna.adventure.pj;

import java.util.ArrayList;

import com.pberna.adventure.items.Item;
import com.pberna.adventure.items.manager.ItemManager;
import com.pberna.engine.localization.ILocalizable;

public class Backpack implements ILocalizable {
	private static final int ListItemsInitialSize = 20;
	
	private ArrayList<Item> listItems;
	
	public Backpack(){
		listItems = new ArrayList<Item>(ListItemsInitialSize);
	}
	
	public int getItemCount() {
		return listItems.size();
	}
	
	public void addItem(Item item) {
		listItems.add(item);
	}
	
	public Item getItem(int index) {
		if(index >= listItems.size() || index < 0) {
			throw new IndexOutOfBoundsException("GetItem: backpack contains " + listItems.size() + " item(s). Index " + index + ".");
		}
		return listItems.get(index);
	}
	
	public void removeItem(int index) {
		if(index >= listItems.size() || index < 0) {
			throw new IndexOutOfBoundsException("RemoveItem: backpack contains " + listItems.size() + " item(s). Index " + index + ".");
		}
		listItems.remove(index);
	}
	
	public void removeItem(Item item) {
		listItems.remove(item);
	}

	@Override
	public void refreshLocalizableItems() {
		ArrayList<Integer> listItemsIds = new ArrayList<Integer>();
		for(Item item:listItems) {
			listItemsIds.add(item.getId());
		}
		listItems.clear();
		for(Integer id:listItemsIds) {
			listItems.add(ItemManager.getInstance().getItem(id.intValue()));
		}
	}
}
