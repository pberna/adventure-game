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

package com.pberna.adventure.screens.pjCreation;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.pjCreation.controls.AttributeSkillEditControlListener;
import com.pberna.adventure.screens.pjCreation.controls.AttributeSkillEditListControl;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;
import com.pberna.adventure.pj.Character;

public class SelectAttributesScreen extends BasePjCreationScreen
implements IModelEditingScreen<Character>{
		
	private ArrayList<Attribute> pjAttributes;
	private Character character;
	private int totalAttributesPoints;

	private AttributeSkillEditListControl attributesEditListControl;
	private Label remainingPointsLabel;
	private Label errorMessageLabel;
	
	public SelectAttributesScreen() {
		super(Localization.getInstance().getTranslation(
				"PjCreation", "chooseAttributes"), "selected_border");
		
		pjAttributes = Attribute.getAttributes();
		character = null;
		setTotalAttributesPoints(Attribute.TotalAttributePointsPerPj);

		setAcceptButtonVisibility(false);
		buildStage();
		
		this.addListener(new com.pberna.adventure.screens.pjCreation.CloseScreenListener() {
			
			@Override
			public void cancel(Object sender) {
				emptyMessageText();				
			}
			
			@Override
			public void accept(Object sender) {
				emptyMessageText();				
			}
		});
	}
	
	@Override
	public void setModel(Character model) {
		this.character = model;
		
		for(int i = 0; i < this.character.getAttributes().size(); i++){
			for(int j = 0; j < pjAttributes.size(); j++) {
				if(this.character.getAttributes().get(i).getId() == pjAttributes.get(j).getId()){
					pjAttributes.set(j, this.character.getAttributes().get(i));
					setAttributeValue(pjAttributes.get(j), pjAttributes.get(j).getValue());
					break;
				}
			}
		}		
		emptyMessageText();
		updateRemaingPointsText();
		updateAcceptButtonVisibility();
	}

	@Override
	public Character getModel() {
		return this.character;
	}

	public void setTotalAttributesPoints(int totalAttributesPoints) {
		this.totalAttributesPoints = totalAttributesPoints;
		updateRemaingPointsText();
	}

	@SuppressWarnings("unchecked")
	private void buildStage() {

		attributesEditListControl = new AttributeSkillEditListControl(pjAttributes);
		attributesEditListControl.addListener(new AttributeSkillEditControlListener() {
			@Override
			public void increasePressed(int currentValue, int id) {
				increaseAttribute(Attribute.findAttribute(pjAttributes, id));
			}

			@Override
			public void decreasePressed(int currentValue, int id) {
				decreaseAttribute(Attribute.findAttribute(pjAttributes, id));
			}
		});
		stage.addActor(attributesEditListControl);

		remainingPointsLabel = new Label(String.format(Localization.getInstance().getTranslation("PjCreation", "remainingPoints"),
				getRemainingAttributePoints()),	StageScreenHelper.getLabelStyle(Color.BLACK));
		remainingPointsLabel.setAlignment(Align.center);
		stage.addActor(remainingPointsLabel);
		
		errorMessageLabel = new Label("", StageScreenHelper.getLabelStyle(Color.ORANGE));
		errorMessageLabel.setAlignment(Align.center);
		stage.addActor(errorMessageLabel);
	}
	
	private void increaseAttribute(Attribute attribute) {
		StringBuilder errorMessage = new StringBuilder();
		if(canIncreaseAttribute(attribute, errorMessage)){
			int attributeValue = attribute.getValue();
			setAttributeValue(attribute, attributeValue + 1);
			emptyMessageText();
			updateRemaingPointsText();
			updateAcceptButtonVisibility();
		} else {
			setErrorMessageText(errorMessage.toString());
		}		
	}
	private void decreaseAttribute(Attribute attribute) {
		StringBuilder errorMessage = new StringBuilder();
		if(canDecreaseAttribute(attribute, errorMessage)){
			int attributeValue = attribute.getValue();
			setAttributeValue(attribute, attributeValue - 1);
			emptyMessageText();
			updateRemaingPointsText();
			updateAcceptButtonVisibility();
		} else {
			setErrorMessageText(errorMessage.toString());
		}	
	}
	
	private boolean canIncreaseAttribute(Attribute attribute, StringBuilder errorMessage){
		
		if(getTotalAttributePointsUsed() >= totalAttributesPoints) {
			errorMessage.append(String.format(Localization.getInstance().getTranslation(
					"PjCreation", "messageCannotIncreaseTotalAttributes"), attribute.getName()));
			return false;
		}
		
		int attributeValue = attribute.getValue();
		if(attributeValue >= Attribute.MaximumGenericValue){
			errorMessage.append(String.format(Localization.getInstance().getTranslation("PjCreation", 
					"messageCannotIncreaseAttribute"), attribute.getName(),
					this.character.getBaseAttributeValue(attribute.getId())));
			return false;
		}
		return true;
	}
	
	private boolean canDecreaseAttribute(Attribute attribute, StringBuilder errorMessage){
				
		int attributeValue = attribute.getValue();
		if(attributeValue <= Attribute.MinimumGenericValue){
			errorMessage.append(String.format(Localization.getInstance().getTranslation("PjCreation", 
					"messageCannotDecreaseAttribute"), attribute.getName(),
					this.character.getBaseAttributeValue(attribute.getId())));
			return false;
		}
		return true;
	}
	
	private void setAttributeValue(Attribute attribute, int attributeValue) {
		attribute.setValue(attributeValue);
		attributesEditListControl.setValue(attribute.getId(), this.character.getBaseAttributeValue(
				attribute.getId()));
	}
	
	private int getTotalAttributePointsUsed() {
		int total = 0;
		for(int i = 0; i < pjAttributes.size(); i++){
			total += pjAttributes.get(i).getValue();
		}
		return total;
	}
	
	private int getRemainingAttributePoints() {
		return totalAttributesPoints - getTotalAttributePointsUsed();
	}
	
	private void setErrorMessageText(String errorMessage) {
		errorMessageLabel.setText(errorMessage);
	}
	
	private void emptyMessageText() {
		errorMessageLabel.setText("");
	}
	
	private void updateRemaingPointsText() {
		if(remainingPointsLabel != null) {
			remainingPointsLabel.setText(String.format(Localization.getInstance().getTranslation("PjCreation", "remainingPoints"), 
				getRemainingAttributePoints()));
		}
	}
	
	private void updateAcceptButtonVisibility()
	{
		if(getRemainingAttributePoints() <= 0) {
			setAcceptButtonVisibility(true);
		} else {
			setAcceptButtonVisibility(false);
		}
	}
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);

		PositionerHelper.setPositionCenteredHorizontalFillingColumn(new Actor[] {
				attributesEditListControl, remainingPointsLabel, errorMessageLabel}, mainPanel, VerticalSide.Top);
	}

	@Override
	public void refreshLocalizableItems() {
		mainText = Localization.getInstance().getTranslation("PjCreation", "chooseAttributes");
		pjAttributes = Attribute.getAttributes();
		attributesEditListControl.refreshLocalizableItems(pjAttributes);
		updateRemaingPointsText();		
		
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());		
		super.refreshLocalizableItems();
		
		updateAcceptButtonVisibility();
	}
}
