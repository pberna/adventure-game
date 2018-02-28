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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.utils2D.graphics.PixmapHelper;

public class CounterControl extends Stack {
    private static final int MaxCount = 9;
    private static final float LabelSizeMultiplier = 1.1f;

    private int countValue;

    private Label countLabel;
    private Image backgroundImage;

    public CounterControl() {
        countValue = 0;

        buildControl();
    }

    private void buildControl() {
        //count label
        countLabel = new Label(String.valueOf(countValue), getLabelStyle());
        countLabel.setAlignment(Align.center);

        //background image
        int sideSize = (int)(Math.max(countLabel.getWidth(), countLabel.getHeight()) * LabelSizeMultiplier);
        backgroundImage = new Image(new TextureRegionDrawable(new TextureRegion(PixmapHelper.getInstance().createCounterBackground(sideSize, sideSize))));

        //add controls
        add(backgroundImage);
        add(countLabel);
        pack();

        //cannot be touched
        setTouchable(Touchable.disabled);
    }

    private Label.LabelStyle getLabelStyle() {
        return StageScreenHelper.getSmallLabelStyle(Color.WHITE);
    }

    public void setCountValue(int newPendingCount) {
        countValue = Math.max(0, Math.min(MaxCount, newPendingCount));
        countLabel.setText(String.valueOf(countValue));
    }

    public void resetCount() {
        setCountValue(0);
    }

    public int getCountValue() {
        return countValue;
    }
}
