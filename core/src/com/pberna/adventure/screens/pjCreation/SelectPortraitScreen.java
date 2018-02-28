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

package com.pberna.adventure.screens.pjCreation;

import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.adventure.screens.controls.PortraitsListControl;
import com.pberna.adventure.screens.controls.PortraitsListControlListener;

public class SelectPortraitScreen extends BasePjCreationScreen
implements IModelEditingScreen<String>{
			
	private static final float PortraitBorderPercentage = 0.1f;
	
	private int selectedPortraitIndex;
	private int portraitsCount;
	private String portraitTexturePrefix;
	
	private PortraitsListControl portraitsList;
		
	public SelectPortraitScreen()
	{
		super(Localization.getInstance().getTranslation(
				"PjCreation", "choosePortrait"), "selected_border_portrait");	
					
		portraitsList = new PortraitsListControl();
		portraitsList.addListener(new PortraitsListControlListener() {			
			@Override
			public void portraitClicked(int portraitIndex) {
				setSelectedPortraitIndex(portraitIndex);				
			}
		});
		
		initializeMainScrollPane(portraitsList);
		setSelectedPortraitIndex(-1);
	}
	
	@Override
	public void setModel(String model) {
		
		if(model.startsWith(PortraitsListControl.ManTextuxePrefix)) {
			setPortraitGenre(true, false);
			int newPortraitIndex = Integer.parseInt(model.replace(PortraitsListControl.ManTextuxePrefix, "")) - 1;			
			setSelectedPortraitIndex(newPortraitIndex);
		} else if(model.startsWith(PortraitsListControl.WomanTextuxePrefix)) {
			setPortraitGenre(false, false);
			int newPortraitIndex = Integer.parseInt(model.replace(PortraitsListControl.WomanTextuxePrefix, "")) - 1;			
			setSelectedPortraitIndex(newPortraitIndex);
		}		
	}

	@Override
	public String getModel() {
		if(selectedPortraitIndex >=0 && selectedPortraitIndex < portraitsCount) {
			return portraitTexturePrefix + (selectedPortraitIndex + 1);
		}
		return "";
	}	
		
	public void setPortraitGenre(boolean maleGenre, boolean resetScrollPane) {
		if(maleGenre){
			portraitsCount = PortraitsListControl.ManPortraitsCount;			
			portraitTexturePrefix = PortraitsListControl.ManTextuxePrefix;
		} else {
			portraitsCount = PortraitsListControl.WomanPortraitsCount;
			portraitTexturePrefix = PortraitsListControl.WomanTextuxePrefix;
		}
		
		setSelectedPortraitIndex(-1);
		portraitsList.setPortraitGenre(maleGenre);
		if(resetScrollPane) {
			mainScrollPane.setScrollY(0);
		}
	}
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);	
		
		portraitsList.setPaddingWidth(width * PortraitBorderPercentage);
	}	

	public void setSelectedPortraitIndex(int selectedPortraitIndex) {
		
		if(selectedPortraitIndex == this.selectedPortraitIndex)
		{
			portraitsList.setSelectedPortrait(this.selectedPortraitIndex , false);
			this.selectedPortraitIndex = -1;
		}
		else
		{
			portraitsList.setSelectedPortrait(this.selectedPortraitIndex , false);		
			this.selectedPortraitIndex = selectedPortraitIndex;
			portraitsList.setSelectedPortrait(this.selectedPortraitIndex , true);
		}
		
		setAcceptButtonVisibility(this.selectedPortraitIndex >= 0);		
	}
	
	@Override
	public void refreshLocalizableItems() {
		mainText = Localization.getInstance().getTranslation("PjCreation", "choosePortrait");
		
		super.refreshLocalizableItems();
	}
}
