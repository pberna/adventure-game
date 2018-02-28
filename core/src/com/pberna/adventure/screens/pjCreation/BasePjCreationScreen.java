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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.screens.controls.ScrollPaneIndicator;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;

public class BasePjCreationScreen extends BaseStageScreen implements ILocalizable{

	protected static final float PercentageBorderScreen = 0.03f;
	
	private ArrayList<CloseScreenListener> closeScreenListeners;
		
	private ExtendedImageButton<Label> cancelButton;
	private ExtendedImageButton<Label> acceptButton;	
	protected Label mainLabel;
	private Image selectedBorder;
	protected ScrollPane mainScrollPane;
	protected ScrollPaneIndicator paneIndicator;
	
	protected Rectangle mainPanel;	
	protected Rectangle textPanel;
	protected Rectangle lowerPanel;
		
	protected String mainText; 
	private String selectedBorderImageName;
	
	public BasePjCreationScreen(String mainText, String selectedBorderImageName)
	{
		setBackgroundTextureName("old_page_dark");
		this.mainText = mainText;
		this.selectedBorderImageName = selectedBorderImageName;
		closeScreenListeners = new ArrayList<CloseScreenListener>(); 
		
		mainPanel = new Rectangle();
		textPanel = new Rectangle();
		lowerPanel = new Rectangle();
				
		buildStage(false);
	}
	
	private void buildStage(boolean onlyLocalizableElements)
	{
		//cancel button		
		cancelButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().getTranslation(
				"PjCreation", "previousButtonLabel"), stage, AssetRepository.getInstance().getSound("click_button"));
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				notifyCancelToListeners();				
			}
		});
			
		//accept button
		acceptButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().getTranslation(
				"PjCreation", "nextButtonLabel"), stage, AssetRepository.getInstance().getSound("click_button"));
		acceptButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				notifyAcceptToListeners();				
			}
		});
				
		//main text
		mainLabel = new Label(mainText, StageScreenHelper.getLabelStyle(Color.BLACK));
		stage.addActor(mainLabel);
		
		if(!onlyLocalizableElements)
		{
			//selected border
			TextureRegion selectedBorderImage =  AssetRepository.getInstance().getTextureRegion("widgets", selectedBorderImageName);
			selectedBorder = new Image(selectedBorderImage);
			selectedBorder.setVisible(false);
			selectedBorder.setTouchable(Touchable.disabled);
			stage.addActor(selectedBorder);
		}	
	}
	
	protected void initializeMainScrollPane(Actor actorInScrollPane) {
		mainScrollPane = new ScrollPane(actorInScrollPane);
		mainScrollPane.setVelocityY(1);
		mainScrollPane.setScrollingDisabled(true, false);		
		stage.addActor(mainScrollPane);
		mainScrollPane.toBack();
		
		//paneIndicator		
		paneIndicator = new ScrollPaneIndicator(mainScrollPane, false);
	}
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
		updatePanelsSizes(width, height);
				
		//accept & cancel button 
		PositionerHelper.setPositionCenteredVerticalFillingRow(new Actor [] {cancelButton, acceptButton}, 
				lowerPanel, HorizontalSide.Left);
		PositionerHelper.setPositionCenterInActor(cancelButton, cancelButton.getTag());
		PositionerHelper.setPositionCenterInActor(acceptButton, acceptButton.getTag());
				
		//main text
		PositionerHelper.setPositionCentered(mainLabel, textPanel);	
		
		//scroll pane
		if(mainScrollPane != null) {
			mainScrollPane.setWidth(mainPanel.getWidth());
			mainScrollPane.setHeight(mainPanel.getHeight());
			PositionerHelper.setPositionFromCorner(mainScrollPane, Corner.TopLeft, 0, 0, mainPanel);
		}
		
		//scroll pane indicator
		float padding = width * PercentageBorderScreen;
		if(paneIndicator != null) {
			paneIndicator.updateIndicatorsPositions(padding, padding / 2.0f);
		}
	}
	
	private void updatePanelsSizes(float width, float height) {
		float lowerPanelHeight = height * LowerPanelPercentageHeight;

		lowerPanel.set(0, 0, width, lowerPanelHeight / 2);
		textPanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, lowerPanelHeight / 2);
		mainPanel.set(0, lowerPanelHeight, width, height - lowerPanelHeight + 1);				
	}
	
	public void addListener(CloseScreenListener closeScreenListener)
	{
		closeScreenListeners.add(closeScreenListener);
	}
	
	protected void notifyAcceptToListeners()
	{
		for(CloseScreenListener listener : closeScreenListeners)
		{
			listener.accept(this);
		}
	}
	
	protected void notifyCancelToListeners()
	{
		for(CloseScreenListener listener : closeScreenListeners)
		{
			listener.cancel(this);
		}
	}
	
	protected void setAcceptButtonVisibility(boolean visible) {
		acceptButton.setVisible(visible);
		acceptButton.getTag().setVisible(visible);
	}
	
	protected void setCancelButtonVisibility(boolean visible) {
		cancelButton.setVisible(visible);
		cancelButton.getTag().setVisible(visible);
	}
	
	protected void setSelectedBorderOnActor(Actor actor)
	{
		float xAdjust = (actor.getWidth() - selectedBorder.getWidth()) / 2.0f;
		float yAdjust = (actor.getHeight() - selectedBorder.getHeight()) / 2.0f;			
		selectedBorder.setPosition(actor.getX() + xAdjust, actor.getY() + yAdjust);
		setBorderSelectedVisibility(true);
	}
	
	protected void setBorderSelectedVisibility(boolean visible) {
		if(selectedBorder == null) {
			return;
		}
		selectedBorder.remove();
		stage.addActor(selectedBorder);
		selectedBorder.setVisible(visible);
	}

	@Override
	public void refreshLocalizableItems() {
		cancelButton.remove();
		cancelButton.getTag().remove();
		acceptButton.remove();	
		acceptButton.getTag().remove();
		mainLabel.remove();
		
		buildStage(true);
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void render(float delta) {		
		super.render(delta);
		
		if(paneIndicator != null) {
			paneIndicator.updateIndicatorsVisibility();
		}
	}
	
	@Override
	public void hide() {		
		super.hide();
		
		if(paneIndicator != null) {
			ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(paneIndicator);
		}
	}

	@Override
	public void show() {
		super.show();

		if(paneIndicator != null) {
			paneIndicator.refreshContinousRendering();
		}
	}
}
