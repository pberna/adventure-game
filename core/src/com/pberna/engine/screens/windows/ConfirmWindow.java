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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;

public class ConfirmWindow extends AnimableWindow implements ILocalizable {
    private int paddingSize;
    private int halfPaddingSize;
    private ConfirmWindowListener listener;
    private float animationDuration;

    private Label titleLabel;
    private Label messageLabel;
    private ExtendedImageButton<Label> acceptButton;
    private ExtendedImageButton<Label> cancelButton;

    public ConfirmWindow() {
        super();
        paddingSize = (int)(Gdx.graphics.getHeight() * 0.03f);
        halfPaddingSize = (int)(paddingSize / 2.0f);
        listener = null;
        animationDuration = 0;

        buildConfirmWindow();
        pack();
        setWidth(Gdx.graphics.getWidth() * 0.9f);

        setModal(true);
        setMovable(false);
        setResizable(false);
        setBackground(new TextureRegionDrawable(new TextureRegion(AssetRepository.getInstance().getTexture("old_page_dark"))));

        originalWidth = getWidth();
        originalHeight = getHeight();
    }

    private void buildConfirmWindow() {
        defaults().padLeft(halfPaddingSize).padRight(halfPaddingSize);

        //title label
        titleLabel = new Label(Localization.getInstance().getTranslation("Common", "confirmWindowTitle"), getLabelStyle());
        titleLabel.setAlignment(Align.center);
        add(titleLabel).padTop(halfPaddingSize).padBottom(paddingSize).colspan(2);

        //message label
        row();
        messageLabel = new Label("                   ", getLabelStyle());
        messageLabel.setAlignment(Align.center);
        messageLabel.setWrap(true);
        add(messageLabel).padBottom(paddingSize).colspan(2);

        //accept button
        row();
        acceptButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("Common", "acceptButtonLabel"), this, AssetRepository.getInstance().getSound("click_button"));
        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hidePopUp(animationDuration);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.acceptPressed();
                        }
                    }
                }, animationDuration);
            }
        });
        this.getCells().peek().padBottom(paddingSize);

        //cancel button
        cancelButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("Common", "cancelButtonLabel"), this, AssetRepository.getInstance().getSound("click_button"));
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hidePopUp(animationDuration);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if(listener != null) {
                            listener.cancelPressed();
                        }
                    }
                }, animationDuration);
            }
        });
        this.getCells().peek().padBottom(paddingSize).padLeft(0);
    }

    private Label.LabelStyle getLabelStyle() {
        return StageScreenHelper.getLabelStyle(Color.BLACK);
    }

    @Override
    public void refreshLocalizableItems() {
        acceptButton.getTag().setText(Localization.getInstance().getTranslation("Common", "acceptButtonLabel"));
        cancelButton.getTag().setText(Localization.getInstance().getTranslation("Common", "cancelButtonLabel"));
    }

    public void setListener(ConfirmWindowListener listener) {
        this.listener = listener;
    }

    public void setTitleLabel(String title) {
        titleLabel.setText(title);
    }

    public void setMessageLabel(String message) {
        messageLabel.setText(message);
    }

    public void showConfirmWindowPopUp(String title, String message, ConfirmWindowListener handler, float animationDuration) {
        setTitleLabel(title);
        setMessageLabel(message);
        setListener(handler);
        this.animationDuration = animationDuration;
        showPopUp(animationDuration, true);
    }
}
