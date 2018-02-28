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

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Gender;
import com.pberna.adventure.screens.*;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.adventure.screens.controls.ActiveSpellsControl;
import com.pberna.adventure.screens.controls.AttributesSkillsControl;
import com.pberna.adventure.screens.controls.LifePowerPointsControl;
import com.pberna.adventure.screens.controls.PortraitsListControl;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;

public class CharacterSheetScreen extends BaseStageScreen implements ILocalizable {
		
	private static final float LowerPanelPercentageHeight = 0.10f;
	private static final float InfoPanelPercentageHeight = 0.06f;
	private static final float MiddlePanelPercentageHeight = 0.59f;
	private static final float UpperPanelPercentageHeight = 0.25f;	
	
	private static final float UpperPanelLeftRightMarginsPercentage = 0.05f;
	private static final float PercentageBorder = 0.015f;
	
	private Image portraitImage;
	private Label nameLabel;
	private Label raceGenderLabel;	
	private LifePowerPointsControl lifePowerPointsControl;	
	private AttributesSkillsControl attributesSkills;	
	private ExtendedImageButton<Label> exitButton;
	private ActiveSpellsControl activeSpellsControl;
	
	private Rectangle portraitPanel;
	private Rectangle infoPanel;
	private Rectangle nameInfoPanel;
	private Rectangle middlePanel;
	private Rectangle lowerPanel;
	
	private Character character;
	private ArrayList<com.pberna.adventure.screens.ExitListener> exitListeners;

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
		refreshCharacterSheet();
	}
	
	public void addListener(com.pberna.adventure.screens.ExitListener exitListener) {
		exitListeners.add(exitListener);
	}
	
	public CharacterSheetScreen() {
		portraitPanel = new Rectangle();
		infoPanel = new Rectangle();
		nameInfoPanel = new Rectangle();
		middlePanel = new Rectangle();
		lowerPanel = new Rectangle();
		character = null;
		exitListeners = new ArrayList<com.pberna.adventure.screens.ExitListener>();
		
		buildStage();		
	}

	private void buildStage() {
		//name label title
		nameLabel = StageScreenHelper.createCenteredLabel(" ", InventoryScreenHelper.getNormalLabelStyle(), stage);
						
		//raceGenderLabel
		raceGenderLabel = StageScreenHelper.createCenteredLabel(" ", InventoryScreenHelper.getNormalLabelStyle(), stage);
					
		//life power table
		lifePowerPointsControl = new LifePowerPointsControl();
		stage.addActor(lifePowerPointsControl);		
		
		//attributes Skills info
		attributesSkills = new AttributesSkillsControl();
		stage.addActor(attributesSkills);
		
		//activeSpellsControl		
		activeSpellsControl = new ActiveSpellsControl(Gdx.graphics.getWidth() * PercentageBorder);
		stage.addActor(activeSpellsControl);
		
		//exit button
		exitButton = createExitButton();	
	}
	
	private ExtendedImageButton<Label> createExitButton() {
		ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().
				getTranslation("Common", "exitLabel"), stage, AssetRepository.getInstance().getSound("click_button"));
		button.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for(com.pberna.adventure.screens.ExitListener exitListener: exitListeners) {
					exitListener.exit(this);
				}
			}
		});
		return button;
	}

	private void refreshCharacterSheet() {
		if(character == null) {
			return;
		}
		
		//upper panel
		updatePortraitImage();
		updateNameLabel();
		updateRaceGenderLabel();
		updatePointsLabel();
		
		//middle panel
		attributesSkills.setModel(character);		
		activeSpellsControl.updateActiveSpells(character, false);
	}
	
	private void updatePortraitImage() {
		if(!character.getPortraitImageName().equals("") && character.getGender() != Gender.NotSet) {
			if(portraitImage != null) {
				portraitImage.remove();
			}
			
			String portraitAtlas = character.getGender() == Gender.Male 
					? PortraitsListControl.ManTextureAtlas 
					: PortraitsListControl.WomanTextureAtlas;
			portraitImage = new Image(AssetRepository.getInstance().getTextureRegion(
					portraitAtlas, character.getPortraitImageName()));
			stage.addActor(portraitImage);
		}		
	}
	
	private void updateNameLabel() {
		nameLabel.setText(character.getName());
	}
	
	private void updateRaceGenderLabel() {
		String labelText = "";
		if(character.getRace() != null) {
			if(character.getGender() == Gender.Male) {
				labelText = Localization.getInstance().getTranslation(
						"PjInformation", "man") + " " + character.getRace().getNameMale();
			} else if (character.getGender() == Gender.Female) {
				labelText = Localization.getInstance().getTranslation(
						"PjInformation", "woman")  + " " + character.getRace().getNameFemale();
			}
		}
		raceGenderLabel.setText(labelText);
	}

	private void updatePointsLabel() {
		lifePowerPointsControl.updatePointsLabel(character);		
	}
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
		updatePanelsSizes(width, height);
			
		//upper panel
		if(portraitImage != null) {
			PositionerHelper.setPositionCentered(portraitImage, portraitPanel);
		}
		PositionerHelper.setPositionCenteredHorizontalFillingColumn(new Actor[] {nameLabel, raceGenderLabel, lifePowerPointsControl}, 
				nameInfoPanel, VerticalSide.Top);
		
		//middle panel
		attributesSkills.setWidth(middlePanel.getWidth());
		attributesSkills.setHeight(middlePanel.getHeight());
		PositionerHelper.setPositionCentered(attributesSkills, middlePanel);
		
		//info panel
		PositionerHelper.setPositionCenteredVertical(activeSpellsControl, HorizontalSide.Left, 
				width * UpperPanelLeftRightMarginsPercentage / 2.0f, infoPanel);
		
		//button
		PositionerHelper.setPositionCenteredVerticalFillingRow(new Actor [] { exitButton }, lowerPanel ,HorizontalSide.Left);
		PositionerHelper.setPositionCenterInActor(exitButton, exitButton.getTag());		
	}
	
	private void updatePanelsSizes(float width, float height) {
		float lowerPanelHeight = height * LowerPanelPercentageHeight;
		float infoPanelHeight = height * InfoPanelPercentageHeight;
		float middlePanelHeight = height * MiddlePanelPercentageHeight;
		float upperPanelHeight = height * UpperPanelPercentageHeight;
		float upperPanelsWidth1 = width * ( 1f - (UpperPanelLeftRightMarginsPercentage * 2f)) * 0.35f;
		float upperPanelsWidth2 = width * ( 1f - (UpperPanelLeftRightMarginsPercentage * 2f)) * 0.65f;
		
		lowerPanel.set(0, 0, width, lowerPanelHeight);
		infoPanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, infoPanelHeight);
		middlePanel.set(0, infoPanel.getY() + infoPanel.getHeight(), width, middlePanelHeight);
		portraitPanel.set(width * UpperPanelLeftRightMarginsPercentage , middlePanel.getY() + middlePanel.getHeight(), 
				upperPanelsWidth1, upperPanelHeight);
		if(portraitImage != null) {
			nameInfoPanel.set(portraitPanel.getX() + portraitPanel.getWidth(), portraitPanel.getY() +
					((portraitPanel.getHeight() - portraitImage.getPrefHeight()) / 2f), upperPanelsWidth2, portraitImage.getHeight());
		} else {
			nameInfoPanel.set(portraitPanel.getX() + portraitPanel.getWidth(), portraitPanel.getY(),
					upperPanelsWidth2, upperPanelHeight);
		}
	}

	@Override
	public void refreshLocalizableItems() {
		attributesSkills.refreshLocalizableItems();	
		
		//exit button
		exitButton.remove();
		exitButton.getTag().remove();
		exitButton = createExitButton();
		
		activeSpellsControl.refreshLocalizableItems();
		
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());				
	}	
}
