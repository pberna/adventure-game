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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.achievements.achievement.Achievement;
import com.pberna.engine.achievements.achievement.AchievementManager;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.utils2D.graphics.ImageManipulationHelper;

import java.text.DateFormat;
import java.util.ArrayList;

public class AchievementListControl extends Table {

    private ArrayList<Achievement> achievements;
    private ArrayList<Label> achievementNameLabels;
    private ArrayList<Label> achievementDescriptionLabels;
    private ArrayList<Label> achievementInfoLabels;
    private ArrayList<Image> achievementImages;
    private ArrayList<Table> labelsGroups;

    private float maxImageWidth;
    private float paddingWidth;

    public AchievementListControl() {
        achievements = new ArrayList<Achievement>();
        achievementNameLabels = new ArrayList<Label>();
        achievementDescriptionLabels = new ArrayList<Label>();
        achievementInfoLabels = new ArrayList<Label>();
        achievementImages = new ArrayList<Image>();
        labelsGroups = new ArrayList<Table>();
        maxImageWidth = 0f;
        paddingWidth = 5;
    }

    @SuppressWarnings("rawtypes")
    public void setControlWidth(float width) {
        float availableWidth = width - maxImageWidth - (paddingWidth * 3f);

        for(Label label: achievementDescriptionLabels) {
            label.setWidth(availableWidth);
        }
        for(Label label: achievementNameLabels) {
            label.setWidth(availableWidth);
        }
        for(Label label: achievementInfoLabels) {
            label.setWidth(availableWidth);
        }
        for(Table group: labelsGroups) {
            group.setWidth(availableWidth);
            for(Cell cellInGroup :group.getCells())
            {
                cellInGroup.width(availableWidth);
            }

            Cell<Table> cell = getCell(group);
            if(cell != null) {
                cell.width(availableWidth);
            }
        }
    }

    public float getPaddingWidth() {
        return paddingWidth;
    }

    public void setPaddingWidth(float paddingWidth) {
        this.paddingWidth = paddingWidth;
    }

    public void showAchievements(ArrayList<Achievement> achievements) {
        this.achievements = achievements;
        buildControls();
    }

    private void buildControls() {
        clearChildren();
        achievementNameLabels.clear();
        achievementDescriptionLabels.clear();
        achievementInfoLabels.clear();
        achievementImages.clear();
        labelsGroups.clear();
        maxImageWidth = 0f;

        for(int i = 0; i < achievements.size(); i++) {
            addAchievementRow(achievements.get(i), i == 0, i == achievements.size() - 1);
        }
        pack();
    }

    private void addAchievementRow(Achievement achievement, boolean firstRow, boolean lastRow) {
        addAchievementImageCell(achievement, firstRow, lastRow);
        addLabelsCell(achievement, firstRow, lastRow);
        row();
    }

    @SuppressWarnings("unchecked")
    private void addAchievementImageCell(Achievement achievement, boolean firstRow, boolean lastRow) {
        TextureRegion achievementTexture = getAchivementTexture(achievement);
        Image achievementImage = new Image(achievementTexture);
        if(!achievement.isUnlocked()) {
            ImageManipulationHelper.setActorTransparency(achievementImage, 0.7f);
        }

        add(achievementImage).center().padLeft(paddingWidth).padTop(firstRow ? paddingWidth :  paddingWidth / 2f).
                padBottom(lastRow ? paddingWidth : paddingWidth / 2f);
        achievementImages.add(achievementImage);

        maxImageWidth = Math.max(maxImageWidth, achievementImage.getWidth());
    }

    private TextureRegion getAchivementTexture(Achievement achievement) {
        return AssetRepository.getInstance().getTextureRegion(
                achievement.isUnlocked() ? "achievements" : "achievements_dark",
                achievement.getHidden() && !achievement.isUnlocked() ? "unknown" : achievement.getImageName());
    }

    private void addLabelsCell(Achievement achievement, boolean firstRow, boolean lastRow) {
        Label achivementNameLabel = new Label(achievement.getName(), getAchievementNameStyle());
        achivementNameLabel.setAlignment(Align.left);
        achivementNameLabel.setWrap(true);
        achievementNameLabels.add(achivementNameLabel);

        Label achivementDescriptionLabel = new Label(getAchievementDescriptionText(achievement),
                getAchievementDescriptionStyle());
        achivementDescriptionLabel.setAlignment(Align.left);
        achivementDescriptionLabel.setWrap(true);
        achievementDescriptionLabels.add(achivementDescriptionLabel);

        Label achievementInfoLabel = new Label(getAchivementInfoText(achievement),
                getAchievementInfoStyle());
        achievementInfoLabel.setAlignment(Align.right);
        achievementInfoLabel.setWrap(true);
        achievementInfoLabels.add(achievementInfoLabel);

        Table group = new Table();
        group.align(Align.left);
        group.add(achivementNameLabel).row();
        group.add(achivementDescriptionLabel).row();
        group.add(achievementInfoLabel).right();
        group.pack();
        add(group).left().expandX().padLeft(paddingWidth).padTop(firstRow ? paddingWidth : paddingWidth / 2f).
                padBottom(lastRow ? paddingWidth : paddingWidth / 2f).padRight(paddingWidth);
        labelsGroups.add(group);
    }

    private String getAchievementDescriptionText(Achievement achievement) {
        if(achievement.isUnlocked()) {
            return achievement.getDescription();
        }
        if(achievement.getHidden()) {
            return Localization.getInstance().getTranslation("Achievements", "hiddenAchievement");
        }

        return achievement.getDescription();
    }

    private String getAchivementInfoText(Achievement achievement) {
        String separatorFromRight = "            ";
        if(achievement.isUnlocked()) {
            DateFormat dayFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Localization.getInstance().getLocale());
            return dayFormat.format(achievement.getUnlockDate()) + separatorFromRight;
        }

        if(achievement.isIncremental() && !achievement.getHidden()) {
            return String.format(Localization.getInstance().getTranslation("Achievements",
                    "achievementProgress"), AchievementManager.getInstance().
                    getAchievementProgress(achievement.getId())) + separatorFromRight;
        }
        return "";
    }

    private Label.LabelStyle getAchievementNameStyle() {
        return StageScreenHelper.getLabelStyle(Color.BLACK);
    }

    private Label.LabelStyle getAchievementDescriptionStyle() {
        return StageScreenHelper.getSmallLabelStyle(Color.BLACK);
    }

    private Label.LabelStyle getAchievementInfoStyle() {
        return StageScreenHelper.getSmallLabelStyle(Color.BLACK);
    }

    public void refreshLocalizableItems() {
        for(int i = 0; i < achievements.size(); i++) {
            achievementNameLabels.get(i).setText(achievements.get(i).getName());
            achievementDescriptionLabels.get(i).setText(getAchievementDescriptionText(achievements.get(i)));
            achievementInfoLabels.get(i).setText(getAchivementInfoText(achievements.get(i)));
        }
    }
}
