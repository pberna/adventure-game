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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.adventure.screens.StageScreenHelper;

public class RollDiceControl extends Table {
	public static int DiceFaces = 8;
	
	private static final String MainTextMask = "[BLACK]%d []%s[BLACK]+ %s = %s[]";
	private static final String UndefinedValueString = "?";
	private static final int UndefinedValueInteger = -1;
	
	private static final int NumRowsAnimationFrame = 2;
	private static final int NumColumnsAnimationFrame = 4;
	private static final float TimeDiceFrameVisible = 0.1f;
	private static final float AnimationDuration = 1.5f;
	
	private Label titleLabel;
	private Label mainLabel;
	private ArrayList<Image> diceImages;
	private int currentVisibleDiceImageIndex;
		
	private int baseValue;
	private int modifierValue;
	private float animationStateTime;
	private int []rollDiceImageSequence;
	boolean rollInProgress;
	private float startStateTime;
	private ArrayList<RollDiceControlListener> listeners;
	private int luckModifierValue;
		
	public RollDiceControl() {
		titleLabel = StageScreenHelper.createCenteredLabel("", StageScreenHelper.getLabelStyle(Color.BLACK));
		mainLabel = StageScreenHelper.createCenteredLabel(String.format(MainTextMask, 0, "", UndefinedValueString, UndefinedValueString),
				getMainLabelStyle());
		diceImages = createDiceImages();		
		baseValue = 0;
		modifierValue = UndefinedValueInteger;
		animationStateTime = 0;	
		rollDiceImageSequence = createRollDiceImageSequence();
		rollInProgress = false;
		startStateTime = 0f;
		listeners = new ArrayList<RollDiceControlListener>();
		luckModifierValue = 0;

		createRows();
	}

	private Label.LabelStyle getMainLabelStyle() {
		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = AssetRepository.getInstance().getFont("ButtonFont", true);

		return labelStyle;
	}

	private ArrayList<Image> createDiceImages() {
		ArrayList<Image> diceImages = new ArrayList<Image>(NumRowsAnimationFrame * NumColumnsAnimationFrame);
		
		TextureRegion diceFramesSet = AssetRepository.getInstance().getTextureRegion("animations", "dice_faces");
		TextureRegion[][] diceFaces = diceFramesSet.split(diceFramesSet.getRegionWidth() / NumColumnsAnimationFrame , 
				diceFramesSet.getRegionHeight() / NumRowsAnimationFrame);
				
		for(int i = 0; i < NumRowsAnimationFrame; i++) {
			for(int j = 0; j < NumColumnsAnimationFrame; j++) {
				Image diceImage = new Image(new TextureRegionDrawable(diceFaces[i][j]));
				diceImages.add(diceImage);
			}
		}
		return diceImages;
	}
	
	private int[] createRollDiceImageSequence() {
		int [] sequence = new int[diceImages.size()];
		
		for(int i = 0; i < sequence.length; i++) {
			sequence[i] = -1;
		}
		
		for(int i = 0; i < sequence.length; i++) {
			int newIndex;
			do
			{
				newIndex = MathUtils.random(0, diceImages.size() - 1);				
			} while(AlreadyUsedInSequence(sequence, newIndex));
			sequence[i] = newIndex;
		}
		
		return sequence;
	}

	private boolean AlreadyUsedInSequence(int[] sequence, int newIndex) {
		for(int i = 0; i < sequence.length; i++) {
			if(sequence[i] == newIndex) {
				return true;
			}
		}
		return false;
	}

	private void createRows() {
		add(titleLabel).center().expandX().height(0);
		row();
			
		addDicesImagesRow();
		
		add(mainLabel).center().expandX();
		row();
		
		pack();
	}

	private void addDicesImagesRow() {
		Stack stack = new Stack();
		currentVisibleDiceImageIndex = 0;
		Image selectedDiceImage = diceImages.get(rollDiceImageSequence[currentVisibleDiceImageIndex]); 
		
		for(Image image:diceImages) {
			image.setVisible(image == selectedDiceImage);
			stack.add(image);
		}
		add(stack).center();
		row();
	}

	public int getBaseValue() {
		return baseValue;
	}

	public void setBaseValue(int baseValue) {
		this.baseValue = baseValue;
		updateMainLabel();		
	}

	private void updateMainLabel() {
		String modifierString;
		String totalString;
		String luckModifierString;

		if(modifierValue != UndefinedValueInteger) {
			modifierString = String.valueOf(modifierValue);
			totalString = String.valueOf(baseValue + luckModifierValue + modifierValue);
		} else {
			modifierString = UndefinedValueString;
			totalString = UndefinedValueString;
		}
		if(luckModifierValue > 0) {
			luckModifierString = "[GREEN]+ " + String.valueOf(luckModifierValue) + "[] ";
		} else if (luckModifierValue == 0) {
			luckModifierString = "";
		} else {
			luckModifierString = "[RED]+ " + String.valueOf(luckModifierValue) + "[] ";
		}

		mainLabel.setText(String.format(MainTextMask, baseValue, luckModifierString, modifierString, totalString));
	}	
	
	public String getTitleText() {
		return titleLabel.getText().toString();
	}
	
	public void setTitleText(String text) {
		titleLabel.setText(text);
		Cell<Label> cell = getCell(titleLabel);
		if(cell != null) {
			cell.height(text.equals("") ? 0 : titleLabel.getPrefHeight() * 1.1f);
		}
	}
	
	public void startRoll() {
		if(!rollInProgress) {			
			modifierValue = UndefinedValueInteger;
			updateMainLabel();
			rollInProgress = true;
			startStateTime = -1;
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
			
		animationStateTime += delta;
		
		if(rollInProgress) {
			if(startStateTime < 0) {
				startStateTime = animationStateTime;
			}
			
			if((animationStateTime - startStateTime) >= AnimationDuration) {
				//Gdx.app.log("RollDiceEnded", String.valueOf(startStateTime) + " " + String.valueOf(animationStateTime));
				endRoll();
			}
			else
			{
				int newDiceImageIndex = ((int)(animationStateTime / TimeDiceFrameVisible)) % rollDiceImageSequence.length;
				
				if(newDiceImageIndex != currentVisibleDiceImageIndex) {
					diceImages.get(rollDiceImageSequence[newDiceImageIndex]).setVisible(true);
					diceImages.get(rollDiceImageSequence[currentVisibleDiceImageIndex]).setVisible(false);
					currentVisibleDiceImageIndex = newDiceImageIndex;
				}
			}
		}
	}

	private void endRoll() {
		rollInProgress = false;
		modifierValue = MathUtils.random(1, DiceFaces);
		if(rollDiceImageSequence[currentVisibleDiceImageIndex] != (modifierValue - 1)) {
			diceImages.get(rollDiceImageSequence[currentVisibleDiceImageIndex]).setVisible(false);			
			diceImages.get(modifierValue - 1).setVisible(true);
		}
		updateMainLabel();
				
		for(RollDiceControlListener listener:listeners) {
			listener.rollFinished(baseValue + luckModifierValue + modifierValue);
		}
	}
	
	public void addListener(RollDiceControlListener listener) {
		listeners.add(listener);
	}

	public int getLuckModifierValue() {
		return this.luckModifierValue;
	}

	public void setLuckModifierValue(int luckModifierValue) {
		this.luckModifierValue = luckModifierValue;
		updateMainLabel();
	}
}
