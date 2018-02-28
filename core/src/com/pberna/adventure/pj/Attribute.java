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

import com.pberna.adventure.screens.controls.IImageName;
import com.pberna.engine.localization.Localization;

public class Attribute implements IImageName {

	public static final int MinimumGenericValue = 1;
	public static final int MaximumGenericValue = 8;
	
	public static final int TotalAttributePointsPerPj = 16;
	
	public static final int IdMight = 1;
	public static final int IdAgility = 2;
	public static final int IdIntelligence = 3;
	public static final int IdFortune = 4;
	
	private int id;
	private String name;
	private int value;
	private String description;
	private String imageName;
	
	public int getId() {
		return id;
	}

	public Attribute setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Attribute setName(String name) {
		this.name = name;
		return this;
	}

	public int getValue() {
		return value;
	}

	public Attribute setValue(int value) {
		this.value = value;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Attribute setDescription(String description) {
		this.description = description;
		return this;
	}


	public String getImageName() {
		return imageName;
	}

	public Attribute setImageName(String imageName) {
		this.imageName = imageName;
		return this;
	}
	
	public static ArrayList<Attribute> getAttributes()
	{
		ArrayList<Attribute> listAttributes = new ArrayList<Attribute>();
		
		//Might
		listAttributes.add(new Attribute()
			.setId(IdMight)
			.setName(Localization.getInstance().getTranslation("PjInformation", "attributeMight"))
			.setValue(MinimumGenericValue)
			.setDescription(Localization.getInstance().getTranslation("PjInformation", "descriptionAttributeMight"))
			.setImageName("might"));
		
		//Agility
		listAttributes.add(new Attribute()
			.setId(IdAgility)
			.setName(Localization.getInstance().getTranslation("PjInformation", "attributeAgility"))
			.setValue(MinimumGenericValue)
			.setDescription(Localization.getInstance().getTranslation("PjInformation", "descriptionAttributeAgility"))
			.setImageName("agility"));
		
		//Intelligence
		listAttributes.add(new Attribute()
			.setId(IdIntelligence)
			.setName(Localization.getInstance().getTranslation("PjInformation", "attributeIntelligence"))
			.setValue(MinimumGenericValue)
			.setDescription(Localization.getInstance().getTranslation("PjInformation", "descriptionAttributeIntelligence"))
			.setImageName("intelligence"));
				
		//Fortune
		listAttributes.add(new Attribute()
			.setId(IdFortune)
			.setName(Localization.getInstance().getTranslation("PjInformation", "attributeFortune"))
			.setValue(MinimumGenericValue)
			.setDescription(Localization.getInstance().getTranslation("PjInformation", "descriptionAttributeFortune"))
			.setImageName("fortune"));
		
		return listAttributes;
	}
	
	public static Attribute findAttribute(ArrayList<Attribute> listAttributes, int idAttribute){
		
		for(Attribute attribute: listAttributes) {
			if(attribute.id == idAttribute) {
				return attribute;
			}
		}
		
		return null;
	}
}
