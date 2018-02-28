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

package com.pberna.engine.screens.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Timer;
import com.pberna.adventure.controllers.ITransitionCallback;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.screens.shapes.LineRectangle;
import com.pberna.engine.utils2D.graphics.AnimationsHelper;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.graphics.IAnimableActor;

public class AnimableWindow extends Window implements IAnimableActor{
	
	protected float originalWidth;
	protected float originalHeight;
	private Image windowBackGround;
	private BaseStageScreen parentScreen;
	private LineRectangle border;
	
	public AnimableWindow(){
		super("", getWindowStyle());

		originalWidth = 0;
		originalHeight = 0;
		setWindowBackGround(null);
		setParentScreen(null);
		setBorder(null);
	}
	
	protected static WindowStyle getWindowStyle() {
		WindowStyle windowStyle = new WindowStyle();
		windowStyle.titleFont = FontHelper.getLabelsFont();
		windowStyle.titleFontColor = Color.BLACK;
						
		return windowStyle;
	}

	@Override
	public float getOriginalWidth() {
		return this.originalWidth;		
	}

	@Override
	public float getOriginalHeight() {
		return this.originalHeight;
	}

	public Image getWindowBackGround() {
		return windowBackGround;
	}

	public void setWindowBackGround(Image windowBackGround) {
		this.windowBackGround = windowBackGround;
	}

	public BaseStageScreen getParentScreen() {
		return parentScreen;
	}

	public void setParentScreen(BaseStageScreen parentScreen) {
		this.parentScreen = parentScreen;
	}

	public LineRectangle getBorder() {
		return border;
	}

	public void setBorder(LineRectangle border) {
		this.border = border;
	}

	public void showPopUp(float animationDuration, final boolean removeContinuosRendering) {
		ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(this);
		setVisible(true);
		toFront();
		addAction(AnimationsHelper.getShowPopUpAction(this, getOriginalWidth(), getOriginalHeight(), animationDuration));
		if(windowBackGround != null) {
			windowBackGround.addAction(AnimationsHelper.getFadeInAction(animationDuration, 0.85f));
		}
		final IAnimableActor windowToRemoveContinuos = this;

		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				if(parentScreen != null && border != null) {
					parentScreen.addLineShape(border);
				}
				if (removeContinuosRendering) {
					ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(windowToRemoveContinuos);
				}
			}
		}, animationDuration);
	}

	public void hidePopUp(float animationDuration) {
		if(parentScreen != null && border != null) {
			parentScreen.removeLineShape(border);
		}

		ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(this);
		addAction(AnimationsHelper.getHidePopUpAction(this, getOriginalWidth(), getOriginalHeight(), animationDuration));
		if(windowBackGround != null) {
			windowBackGround.addAction(AnimationsHelper.getFadeOutAction(animationDuration));
		}

		final IAnimableActor windowToRemoveContinuos = this;
		Timer.schedule(new Timer.Task() {

			@Override
			public void run() {
				ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(windowToRemoveContinuos);
				setVisible(false);
			}
		}, animationDuration);
	}

	public void showFromAboveScreenAndReturn(float showHideAnimationDuration, float stayAnimationDuration,
											 ITransitionCallback callback) {
		ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(this);
		setKeepWithinStage(false);
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		setPosition((screenWidth - getWidth()) / 2f, screenHeight);
		setVisible(true);
		toFront();

		addAction(getFromAboveAndReturnAnimation(showHideAnimationDuration, stayAnimationDuration, screenHeight));

		final IAnimableActor windowToRemoveContinuos = this;
		final ITransitionCallback finalCallback = callback;
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				setVisible(false);
				ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(windowToRemoveContinuos);
				if(finalCallback != null) {
					finalCallback.callback();
				}
			}
		}, (showHideAnimationDuration * 2f) + stayAnimationDuration);
	}

	private Action getFromAboveAndReturnAnimation(float showHideAnimationDuration, float stayAnimationDuration, float screenHeight) {
		Action fromAboveAction = AnimationsHelper.getMoveToAction(getX(), screenHeight - getHeight(), showHideAnimationDuration);
		Action delayAction = AnimationsHelper.getDelayAction(stayAnimationDuration);
		Action returnAction = AnimationsHelper.getMoveToAction(getX(), screenHeight, showHideAnimationDuration);

		return AnimationsHelper.getSequenceAction(fromAboveAction, delayAction, returnAction);
	}
}
