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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.localization.Localization;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.ingame.InventoryScreenHelper;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Enemy;

public class LifePowerPointsControl extends Table {
	
	private Label lifePointsLabel;
	private Label powerPointsLabel;
	private Label luckPointsLabel;
	private Image lifePointsImage;
	private Image powerPointsImage;
	private Image luckPointsImage;
	
	public LifePowerPointsControl() {
		defaults().center();
		//life points label and image
		lifePointsLabel = StageScreenHelper.createCenteredLabel("00/00", InventoryScreenHelper.getNormalLabelStyle());
		lifePointsImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "life_points"));
		add(lifePointsImage);
		add(lifePointsLabel);
		
		//power points label and image
		powerPointsLabel = StageScreenHelper.createCenteredLabel("00/00", InventoryScreenHelper.getNormalLabelStyle());
		powerPointsImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "mana_points"));		
		add(powerPointsImage);
		add(powerPointsLabel);

		//luck points label and image
		luckPointsLabel = StageScreenHelper.createCenteredLabel("00/00", InventoryScreenHelper.getNormalLabelStyle());
		luckPointsImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "luck_points"));
		add(luckPointsImage);
		add(luckPointsLabel);

		pack();
	}
	
	public void updatePointsLabel(Character character) {
		updatePointsLabel(character.getCurrentLifePoints(), character.getMaximumLifePoints(), 
				character.getCurrentPowerPoints(), character.getMaximumPowerPoints(),
				character.getCurrentLuckPoints(), character.getMaximumLuckPoints());
	}
	
	public void updatePointsLabel(Enemy enemy) {
		updatePointsLabel(enemy.getCurrentLifePoints(), enemy.getMaximumLifePoints(), 
				enemy.getCurrentPowerPoints(), enemy.getMaximumPowerPoints(),
				enemy.getCurrentLuckPoints(), enemy.getMaximumLuckPoints());
	}
	
	public void resetPointsLabel() {
		updatePointsLabel(0, 0, 0, 0, 0, 0);
	}
	
	public void updatePointsLabel(int currentLifePoints, int maximumLifePoints, int currentPowerPoints, int maximumPowerPoints,
								  int currentLuckPoints, int maximumLuckPoints) {
		lifePointsLabel.setText(String.format(" " + Localization.getInstance().getTranslation("PjCharacterSheet", "lifePoints") + "  ", 
				currentLifePoints, maximumLifePoints));
		
		powerPointsLabel.setText(String.format(" " + Localization.getInstance().getTranslation("PjCharacterSheet", "powerPoints") + "  ",
				currentPowerPoints, maximumPowerPoints));

		luckPointsLabel.setText(String.format(" " + Localization.getInstance().getTranslation("PjCharacterSheet", "luckPoints"),
				currentLuckPoints, maximumLuckPoints));
	}
	
	public void setLifePointsVisible(boolean visible) {
		lifePointsImage.setVisible(visible);
		lifePointsLabel.setVisible(visible);
		if(!visible) {
			removeFromTable(lifePointsImage);
			removeFromTable(lifePointsLabel);
		} else {
			add(lifePointsImage);
			add(lifePointsLabel);
		}
		pack();
	}

	public void setLuckPointsVisible(boolean visible) {
		luckPointsImage.setVisible(visible);
		luckPointsLabel.setVisible(visible);
		if(!visible) {
			removeFromTable(luckPointsImage);
			removeFromTable(luckPointsLabel);
		} else {
			add(luckPointsImage);
			add(luckPointsLabel);
		}
		pack();
	}

	public void setPowerPointsVisible(boolean visible) {
		powerPointsImage.setVisible(visible);
		powerPointsLabel.setVisible(visible);
		if(!visible) {
			removeFromTable(powerPointsImage);
			removeFromTable(powerPointsLabel);
		} else {
			add(powerPointsImage);
			add(powerPointsLabel);
		}
		pack();
	}

	private void removeFromTable(Actor actor) {
		Cell cell = this.getCell(actor);
		actor.remove();
		if(cell != null) {
			cell.width(0);
		}
	}
}
