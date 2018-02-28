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
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pberna.adventure.items.Item;
import com.pberna.adventure.items.ItemEquipable;
import com.pberna.adventure.items.ItemUsable;
import com.pberna.adventure.pj.Backpack;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.EEquipmentPosition;
import com.pberna.adventure.pj.Gender;
import com.pberna.adventure.screens.*;
import com.pberna.adventure.screens.controls.backpack.BackpackControl;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.adventure.screens.controls.LifePowerPointsControl;
import com.pberna.adventure.screens.controls.PortraitsListControl;
import com.pberna.engine.screens.controls.ScrollPaneIndicator;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;

public class InventoryScreen extends BaseStageScreen 
	implements IModelEditingScreen<Character>, ILocalizable{

	private static final float LowerPanelPercentageHeight = 0.10f;
	private static final float MessagePanelPercentageHeight = 0.06f;
	private static final float CenterPanelPercentageHeight = 0.62f;
	private static final float UpperPanelPercentageHeight = 1.0f - CenterPanelPercentageHeight - 
			MessagePanelPercentageHeight - LowerPanelPercentageHeight;
	
	private static final float UpperPanelLeftRightMarginsPercentage = 0.05f;
	private static final float PercentageBorderScreen = 0.03f;
	
	private Character character;
	private ArrayList<com.pberna.adventure.screens.ExitListener> listeners;
	private ExtendedImageButton<Item> selectedItemButton;
	
	public enum InventoryScreenMode {
		Adventure,
		Combat
	}
	private InventoryScreenMode screenMode;
	private boolean itemAlreadyUsed;	
		
	private Image portraitImage;
	private Label nameLabel;
	private Label raceGenderLabel;	
	private LifePowerPointsControl lifePowerPointsControl;
	
	private Image equippedGrid;
	private HashMap<EEquipmentPosition, Image> equipmentPositionImages;
	private ArrayList<ExtendedImageButton<Item>> backpackItemsButtons;
	private ArrayList<ExtendedImageButton<Item>> equipmentItemsButtons;
	private ScrollPane backpackScrollPane;
	private ScrollPaneIndicator scrollPaneIndicator;
	private BackpackControl backpackControl;
	private Image backpackFrame;
	private Image selectedBorder;
	private Image selectedBorderRing;
	private ExtendedImageButton<Label> useItemButton;
	private ExtendedImageButton<Label> equipItemButton;
	private ExtendedImageButton<Label> unEquipItemButton;
	private ExtendedImageButton<Label> exitScreenButton;
	private Label itemInfoLabel;
	private Label itemEffectLabel;
	private Label errorMessageLabel;
		
	private Rectangle upperPanel;
	private Rectangle portraitPanel;
	private Rectangle upperLabelsPanel;
	private Rectangle centerPanel;
	private Rectangle messagePanel;
	private Rectangle lowerPanel;
	private float backpackWidth;
	private float backpackHeight;

	public InventoryScreen() {
		character = null;
		listeners = new ArrayList<com.pberna.adventure.screens.ExitListener>();
		selectedItemButton = null;
		screenMode = InventoryScreenMode.Adventure;
		itemAlreadyUsed = true;
		
		upperPanel = new Rectangle();
		portraitPanel = new Rectangle();
		upperLabelsPanel = new Rectangle();
		centerPanel = new Rectangle();	
		messagePanel = new Rectangle();
		lowerPanel = new Rectangle();

		buildStage();
	}
	
	public void addListener(com.pberna.adventure.screens.ExitListener listener) {
		listeners.add(listener);
	}
	
	private void buildStage() {
		//name label title
		nameLabel = StageScreenHelper.createCenteredLabel(" ", InventoryScreenHelper.getNormalLabelStyle(), stage);
				
		//raceGenderLabel
		raceGenderLabel = StageScreenHelper.createCenteredLabel(" ", InventoryScreenHelper.getNormalLabelStyle(), stage);
			
		//life power table
		lifePowerPointsControl = new LifePowerPointsControl();
		stage.addActor(lifePowerPointsControl);
		
		//equipped grid
		equippedGrid = new Image(AssetRepository.getInstance().getTextureRegion("inventory", "equipped"));
		stage.addActor(equippedGrid);

		//backpack items buttons
		backpackItemsButtons = new ArrayList<ExtendedImageButton<Item>>();
		
		//equipmentItemsButtons
		equipmentItemsButtons = new ArrayList<ExtendedImageButton<Item>>();
		
		//selectedBorder		
		selectedBorder = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "selected_border_item"));
		selectedBorder.setVisible(false);
		stage.addActor(selectedBorder);

		//selectedBorderRing
		selectedBorderRing = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "selected_border_item"));
		selectedBorderRing.setVisible(false);
		selectedBorderRing.setSize(selectedBorder.getWidth() / 2f, selectedBorder.getHeight() / 2f);
		stage.addActor(selectedBorderRing);

		//equipment position images
		equipmentPositionImages = new HashMap<EEquipmentPosition, Image>(EEquipmentPosition.values().length);
		for(EEquipmentPosition equipmentPosition: EEquipmentPosition.values()){
			Image image = new Image(InventoryScreenHelper.getEquipmentPositionImage(character, equipmentPosition));
			equipmentPositionImages.put(equipmentPosition, image);
			stage.addActor(image);
		}

		//backpackControl
		backpackControl = new BackpackControl();
		backpackWidth = backpackControl.getWidth();
		backpackHeight = backpackControl.getHeight();
		backpackScrollPane = new ScrollPane(backpackControl);
		backpackScrollPane.setVelocityX(1);
		backpackScrollPane.setScrollingDisabled(false, true);
		stage.addActor(backpackScrollPane);

		//backpackFrame
		backpackFrame = new Image(AssetRepository.getInstance().getTextureRegion("inventory","backpack_frame"));
		backpackFrame.setTouchable(Touchable.disabled);
		backpackFrame.setSize(backpackWidth + 2, backpackHeight + 6);
		stage.addActor(backpackFrame);

		//scrollpane indicator
		scrollPaneIndicator = new ScrollPaneIndicator(backpackScrollPane, true);
		scrollPaneIndicator.setScrollIncrement(backpackWidth / BackpackControl.MinimumItemSlots, false);

		//itemInfoLabel
		itemInfoLabel = new Label("", StageScreenHelper.getLabelStyle(Color.BLACK));
		itemInfoLabel.setAlignment(Align.center);
		itemInfoLabel.setWrap(true);
		stage.addActor(itemInfoLabel);
		
		//itemEffectLabel
		itemEffectLabel = new Label("", StageScreenHelper.getLabelStyle(Color.BLACK));
		itemEffectLabel.setAlignment(Align.center);
		itemEffectLabel.setWrap(true);
		stage.addActor(itemEffectLabel);
		
		//errorMessageLabel
		errorMessageLabel = new Label("", StageScreenHelper.getLabelStyle(Color.ORANGE));
		errorMessageLabel.setAlignment(Align.center);
		errorMessageLabel.setWrap(true);
		stage.addActor(errorMessageLabel);
		
		//use item
		useItemButton = createUseItemButton();
		
		//equip item
		equipItemButton = createEquipItemButton();
		
		//unequip item
		unEquipItemButton = createUnEquipItemButton();
		
		//items buttons visibility
		setItemButtonsVisibility(false, false, false);
						
		//exit screen 
		exitScreenButton = createExitScreenButton();
	}
	
	private ExtendedImageButton<Label> createUseItemButton() {
		ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().getTranslation(
				"PjCharacterSheet", "useItem"), stage, AssetRepository.getInstance().getSound("click_button"));
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				useSelectedItem();
			}
		});
		return button;
	}
	
	private ExtendedImageButton<Label> createEquipItemButton() {
		ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().getTranslation(
				"PjCharacterSheet", "equipItem"), stage, AssetRepository.getInstance().getSound("click_button"));
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				equipSelectedItem();				
			}			
		});
		return button;
	}
	
	private ExtendedImageButton<Label> createUnEquipItemButton() {
		ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().getTranslation(
				"PjCharacterSheet", "unequipItem"), stage, AssetRepository.getInstance().getSound("click_button"));
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				unEquipSelectedItem();				
			}			
		});
		return button;
	}
	
	private ExtendedImageButton<Label> createExitScreenButton() {
		ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().getTranslation(
				"PjCharacterSheet", "exitScreen"), stage, AssetRepository.getInstance().getSound("click_button"));
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				exitScreen();				
			}						
		});
		return button;
	}
	
	protected void useSelectedItem() {
		if(screenMode == InventoryScreenMode.Combat && itemAlreadyUsed) {
			//cannot use items more than once per round in combat
			return;
		}
		
		if(character != null && selectedItemButton != null && selectedItemButton.getTag() instanceof ItemUsable) {
			ItemUsable item = (ItemUsable) selectedItemButton.getTag();
			
			item.useItem(character);			
			character.getBackpack().removeItem(item);
			itemAlreadyUsed = true;
			itemEffectLabel.setText(item.getEffectDescription());
			itemEffectLabel.setVisible(true);
			
			backpackItemsButtons.remove(selectedItemButton);
			backpackControl.takeSelectedItemButton();
			lifePowerPointsControl.updatePointsLabel(character);
			selectedItemButton = null;
			itemImageButtonPressed(null);

			if(scrollPaneIndicator != null) {
				scrollPaneIndicator.updateIndicatorsVisibility();
			}
		}
	}
	
	protected void unEquipSelectedItem() {
		if(screenMode == InventoryScreenMode.Combat) {
			//cannot equip/unequip items in combat
			return;
		}
		
		if(character != null && selectedItemButton != null && selectedItemButton.getTag() instanceof ItemEquipable) {
			ItemEquipable item = (ItemEquipable) selectedItemButton.getTag();
			if(item.isEquipped()) {
				EEquipmentPosition equipPosition = item.getEquipmentPosition();
				character.setEquippedItem(equipPosition, null);
				item.unEquip();
				character.getBackpack().addItem(item);
								
				Image positionImage = equipmentPositionImages.get(equipPosition);
				positionImage.setVisible(true);

				ExtendedImageButton<Item> selectedItemButtonAux = selectedItemButton;
				itemImageButtonPressed(selectedItemButton);
				equipmentItemsButtons.remove(selectedItemButtonAux);
				backpackItemsButtons.add(selectedItemButtonAux);
				backpackControl.addItemButton(selectedItemButtonAux);
			}

			if(scrollPaneIndicator != null) {
				scrollPaneIndicator.updateIndicatorsVisibility();
			}
		}
	}

	protected void equipSelectedItem() {
		if(screenMode == InventoryScreenMode.Combat) {
			//cannot equip/unequip items in combat
			return;
		}
		
		if(character != null && selectedItemButton != null && selectedItemButton.getTag() instanceof ItemEquipable) {
			ItemEquipable item = (ItemEquipable) selectedItemButton.getTag();
			EEquipmentPosition equipPosition = null;
			for(EEquipmentPosition allowedPosition: item.getAllowedPositions()) {
				if(character.getEquippedItem(allowedPosition) == null) {
					equipPosition = allowedPosition;
					break;
				} else if (equipPosition == null) {
					equipPosition = allowedPosition;
				}
			}
			
			if(equipPosition == null) {
				return;
			}

			ItemEquipable previouslyEquippedItem = character.getEquippedItem(equipPosition);
			boolean equipped = character.setEquippedItem(equipPosition, item);
			
			if(equipped) {
				character.getBackpack().removeItem(item);
				backpackControl.takeSelectedItemButton();
				Image positionImage = equipmentPositionImages.get(equipPosition);
				stage.addActor(selectedItemButton);
				PositionerHelper.setPositionCenterInActor(positionImage , selectedItemButton);
				positionImage.setVisible(false);

				backpackItemsButtons.remove(selectedItemButton);
				equipmentItemsButtons.add(selectedItemButton);
				itemImageButtonPressed(selectedItemButton);

				if(previouslyEquippedItem != null) {
					previouslyEquippedItem.unEquip();
					character.getBackpack().addItem(previouslyEquippedItem);

					ExtendedImageButton<Item> previouslyEquippedItemButton = null;
					for(ExtendedImageButton<Item> itemButton: this.equipmentItemsButtons) {
						if(itemButton.getTag() == previouslyEquippedItem) {
							previouslyEquippedItemButton = itemButton;
							break;
						}
					}

					if(previouslyEquippedItemButton != null) {
						equipmentItemsButtons.remove(previouslyEquippedItemButton);
						backpackItemsButtons.add(previouslyEquippedItemButton);
						backpackControl.addItemButton(previouslyEquippedItemButton);
					}
				}
			}

			if(scrollPaneIndicator != null) {
				scrollPaneIndicator.updateIndicatorsVisibility();
			}
		}
	}		
	
	private void exitScreen() {
		for(com.pberna.adventure.screens.ExitListener listener: listeners) {
			listener.exit(this);
		}
	}

	@Override
	public void setModel(Character model) {
		boolean updatedGenre = (character == null && model.getGender() == Gender.Female) 
				|| (character != null && character.getGender() != model.getGender()); 
		character = model;
		if(selectedItemButton != null) {
			itemImageButtonPressed(selectedItemButton);
		}
		itemEffectLabel.setText("");
		errorMessageLabel.setText("");
		updateControls(updatedGenre);
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backpackScrollPane.setScrollPercentX(0);
	}	

	@Override
	public Character getModel() {
		return this.character;
	}
	
	private void updateControls(boolean updatedGenre) {
		
		if(character == null) {
			return;
		}
		
		//upper panel
		updatePortraitImage();
		updateNameLabel();
		updateRaceGenderLabel();
		updatePointsLabel();
		
		//center panel
		updateEquippedPositionItemsImages(updatedGenre);
		updateBackpackItemsImages();
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
	
	private void updateEquippedPositionItemsImages(boolean updatedGenre) {
		if(updatedGenre) {
			//add specific genre images and add them again
			for(EEquipmentPosition equipmentPosition: InventoryScreenHelper.GenderDependantEquipmentPositions){
				Image oldImage = equipmentPositionImages.get(equipmentPosition);
				oldImage.remove();
				Image newImage = new Image(InventoryScreenHelper.getEquipmentPositionImage(character, equipmentPosition));
				equipmentPositionImages.put(equipmentPosition, newImage);
				stage.addActor(newImage);
				InventoryScreenHelper.setPositionInEquippedGrid(equipmentPosition, newImage, equippedGrid);
			}
		}
		
		emptyEquippedItemsButtons();
		
		//update visibility of the position item image
		for(EEquipmentPosition equipmentPosition: EEquipmentPosition.values()){
			ItemEquipable item = character.getEquippedItem(equipmentPosition);
			Image equipmentPositionImage = equipmentPositionImages.get(equipmentPosition);			
			equipmentPositionImage.setVisible(item == null);
			
			if(item != null) {
				ExtendedImageButton<Item> itemButton = createItemButton(item);
				equipmentItemsButtons.add(itemButton);
				stage.addActor(itemButton);
				PositionerHelper.setPositionCenterInActor(equipmentPositionImage, itemButton);
			}			
		}		
	}
	
	private void emptyEquippedItemsButtons() {
		if(equipmentItemsButtons != null) {
			for(int i = 0; i < equipmentItemsButtons.size(); i++) {
				equipmentItemsButtons.get(i).remove();								
			}
			equipmentItemsButtons.clear();
		}				
	}
	
	private void updateBackpackItemsImages() {
		emptyBackpackItems();
		
		Backpack backpack = character.getBackpack();
		for(int i = 0; i < backpack.getItemCount(); i++) {
			Item item = backpack.getItem(i);
			ExtendedImageButton<Item> itemImageButton = createItemButton(item);
			
			//image
			backpackItemsButtons.add(itemImageButton);
			backpackControl.addItemButton(itemImageButton);
		}
	}
	
	private void emptyBackpackItems() {
		if(backpackItemsButtons != null) {
			backpackItemsButtons.clear();
		}
		backpackControl.removeAllItems();
	}
	
	private ExtendedImageButton<Item> createItemButton(Item item) {
		ExtendedImageButton<Item> itemImageButton = new ExtendedImageButton<Item>(new TextureRegionDrawable(
				AssetRepository.getInstance().getTextureRegion("items", item.getImageName())));
		itemImageButton.setTag(item);
		StageScreenHelper.addOnClickSound(itemImageButton, AssetRepository.getInstance().getSound("click_button"));
		itemImageButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				@SuppressWarnings("unchecked")
				ExtendedImageButton<Item> itemButtonPressed = (ExtendedImageButton<Item>) actor;
				itemImageButtonPressed(itemButtonPressed);
			}
		});
		return itemImageButton;
	}

	protected void itemImageButtonPressed(ExtendedImageButton<Item> itemButtonPressed) {
				
		if(selectedItemButton == itemButtonPressed ) {
			if (backpackItemsButtons.contains(selectedItemButton)) {
				backpackControl.setSelectedItemButton(selectedItemButton, false);
			} else {
				hideSelectedBorder();
			}
			selectedItemButton = null;
			setItemButtonsVisibility(false, false, false);
		} else {
			selectedItemButton = itemButtonPressed;
			if(backpackItemsButtons.contains(itemButtonPressed)) {
				backpackControl.setSelectedItemButton(itemButtonPressed, true);
				hideSelectedBorder();
			} else {
				showSelectedBorder(itemButtonPressed);
				backpackControl.setSelectedItemButton(null, false);
			}
			
			Item item = selectedItemButton.getTag();
			if(item instanceof ItemEquipable && screenMode != InventoryScreenMode.Combat) {
				if(((ItemEquipable) item).isEquipped()) {
					setItemButtonsVisibility(false, false, true);
				} else {
					setItemButtonsVisibility(false, true, false);
				}
			} else if (item instanceof ItemUsable && !(screenMode == InventoryScreenMode.Combat && itemAlreadyUsed)) {
				setItemButtonsVisibility(true, false, false);
			} else {
				setItemButtonsVisibility(false, false, false);
			}
		}
		
		refreshItemInfoMessage();
	}
	
	private void refreshItemInfoMessage() {
		if(selectedItemButton == null) {
			itemInfoLabel.setText("");
			errorMessageLabel.setText("");
		} else {
			Item item = selectedItemButton.getTag();
			if(item != null) {
				if(item instanceof ItemEquipable && screenMode == InventoryScreenMode.Combat) {
					errorMessageLabel.setText(Localization.getInstance().getTranslation("InGame", "equipUnequipNotAllowedInCombat"));
					itemInfoLabel.setText("");
				} else {
					itemInfoLabel.setText(item.getName() + " :  " + item.getDescription());
					errorMessageLabel.setText("");
				}				
				itemEffectLabel.setText("");				
			}
		}		
	}

	private void showSelectedBorder(ExtendedImageButton<Item> actor) {
		PositionerHelper.setPositionCenterInActor(actor, selectedBorder);
		PositionerHelper.setPositionCenterInActor(actor, selectedBorderRing);
		if(actor.getTag() instanceof ItemEquipable) {
			ItemEquipable itemEquipable = (ItemEquipable) actor.getTag();
			if(itemEquipable.getAllowedPositions().length > 0 &&
					(itemEquipable.getAllowedPositions()[0] == EEquipmentPosition.LeftHandRing
					|| itemEquipable.getAllowedPositions()[0] == EEquipmentPosition.RightHandRing))
			{
				selectedBorder.setVisible(false);
				selectedBorderRing.setVisible(true);
				selectedBorderRing.toFront();
				return;
			}
		}

		selectedBorderRing.setVisible(false);
		selectedBorder.setVisible(true);
		selectedBorder.toFront();
	}
	
	private void hideSelectedBorder() {
		selectedBorder.setVisible(false);
		selectedBorderRing.setVisible(false);
	}
	
	private void setItemButtonsVisibility(boolean useItemVisible, boolean equipItemVisible, boolean unEquipButtonVisible) {
		StageScreenHelper.setImageButtonVisible(useItemButton, useItemVisible);
		StageScreenHelper.setImageButtonVisible(equipItemButton, equipItemVisible);
		StageScreenHelper.setImageButtonVisible(unEquipItemButton, unEquipButtonVisible);
	}
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
	
		updatePanelsSizes(width, height);
		float padding = width * PercentageBorderScreen;
		
		//upper panel
		if(portraitImage != null) {
			PositionerHelper.setPositionCentered(portraitImage, portraitPanel);
		}
		PositionerHelper.setPositionCenteredHorizontalFillingColumn(new Actor[] {nameLabel, raceGenderLabel, lifePowerPointsControl}, 
				upperLabelsPanel, VerticalSide.Top);

		//backpack and its scroll pane
		backpackScrollPane.setWidth(backpackWidth);
		backpackScrollPane.setHeight(backpackHeight);
		scrollPaneIndicator.updateIndicatorsPositions(padding, padding / 2.0f);
		
		//center panel		
		PositionerHelper.setPositionCenteredHorizontalFillingColumn(new Actor[] { equippedGrid, backpackScrollPane }, centerPanel, VerticalSide.Top);
		PositionerHelper.setPositionCenterInActor(backpackScrollPane, backpackFrame);
		for(EEquipmentPosition equipmentPosition: equipmentPositionImages.keySet()) {
			InventoryScreenHelper.setPositionInEquippedGrid(equipmentPosition, 
					equipmentPositionImages.get(equipmentPosition), equippedGrid);
		}

		//message panel
		itemInfoLabel.setWidth(messagePanel.getWidth() - (padding * 2.0f));
		PositionerHelper.setPositionCentered(itemInfoLabel, messagePanel);
		itemEffectLabel.setWidth(messagePanel.getWidth() - (padding * 2.0f));
		PositionerHelper.setPositionCentered(itemEffectLabel, messagePanel);
		errorMessageLabel.setWidth(messagePanel.width - (padding * 2.0f));
		PositionerHelper.setPositionCentered(errorMessageLabel, messagePanel);
		
		//lower panel
		PositionerHelper.setPositionCenteredVerticalFillingRow(new Actor[] { useItemButton, exitScreenButton }, lowerPanel, HorizontalSide.Left);
		PositionerHelper.setPositionCenterInActor(useItemButton, equipItemButton);
		PositionerHelper.setPositionCenterInActor(useItemButton, unEquipItemButton);
		
		//lower panel - buttons labels
		PositionerHelper.setPositionCenterInActor(useItemButton, useItemButton.getTag());
		PositionerHelper.setPositionCenterInActor(exitScreenButton, exitScreenButton.getTag());
		PositionerHelper.setPositionCenterInActor(equipItemButton, equipItemButton.getTag());
		PositionerHelper.setPositionCenterInActor(unEquipItemButton, unEquipItemButton.getTag());		
	}		

	private void updatePanelsSizes(float width, float height) {
		//lower panel
		lowerPanel.set(0, 0, width, height * LowerPanelPercentageHeight);
		
		//message panel
		messagePanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, height * MessagePanelPercentageHeight);
		
		//center panel
		centerPanel.set(0, messagePanel.getY() + messagePanel.getHeight(), width, height * CenterPanelPercentageHeight);
				
		//upper panel
		float subPanelsWidth1 = width * ( 1f - (UpperPanelLeftRightMarginsPercentage * 2f)) * 0.35f;
		float subPanelsWidth2 = width * ( 1f - (UpperPanelLeftRightMarginsPercentage * 2f)) * 0.65f;
		upperPanel.set(0, centerPanel.getY() + centerPanel.getHeight(), width, height * UpperPanelPercentageHeight);
		portraitPanel.set(width * UpperPanelLeftRightMarginsPercentage, upperPanel.getY(), subPanelsWidth1, upperPanel.getHeight());
		if(portraitImage != null) {
			upperLabelsPanel.set(portraitPanel.getX() + portraitPanel.getWidth(), portraitPanel.getY() +
					((portraitPanel.getHeight() - portraitImage.getPrefHeight()) / 2f),	subPanelsWidth2, portraitImage.getHeight());
		} else {
			upperLabelsPanel.set(portraitPanel.getX() + portraitPanel.getWidth(), portraitPanel.getY(),
					subPanelsWidth2, portraitPanel.getHeight());
		}
	}

	public InventoryScreenMode getScreenMode() {
		return screenMode;
	}

	public void setScreenMode(InventoryScreenMode screenMode) {
		this.screenMode = screenMode;
		this.itemAlreadyUsed = false;
	}
	
	public boolean isItemAlreadyUsed() {
		return this.itemAlreadyUsed;
	}

	@Override
	public void refreshLocalizableItems() {
		//useItemButton
		useItemButton.remove();
		useItemButton.getTag().remove();
		useItemButton = createUseItemButton();
		
		//equipItemButton
		equipItemButton.remove();
		equipItemButton.getTag().remove();
		equipItemButton = createEquipItemButton();
		
		//unEquipItemButton
		unEquipItemButton.remove();
		unEquipItemButton.getTag().remove();
		unEquipItemButton = createUnEquipItemButton();
		
		//exitScreenButton
		exitScreenButton.remove();
		exitScreenButton.getTag().remove();
		exitScreenButton = createExitScreenButton();
		
		setItemButtonsVisibility(false, false, false);
		
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if(scrollPaneIndicator != null) {
			scrollPaneIndicator.updateIndicatorsVisibility();
		}
	}

	@Override
	public void hide() {
		super.hide();

		ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(scrollPaneIndicator);
	}

	@Override
	public void show() {
		super.show();

		if(scrollPaneIndicator != null) {
			scrollPaneIndicator.refreshContinousRendering();
		}
	}
}
