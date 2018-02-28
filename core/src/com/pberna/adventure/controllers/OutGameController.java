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
import com.pberna.adventure.AdventureScoreManager;
import com.pberna.adventure.games.StoredGame;
import com.pberna.adventure.games.StoredGameManager;
import com.pberna.adventure.screens.ExitListener;
import com.pberna.adventure.screens.outgame.AchievementsScreen;
import com.pberna.adventure.screens.outgame.CreditsScreen;
import com.pberna.adventure.screens.outgame.AchievementsScreenEventListener;
import com.pberna.adventure.screens.outgame.LoadGameScreen;
import com.pberna.adventure.screens.outgame.LoadGameScreenEventsListener;
import com.pberna.adventure.screens.outgame.StoreScreen;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.adventure.screens.outgame.MainMenuScreen;
import com.pberna.adventure.screens.outgame.MainMenuScreenEvents;

import java.util.ArrayList;
import java.util.Locale;

public class OutGameController extends BaseController {

    private ArrayList<OutGameControllerEvents> listeners;
    private ArrayList<ILocalizable> listLocalizables;

    private MainMenuScreen mainMenuScreen;
    private LoadGameScreen loadGameScreen;
    private CreditsScreen creditsScreen;
    private StoreScreen storeScreen;
    private AchievementsScreen achievementsScreen;

    public OutGameController(final Game game, BaseController parentController) {
        super(game, parentController);

        listeners = new ArrayList<OutGameControllerEvents>();
        listLocalizables = new ArrayList<ILocalizable>();

        mainMenuScreen = new MainMenuScreen();
        disposables.add(mainMenuScreen);
        mainMenuScreen.addListener(new MainMenuScreenEvents() {
            @Override
            public void newGame() {
                for (OutGameControllerEvents listener : listeners) {
                    listener.newGame();
                }
            }

            @Override
            public void loadGame() {
                transitionBetweenScreens(game.getScreen(), loadGameScreen, new ITransitionCallback() {
                    @Override
                    public void callback() {
                        loadGameScreen.showLoadGames(StoredGameManager.getInstance().getAllStoredGames());
                    }
                });
            }

            @Override
            public void goToStore() {
                transitionBetweenScreens(game.getScreen(), storeScreen, null);
            }

            @Override
            public void exitGame() {
                for (OutGameControllerEvents listener : listeners) {
                    listener.showInterstitialAdvertising(new ITransitionCallback() {
                        @Override
                        public void callback() {
                            end();
                        }
                    });
                }
                if(listeners.size() == 0) {
                    end();
                }
            }

            @Override
            public void watchCredits() {
                transitionBetweenScreens(game.getScreen(), creditsScreen, null);
            }

            @Override
            public void watchAchievements() {
                achievementsScreen.showAchievements();
                transitionBetweenScreens(game.getScreen(), achievementsScreen, null);
            }

            @Override
            public void watchLeaderboard() {
                for(OutGameControllerEvents listener: listeners) {
                    listener.showGplayLeaderboardScreen();
                }
            }

            @Override
            public void selectLanguage(Locale locale) {
                notifyrefreshLocalizableItems();
            }
        });
        listLocalizables.add(mainMenuScreen);

        //loadGameScreen
        loadGameScreen = new LoadGameScreen();
        disposables.add(loadGameScreen);
        loadGameScreen.setConfirmLoadGame(false);
        loadGameScreen.addListener(new LoadGameScreenEventsListener() {
            @Override
            public void exit() {
                transitionBetweenScreens(game.getScreen(), mainMenuScreen, null);
            }

            @Override
            public void loadGame(StoredGame storedGame) {
                for (OutGameControllerEvents listener : listeners) {
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

        //creditsScreen
        creditsScreen = new CreditsScreen();
        disposables.add(creditsScreen);
        creditsScreen.setListener(new ExitListener() {
            @Override
            public void exit(Object sender) {
                transitionBetweenScreens(game.getScreen(), mainMenuScreen, null);
            }
        });
        listLocalizables.add(creditsScreen);

        //storeScreen
        storeScreen = new StoreScreen();
        disposables.add(storeScreen);
        storeScreen.setListener(new ExitListener() {
            @Override
            public void exit(Object sender) {
                transitionBetweenScreens(game.getScreen(), mainMenuScreen, null);
            }
        });
        listLocalizables.add(storeScreen);

        //achievementsScreen
        achievementsScreen = new AchievementsScreen();
        disposables.add(achievementsScreen);
        achievementsScreen.addListener(new AchievementsScreenEventListener() {
            @Override
            public void showGplayAchievementsScreen() {
                for(OutGameControllerEvents listener: listeners) {
                    listener.showGplayAchievementsScreen();
                }
            }

            @Override
            public void exit(Object sender) {
                transitionBetweenScreens(game.getScreen(), mainMenuScreen, null);
            }
        });
        listLocalizables.add(achievementsScreen);
    }

    protected void notifyrefreshLocalizableItems() {
        parentController.refreshLocalizableItems();
    }

    @Override
    public void start() {
        start(true, false, false);
    }

    public void start(boolean showTransition, final boolean showAdvertising, final boolean showScoreNotification) {
        if(showTransition) {
            transitionBetweenScreens(game.getScreen(), mainMenuScreen, new ITransitionCallback() {
                @Override
                public void callback() {
                    if(showAdvertising) {
                        for (OutGameControllerEvents listener : listeners) {
                            listener.showInterstitialAdvertising(null);
                        }
                    }
                    if(showScoreNotification) {
                        mainMenuScreen.showScoreNotification(AdventureScoreManager.getInstance().getScore());
                    }
                }
            });
        } else {
            game.setScreen(mainMenuScreen);
            if(showScoreNotification) {
                mainMenuScreen.showScoreNotification(AdventureScoreManager.getInstance().getScore());
            }
        }
        AudioManager audioManager = AudioManager.getInstance();
        audioManager.loadSettings();
        audioManager.setCurrentMusic(AssetRepository.getInstance().getMusic("main_game"));
        audioManager.playMusic();
    }

    @Override
    protected void childEnded(BaseController childController) {
        //there are no other child controllers
    }

    @Override
    public void onBackPressed() {
        if(game.getScreen() == mainMenuScreen) {
            mainMenuScreen.onBackPressed();
        } else if (game.getScreen() == loadGameScreen) {
            loadGameScreen.onBackPressed();
        } else if (game.getScreen() == creditsScreen) {
            transitionBetweenScreens(game.getScreen(), mainMenuScreen, null);
        } else if (game.getScreen() == storeScreen) {
            storeScreen.onBackPressed();
        } else if (game.getScreen() == achievementsScreen) {
            transitionBetweenScreens(game.getScreen(), mainMenuScreen, null);
        }
    }

    @Override
    public void refreshLocalizableItems() {
        for(ILocalizable localizable:listLocalizables) {
            localizable.refreshLocalizableItems();
        }
    }

    public void addListener(OutGameControllerEvents listener) {
        listeners.add(listener);
    }
}
