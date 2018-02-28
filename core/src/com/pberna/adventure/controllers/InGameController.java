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
import java.util.Locale;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.pberna.adventure.AchievementsHelper;
import com.pberna.adventure.AdventureScoreManager;
import com.pberna.adventure.adventure.Adventure;
import com.pberna.adventure.adventure.AdventureManager;
import com.pberna.adventure.combat.Combat;
import com.pberna.adventure.combat.CombatWinner;
import com.pberna.adventure.games.StoredGame;
import com.pberna.adventure.games.StoredGameManager;
import com.pberna.adventure.items.Item;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.pj.effects.Effect;
import com.pberna.adventure.places.CombatPlace;
import com.pberna.adventure.places.EffectPlace;
import com.pberna.adventure.places.EndAdventurePlace;
import com.pberna.adventure.places.ItemUsePlace;
import com.pberna.adventure.places.ItemUsePlaceToGo;
import com.pberna.adventure.places.Place;
import com.pberna.adventure.places.SkillCheckPlace;
import com.pberna.adventure.places.SpellUsePlaceToGo;
import com.pberna.adventure.screens.outgame.LoadGameScreen;
import com.pberna.adventure.screens.outgame.LoadGameScreenEventsListener;
import com.pberna.adventure.spells.AttackSpell;
import com.pberna.adventure.spells.Spell;
import com.pberna.engine.achievements.achievement.Achievement;
import com.pberna.engine.achievements.achievement.AchievementEventListener;
import com.pberna.engine.achievements.achievement.AchievementManager;
import com.pberna.engine.achievements.playerAction.IPlayerActionManager;
import com.pberna.engine.achievements.playerAction.PlayerActionManager;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.adventure.screens.ingame.CharacterSheetScreen;
import com.pberna.adventure.screens.ExitListener;
import com.pberna.adventure.screens.ingame.InventoryScreen;
import com.pberna.adventure.screens.ingame.InventoryScreen.InventoryScreenMode;
import com.pberna.adventure.screens.ingame.InGameEventsListener;
import com.pberna.adventure.screens.ingame.MainInGameScreen;
import com.pberna.adventure.screens.ingame.SpellsScreen;
import com.pberna.adventure.screens.ingame.SpellsScreen.SpellScreenMode;
import com.pberna.adventure.screens.ingame.SpellsScreenListener;
import com.pberna.adventure.screens.ingame.StartAdventureScreen;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.logging.Logger;

public class InGameController extends BaseController {

	private ArrayList<ILocalizable> listLocalizables;
	private MainInGameScreen mainInGameScreen;
	private InventoryScreen inventoryScreen;
	private SpellsScreen spellsScreen;
	private CharacterSheetScreen characterSheetScreen;
	private StartAdventureScreen startAdventureScreen;
	private LoadGameScreen loadGameScreen;
	
	private Character character;
	private Adventure adventure;
	private Place activePlace;
	private ArrayList<InGameControllerEvents> listeners;
	private boolean isEndAdventure;

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}
	
	public Adventure getAdventure() {
		return adventure;
	}

	public void setAdventure(Adventure adventure) {
		this.adventure = adventure;
	}

	public InGameController(final Game game, BaseController parentController) {
		super(game, parentController);
		listLocalizables = new ArrayList<ILocalizable>();
		listeners = new ArrayList<InGameControllerEvents>();
		
		mainInGameScreen = new MainInGameScreen();
		disposables.add(mainInGameScreen);
		mainInGameScreen.addListener(new InGameEventsListener() {			
			@Override
			public void quitGame() {
				isEndAdventure = false;
				end();
			}

			@Override
			public void openInventory() {
				inventoryScreen.setScreenMode(InventoryScreenMode.Adventure);
				inventoryScreen.setModel(character);
				transitionBetweenScreens(game.getScreen(), inventoryScreen, null);
			}

			@Override
			public void openSpells() {
				spellsScreen.setScreenMode(SpellScreenMode.Adventure);
				spellsScreen.setCharacter(character);
				transitionBetweenScreens(game.getScreen(), spellsScreen, null);
			}

			@Override
			public void openCharacterSheet() {
				characterSheetScreen.setCharacter(character);
				transitionBetweenScreens(game.getScreen(), characterSheetScreen, null);
			}
			
			@Override
			public void moveToOtherPlace(int idPlace) {
				Place newPlace = AdventureManager.getInstance().getPlace(adventure.getId(), idPlace);
				transitionBetweenPlaces(activePlace, newPlace, null, null);
				mainInGameScreen.setActivePlace(activePlace);
				mainInGameScreen.setConfirmExit(true);
				game.setScreen(mainInGameScreen);
			}

			@Override
			public void endAdventure() {
				isEndAdventure = true;
				end();
			}

			@Override
			public void useItemInCombat() {
				inventoryScreen.setScreenMode(InventoryScreenMode.Combat);
				inventoryScreen.setModel(character);
				transitionBetweenScreens(game.getScreen(), inventoryScreen, null);
			}

			@Override
			public void openSpellScreenInCombat() {
				spellsScreen.setScreenMode(SpellScreenMode.Combat);
				spellsScreen.setCharacter(character);
				transitionBetweenScreens(game.getScreen(), spellsScreen, null);
			}

			@Override
			public void saveGame() {
				try
				{
					StoredGameManager.getInstance().createStoredGame(character, adventure, activePlace.getId(),
							mainInGameScreen.getPendingItemsCount(), AdventureScoreManager.getInstance().getScore());
					mainInGameScreen.showOkNotification(Localization.getInstance().getTranslation("InGame", "gameSavedOkMessage"));
					mainInGameScreen.setConfirmExit(false);

					for(InGameControllerEvents listener: listeners) {
						listener.showInterstitialAdvertising(null);
					}

				} catch(Exception ex)
				{
					mainInGameScreen.showErrorNotification(Localization.getInstance().getTranslation("InGame", "gameSavedErrorMessage"));
				}
			}

			@Override
			public void selectLanguage(Locale locale) {
				notifyRefreshLocalizableItems();
			}

			@Override
			public void loadGame() {
				loadGameScreen.showLoadGames(StoredGameManager.getInstance().getAllStoredGames());
				transitionBetweenScreens(game.getScreen(), loadGameScreen, null);
			}

			@Override
			public void moveToOtherPlace(SpellUsePlaceToGo placeToGo) {
				Place newPlace = AdventureManager.getInstance().getPlace(adventure.getId(), placeToGo.getIdPlaceToGo());
				transitionBetweenPlaces(activePlace, newPlace, placeToGo.getSpell());
				mainInGameScreen.setActivePlace(activePlace);
				mainInGameScreen.setConfirmExit(true);
				game.setScreen(mainInGameScreen);
			}

			@Override
			public void moveToOtherPlace(ItemUsePlaceToGo placeToGo) {
				Place newPlace = AdventureManager.getInstance().getPlace(adventure.getId(), placeToGo.getIdPlaceToGo());
				transitionBetweenPlaces(activePlace, newPlace, placeToGo.getItem());
				mainInGameScreen.setActivePlace(activePlace);
				mainInGameScreen.setConfirmExit(true);
				game.setScreen(mainInGameScreen);
			}

			@Override
			public void combatEnded(Combat combat) {
				if(combat.getRound() == 1 && combat.getCombatWinner() == CombatWinner.Character) {
					PlayerActionManager.getInstance().registerAction(AchievementsHelper.WinQuick);
				}

				if(!combat.isCharacterUsedMeleeInCombat() && combat.getCombatWinner() == CombatWinner.Character) {
					PlayerActionManager.getInstance().registerAction(AchievementsHelper.WinNoMelee);
				}
			}

		});
		listLocalizables.add(mainInGameScreen);
		
		inventoryScreen = new InventoryScreen();
		disposables.add(inventoryScreen);
		inventoryScreen.addListener(new ExitListener() {			
			@Override
			public void exit(Object sender) {
				exitInventoryScreen();			
			}
		});
		listLocalizables.add(inventoryScreen);
		
		spellsScreen = new SpellsScreen();
		disposables.add(spellsScreen);
		spellsScreen.addListener(new SpellsScreenListener() {
			
			@Override
			public void exit(Object sender) {
				exitSpellsScreen();				
			}

			@Override
			public void castAttackSpell(final AttackSpell attackSpell) {
				mainInGameScreen.setCharacter(character);
				transitionBetweenScreens(game.getScreen(), mainInGameScreen, new ITransitionCallback() {
					@Override
					public void callback() {
						mainInGameScreen.castAttackSpell(attackSpell);
					}
				});
				for(Spell spell: spellsScreen.getCastedSpells()) {
					PlayerActionManager.getInstance().registerAction(AchievementsHelper.CastSpell1Prefix + String.valueOf(spell.getId()));
				}
			}
		});
		listLocalizables.add(spellsScreen);
		
		characterSheetScreen = new CharacterSheetScreen();
		disposables.add(characterSheetScreen);
		characterSheetScreen.addListener(new ExitListener() {			
			@Override
			public void exit(Object sender) {
				exitCharacterSheetScreen();				
			}
		});
		listLocalizables.add(characterSheetScreen);
		
		startAdventureScreen = new StartAdventureScreen();
		disposables.add(startAdventureScreen);
		startAdventureScreen.addListener(new ExitListener() {			
			@Override
			public void exit(Object sender) {
				Place newPlace = AdventureManager.getInstance().getStartPlace(adventure.getId());
				transitionBetweenPlaces(null, newPlace, null, null);
				mainInGameScreen.setCharacter(character);
				mainInGameScreen.setActivePlace(activePlace);
				mainInGameScreen.resetPendingItemsCount();
				transitionBetweenScreens(game.getScreen(), mainInGameScreen, null);
			}
		});
		listLocalizables.add(startAdventureScreen);

		loadGameScreen = new LoadGameScreen();
		disposables.add(loadGameScreen);
		loadGameScreen.setConfirmLoadGame(true);
		loadGameScreen.addListener(new LoadGameScreenEventsListener() {
			@Override
			public void exit() {
				transitionBetweenScreens(game.getScreen(), mainInGameScreen, null);
			}

			@Override
			public void loadGame(StoredGame storedGame) {
				for (InGameControllerEvents listener : listeners) {
					listener.loadGame(storedGame);
				}
			}

			@Override
			public void deleteGame(StoredGame storedGame) {
				StoredGameManager.getInstance().deleteStoredGame(storedGame);
				loadGameScreen.showLoadGames(StoredGameManager.getInstance().getAllStoredGames());
			}
		});
		listLocalizables.add(loadGameScreen);
		
		character = null;
		adventure = null;
		activePlace = null;
		AchievementManager.getInstance().addListener(new AchievementEventListener() {
			@Override
			public void achievementsUnlocked(ArrayList<Achievement> unlockedAchievements) {
				for(Achievement achievement: unlockedAchievements) {
					Logger.getInstance().addLogInfo("Achievement", "Achievement unlocked: " + achievement.getNameKey());
					mainInGameScreen.showAchievementNotification(achievement);
				}
				AudioManager.getInstance().playSound(AssetRepository.getInstance().getSound("new_achievement"));
			}
		});
	}

	private void notifyRefreshLocalizableItems() {
		parentController.refreshLocalizableItems();
	}

	private void exitInventoryScreen() {
		mainInGameScreen.resetPendingItemsCount();
		mainInGameScreen.setCharacter(character);
		transitionBetweenScreens(game.getScreen(), mainInGameScreen, new ITransitionCallback() {
			@Override
			public void callback() {
				if (inventoryScreen.getScreenMode() == InventoryScreenMode.Combat && inventoryScreen.isItemAlreadyUsed()) {
					mainInGameScreen.advanceCombatToNextTurn();
				}
			}
		});
	}

	private void exitSpellsScreen() {
		mainInGameScreen.setCharacter(character);
		transitionBetweenScreens(game.getScreen(), mainInGameScreen, new ITransitionCallback() {
			@Override
			public void callback() {
				if (spellsScreen.getScreenMode() == SpellScreenMode.Combat && spellsScreen.isSpellAlreadycasted()) {
					mainInGameScreen.advanceCombatToNextTurn();
				}
			}
		});
		for(Spell spell: spellsScreen.getCastedSpells()) {
			PlayerActionManager.getInstance().registerAction(AchievementsHelper.CastSpell1Prefix + String.valueOf(spell.getId()));
		}
	}

	private void exitCharacterSheetScreen() {
		mainInGameScreen.setCharacter(character);
		transitionBetweenScreens(game.getScreen(), mainInGameScreen, null);
	}

	private void transitionBetweenPlaces(Place placeFrom, Place placeTo, Item item) {
		transitionBetweenPlaces(placeFrom, placeTo, item, null);
	}

	private void transitionBetweenPlaces(Place placeFrom, Place placeTo, Spell spell) {
		transitionBetweenPlaces(placeFrom, placeTo, null, spell);
	}

	private void transitionBetweenPlaces(Place placeFrom, Place placeTo, Item item, Spell spell) {
		removeEffectsFromCharacter(placeFrom);
		removeSpentItemsFromCharacter(placeFrom, placeTo, item);
		giveObjectsToCharacter(placeTo);

		if(placeTo instanceof EndAdventurePlace) {
			if(((EndAdventurePlace)placeTo).isPlayerDead()) {
				character.setCurrentLifePoints(0);
				character.setCurrentPowerPoints(0);
				character.setCurrentLuckPoints(0);
			}
		} else if(placeTo instanceof EffectPlace) {
			EffectPlace effectPlace = (EffectPlace) placeTo;
			applyEffectsToCharacter(effectPlace);
		}
		for (String action: placeTo.getPlayerActions()) {
			PlayerActionManager.getInstance().registerAction(action);
		}
		if(spell != null) {
			PlayerActionManager.getInstance().registerAction(AchievementsHelper.CastSpell1Prefix + String.valueOf(spell.getId()));
		}

		activePlace = placeTo;		
	}

	private void removeEffectsFromCharacter(Place placeFrom) {
		if(placeFrom != null) {
			if(placeFrom instanceof CombatPlace) {
				//remove Might, Shield and Magic Ritual spells
				character.removeActiveSpell(Spell.IdMight);
				character.removeActiveSpell(Spell.IdShield);
				character.removeActiveSpell(Spell.IdMagicRitual);
			}
			if(placeFrom instanceof SkillCheckPlace) {
				if(((SkillCheckPlace)placeFrom).getSkill().getId() == Skill.IdStealth) {
					//remove expertise spell
					character.removeActiveSpell(Spell.IdExpertise);
				}
			}
		}		
	}

	private void removeSpentItemsFromCharacter(Place placeFrom, Place placeTo, Item item) {
		if(item != null) {
			character.removeItem(item.getId());
			return;
		}

		if(placeFrom instanceof ItemUsePlace) {
			ItemUsePlace itemUsePlace = (ItemUsePlace) placeFrom;
			for(ItemUsePlaceToGo placeToGo: itemUsePlace.getPlacesToGo()) {
				if(placeToGo.getIdPlaceToGo() == placeTo.getId() && placeToGo.isItemIsSpent()) {
					character.removeItem(placeToGo.getItem().getId());
					break;
				}
			}
		}
	}
	
	private void giveObjectsToCharacter(Place placeTo) {
		if(placeTo != null) {
			for(Item item: placeTo.getItemsCharacterGets()) {
				character.getBackpack().addItem(item);
			}
			int pendingItemsCountIncrease = placeTo.getItemsCharacterGets().size();
			if(pendingItemsCountIncrease > 0) {
				mainInGameScreen.increasePendingItemsCount(pendingItemsCountIncrease);
			}
		}
	}

	private void applyEffectsToCharacter(EffectPlace effectPlace) {
		if(effectPlace != null) {
			for (Effect effect : effectPlace.getEffects()) {
				effect.applyTo(character);
			}
		}
	}

	@Override
	public void start() {
		startAdventureScreen.setModel(adventure);
		transitionBetweenScreens(game.getScreen(), startAdventureScreen, null);
		AudioManager.getInstance().setCurrentMusic(AssetRepository.getInstance().getMusic("in_game"));
		AudioManager.getInstance().playMusic();
	}

	@Override
	protected void childEnded(BaseController childController) {
		//there are no other child controllers
	}

	@Override
	public void onBackPressed() {
		Screen currentScreen = game.getScreen();
		
		 if(currentScreen.equals(startAdventureScreen)) {
			 return;
		 }
		 if (currentScreen.equals(mainInGameScreen)) {
			mainInGameScreen.onBackPressed();
		 } else if (currentScreen.equals(inventoryScreen)) {
			 exitInventoryScreen();
		 } else if (currentScreen.equals(spellsScreen)) {
			 exitSpellsScreen();
		 } else if (currentScreen.equals(characterSheetScreen)) {
			 exitCharacterSheetScreen();
		 }  else if(currentScreen.equals(loadGameScreen)) {
			 loadGameScreen.onBackPressed();
		 }
	}

	@Override
	public void refreshLocalizableItems() {
		for(ILocalizable localizable:listLocalizables) {
			localizable.refreshLocalizableItems();
		}
	}

	void showPlace(int placeId, int pendingItemsCount, ITransitionCallback callback) {
		AudioManager.getInstance().setCurrentMusic(AssetRepository.getInstance().getMusic("in_game"));
		AudioManager.getInstance().playMusic();
		activePlace = AdventureManager.getInstance().getPlace(adventure.getId(), placeId);
		mainInGameScreen.setCharacter(character);
		mainInGameScreen.resetPendingItemsCount();
		mainInGameScreen.increasePendingItemsCount(pendingItemsCount);
		mainInGameScreen.setActivePlace(activePlace);
		mainInGameScreen.setConfirmExit(false);
		transitionBetweenScreens(game.getScreen(), mainInGameScreen, callback);
	}

	public void addListener(InGameControllerEvents listener) {
		listeners.add(listener);
	}

	public boolean isEndAdventure() {
		return isEndAdventure;
	}
}
