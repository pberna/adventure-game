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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Gender;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.adventure.screens.controls.LifePowerPointsControl;
import com.pberna.adventure.screens.controls.PortraitsListControl;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;

public class SelectNameScreen extends BasePjCreationScreen
implements IModelEditingScreen<Character>{

	private Character character;
	private ArrayList<Attribute> listAttributes;
	private ArrayList<Skill> listSkills;
	
	private TextField nameTextField;
	private Image backGroundInputTextImage;
	private Image portraitImage;
	private Label raceGenderLabel;
	private Label nameLabel;
	private Label attributesTitleLabel;
	private Label attributesTitleDecorationLabel;
	private Stack stackAttributesLabel;
	private Label skillsTitleLabel;
	private Label skillsTitleDecorationLabel;
	private Stack stackSkillsLabel;
	private Image [] attributeImages;
	private Label [] attributeLabels;
	private Image [] skillImages;
	private Label [] skillLabels;
	private Image spellsImage;
	private Label spellsLabel;
	private LifePowerPointsControl lifePowerPoints;
	
	private Rectangle portraitPanel;
	private Rectangle attributesPanel;
	private Rectangle skillsPanel;
	private Rectangle namePanel;
	
	public SelectNameScreen() {
		super(Localization.getInstance().getTranslation(
				"PjCreation", "chooseCharacterName"), "selected_border");
		
		character = null;
		listAttributes = Attribute.getAttributes();
		listSkills = Skill.getSkills();
		
		portraitImage = null;
		portraitPanel = new Rectangle();
		attributesPanel = new Rectangle();
		skillsPanel = new Rectangle();
		namePanel = new Rectangle();
				
		buildStage();
	}

	@Override
	public void setModel(Character model) {		
		character = model;
		nameTextField.setText(character.getName());
		spellsLabel.setText(Localization.getInstance().getTranslation("PjCreation", "spells") +
				"   "  + String.valueOf(character.getSpells().size()));
		lifePowerPoints.updatePointsLabel(character);
		setPortraitImage();
		setRaceGenderLabel();
		setAttributeLabels();
		setSkillLabels();
		updateAcceptButtonVisibility();
	}

	@Override
	public Character getModel() {
		return character;
	}
		
	private void buildStage() {
		
		//backGroundInputTextImage
		backGroundInputTextImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "input_text_box"));
		stage.addActor(backGroundInputTextImage);
		
		//text field
		nameTextField = new TextField("", getNameTextFieldStyle());
		nameTextField.setMaxLength(Character.MaxCharacterNameLength);
		nameTextField.setWidth(backGroundInputTextImage.getWidth() * 0.9f);
		nameTextField.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				nameTextField.setText("");
				if (character != null) {
					character.setName(nameTextField.getText());
				}
				updateAcceptButtonVisibility();
			}
		});
		nameTextField.setTextFieldListener(new TextField.TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				if (character != null) {
					character.setName(nameTextField.getText());
				}
				updateAcceptButtonVisibility();
			}
		});
		nameTextField.setAlignment(Align.center);
				
		stage.addActor(nameTextField);
		
		//create portrait image
		setPortraitImage();
		
		//raceGenderLabel
		raceGenderLabel = new Label("", getLabelStyle());
		raceGenderLabel.setAlignment(Align.center);
		stage.addActor(raceGenderLabel);
		setRaceGenderLabel();
		
		//attributes label
		attributesTitleLabel = StageScreenHelper.createCenteredLabel(Localization.getInstance().
				getTranslation("PjInformation", "attributes"), getLabelStyle());
		attributesTitleDecorationLabel = StageScreenHelper.createCenteredLabel(StageScreenHelper.getDecorationText(
				attributesTitleLabel.getText().length + 3), StageScreenHelper.getDecorationLabelStyle());
		stackAttributesLabel = StageScreenHelper.createStackWithActors(attributesTitleLabel, attributesTitleDecorationLabel);
		stage.addActor(stackAttributesLabel);
		
		//skills title label
		skillsTitleLabel = StageScreenHelper.createCenteredLabel(Localization.getInstance().
				getTranslation("PjInformation", "skills"), getLabelStyle());
		skillsTitleDecorationLabel = StageScreenHelper.createCenteredLabel(StageScreenHelper.getDecorationText(
				skillsTitleLabel.getText().length + 2), StageScreenHelper.getDecorationLabelStyle());
		stackSkillsLabel = StageScreenHelper.createStackWithActors(skillsTitleLabel, skillsTitleDecorationLabel);
		stage.addActor(stackSkillsLabel);

		//attributeImages
		attributeImages = new Image[listAttributes.size()];
		for(int i = 0; i< listAttributes.size(); i++) {
			attributeImages[i] = new Image(AssetRepository.getInstance().
					getTextureRegion("attribute_skills", listAttributes.get(i).getImageName()));
			stage.addActor(attributeImages[i]);
		}

		//attributes labels
		attributeLabels = new Label[listAttributes.size()];
		for(int i = 0; i < listAttributes.size(); i++) {
			attributeLabels[i] = new Label("  " + listAttributes.get(i).getName() + "  " + listAttributes.get(i).getValue(), getAttributeSkillLabelStyle());
			attributeLabels[i].setAlignment(Align.center);
			stage.addActor(attributeLabels[i]);
		}

		// skillImages;
		skillImages = new Image[listSkills.size()];
		for(int i = 0; i < listSkills.size(); i++) {
			skillImages[i] = new Image(AssetRepository.getInstance().
					getTextureRegion("attribute_skills", listSkills.get(i).getImageName()));
			stage.addActor(skillImages[i]);
		}

		//skill labels
		skillLabels = new Label[listSkills.size()];
		for(int i = 0; i < listSkills.size(); i++) {
			skillLabels[i] = new Label("  " + listSkills.get(i).getName() + "  " + listSkills.get(i).getValue(), getAttributeSkillLabelStyle());
			skillLabels[i].setAlignment(Align.center);
			stage.addActor(skillLabels[i]);
		}
		
		//name label title
		nameLabel = new Label(Localization.getInstance().getTranslation("PjInformation", "name"), getLabelStyle());
		nameLabel.setAlignment(Align.center | Align.top);
		stage.addActor(nameLabel);

		//spellsImage
		spellsImage = new Image(AssetRepository.getInstance().getTextureRegion("attribute_skills", "spells"));
		stage.addActor(spellsImage);

		//spells label
		spellsLabel = new Label(Localization.getInstance().getTranslation("PjCreation", "spells") + "   0", getLabelStyle());
		spellsLabel.setAlignment(Align.center);
		stage.addActor(spellsLabel);
		
		//life points label
		lifePowerPoints = new LifePowerPointsControl();
		stage.addActor(lifePowerPoints);		
	}	

	private TextField.TextFieldStyle getNameTextFieldStyle(){
		Image cursorImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "cursor_text_box"));
		return new TextField.TextFieldStyle(FontHelper.getLabelsFont(),	Color.BLACK, cursorImage.getDrawable(), null, null);
	}
	
	private void setPortraitImage() {
		if(this.character != null && this.character.getPortraitImageName() != "" && this.character.getGender() != Gender.NotSet) {
			if(portraitImage != null) {
				portraitImage.remove();
			}
			
			String portraitAtlas = this.character.getGender() == Gender.Male 
					? PortraitsListControl.ManTextureAtlas 
					: PortraitsListControl.WomanTextureAtlas;
			portraitImage = new Image(AssetRepository.getInstance().getTextureRegion(
					portraitAtlas, this.character.getPortraitImageName()));
			stage.addActor(portraitImage);
		}		
	}
	
	private Label.LabelStyle getLabelStyle() {
		return StageScreenHelper.getLabelStyle(Color.BLACK);
	}
	
	private Label.LabelStyle getAttributeSkillLabelStyle() {
		return StageScreenHelper.getLabelStyle(Color.BLACK);
	}
	
	private void setRaceGenderLabel() {
		String labelText = "";
		if(character != null && character.getRace() != null) {
			if(character.getGender() == Gender.Male) {
				labelText = Localization.getInstance().getTranslation(
						"PjInformation", "man") + "   " + character.getRace().getNameMale();
			} else if (character.getGender() == Gender.Female) {
				labelText = Localization.getInstance().getTranslation(
						"PjInformation", "woman")  + "   " + character.getRace().getNameFemale();
			}
		}
		raceGenderLabel.setText(labelText);
	}
	
	private void setAttributeLabels()
	{
		if(character != null) {
			for(int i = 0; i < listAttributes.size(); i++)
			{
				int attributeValue = character.getBaseAttributeValue(listAttributes.get(i).getId());
				attributeLabels[i].setText("  " + listAttributes.get(i).getName() + "  " + attributeValue);
			}
		}
	}
	
	private void setSkillLabels()
	{
		if(character != null) {
			for(int i = 0; i < listSkills.size(); i++)
			{
				int skillValue = character.getBaseSkillValue(listSkills.get(i).getId());
				skillLabels[i].setText("  " + listSkills.get(i).getName() + "  " + skillValue);
			}
		}
	}
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
		
		updatePanelsSizes();
		
		//portrait
		Actor [] portraitPanelActors = null;
		if(portraitImage != null) {
			portraitPanelActors = new Actor[] { portraitImage, raceGenderLabel, lifePowerPoints};
		}
		else {
			portraitPanelActors = new Actor[] { raceGenderLabel, lifePowerPoints};
		}
		PositionerHelper.setPositionCenteredHorizontalFillingColumn(portraitPanelActors, portraitPanel, VerticalSide.Top);
						
		//attributes panel
		float leftPanelGapSize = 10;
		if(attributeLabels.length > 0) {
			int numLabels = Math.max(attributeLabels.length, skillLabels.length);
			leftPanelGapSize = (attributesPanel.getHeight() - (numLabels * attributeLabels[0].getHeight()) - attributesTitleLabel.getHeight() ) 
					/ (numLabels + 2);
			
			PositionerHelper.setPositionCenteredHorizontalFillingColumn(getLabelsList(stackAttributesLabel, attributeLabels),
					attributesPanel, VerticalSide.Top, leftPanelGapSize);

			for(int i = 0; i < attributeLabels.length && i < attributeImages.length; i++) {
				attributeImages[i].setPosition(attributeLabels[i].getX() - attributeImages[i].getWidth() / 2f,
						attributeLabels[i].getY() - (attributeImages[i].getHeight() / 5f));
				attributeLabels[i].setX(attributeLabels[i].getX() + attributeImages[i].getWidth() / 2f);
			}
			stackAttributesLabel.setX(stackAttributesLabel.getX() + attributeImages[0].getWidth() / 2f);
			
		} else {
			PositionerHelper.setPositionCenteredHorizontal(attributesTitleLabel, VerticalSide.Top, 15, attributesPanel);
		}
				
		//skills panel
		if(skillLabels.length > 0) {
			int numLabels = Math.max(attributeLabels.length, skillLabels.length);
			float rightPanelGapSize = (skillsPanel.getHeight() - (numLabels * skillLabels[0].getHeight()) - skillsTitleLabel.getHeight() ) 
					/ (numLabels + 2);
			
			PositionerHelper.setPositionCenteredHorizontalFillingColumn( getLabelsList(stackSkillsLabel, skillLabels),
					skillsPanel, VerticalSide.Top, rightPanelGapSize);

			for(int i = 0; i < skillLabels.length && i < skillImages.length; i++) {
				skillImages[i].setPosition(skillLabels[i].getX() - skillImages[i].getWidth() / 2f,
						skillLabels[i].getY() - (skillImages[i].getHeight() / 5f));
				skillLabels[i].setX(skillLabels[i].getX() + skillImages[i].getWidth() / 2f);
			}
			stackSkillsLabel.setX(stackSkillsLabel.getX() + skillImages[0].getWidth() / 2f);
			
		} else {
			PositionerHelper.setPositionCenteredHorizontal(skillsTitleLabel, VerticalSide.Top, 15, skillsPanel);
		}		
		
		//name panel
		//float nameGapSize = (namePanel.getHeight() - nameLabel.getHeight() - backGroundInputTextImage.getHeight()) / 3.0f;
		PositionerHelper.setPositionCenteredHorizontalFillingColumn( new Actor[] { nameLabel, backGroundInputTextImage }, 
				namePanel, VerticalSide.Top);		
		
		PositionerHelper.setPositionCenterInActor(backGroundInputTextImage, nameTextField);
		
		//spells labels and values
		Label labelReference = (attributeLabels.length > 1) ? attributeLabels[attributeLabels.length - 2] : attributesTitleLabel;
		spellsLabel.setPosition(labelReference.getX(), labelReference.getY() - 2 * (leftPanelGapSize + spellsLabel.getHeight()));
		spellsImage.setPosition(spellsLabel.getX() - spellsImage.getWidth() / 2f,
				spellsLabel.getY() - (spellsImage.getHeight() / 5f));
		spellsLabel.setX(spellsLabel.getX() + spellsImage.getWidth() / 2f);
	}
	
	private Actor [] getLabelsList(Actor titleLabel, Label [] otherLabels) {
		Actor [] actors = new Actor[otherLabels.length + 1];
		actors[0] = titleLabel;
		for(int i = 0; i < otherLabels.length; i++) {
			actors[i + 1] = otherLabels[i];
		}
		return actors;
	}
	
	private void updatePanelsSizes() {
		attributesPanel.set(mainPanel.getX(), mainPanel.getY(), mainPanel.getWidth() * 0.5f, mainPanel.getHeight() * 0.45f);
		skillsPanel.set(attributesPanel.getX() + attributesPanel.getWidth() + 1, mainPanel.getY(), 
				mainPanel.getWidth() * 0.5f, mainPanel.getHeight() * 0.45f);
		portraitPanel.set(mainPanel.getX(), attributesPanel.getY() + attributesPanel.getHeight() + 1, 
				mainPanel.getWidth(), mainPanel.getHeight() * 0.40f);		
		namePanel.set(mainPanel.getX(), portraitPanel.getY() + portraitPanel.getHeight() + 1, 
				mainPanel.getWidth(), mainPanel.getHeight() * 0.15f );	
	}
	
	@Override
	public void refreshLocalizableItems() {
		mainText = Localization.getInstance().getTranslation("PjCreation", "chooseCharacterName");
		attributesTitleLabel.setText(Localization.getInstance().getTranslation("PjInformation", "attributes"));
		attributesTitleDecorationLabel.setText(StageScreenHelper.getDecorationText(attributesTitleLabel.getText().length + 3));
		skillsTitleLabel.setText(Localization.getInstance().getTranslation("PjInformation", "skills"));
		skillsTitleDecorationLabel.setText(StageScreenHelper.getDecorationText(skillsTitleLabel.getText().length + 2));
		nameLabel.setText(Localization.getInstance().getTranslation("PjInformation", "name"));
		spellsLabel.setText(Localization.getInstance().getTranslation("PjCreation", "spells") + "   " +
				(character != null ? character.getSpells().size() : "0"));
		if(character != null) {
			lifePowerPoints.updatePointsLabel(character);
		}		
		
		listAttributes = Attribute.getAttributes();
		listSkills = Skill.getSkills();
		setRaceGenderLabel();
		setAttributeLabels();
		setSkillLabels();
				
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());		
		super.refreshLocalizableItems();
		
		updateAcceptButtonVisibility();
	}
	
	private void updateAcceptButtonVisibility()
	{		
		/*if(nameTextField != null && nameTextField.getText().trim().length() > 0) {
			setAcceptButtonVisibility(true);
		} else {
			setAcceptButtonVisibility(false);
		}*/
		setAcceptButtonVisibility(true);
	}

	public void promptForCharacterName() {
		Gdx.input.getTextInput(
				new Input.TextInputListener() {
					@Override
					public void input(String text) {
						nameTextField.setText(text);
						if (character != null) {
							character.setName(nameTextField.getText().trim());
						}
						notifyAcceptToListeners();
					}

					@Override
					public void canceled() {

					}
				}, Localization.getInstance().getTranslation("PjCreation", "promptPjNameTitle"),
				Localization.getInstance().getTranslation("PjCreation", "prompPjDefaultName"), "");
	}
}
