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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Disposable;
import com.pberna.adventure.AdventureBetaHelper;
import com.pberna.adventure.AdventureScoreManager;
import com.pberna.adventure.GlobalStoreManager;
import com.pberna.adventure.TestingHelper;
import com.pberna.adventure.adventure.Adventure;
import com.pberna.adventure.dependencies.Settings;
import com.pberna.adventure.games.StoredGame;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.store.StoreItem;

public class MainController extends BaseController {
	
	private PjCreationController pjCreationController;
	private InGameController inGameController;
	private OutGameController outGameController;
	private BaseController activeController;
	
	private Character activeCharacter;
	private Adventure activeAdventure;
	private MainControllerEvents listener;

	public MainController(final Game game, BaseController parentController)
    {
		super(game, parentController); 	
				
		pjCreationController = new PjCreationController(this.game, this);
		disposables.add(pjCreationController);

		inGameController = new InGameController(this.game, this);
		disposables.add(inGameController);
		inGameController.addListener(new InGameControllerEvents() {
			@Override
			public void loadGame(StoredGame storedGame) {
				loadStoredGame(storedGame);
			}

			@Override
			public void showInterstitialAdvertising(ITransitionCallback callback) {
				showInterstitialAdvertisingInGame(callback);
			}
		});

		outGameController = new OutGameController(this.game, this);
		disposables.add(outGameController);
		outGameController.addListener(new OutGameControllerEvents() {
			@Override
			public void newGame() {
				startNewGame();
			}

			@Override
			public void loadGame(StoredGame storedGame) {
				loadStoredGame(storedGame);
			}

			@Override
			public void showGplayAchievementsScreen() {
				if(listener != null) {
					listener.showGplayAchievementsScreen();
				}
			}

			@Override
			public void showInterstitialAdvertising(ITransitionCallback callback) {
				showInterstitialAdvertisingInGame(callback);
			}

			@Override
			public void showGplayLeaderboardScreen() {
				if(listener != null) {
					listener.showGplayLeaderboardScreen();
				}
			}

		});
		activeController = null;
		
		activeCharacter = null;
		GlobalStoreManager.getInstance().setCharacter(activeCharacter);
    }

	private void startNewGame() {
		AdventureScoreManager.getInstance().resetScore();
		setActiveController(pjCreationController);
		activeController.start();
		//TEST Avoid PJ creation screen
		/*activeCharacter = TestingHelper.createTestingCharacter();
		GlobalStoreManager.getInstance().setCharacter(activeCharacter);
		inGameController.setCharacter(activeCharacter);
		activeAdventure = AdventureBetaHelper.createTestingAdventure();
		inGameController.setAdventure(activeAdventure);

		setActiveController(inGameController);
		activeController.start();*/
	}

	private void loadStoredGame(StoredGame storedGame) {
		activeCharacter = Character.getCharacterFromJson(storedGame.getCharacterJson());
		activeCharacter.refreshLocalizableItems();
		GlobalStoreManager.getInstance().setCharacter(activeCharacter);

		inGameController.setCharacter(activeCharacter);
		activeAdventure = AdventureBetaHelper.createTestingAdventure();
		inGameController.setAdventure(activeAdventure);

		AdventureScoreManager.getInstance().resetScore(storedGame.getScore());

		setActiveController(inGameController);
		inGameController.showPlace(storedGame.getPlaceId(),	storedGame.getPendingInventoryItemsCount(),
				new ITransitionCallback() {
					@Override
					public void callback() {
						showInterstitialAdvertisingInGame(null);
					}
				});
	}

	@Override
	public void start() {
		refreshLocalizableItems();
		setActiveController(outGameController);
		outGameController.start(false, false, false);
	}
	
	private void setActiveController(BaseController controller) {
		activeController = controller;
	}

	@Override
	protected void childEnded(BaseController childController) {
		if(childController == pjCreationController){
			//Ended of cancelled Pj Creation
			if(pjCreationController.isCharacterCreated()){
				finishedPjCreation(pjCreationController.getCharacter());
			}
			else {
				cancelledPjCreation();
			}
		} else if (childController == inGameController) {
			finishedInGamePlay();
		} else if (childController == outGameController) {
			end();
		}
	}

	@Override
	public void onBackPressed() {
		if(activeController != null) {
			activeController.onBackPressed();
		}
	}	

	private void finishedPjCreation(Character character) {
		activeCharacter = character;
		GlobalStoreManager.getInstance().setCharacter(activeCharacter);
		inGameController.setCharacter(activeCharacter);

		activeAdventure = AdventureBetaHelper.createTestingAdventure();
		inGameController.setAdventure(activeAdventure);
		
		setActiveController(inGameController);
		activeController.start();		
	}
	
	private void cancelledPjCreation() {
		setActiveController(outGameController);
		outGameController.start();
	}

	private void finishedInGamePlay() {
		boolean showScoreNotification = false;
		if(inGameController.isEndAdventure() && Settings.getInstance().isLeadeboardAvailable()) {
			showScoreNotification = true;
			AdventureScoreManager.getInstance().submitScore();
		}
		setActiveController(outGameController);
		outGameController.start(true, true, showScoreNotification);
	}

	@Override
	public void refreshLocalizableItems() {		
		if(activeCharacter != null) {
			activeCharacter.refreshLocalizableItems();
			GlobalStoreManager.getInstance().setCharacter(activeCharacter);
		}
		pjCreationController.refreshLocalizableItems();
		inGameController.refreshLocalizableItems();
		outGameController.refreshLocalizableItems();
	}

	public void setListener(MainControllerEvents listener) {
		this.listener = listener;
	}

	private void showInterstitialAdvertisingInGame(ITransitionCallback callback) {
		if(listener != null) {
			listener.showInterstitialAdvertising(callback);
		}
	}
}
