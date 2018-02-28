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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pberna.adventure.pj.Gender;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;

public class SelectGenderScreen extends BasePjCreationScreen
	implements IModelEditingScreen<Gender>{
	
	private Gender gender;

	private ImageButton femaleButton;
	private ImageButton maleButton;	
	private Label femaleLabel;
	private Label maleLabel;
	
	public SelectGenderScreen()
	{				
		super(Localization.getInstance().getTranslation(
				"PjCreation", "chooseGender"), "selected_border");
		setModel(Gender.NotSet);
		buildStage(false);		
	}
	
	@Override
	public void setModel(Gender model) {
		this.gender = model;
		setAcceptButtonVisibility(this.gender != Gender.NotSet);
		refreshSelectedBorder();		
	}

	@Override
	public Gender getModel() {
		return gender;
	}	
	
	private void buildStage(boolean onlyLocalizableElements) {
		
		if(!onlyLocalizableElements) {
			//female button
			TextureRegion femaleButtonImage = AssetRepository.getInstance().getTextureRegion("widgets", "female_button");
			femaleButton = new ImageButton(new TextureRegionDrawable(femaleButtonImage));
			StageScreenHelper.addOnClickSound(femaleButton, AssetRepository.getInstance().getSound("click_button"));
			femaleButton.addListener(new ChangeListener() {			
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(getModel() != Gender.Female) {
						setModel(Gender.Female);
					} else {
						setModel(Gender.NotSet);
					}
				}
			});
			stage.addActor(femaleButton);
		}
		
		//female text
		femaleLabel = new Label(Localization.getInstance().getTranslation(
				"PjCreation", "female"), StageScreenHelper.getLabelStyle(Color.BLACK));
		stage.addActor(femaleLabel);
		
		//male button
		if(!onlyLocalizableElements) {
			TextureRegion maleButtonImage = AssetRepository.getInstance().getTextureRegion("widgets", "male_button");
			maleButton = new ImageButton(new TextureRegionDrawable(maleButtonImage));
			StageScreenHelper.addOnClickSound(maleButton, AssetRepository.getInstance().getSound("click_button"));
			maleButton.addListener(new ChangeListener() {			
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(getModel() != Gender.Male) {
						setModel(Gender.Male);
					} else {
						setModel(Gender.NotSet);
					}
				}
			});
			stage.addActor(maleButton);
		}
		
		//male text
		maleLabel = new Label(Localization.getInstance().getTranslation(
				"PjCreation", "male"), StageScreenHelper.getLabelStyle(Color.BLACK));
		stage.addActor(maleLabel);		
	}	
	
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
		
		float maleFemaleButtonGap = (mainPanel.height - femaleButton.getHeight() - maleButton.getHeight()) / 3.0f; 
				
		//female button and text
		PositionerHelper.setPositionCenteredHorizontal(femaleButton, VerticalSide.Top, maleFemaleButtonGap, mainPanel);
		PositionerHelper.setPositionCenteredHorizontal(femaleLabel, VerticalSide.Bottom, femaleButton.getY() - femaleLabel.getHeight() - 5 , stage);				
		
		//male button and text
		PositionerHelper.setPositionCenteredHorizontal(maleButton, VerticalSide.Bottom, maleFemaleButtonGap, mainPanel);
		PositionerHelper.setPositionCenteredHorizontal(maleLabel, VerticalSide.Bottom, maleButton.getY() - maleLabel.getHeight() - 5 , stage);
		
		//selected border
		refreshSelectedBorder();
	}	
	
	private void refreshSelectedBorder()
	{		
		if(gender == Gender.Male) {
			setSelectedBorderOnActor(maleButton);
			return;
		}
		if(gender == Gender.Female) {
			setSelectedBorderOnActor(femaleButton);
			return;
		}
		setBorderSelectedVisibility(false);
	}	
	
	@Override
	public void refreshLocalizableItems() {
		femaleLabel.remove();
		maleLabel.remove();
		mainText = Localization.getInstance().getTranslation("PjCreation", "chooseGender");
		
		buildStage(true);
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		super.refreshLocalizableItems();
	}
}
