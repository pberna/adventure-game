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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.games.StoredGame;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.utils2D.graphics.ImageManipulationHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StoredGameControl extends Stack {
    private Image selectedGameImage;

    private StoredGame storedGame;
    private ArrayList<StoredGameControlListener> listeners;

    public StoredGameControl(StoredGame storedGame) {
        this.storedGame = storedGame;
        listeners = new ArrayList<StoredGameControlListener>();
        buildControls();
        pack();
    }

    private void buildControls() {
        //backgroundImage
        ImageButton backgroundImageButton = new ImageButton(new TextureRegionDrawable(AssetRepository.getInstance().
                getTextureRegion("widgets", "load_game_background")));
        backgroundImageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (StoredGameControlListener listener : listeners) {
                    AudioManager.getInstance().playSound(AssetRepository.getInstance().getSound("click_button"));
                    listener.storedGameClicked(storedGame);
                }
            }
        });
        add(backgroundImageButton);

        //mainTable
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        add(mainTable);

        //portraitImage
        String portraitAtlas = storedGame.getCharacterPortrait().startsWith(PortraitsListControl.ManTextuxePrefix)
                ? PortraitsListControl.ManTextureAtlas : PortraitsListControl.WomanTextureAtlas;
        Image portraitImage = new Image(AssetRepository.getInstance().getTextureRegion(portraitAtlas, storedGame.getCharacterPortrait()));
        portraitImage.setTouchable(Touchable.disabled);
        float portraitPadding = portraitImage.getWidth() * 0.15f;
        mainTable.add(portraitImage).center().padLeft(portraitPadding * 1.5f).padRight(portraitPadding).width(portraitImage.getWidth());

        //rightTable
        Table rightTable = new Table();
        mainTable.add(rightTable).left().expandX().padTop(portraitPadding * 0).padBottom(portraitPadding * 0);

        //adventureNameLabel
        Label adventureNameLabel = createLabel(storedGame.getAdventureTitleTranslated(), getAdventureNameLabelStyle());
        rightTable.add(adventureNameLabel).left().row();

        //characterNameLabel
        Label characterNameLabel = createLabel(storedGame.getCharacterName(), getAdventureNameLabelStyle());
        rightTable.add(characterNameLabel).left().width(characterNameLabel.getWidth()).row();

        //lifePowerPointsControl
        LifePowerPointsControl lifePowerPointsControl = new LifePowerPointsControl();
        lifePowerPointsControl.setTouchable(Touchable.disabled);
        lifePowerPointsControl.updatePointsLabel(storedGame.getCurrentLifePoints(), storedGame.getMaximumLifePoints(),
                storedGame.getCurrentPowerPoints(), storedGame.getMaximumPowerPoints(), storedGame.getCurrentLuckPoints(),
                storedGame.getMaximumLuckPoints());
        rightTable.add(lifePowerPointsControl).left().row();

        //adventureInfoLabel
        Date date = storedGame.getCreationDate();
        DateFormat dayFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Localization.getInstance().getLocale());
        DateFormat timeFormat =  DateFormat.getTimeInstance(DateFormat.SHORT, Localization.getInstance().getLocale());
        Label adventureInfoLabel = createLabel(dayFormat.format(date) + " " + timeFormat.format(date)
                //+ " " + String.valueOf((int) storedGame.getProgressPercentage()) + " %"
                , getAdventureInfoLabelStyle());
        rightTable.add(adventureInfoLabel).left().row();

        //selectedGameImage
        selectedGameImage = new Image(new TextureRegionDrawable(AssetRepository.getInstance().
                getTextureRegion("widgets", "selected_game_border")));
        selectedGameImage.setTouchable(Touchable.disabled);
        selectedGameImage.setVisible(false);
        add(selectedGameImage);

        //deleteGameButton
        TextureRegion deleteButtonTexture = AssetRepository.getInstance().getTextureRegion("widgets", "delete_button");
        ImageButton deleteGameButton = new ImageButton(new TextureRegionDrawable(deleteButtonTexture), new TextureRegionDrawable(
                ImageManipulationHelper.moveTextureRegionForPressed(deleteButtonTexture)));
        StageScreenHelper.addOnClickSound(deleteGameButton, AssetRepository.getInstance().getSound("click_button"));
        deleteGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (StoredGameControlListener listener : listeners) {
                    listener.deleteStoredGame(storedGame);
                }
            }
        });

        //deleteGameButton container
        Table deleteGameButtonTable = new Table();
        deleteGameButtonTable.add(deleteGameButton);
        deleteGameButtonTable.right().top();

        add(deleteGameButtonTable);
    }

    private Label createLabel(String text, Label.LabelStyle labelStyle) {
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        label.setAlignment(Align.left);
        label.setTouchable(Touchable.disabled);
        return label;
    }

    private Label.LabelStyle getAdventureNameLabelStyle() {
        return StageScreenHelper.getLabelStyle(Color.BLACK);
    }

    private Label.LabelStyle getAdventureInfoLabelStyle() {
        return new Label.LabelStyle(FontHelper.getReadTextForLabelFont(), Color.BLACK);
    }

    public void addListener(StoredGameControlListener listener) {
        listeners.add(listener);
    }

    public void setSelected(boolean selected) {
        selectedGameImage.setVisible(selected);
    }

    public StoredGame getStoredGame() {
        return storedGame;
    }
}
