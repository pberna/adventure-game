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

package com.pberna.adventure.screens.pjCreation.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.assets.AssetRepository;

import java.util.ArrayList;

public class AttributeSkillEditControl extends Table {

    private Label titleLabel;
    private Label valueLabel;
    private Label descriptionLabel;

    private int id;
    private int value;
    private ArrayList<AttributeSkillEditControlListener> listeners;

    public AttributeSkillEditControl(Attribute attribute) {
        id = attribute.getId();
        value = 0;
        listeners = new ArrayList<AttributeSkillEditControlListener>();

        buildControl(attribute.getImageName(), attribute.getName(), attribute.getDescription());
        pack();
    }

    public AttributeSkillEditControl(Skill skill) {
        id = skill.getId();
        value = 0;
        listeners = new ArrayList<AttributeSkillEditControlListener>();

        buildControl(skill.getImageName(), skill.getName(), skill.getDescription());
        pack();
    }

    private void buildControl(String imageName, String title, String description) {
        float widthSeparation = Gdx.graphics.getWidth() * 0.045f;
        defaults().center().padRight(widthSeparation);

        //image
        Image image = new Image(AssetRepository.getInstance().getTextureRegion("attribute_skills", imageName));
        add(image).width(image.getWidth()).height(image.getHeight());

        //titleLabel
        titleLabel = new Label(title, StageScreenHelper.getLabelStyle(Color.BLACK));
        add(titleLabel);

        //valueLabel
        valueLabel = new Label("00", StageScreenHelper.getLabelStyle(Color.WHITE));
        add(valueLabel);

        //substractButton
        ImageButton substractButton = new ImageButton(
                new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "substract_button")),
                new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "substract_button_pressed")));
        StageScreenHelper.addOnClickSound(substractButton, AssetRepository.getInstance().getSound("click_button"));
        substractButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for(AttributeSkillEditControlListener listener: listeners) {
                    listener.decreasePressed(value, id);
                }
            }
        });
        add(substractButton);

        //addButton
        ImageButton addButton = new ImageButton(
                new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "add_button")),
                new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "add_button_pressed")));
        StageScreenHelper.addOnClickSound(addButton, AssetRepository.getInstance().getSound("click_button"));
        addButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for(AttributeSkillEditControlListener listener: listeners) {
                    listener.increasePressed(value, id);
                }
            }
        });
        add(addButton).padRight(0);

        //descriptionLabel
        descriptionLabel = new Label(description, StageScreenHelper.getSmallLabelStyle(Color.BLACK));
        descriptionLabel.setAlignment(Align.center);
        descriptionLabel.setWrap(true);
        row();
        add(descriptionLabel).colspan(5).width(getPrefWidth()).center();
    }

    public void addListener(AttributeSkillEditControlListener listener) {
        listeners.add(listener);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        valueLabel.setText(String.valueOf(value));
    }

    public int getId() {
        return this.id;
    }

    public void refreshLocalizableItems(Attribute attribute) {
        if(id == attribute.getId()) {
            titleLabel.setText(attribute.getName());
            descriptionLabel.setText(attribute.getDescription());
        }
    }

    public void refreshLocalizableItems(Skill skill) {
        if(id == skill.getId()) {
            titleLabel.setText(skill.getName());
            descriptionLabel.setText(skill.getDescription());
        }
    }
}
