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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pberna.adventure.games.StoredGame;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.controls.StoredGameControl;
import com.pberna.adventure.screens.controls.StoredGameControlListener;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.screens.controls.ScrollPaneIndicator;
import com.pberna.engine.screens.shapes.ShapesHelper;
import com.pberna.engine.screens.windows.ConfirmWindow;
import com.pberna.engine.screens.windows.ConfirmWindowListener;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;

import java.util.ArrayList;
import java.util.Collection;

public class LoadGameScreen extends BaseStageScreen implements ILocalizable {
    private static final float LowerPanelPercentageHeight = 0.10f;
    private static final float MainPanelPercentageHeight = 0.90f;
    private static final float PercentageBorderScreen = 0.03f;

    private Table mainTable;
    private ScrollPane mainScrollPane;
    private ScrollPaneIndicator paneIndicator;
    private ExtendedImageButton<Label> loadGameButton;
    private ExtendedImageButton<Label> exitButton;
    private ArrayList<StoredGameControl> listStoredGamesControls;
    private Label noSavedGamesMessageLabel;
    private ConfirmWindow confirmWindow;

    private Rectangle mainPanel;
    private Rectangle lowerPanel;
    private ArrayList<LoadGameScreenEventsListener> listeners;
    private StoredGame selectedStoredGame;
    private boolean confirmLoadGame;

    public void setConfirmLoadGame(boolean confirmLoadGame) {
        this.confirmLoadGame = confirmLoadGame;
    }

    public LoadGameScreen() {
        setBackgroundTextureName("old_page_dark");

        mainPanel = new Rectangle();
        lowerPanel = new Rectangle();
        listeners = new ArrayList<LoadGameScreenEventsListener>();
        listStoredGamesControls = new ArrayList<StoredGameControl>();
        confirmLoadGame = false;

        buildStage();

        setSelectedStoredGame(null);
    }

    protected Label.LabelStyle getLabelStyle()
    {
        return StageScreenHelper.getLabelStyle(Color.ORANGE);
    }

    private void buildStage() {
        //main vertical group
        mainTable = new Table();

        //main scroll pane
        mainScrollPane = new ScrollPane(mainTable);
        mainScrollPane.setVelocityY(1);
        mainScrollPane.setScrollingDisabled(true, false);
        stage.addActor(mainScrollPane);

        //paneIndicator
        paneIndicator = new ScrollPaneIndicator(mainScrollPane, false);

        //loadGameButton
        loadGameButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("OutGame", "loadGameButtonText"), stage, AssetRepository.getInstance().getSound("click_button"));
        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedStoredGame != null) {
                    if (confirmLoadGame) {
                        confirmWindow.showConfirmWindowPopUp(Localization.getInstance().getTranslation("OutGame", "loadGameButtonText"),
                                Localization.getInstance().getTranslation("OutGame", "loadGameMessage"), new ConfirmWindowListener() {
                                    @Override
                                    public void acceptPressed() {
                                        notifyLoadGame(selectedStoredGame);
                                    }

                                    @Override
                                    public void cancelPressed() {

                                    }
                                }, MainMenuScreen.ShowHideWindowAnimationDuration);
                    } else {
                        notifyLoadGame(selectedStoredGame);
                    }
                }
            }
        });

        //exitButton
        exitButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("OutGame", "exitButtonText"), stage, AssetRepository.getInstance().getSound("click_button"));
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (LoadGameScreenEventsListener listener : listeners) {
                    listener.exit();
                }
            }
        });

        //no saved message
        noSavedGamesMessageLabel = StageScreenHelper.createCenteredLabel(Localization.getInstance().
                getTranslation("OutGame", "noSavedGames"), getLabelStyle());
        noSavedGamesMessageLabel.setWrap(true);

        //confirm window
        confirmWindow = new ConfirmWindow();
        confirmWindow.setWindowBackGround(getModalWindowBackground());
        confirmWindow.setBorder(ShapesHelper.getBorder(confirmWindow));
        confirmWindow.setParentScreen(this);

        confirmWindow.setVisible(false);
        stage.addActor(confirmWindow);
        confirmWindow.setWidth(0);
        confirmWindow.setHeight(0);
    }

    @Override
    protected void recalculateActorsPositions(float width, float height) {
        super.recalculateActorsPositions(width, height);
        updatePanelsSizes(width, height);
        float padding = width * PercentageBorderScreen;
        float contentWidth = mainPanel.getWidth() - (padding * 2.0f);

        //load game buttons and container
        mainTable.setWidth(contentWidth);
        noSavedGamesMessageLabel.setWidth(contentWidth);
        mainScrollPane.setWidth(mainPanel.getWidth());
        mainScrollPane.setHeight(mainPanel.getHeight());
        PositionerHelper.setPositionFromCorner(mainScrollPane, Corner.TopLeft, 0, 0, mainPanel);
        paneIndicator.updateIndicatorsPositions(padding, padding / 2.0f);

        //lower buttons
        PositionerHelper.setPositionCenteredVerticalFillingRow(new Actor[]{loadGameButton, exitButton},
                lowerPanel, HorizontalSide.Left);
        PositionerHelper.setPositionCenterInActor(loadGameButton, loadGameButton.getTag());
        PositionerHelper.setPositionCenterInActor(exitButton, exitButton.getTag());

        //modal windows
        PositionerHelper.setPositionCentered(confirmWindow, stage);
        setBorderOnActor(confirmWindow, confirmWindow.getBorder());
    }

    private void updatePanelsSizes(float width, float height) {
        float lowerPanelHeight = height * LowerPanelPercentageHeight;
        float mainPanelHeight = height * MainPanelPercentageHeight;

        lowerPanel.set(0, 0, width, lowerPanelHeight);
        mainPanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, mainPanelHeight);
    }

    public void addListener(LoadGameScreenEventsListener listener) {
        listeners.add(listener);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if(paneIndicator != null) {
            paneIndicator.updateIndicatorsVisibility();
        }
    }

    @Override
    public void hide() {
        super.hide();

        ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(paneIndicator);
    }

    @Override
    public void show() {
        super.show();

        if(paneIndicator != null) {
            paneIndicator.refreshContinousRendering();
        }
        sendToFrontIfVisible(confirmWindow);
    }

    @Override
    public void refreshLocalizableItems() {
        loadGameButton.getTag().setText(Localization.getInstance().getTranslation("OutGame", "loadGameButtonText"));
        exitButton.getTag().setText(Localization.getInstance().getTranslation("OutGame", "exitButtonText"));
        noSavedGamesMessageLabel.setText(Localization.getInstance().getTranslation("OutGame", "noSavedGames"));
        confirmWindow.refreshLocalizableItems();
    }

    private void setSelectedStoredGame(StoredGame storedGame) {
        if(storedGame == null) {
            if(selectedStoredGame != null) {
                setStoredGameControlSelected(selectedStoredGame.getId(), false);
            }
        } else {
            if(selectedStoredGame != null && selectedStoredGame.getId() != storedGame.getId()) {
                setStoredGameControlSelected(selectedStoredGame.getId(), false);
            }
            setStoredGameControlSelected(storedGame.getId(), true);
        }
        selectedStoredGame = storedGame;

        loadGameButton.setVisible(selectedStoredGame != null);
        loadGameButton.getTag().setVisible(selectedStoredGame != null);
    }

    private void setStoredGameControlSelected(int id, boolean selected) {
        for(StoredGameControl storedGameControl: listStoredGamesControls) {
            if(storedGameControl.getStoredGame() != null && storedGameControl.getStoredGame().getId() == id) {
                storedGameControl.setSelected(selected);
            }
        }
    }

    public void showLoadGames(Collection<StoredGame> storedGames) {
        mainTable.clearChildren();
        listStoredGamesControls.clear();
        mainScrollPane.setScrollY(0);
        setSelectedStoredGame(null);

        if(storedGames.size() > 0) {
            for (StoredGame storedGame : storedGames) {
                StoredGameControl storedGameControl = new StoredGameControl(storedGame);
                storedGameControl.addListener(new StoredGameControlListener() {
                    @Override
                    public void storedGameClicked(StoredGame storedGame) {
                        setSelectedStoredGame(selectedStoredGame != null && selectedStoredGame.getId() == storedGame.getId() ? null : storedGame);
                    }

                    @Override
                    public void deleteStoredGame(final StoredGame storedGame) {
                        confirmWindow.showConfirmWindowPopUp(Localization.getInstance().getTranslation("OutGame", "confirmDeleteGameTitle"),
                                Localization.getInstance().getTranslation("OutGame", "deleteGameMessage"), new ConfirmWindowListener() {
                                    @Override
                                    public void acceptPressed() {
                                        notifyDeleteGame(storedGame);
                                    }

                                    @Override
                                    public void cancelPressed() {

                                    }
                                },  MainMenuScreen.ShowHideWindowAnimationDuration);
                    }
                });
                mainTable.add(storedGameControl).expandX().row();
                listStoredGamesControls.add(storedGameControl);
            }
        } else {
            mainTable.add(noSavedGamesMessageLabel);
        }

        mainTable.pack();
    }

    private void notifyDeleteGame(StoredGame storedGame) {
        for (LoadGameScreenEventsListener listener : listeners) {
            listener.deleteGame(storedGame);
        }
    }

    private void notifyLoadGame(StoredGame storedGame) {
        for (LoadGameScreenEventsListener listener : listeners) {
            listener.loadGame(storedGame);
        }
    }

    public void onBackPressed() {
        if(confirmWindow.isVisible()) {
            confirmWindow.hidePopUp(MainMenuScreen.ShowHideWindowAnimationDuration);
        } else {
            for (LoadGameScreenEventsListener listener : listeners) {
                listener.exit();
            }
        }
    }
}
