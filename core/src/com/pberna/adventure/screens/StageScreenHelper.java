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

package com.pberna.adventure.screens;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.controls2D.ExtendedImageButton;

import java.util.ArrayList;

public class StageScreenHelper {
	
	public static Label createCenteredLabel(String text, LabelStyle labelStyle) {
		Label label = new Label(text, labelStyle);
		label.setAlignment(Align.center);
				
		return label;
	}
	
	public static Label createCenteredLabel(String text, LabelStyle labelStyle, Stage stage) {
		Label label = createCenteredLabel(text, labelStyle);
		stage.addActor(label);
		
		return label;
	}

	public static Label.LabelStyle getLabelStyle(Color color)
	{
		 return new Label.LabelStyle(FontHelper.getLabelsFont(), color);
	}

	public static Label.LabelStyle getSmallLabelStyle(Color color)
	{
		return new Label.LabelStyle(FontHelper.getSmallLabelsFont(), color);
	}
	
	public static Label.LabelStyle getLabelButtonStyle()
	{
		 return new Label.LabelStyle(FontHelper.getButtonsFont(), Color.WHITE);
	}
	
	public static ExtendedImageButton<Label> createLabelImageButton(String text, Stage stage, final Sound onClickSound) {
		ExtendedImageButton<Label> imageButton = internalCreateLabelImageButton(text, onClickSound);
		
		stage.addActor(imageButton);
		stage.addActor(imageButton.getTag());
		
		return imageButton;
	}

	private static ExtendedImageButton<Label> internalCreateLabelImageButton(String text, final Sound onClickSound) {
		ExtendedImageButton<Label> imageButton = new ExtendedImageButton<Label>(
				new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "button")),
				new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "button_pressed")));
		addOnClickSound(imageButton, onClickSound);

		Label label = new Label(text, getLabelButtonStyle());
		label.setTouchable(Touchable.disabled);
		label.setAlignment(Align.center);
		imageButton.setTag(label);

		return imageButton;
	}

	public static void addOnClickSound(Button button, final Sound onClickSound) {
		if (button != null && onClickSound != null) {
			button.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					AudioManager.getInstance().playSound(onClickSound);
					return super.touchDown(event, x, y, pointer, button);
				}
			});
		}
	}
	
	public static ExtendedImageButton<Label> createLabelImageButton(String text, Table table, final Sound onClickSound) {
		ExtendedImageButton<Label> imageButton = internalCreateLabelImageButton(text, onClickSound);

		Stack stack = new Stack();
		stack.addActor(imageButton);
		stack.addActor(imageButton.getTag());
		table.add(stack);
		
		return imageButton;
	}

	public static ImageButton createImageButton(Stage stage, String textureAtlasAlias, String regionNameUp,
												String regionNameDown, final Sound onClickSound) {
		ImageButton button = new ImageButton(
				new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion(textureAtlasAlias, regionNameUp)),
				new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion(textureAtlasAlias, regionNameDown)));
		addOnClickSound(button, onClickSound);

		stage.addActor(button);

		return button;
	}
	
	public static Stack createStackWithActors(Actor... actors) {
		Stack stack = new Stack();
		float maxWidth = 0;
		float maxHeight = 0;
		
		for(Actor actor: actors) {
			stack.addActor(actor);
			maxWidth = Math.max(actor.getWidth(), maxWidth);
			maxHeight = Math.max(actor.getHeight(), maxHeight);
		}
		
		if(maxWidth > 0) {
			stack.setWidth(maxWidth);
		}
		
		if(maxHeight > 0) {
			stack.setHeight(maxHeight);
		}
		
		return stack;
	}

	public static Stack createStackWithActors(ArrayList listActors, Actor... actors) {
		Stack stack = new Stack();
		float maxWidth = 0;
		float maxHeight = 0;

		for(Object obj: listActors) {
			if(obj instanceof Actor) {
				Actor actor = (Actor) obj;
				stack.addActor(actor);
				maxWidth = Math.max(actor.getWidth(), maxWidth);
				maxHeight = Math.max(actor.getHeight(), maxHeight);
			}
		}

		for(Actor actor: actors) {
			stack.addActor(actor);
			maxWidth = Math.max(actor.getWidth(), maxWidth);
			maxHeight = Math.max(actor.getHeight(), maxHeight);
		}

		if(maxWidth > 0) {
			stack.setWidth(maxWidth);
		}

		if(maxHeight > 0) {
			stack.setHeight(maxHeight);
		}

		return stack;
	}
	
	public static HorizontalGroup createHorizontalGroupWithActors(Actor... actors) {
		HorizontalGroup group = new HorizontalGroup();
		
		for(Actor actor: actors) {
			group.addActor(actor);
		}
		
		return group;
	}
	
	public static VerticalGroup createVerticalGroupWithActors(Actor... actors) {
		VerticalGroup group = new VerticalGroup();
		
		for(Actor actor: actors) {
			group.addActor(actor);
		}
		
		return group;
	}
	
	public static void setImageButtonVisible(ExtendedImageButton<Label> imageButton, boolean visible) {
		imageButton.setVisible(visible);
		imageButton.getTag().setVisible(visible);
	}

	public static String getDecorationText(int length) {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < length; i++) {
			sb.append("_");
		}

		return sb.toString();
	}

	public static Label.LabelStyle getDecorationLabelStyle() {
		return new LabelStyle(FontHelper.getDecorationTextFont(), Color.BLACK);
	}
}
