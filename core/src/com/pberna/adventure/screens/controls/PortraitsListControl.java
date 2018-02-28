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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.adventure.screens.StageScreenHelper;

public class PortraitsListControl extends Table {	
		
	public static final int ManPortraitsCount = 10;
	public static final String ManTextureAtlas = "man_portraits";	
	public static final String ManTextuxePrefix = "man";
	public static final int WomanPortraitsCount = 7;
	public static final String WomanTextureAtlas = "woman_portraits";
	public static final String WomanTextuxePrefix = "woman";
	
	private ArrayList<ExtendedImageButton<Integer>>  portraitImages;
	private ArrayList<Image> selectedBorders;
	
	private int portraitsCount;
	private String portraitAtlas;
	private String portraitTexturePrefix;	
	private ArrayList<PortraitsListControlListener> listeners;
	private float paddingWidth;
	
	public PortraitsListControl() {
		portraitImages =  new ArrayList<ExtendedImageButton<Integer>>();
		selectedBorders = new ArrayList<Image>();
				
		portraitsCount = 0;
		portraitAtlas = "";
		portraitTexturePrefix = "";		
		listeners = new ArrayList<PortraitsListControlListener>();		
		this.paddingWidth = 0; 
	}
	
	public void addListener(PortraitsListControlListener listener) {
		listeners.add(listener);
	}
	
	public float getPaddingWidth() {
		return paddingWidth;
	}

	@SuppressWarnings("rawtypes")
	public void setPaddingWidth(float paddingWidth) {
		this.paddingWidth = paddingWidth;
		
		Array<Cell> cells = getCells();
		for(int i = 0; i < cells.size; i++) {
			float padLeft = i%2 == 0 ? paddingWidth : 0;
			float padRight = i%2 > 0 ? paddingWidth : 0;
			float padTop = i < 2 ? paddingWidth : 0;
			float padBottom = paddingWidth;
			cells.get(i).padLeft(padLeft).padRight(padRight).padTop(padTop).padBottom(padBottom);
		}
	}

	public void setPortraitGenre(boolean maleGenre) {		
		if(maleGenre){
			portraitsCount = ManPortraitsCount;
			portraitAtlas = ManTextureAtlas;
			portraitTexturePrefix = ManTextuxePrefix;
		} else {
			portraitsCount = WomanPortraitsCount;
			portraitAtlas = WomanTextureAtlas;
			portraitTexturePrefix = WomanTextuxePrefix;
		}
			
		showPortraits();
	}	
	
	private void showPortraits(){
		clearChildren();
		portraitImages.clear();
		selectedBorders.clear();
		
		for(int i = 0; i < portraitsCount; i++){
			
			ExtendedImageButton<Integer> portraitButton = createPortraitButton(i);
			portraitImages.add(portraitButton);
			
			Image selectedBorder = createSelectedBorderImage();
			selectedBorders.add(selectedBorder);
			
			Stack stack = StageScreenHelper.createStackWithActors(portraitButton, selectedBorder);
			
			float padLeft = i%2 == 0 ? paddingWidth : 0;
			float padRight = i%2 > 0 ? paddingWidth : 0;
			float padTop = i < 2 ? paddingWidth : 0;
			float padBottom = paddingWidth;
						
			add(stack).expandX().center().padLeft(padLeft).padRight(padRight).padTop(padTop).padBottom(padBottom);
			
			if(i > 0 && i%2 > 0) {
				row();
			}
		}
				
		pack();
	}
	
	private ExtendedImageButton<Integer> createPortraitButton(int index) {
		TextureRegion portraitTexture = AssetRepository.getInstance().getTextureRegion(
				portraitAtlas, portraitTexturePrefix + (index + 1));
		ExtendedImageButton<Integer> portraitButton = new ExtendedImageButton<Integer>(new TextureRegionDrawable(portraitTexture));
		portraitButton.setTag(new Integer(index));
		portraitButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				@SuppressWarnings("unchecked")
				ExtendedImageButton<Integer> portraitImageButton = (ExtendedImageButton<Integer>) actor;
				for(PortraitsListControlListener listener: listeners) {
					AudioManager.getInstance().playSound(AssetRepository.getInstance().getSound("click_button"));
					listener.portraitClicked(portraitImageButton.getTag().intValue());
				}					
			}
		});
		
		return portraitButton;
	}
	
	private Image createSelectedBorderImage() {
		TextureRegion selectedBorderImage =  AssetRepository.getInstance().getTextureRegion("widgets", "selected_border_portrait");
		Image selectedBorder = new Image(selectedBorderImage);
		selectedBorder.setVisible(false);
		selectedBorder.setTouchable(Touchable.disabled);
		return selectedBorder;
	}
	
	public void setSelectedPortrait(int index, boolean selected) {
		if(index >= 0 && index < selectedBorders.size()){
			selectedBorders.get(index).setVisible(selected);
		}		
	}
	
	public int getPortraitCount() {
		return this.portraitsCount;
	}
}
