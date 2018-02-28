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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Skill;

import java.util.ArrayList;

public class AttributeSkillEditListControl extends Table {

    private ArrayList<AttributeSkillEditControlListener> listeners;
    private ArrayList<AttributeSkillEditControl> listControls;

    public AttributeSkillEditListControl(ArrayList<Attribute> listAttributes) {
        listeners = new ArrayList<AttributeSkillEditControlListener>();
        listControls = new ArrayList<AttributeSkillEditControl>(listAttributes.size());

        buildControls(listAttributes);
        pack();
    }

    public AttributeSkillEditListControl(ArrayList<Skill> listSkills, boolean parameterToAllowSameName) {
        listeners = new ArrayList<AttributeSkillEditControlListener>();
        listControls = new ArrayList<AttributeSkillEditControl>(listSkills.size());

        buildControlsSkills(listSkills);
        pack();
    }

    private void buildControls(ArrayList<Attribute> listAttributes) {
        float heightSeparation = Gdx.graphics.getHeight() * 0.02f;
        for(Attribute attribute: listAttributes) {
            AttributeSkillEditControl control = new AttributeSkillEditControl(attribute);
            control.addListener(new AttributeSkillEditControlListener() {
                @Override
                public void increasePressed(int currentValue, int id) {
                    for(AttributeSkillEditControlListener listener: listeners) {
                        listener.increasePressed(currentValue, id);
                    }
                }

                @Override
                public void decreasePressed(int currentValue, int id) {
                    for(AttributeSkillEditControlListener listener: listeners) {
                        listener.decreasePressed(currentValue, id);
                    }
                }
            });
            add(control).right().padBottom(heightSeparation);
            row();
            listControls.add(control);
        }
    }

    private void buildControlsSkills(ArrayList<Skill> listSkills) {
        float heightSeparation = 0; // Gdx.graphics.getHeight() * 0.01f;
        for(Skill skill: listSkills) {
            AttributeSkillEditControl control = new AttributeSkillEditControl(skill);
            control.addListener(new AttributeSkillEditControlListener() {
                @Override
                public void increasePressed(int currentValue, int id) {
                    for(AttributeSkillEditControlListener listener: listeners) {
                        listener.increasePressed(currentValue, id);
                    }
                }

                @Override
                public void decreasePressed(int currentValue, int id) {
                    for(AttributeSkillEditControlListener listener: listeners) {
                        listener.decreasePressed(currentValue, id);
                    }
                }
            });
            add(control).right().padBottom(heightSeparation);
            row();
            listControls.add(control);
        }
    }

    public void addListener(AttributeSkillEditControlListener listener) {
        listeners.add(listener);
    }

    public void setValue(int id, int value) {
        for (AttributeSkillEditControl control: listControls) {
            if(control.getId() == id) {
                control.setValue(value);
                break;
            }
        }
    }

    public void refreshLocalizableItems(ArrayList<Attribute> listAttributes) {
        for (Attribute attribute: listAttributes) {
            for (AttributeSkillEditControl control: listControls) {
                if(attribute.getId() == control.getId()) {
                    control.refreshLocalizableItems(attribute);
                    break;
                }
            }
        }
    }

    public void refreshLocalizableItemsSkills(ArrayList<Skill> listSkills) {
        for (Skill skill: listSkills) {
            for (AttributeSkillEditControl control: listControls) {
                if(skill.getId() == control.getId()) {
                    control.refreshLocalizableItems(skill);
                    break;
                }
            }
        }
    }
}
