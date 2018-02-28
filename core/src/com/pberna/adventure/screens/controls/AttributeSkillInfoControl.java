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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.adventure.screens.StageScreenHelper;

public class AttributeSkillInfoControl extends Table {

	private Image image;
	private Label mainTextLabel;
	private Label mainValueLabel;
	private Label modifierLabel;
	private Label totalLabel;

	private IImageName tag;
	private int mainValue;
	private int modifierValue;
	
	public IImageName getTag() {
		return tag;
	}
	
	public void setMainValue(int mainValue) {
		this.mainValue = mainValue;
		mainValueLabel.setText("  " + String.valueOf(this.mainValue));
		refreshTotal();
	}

	public int getModifierValue() {
		return modifierValue;
	}

	public void setModifierValue(int modifierValue) {
		this.modifierValue = modifierValue;
		if(this.modifierValue < 0) {
			modifierLabel.setText("  -  " + String.valueOf(Math.abs(this.modifierValue)));			
			modifierLabel.setStyle(getNormalLabelStyle(Color.ORANGE));
			modifierLabel.setColor(Color.ORANGE);
			modifierLabel.setVisible(true);
		} else if (this.modifierValue > 0 ) {
			modifierLabel.setText("  +  " + String.valueOf(this.modifierValue));
			modifierLabel.setStyle(getNormalLabelStyle(Color.GREEN));
			modifierLabel.setColor(Color.GREEN);
			modifierLabel.setVisible(true);
		} else {
			modifierLabel.setText("");
			modifierLabel.setStyle(getNormalLabelStyle(Color.BLACK));
			modifierLabel.setColor(Color.BLACK);
			modifierLabel.setVisible(false);
		}
		refreshTotal();
	}

	public void setMainText(String mainText) {
		mainTextLabel.setText(" " + mainText);
		pack();
	}

	public AttributeSkillInfoControl(IImageName tag) {
		this.tag = tag;
		defaults().center();

		image = new Image(AssetRepository.getInstance().getTextureRegion("attribute_skills", tag.getImageName()));
		mainTextLabel = StageScreenHelper.createCenteredLabel("         ", getNormalLabelStyle(Color.BLACK));
		mainValueLabel = StageScreenHelper.createCenteredLabel("0", getNormalLabelStyle(Color.BLACK));
		modifierLabel = StageScreenHelper.createCenteredLabel("", getNormalLabelStyle(Color.BLACK));
		totalLabel = StageScreenHelper.createCenteredLabel("", getNormalLabelStyle(Color.BLACK));

		add(image);
		add(mainTextLabel);
		add(mainValueLabel);
		add(modifierLabel);
		add(totalLabel);
		
		pack();
	}
	
	private void refreshTotal() {
		if(modifierValue != 0) {
			totalLabel.setText("  =  " + String.valueOf(mainValue + modifierValue));
			totalLabel.setVisible(true);
		} else {
			totalLabel.setText("");
			totalLabel.setVisible(false);
		}
	}

	private static Label.LabelStyle getNormalLabelStyle(Color color) {
		return StageScreenHelper.getLabelStyle(color);
	}
}
