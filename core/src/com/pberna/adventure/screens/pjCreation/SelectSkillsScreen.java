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
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.pjCreation.controls.AttributeSkillEditControlListener;
import com.pberna.adventure.screens.pjCreation.controls.AttributeSkillEditListControl;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;

public class SelectSkillsScreen extends BasePjCreationScreen
implements IModelEditingScreen<Character>{

	private ArrayList<Skill> pjSkills;
	private Character character;
	private int totalSkillsPoints;

	private AttributeSkillEditListControl attributesEditListControl;
	private Label remainingPointsLabel;
	private Label errorMessageLabel;
	
	public SelectSkillsScreen() {
		super(Localization.getInstance().getTranslation(
				"PjCreation", "chooseSkills"), "selected_border");
		
		pjSkills = Skill.getSkills();
		character = null;
		setTotalSkillsPoints(Skill.TotalSkillPointsPerPj);

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
		
		for(int i = 0; i < this.character.getSkills().size(); i++){
			for(int j = 0; j < pjSkills.size(); j++) {
				if(this.character.getSkills().get(i).getId() == pjSkills.get(j).getId()){
					pjSkills.set(j, this.character.getSkills().get(i));
					setSkillValue(pjSkills.get(j), pjSkills.get(j).getValue());
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
	
	public void setTotalSkillsPoints(int totalSkillsPoints) {
		this.totalSkillsPoints = totalSkillsPoints;
		updateRemaingPointsText();
	}
	
	@SuppressWarnings("unchecked")
	private void buildStage() {
		attributesEditListControl = new AttributeSkillEditListControl(pjSkills, true);
		attributesEditListControl.addListener(new AttributeSkillEditControlListener() {
			@Override
			public void increasePressed(int currentValue, int id) {
				increaseSkill(Skill.findSkill(pjSkills, id));
			}

			@Override
			public void decreasePressed(int currentValue, int id) {
				decreaseSkill(Skill.findSkill(pjSkills, id));
			}
		});
		stage.addActor(attributesEditListControl);
		
		remainingPointsLabel = new Label(String.format(Localization.getInstance().getTranslation("PjCreation", "remainingPoints"), 
				getRemainingSkillPoints()),	StageScreenHelper.getLabelStyle(Color.BLACK));
		remainingPointsLabel.setAlignment(Align.center);
		stage.addActor(remainingPointsLabel);
		
		errorMessageLabel = new Label("", StageScreenHelper.getLabelStyle(Color.ORANGE));
		errorMessageLabel.setAlignment(Align.center);
		stage.addActor(errorMessageLabel);
	}

	private void increaseSkill(Skill skill) {
		StringBuilder errorMessage = new StringBuilder();
		if(canIncreaseSkill(skill, errorMessage)){
			int SkillValue = skill.getValue();
			setSkillValue(skill, SkillValue + 1);
			emptyMessageText();
			updateRemaingPointsText();
			updateAcceptButtonVisibility();
		} else {
			setErrorMessageText(errorMessage.toString());
		}		
	}
	private void decreaseSkill(Skill skill) {
		StringBuilder errorMessage = new StringBuilder();
		if(canDecreaseSkill(skill, errorMessage)){
			int SkillValue = skill.getValue();
			setSkillValue(skill, SkillValue - 1);
			emptyMessageText();
			updateRemaingPointsText();
			updateAcceptButtonVisibility();
		} else {
			setErrorMessageText(errorMessage.toString());
		}	
	}
	
	private boolean canIncreaseSkill(Skill skill, StringBuilder errorMessage){
		
		if(getTotalSkillPointsUsed() >= totalSkillsPoints) {
			errorMessage.append(String.format(Localization.getInstance().getTranslation(
					"PjCreation", "messageCannotIncreaseTotalSkills"), skill.getName()));
			return false;
		}
		
		int SkillValue = skill.getValue();
		if(SkillValue >= Skill.MaximumGenericValue){
			errorMessage.append(String.format(Localization.getInstance().getTranslation("PjCreation", 
					"messageCannotIncreaseSkill"), skill.getName(),
					this.character.getBaseSkillValue(skill.getId())));
			return false;
		}
		return true;
	}
	
	private boolean canDecreaseSkill(Skill skill, StringBuilder errorMessage){
				
		int SkillValue = skill.getValue();
		if(SkillValue <= Skill.MinimumGenericValue){
			errorMessage.append(String.format(Localization.getInstance().getTranslation("PjCreation", 
					"messageCannotDecreaseSkill"), skill.getName(),
					this.character.getBaseSkillValue(skill.getId())));
			return false;
		}
		return true;
	}
	
	private void setSkillValue(Skill skill, int SkillValue) {
		skill.setValue(SkillValue);
		attributesEditListControl.setValue(skill.getId(), this.character.getBaseSkillValue(
				skill.getId()));
	}
	
	private int getTotalSkillPointsUsed() {
		int total = 0;
		for(int i = 0; i < pjSkills.size(); i++){
			total += pjSkills.get(i).getValue();
		}
		return total;
	}
	
	private int getRemainingSkillPoints() {
		return totalSkillsPoints - getTotalSkillPointsUsed();
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
				getRemainingSkillPoints()));
		}
	}
	
	private void updateAcceptButtonVisibility()
	{
		if(getRemainingSkillPoints() <= 0) {
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
		mainText = Localization.getInstance().getTranslation("PjCreation", "chooseSkills");
		pjSkills = Skill.getSkills();
		attributesEditListControl.refreshLocalizableItemsSkills(pjSkills);
		updateRemaingPointsText();
				
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());		
		super.refreshLocalizableItems();
		
		updateAcceptButtonVisibility();
	}

}
