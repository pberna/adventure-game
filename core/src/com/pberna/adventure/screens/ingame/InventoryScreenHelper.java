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

package com.pberna.adventure.screens.ingame;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.pberna.adventure.pj.EEquipmentPosition;
import com.pberna.adventure.pj.Gender;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.adventure.pj.Character;

public class InventoryScreenHelper {
	
	public static final EEquipmentPosition [] GenderDependantEquipmentPositions = { EEquipmentPosition.Head, 
		EEquipmentPosition.Breast, EEquipmentPosition.Feet, EEquipmentPosition.Neck };
	
	public static Label.LabelStyle getNormalLabelStyle() {
		return StageScreenHelper.getLabelStyle(Color.BLACK);
	}

	public static TextureRegion getEquipmentPositionImage(Character character, EEquipmentPosition equipmentPosition) {
		String imageName = getEquipmentPositionImageName(character, equipmentPosition);
		
		if(imageName != null && imageName != "") {
			return AssetRepository.getInstance().getTextureRegion("inventory", imageName);
		}
		
		return null;
	}
	
	private static String getEquipmentPositionImageName(Character character, EEquipmentPosition equipmentPosition) {		
		switch(equipmentPosition) {
			case Breast:
				if(character != null && character.getGender() == Gender.Female) {
					return "female_breast";					
				} else {
					return "male_breast";
				}
				
			case Head:
				if(character != null && character.getGender() == Gender.Female) {
					return "female_head";					
				} else {
					return "male_head";
				}
				
			case LeftHand:
				return "left_hand";
			
			case RightHand:
				return "right_hand";
			
			case LeftHandRing:
			case RightHandRing:
				return "ring";
			
			case Feet:
				if(character != null && character.getGender() == Gender.Female) {
					return "female_feet";					
				} else {
					return "male_feet";
				}
			case Neck:
				if(character != null && character.getGender() == Gender.Female) {
					return "female_neck";					
				} else {
					return "male_neck";
				}
				
			default:
				return "";						
		}		
	}
	
	public static void setPositionInEquippedGrid(EEquipmentPosition position, Image image, Image imageGrid) {
		switch(position) {
			case Breast:
				PositionerHelper.setPositionCentered(image, new Rectangle(imageGrid.getX() + imageGrid.getWidth() / 3f, imageGrid.getY(),
					imageGrid.getWidth() / 3f, imageGrid.getHeight() / 2f));
			break;
			
		case Head:
				PositionerHelper.setPositionCentered(image, new Rectangle(imageGrid.getX() + imageGrid.getWidth() / 3f, 
					imageGrid.getY() + imageGrid.getHeight() / 2f, imageGrid.getWidth() / 3f, imageGrid.getHeight() / 2f));
			break;
			
		case LeftHand:
				PositionerHelper.setPositionCentered(image, new Rectangle(imageGrid.getX() + (imageGrid.getWidth() / 3f * 2f), 
					imageGrid.getY(), imageGrid.getWidth() / 3f, imageGrid.getHeight() / 2f));
			break;
		
		case RightHand:
				PositionerHelper.setPositionCentered(image, new Rectangle(imageGrid.getX(),	imageGrid.getY(), 
						imageGrid.getWidth() / 3f, imageGrid.getHeight() / 2f));
			break;
		
		case LeftHandRing:
			PositionerHelper.setPositionCentered(image, new Rectangle(imageGrid.getX() + (imageGrid.getWidth() / 3f * 2.5f), 
					imageGrid.getY() + imageGrid.getHeight() / 2f, imageGrid.getWidth() / 6f, imageGrid.getHeight() * 0.25f));
			break;
			
		case RightHandRing:
			PositionerHelper.setPositionCentered(image, new Rectangle(imageGrid.getX() + (imageGrid.getWidth() / 3f * 2f), 
					imageGrid.getY() + imageGrid.getHeight() / 2f, imageGrid.getWidth() / 6f, imageGrid.getHeight() * 0.25f));
			break;
		
		case Feet:
				PositionerHelper.setPositionCentered(image, new Rectangle(imageGrid.getX(),	imageGrid.getY() + imageGrid.getHeight() / 2f, 
					imageGrid.getWidth() / 3f, imageGrid.getHeight() / 2f));
			break;
			
		case Neck:
				PositionerHelper.setPositionCentered(image, new Rectangle(imageGrid.getX() + (imageGrid.getWidth() / 3f * 2f), 
					imageGrid.getY() + imageGrid.getHeight() * 0.75f, imageGrid.getWidth() / 3f, imageGrid.getHeight() *0.25f));
			break;
			
		default:
			break;
					
		}		
	}

}
