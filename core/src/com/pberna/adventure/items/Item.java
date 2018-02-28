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

package com.pberna.adventure.items;

import java.util.ArrayList;

public class Item {
	
	private int id;
	private String name;
	private String description;
	private String imageName;
	
	public Item() {
		id = 0;
		name = "";
		description = "";
		imageName = "";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public Item setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Item setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getImageName() {
		return imageName;
	}

	public Item setImageName(String imageName) {
		this.imageName = imageName;
		return this;
	}
	
	public static Item findItem(ArrayList<Item> listItems, int idItem) {
		
		for(Item item: listItems) {
			if(item.getId() == idItem) {
				return item;
			}
		}
		
		return null;
	}
	
}
