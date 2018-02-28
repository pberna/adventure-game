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
 *
 *   Parts of this code are based on the code from TheInvader360:
 *   https://github.com/TheInvader360/libgdx-gameservices-tutorial/
 *
 *   Other parts are based on this example:
 *   https://bitbucket.org/just4phil/gdxpayexample/
 */

package com.pberna.adventure.android;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.pberna.adventure.AdventureGame;
import com.pberna.adventure.android.persistence.DatabaseAndroid;
import com.pberna.adventure.android.playservices.AchievementConstants;
import com.pberna.adventure.android.playservices.ActionResolver;
import com.pberna.adventure.android.playservices.GameHelper;
import com.pberna.adventure.android.store.GooglePlayResolver;
import com.pberna.adventure.controllers.ITransitionCallback;
import com.pberna.adventure.dependencies.DependenciesContainer;
import com.pberna.adventure.dependencies.StoreItemConfig;
import com.pberna.adventure.AdventureGameEventsListener;
import com.pberna.adventure.store.PurchaseManager;
import com.pberna.adventure.store.StoreItem;
import com.pberna.engine.achievements.achievement.Achievement;
import com.pberna.engine.achievements.achievement.AchievementEventListener;
import com.pberna.engine.achievements.achievement.AchievementManager;
import com.pberna.engine.achievements.playerAction.PlayerAction;
import com.pberna.engine.achievements.playerAction.PlayerActionEvents;
import com.pberna.engine.achievements.playerAction.PlayerActionManager;
import com.pberna.engine.localization.Localization;

import java.util.ArrayList;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import hotchemi.android.rate.StoreType;

public class AndroidLauncher extends AndroidApplication implements
		GameHelper.GameHelperListener, ActionResolver {

	private static final String AD_UNIT_ID_INTERSTITIAL = "InterstitialAdId";
	private static final String StoreIdRemoveAdvertising = "inapp.item.id";
	private static final String LeaderboardId = "YourLeaderboardId";
	private InterstitialAd interstitialAd;
	private AdventureGame adventureGame;
	private GooglePlayResolver googlePlayResolver;

	private static final String TAGACHIEVEMENT = "ACHIEVE";

	private GameHelper gameHelper;
	private ITransitionCallback callbackAfterAdvertising = null;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		createGameAndResolver();

		if(isPremiumVersionGame()) {
			initialize(adventureGame, config);
		} else {
			initializeWithAds(config);
		}

		try {
			if (gameHelper == null) {
				gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
				gameHelper.setConnectOnStart(false);
				gameHelper.enableDebugLog(true);
			}
			gameHelper.setup(this);
		} catch (Exception ex) {
			Log.e(TAGACHIEVEMENT, ex.getMessage());
		}

		try {
			PurchaseManager.getInstance().setPlatformResolver(googlePlayResolver);
			PurchaseManager.getInstance().getPlatformResolver().installIAP();
		} catch (Exception ex) {
			Log.e("PAY", "Error installing IAP " + ex.getMessage());
		}
	}

	private void createGameAndResolver() {
		DependenciesContainer depContainer = buildDependenciesContainer();
		adventureGame = new AdventureGame(depContainer);
		googlePlayResolver = new GooglePlayResolver(adventureGame, getBaseContext(), this);
		depContainer.getSettings().setStoreQuerier(googlePlayResolver);
		adventureGame.setListener(new AdventureGameEventsListener() {
			@Override
			public void removeAdvertisingPurchased() {

			}

			@Override
			public void gameInitialized() {
				subscribeToAchievementEvents();
				loginGPGS();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						checkAppRateMessage();
					}
				});
			}

			@Override
			public void showGplayAchievementsScreen() {
				getAchievementsGPGS();
			}

			@Override
			public void showInterstitialAdvertising(ITransitionCallback callback) {
				showOrLoadInterstital(callback);
			}

			@Override
			public void submitScoreGplay(int score) {
				submitScoreGPGS(score);
			}

			@Override
			public void showLeaderboardGplay() {
				getLeaderboardGPGS();
			}

		});
	}

	private DependenciesContainer buildDependenciesContainer() {
		DependenciesContainer container = new DependenciesContainer();
		container.setDatabase(new DatabaseAndroid(this.getBaseContext()));
		container.getSettings().setStoreAvailable(true);
		container.getSettings().getStoreItemConfigs().clear();
		container.getSettings().getStoreItemConfigs().add(new StoreItemConfig(
				StoreItem.RemoveAdvertisingId, StoreIdRemoveAdvertising));
		container.getSettings().setGplayAchievementsAvailable(true);
		container.getSettings().setLeadeboardAvailable(true);

		PackageInfo pinfo = null;
		try {
			pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			container.getSettings().setVersionName(pinfo.versionName);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return container;
	}

	private boolean isPremiumVersionGame() {
		try {
			PurchaseManager.getInstance().setUniqueId(Settings.Secure.ANDROID_ID);
			return PurchaseManager.getInstance().isItemPurchased(StoreIdRemoveAdvertising);
		} catch (Exception ex) {
			Log.e("PAY", "Error checking premium: " + ex.getMessage());
			return false;
		}
	}

	private void initializeWithAds(AndroidApplicationConfiguration config) {
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {

			}

			@Override
			public void onAdClosed() {
				if(callbackAfterAdvertising != null) {
					ITransitionCallback callback = callbackAfterAdvertising;
					callbackAfterAdvertising = null;
					callback.callback();
				}
				loadInterstitialAdvertising();
			}
		});
		initialize(adventureGame, config);
		loadInterstitialAdvertising();
	}

	private void loadInterstitialAdvertising() {
		AdRequest interstitialRequest = new AdRequest.Builder().build();
		interstitialAd.loadAd(interstitialRequest);
	}

	private void checkAppRateMessage() {
		AppRate.with(this)
				.setStoreType(StoreType.GOOGLEPLAY) //default is Google, other option is Amazon
				.setInstallDays(3) // default 10, 0 means install day.
				.setLaunchTimes(5) // default 10 times.
				.setRemindInterval(2) // default 1 day.
				.setShowLaterButton(true) // default true.
				//.setDebug(true) // default false.
				.setCancelable(false) // default false.
				.setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
					@Override
					public void onClickButton(int which) {
						//Log.d(AndroidLauncher.class.getName(), Integer.toString(which));
					}
				})
				.setTitle(Localization.getInstance().getTranslation("App", "rate_dialog_title"))
				.setMessage(Localization.getInstance().getTranslation("App", "rate_dialog_message"))
				.setTextLater(Localization.getInstance().getTranslation("App", "rate_dialog_cancel"))
				.setTextNever(Localization.getInstance().getTranslation("App", "rate_dialog_no"))
				.setTextRateNow(Localization.getInstance().getTranslation("App", "rate_dialog_ok"))
				.monitor();

		AppRate.showRateDialogIfMeetsConditions(this);
	}

	@Override
	public void onStart(){
		super.onStart();
		if(gameHelper != null) {
			gameHelper.onStart(this);
		}
	}

	@Override
	public void onStop(){
		super.onStop();
		if(gameHelper != null) {
			gameHelper.onStop();
		}
	}

	@Override
	public void onBackPressed() {
		 adventureGame.onBackPressed();
	 }

	private void subscribeToAchievementEvents() {
		try {
			AchievementManager.getInstance().addListener(new AchievementEventListener() {
				@Override
				public void achievementsUnlocked(ArrayList<Achievement> unlockedAchievements) {
					if (getSignedInGPGS()) {
						for (Achievement achievement : unlockedAchievements) {
							if (!achievement.isIncremental()) {
								unlockAchievementGPGS(AchievementConstants.getGooglePlayAchievementId(achievement.getId()));
							}
						}
					}
				}
			});
			PlayerActionManager.getInstance().addListener(new PlayerActionEvents() {
				@Override
				public void newPlayerActionRegistered(PlayerAction playerAction) {
					if (getSignedInGPGS()) {
						for(Achievement achievement: AchievementManager.getInstance().
								getAchievementsShouldBeIncrementedWhenRegisteringActions(playerAction.getActionId())) {
							incrementAchievementProgress(AchievementConstants.getGooglePlayAchievementId(achievement.getId()), 1);
						}
					}
				}

				@Override
				public void playerActionDone(PlayerAction playerAction) {

				}
			});
		} catch (Exception ex) {
			Log.e("ACHIEV", ex.getMessage());
		}
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		if(gameHelper != null) {
			gameHelper.onActivityResult(request, response, data);
		}
	}


	@Override
	public boolean getSignedInGPGS() {
		if(gameHelper != null) {
			return gameHelper.isSignedIn();
		}
		return false;
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if(gameHelper != null && !getSignedInGPGS()) {
						gameHelper.beginUserInitiatedSignIn();
					}
				}
			});
		} catch (Exception ex) {
			Log.e("MainActivity", "Log in failed: " + ex.getMessage() + ".");
		}
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		if(gameHelper != null && getSignedInGPGS()) {
			Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
		}
	}

	@Override
	public void getAchievementsGPGS() {
		if (getSignedInGPGS()) {
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		}
		else if (gameHelper != null && !gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	@Override
	public void incrementAchievementProgress(String achievementId, int increment) {
		if (gameHelper != null && getSignedInGPGS()) {
			Games.Achievements.increment(gameHelper.getApiClient(), achievementId, increment);
		}
	}

	@Override
	public void onSignInFailed() {

	}

	@Override
	public void onSignInSucceeded() {

	}

	@Override
	public void showOrLoadInterstital(ITransitionCallback callback) {
		if(isPremiumVersionGame()) {
			if(callback != null) {
				callback.callback();
			}
			return;
		}
		final ITransitionCallback tempCallback = callback;

		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (interstitialAd.isLoaded()) {
						callbackAfterAdvertising = tempCallback;
						interstitialAd.show();
					} else {
						if (tempCallback != null) {
							tempCallback.callback();
						}
						loadInterstitialAdvertising();
					}
				}
			});
		} catch (Exception e) {
			Log.e("MainActivity", "Error showing interstitial ad " + e.getMessage());
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		if (gameHelper != null && getSignedInGPGS()) {
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), LeaderboardId, score);
		}
	}

	@Override
	public void getLeaderboardGPGS() {
		if (getSignedInGPGS()) {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), LeaderboardId), 100);
		}
		else if (gameHelper != null && !gameHelper.isConnecting()) {
			loginGPGS();
		}
	}
}
