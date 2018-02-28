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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.spells.AttackSpell;
import com.pberna.adventure.spells.EnhanceCombatStatsSpell;
import com.pberna.adventure.spells.EnhanceSkillSpell;
import com.pberna.adventure.spells.HealSpell;
import com.pberna.adventure.spells.Spell;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.controls.ActiveSpellsControl;
import com.pberna.adventure.screens.controls.LifePowerPointsControl;
import com.pberna.engine.screens.controls.ScrollPaneIndicator;
import com.pberna.adventure.screens.controls.SpellListControlListener;
import com.pberna.adventure.screens.controls.SpellsListControl;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.adventure.pj.Character;

public class SpellsScreen extends BaseStageScreen implements ILocalizable {
	private static final float LowerPanelPercentageHeight = 0.10f;
	private static final float InfoPanelPercentageHeight = 0.06f;
	private static final float MessagePanelPercentageHeight = 0.06f;
	private static final float PercentageBorderSpells = 0.03f;
	private static final float PercentageBorder = 0.015f;

	public ArrayList<Spell> getCastedSpells() {
		return castedSpells;
	}

	public enum SpellScreenMode {
		Adventure,
		Combat		
	}
	
	private ScrollPane mainScrollPane;
	private ScrollPaneIndicator scrollPaneIndicator;
	private SpellsListControl spellsListControl;
	private ExtendedImageButton<Label> castSpellButton;
	private ExtendedImageButton<Label> exitButton;
	private Rectangle mainPanel;
	private Rectangle infoPanel;
	private Rectangle messagePanel;
	private Rectangle lowerPanel;
	private LifePowerPointsControl powerPointsControl;
	private ActiveSpellsControl activeSpellsControl;
	private Label errorMessageLabel;
	private Label infoMessageLabel;
	
	private ArrayList<Spell> spells;
	private ArrayList<SpellsScreenListener> listeners;
	private Spell selectedSpell;
	private SpellScreenMode mode;
	private boolean spellAlreadyCasted;
	private Character character;
	private ArrayList<Spell> castedSpells;
	
	public SpellsScreen()
	{
		mainPanel = new Rectangle();
		infoPanel = new Rectangle();
		messagePanel = new Rectangle();
		lowerPanel = new Rectangle();
		spells = new ArrayList<Spell>();
		listeners = new ArrayList<SpellsScreenListener>();
		selectedSpell = null;
		mode = SpellScreenMode.Adventure;
		spellAlreadyCasted = false;
		character = null;
		castedSpells = new ArrayList<Spell>();
		
		buildStage();
		
		refreshCastSpellState(true);
	}
	
	public ArrayList<Spell> getSpells() {
		return spells;
	}
	
	public void addListener(SpellsScreenListener listener) {
		listeners.add(listener);
	}
	
	public void setCharacter(Character character) {
		this.character = character;
		if(this.character != null) {
			if(mode == SpellScreenMode.Adventure) {
				this.spells = this.character.getSpells();	
			} else {
				this.spells = this.character.getCombatSpells();
			}
			spellsListControl.showSpells(this.spells, this.character.getTotalSkillValue(Skill.IdMagic), this.character.getCurrentPowerPoints());
			mainScrollPane.setScrollPercentY(0);
			selectedSpell = null;
			refreshCastSpellState(true);
			powerPointsControl.updatePointsLabel(this.character);
			activeSpellsControl.updateActiveSpells(this.character, false);
		}
	}
	
	public Character getCharacter() {
		return this.character;
	}

	private void buildStage() {
		//spells list and scroll pane
		spellsListControl = new SpellsListControl();
		spellsListControl.setPaddingWidth(Gdx.graphics.getWidth() * PercentageBorderSpells);
		spellsListControl.addListener(new SpellListControlListener() {			
			@Override
			public void spellClicked(Spell spellClicked) {
				if(selectedSpell == null)
				{
					//selected spell
					selectedSpell = spellClicked;
					spellsListControl.setSpellSelected(selectedSpell, true);
				} else if (selectedSpell.getId() != spellClicked.getId()) {
					//change selected spell
					spellsListControl.setSpellSelected(selectedSpell, false);
					selectedSpell = spellClicked;
					spellsListControl.setSpellSelected(selectedSpell, true);
				} else {
					//unselect spell
					spellsListControl.setSpellSelected(selectedSpell, false);
					selectedSpell = null;
				}
				
				refreshCastSpellState(true);
			}
		});
		mainScrollPane = new ScrollPane(spellsListControl);
		mainScrollPane.setVelocityY(1);
		mainScrollPane.setScrollingDisabled(true, false);		
		stage.addActor(mainScrollPane);
		scrollPaneIndicator = new ScrollPaneIndicator(mainScrollPane, false);
		
		//cast spell button		
		castSpellButton = createCastSpellButton();
		
		//exit button
		exitButton = createExitButton();
		
		//powerPointsControl
		powerPointsControl = new LifePowerPointsControl();
		powerPointsControl.setLifePointsVisible(false);
		powerPointsControl.setLuckPointsVisible(false);
		stage.addActor(powerPointsControl);
		
		//activeSpellsControl		
		activeSpellsControl = new ActiveSpellsControl(Gdx.graphics.getWidth() * PercentageBorder);
		stage.addActor(activeSpellsControl);
		
		//errorMessageLabel
		errorMessageLabel = new Label("", StageScreenHelper.getLabelStyle(Color.ORANGE));
		errorMessageLabel.setAlignment(Align.center);
		errorMessageLabel.setWrap(true);
		stage.addActor(errorMessageLabel);
		
		//infoMessageLabel
		infoMessageLabel = new Label("", StageScreenHelper.getLabelStyle(Color.BLACK));
		infoMessageLabel.setAlignment(Align.center);
		infoMessageLabel.setWrap(true);
		stage.addActor(infoMessageLabel);
	}	
	
	private ExtendedImageButton<Label> createCastSpellButton() {
		ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().
				getTranslation("Common", "castSpellLabel") , stage, AssetRepository.getInstance().getSound("click_button"));
		button.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {				
				castSpell();
			}
		});
		return button;
	}
	
	private ExtendedImageButton<Label> createExitButton() {
		ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().
				getTranslation("Common", "exitLabel"), stage, AssetRepository.getInstance().getSound("click_button"));
		button.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for(SpellsScreenListener listener: listeners) {
					listener.exit(this);
				}
			}
		});	
		return button;
	}

	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
		updatePanelsSizes(width, height);
		float padding = width * PercentageBorderSpells;
		
		//spells list & main scroll pane
		spellsListControl.setControlWidth(mainPanel.getWidth());
		spellsListControl.setPaddingWidth(padding);
		mainScrollPane.setWidth(mainPanel.getWidth());
		mainScrollPane.setHeight(mainPanel.getHeight());		
		PositionerHelper.setPositionFromCorner(mainScrollPane, Corner.TopLeft, 0, 0, mainPanel);
		scrollPaneIndicator.updateIndicatorsPositions(padding, padding / 2.0f);
		
		//buttons
		PositionerHelper.setPositionCenteredVerticalFillingRow(new Actor [] { castSpellButton, exitButton }, lowerPanel ,HorizontalSide.Left);
		PositionerHelper.setPositionCenterInActor(castSpellButton, castSpellButton.getTag());
		PositionerHelper.setPositionCenterInActor(exitButton, exitButton.getTag());
		
		//power points control
		PositionerHelper.setPositionCenteredVertical(powerPointsControl, HorizontalSide.Right, padding, infoPanel);
				
		//activeSpellsControl
		PositionerHelper.setPositionCenteredVertical(activeSpellsControl, HorizontalSide.Left, padding, infoPanel);
		
		//error message label
		errorMessageLabel.setWidth(messagePanel.width - (padding * 2.0f));
		PositionerHelper.setPositionCentered(errorMessageLabel, messagePanel);
		
		//infoMessageLabel
		infoMessageLabel.setWidth(messagePanel.width - (padding * 2.0f));
		PositionerHelper.setPositionCentered(infoMessageLabel, messagePanel);		
	}
	
	private void updatePanelsSizes(float width, float height) {
		float lowerPanelHeight = (height * LowerPanelPercentageHeight);
		float messagePanelHeight = (height * MessagePanelPercentageHeight);
		float infoPanelHeight = (height * InfoPanelPercentageHeight);
		
		lowerPanel.set(0, 0, width, lowerPanelHeight);
		messagePanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, messagePanelHeight);
		infoPanel.set(0, messagePanel.getY() + messagePanel.getHeight(), width, infoPanelHeight);
		mainPanel.set(0, infoPanel.getY() + infoPanel.getHeight(), width, 
				height - lowerPanel.getHeight()- infoPanel.getHeight() - messagePanel.getHeight());
	}	
		
	protected void refreshCastSpellState(boolean emptyInfoMessage) {
		if(selectedSpell == null) {
			setCastSpellButtonVisibility(false);
			setErrorMessageText("");
			if(emptyInfoMessage) {
				setInfoMessageText("");
			}
			return;
		}
		
		if(mode == SpellScreenMode.Adventure) {
			if(!selectedSpell.canBeCastedInAdventure()) {
				setCastSpellButtonVisibility(false);
				if(!selectedSpell.canBeCastedInCombat()) {
					setErrorMessageText(Localization.getInstance().getTranslation("InGame", "spellOnlyOption"));					
				} else {
					setErrorMessageText(Localization.getInstance().getTranslation("InGame", "spellOnlyCombat"));
				}
			} else {
				if(selectedSpell.getPowerPointsNeeded() > character.getCurrentPowerPoints()) {
					setCastSpellButtonVisibility(false);
					setErrorMessageText(Localization.getInstance().getTranslation("InGame", "notEnoughtPPSpell"));
				} else {
					if(character.hasActiveSpell(selectedSpell.getId())) {
						setCastSpellButtonVisibility(false);
						setErrorMessageText(Localization.getInstance().getTranslation("InGame", "activeAlreadyActive"));
					} else {
						setCastSpellButtonVisibility(true);
						setErrorMessageText("");
						if(emptyInfoMessage) {
							setInfoMessageText("");
						}
					}
				}
			}
		}	
		if(mode == SpellScreenMode.Combat) {
			if(spellAlreadyCasted) {
				setCastSpellButtonVisibility(false);
				setErrorMessageText(Localization.getInstance().getTranslation("InGame", "noMoreSpellsThisTurn"));			
			} else if(!selectedSpell.canBeCastedInCombat()) {
				setCastSpellButtonVisibility(false);
				setErrorMessageText(Localization.getInstance().getTranslation("InGame", "spellNotInCombat"));
			} else {
				if(selectedSpell.getPowerPointsNeeded() > character.getCurrentPowerPoints()) {
					setCastSpellButtonVisibility(false);
					setErrorMessageText(Localization.getInstance().getTranslation("InGame", "notEnoughtPPSpell"));
				} else {
					if(character.hasActiveSpell(selectedSpell.getId())) {
						setCastSpellButtonVisibility(false);
						setErrorMessageText(Localization.getInstance().getTranslation("InGame", "activeAlreadyActive"));
					} else {
						setCastSpellButtonVisibility(true);
						setErrorMessageText("");
						if(emptyInfoMessage) {
							setInfoMessageText("");
						}
					}
				}
			}
		}
	}
	
	private void setCastSpellButtonVisibility(boolean visible) {
		castSpellButton.setVisible(visible);
		castSpellButton.getTag().setVisible(visible);
	}
	
	private void setErrorMessageText(String errorMessageText) {
		errorMessageLabel.setText(errorMessageText);
		errorMessageLabel.setVisible(!errorMessageText.equals(""));
		if(errorMessageLabel.isVisible()) {
			infoMessageLabel.setText("");
		}
	}
	
	private void setInfoMessageText(String infoMessageText) {
		infoMessageLabel.setText(infoMessageText);
		infoMessageLabel.setVisible(!infoMessageText.equals(""));
		if(infoMessageLabel.isVisible()) {
			errorMessageLabel.setText("");
		}
	}	
	
	protected void castSpell() {
		if(selectedSpell == null) {
			return;
		}
		
		if((mode == SpellScreenMode.Adventure && !selectedSpell.canBeCastedInAdventure()) ||
			(mode == SpellScreenMode.Combat && !selectedSpell.canBeCastedInCombat()) ||
			(mode == SpellScreenMode.Combat && spellAlreadyCasted))
		{
			return;
		}
		
		boolean updateControls = false;

		castedSpells.add(selectedSpell);
		if(selectedSpell instanceof EnhanceCombatStatsSpell || selectedSpell instanceof EnhanceSkillSpell) {
			character.addActiveSpell(selectedSpell);
			character.setCurrentPowerPoints(character.getCurrentPowerPoints() - selectedSpell.getPowerPointsNeeded());
			updateControls = true;
			spellAlreadyCasted = true;
			setInfoMessageText(String.format(Localization.getInstance().getTranslation("InGame", "spellCasted"), selectedSpell.getName()));
		} else {		
			if(selectedSpell instanceof HealSpell) {
				((HealSpell)selectedSpell).heal(character);
				character.setCurrentPowerPoints(character.getCurrentPowerPoints() - selectedSpell.getPowerPointsNeeded());
				updateControls = true;
				spellAlreadyCasted = true;
				setInfoMessageText(String.format(Localization.getInstance().getTranslation("InGame", "spellCasted"), selectedSpell.getName()));
			} else if (selectedSpell instanceof AttackSpell) {
				AttackSpell attackSpell = (AttackSpell) selectedSpell;
				character.setCurrentPowerPoints(character.getCurrentPowerPoints() - selectedSpell.getPowerPointsNeeded());
				updateControls = true;
				spellAlreadyCasted = true;
				for(SpellsScreenListener listener: listeners) {
					listener.castAttackSpell(attackSpell);
				}
			}
		}
		
		if(updateControls) {
			powerPointsControl.updatePointsLabel(character);
			activeSpellsControl.updateActiveSpells(character, true);
			spellsListControl.updateSpellsDescriptionStyle(this.character.getTotalSkillValue(Skill.IdMagic), this.character.getCurrentPowerPoints());
			spellsListControl.setControlWidth(mainPanel.getWidth());
			PositionerHelper.setPositionCenteredVertical(activeSpellsControl, HorizontalSide.Left,  Gdx.graphics.getWidth() * PercentageBorderSpells, infoPanel);
		}		
		spellsListControl.setSpellSelected(selectedSpell, false);
		selectedSpell = null;
		refreshCastSpellState(false);		
	}
	
	public SpellScreenMode getScreenMode() {
		return mode;
	}
	
	public void setScreenMode(SpellScreenMode mode) {
		this.mode = mode;
		spellAlreadyCasted = false;
	}
	
	public boolean isSpellAlreadycasted() {
		return this.spellAlreadyCasted;
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
		castedSpells.clear();
	}

	@Override
	public void refreshLocalizableItems() {
		//castSpellButton
		castSpellButton.remove();
		castSpellButton.getTag().remove();
		castSpellButton = createCastSpellButton();
		
		//exit button
		exitButton.remove();
		exitButton.getTag().remove();
		exitButton = createExitButton();
		
		activeSpellsControl.refreshLocalizableItems();
		
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());	
	}
}
