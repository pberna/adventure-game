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

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pberna.adventure.dependencies.DependenciesContainer;
import com.pberna.adventure.dependencies.Settings;
import com.pberna.adventure.screens.windows.OptionsMenuEventsListener;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.screens.shapes.ShapesHelper;
import com.pberna.adventure.screens.windows.OptionsMenuWindow;
import com.pberna.engine.screens.windows.ConfirmWindow;
import com.pberna.engine.screens.windows.ConfirmWindowListener;
import com.pberna.engine.screens.windows.NotificationWindow;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;

import java.util.ArrayList;
import java.util.Locale;

public class MainMenuScreen extends BaseStageScreen implements ILocalizable{
    private static final float LowerPanelPercentageHeight = 0.10f;
    private static final float MainOptionsPanelPercentageHeight = 0.55f;
    private static final float LogoPanelPercentageHeight = 0.35f;

    public static final float ShowHideWindowAnimationDuration = 0.6f;

    private ExtendedImageButton<Label> newGameButton;
    private ExtendedImageButton<Label> loadGameButton;
    private ExtendedImageButton<Label> storeButton;
    private ExtendedImageButton<Label> exitButton;
    private ImageButton optionsButton;
    private ImageButton creditsButton;
    private ImageButton achievementsButton;
    private ImageButton leaderboardButton;
    private Image logoImage;
    private Label versionLabel;

    private OptionsMenuWindow optionsMenuWindow;
    private ConfirmWindow confirmWindow;
    private NotificationWindow notificationWindow;

    private Rectangle lowerPanel;
    private Rectangle mainOptionsPanel;
    private Rectangle logoPanel;

    private ArrayList<MainMenuScreenEvents> listeners;
    private ArrayList<ILocalizable> localizables;

    public MainMenuScreen() {
        setBackgroundTextureName("main_screen_bg");

        lowerPanel = new Rectangle();
        mainOptionsPanel = new Rectangle();
        logoPanel = new Rectangle();
        listeners = new ArrayList<MainMenuScreenEvents>();
        localizables = new ArrayList<ILocalizable>();

        buildStage();
    }

    private void buildStage() {
        //newGameButton
        newGameButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("OutGame", "newGameButtonText"), stage, AssetRepository.getInstance().getSound("click_button"));
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for(MainMenuScreenEvents listener:listeners) {
                    listener.newGame();
                }
            }
        });

        //loadGameButton
        loadGameButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("OutGame", "loadGameButtonText"), stage, AssetRepository.getInstance().getSound("click_button"));
        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for(MainMenuScreenEvents listener:listeners) {
                    listener.loadGame();
                }
            }
        });

        //storeButton
        if(Settings.getInstance().isStoreAvailable()) {
            storeButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                    getTranslation("OutGame", "storeButtonText"), stage, AssetRepository.getInstance().getSound("click_button"));
            storeButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    for (MainMenuScreenEvents listener : listeners) {
                        listener.goToStore();
                    }
                }
            });
        }

        //exitButton
        exitButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("OutGame", "exitButtonText"), stage, AssetRepository.getInstance().getSound("click_button"));
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitButtonPressed();
            }
        });

        //optionsButton
        optionsButton = StageScreenHelper.createImageButton(stage, "widgets",
                "options_button", "options_button_pressed", AssetRepository.getInstance().getSound("click_button"));
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsMenuWindow.refreshControlsFromConfig();
                optionsMenuWindow.showPopUp(ShowHideWindowAnimationDuration, true);
            }
        });

        //creditsButton
        creditsButton = StageScreenHelper.createImageButton(stage, "widgets",
                "credits_button", "credits_button_pressed", AssetRepository.getInstance().getSound("click_button"));
        creditsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (MainMenuScreenEvents listener : listeners) {
                    listener.watchCredits();
                }
            }
        });

        //achievementsButton
        achievementsButton = StageScreenHelper.createImageButton(stage, "widgets",
                "achivement_button", "achivement_button_pressed", AssetRepository.getInstance().getSound("click_button"));
        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (MainMenuScreenEvents listener : listeners) {
                    listener.watchAchievements();
                }
            }
        });

        //leaderboardButton
        if(Settings.getInstance().isLeadeboardAvailable()) {
            leaderboardButton = StageScreenHelper.createImageButton(stage, "widgets",
                    "leaderboard_button", "leaderboard_button_pressed", AssetRepository.getInstance().getSound("click_button"));
            leaderboardButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    for (MainMenuScreenEvents listener : listeners) {
                        listener.watchLeaderboard();
                    }
                }
            });
        }

        //optionsMenuWindow
        optionsMenuWindow = new OptionsMenuWindow();
        optionsMenuWindow.setWindowBackGround(getModalWindowBackground());
        optionsMenuWindow.setBorder(ShapesHelper.getBorder(optionsMenuWindow));
        optionsMenuWindow.setParentScreen(this);
        optionsMenuWindow.addListener(new OptionsMenuEventsListener() {
            @Override
            public void exit() {
                optionsMenuWindow.hidePopUp(ShowHideWindowAnimationDuration);
            }

            @Override
            public void selectLanguage(Locale locale) {
                for (MainMenuScreenEvents listener : listeners) {
                    listener.selectLanguage(locale);
                }
            }
        });
        localizables.add(optionsMenuWindow);

        optionsMenuWindow.setVisible(false);
        stage.addActor(optionsMenuWindow);
        optionsMenuWindow.setWidth(0);
        optionsMenuWindow.setHeight(0);

        //confirmWindow
        confirmWindow = new ConfirmWindow();
        confirmWindow.setWindowBackGround(getModalWindowBackground());
        confirmWindow.setBorder(ShapesHelper.getBorder(confirmWindow));
        confirmWindow.setParentScreen(this);
        localizables.add(confirmWindow);

        confirmWindow.setVisible(false);
        stage.addActor(confirmWindow);
        confirmWindow.setWidth(0);
        confirmWindow.setHeight(0);

        //notificationWindow
        notificationWindow = new NotificationWindow();
        notificationWindow.setParentScreen(this);
        notificationWindow.setVisible(false);
        stage.addActor(notificationWindow);

        //logoImage
        logoImage = new Image(AssetRepository.getInstance().getTexture("main_header"));
        stage.addActor(logoImage);

        //versionLabel
        versionLabel = new Label("v" + Settings.getInstance().getVersionName(),
                StageScreenHelper.getLabelButtonStyle());
        stage.addActor(versionLabel);
    }

    private void exitButtonPressed() {
        confirmWindow.showConfirmWindowPopUp(Localization.getInstance().getTranslation("OutGame", "confirmExitTitle"),
                Localization.getInstance().getTranslation("OutGame", "exitGameMessage"), new ConfirmWindowListener() {
                    @Override
                    public void acceptPressed() {
                        for(MainMenuScreenEvents listener:listeners) {
                            listener.exitGame();
                        }
                    }

                    @Override
                    public void cancelPressed() {

                    }
                }, ShowHideWindowAnimationDuration);
    }

    @Override
    protected void recalculateActorsPositions(float width, float height) {
        super.recalculateActorsPositions(width, height);
        updatePanelsSizes(width, height);

        //main buttons
        PositionerHelper.setPositionCenteredHorizontalFillingColumn(getMainButtons(), mainOptionsPanel, VerticalSide.Top);
        PositionerHelper.setPositionCenterInActor(newGameButton, newGameButton.getTag());
        PositionerHelper.setPositionCenterInActor(loadGameButton, loadGameButton.getTag());
        if(storeButton != null) {
            PositionerHelper.setPositionCenterInActor(storeButton, storeButton.getTag());
        }
        PositionerHelper.setPositionCenterInActor(exitButton, exitButton.getTag());

        //options/credits buttons
        if(leaderboardButton != null) {
            PositionerHelper.setPositionCenteredVerticalFillingRow(new Actor[]{creditsButton,
                    optionsButton, achievementsButton, leaderboardButton}, lowerPanel, HorizontalSide.Left);
        } else {
            PositionerHelper.setPositionCenteredVerticalFillingRow(new Actor[]{creditsButton,
                    optionsButton, achievementsButton}, lowerPanel, HorizontalSide.Left);
        }

        //modalwindows
        PositionerHelper.setPositionCentered(optionsMenuWindow, stage);
        setBorderOnActor(optionsMenuWindow, optionsMenuWindow.getBorder());
        PositionerHelper.setPositionCentered(confirmWindow, stage);
        setBorderOnActor(confirmWindow, confirmWindow.getBorder());

        //logoImage
        PositionerHelper.setPositionCentered(logoImage, logoPanel);

        //version label
        PositionerHelper.setPositionFromCorner(versionLabel, Corner.TopRight, 0,0, stage);
    }

    private Actor [] getMainButtons() {
        if(storeButton == null) {
            return new Actor[]{ newGameButton, loadGameButton, exitButton};
        }
        return new Actor[]{ newGameButton, loadGameButton, storeButton, exitButton};
    }

    private void updatePanelsSizes(float width, float height) {
        float lowerPanelHeight = height * LowerPanelPercentageHeight;
        float mainOptionsPanelHeight = height * MainOptionsPanelPercentageHeight;
        float logoPanelHeight = height * LogoPanelPercentageHeight;

        lowerPanel.set(0, 0, width, lowerPanelHeight);
        mainOptionsPanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, mainOptionsPanelHeight);
        logoPanel.set(0, mainOptionsPanel.getY() + mainOptionsPanel.getHeight(), width, logoPanelHeight);
    }

    @Override
    public void refreshLocalizableItems() {
        newGameButton.getTag().setText(Localization.getInstance().getTranslation("OutGame", "newGameButtonText"));
        loadGameButton.getTag().setText(Localization.getInstance().getTranslation("OutGame", "loadGameButtonText"));
        if(storeButton != null) {
            storeButton.getTag().setText(Localization.getInstance().getTranslation("OutGame", "storeButtonText"));
        }
        exitButton.getTag().setText(Localization.getInstance().getTranslation("OutGame", "exitButtonText"));

        for(ILocalizable localizable: localizables) {
            localizable.refreshLocalizableItems();
        }
    }

    @Override
    public void show() {
        super.show();

        sendToFrontIfVisible(optionsMenuWindow);
        sendToFrontIfVisible(confirmWindow);
        sendToFrontIfVisible(notificationWindow);
    }

    public void addListener(MainMenuScreenEvents listener) {
        listeners.add(listener);
    }

    public void onBackPressed() {
        if(optionsMenuWindow.isVisible()) {
            optionsMenuWindow.hidePopUp(ShowHideWindowAnimationDuration);
        } else if(confirmWindow.isVisible()) {
            confirmWindow.hidePopUp(ShowHideWindowAnimationDuration);
        } else {
            exitButtonPressed();
        }
    }

    public void showScoreNotification(int score) {
        notificationWindow.setMessageLabel(String.format(Localization.getInstance().getTranslation(
                "OutGame", "scoreNotificationMessage", true), score));
        notificationWindow.setNotificationImage(NotificationWindow.NotificationImage.Score);
        notificationWindow.showFromAboveScreenAndReturn(ShowHideWindowAnimationDuration, ShowHideWindowAnimationDuration * 2f, null);
    }
}
