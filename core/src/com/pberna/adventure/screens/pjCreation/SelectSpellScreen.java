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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.spells.Spell;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.adventure.screens.controls.SpellListControlListener;
import com.pberna.adventure.screens.controls.SpellsListControl;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;

public class SelectSpellScreen extends BasePjCreationScreen
	implements IModelEditingScreen<Character> {
		
	private static final float InfoPanelPercentageHeight = 0.10f;
	private static final float PercentageBorderSpells = 0.03f;
		
	private ArrayList<Spell> spells;
	private Character character;
	private ArrayList<Spell> selectedSpells;
	
	private SpellsListControl spellsListControl;
	private Label remainingSpellsLabel;
	private Label errorMessageLabel;
		
	private Rectangle infoPanel;
	
	public SelectSpellScreen() {
		super(String.format(Localization.getInstance().getTranslation("PjCreation", "chooseSpells"), 0),
				"selected_border_spell");
		
		spells = Spell.getSpells();
		character = null;
		selectedSpells = new ArrayList<Spell>(spells.size());
				
		infoPanel = new Rectangle();				
		
		buildStage();
	}

	@Override
	public void setModel(Character model) {
		character = model;
		selectedSpells = character.getSpells();
		
		emptyMessageText();
		refreshMainLabel();
		refreshLocalizableItems();		
		updateRemaingSpellText();
		updateAcceptButtonVisibility();
		refreshSelectedSpells();
	}	

	@Override
	public Character getModel() {
		Spell.orderSpellsById(selectedSpells);
		character.setSpells(selectedSpells);
		return character;
	}
	
	private void buildStage() {		
		//spells list control
		spellsListControl = new SpellsListControl();
		spellsListControl.addListener(new SpellListControlListener() {			
			@Override
			public void spellClicked(Spell spellClicked) {
				toggleSelectedSpell(spellClicked);				
			}
		});		
		initializeMainScrollPane(spellsListControl);
		
		remainingSpellsLabel = new Label(String.format(Localization.getInstance().getTranslation("PjCreation", "remainingSpells"), 
				getRemainingSpells()),	getLabelStyle(Color.BLACK));
		remainingSpellsLabel.setAlignment(Align.center);
		stage.addActor(remainingSpellsLabel);
		
		errorMessageLabel = new Label(" ", getLabelStyle(Color.ORANGE));
		errorMessageLabel.setAlignment(Align.center);
		errorMessageLabel.setWrap(true);
		stage.addActor(errorMessageLabel);
		
		showSpells();		
	}
		
	private int getRemainingSpells() {
		if(character != null) {
			return Math.max(0, character.getBaseSkillValue(Skill.IdMagic) - selectedSpells.size());
		}
		return 0;
	}
	
	private void setErrorMessageText(String errorMessage) {
		errorMessageLabel.setText(errorMessage);
	}
	
	private void emptyMessageText() {
		errorMessageLabel.setText("");
	}
	
	private void updateRemaingSpellText() {
		if(remainingSpellsLabel != null) {
			remainingSpellsLabel.setText(String.format(Localization.getInstance().getTranslation("PjCreation", "remainingSpells"),
					getRemainingSpells()));
		}
	}
	
	private void updateAcceptButtonVisibility()
	{
		if(getRemainingSpells() <= 0) {
			setAcceptButtonVisibility(true);
		} else {
			setAcceptButtonVisibility(false);
		}
	}
	
	protected Label.LabelStyle getLabelStyle(Color color)
	{
		return StageScreenHelper.getLabelStyle(color);
	}
		
	private void showSpells(){

		if(character != null) {
			spellsListControl.showSpells(this.spells, character.getTotalSkillValue(Skill.IdMagic), character.getMaximumPowerPoints());
		} else {
			spellsListControl.showSpells(this.spells);
		}
	}	
	
	private void toggleSelectedSpell(Spell spell) {
		if(character != null) {
			Spell alreadySelectedSpell = Spell.findSpells(selectedSpells, spell.getId()); 
			if(alreadySelectedSpell == null){				
				selectSpell(spell);	
			} else {
				unselectSpell(alreadySelectedSpell);								
			}
		}
	}
	
	private void selectSpell(Spell spell) {
		if(getRemainingSpells() <= 0) {
			setErrorMessageText(Localization.getInstance().getTranslation("PjCreation", "messageCannotSelectMoreSpells"));			
		} else if (spell.getMinimumMagicRequired() > this.character.getBaseSkillValue(Skill.IdMagic)) 
		{
			setErrorMessageText(Localization.getInstance().getTranslation("PjCreation", "messageCannotSelectSpellEnoughMagic"));
		} else if(spell.getPowerPointsNeeded() > this.character.getMaximumPowerPoints()) {
			setErrorMessageText(Localization.getInstance().getTranslation("PjCreation", "messageCannotSelectSpellEnoughPP"));
		}
		else
		{
			selectedSpells.add(spell);
			spellsListControl.setSpellSelected(spell, true);
			emptyMessageText();			
			updateRemaingSpellText();
			updateAcceptButtonVisibility();
		}
	}
	
	private void unselectSpell(Spell spell) {
		selectedSpells.remove(spell);
		spellsListControl.setSpellSelected(spell, false);
		emptyMessageText();		
		updateRemaingSpellText();
		updateAcceptButtonVisibility();
	}
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
		updatePanelsSizes(width, height);
			
		//spells
		if(mainScrollPane != null) {
			mainScrollPane.setWidth(mainPanel.getWidth());
			mainScrollPane.setHeight(mainPanel.getHeight());
			PositionerHelper.setPositionFromCorner(mainScrollPane, Corner.TopLeft, 0, 0, mainPanel);
		}
		
		//scroll pane indicator
		float paddingPaneIndicator = width * PercentageBorderScreen;		
		if(paneIndicator != null) {
			paneIndicator.updateIndicatorsPositions(paddingPaneIndicator, paddingPaneIndicator / 2.0f);
		}
		
		float padding = width * PercentageBorderSpells;
		
		//spells list & main scroll pane
		spellsListControl.setControlWidth(mainPanel.getWidth());
		spellsListControl.setPaddingWidth(padding);
		
		//labels
		/*errorMessageLabel.setWidth(mainPanel.getWidth() * 0.9f);
		PositionerHelper.setPositionCenteredHorizontal(errorMessageLabel, VerticalSide.Bottom,
					(remainingSpellsLabel.getY() + mainLabel.getY()) / 2.0f, stage);*/
		errorMessageLabel.setWidth(mainPanel.getWidth() * 0.9f);
		PositionerHelper.setPositionCenteredHorizontalFillingColumn(new Actor[]{remainingSpellsLabel, errorMessageLabel},
				infoPanel, VerticalSide.Top);
	}
	
	private void updatePanelsSizes(float width, float height) {
		float infoPanelHeight = height * InfoPanelPercentageHeight;
		float lowerPanelHeight = height * LowerPanelPercentageHeight;
		
		infoPanel.set(0, lowerPanelHeight, width, infoPanelHeight);
		mainPanel.set(0, infoPanel.getY() + infoPanel.getHeight(), width, height - infoPanelHeight - lowerPanelHeight);
	}
		
	public void refreshMainLabel()
	{
		int magic = character != null ? character.getBaseSkillValue(Skill.IdMagic) : 0;
		mainText = String.format(Localization.getInstance().getTranslation("PjCreation", "chooseSpells"), magic);		
	}
	
	@Override
	public void refreshLocalizableItems() {
		refreshMainLabel();
		updateRemaingSpellText();
		
		spells = Spell.getSpells();
		for(Spell selectedSpell: selectedSpells) {
			for(Spell spell: spells) {
				if(selectedSpell.getId() == spell.getId())
				{
					selectedSpell.setName(spell.getName());
					selectedSpell.setDescription(spell.getDescription());
				}
			}			
		}		
		showSpells();
		
		super.refreshLocalizableItems();
	}
	
	private void refreshSelectedSpells() {
		for(Spell selectedSpell: selectedSpells) {
			spellsListControl.setSpellSelected(selectedSpell, true);
		}
	}

	public void resetScrollPane() {
		mainScrollPane.setScrollY(0);
	}
}
