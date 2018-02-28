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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.screens.windows.AnimableWindow;

public class MainMenuWindow extends AnimableWindow implements ILocalizable {
	
	private int paddingSize;
	private int halfPaddingSize;
	private ArrayList<MainMenuWindowListener> listeners;
	
	private Label titleLabel;
	private ExtendedImageButton<Label> continueButton;
	private ExtendedImageButton<Label> loadGameButton;
	private ExtendedImageButton<Label> quitButton;

	public MainMenuWindow() {
		super();
		paddingSize = (int)(Gdx.graphics.getHeight() * 0.03f);
		halfPaddingSize = (int)(paddingSize / 2.0f);
		listeners = new ArrayList<MainMenuWindowListener>();
		
		buildMenuOptions();
		pack();
				
		setModal(true);
		setMovable(false);
		setResizable(false);
		setBackground(new TextureRegionDrawable(new TextureRegion(AssetRepository.getInstance().getTexture("old_page_dark"))));
		
		originalWidth = getWidth();
		originalHeight = getHeight();
	}	
	
	public void addListener(MainMenuWindowListener listener) {
		listeners.add(listener);
	}	
	
	private void buildMenuOptions() {
		defaults().padLeft(paddingSize).padRight(paddingSize);
		
		//title label
		titleLabel = new Label(Localization.getInstance().getTranslation("Common", "menuWindowTitle"), getTitleLabelStyle());
		titleLabel.setAlignment(Align.center);
		add(titleLabel).padTop(halfPaddingSize).padBottom(halfPaddingSize).colspan(2);

		//save game button
		row();
		ImageButton saveGameButton = new ImageButton(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "save_button")),
				new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "save_button_pressed")));
		StageScreenHelper.addOnClickSound(saveGameButton, AssetRepository.getInstance().getSound("click_button"));
		saveGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (MainMenuWindowListener listener : listeners) {
					listener.savePressed();
				}
			}
		});
		add(saveGameButton).padBottom(paddingSize).padLeft(paddingSize).padRight(paddingSize / 2f);

		//options Game Button
		ImageButton optionsGameButton = new ImageButton(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "options_button")),
				new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "options_button_pressed")));
		StageScreenHelper.addOnClickSound(optionsGameButton,  AssetRepository.getInstance().getSound("click_button"));
		optionsGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (MainMenuWindowListener listener : listeners) {
					listener.optionsPressed();
				}
			}
		});
		add(optionsGameButton).padBottom(paddingSize).padLeft(paddingSize / 2f).padRight(paddingSize);

		//continue button
		row();
		continueButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
				getTranslation("Common", "continueButtonLabel"), this, AssetRepository.getInstance().getSound("click_button"));
		continueButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (MainMenuWindowListener listener : listeners) {
					listener.continuePressed();
				}
			}
		});		
		this.getCells().peek().padBottom(paddingSize).colspan(2);

		//load GameButton
		row();
		loadGameButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
				getTranslation("Common", "loadButtonLabel"), this, AssetRepository.getInstance().getSound("click_button"));
		loadGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (MainMenuWindowListener listener : listeners) {
					listener.loadPressed();
				}
			}
		});
		this.getCells().peek().padBottom(paddingSize).colspan(2);

		//quit button
		row();
		quitButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
				getTranslation("Common", "quitButtonLabel"), this, AssetRepository.getInstance().getSound("click_button"));
		quitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for(MainMenuWindowListener listener: listeners) {
					listener.quitPressed();
				}
			}
		});
		this.getCells().peek().padBottom(paddingSize).colspan(2);
	}

	private LabelStyle getTitleLabelStyle() {
		return StageScreenHelper.getLabelStyle(Color.BLACK);
	}

	@Override
	public void refreshLocalizableItems() {
		titleLabel.setText(Localization.getInstance().getTranslation("Common", "menuWindowTitle"));
		continueButton.getTag().setText(Localization.getInstance().getTranslation("Common", "continueButtonLabel"));
		loadGameButton.getTag().setText(Localization.getInstance().getTranslation("Common", "loadButtonLabel"));
		quitButton.getTag().setText(Localization.getInstance().getTranslation("Common", "quitButtonLabel"));
	}
}
