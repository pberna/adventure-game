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

package com.pberna.adventure.screens.controls;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.spells.Spell;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.controls2D.ExtendedTextButton;
import com.pberna.engine.localization.Localization;

public class SpellsListControl extends Table {
	private static final int UndefinedIntValue = -1;

	private ArrayList<Spell> spells;
	private int currentCharacterMagic;
	private int currentCharacterPowerPoints;
	private ArrayList<Label> spellNameLabels;
	private ArrayList<ExtendedTextButton<Spell>> spellDescriptionTextButtons;
	private ArrayList<ExtendedImageButton<Spell>> spellImageButtons;
	private ArrayList<Table> labelsGroups;
	private ArrayList<Image> selectedBorders;
	
	private ArrayList<SpellListControlListener> listeners;
	private float maxImageWidth;
	private float paddingWidth;
	
	public SpellsListControl() {
		spells = new ArrayList<Spell>();
		currentCharacterMagic = UndefinedIntValue;
		currentCharacterPowerPoints = UndefinedIntValue;
		spellNameLabels = new ArrayList<Label>();
		spellDescriptionTextButtons = new ArrayList<ExtendedTextButton<Spell>>();
		spellImageButtons = new ArrayList<ExtendedImageButton<Spell>>();
		labelsGroups = new ArrayList<Table>();
		selectedBorders = new ArrayList<Image>();
		listeners = new ArrayList<SpellListControlListener>();
		maxImageWidth = 0f;
		paddingWidth = 5;
	}
	
	public void addListener(SpellListControlListener listener) {
		listeners.add(listener);
	}
	
	@SuppressWarnings("rawtypes")
	public void setControlWidth(float width) {
		float availableWidth = width - maxImageWidth - (paddingWidth * 3f);
		
		for(ExtendedTextButton<Spell> textButton: spellDescriptionTextButtons) {
			textButton.setWidth(availableWidth);			
		}
		for(Label label: spellNameLabels) {
			label.setWidth(availableWidth);			
		}
		for(Table group: labelsGroups) {
			group.setWidth(availableWidth);
			for(Cell cellInGroup :group.getCells())
			{
				cellInGroup.width(availableWidth);
			}
			
			Cell<Table> cell = getCell(group);
			if(cell != null) {
				cell.width(availableWidth);
			}			
		}	
	}
	
	public float getPaddingWidth() {
		return paddingWidth;
	}

	public void setPaddingWidth(float paddingWidth) {
		this.paddingWidth = paddingWidth;
	}

	public void showSpells(ArrayList<Spell> spells) {
		showSpells(spells, UndefinedIntValue, UndefinedIntValue);
	}

	public void showSpells(ArrayList<Spell> spells, int currentCharacterMagic, int currentCharacterPowerPoints) {
		this.currentCharacterMagic = currentCharacterMagic;
		this.currentCharacterPowerPoints = currentCharacterPowerPoints;
		this.spells = spells;
		buildControls();
	}

	private void buildControls() {
		clearChildren();
		spellNameLabels.clear();
		spellDescriptionTextButtons.clear();
		spellImageButtons.clear();
		labelsGroups.clear();
		selectedBorders.clear();
		maxImageWidth = 0f;
		
		for(int i = 0; i < spells.size(); i++) {
			addSpellRow(spells.get(i), i == 0, i == spells.size() - 1);
		}
		pack();
	}

	private void addSpellRow(Spell spell, boolean firstRow, boolean lastRow) {
		addSpellImageCell(spell, firstRow, lastRow);
		addLabelsCell(spell, firstRow, lastRow);		
		row();
	}	

	@SuppressWarnings("unchecked")
	private void addSpellImageCell(Spell spell, boolean firstRow, boolean lastRow) {
		TextureRegion spellTexture = AssetRepository.getInstance().getTextureRegion("spells", spell.getImageName());
		ExtendedImageButton<Spell> spellImageButton = new ExtendedImageButton<Spell>(new TextureRegionDrawable(spellTexture));
		spellImageButton.setTag(spell);
		spellImageButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ExtendedImageButton<Spell> spellImageButton = (ExtendedImageButton<Spell>) actor;
				for(SpellListControlListener listener: listeners) {
					AudioManager.getInstance().playSound(AssetRepository.getInstance().getSound("click_button"));
					listener.spellClicked(spellImageButton.getTag());
				}
			}
		});
		
		TextureRegion selectedBorderImage =  AssetRepository.getInstance().getTextureRegion("widgets", "selected_border_spell");
		Image selectedBorder = new Image(selectedBorderImage);
		selectedBorder.setVisible(false);
		selectedBorder.setTouchable(Touchable.disabled);
		selectedBorders.add(selectedBorder);
		
		Stack stack = new Stack();
		stack.add(spellImageButton);
		stack.add(selectedBorder);
		
		add(stack).center().padLeft(paddingWidth).padTop(firstRow ? paddingWidth :  paddingWidth / 2f).
			padBottom(lastRow ? paddingWidth : paddingWidth / 2f);
		spellImageButtons.add(spellImageButton);
		
		maxImageWidth = Math.max(maxImageWidth, Math.max(selectedBorder.getWidth(), spellImageButton.getWidth()));
	}
	
	private void addLabelsCell(Spell spell, boolean firstRow, boolean lastRow) {
		Label spellNameLabel = new Label(spell.getName(), getSpellNameStyle());
		spellNameLabel.setAlignment(Align.left);
		spellNameLabel.setWrap(true);
		spellNameLabels.add(spellNameLabel);

		String magicColor = getSpellRequirementColor(spell.getMinimumMagicRequired(), currentCharacterMagic);
		String powerPointsColor = getSpellRequirementColor(spell.getMinimumMagicRequired(), currentCharacterPowerPoints);

		String statsSpellText = String.format(Localization.getInstance().formatTranslation("PjCreation",
						"spellMagicPPRequired"), magicColor, spell.getMinimumMagicRequired(),
				powerPointsColor, spell.getPowerPointsNeeded());
		ExtendedTextButton<Spell> spellDescriptionTextButton = new ExtendedTextButton<Spell> (
				"[BLACK]" + spell.getDescription() + "\n" + statsSpellText + "[]", getSpellDescriptionStyle());
		spellDescriptionTextButton.setTag(spell);
		spellDescriptionTextButton.getLabel().setAlignment(Align.top | Align.left);
		spellDescriptionTextButton.getLabel().setWrap(true);
		spellDescriptionTextButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				@SuppressWarnings("unchecked")
				ExtendedTextButton<Spell> spellDescription  = (ExtendedTextButton<Spell>) actor;
				for(SpellListControlListener listener: listeners) {
					AudioManager.getInstance().playSound(AssetRepository.getInstance().getSound("click_button"));
					listener.spellClicked(spellDescription.getTag());
				}
			}			
	});
		spellDescriptionTextButtons.add(spellDescriptionTextButton);
		
		Table group = new Table();
		group.align(Align.left);
		group.add(spellNameLabel);
		group.row();
		group.add(spellDescriptionTextButton);
		group.pack();
		add(group).left().expandX().padLeft(paddingWidth).padTop(firstRow ? paddingWidth : paddingWidth / 2f).
			padBottom(lastRow ? paddingWidth : paddingWidth / 2f).padRight(paddingWidth);
		labelsGroups.add(group);
	}

	private String getSpellRequirementColor(int minimumValue, int currentValue) {
		if(currentValue == UndefinedIntValue || currentValue >= minimumValue) {
			return "BLACK";
		}
		return "ORANGE";
	}

	private LabelStyle getSpellNameStyle() {
		return StageScreenHelper.getLabelStyle(Color.WHITE);
	}
	
	private TextButtonStyle getSpellDescriptionStyle() {
		return new TextButtonStyle(null, null, null, FontHelper.getSmallLabelsFont(true));
	}
	
	public void setSpellSelected(Spell spell, boolean selected) {
		for(int i = 0; i < spellImageButtons.size(); i++) {
			if(spellImageButtons.get(i).getTag().getId() == spell.getId()) {
				selectedBorders.get(i).setVisible(selected);
				break;
			}
		}
	}

	public void updateSpellsDescriptionStyle(int currentCharacterMagic, int currentCharacterPowerPoints) {
		this.currentCharacterMagic = currentCharacterMagic;
		this.currentCharacterPowerPoints = currentCharacterPowerPoints;

		for(ExtendedTextButton<Spell> spellButton: spellDescriptionTextButtons)	{
			Spell spell = spellButton.getTag();
			String magicColor = getSpellRequirementColor(spell.getMinimumMagicRequired(), currentCharacterMagic);
			String powerPointsColor = getSpellRequirementColor(spell.getMinimumMagicRequired(), currentCharacterPowerPoints);
			String statsSpellText = String.format(Localization.getInstance().formatTranslation("PjCreation",
							"spellMagicPPRequired"), magicColor, spell.getMinimumMagicRequired(),
							powerPointsColor, spell.getPowerPointsNeeded());
			spellButton.setText("[BLACK]" + spell.getDescription() + "\n" + statsSpellText + "[]");
		}
	}
}