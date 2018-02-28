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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.utils2D.graphics.AnimationsHelper;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;

public class EffectSufferedControl extends HorizontalGroup {

    private static final float FadeInAnimationDuration = 0.75f;
    private static final float StayAnimationDuration = 0.75f;
    private static final float FadeOutAnimationDuration = 1.25f;

    private Image image;
    private Label label;
    private int value;


    public EffectSufferedControl(String imageName) {
        value = 0;

        buildControls(imageName);
    }

    private void buildControls(String imageName) {

        image = new Image(AssetRepository.getInstance().getTextureRegion("widgets", imageName));
        label = new Label((value > 0 ? "+" : "") + String.valueOf(value), getLabelStyle(Color.WHITE));

        //add controls
        addActor(image);
        addActor(label);
        pack();

        //hide
        AnimationsHelper.hideActorByAlpha(this);

        //cannot be touched
        setTouchable(Touchable.disabled);
    }

    private Label.LabelStyle getLabelStyle(Color color) {
        return StageScreenHelper.getSmallLabelStyle(color);
    }

    public void startShowAnimation(int value, float delay) {
        setValue(value);
        ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(this);

        Action delayAction = delay > 0 ?  AnimationsHelper.getDelayAction(delay) : null;
        Action fadeInAction = AnimationsHelper.getFadeInAction(FadeInAnimationDuration);
        Action stayAction = AnimationsHelper.getDelayAction(StayAnimationDuration);
        Action fadeOutAction = AnimationsHelper.getFadeOutAction(FadeOutAnimationDuration);

        float totalDuration = delay + FadeInAnimationDuration + StayAnimationDuration + FadeOutAnimationDuration;
        Action globalAction = delayAction != null
                ? AnimationsHelper.getSequenceAction(delayAction, fadeInAction, stayAction, fadeOutAction)
                : AnimationsHelper.getSequenceAction(fadeInAction, stayAction, fadeOutAction);
        addAction(globalAction);

        createTaskToRemoveFromContinousRendering(totalDuration);
    }

    private void setValue(int value) {
        this.value = value;
        label.setText((value > 0 ? "+" : "") + String.valueOf(this.value));
        Color color = Color.WHITE;
        if(value > 0) {
            color = Color.GREEN;
        } else {
            color = Color.ORANGE;
        }
        label.setStyle(getLabelStyle(color));
    }

    private void createTaskToRemoveFromContinousRendering(final float duration) {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(
                        getObjectToRemoveFromContinousRendering());
            }
        }, duration);
    }

    protected EffectSufferedControl getObjectToRemoveFromContinousRendering() {
        return this;
    }

    public float getTotalDurationWithoutDelay() {
        return FadeInAnimationDuration + StayAnimationDuration + FadeOutAnimationDuration;
    }
}
