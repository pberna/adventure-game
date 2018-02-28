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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ExtendedLocale;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.screens.windows.AnimableWindow;
import com.pberna.engine.utils2D.graphics.ImageManipulationHelper;
import com.pberna.engine.utils2D.graphics.PixmapHelper;
import java.util.ArrayList;
import java.util.Locale;

public class OptionsMenuWindow extends AnimableWindow implements ILocalizable {

    private int paddingSize;
    private int halfPaddingSize;
    private ArrayList<OptionsMenuEventsListener> listeners;

    private Label titleLabel;
    private ImageButton soundOnButton;
    private ImageButton soundOffButton;
    private ImageButton musicOnButton;
    private ImageButton musicOffButton;
    private Slider soundSlider;
    private Slider musicSlider;
    private Label soundLabel;
    private Label musicLabel;
    private ImageButton languageButton;
    private SelectBox<ExtendedLocale> languageSelector;
    private ExtendedImageButton<Label> exitButton;

    private float lastSoundVolume;
    private float lastMusicVolume;

    public OptionsMenuWindow() {
        super();
        paddingSize = (int)(Gdx.graphics.getHeight() * 0.03f);
        halfPaddingSize = (int)(paddingSize / 2.0f);
        listeners = new ArrayList<OptionsMenuEventsListener>();

        buildMenuOptions();
        pack();

        setModal(true);
        setMovable(false);
        setResizable(false);
        setBackground(new TextureRegionDrawable(new TextureRegion(AssetRepository.getInstance().getTexture("old_page_dark"))));

        originalWidth = getWidth();
        originalHeight = getHeight();

        AudioManager audioManager = AudioManager.getInstance();
        setSoundVolume(audioManager.getSoundVolume(), false);
        setMusicVolume(audioManager.getMusicVolume());
    }

    public void addListener(OptionsMenuEventsListener listener) {
        listeners.add(listener);
    }

    private void buildMenuOptions() {
        //title label
        titleLabel = new Label(Localization.getInstance().getTranslation("OutGame", "optionsTitle"),
                getTitleLabelStyle());
        titleLabel.setAlignment(Align.center);
        add(titleLabel).colspan(2).padTop(halfPaddingSize).padBottom(halfPaddingSize).padLeft(paddingSize).padRight(paddingSize);

        //sound row
        createSoundRow();

        //music row
        createMusicRow();

        //language row
        createLanguageRow();

        //exit button
        row();
        exitButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("OutGame", "exitOptionsLabel"), this, AssetRepository.getInstance().getSound("click_button"));
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().saveSettings();
                Localization.getInstance().saveSettings();
                for (OptionsMenuEventsListener listener : listeners) {
                    listener.exit();
                }
            }
        });
        this.getCells().peek().colspan(2).padBottom(paddingSize).padLeft(paddingSize).padRight(paddingSize);
    }

    private Label.LabelStyle getTitleLabelStyle() {
        return new Label.LabelStyle(FontHelper.getLabelsFont(), Color.BLACK);
    }

    private void createSoundRow() {
        row();

        //sound on button
        TextureRegion soundOnImage = AssetRepository.getInstance().getTextureRegion("widgets", "sound_on_button");
        soundOnButton = new ImageButton(new TextureRegionDrawable(soundOnImage),
                new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegion(soundOnImage, -1, -1)));
        StageScreenHelper.addOnClickSound(soundOnButton, AssetRepository.getInstance().getSound("click_button"));
        soundOnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lastSoundVolume = AudioManager.getInstance().getSoundVolume();
                setSoundVolume(AudioManager.MinVolume, true);
            }
        });
        soundOnButton.setVisible(true);

        //sound off button
        TextureRegion soundOffImage = AssetRepository.getInstance().getTextureRegion("widgets", "sound_off_button");
        soundOffButton = new ImageButton(new TextureRegionDrawable(soundOffImage),
                new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegion(soundOffImage, -1, -1)));
        StageScreenHelper.addOnClickSound(soundOffButton, AssetRepository.getInstance().getSound("click_button"));
        soundOffButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setSoundVolume(lastSoundVolume > AudioManager.MinVolume ? lastSoundVolume : AudioManager.MaxVolume, true);
            }
        });
        soundOffButton.setVisible(false);

        //add to stage
        Stack soundButtonsStack = StageScreenHelper.createStackWithActors(soundOnButton, soundOffButton);
        add(soundButtonsStack).padBottom(paddingSize).padLeft(paddingSize).padRight(halfPaddingSize);

        //add to window
        soundLabel = new Label(Localization.getInstance().getTranslation("OutGame", "soundOffText"), getTitleLabelStyle());
        soundSlider = new Slider(AudioManager.MinVolume, AudioManager.MaxVolume, (AudioManager.MaxVolume - AudioManager.MinVolume) / 20f,
                false, getSliderStyle());
        soundSlider.setValue(AudioManager.getInstance().getSoundVolume());
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setSoundVolume(soundSlider.getValue(), true);
            }
        });
        VerticalGroup soundGroup = StageScreenHelper.createVerticalGroupWithActors(soundLabel, soundSlider);
        soundGroup.fill();
        add(soundGroup).padBottom(paddingSize).padLeft(halfPaddingSize).padRight(paddingSize);
    }

    private Slider.SliderStyle getSliderStyle() {
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "slider_background"));
        style.knob = new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "slider_knob"));
        return style;
    }

    private void createMusicRow() {
        row();

        //music on button
        TextureRegion musicOnImage = AssetRepository.getInstance().getTextureRegion("widgets", "music_on_button");
        musicOnButton = new ImageButton(new TextureRegionDrawable(musicOnImage),
                new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegion(musicOnImage, -1, -1)));
        StageScreenHelper.addOnClickSound(musicOnButton, AssetRepository.getInstance().getSound("click_button"));
        musicOnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lastMusicVolume = AudioManager.getInstance().getMusicVolume();
                setMusicVolume(AudioManager.MinVolume);
            }
        });
        musicOnButton.setVisible(true);

        //music off button
        TextureRegion musicOffImage = AssetRepository.getInstance().getTextureRegion("widgets", "music_off_button");
        musicOffButton = new ImageButton(new TextureRegionDrawable(musicOffImage),
                new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegion(musicOffImage, -1, -1)));
        StageScreenHelper.addOnClickSound(musicOffButton, AssetRepository.getInstance().getSound("click_button"));
        musicOffButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setMusicVolume(lastMusicVolume > AudioManager.MinVolume ? lastMusicVolume : AudioManager.MaxVolume);
            }
        });
        musicOffButton.setVisible(false);

        //add to window
        Stack musicButtonsStack = StageScreenHelper.createStackWithActors(musicOnButton, musicOffButton);
        add(musicButtonsStack).padBottom(paddingSize).padLeft(paddingSize).padRight(halfPaddingSize);

        //music label
        musicLabel = new Label(Localization.getInstance().getTranslation("OutGame", "musicOffText"), getTitleLabelStyle());
        musicSlider = new Slider(AudioManager.MinVolume, AudioManager.MaxVolume, (AudioManager.MaxVolume - AudioManager.MinVolume) / 20f,
                false, getSliderStyle());
        musicSlider.setValue(AudioManager.getInstance().getMusicVolume());
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setMusicVolume(musicSlider.getValue());
            }
        });
        VerticalGroup musicGroup = StageScreenHelper.createVerticalGroupWithActors(musicLabel, musicSlider);
        musicGroup.fill();
        add(musicGroup).padBottom(paddingSize).padLeft(halfPaddingSize).padRight(paddingSize).expandX();
    }

    private void createLanguageRow() {
        row();

        //language image
        final TextureRegion languageImage = AssetRepository.getInstance().getTextureRegion("widgets", "language");
        languageButton = new ImageButton(new TextureRegionDrawable(languageImage),
                new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegion(languageImage, -1, -1)));
        languageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                languageSelector.setSelectedIndex((languageSelector.getSelectedIndex() + 1) % languageSelector.getItems().size);
            }
        });
        add(languageButton).padBottom(paddingSize).padLeft(paddingSize).padRight(halfPaddingSize);

        //language selector
        languageSelector = new SelectBox<ExtendedLocale>(getSelectBoxStyle());
        languageSelector.setItems(Localization.getInstance().getExtendedLocaleList());
        languageSelector.setSelected(Localization.getInstance().getExtendedLocale());
        languageSelector.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Locale newLocale = languageSelector.getSelected().getLocale();
                Localization.getInstance().setLocale(newLocale);
                Localization.getInstance().loadAllBundles();

                for(OptionsMenuEventsListener listener: listeners) {
                    listener.selectLanguage(newLocale);
                }
            }
        });

        add(languageSelector).padBottom(paddingSize).padLeft(halfPaddingSize).padRight(paddingSize).left();
    }

    private SelectBox.SelectBoxStyle getSelectBoxStyle() {
        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = FontHelper.getLabelsFont();
        selectBoxStyle.fontColor = Color.BLACK;
        selectBoxStyle.listStyle = getListStyle();
        selectBoxStyle.scrollStyle = getScrollStyle();

        return selectBoxStyle;
    }

    private List.ListStyle getListStyle() {
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = FontHelper.getLabelsFont();
        listStyle.fontColorSelected = Color.BLACK;
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.background = new TextureRegionDrawable(new TextureRegion(
                PixmapHelper.getInstance().createFillRectangle(paddingSize, paddingSize, Color.WHITE)));
        listStyle.selection = new TextureRegionDrawable(new TextureRegion(
                PixmapHelper.getInstance().createFillRectangle(paddingSize, paddingSize, Color.BLUE)));

        return listStyle;
    }

    private ScrollPane.ScrollPaneStyle getScrollStyle() {
        return new ScrollPane.ScrollPaneStyle();
    }

    private void setSoundVolume(float soundVolume, boolean playTestSound) {
        AudioManager audioManager = AudioManager.getInstance();
        audioManager.setSoundVolume(soundVolume);
        soundOnButton.setVisible(audioManager.getSoundVolume() > AudioManager.MinVolume);
        soundOffButton.setVisible(audioManager.getSoundVolume() <= AudioManager.MinVolume);
        soundSlider.setValue(audioManager.getSoundVolume());
        soundLabel.setText(audioManager.getSoundVolume() > AudioManager.MinVolume
                ? Localization.getInstance().getTranslation("OutGame", "soundOnText")
                : Localization.getInstance().getTranslation("OutGame", "soundOffText"));

        //sound to test volume
        if(playTestSound) {
            audioManager.playSound(AssetRepository.getInstance().getSound("pages"));
        }

    }

    private void setMusicVolume(float musicVolume) {
        AudioManager audioManager = AudioManager.getInstance();
        audioManager.setMusicVolume(musicVolume);
        musicOnButton.setVisible(audioManager.getMusicVolume() > AudioManager.MinVolume);
        musicOffButton.setVisible(audioManager.getMusicVolume() <= AudioManager.MinVolume);
        musicSlider.setValue(audioManager.getMusicVolume());
        musicLabel.setText(audioManager.getMusicVolume() > AudioManager.MinVolume
                ? Localization.getInstance().getTranslation("OutGame", "musicOnText")
                : Localization.getInstance().getTranslation("OutGame", "musicOffText"));
    }

    @Override
    public void refreshLocalizableItems() {
        titleLabel.setText(Localization.getInstance().getTranslation("OutGame", "optionsTitle"));
        AudioManager audioManager = AudioManager.getInstance();
        soundLabel.setText(audioManager.getSoundVolume() > AudioManager.MinVolume
                ? Localization.getInstance().getTranslation("OutGame", "soundOnText")
                : Localization.getInstance().getTranslation("OutGame", "soundOffText"));
        musicLabel.setText(audioManager.getMusicVolume() > AudioManager.MinVolume
                ? Localization.getInstance().getTranslation("OutGame", "musicOnText")
                : Localization.getInstance().getTranslation("OutGame", "musicOffText"));
        exitButton.getTag().setText(Localization.getInstance().getTranslation("OutGame", "exitOptionsLabel"));
    }

    public void refreshControlsFromConfig() {
        AudioManager audioManager = AudioManager.getInstance();
        setSoundVolume(audioManager.getSoundVolume(), false);
        setMusicVolume(audioManager.getMusicVolume());
        languageSelector.setSelected(Localization.getInstance().getExtendedLocale());
    }
}
