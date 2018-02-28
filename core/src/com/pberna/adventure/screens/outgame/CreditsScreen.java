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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.XmlReader;
import com.pberna.adventure.screens.ExitListener;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.screens.controls.ScrollPaneIndicator;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.PositionerHelper;

import java.io.IOException;
import java.util.ArrayList;

public class CreditsScreen extends BaseStageScreen implements ILocalizable {

    private static final float LowerPanelPercentageHeight = 0.10f;
    private static final float MainPanelPercentageHeight = 0.90f;
    private static final float PercentageBorderScreen = 0.03f;

    private Table mainTable;
    private ScrollPane mainScrollPane;
    private ScrollPaneIndicator paneIndicator;
    private ExtendedImageButton<Label> exitButton;
    private ArrayList<Label> creditsLabels;

    private Rectangle mainPanel;
    private Rectangle lowerPanel;
    private ExitListener exitListener;

    public CreditsScreen() {
        setBackgroundTextureName("old_page");

        creditsLabels = new ArrayList<Label>();
        mainPanel = new Rectangle();
        lowerPanel = new Rectangle();
        exitListener = null;

        buildStage();

        buildCreditList();
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
                if(exitListener != null) {
                    exitListener.exit(this);
                }
            }
        });
    }

    private void buildCreditList() {
        mainTable.clearChildren();
        creditsLabels.clear();

        XmlReader xmlReader = new XmlReader();
        try
        {
            XmlReader.Element root = xmlReader.parse(Gdx.files.internal("data/credits.xml"));
            if(root.getName().equals("credits")) {
                for (int i = 0; i < root.getChildCount(); i++) {
                    XmlReader.Element childNode = root.getChild(i);
                    createCreditLabel(childNode);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void createCreditLabel(XmlReader.Element childNode) {
        Label label = null;
        if(childNode.getName().equals("mainTitle")) {
            label = createMainTitleLabel(Localization.getInstance().getTranslation("Credits", childNode.getText(), true));
        } else if(childNode.getName().equals("title")) {
            label = createTitleLabel(Localization.getInstance().getTranslation("Credits", childNode.getText(), true));
        } else if(childNode.getName().equals("p")) {
            label = createParagraphLabel(Localization.getInstance().getTranslation("Credits", childNode.getText(), false));
        }
        mainTable.add(label);
        mainTable.row();
        creditsLabels.add(label);
    }

    private Label createMainTitleLabel(String text) {
        return createLabel("\n" + text, FontHelper.getLabelsFont(), Color.BLACK);
    }

    private Label createTitleLabel(String text) {
        return createLabel(text, FontHelper.getLabelsFont(), Color.BLACK);
    }

    private Label createParagraphLabel(String text) {
        return createLabel(text + "\n", FontHelper.getOptionsTextForLabelFont(), Color.BLACK);
    }

    private Label createLabel(String text, BitmapFont font, Color color) {
        Label label = new Label(text, new Label.LabelStyle(font, color));
        label.setAlignment(Align.center);
        label.setWrap(true);
        return label;
    }

    @Override
    protected void recalculateActorsPositions(float width, float height) {
        super.recalculateActorsPositions(width, height);
        updatePanelsSizes(width, height);
        float padding = width * PercentageBorderScreen;
        float labelsWidth = mainPanel.getWidth() - (padding * 2.0f);

        //adventure data & main scroll pane
        mainTable.setWidth(width);
        mainScrollPane.setWidth(mainPanel.getWidth());
        mainScrollPane.setHeight(mainPanel.getHeight());
        PositionerHelper.setPositionFromCorner(mainScrollPane, Corner.TopLeft, 0, 0, mainPanel);
        paneIndicator.updateIndicatorsPositions(padding, padding / 2.0f);

        //credits labels
        for(Label label: creditsLabels) {
            label.setWidth(labelsWidth);
            Cell<Label> cell = mainTable.getCell(label);
            if(cell != null) {
                cell.width(width);
            }
        }

        //start adventure button
        PositionerHelper.setPositionCentered(exitButton, lowerPanel);
        PositionerHelper.setPositionCenterInActor(exitButton, exitButton.getTag());
    }

    private void updatePanelsSizes(float width, float height) {
        float lowerPanelHeight = height * LowerPanelPercentageHeight;
        float mainPanelHeight = height * MainPanelPercentageHeight;

        lowerPanel.set(0, 0, width, lowerPanelHeight);
        mainPanel.set(0, lowerPanel.getY() + lowerPanel.getHeight(), width, mainPanelHeight);
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
        mainScrollPane.setScrollY(0);
    }

    public void setListener(ExitListener exitListener) {
        this.exitListener = exitListener;
    }

    @Override
    public void refreshLocalizableItems() {
        //exitButton
        exitButton.getTag().setText(Localization.getInstance().getTranslation("Common", "exitLabel"));
        buildCreditList();
    }
}
