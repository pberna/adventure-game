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

package com.pberna.adventure.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.utils2D.graphics.AnimationsHelper;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.graphics.ImageManipulationHelper;

import java.util.ArrayList;

public abstract class BaseController implements ILocalizable, Disposable {
	private static final float ShowHideTransitionsBetweenScreensTime = 0.2f;

	protected final Game game;
	protected BaseController parentController;
	protected ArrayList<Disposable> disposables;
	
	public BaseController(final Game game, BaseController parentController){
		this.game = game;
		this.parentController = parentController;
		disposables = new ArrayList<Disposable>();
	}
		
	public abstract void start();
	protected abstract void childEnded(BaseController childController);
	
	protected void end(){
		if(this.parentController != null) {
			this.parentController.childEnded(this);
		} else {
			Gdx.app.exit();
		}
	}
	
	public abstract void onBackPressed();

	protected void transitionBetweenScreens(Screen fromScreen, final Screen toScreen, final ITransitionCallback transitionCallback) {
		final BaseController objForContinuos = this;
		ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(objForContinuos);

		final BaseStageScreen fromBaseStageScreen = getCastedScreen(fromScreen);
		final BaseStageScreen toBaseStageScreen = getCastedScreen(toScreen);

		if(fromBaseStageScreen != null) {
			Action fadeIn = AnimationsHelper.getFadeInAction(ShowHideTransitionsBetweenScreensTime);
			fromBaseStageScreen.getDarkBackground().toFront();
			fromBaseStageScreen.getDarkBackground().addAction(fadeIn);
		}

		if(toBaseStageScreen != null) {
			ImageManipulationHelper.setActorNonTransparent(toBaseStageScreen.getDarkBackground());
			toBaseStageScreen.getDarkBackground().toFront();
		}

		if(fromBaseStageScreen != null) {
			Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					endTransitionBetweenScreens(fromBaseStageScreen, toBaseStageScreen, toScreen, transitionCallback, objForContinuos);
				}
			}, ShowHideTransitionsBetweenScreensTime);
		} else {
			endTransitionBetweenScreens(fromBaseStageScreen, toBaseStageScreen, toScreen, transitionCallback, objForContinuos);
		}
	}

	private BaseStageScreen getCastedScreen(Screen screen) {
		return screen instanceof BaseStageScreen ? (BaseStageScreen) screen : null;
	}

	private void endTransitionBetweenScreens(final BaseStageScreen fromBaseStageScreen, final BaseStageScreen toBaseStageScreen, Screen toScreen,
											 final ITransitionCallback transitionCallback, final BaseController objForContinuos) {
		if(transitionCallback != null) {
			transitionCallback.callback();
		}
		game.setScreen(toScreen);
		ImageManipulationHelper.setActorTransparent(fromBaseStageScreen.getDarkBackground());
		if(toBaseStageScreen != null) {
			Action fadeOut = AnimationsHelper.getFadeOutAction(ShowHideTransitionsBetweenScreensTime);
			toBaseStageScreen.getDarkBackground().addAction(fadeOut);
			Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(objForContinuos);
				}
			}, ShowHideTransitionsBetweenScreensTime);
		} else {
			ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(objForContinuos);
		}
	}

	@Override
	public void dispose() {
		for(Disposable disposable : disposables) {
			if(disposable != null) {
				disposable.dispose();
			}
		}
	}

}
