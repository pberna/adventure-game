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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.dependencies.IStoreItemInfo;
import com.pberna.adventure.dependencies.IStoreQuerierCallback;
import com.pberna.adventure.dependencies.Settings;
import com.pberna.adventure.store.PlatformResolver;
import com.pberna.adventure.store.PurchaseManager;
import com.pberna.adventure.store.StoreItem;
import com.pberna.adventure.screens.ExitListener;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.controls.StoreItemControl;
import com.pberna.adventure.screens.controls.StoreItemControlListener;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.logging.Logger;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.screens.controls.ScrollPaneIndicator;
import com.pberna.engine.screens.shapes.ShapesHelper;
import com.pberna.engine.screens.windows.ConfirmWindow;
import com.pberna.engine.screens.windows.ConfirmWindowListener;
import com.pberna.engine.screens.windows.WaitWindow;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.graphics.ImageManipulationHelper;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;

import java.util.ArrayList;
import java.util.List;

public class StoreScreen extends BaseStageScreen implements ILocalizable {
    private static final float LowerPanelPercentageHeight = 0.10f;
    private static final float InfoPanelPercentageHeight = 0.06f;
    private static final float MainPanelPercentageHeight = 1f - LowerPanelPercentageHeight - InfoPanelPercentageHeight;
    private static final float PercentageBorderScreen = 0.03f;

    private Table mainTable;
    private ScrollPane mainScrollPane;
    private ScrollPaneIndicator paneIndicator;
    private ExtendedImageButton<Label> exitButton;
    private ExtendedImageButton<Label> buyButton;
    private ArrayList<StoreItemControl> storeItemControls;
    private StoreItemControl selectedStoreItemControl;
    private ImageButton restorePurchasesButton;
    private boolean storeInfoLoaded;
    private List<IStoreItemInfo> listStoreItemInfo;
    private Label errorMessageLabel;

    private ConfirmWindow confirmWindow;
    private WaitWindow waitWindow;

    private Rectangle mainPanel;
    private Rectangle infoPanel;
    private Rectangle lowerPanel;
    private ExitListener exitListener;

    public StoreScreen() {
        setBackgroundTextureName("old_page_dark");
        storeItemControls = new ArrayList<StoreItemControl>();
        selectedStoreItemControl = null;
        storeInfoLoaded = false;
        listStoreItemInfo = new ArrayList<IStoreItemInfo>();

        mainPanel = new Rectangle();
        infoPanel = new Rectangle();
        lowerPanel = new Rectangle();
        exitListener = null;

        buildStage();
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

        //exit button
        exitButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("Common", "exitLabel"), stage, AssetRepository.getInstance().getSound("click_button"));
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitButtonPressed();
            }
        });

        //buy button
        buyButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().
                getTranslation("Common", "buyLabel"), stage, AssetRepository.getInstance().getSound("click_button"));
        buyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                purchaseSelectedItem();
            }
        });
        buyButton.setVisible(false);
        buyButton.getTag().setVisible(false);

        //restorePurchasesButton
        TextureRegion restorePurchaseTexture = AssetRepository.getInstance().getTextureRegion("widgets", "reload_purchases");
        restorePurchasesButton = new ImageButton(new TextureRegionDrawable(restorePurchaseTexture),
                new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegionForPressed(restorePurchaseTexture)));
        restorePurchasesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                restorePurchases();
            }
        });
        StageScreenHelper.addOnClickSound(restorePurchasesButton, AssetRepository.getInstance().getSound("click_button"));
        stage.addActor(restorePurchasesButton);

        //storeItemControl
        addStoreItemControls();

        //errorMessageLabel
        errorMessageLabel = new Label("", StageScreenHelper.getLabelStyle(Color.ORANGE));
        errorMessageLabel.setAlignment(Align.center);
        errorMessageLabel.setWrap(true);
        stage.addActor(errorMessageLabel);

        //confirmWindow
        confirmWindow = new ConfirmWindow();
        confirmWindow.setWindowBackGround(getModalWindowBackground());
        confirmWindow.setBorder(ShapesHelper.getBorder(confirmWindow));
        confirmWindow.setParentScreen(this);

        confirmWindow.setVisible(false);
        stage.addActor(confirmWindow);
        confirmWindow.setWidth(0);
        confirmWindow.setHeight(0);

        //waitImage
        waitWindow = new WaitWindow();
        waitWindow.setParentScreen(this);

        waitWindow.setVisible(false);
        stage.addActor(waitWindow);
    }

    private void addStoreItemControls() {
        mainTable.clearChildren();
        storeItemControls.clear();
        selectedStoreItemControl = null;
        for(StoreItem storeItem: StoreItem.getAllStoreItems()) {
            StoreItemControl storeItemControl = new StoreItemControl(storeItem);
            storeItemControl.setListener(new StoreItemControlListener() {
                @Override
                public void storeItemClicked(StoreItemControl storeItemControl) {
                    handleStoreItemClicked(storeItemControl);
                }
            });

            mainTable.add(storeItemControl).expandX();
            mainTable.row();
            storeItemControls.add(storeItemControl);
        }
    }

    private void exitButtonPressed() {
        if(exitListener != null) {
            exitListener.exit(this);
        }
        hideWaitImage();
    }

    private void handleStoreItemClicked(StoreItemControl storeItemControl) {
        if(selectedStoreItemControl == null) {
            selectedStoreItemControl = storeItemControl;
            selectedStoreItemControl.setSelected(true);
            if(storeItemControl.getStoreItem().hasPurchased()) {
                setErrorMessageText(Localization.getInstance().getTranslation("OutGame", "itemPurchased"));
            } else {
                buyButton.setVisible(true);
                buyButton.getTag().setVisible(true);
                setErrorMessageText("");
            }
        } else if(selectedStoreItemControl == storeItemControl) {
            selectedStoreItemControl.setSelected(false);
            selectedStoreItemControl = null;
            buyButton.setVisible(false);
            buyButton.getTag().setVisible(false);
            setErrorMessageText("");
        } else {
            selectedStoreItemControl.setSelected(false);
            selectedStoreItemControl = storeItemControl;
            storeItemControl.setSelected(true);
            if(storeItemControl.getStoreItem().hasPurchased()) {
                setErrorMessageText(Localization.getInstance().getTranslation("OutGame", "itemPurchased"));
            } else {
                buyButton.setVisible(true);
                buyButton.getTag().setVisible(true);
                setErrorMessageText("");
            }
        }
    }

    private void purchaseSelectedItem() {
        if(selectedStoreItemControl == null || selectedStoreItemControl.getStoreItem().hasPurchased()) {
            return;
        }

        try {
            PlatformResolver platformResolver = PurchaseManager.getInstance().getPlatformResolver();
            if(platformResolver != null) {
                platformResolver.requestPurchase(selectedStoreItemControl.
                        getStoreItem().getPlatformStoreId());
            }
        } catch (Exception ex) {
            Logger.getInstance().addLogInfo(Logger.TagPay, "Error in purchase: " + ex.getMessage());
        }
        unselectItemToPurchase();
    }

    private void unselectItemToPurchase()  {
        if(selectedStoreItemControl != null) {
            handleStoreItemClicked(selectedStoreItemControl);
        }
    }

    private void restorePurchases() {
        unselectItemToPurchase();
        confirmWindow.showConfirmWindowPopUp(Localization.getInstance().getTranslation("OutGame", "restorePurchasesTitle"),
                Localization.getInstance().getTranslation("OutGame", "retorePurchasesConfirmation"), new ConfirmWindowListener() {
                    @Override
                    public void acceptPressed() {
                        try {
                            PlatformResolver platformResolver = PurchaseManager.getInstance().getPlatformResolver();
                            if (platformResolver != null) {
                                platformResolver.requestPurchaseRestore();
                            } else {
                                Logger.getInstance().addLogInfo(Logger.TagPay, "Not platform resolver set");
                            }
                        }
                        catch (Exception ex) {
                            Logger.getInstance().addLogInfo(Logger.TagPay, "Error in request purchase: " + ex.getMessage());
                        }
                    }

                    @Override
                    public void cancelPressed() {

                    }
                }, MainMenuScreen.ShowHideWindowAnimationDuration);
    }

    @Override
    protected void recalculateActorsPositions(float width, float height) {
        super.recalculateActorsPositions(width, height);
        updatePanelsSizes(width, height);
        float padding = width * PercentageBorderScreen;
        float controlsWidth = mainPanel.getWidth() - (padding * 2.0f);

        //adventure data & main scroll pane
        mainTable.setWidth(width);
        mainScrollPane.setWidth(mainPanel.getWidth());
        mainScrollPane.setHeight(mainPanel.getHeight());
        PositionerHelper.setPositionFromCorner(mainScrollPane, Corner.TopLeft, 0, 0, mainPanel);
        paneIndicator.updateIndicatorsPositions(padding, padding / 2.0f);

        //start adventure button
        PositionerHelper.setPositionCenteredVerticalFillingRow (new Actor[]
                { buyButton, exitButton}, lowerPanel, HorizontalSide.Left);
        PositionerHelper.setPositionCenterInActor(buyButton, buyButton.getTag());
        PositionerHelper.setPositionCenterInActor(exitButton, exitButton.getTag());

        //restorePurchasesButton
        PositionerHelper.setPositionFromCorner(restorePurchasesButton, Corner.TopRight, padding, padding, mainPanel);

        //store item control
        for(StoreItemControl storeItemControl: storeItemControls) {
            storeItemControl.setAvailableWidth(controlsWidth);

            Cell<StoreItemControl> cell = mainTable.getCell(storeItemControl);
            if(cell != null) {
                cell.width(controlsWidth);
            }
        }

        //error message label
        errorMessageLabel.setWidth(infoPanel.width - (padding * 2.0f));
        PositionerHelper.setPositionCentered(errorMessageLabel, infoPanel);

        //modal windows
        PositionerHelper.setPositionCentered(confirmWindow, stage);
        setBorderOnActor(confirmWindow, confirmWindow.getBorder());
        waitWindow.setWidth(mainPanel.getWidth());
        waitWindow.setHeight(mainPanel.getHeight());
        PositionerHelper.setPositionCentered(waitWindow, mainPanel);
    }

    private void updatePanelsSizes(float width, float height) {
        float lowerPanelHeight = height * LowerPanelPercentageHeight;
        float infoPanelHeight = height * InfoPanelPercentageHeight;
        float mainPanelHeight = height * MainPanelPercentageHeight;

        lowerPanel.set(0, 0, width, lowerPanelHeight);
        infoPanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, infoPanelHeight);
        mainPanel.set(0, infoPanel.getY() + infoPanel.getHeight(), width, mainPanelHeight);
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

        if(selectedStoreItemControl != null) {
            selectedStoreItemControl.setSelected(false);
        }
        selectedStoreItemControl = null;
        buyButton.setVisible(false);
        buyButton.getTag().setVisible(false);
        setErrorMessageText("");

        if(paneIndicator != null) {
            paneIndicator.refreshContinousRendering();
        }
        sendToFrontIfVisible(waitWindow);
        sendToFrontIfVisible(confirmWindow);

        if(!storeInfoLoaded) {
            showWaitImage();
            loadStoreInfo();
        }
    }

    private void loadStoreInfo() {
        try {
            Settings.getInstance().getStoreQuerier().getStoreItemsInfo(getListSkusItems(),
                new IStoreQuerierCallback() {
                    @Override
                    public void queryInventoryFinished(List<IStoreItemInfo> storeItemInfoList) {
                        listStoreItemInfo = storeItemInfoList;
                        for(StoreItemControl storeItemControl: storeItemControls) {
                            storeItemControl.refreshLocalizableItems(listStoreItemInfo);
                        }
                        storeInfoLoaded = true;
                        hideWaitImage();
                    }
                });
        } catch (Exception ex) {
            Logger.getInstance().addLogInfo(Logger.TagPay, "Error updating items prices from store: " + ex.getMessage());
            storeInfoLoaded = true;
            hideWaitImage();
        }
    }

    private List<String> getListSkusItems() {
        ArrayList<String>  listSkus = new ArrayList<String>(storeItemControls.size());
        for(StoreItemControl storeItemControl: storeItemControls) {
            listSkus.add(storeItemControl.getStoreItem().getPlatformStoreId());
        }

        return listSkus;
    }

    public void setListener(ExitListener exitListener) {
        this.exitListener = exitListener;
    }

    @Override
    public void refreshLocalizableItems() {
        //exitButton and buy button
        exitButton.getTag().setText(Localization.getInstance().getTranslation("Common", "exitLabel"));
        buyButton.getTag().setText(Localization.getInstance().getTranslation("Common", "buyLabel"));
        confirmWindow.refreshLocalizableItems();

        //store item control
        for(StoreItemControl storeItemControl: storeItemControls) {
            storeItemControl.refreshLocalizableItems(listStoreItemInfo);
        }
    }

    public void onBackPressed() {
       if(confirmWindow.isVisible()) {
            confirmWindow.hidePopUp(MainMenuScreen.ShowHideWindowAnimationDuration);
        } else {
            exitButtonPressed();
        }
    }

    private void showWaitImage() {
        waitWindow.setVisible(true);
        ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(waitWindow);
    }

    private void hideWaitImage() {
        waitWindow.setVisible(false);
        ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(waitWindow);
    }

    private void setErrorMessageText(String errorMessageText) {
        errorMessageLabel.setText(errorMessageText);
        errorMessageLabel.setVisible(!errorMessageText.equals(""));
    }
}
