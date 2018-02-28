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

package com.pberna.adventure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.pberna.adventure.controllers.ITransitionCallback;
import com.pberna.adventure.controllers.MainController;
import com.pberna.adventure.controllers.MainControllerEvents;
import com.pberna.adventure.dependencies.DependenciesContainer;
import com.pberna.adventure.dependencies.Settings;
import com.pberna.adventure.store.PurchaseManager;
import com.pberna.adventure.store.PurchaseManagerEventsListener;
import com.pberna.engine.achievements.achievement.AchievementManager;
import com.pberna.engine.achievements.achievementAction.AchievementActionManager;
import com.pberna.engine.achievements.playerAction.PlayerActionManager;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.persistence.Database;
import com.pberna.engine.screens.load.LoadScreenInitGame;
import com.pberna.engine.screens.load.LoadScreenListener;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.graphics.PixmapHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AdventureGame extends Game {
	
	private MainController mainController;
	private LoadScreenInitGame loadScreenInitGame;
	private AdventureGameEventsListener listener;

	private static Class<?> threadClass;

	public AdventureGame(DependenciesContainer dependenciesContainer) {
		Database.setInstance(dependenciesContainer.getDatabase());
		Settings.setInstance(dependenciesContainer.getSettings());
		listener = null;
	}
	
	public void create () {
		if(Database.getInstance() != null) {
			Database.getInstance().onCreate();
			Database.getInstance().onUpgrade();
		}

		disposeSingletons();

		AudioManager.getInstance().loadSettings();
		Localization.getInstance().loadSettings();

		PurchaseManager.getInstance().addListener(new PurchaseManagerEventsListener() {
			@Override
			public void removeAdvertisingPurchased() {
				if(listener != null) {
					listener.removeAdvertisingPurchased();
				}
			}
		});

		loadScreenInitGame = new LoadScreenInitGame();
		loadScreenInitGame.addListener(new LoadScreenListener() {			
			@Override
			public void loadFinished() {
				assetLoadFinished();
			}
		});	
		
		Localization.getInstance().readLocalizationDefinitionFromXml("data/i18N.xml");
		Localization.getInstance().setLocale(Localization.getInstance().getLocale());
		Localization.getInstance().loadAllBundles();
    	
		AssetRepository.getInstance().readAssetsDefinitionFromXml("data/assets.xml");
	    
		setScreen(loadScreenInitGame);
	}

	private void disposeSingletons() {
		disposeIfNotNull(AssetRepository.getInstance());
		disposeIfNotNull(Localization.getInstance());
		disposeIfNotNull(PixmapHelper.getInstance());
		disposeIfNotNull(GlobalStoreManager.getInstance());
		disposeIfNotNull(AchievementManager.getInstance());
		disposeIfNotNull(AchievementActionManager.getInstance());
		disposeIfNotNull(ContinuosRenderingManager.getInstance());
		disposeIfNotNull(AdventureScoreManager.getInstance());
		disposeIfNotNull(PlayerActionManager.getInstance());
		disposeTimer();
	}

	private void disposeTimer() {
		try {
			Field threadField = Timer.class.getDeclaredField("thread");
			threadField.setAccessible(true);
			Object oldThread = threadField.get(null);
			if (oldThread != null) {
				threadClass = oldThread.getClass();
			}

			if(threadClass != null) {
				Constructor<?> threadConstructor = threadClass.getConstructor((Class<?>[]) null);
				threadConstructor.setAccessible(true);
				Object newThread = threadConstructor.newInstance((Object[]) null);
				threadField.set(null, newThread);
				Method disposeMethod = threadClass.getDeclaredMethod("dispose",
						(Class<?>[]) null);
				disposeMethod.setAccessible(true);
				disposeMethod.invoke(newThread, (Object[]) null);
			}
		} catch (NoSuchFieldException ignored) {
		} catch (IllegalAccessException ignored) {
		} catch (IllegalArgumentException ignored) {
		} catch (NoSuchMethodException ignored) {
		} catch (InstantiationException ignored) {
		} catch (InvocationTargetException ignored) {
		}

		Timer.instance().clear();
		Timer.instance().start();
	}

	private void assetLoadFinished() {
		AdventureScoreManager.getInstance().setListener(new AdventureScoreManagerEvents() {
			@Override
			public void submitScore(int score) {
				if(listener != null) {
					listener.submitScoreGplay(score);
				}
			}
		});

		mainController = new MainController(this, null);
		mainController.setListener(new MainControllerEvents() {
			@Override
			public void showGplayAchievementsScreen() {
				if (listener != null) {
					listener.showGplayAchievementsScreen();
				}
			}

			@Override
			public void showInterstitialAdvertising(ITransitionCallback callback) {
				if (listener != null) {
					listener.showInterstitialAdvertising(callback);
				} else {
					if(callback != null) {
						callback.callback();
					}
				}
			}

			@Override
			public void showGplayLeaderboardScreen() {
				if (listener != null) {
					listener.showLeaderboardGplay();
				}
			}
		});
		mainController.start();

		if(listener != null) {
			listener.gameInitialized();
		}
	}

	@Override
	public void render () {
		super.render(); //important!
	}
	
	public void onBackPressed() {
		if(mainController != null) {
			mainController.onBackPressed();
		}
	}

	@Override
	public void dispose() {
		super.dispose();

		disposeIfNotNull(loadScreenInitGame);
		disposeIfNotNull(mainController);
		disposeIfNotNull(AssetRepository.getInstance());
		disposeIfNotNull(Localization.getInstance());
		disposeIfNotNull(PixmapHelper.getInstance());
		disposeIfNotNull(Database.getInstance());
		disposeIfNotNull(GlobalStoreManager.getInstance());
		disposeIfNotNull(PurchaseManager.getInstance());
		disposeIfNotNull(AchievementManager.getInstance());
		disposeIfNotNull(AchievementActionManager.getInstance());
		disposeIfNotNull(ContinuosRenderingManager.getInstance());
		disposeIfNotNull(AdventureScoreManager.getInstance());
		disposeIfNotNull(PlayerActionManager.getInstance());
    }

	private void disposeIfNotNull(Disposable disposable) {
		if(disposable != null) {
			disposable.dispose();
		}
	}

	public void setListener(AdventureGameEventsListener listener) {
		this.listener = listener;
	}
}
