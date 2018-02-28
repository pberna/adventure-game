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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.screens.ExitListener;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.controls.LifePowerPointsControl;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.windows.AnimableWindow;

import java.util.ArrayList;


public class UseLuckWindow extends AnimableWindow implements ILocalizable {

    private static final int MaximumLuckPoints = 9;

    private int paddingSize;
    private int halfPaddingSize;
    private ArrayList<ExitListener> listeners;
    private int luckPoints;
    private Character character;

    private Label titleLabel;
    private Label useLabel;
    private ExtendedImageButton<Integer> addLuckPoints;
    private Label luckPointsToUseLabel;
    private ExtendedImageButton<Integer> substractLuckPoints;
    private LifePowerPointsControl lifePowerPointsControl;
    private ExtendedImageButton<Label> acceptButton;

    public UseLuckWindow() {
        super();
        paddingSize = (int)(Gdx.graphics.getHeight() * 0.03f);
        halfPaddingSize = (int)(paddingSize / 2.0f);
        listeners = new ArrayList<ExitListener>();
        luckPoints = 0;
        character = null;

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
        titleLabel = new Label(Localization.getInstance().getTranslation("PjInformation", "luck"), getLabelStyle());
        titleLabel.setAlignment(Align.center);
        add(titleLabel).padTop(halfPaddingSize).padBottom(paddingSize).colspan(4);
        row();

        //useLabel
        useLabel = new Label(Localization.getInstance().getTranslation("InGame", "useLuck"), getLabelStyle());
        useLabel.setAlignment(Align.center);
        add(useLabel).padBottom(paddingSize).padRight(halfPaddingSize);

        //substractLuckPoints
        substractLuckPoints = new ExtendedImageButton<Integer>(
                new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "substract_button")),
                new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "substract_button_pressed")))
                .setTag(0);
        StageScreenHelper.addOnClickSound(substractLuckPoints, AssetRepository.getInstance().getSound("click_button"));
        substractLuckPoints.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                decreaseLuck();
            }
        });
        add(substractLuckPoints).padBottom(paddingSize).padLeft(halfPaddingSize).padRight(halfPaddingSize);

        //useLabel
        luckPointsToUseLabel = new Label(String.valueOf(luckPoints), getLuckPointsLabelStyle());
        luckPointsToUseLabel.setAlignment(Align.center);
        add(luckPointsToUseLabel).padBottom(paddingSize).padLeft(halfPaddingSize).padRight(halfPaddingSize);

        //addLuckPoints
        addLuckPoints = new ExtendedImageButton<Integer>(
                new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "add_button")),
                new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "add_button_pressed")))
                .setTag(0);
        StageScreenHelper.addOnClickSound(addLuckPoints, AssetRepository.getInstance().getSound("click_button"));
        addLuckPoints.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                increaseLuck();
            }
        });
        add(addLuckPoints).padBottom(paddingSize).padLeft(halfPaddingSize);
        row();

        //lifePowerPointsControl
        lifePowerPointsControl = new LifePowerPointsControl();
        lifePowerPointsControl.setLifePointsVisible(false);
        lifePowerPointsControl.setPowerPointsVisible(false);
        add(lifePowerPointsControl).padBottom(paddingSize).colspan(4);
        row();

        //acceptButton
        acceptButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("Common", "acceptButtonLabel"), this, AssetRepository.getInstance().getSound("click_button"));
        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for(ExitListener listener:listeners) {
                    listener.exit(this);
                }
            }
        });
        this.getCells().peek().padBottom(paddingSize).colspan(4);
    }

    private Label.LabelStyle getLabelStyle() {
        return StageScreenHelper.getLabelStyle(Color.BLACK);
    }

    private Label.LabelStyle getLuckPointsLabelStyle() {
        return StageScreenHelper.getLabelStyle(Color.WHITE);
    }

    private void increaseLuck() {
        setLuckPoints(luckPoints + 1);
    }

    private void decreaseLuck() {
        setLuckPoints(luckPoints - 1);
    }

    public void setLuckPoints(int newLuckPoints) {
        int maxLuckPoints;
        if(character != null) {
            maxLuckPoints = Math.min(MaximumLuckPoints, character.getCurrentLuckPoints());
        } else {
            maxLuckPoints = MaximumLuckPoints;
        }
        luckPoints = Math.max(0, Math.min(newLuckPoints, maxLuckPoints));
        luckPointsToUseLabel.setText(String.valueOf(luckPoints));
    }

    public void addListener(ExitListener listener) {
        listeners.add(listener);
    }

    @Override
    public void refreshLocalizableItems() {
        titleLabel.setText(Localization.getInstance().getTranslation("PjInformation", "luck"));
        useLabel.setText(Localization.getInstance().getTranslation("InGame", "useLuck"));
        acceptButton.getTag().setText(Localization.getInstance().getTranslation("Common", "acceptButtonLabel"));
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        if(this.character != null) {
            lifePowerPointsControl.updatePointsLabel(this.character);
        }
    }

    public int getLuckPoints() {
        return this.luckPoints;
    }
}
