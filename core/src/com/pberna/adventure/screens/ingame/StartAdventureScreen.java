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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pberna.adventure.adventure.Adventure;
import com.pberna.adventure.screens.*;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.engine.screens.controls.ScrollPaneIndicator;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.PositionerHelper;

public class StartAdventureScreen extends BaseStageScreen implements IModelEditingScreen<Adventure>, ILocalizable{
	private static final float LowerPanelPercentageHeight = 0.10f;
	private static final float MainPanelPercentageHeight = 0.90f;
	private static final float PercentageBorderScreen = 0.03f;

	private static final String ImageInitialTag = "<image>";
	private static final String ImageFinalTag = "</image>";

	private Table mainTable;
	private Label titleLabel;
	private Label authorLabel;
	private ArrayList<Label> descriptionLabels;
	private Image image;
	private ScrollPane mainScrollPane;
	private ScrollPaneIndicator paneIndicator;
	private ExtendedImageButton<Label> startAdventureButton;
	
	private Rectangle mainPanel;
	private Rectangle lowerPanel;
	private ArrayList<ExitListener> exitListeners;
	private Adventure adventure;
	
	public StartAdventureScreen() {
		setBackgroundTextureName("old_page");
		
		mainPanel = new Rectangle();
		lowerPanel = new Rectangle();
		exitListeners = new ArrayList<ExitListener>();
		adventure = null;
		descriptionLabels = new ArrayList<Label>();
		image = null;

		buildStage();
	}
	
	@Override
	public void setModel(Adventure model) {
		if(model == null) {
			return;
		}		
		adventure = model;
		
		//update controls
		titleLabel.setText("\n" + adventure.getTranslatedTitle());
		if(adventure.getAuthor() != null) {
			authorLabel.setText(String.format(Localization.getInstance().getTranslation("InGame", "adventureAuthorLabel", false), 
					adventure.getAuthor().getName()));
		} else {
			authorLabel.setText("");
		}
		refreshMainTable();

		mainScrollPane.setScrollPercentY(0);
	}

	private void clearDescriptionLabels() {
		for(Label descriptionLabel: descriptionLabels) {
			descriptionLabel.remove();
		}
		descriptionLabels.clear();
	}

	private void clearImage() {
		if(image != null) {
			image.remove();
			image = null;
		}
	}

	private void refreshMainTable() {
		titleLabel.remove();
		authorLabel.remove();
		clearDescriptionLabels();
		clearImage();
		mainTable.clearChildren();

		//title
		mainTable.add(titleLabel);
		mainTable.row();

		//author
		mainTable.add(authorLabel);
		mainTable.row();

		String translateDesccription = adventure.getTranslatedDescription().replace("\n", "\n\n");
		int imageInitialTagIndex = translateDesccription.indexOf(ImageInitialTag);
		int imageFinalTagIndex = translateDesccription.indexOf(ImageFinalTag);

		if(imageInitialTagIndex >= 0 && imageFinalTagIndex > 0 && imageInitialTagIndex < imageFinalTagIndex) {
			Label label1 = createDescriptionLabel();
			label1.setText("\n" +translateDesccription.substring(0, imageInitialTagIndex));
			descriptionLabels.add(label1);
			Label label2 = createDescriptionLabel();
			label2.setText("\n" +translateDesccription.substring(imageFinalTagIndex + ImageFinalTag.length(), translateDesccription.length()));
			descriptionLabels.add(label2);
			String regionName = translateDesccription.substring(imageInitialTagIndex, imageFinalTagIndex).
					replace(ImageInitialTag, "").replace(ImageFinalTag, "");
			image = new Image(AssetRepository.getInstance().getTextureRegion("illustrations", regionName));

			mainTable.add(label1);
			mainTable.row();
			mainTable.add(image);
			mainTable.row();
			mainTable.add(label2);

		} else {
			Label label = createDescriptionLabel();
			label.setText("\n" + translateDesccription);
			mainTable.add(label);
			descriptionLabels.add(label);
		}
	}
	
	@Override
	public Adventure getModel() {
		return adventure;
	}
	
	public void addListener(ExitListener exitListener) {
		exitListeners.add(exitListener);
	}
	
	private void buildStage() {
		//main vertical group
		mainTable = new Table();
		
		//title
		titleLabel = createTitleLabel();

		//author
		authorLabel = createAuthorLabel();

		//main scroll pane
		mainScrollPane = new ScrollPane(mainTable);
		mainScrollPane.setVelocityY(1);
		mainScrollPane.setScrollingDisabled(true, false);		
		stage.addActor(mainScrollPane);
		
		//paneIndicator
		paneIndicator = new ScrollPaneIndicator(mainScrollPane, false);
		
		//exit button
		startAdventureButton = createStartAdventureButton();
	}
	
	private ExtendedImageButton<Label> createStartAdventureButton() {
		ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().
				getTranslation("InGame", "startAdventureLabel"), stage, AssetRepository.getInstance().getSound("click_button"));
		button.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for(ExitListener exitListener: exitListeners) {
					exitListener.exit(this);
				}
			}
		});	
		return button;
	}
	
	private Label createTitleLabel() {
		Label label = new Label("", StageScreenHelper.getLabelStyle(Color.BLACK));
		label.setAlignment(Align.center);
		label.setWrap(true);
		return label;
	}
	
	private Label createAuthorLabel() {		
		Label label = new Label("", new Label.LabelStyle(FontHelper.getOptionsTextFont(), Color.BLACK));
		label.setAlignment(Align.center);
		label.setWrap(true);
		return label;
	}
	
	private Label createDescriptionLabel() {		
		Label label = new Label("", new Label.LabelStyle(FontHelper.getReadTextFont(), Color.BLACK));
		label.setAlignment(Align.top | Align.left);
		label.setWrap(true);
		return label;
	}

	private void setTitleWidth(float width) {
		titleLabel.setWidth(width);
		Cell<Label> cell = mainTable.getCell(titleLabel);
		if(cell != null) {
			cell.width(width);
		}
	}

	private void setAuthorWidth(float width) {
		authorLabel.setWidth(width);
		Cell<Label> cell = mainTable.getCell(authorLabel);
		if(cell != null) {
			cell.width(width);
		}
	}
	
	private void setDescriptionWidth(float width) {
		for(Label descriptionLabel: descriptionLabels) {
			descriptionLabel.setWidth(width);
			Cell<Label> cell = mainTable.getCell(descriptionLabel);
			if (cell != null) {
				cell.width(width);
			}
		}
	}
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
		updatePanelsSizes(width, height);
		float padding = width * PercentageBorderScreen;
		float labelsWidth = mainPanel.getWidth() - (padding * 2.0f);
		
		//adventure data & main scroll pane
		mainTable.setWidth(width);
		setTitleWidth(labelsWidth);
		setAuthorWidth(labelsWidth);
		setDescriptionWidth(labelsWidth);
		mainScrollPane.setWidth(mainPanel.getWidth());
		mainScrollPane.setHeight(mainPanel.getHeight());
		PositionerHelper.setPositionFromCorner(mainScrollPane, Corner.TopLeft, 0, 0, mainPanel);
		paneIndicator.updateIndicatorsPositions(padding, padding / 2.0f);
		
		//start adventure button
		PositionerHelper.setPositionCentered(startAdventureButton, lowerPanel);
		PositionerHelper.setPositionCenterInActor(startAdventureButton, startAdventureButton.getTag());
	}
	
	private void updatePanelsSizes(float width, float height) {
		float lowerPanelHeight = height * LowerPanelPercentageHeight;
		float mainPanelHeight = height * MainPanelPercentageHeight;
		
		lowerPanel.set(0, 0, width, lowerPanelHeight);		
		mainPanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, mainPanelHeight);				
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
		
		ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(paneIndicator);
	}

	@Override
	public void show() {
		super.show();

		if(paneIndicator != null) {
			paneIndicator.refreshContinousRendering();
		}
	}

	@Override
	public void refreshLocalizableItems() {
		//startAdventureButton
		startAdventureButton.remove();
		startAdventureButton.getTag().remove();
		startAdventureButton = createStartAdventureButton();
		
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());		
	}
}
