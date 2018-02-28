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

package com.pberna.adventure.screens.windows;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Difficulty;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.controls.RollDiceControl;
import com.pberna.adventure.screens.controls.RollDiceControlListener;
import com.pberna.engine.screens.windows.AnimableWindow;

public class AttributeSkillCheckWindow extends AnimableWindow implements ILocalizable {
	private Label titleLabel;
	private Label subTitleLabel;
	private Image successImage;
	private Label successLabel;
	private Image failImage;
	private Label failLabel;
	private Label waitingMessage;
	private RollDiceControl rollDiceControl;
	private ExtendedImageButton<Label> continueButton;
	
	private int paddingSize;
	private int halfPaddingSize;
	private Difficulty difficulty;
	private ArrayList<AttributeSkillCheckWindowEventsListener> listeners;
	private boolean checkPassed;
	
	public AttributeSkillCheckWindow() {
		super();
		paddingSize = (int)(Gdx.graphics.getHeight() * 0.03f);
		halfPaddingSize = (int)(paddingSize / 2.0f);
		difficulty = new Difficulty(0, "");
		listeners = new ArrayList<AttributeSkillCheckWindowEventsListener>();
		checkPassed = false;
				
		buildWindow();
		pack();
				
		setModal(true);
		setMovable(false);
		setResizable(false);
		setBackground(new TextureRegionDrawable(new TextureRegion(AssetRepository.getInstance().getTexture("old_page_dark"))));
		
		originalWidth = getWidth();
		originalHeight = getHeight();
	}
		
	private void buildWindow() {
		defaults().padLeft(paddingSize).padRight(paddingSize);
		
		//title label
		titleLabel = new Label(String.format(Localization.getInstance().getTranslation("InGame", "diceRollOf"), ""), getTitleLabelStyle());
		titleLabel.setAlignment(Align.center);
		add(titleLabel).expandX().padTop(paddingSize);
		row();
		
		//subTitle Label
		subTitleLabel = new Label("  ", getSubTitleLabelStyle());
		subTitleLabel.setAlignment(Align.center);
		add(subTitleLabel).expandX();		
		row();
		
		//roll dice control		
		rollDiceControl = new RollDiceControl();
		rollDiceControl.addListener(new RollDiceControlListener() {			
			@Override
			public void rollFinished(int totalRollResult) {
				//roll finished
				rollEnded(totalRollResult);				
			}
		});		
		add(rollDiceControl).padTop(halfPaddingSize).width(rollDiceControl.getWidth()).height(rollDiceControl.getHeight()).center();		
		row();
		
		//success message
		successImage = new Image(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "success")));
		successLabel = new Label("  " + String.format(Localization.getInstance().getTranslation("InGame", "successSkillAttributeDiceRoll"), ""), 
				getSubTitleLabelStyle());
		successLabel.setAlignment(Align.center);
		HorizontalGroup groupSucess = StageScreenHelper.createHorizontalGroupWithActors(successImage, successLabel);
				
		//fail message
		failImage = new Image(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "fail")));
		failLabel = new Label("  " + String.format(Localization.getInstance().getTranslation("InGame", "failSkillAttributeDiceRoll"), ""), 
				getSubTitleLabelStyle());
		failLabel.setAlignment(Align.center);				
		HorizontalGroup groupfail = StageScreenHelper.createHorizontalGroupWithActors(failImage, failLabel);
		
		//waiting message
		waitingMessage = new Label(Localization.getInstance().getTranslation("InGame", "rollingDiceMessage"), getSubTitleLabelStyle());
		waitingMessage.setAlignment(Align.center);
		
		//message row
		Stack stack = StageScreenHelper.createStackWithActors(groupSucess, groupfail, waitingMessage);		
		add(stack).center().padBottom(halfPaddingSize);
		row();
		
		//continue button		
		continueButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
				getTranslation("InGame", "continue"), this, AssetRepository.getInstance().getSound("click_button"));
		continueButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for(AttributeSkillCheckWindowEventsListener listener: listeners) {
					listener.checkFinished(checkPassed);
				}
			}			
		});		
		this.getCells().peek().padBottom(paddingSize * 1.5f);			
	}
	
	private LabelStyle getTitleLabelStyle() {
		return StageScreenHelper.getLabelStyle(Color.BLACK);
	}
	
	private LabelStyle getSubTitleLabelStyle() {
		return new LabelStyle(FontHelper.getOptionsTextForLabelFont(), Color.BLACK);
	}
	
	public void setCheckInfo(Attribute attribute, Difficulty difficulty, int baseAttributeValue, int luckModifierValue) {
		setCheckInfo(attribute.getName(), difficulty, baseAttributeValue, luckModifierValue);
	}
	
	public void setCheckInfo(Skill skill, Difficulty difficulty, int baseAttributeValue, int luckModifierValue) {
		setCheckInfo(skill.getName(), difficulty, baseAttributeValue, luckModifierValue);
	}
	
	private void setCheckInfo(String name, Difficulty difficulty, int baseAttributeValue, int luckModifierValue) {
		setTitleWord(name);
		setSubTitle(String.format(Localization.getInstance().getTranslation("InGame", "difficultyLabelSplitted", false),
				difficulty.getDifficulty(), difficulty.getName()));
		successLabel.setText("  " + String.format(Localization.getInstance().getTranslation(
				"InGame", "successSkillAttributeDiceRoll"), name));
		failLabel.setText("  " + String.format(Localization.getInstance().getTranslation(
				"InGame", "failSkillAttributeDiceRoll"), name));
		this.difficulty = difficulty;
		rollDiceControl.setBaseValue(baseAttributeValue);
		rollDiceControl.setLuckModifierValue(luckModifierValue);
	}
	
	public void setTitleWord(String word) {
		titleLabel.setText(String.format(Localization.getInstance().getTranslation("InGame", "diceRollOf"), word));
	}
	
	public void setSubTitle(String text) {
		subTitleLabel.setText(text);
		Cell<Label> cell = getCell(subTitleLabel);
		if(cell != null) {
			cell.width(subTitleLabel.getWidth());
		}
	}
	
	public void startCheck(float delay) {
		setSuccessMessageVisible(false);
		setFailMessageVisible(false);
		setWaitingMessageVisible(true);		
		StageScreenHelper.setImageButtonVisible(continueButton, false);
		
		if(delay > 0) {
			Timer.schedule(new Task() {
				
				@Override
				public void run() {
					rollDiceControl.startRoll();					
				}
			}, delay);
		} else {		
			rollDiceControl.startRoll();
		}
	}
	
	protected void rollEnded(int totalRollResult) {
		if(totalRollResult >= difficulty.getDifficulty()) {
			checkPassed = true;
			setSuccessMessageVisible(true);
			setFailMessageVisible(false);
		} else {
			checkPassed = false;
			setSuccessMessageVisible(false);
			setFailMessageVisible(true);
		}
		
		setWaitingMessageVisible(false);
		StageScreenHelper.setImageButtonVisible(continueButton, true);				
	}
	
	public void setSuccessMessageVisible(boolean visible) {
		successImage.setVisible(visible);
		successLabel.setVisible(visible);
	}
	
	public void setFailMessageVisible(boolean visible) {
		failImage.setVisible(visible);
		failLabel.setVisible(visible);
	}
	
	public void setWaitingMessageVisible(boolean visible) {
		waitingMessage.setVisible(visible);		
	}
	
	public void addListener(AttributeSkillCheckWindowEventsListener listener) {
		listeners.add(listener);
	}

	@Override
	public void refreshLocalizableItems() {
		waitingMessage.setText(Localization.getInstance().getTranslation("InGame", "rollingDiceMessage"));
	}

	public int getLuckPointsUsed() {
		return rollDiceControl.getLuckModifierValue();
	}
}
