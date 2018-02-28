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

package com.pberna.adventure.places;

import java.util.ArrayList;
import java.util.Locale;

import com.pberna.adventure.items.Item;
import com.pberna.engine.localization.ExtendedLocale;
import com.pberna.engine.localization.Localization;

public class Place {
			
	private int id;
	private String text;
	private PlaceType placeType;
	private ArrayList<PlaceTranslation> translations;
	private ArrayList<Item> itemsCharacterGets;
	private ArrayList<String> playerActions;
	
	protected Place() {
		translations = new ArrayList<PlaceTranslation>();
		setItemsCharacterGets(new ArrayList<Item>());
		playerActions = new ArrayList<String>();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public PlaceType getPlaceType() {
		return placeType;
	}
	public void setPlaceType(PlaceType placeType) {
		this.placeType = placeType;
	}
	
	public ArrayList<PlaceTranslation> getTranslations() {
		return translations;
	}
	public void setTranslations(ArrayList<PlaceTranslation> translations) {
		this.translations = translations;
	}
	
	public String getTranslatedText() {
		if(translations != null) {
			Locale locale = Localization.getInstance().getLocale();
			
			for(PlaceTranslation placeTranslation: translations) {
				if(ExtendedLocale.isTranslationCompatible(placeTranslation.getLanguage(),
						locale.getLanguage())) {
					return placeTranslation.getText();
				}
			}
		}
		
		return getText();
	}

	public ArrayList<Item> getItemsCharacterGets() {
		return itemsCharacterGets;
	}

	public void setItemsCharacterGets(ArrayList<Item> itemsCharacterGets) {
		this.itemsCharacterGets = itemsCharacterGets;
	}

	public ArrayList<String> getPlayerActions() {
		return playerActions;
	}

	public void addPlayerAction(String actionId) {
		playerActions.add(actionId);
	}
}
