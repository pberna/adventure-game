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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pberna.adventure.pj.race.BaseRace;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;

public class SelectRaceScreen extends BasePjCreationScreen
implements IModelEditingScreen<BaseRace>{

	private ExtendedImageButton<Integer> [] raceImageButtons;
	private Label [] raceLabels;
	private Label descriptionLabel;
	
	ArrayList<BaseRace> listRaces;
	private int racesCount;
	private int selectedRaceIndex;
	
	public SelectRaceScreen() {
		super(Localization.getInstance().getTranslation(
				"PjCreation", "chooseRace"), "selected_border");
		
		listRaces = BaseRace.getAvailableRaces();
		racesCount = listRaces.size();
		setSelectedRaceIndex(-1);		
		
		buildStage();
	}
	
	@Override
	public void setModel(BaseRace model) {
		if(model != null) {
			for(int i = 0; i < racesCount; i++) {
				if(listRaces.get(i).getId() == model.getId()) {
					setSelectedRaceIndex(i);
					return;
				}
			}
		}
		setSelectedRaceIndex(-1);
	}

	@Override
	public BaseRace getModel() {
		if(selectedRaceIndex >= 0 && selectedRaceIndex <= racesCount) {
			return listRaces.get(selectedRaceIndex);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void buildStage() {			
		raceImageButtons = new ExtendedImageButton[racesCount];
		raceLabels = new Label[racesCount];
		
		for(int i = 0; i < racesCount; i++){					
			TextureRegion raceTexture = AssetRepository.getInstance().getTextureRegion(
					"widgets", listRaces.get(i).getImageName());
			raceImageButtons[i] = new ExtendedImageButton<Integer>(new TextureRegionDrawable(raceTexture));
			raceImageButtons[i].setTag(new Integer(i));
			StageScreenHelper.addOnClickSound(raceImageButtons[i], AssetRepository.getInstance().getSound("click_button"));
			raceImageButtons[i].addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					ExtendedImageButton<Integer> portraitImageButton = (ExtendedImageButton<Integer>) actor;
					if (selectedRaceIndex != portraitImageButton.getTag().intValue()) {
						setSelectedRaceIndex(portraitImageButton.getTag());
					} else {
						setSelectedRaceIndex(-1);
					}
				}
			});
			stage.addActor(raceImageButtons[i]);
			
			raceLabels[i] = new Label(listRaces.get(i).getName(),
					StageScreenHelper.getLabelStyle(Color.BLACK));
			stage.addActor(raceLabels[i]);
		}
		
		descriptionLabel = createDescriptionLabel();
		stage.addActor(descriptionLabel);
		descriptionLabel.setText("");
	}
		
	private Label createDescriptionLabel() {
		Label label = new Label("Text enough long to fix the bug with the width of a label",
				StageScreenHelper.getLabelStyle(Color.BLACK));
		label.setAlignment(Align.center);
		return label;
	}

	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
					
		//races
		if(raceImageButtons.length > 0)
		{
			int portraitWidth = (int)raceImageButtons[0].getWidth();
			int portraitHeight = (int)raceImageButtons[0].getHeight();
			int xGap = (int)((mainPanel.getWidth() - (portraitWidth * 2)) / 3.0);
			int rows = 2;
			int yGap = (int)((mainPanel.getHeight() - (portraitHeight * rows)) / (rows + 1));
			
			int x = 0;
			int y = yGap;
			for(int i = 0; i < raceImageButtons.length; i++) {				
				if(i % 2 == 0){
					x = xGap;
					if(i > 0) {
						y += yGap + portraitHeight;
					}
				} else {
					x = (xGap * 2) + portraitWidth;
				}
				
				PositionerHelper.setPositionFromCorner(raceImageButtons[i], 
						Corner.TopLeft, x, y, mainPanel);
				
				raceLabels[i].setPosition(x + (raceImageButtons[i].getWidth() - raceLabels[i].getWidth()) / 2, 
						raceImageButtons[i].getY() - raceLabels[i].getHeight() - 5 );						
			}
			//description label
			float yDescription = (raceLabels[raceImageButtons.length - 1].getY() + mainLabel.getY()) / 2.0f;
			PositionerHelper.setPositionCenteredHorizontal(descriptionLabel, VerticalSide.Bottom, yDescription, stage);
		}		
	}
	
	private void setSelectedRaceIndex(int selectedRaceIndex) {
		this.selectedRaceIndex = selectedRaceIndex;
		
		if(this.selectedRaceIndex < 0 || this.selectedRaceIndex > (racesCount - 1))
		{			
			if(descriptionLabel != null) {
				descriptionLabel.setText("");				
			}
			setAcceptButtonVisibility(false);
		}
		else
		{
			if(descriptionLabel != null) {
				descriptionLabel.setText(listRaces.get(this.selectedRaceIndex).getDescription());
			}
			setAcceptButtonVisibility(true);
		}
		refreshSelectedBorder();
	}	
	
	private void refreshSelectedBorder()
	{		
		if(raceImageButtons != null && selectedRaceIndex >= 0 && selectedRaceIndex < raceImageButtons.length)
		{
			setSelectedBorderOnActor(raceImageButtons[selectedRaceIndex]);
			return;
		}
		
		setBorderSelectedVisibility(false);
	}
	
	@Override
	public void refreshLocalizableItems() {		
		mainText = Localization.getInstance().getTranslation("PjCreation", "chooseRace");
		for(int i = 0; i < listRaces.size(); i++) {
			raceLabels[i].setText(listRaces.get(i).getName());
		}
		
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());		
		super.refreshLocalizableItems();
	}
}
