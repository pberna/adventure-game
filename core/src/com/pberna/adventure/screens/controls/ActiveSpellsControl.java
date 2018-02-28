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

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.spells.Spell;
import com.pberna.engine.utils2D.graphics.AnimationsHelper;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;

public class ActiveSpellsControl extends Table implements ILocalizable{

	private static final float ShowSpellAnimationDuration = 0.6f;

	private Label titleLabel;

	public ActiveSpellsControl(float padding) {
		
		titleLabel = StageScreenHelper.createCenteredLabel(Localization.getInstance().getTranslation
				("InGame", "activeSpells"),	StageScreenHelper.getLabelStyle(Color.BLACK));
		defaults().padLeft(padding).padRight(padding);
		add(titleLabel).height(titleLabel.getHeight());

		pack();
	}

	public void updateActiveSpells(Character character, boolean animateLastSpell) {
		ArrayList<Spell> activeSpells = character.getActiveSpells();
		clearChildren();
		add(titleLabel).height(titleLabel.getHeight());
		Image spellImageToAnimate = null;
		
		if(activeSpells.size() > 0) {
			for(int i = 0; i < activeSpells.size(); i++) {
				Image spellImage = new Image(AssetRepository.getInstance().getTextureRegion("spells", activeSpells.get(i).getImageName()));
				if(animateLastSpell && i == (activeSpells.size() - 1)) {
					AnimationsHelper.hideActorByAlpha(spellImage);
					spellImageToAnimate = spellImage;
				}
				add(spellImage).height(titleLabel.getHeight() * 1.5f).width(titleLabel.getHeight()*1.5f);
			}
		}
		pack();

		if(spellImageToAnimate != null) {
			ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(spellImageToAnimate);
			spellImageToAnimate.addAction(AnimationsHelper.getFadeInAction(ShowSpellAnimationDuration));

			final Image finalSpellImageToAnimate = spellImageToAnimate;
			Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(finalSpellImageToAnimate);
				}
			}, ShowSpellAnimationDuration);
		}
	}

	@Override
	public void refreshLocalizableItems() {
		titleLabel.setText(Localization.getInstance().getTranslation("InGame", "activeSpells"));		
	}
}
