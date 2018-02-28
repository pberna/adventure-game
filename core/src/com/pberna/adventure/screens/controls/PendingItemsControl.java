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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.utils2D.graphics.AnimationsHelper;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.graphics.PixmapHelper;

public class PendingItemsControl extends Stack {
	
	private static final int MaxCount = 9;
	private static final float LabelSizeMultiplier = 1.1f;
	private static final float AnimationDuration = 1f;

	private int pendingCount;
	private int beforePendingCount;
	private float originalWidth;
	private float originalHeight;
	
	private Label pendingCountLabel;
	private Image backgroudImage;
	
	public PendingItemsControl() {
		pendingCount = 0;
		beforePendingCount = 0;
		
		buildControl();
	}

	private void buildControl() {
		//count label
		pendingCountLabel = new Label(String.valueOf(pendingCount), getLabelStyle());
		pendingCountLabel.setAlignment(Align.center);
		
		//background image
		int sideSize = (int)(Math.max(pendingCountLabel.getWidth(), pendingCountLabel.getHeight()) * LabelSizeMultiplier);
		backgroudImage = new Image(new TextureRegionDrawable(new TextureRegion(PixmapHelper.getInstance().createPendingItemsBackground(sideSize, sideSize))));
		
		//add controls
		add(backgroudImage);
		add(pendingCountLabel);				
		pack();		
		
		//store original size
		originalWidth = getWidth();
		originalHeight = getHeight();
		
		//hide the control initially
		setWidth(0);
		setHeight(0);
		pendingCountLabel.setVisible(false);
		
		//cannot be touched
		setTouchable(Touchable.disabled);
	}
	
	private Label.LabelStyle getLabelStyle() {
		return StageScreenHelper.getSmallLabelStyle(Color.WHITE);
	}
	
	public void increaseCount(int increment) {
		setPendingCount(Math.max(0, Math.min(MaxCount, pendingCount + increment)));
		pendingCountLabel.setText(String.valueOf(pendingCount));
		
		if(pendingCount > 0 && beforePendingCount == 0) {
			startShowAnimation();
		} else if (pendingCount == 0 && beforePendingCount > 0){
			startHideAnimation();
		}
	}
	
	private void setPendingCount(int newPendingCount) {
		beforePendingCount = pendingCount;
		pendingCount = newPendingCount;
	}
		
	public void resetCount() {
		setPendingCount(0);
		pendingCountLabel.setText(String.valueOf(pendingCount));
		
		if (pendingCount == 0 && beforePendingCount > 0){
			startHideAnimation();
		}
	}

	public int getPendingCount() {
		return pendingCount;
	}
	
	private void startShowAnimation() {
		ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(this);
		Action action = AnimationsHelper.getSizeToAction(originalWidth, originalHeight, AnimationDuration);
		addAction(action);	
		createTaskToRemoveFromContinousRendering(true);
	}
	
	private void startHideAnimation() {		
		setWidth(0);
		setHeight(0);
		setPendingCountLabelVisible(false);
	}	
	
	private void createTaskToRemoveFromContinousRendering(final boolean pendingCountLabelVisible) {
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		    	setPendingCountLabelVisible(pendingCountLabelVisible);
		    	ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(
		    			getObjectToRemoveFromContinousRendering());		    	
		    }
		}, AnimationDuration);
	}
	
	protected PendingItemsControl getObjectToRemoveFromContinousRendering() {
		return this;
	}
	
	protected void setPendingCountLabelVisible(boolean visible) {
		pendingCountLabel.setVisible(visible);
	}
	
	public float getOriginalWidth() {
		return originalWidth;
	}
	
	public float getOriginalHeight() {
		return originalHeight;
	}
}
