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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.dependencies.IStoreItemInfo;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.adventure.store.StoreItem;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedTextButton;
import com.pberna.engine.localization.Localization;

import java.util.List;

public class StoreItemControl extends Table {

    private ImageButton storeItemImage;
    private Image selectedBorder;
    private Label storeItemTitleLabel;
    private ExtendedTextButton<StoreItem> storeItemDescriptionButton;
    private Label priceLabel;
    private Table labelsTable;

    private StoreItem storeItem;
    private StoreItemControlListener listener;
    private float paddingWidth;

    public StoreItemControl(StoreItem storeItem) {
        this.storeItem = storeItem;
        listener = null;
        paddingWidth  = 5f;

        buildControl();
    }

    private void buildControl() {
        addStoreItemImageCell();
        addLabelsCell();
    }

    private void addStoreItemImageCell() {
        storeItemImage = new ImageButton(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("items", storeItem.getImageName())));
        StageScreenHelper.addOnClickSound(storeItemImage, AssetRepository.getInstance().getSound("click_button"));
        final StoreItemControl that = this;
        storeItemImage.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(listener != null) {
                    listener.storeItemClicked(that);
                }
            }
        });

        TextureRegion selectedBorderImage =  AssetRepository.getInstance().getTextureRegion("widgets", "selected_border_item");
        selectedBorder = new Image(selectedBorderImage);
        selectedBorder.setVisible(false);
        selectedBorder.setTouchable(Touchable.disabled);

        Stack stack = new Stack();
        stack.add(storeItemImage);
        stack.add(selectedBorder);

        add(stack).center().padLeft(paddingWidth);
    }

    private void addLabelsCell() {
        storeItemTitleLabel = new Label(storeItem.getTitle(), getStoreItemTitleStyle());
        storeItemTitleLabel.setAlignment(Align.left);
        storeItemTitleLabel.setWrap(true);

        storeItemDescriptionButton = new ExtendedTextButton<StoreItem> (getStoreItemDescriptionText(storeItem), getStoreItemDescriptionStyle());
        storeItemDescriptionButton.setTag(storeItem);
        storeItemDescriptionButton.getLabel().setAlignment(Align.top | Align.left);
        storeItemDescriptionButton.getLabel().setWrap(true);
        final StoreItemControl that = this;
        storeItemDescriptionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(listener != null) {
                    listener.storeItemClicked(that);
                }
            }
        });
        priceLabel = new Label(getStoreItemPriceText(storeItem), getStoreItemPriceStyle());
        priceLabel.setAlignment(Align.left);
        priceLabel.setWrap(true);

        labelsTable = new Table();
        labelsTable.align(Align.left);
        labelsTable.add(storeItemTitleLabel).left();
        labelsTable.row();
        labelsTable.add(storeItemDescriptionButton).left();
        labelsTable.row();
        labelsTable.add(priceLabel).left();
        labelsTable.pack();
        add(labelsTable).left().padLeft(paddingWidth).padRight(paddingWidth);
    }

    private Label.LabelStyle getStoreItemTitleStyle() {
        return StageScreenHelper.getLabelStyle(Color.WHITE);
    }

    private static String getStoreItemDescriptionText(StoreItem storeItem) {
        return "[BLACK]" + storeItem.getDescription() + "[]";
    }

    private static String getStoreItemPriceText(StoreItem storeItem) {
        return String.format(Localization.getInstance().getTranslation("OutGame",
                "storeItemPriceLabel", false), storeItem.getPrice());
    }

    private TextButton.TextButtonStyle getStoreItemDescriptionStyle() {
        return new TextButton.TextButtonStyle(null, null, null, FontHelper.getSmallLabelsFont());
    }

    private Label.LabelStyle getStoreItemPriceStyle() {
        return StageScreenHelper.getSmallLabelStyle(Color.BLACK);
    }

    public void refreshLocalizableItems(List<IStoreItemInfo> listStoreItemInfo) {
        StoreItem localizedStoreItem = StoreItem.findStoreItem(StoreItem.getAllStoreItems(), storeItem.getId());
        if(localizedStoreItem != null) {
            storeItem = localizedStoreItem;
        }
        for (IStoreItemInfo storeItemInfo: listStoreItemInfo) {
            if(storeItemInfo.getStoreItemId().equals(storeItem.getPlatformStoreId())) {
                storeItem.setPrice(storeItemInfo.getPrice());
                storeItem.setHasPurchased(storeItemInfo.getHasPurchased());
                break;
            }
        }

        storeItemTitleLabel.setText(storeItem.getTitle());
        storeItemDescriptionButton.getLabel().setText(getStoreItemDescriptionText(storeItem));
        priceLabel.setText(getStoreItemPriceText(storeItem));
    }

    public void setSelected(boolean selected) {
        selectedBorder.setVisible(selected);
    }

    public StoreItem getStoreItem() {
        return storeItem;
    }

    public void setListener(StoreItemControlListener listener) {
        this.listener = listener;
    }

    public void setAvailableWidth(float controlsWidth) {
        setWidth(controlsWidth);
        float labelsWidth = controlsWidth - (paddingWidth * 3f) - Math.max(storeItemImage.getWidth(), selectedBorder.getWidth());
        storeItemTitleLabel.setWidth(labelsWidth);
        storeItemDescriptionButton.setWidth(labelsWidth);

        setCellWidth(storeItemTitleLabel, labelsTable, labelsWidth);
        setCellWidth(storeItemDescriptionButton, labelsTable, labelsWidth);
    }

    private void setCellWidth(Actor actor, Table container, float width) {
        Cell<Actor> cell = container.getCell(actor);
        if(cell != null) {
            cell.width(width);
        }
    }
}
