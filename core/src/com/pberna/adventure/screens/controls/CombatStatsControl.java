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


import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Enemy;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.ingame.InventoryScreenHelper;
import com.pberna.engine.assets.AssetRepository;

public class CombatStatsControl extends Table {

    private static final String PostSeparator = "  ";

    private Label fighterNameLabel;
    private Image initiativeImage;
    private Label initiativeValueLabel;
    private Image attackImage;
    private Label attackValueLabel;
    private Image defenseImage;
    private Label defenseValueLabel;
    private Image magicImage;
    private Label magicValueLabel;

    public CombatStatsControl() {
        defaults().center();

        //fighter name
        fighterNameLabel = new Label("Fulanito", InventoryScreenHelper.getNormalLabelStyle());
        fighterNameLabel.setAlignment(Align.center);
        add(fighterNameLabel);

        //initiative image and label
        initiativeImage = new Image(AssetRepository.getInstance().getTextureRegion("attribute_skills", "initiative"));
        initiativeValueLabel = StageScreenHelper.createCenteredLabel("0" + PostSeparator, InventoryScreenHelper.getNormalLabelStyle());
        add(initiativeImage);
        add(initiativeValueLabel);

        //attack image and label
        attackImage = new Image(AssetRepository.getInstance().getTextureRegion("attribute_skills", "attack"));
        attackValueLabel = StageScreenHelper.createCenteredLabel("00" + PostSeparator, InventoryScreenHelper.getNormalLabelStyle());
        add(attackImage);
        add(attackValueLabel);

        //defense image and label
        defenseImage = new Image(AssetRepository.getInstance().getTextureRegion("attribute_skills", "defense"));
        defenseValueLabel = StageScreenHelper.createCenteredLabel("00" + PostSeparator, InventoryScreenHelper.getNormalLabelStyle());
        add(defenseImage);
        add(defenseValueLabel);

        //magic image and label
        magicImage = new Image(AssetRepository.getInstance().getTextureRegion("attribute_skills", "magic"));
        magicValueLabel = StageScreenHelper.createCenteredLabel("0", InventoryScreenHelper.getNormalLabelStyle());
        add(magicImage);
        add(magicValueLabel);

        pack();
    }

    public void update(Character character) {
        fighterNameLabel.setText(getAdjustedString(character.getName()) + PostSeparator);
        initiativeValueLabel.setText(String.valueOf(character.getTotalInitiativeValue()) + PostSeparator);
        attackValueLabel.setText(String.valueOf(character.getTotalAttackValue()) + PostSeparator);
        defenseValueLabel.setText(String.valueOf(character.getTotalDefenseValue()) + PostSeparator);
        magicValueLabel.setText(String.valueOf(character.getTotalSkillValue(Skill.IdMagic)));
    }

    public void update(Enemy enemy) {
        fighterNameLabel.setText(getAdjustedString(enemy.getName()) + PostSeparator);
        initiativeValueLabel.setText(String.valueOf(enemy.getInitiativeValue()) + PostSeparator);
        attackValueLabel.setText(String.valueOf(enemy.getAttackValue()) + PostSeparator);
        defenseValueLabel.setText(String.valueOf(enemy.getDefenseValue()) + PostSeparator);
        magicValueLabel.setText(String.valueOf(enemy.getMagicValue()));
    }

    private String getAdjustedString(String text) {
        if(text.length() <= Character.MaxCharacterNameLength) {
            return text;
        }

        if(text.contains(" ")) {
            int midLength = text.length() / 2;
            int distanceToMiddle = text.length();
            int selectedIndex = -1;

            int index = text.indexOf(" ");
            while (index >= 0) {
                if(Math.abs(midLength - index) < distanceToMiddle) {
                    selectedIndex = index;
                    distanceToMiddle = Math.abs(midLength - index);
                }

                index = text.indexOf(" ", index + 1);
            }

            if(selectedIndex > 0) {
                return text.substring(0, selectedIndex) + "\n" + text.substring(selectedIndex + 1,
                        text.length());
            }
        }

        return text;
    }
}
