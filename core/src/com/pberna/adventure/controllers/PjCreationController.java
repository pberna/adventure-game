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

package com.pberna.adventure.controllers;

import java.util.ArrayList;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Gender;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.screens.pjCreation.CloseScreenListener;
import com.pberna.adventure.screens.pjCreation.SelectAttributesScreen;
import com.pberna.adventure.screens.pjCreation.SelectGenderScreen;
import com.pberna.adventure.screens.pjCreation.SelectNameScreen;
import com.pberna.adventure.screens.pjCreation.SelectPortraitScreen;
import com.pberna.adventure.screens.pjCreation.SelectRaceScreen;
import com.pberna.adventure.screens.pjCreation.SelectSkillsScreen;
import com.pberna.adventure.screens.pjCreation.SelectSpellScreen;
import com.pberna.adventure.pj.Character;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.localization.ILocalizable;

public class PjCreationController extends BaseController{

	private Character character;
	private Boolean isCharacterCreated;
	
	private ArrayList<ILocalizable> listLocalizables;
	private SelectPortraitScreen selectPortraitScreen;
	private SelectGenderScreen selectGenderScreen;
	private SelectRaceScreen selectRaceScreen;
	private SelectAttributesScreen selectAttributesScreen;
	private SelectSkillsScreen selectSkillsScreen;
	private SelectSpellScreen selectSpellsScreen;
	private SelectNameScreen selectNameScreen;
		
	public PjCreationController(Game game, BaseController parentController) {
		super(game, parentController);
		
		this.character = new Character();
		this.isCharacterCreated = false;
		
		this.listLocalizables = new ArrayList<ILocalizable>();
		this.selectGenderScreen = createSelectGenderScreen();
    	this.selectPortraitScreen = createSelectPortraitScreen();
		this.selectRaceScreen = createSelectRaceScreen();
		this.selectAttributesScreen = createSelectAttributesScreen();
		this.selectSkillsScreen = createSelectSkillsScreen();
		this.selectSpellsScreen = createSelectSpellsScreen();
		this.selectNameScreen = createSelectNameScreen();
		fillListLocalizablesAndDisposables();
	}
	
	private void fillListLocalizablesAndDisposables()
	{
		listLocalizables.add(selectGenderScreen);
		listLocalizables.add(selectPortraitScreen);
		listLocalizables.add(selectRaceScreen);
		listLocalizables.add(selectAttributesScreen);
		listLocalizables.add(selectSkillsScreen);
		listLocalizables.add(selectSpellsScreen);
		listLocalizables.add(selectNameScreen);

		disposables.add(selectGenderScreen);
		disposables.add(selectPortraitScreen);
		disposables.add(selectRaceScreen);
		disposables.add(selectAttributesScreen);
		disposables.add(selectSkillsScreen);
		disposables.add(selectSpellsScreen);
		disposables.add(selectNameScreen);
	}

	public Character getCharacter(){
		return this.character;
	}
	
	public Boolean isCharacterCreated(){
		return this.isCharacterCreated;
	}

	private SelectGenderScreen createSelectGenderScreen()
	{
		SelectGenderScreen selectGenderScreen = new SelectGenderScreen();
    	selectGenderScreen.addListener(new CloseScreenListener() {
			@Override
			public void cancel(Object sender) {
				cancelSelectGender();				
			}			

			@Override
			public void accept(Object sender) {
				acceptSelectGender();				
			}			
		});  
    	return selectGenderScreen;
	}
	
	private SelectPortraitScreen createSelectPortraitScreen()
	{
		SelectPortraitScreen selectPortraitScreen = new SelectPortraitScreen();
    	selectPortraitScreen.addListener(new CloseScreenListener() {
			@Override
			public void cancel(Object sender) {
				cancelSelectPortrait();		
			}		

			@Override
			public void accept(Object sender) {
				acceptSelectPortrait();				
			}			
		}); 
    	return selectPortraitScreen;
	}
	
	private SelectRaceScreen createSelectRaceScreen(){
    	
		SelectRaceScreen selectRaceScreen = new SelectRaceScreen();
    	selectRaceScreen.addListener(new CloseScreenListener() {
			
			@Override
			public void cancel(Object sender) {
				cancelSelectRace();				
			}			

			@Override
			public void accept(Object sender) {
				acceptSelectRace();				
			}
			
		});
    	
    	return selectRaceScreen;
	}
	
	private SelectAttributesScreen createSelectAttributesScreen()
	{
		SelectAttributesScreen selectAttributesScreen = new SelectAttributesScreen();
    	selectAttributesScreen.addListener(new CloseScreenListener() {
			
			@Override
			public void cancel(Object sender) {
				cancelSelectAttributes();				
			}			

			@Override
			public void accept(Object sender) {
				acceptSelectAttributes();				
			}		
			
		});
    	return selectAttributesScreen;
	}
	
	private SelectSkillsScreen createSelectSkillsScreen()
	{
		SelectSkillsScreen selectSkillsScreen = new SelectSkillsScreen();
		selectSkillsScreen.addListener(new CloseScreenListener() {
			
			@Override
			public void cancel(Object sender) {
				cancelSelectSkills();				
			}			

			@Override
			public void accept(Object sender) {
				acceptSelectSkills();				
			}		
			
		});
    	return selectSkillsScreen;
	}
	
	private SelectSpellScreen createSelectSpellsScreen()
	{
		SelectSpellScreen screen = new SelectSpellScreen();
		screen.addListener(new CloseScreenListener() {
			
			@Override
			public void cancel(Object sender) {
				cancelSelectSpells();				
			}			

			@Override
			public void accept(Object sender) {
				acceptSelectSpells();				
			}		
			
		});
    	return screen;
	}	

	private SelectNameScreen createSelectNameScreen() {
		SelectNameScreen selectNameScreen = new SelectNameScreen();
		selectNameScreen.addListener(new CloseScreenListener() {
			
			@Override
			public void cancel(Object sender) {
				cancelSelectName();				
			}			

			@Override
			public void accept(Object sender) {
				acceptSelectName();				
			}				
			
		});
    	return selectNameScreen;
	}
	
	@Override
	public void start() {
		character = new Character();
		isCharacterCreated = false;

		selectGenderScreen.setModel(Gender.NotSet);
		transitionBetweenScreens(game.getScreen(), selectGenderScreen, null);

		AudioManager.getInstance().setCurrentMusic(AssetRepository.getInstance().getMusic("create_pj"));
		AudioManager.getInstance().playMusic();
	}

	@Override
	protected void childEnded(BaseController childController) {
		// There are no other child controllers 		
	}
	
	@Override
	public void onBackPressed() {
		 Screen currentScreen = game.getScreen();
		
		 if(currentScreen.equals(selectGenderScreen)) {
			 cancelSelectGender();
		 } else if(currentScreen.equals(selectPortraitScreen)) {
			 cancelSelectPortrait();
		 }  else if(currentScreen.equals(selectRaceScreen)) {
			 cancelSelectRace();
		 }  else if(currentScreen.equals(selectAttributesScreen)) {
			 cancelSelectAttributes();
		 }  else if(currentScreen.equals(selectSkillsScreen)) {
			 cancelSelectSkills();
		 }  else if(currentScreen.equals(selectSpellsScreen)) {
			 cancelSelectSpells();
		 }  else if(currentScreen.equals(selectNameScreen)) {
			 cancelSelectName();
		 }  
	}

	private void cancelSelectGender() {
		this.isCharacterCreated = false;
		end();
	}	
	
	private void acceptSelectGender() {
		this.character.setGender(selectGenderScreen.getModel());		
		
		selectPortraitScreen.setPortraitGenre(this.character.getGender() == Gender.Male, true);
		selectPortraitScreen.setSelectedPortraitIndex(-1);
		transitionBetweenScreens(game.getScreen(), selectPortraitScreen, null);
	}
	
	private void cancelSelectPortrait() {
		selectGenderScreen.setModel(this.character.getGender());
		transitionBetweenScreens(game.getScreen(), selectGenderScreen, null);
	}

	private void acceptSelectPortrait() {
		this.character.setPortraitImageName(selectPortraitScreen.getModel());
		
		selectRaceScreen.setModel(null);
		transitionBetweenScreens(game.getScreen(), selectRaceScreen, null);
	}
	
	private void cancelSelectRace() {
		selectPortraitScreen.setModel(this.character.getPortraitImageName());
				
		transitionBetweenScreens(game.getScreen(), selectPortraitScreen, null);
	}

	private void acceptSelectRace() {
		this.character.setRace(selectRaceScreen.getModel());
		
		this.character.resetAttributesValues();
		selectAttributesScreen.setModel(this.character);
		selectAttributesScreen.setTotalAttributesPoints(Attribute.TotalAttributePointsPerPj);
		transitionBetweenScreens(game.getScreen(), selectAttributesScreen, null);
	}
	
	private void cancelSelectAttributes() {
		selectRaceScreen.setModel(this.character.getRace());
				
		transitionBetweenScreens(game.getScreen(), selectRaceScreen, null);
	}

	private void acceptSelectAttributes() {
		character = selectAttributesScreen.getModel();
		character.setCurrentLifePoints(character.getMaximumLifePoints());
		character.setCurrentPowerPoints(character.getMaximumPowerPoints());
		character.setCurrentLuckPoints(character.getMaximumLuckPoints());

		character.resetSkillValues();
		selectSkillsScreen.setModel(this.character);
		selectSkillsScreen.setTotalSkillsPoints(Skill.TotalSkillPointsPerPj);
		transitionBetweenScreens(game.getScreen(), selectSkillsScreen, null);
	}
	
	private void cancelSelectSkills() {
		selectAttributesScreen.setModel(this.character);
		
		transitionBetweenScreens(game.getScreen(), selectAttributesScreen, null);
	}

	private void acceptSelectSkills() {
		this.character = selectSkillsScreen.getModel();
		
		this.character.resetSpells();
		selectSpellsScreen.setModel(this.character);
		selectSpellsScreen.resetScrollPane();
		transitionBetweenScreens(game.getScreen(), selectSpellsScreen, null);
	}

	protected void cancelSelectSpells() {
		selectSkillsScreen.setModel(this.character);
		
		transitionBetweenScreens(game.getScreen(), selectSkillsScreen, null);
	}
	
	protected void acceptSelectSpells() {
		this.character = selectSpellsScreen.getModel();

		this.character.setName("");
		selectNameScreen.setModel(this.character);
		transitionBetweenScreens(game.getScreen(), selectNameScreen, null);
	}

	private void cancelSelectName() {
		selectSpellsScreen.setModel(this.character);
		
		transitionBetweenScreens(game.getScreen(), selectSpellsScreen, null);
	}
	
	private void acceptSelectName() {
		this.character = selectNameScreen.getModel();

		if(this.character.getName() == null || this.character.getName().trim().length() == 0) {
			selectNameScreen.promptForCharacterName();
		} else {
			this.isCharacterCreated = true;
			end();
		}
	}

	@Override
	public void refreshLocalizableItems() {
		for(ILocalizable localizable: listLocalizables)
		{
			localizable.refreshLocalizableItems();
		}		
	}
}
