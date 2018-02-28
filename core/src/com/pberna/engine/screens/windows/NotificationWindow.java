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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;

public class NotificationWindow extends AnimableWindow {

    private Label messageLabel;
    private Image okImage;
    private Image errorImage;
    private Image scoreImage;

    public NotificationWindow() {
        super();

        Texture backgroundTexture = AssetRepository.getInstance().getTexture("notification_background");
        setBackground(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
        setSize(backgroundTexture.getWidth(), backgroundTexture.getHeight());

        buildNotificationWindow();
        pack();

        setModal(true);
        setMovable(false);
        setResizable(false);

        originalWidth = getWidth();
        originalHeight = getHeight();
    }

    private void buildNotificationWindow() {
        defaults().center();

        //notification image
        okImage = new Image(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "success")));
        okImage.setVisible(false);
        errorImage = new Image(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "fail")));
        errorImage.setVisible(false);
        scoreImage = new Image(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "score")));
        scoreImage.setVisible(false);

        //stack
        Stack stack = StageScreenHelper.createStackWithActors(scoreImage, errorImage, okImage);
        stack.setFillParent(false);
        float padding = getWidth() * 0.03F;
        add(stack).width(stack.getWidth()).right().padLeft(padding * 2f).padRight(padding);

        //message label
        messageLabel = new Label("                   ", getLabelStyle());
        messageLabel.setAlignment(Align.center);
        messageLabel.setWrap(true);
        messageLabel.setWidth(getWidth() - stack.getWidth() - (padding * 5f));
        add(messageLabel).width(getWidth() - stack.getWidth() - (padding * 5f)).center().padRight(padding * 2f);
    }

    private Label.LabelStyle getLabelStyle() {
        return StageScreenHelper.getLabelStyle(Color.WHITE);
    }

    public void setMessageLabel(String message) {
        messageLabel.setText(message);
    }

    public enum NotificationImage {
        Ok,
        Error,
        Score
    }

    public void setNotificationImage(NotificationImage notificationImage) {
        switch (notificationImage){
            case Error:
                okImage.setVisible(false);
                errorImage.setVisible(true);
                scoreImage.setVisible(false);
                break;
            case Score:
                okImage.setVisible(false);
                errorImage.setVisible(false);
                scoreImage.setVisible(true);
                break;
            case Ok:
            default:
                okImage.setVisible(true);
                errorImage.setVisible(false);
                scoreImage.setVisible(false);
                break;
        }
    }
}
