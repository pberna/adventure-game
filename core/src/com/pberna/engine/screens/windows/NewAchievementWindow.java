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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.controllers.ITransitionCallback;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.achievements.achievement.Achievement;
import com.pberna.engine.achievements.achievement.AchievementManager;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImage;
import com.pberna.engine.localization.Localization;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class NewAchievementWindow extends AnimableWindow {

    private static final int AchievementsListSize = 20;
    private static final float StayAchievementAnimationDuration = 4.5f;
    private static final float ShowHideAchievementAnimationDuration = 0.6f;

    private Label titleLabel;
    private Label subtitleLabel;
    private ArrayList<ExtendedImage<Achievement>> achievementImages;
    private Queue<Achievement> queue;
    private boolean startedAnimation = false;

    public NewAchievementWindow() {
        super();

        Texture backgroundTexture = AssetRepository.getInstance().getTexture("notification_background");
        setBackground(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
        setSize(backgroundTexture.getWidth(), backgroundTexture.getHeight());

        buildWindow();
        pack();

        setModal(true);
        setMovable(false);
        setResizable(false);

        originalWidth = getWidth();
        originalHeight = getHeight();
        queue = new LinkedList<Achievement>();
    }

    private void buildWindow() {
        defaults().center();

        //achievement images
        achievementImages = new ArrayList<ExtendedImage<Achievement>>(AchievementsListSize);
        for(Achievement achievement: AchievementManager.getInstance().getAllAchievements()) {
            ExtendedImage<Achievement> image = new ExtendedImage<Achievement>(AssetRepository.
                    getInstance().getTextureRegion("achievements", achievement.getImageName()));
            image.setTag(achievement);
            image.setVisible(false);
            achievementImages.add(image);
        }

        Stack stack = StageScreenHelper.createStackWithActors(achievementImages);
        stack.setFillParent(false);
        float padding = getWidth() * 0.03F;
        add(stack).width(stack.getWidth()).right().padLeft(padding * 2f).padRight(padding);

        //title label
        titleLabel = new Label("                   ", getTitleLabelStyle());
        titleLabel.setAlignment(Align.center);
        titleLabel.setWrap(true);
        titleLabel.setWidth(getWidth() - stack.getWidth() - (padding * 5f));

        //subtitlelabel
        subtitleLabel = new Label("                   ", getSubtitleLabelStyle());
        subtitleLabel.setAlignment(Align.center);
        subtitleLabel.setWrap(true);
        subtitleLabel.setWidth(titleLabel.getWidth());

        Table table = new Table();
        table.add(titleLabel).width(titleLabel.getWidth()).row();
        table.add(subtitleLabel).width(subtitleLabel.getWidth());
        table.setWidth(titleLabel.getWidth());
        add(table).width(getWidth() - stack.getWidth() - (padding * 5f)).center().padRight(padding * 2f);
    }

    private Label.LabelStyle getTitleLabelStyle() {
        return StageScreenHelper.getLabelStyle(Color.WHITE);
    }

    private Label.LabelStyle getSubtitleLabelStyle() {
        return StageScreenHelper.getSmallLabelStyle(Color.WHITE);
    }

    private void setAchievement(Achievement achievement) {
        String title = Localization.getInstance().getTranslation("Achievements", "newAchievement") +
                "\n" + achievement.getName();
        titleLabel.setText(title);
        subtitleLabel.setText(achievement.getDescription());

        for(ExtendedImage<Achievement> image: achievementImages) {
            image.setVisible(image.getTag().getId() == achievement.getId());
        }
    }

    public void enqueueAchivement(Achievement achievement) {
        boolean startAnimation = false;
        synchronized (queue) {
            if(queue.size() == 0) {
                startAnimation = true;
            }
            queue.add(achievement);
        }

        if(startAnimation && !startedAnimation) {
            startedAnimation = true;
            checkStartNewAnimation();
        }
    }

    private void checkStartNewAnimation() {
        Achievement achievement;
        synchronized (queue) {
            achievement = queue.poll();
        }
        if(achievement != null) {
            setAchievement(achievement);
            showFromAboveScreenAndReturn(ShowHideAchievementAnimationDuration, StayAchievementAnimationDuration,
                    new ITransitionCallback() {
                        @Override
                        public void callback() {
                            checkStartNewAnimation();
                        }
                    });
        } else {
            startedAnimation = false;
        }
    }
}
