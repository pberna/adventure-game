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

package com.pberna.adventure.screens.outgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.dependencies.Settings;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.controls.AchievementListControl;
import com.pberna.engine.achievements.achievement.Achievement;
import com.pberna.engine.achievements.achievement.AchievementManager;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.screens.controls.ScrollPaneIndicator;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;

import java.util.ArrayList;

public class AchievementsScreen extends BaseStageScreen implements ILocalizable {

    private static final float LowerPanelPercentageHeight = 0.10f;
    private static final float MessagePanelPercentageHeight = 0.06f;
    private static final float PercentageBorderAchievements = 0.03f;

    private ScrollPane mainScrollPane;
    private ScrollPaneIndicator scrollPaneIndicator;
    private AchievementListControl achievementListControl;
    private ExtendedImageButton<Label> exitButton;
    private ExtendedImageButton<Label> gplayButton;
    private Rectangle mainPanel;
    private Rectangle messagePanel;
    private Rectangle lowerPanel;
    private Label infoMessageLabel;

    private ArrayList<Achievement> achievements;
    private ArrayList<AchievementsScreenEventListener> listeners;

    public AchievementsScreen()
    {
        setBackgroundTextureName("old_page");
        mainPanel = new Rectangle();
        messagePanel = new Rectangle();
        lowerPanel = new Rectangle();
        achievements = new ArrayList<Achievement>();
        listeners = new ArrayList<AchievementsScreenEventListener>();

        buildStage();
    }

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void addListener(AchievementsScreenEventListener listener) {
        listeners.add(listener);
    }

    private void buildStage() {
        //spells list and scroll pane
        achievementListControl = new AchievementListControl();
        achievementListControl.setPaddingWidth(Gdx.graphics.getWidth() * PercentageBorderAchievements);
        mainScrollPane = new ScrollPane(achievementListControl);
        mainScrollPane.setVelocityY(1);
        mainScrollPane.setScrollingDisabled(true, false);
        stage.addActor(mainScrollPane);
        scrollPaneIndicator = new ScrollPaneIndicator(mainScrollPane, false);

        //button
        exitButton = createExitButton();
        if(Settings.getInstance().isGplayAchievementsAvailable()) {
            gplayButton = createGPlayButton();
        }

        //infoMessageLabel
        infoMessageLabel = new Label("", StageScreenHelper.getLabelStyle(Color.BLACK));
        infoMessageLabel.setAlignment(Align.center);
        infoMessageLabel.setWrap(true);
        stage.addActor(infoMessageLabel);
    }

    private ExtendedImageButton<Label> createExitButton() {
        ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("Common", "exitLabel"), stage, AssetRepository.getInstance().getSound("click_button"));
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for(AchievementsScreenEventListener listener: listeners) {
                    listener.exit(this);
                }
            }
        });
        return button;
    }

    private ExtendedImageButton<Label> createGPlayButton() {
        ExtendedImageButton<Label> button = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("Common", "gplayName"), stage, AssetRepository.getInstance().getSound("click_button"));
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for(AchievementsScreenEventListener listener: listeners) {
                    listener.showGplayAchievementsScreen();
                }
            }
        });
        return button;
    }

    @Override
    protected void recalculateActorsPositions(float width, float height) {
        super.recalculateActorsPositions(width, height);
        updatePanelsSizes(width, height);
        float padding = width * PercentageBorderAchievements;

        //achievements list & main scroll pane
        achievementListControl.setControlWidth(mainPanel.getWidth());
        achievementListControl.setPaddingWidth(padding);
        mainScrollPane.setWidth(mainPanel.getWidth());
        mainScrollPane.setHeight(mainPanel.getHeight());
        PositionerHelper.setPositionFromCorner(mainScrollPane, Corner.TopLeft, 0, 0, mainPanel);
        scrollPaneIndicator.updateIndicatorsPositions(padding, padding / 2.0f);

        //buttons
        PositionerHelper.setPositionCenteredVerticalFillingRow( gplayButton == null ?
                new Actor [] { exitButton } : new Actor [] { gplayButton, exitButton } ,
                lowerPanel , HorizontalSide.Left);
        PositionerHelper.setPositionCenterInActor(exitButton, exitButton.getTag());
        if(gplayButton != null) {
            PositionerHelper.setPositionCenterInActor(gplayButton, gplayButton.getTag());
        }

        //infoMessageLabel
        infoMessageLabel.setWidth(messagePanel.width - (padding * 2.0f));
        PositionerHelper.setPositionCentered(infoMessageLabel, messagePanel);
    }

    private void updatePanelsSizes(float width, float height) {
        float lowerPanelHeight = (height * LowerPanelPercentageHeight);
        float messagePanelHeight = (height * MessagePanelPercentageHeight);

        lowerPanel.set(0, 0, width, lowerPanelHeight);
        messagePanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, messagePanelHeight);
        mainPanel.set(0, messagePanel.getY() + messagePanel.getHeight(), width,
                height - lowerPanel.getHeight()- messagePanel.getHeight());
    }


    private void setInfoMessageText(String infoMessageText) {
        infoMessageLabel.setText(infoMessageText);
        infoMessageLabel.setVisible(!infoMessageText.equals(""));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if(scrollPaneIndicator != null) {
            scrollPaneIndicator.updateIndicatorsVisibility();
        }
    }

    @Override
    public void hide() {
        super.hide();

        ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(scrollPaneIndicator);
    }

    @Override
    public void show() {
        super.show();

        if(scrollPaneIndicator != null) {
            scrollPaneIndicator.refreshContinousRendering();
        }
        mainScrollPane.setScrollY(0);
    }

    @Override
    public void refreshLocalizableItems() {
        //exit button
        exitButton.remove();
        exitButton.getTag().remove();
        exitButton = createExitButton();
        if(Settings.getInstance().isGplayAchievementsAvailable()) {
            gplayButton.remove();
            gplayButton.getTag().remove();
            gplayButton = createGPlayButton();
        }
        achievementListControl.refreshLocalizableItems();

        recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void showAchievements() {
        ArrayList<Achievement> listAchievements = AchievementManager.getInstance().getAllAchievements();
        achievementListControl.showAchievements(listAchievements);
        int totalUnlocked = getTotalUnlockedAchievements(listAchievements);

        if(totalUnlocked >= listAchievements.size()) {
            setInfoMessageText(String.format(Localization.getInstance().formatTranslation("Achievements",
                    "allAchievementsUnlocked"), listAchievements.size()));
        } else {
            setInfoMessageText(String.format(Localization.getInstance().formatTranslation("Achievements",
                    "summaryAchievements"), totalUnlocked, listAchievements.size()));
        }
    }

    private int getTotalUnlockedAchievements(ArrayList<Achievement> listAchievements) {
        int total = 0;

        for(Achievement achievement: listAchievements) {
            if(achievement.isUnlocked()) {
                total++;
            }
        }

        return total;
    }
}
