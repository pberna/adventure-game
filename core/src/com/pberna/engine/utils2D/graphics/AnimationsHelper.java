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

package com.pberna.engine.utils2D.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;

public class AnimationsHelper {

	public static Action getFadeInAction(float duration) {
		return getFadeInAction(duration, Interpolation.fade);
	}
	
	public static Action getFadeInAction(float duration, Interpolation interpolationMode) {
		return getFadeInAction(duration, interpolationMode, 1f);
	}
	
	public static Action getFadeInAction(float duration, Interpolation interpolationMode, float alpha) {
		AlphaAction action = Actions.action(AlphaAction.class);
		action.setAlpha(alpha);
		action.setDuration(duration);
		action.setInterpolation(interpolationMode);
		
		return action;
	}
	
	public static Action getFadeInAction(float duration, float alpha) {
		return getFadeInAction(duration, Interpolation.fade, alpha);
	}
	
	public static Action getFadeOutAction(float duration) {
		return getFadeOutAction(duration, Interpolation.fade);
	}
	
	public static Action getFadeOutAction(float duration, Interpolation interpolationMode) {
		AlphaAction action = Actions.action(AlphaAction.class);
		action.setAlpha(0f);
		action.setDuration(duration);
		action.setInterpolation(interpolationMode);
		
		return action;
	}
	
	public static void hideActorByAlpha(Actor actor) {
		changeActorAlpha(actor, 0f);
	}
	
	public static void showActorByAlpha(Actor actor) {
		changeActorAlpha(actor, 1f);
	}
	
	private static void changeActorAlpha(Actor actor, float alpha) {
		Color color = actor.getColor();
		actor.setColor(color.r, color.g, color.b, alpha);
	}
	
	public static Action getSizeToAction(float width, float height, float duration) {
		return getSizeToAction(width, height, duration, Interpolation.swing);
	}
	
	public static Action getSizeToAction(float width, float height, float duration, Interpolation interpolationMode) {
		SizeToAction action = Actions.action(SizeToAction.class);
		action.setSize(width, height);
		action.setDuration(duration);
		action.setInterpolation(interpolationMode);
		
		return action;
	}
	
	public static Action getMoveToAction(float x, float y, float duration) {
		return getMoveToAction(x, y, duration, Interpolation.linear);
	}
	
	public static Action getMoveToAction(float x, float y, float duration, Interpolation interpolationMode) {
		MoveToAction action = Actions.action(MoveToAction.class);
		action.setPosition(x, y);
		action.setDuration(duration);
		action.setInterpolation(interpolationMode);
		
		return action;
	}
	
	public static Action getShowPopUpAction(Actor actor, float originalWidth, float originalHeight, float duration) {
		return getShowPopUpAction(actor, originalWidth, originalHeight, duration, Interpolation.exp10);
	}
	
	public static Action getShowPopUpAction(Actor actor, float originalWidth, float originalHeight, 
			float duration, Interpolation interpolation) {		
		Action sizeAction = getSizeToAction(originalWidth, originalHeight, duration, interpolation);
		Action moveAction = getMoveToAction(actor.getX() - (originalWidth / 2f), actor.getY() - (originalHeight / 2f), 
				duration, interpolation);
		
		ParallelAction action = Actions.action(ParallelAction.class);
		action.addAction(sizeAction);
		action.addAction(moveAction);
		
		return action;
	}
	
	public static Action getHidePopUpAction(Actor actor, float originalWidth, float originalHeight, float duration) {
		return getHidePopUpAction(actor, originalWidth, originalHeight, duration, Interpolation.exp10);
	}
	
	public static Action getHidePopUpAction(Actor actor, float originalWidth, float originalHeight, 
			float duration, Interpolation interpolation) {		
		Action sizeAction = getSizeToAction(0, 0, duration, interpolation);
		Action moveAction = getMoveToAction(actor.getX() + (originalWidth / 2f), actor.getY() + (originalHeight / 2f), 
				duration, interpolation);
		
		ParallelAction action = Actions.action(ParallelAction.class);
		action.addAction(sizeAction);
		action.addAction(moveAction);
		
		return action;
	}

	public static SequenceAction getSequenceAction(Action... actions) {
		SequenceAction sequenceAction = Actions.action(SequenceAction.class);
		for(Action action : actions) {
			sequenceAction.addAction(action);
		}

		return sequenceAction;
	}

	public static Action getDelayAction(float duration) {
		DelayAction action = Actions.action(DelayAction.class);
		action.setDuration(duration);

		return action;
	}

	public static Action getRotateByAction(float rotationAngle, float duration) {
		RotateByAction action = Actions.action(RotateByAction.class);
		action.setAmount(rotationAngle);
		action.setDuration(duration);

		return action;
	}

	public static Action getInfiniteAction(Action action) {
		RepeatAction repeatAction = Actions.action(RepeatAction.class);
		repeatAction.setAction(action);
		repeatAction.setCount(RepeatAction.FOREVER);

		return repeatAction;
	}
}
