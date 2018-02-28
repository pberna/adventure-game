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

package com.pberna.adventure.screens.ingame;

import java.util.ArrayList;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pberna.adventure.pj.effects.Effect;
import com.pberna.adventure.pj.effects.PermanentEffect;
import com.pberna.adventure.screens.ExitListener;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.controls.EffectSufferedControl;
import com.pberna.adventure.screens.windows.*;
import com.pberna.engine.achievements.achievement.Achievement;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.screens.TextureRegionToDraw;
import com.pberna.adventure.screens.controls.PendingItemsControl;
import com.pberna.engine.screens.controls.ScrollPaneIndicator;
import com.pberna.adventure.screens.ingame.places.PlaceViewer;
import com.pberna.adventure.screens.ingame.places.PlaceViewerEventsListener;
import com.pberna.engine.screens.shapes.LineRectangle;
import com.pberna.engine.screens.shapes.ShapesHelper;
import com.pberna.engine.screens.windows.ConfirmWindow;
import com.pberna.engine.screens.windows.ConfirmWindowListener;
import com.pberna.engine.screens.windows.NewAchievementWindow;
import com.pberna.engine.screens.windows.NotificationWindow;
import com.pberna.engine.utils2D.graphics.AnimationsHelper;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.graphics.ImageManipulationHelper;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Difficulty;
import com.pberna.adventure.pj.Gender;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.places.*;
import com.pberna.adventure.spells.AttackSpell;

public class MainInGameScreen extends BaseStageScreen implements ILocalizable {
	
	private static final float PercentageHeightButtonsPanel = 0.10f;	
	private static final float PercentageBorderAroundMainText = 0.05f;
		
	private static final float ShowPlaceAnimationDuration = 0.8f;
	private static final float ShowHideWindowAnimationDuration = 0.6f;
			
	private ImageButton mainMenuButton;
	private ImageButton spellsButton;
	private ImageButton inventoryButton;	
	private ImageButton characterSheetButton;
	private PendingItemsControl pendingItemsControl;
	private EffectSufferedControl effectLifePointsChangeControl;
	private EffectSufferedControl effectPowerPointsChangeControl;
	private EffectSufferedControl effectLuckPointsChangeControl;
	
	private ScrollPane mainScrollPane;
	private ScrollPaneIndicator paneIndicator;
	private PlaceViewer placeViewer;

	private CombatWindow combatWindow;
	private AttributeSkillCheckWindow attributeSkillCheckWindow;
	private MainMenuWindow mainMenu;
	private OptionsMenuWindow optionsMenuWindow;
	private NotificationWindow notificationWindow;
	private NewAchievementWindow newAchievementWindow;
	private UseLuckWindow useLuckWindow;
	private ConfirmWindow confirmWindow;
		
	private Rectangle lowerPanel;
	private Rectangle mainPanel;
	private Character character;
	private Place activePlace;
	
	private ArrayList<InGameEventsListener> listeners;
	private ArrayList<ILocalizable> localizables;
	private boolean confirmExit;
	private int previousAllocateLuckPoints;
	
	public MainInGameScreen()
	{
		setBackgroundTextureName("old_page");		
		lowerPanel = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mainPanel = new Rectangle();
		addTextureRegionToDraw(getTextureRegionToDraw());
		addLineShape(getLowerPanelBorder());
		character = null;
		activePlace = null;		
		
		listeners = new ArrayList<InGameEventsListener>();
		localizables = new ArrayList<ILocalizable>();
		confirmExit = true;
		previousAllocateLuckPoints = 0;
		
		buildStage();
	}	
	
	public void addListener(InGameEventsListener listener) {
		listeners.add(listener);
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
		placeViewer.setCharacter(this.character);		
		showCharacterButton();
	}	

	public void setActivePlace(Place activePlace) {
		AnimationsHelper.hideActorByAlpha(placeViewer);		
		this.activePlace = activePlace;		
		placeViewer.showPlace(this.activePlace);
		mainScrollPane.setScrollPercentY(0);
		
		ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(placeViewer);
		placeViewer.addAction(AnimationsHelper.getFadeInAction(ShowPlaceAnimationDuration));
					
		Timer.schedule(new Task() {
			@Override
			public void run() {
				endSetActivePlace();
			}
		}, ShowPlaceAnimationDuration);

		if(this.activePlace instanceof EndAdventurePlace) {
			if (((EndAdventurePlace) this.activePlace).isPlayerDead()) {
				setInventoryScreenEnabled(false);
			} else {
				setInventoryScreenEnabled(true);
			}
		} else if(this.character.getCurrentLifePoints() <= 0) {
			setInventoryScreenEnabled(false);
		} else {
			setInventoryScreenEnabled(true);
		}
	}

	private void endSetActivePlace() {
		ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(placeViewer);
	}

	private void setInventoryScreenEnabled(boolean enabled) {
		if(enabled) {
			ImageManipulationHelper.setActorNonTransparent(spellsButton);
			spellsButton.setTouchable(Touchable.enabled);
			ImageManipulationHelper.setActorNonTransparent(inventoryButton);
			inventoryButton.setTouchable(Touchable.enabled);
		} else {
			ImageManipulationHelper.setActorSemiTransparent(spellsButton);
			spellsButton.setTouchable(Touchable.disabled);
			ImageManipulationHelper.setActorSemiTransparent(inventoryButton);
			inventoryButton.setTouchable(Touchable.disabled);
		}
	}

	private TextureRegionToDraw getTextureRegionToDraw() {
		TextureRegionToDraw toDraw = new TextureRegionToDraw();
		
		toDraw.setTextureRegion(new TextureRegion( AssetRepository.getInstance().getTexture("old_page_dark"), 
				0, 0, Gdx.graphics.getWidth(), (int)(Gdx.graphics.getHeight() * PercentageHeightButtonsPanel)));
		toDraw.setX(0);
		toDraw.setY(0);
		
		return toDraw;
	}
	
	private LineRectangle getLowerPanelBorder(){
		LineRectangle lineRectangle = new LineRectangle();
		lineRectangle.setColor(Color.BLACK);
		lineRectangle.setRectangle(lowerPanel);
		
		return lineRectangle;
	}

	public void setConfirmExit(boolean confirmExit) {
		this.confirmExit = confirmExit;
	}

	private void buildStage() {
		//main menu button		
		mainMenuButton = new ImageButton(
				new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "home_button")),
				new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "home_button_pressed")));
		StageScreenHelper.addOnClickSound(mainMenuButton, AssetRepository.getInstance().getSound("click_button"));
		mainMenuButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//main menu
				mainMenu.showPopUp(ShowHideWindowAnimationDuration, true);
			}
		});
		stage.addActor(mainMenuButton);

		//spells button
		TextureRegion spellButtonTexture = AssetRepository.getInstance().getTextureRegion("widgets", "spell_button");
		spellsButton = new ImageButton(
				new TextureRegionDrawable(spellButtonTexture),
				new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegionForPressed(spellButtonTexture)));
		StageScreenHelper.addOnClickSound(spellsButton, AssetRepository.getInstance().getSound("click_button"));
		spellsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//spells
				for (InGameEventsListener listener : listeners) {
					listener.openSpells();
				}
			}
		});
		stage.addActor(spellsButton);

		//inventory button
		TextureRegion inventoryButtonTexture = AssetRepository.getInstance().getTextureRegion("widgets", "backpack");
		inventoryButton = new ImageButton(
				new TextureRegionDrawable(inventoryButtonTexture),
				new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegionForPressed(inventoryButtonTexture)));
		StageScreenHelper.addOnClickSound(inventoryButton, AssetRepository.getInstance().getSound("click_button"));
		inventoryButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//inventory
				for (InGameEventsListener listener : listeners) {
					listener.openInventory();
				}
			}
		});
		stage.addActor(inventoryButton);

		//pendingItemsControl
		pendingItemsControl = new PendingItemsControl();
		stage.addActor(pendingItemsControl);

		//effectLifeChangeControl
		effectLifePointsChangeControl = new EffectSufferedControl("life_points");
		stage.addActor(effectLifePointsChangeControl);

		//effectPowerPointsChangeControl
		effectPowerPointsChangeControl = new EffectSufferedControl("mana_points");
		stage.addActor(effectPowerPointsChangeControl);

		//effectLuckPointsChangeControl
		effectLuckPointsChangeControl = new EffectSufferedControl("luck_points");
		stage.addActor(effectLuckPointsChangeControl);

		//place viewer and scroll pane
		placeViewer = new PlaceViewer();
		placeViewer.setPaddingWidth(Gdx.graphics.getWidth() * PercentageBorderAroundMainText);
		placeViewer.addListener(new PlaceViewerEventsListener() {

			@Override
			public void checkAttributePressed(Attribute attribute, Difficulty difficulty) {
				attributeSkillCheckWindow.setCheckInfo(attribute, difficulty, character.getTotalAttributeValue(attribute.getId()), useLuckWindow.getLuckPoints());
				attributeSkillCheckWindow.showPopUp(ShowHideWindowAnimationDuration, false);
				ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(attributeSkillCheckWindow);
				attributeSkillCheckWindow.startCheck(ShowHideWindowAnimationDuration);
			}

			@Override
			public void checkSkillPressed(Skill skill, Difficulty difficulty) {
				attributeSkillCheckWindow.setCheckInfo(skill, difficulty, character.getTotalSkillValue(skill.getId()), useLuckWindow.getLuckPoints());
				attributeSkillCheckWindow.showPopUp(ShowHideWindowAnimationDuration, false);
				ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(attributeSkillCheckWindow);
				attributeSkillCheckWindow.startCheck(ShowHideWindowAnimationDuration);
			}

			@Override
			public void startCombat(CombatPlace combatPlace, boolean runAway) {
				combatWindow.setCharacter(character);
				combatWindow.setCombatPlace(combatPlace);
				combatWindow.showPopUp(ShowHideWindowAnimationDuration, false);
				ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(combatWindow);
				combatWindow.startCombat(ShowHideWindowAnimationDuration, runAway);
			}

			@Override
			public void moveToOtherPlace(int idPlace) {
				for (InGameEventsListener listener : listeners) {
					listener.moveToOtherPlace(idPlace);
				}
			}

			@Override
			public void endAdventure() {
				for (InGameEventsListener listener : listeners) {
					listener.endAdventure();
				}
			}

			@Override
			public void allocateLuckPoints() {
				useLuckWindow.setCharacter(character);
				ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(useLuckWindow);
				previousAllocateLuckPoints = useLuckWindow.getLuckPoints();
				useLuckWindow.showPopUp(ShowHideWindowAnimationDuration, true);
			}

			@Override
			public void effectSuffered(Effect effect) {
				if(effect instanceof PermanentEffect) {
					PermanentEffect permanentEffect = (PermanentEffect) effect;
					float delay = 0;
					if(permanentEffect.getLifePointsAdjustment() != 0) {
						effectLifePointsChangeControl.startShowAnimation(permanentEffect.getLifePointsAdjustment(), delay);
						delay += effectLifePointsChangeControl.getTotalDurationWithoutDelay();
					}
					if(permanentEffect.getPowerPointsAdjustment() != 0) {
						effectPowerPointsChangeControl.startShowAnimation(permanentEffect.getPowerPointsAdjustment(), delay);
						delay += effectPowerPointsChangeControl.getTotalDurationWithoutDelay();
					}
					if(permanentEffect.getLuckAdjustment() != 0) {
						effectLuckPointsChangeControl.startShowAnimation(permanentEffect.getLuckAdjustment(), delay);
					}
				}
			}

			@Override
			public void moveToOtherPlace(SpellUsePlaceToGo place) {
				for (InGameEventsListener listener : listeners) {
					listener.moveToOtherPlace(place);
				}
			}

			@Override
			public void moveToOtherPlace(ItemUsePlaceToGo place) {
				for (InGameEventsListener listener : listeners) {
					listener.moveToOtherPlace(place);
				}
			}
		});
		localizables.add(placeViewer);
		mainScrollPane = new ScrollPane(placeViewer);
		mainScrollPane.setVelocityY(1);
		mainScrollPane.setScrollingDisabled(true, false);
		stage.addActor(mainScrollPane);

		//paneIndicator
		paneIndicator = new ScrollPaneIndicator(mainScrollPane, false);

		//combat window
		combatWindow = new CombatWindow();
		combatWindow.setWindowBackGround(getModalWindowBackground());
		combatWindow.setBorder(ShapesHelper.getBorder(combatWindow));
		combatWindow.setParentScreen(this);
		combatWindow.addListener(new CombatWindowEventsListener() {
			@Override
			public void combatFinished(int idPlaceToGo) {
				combatWindow.hidePopUp(ShowHideWindowAnimationDuration);
				for (InGameEventsListener listener : listeners) {
					listener.moveToOtherPlace(idPlaceToGo);
					listener.combatEnded(combatWindow.getCombat());
				}
			}

			@Override
			public void useItemFromInventory() {
				for (InGameEventsListener listener : listeners) {
					listener.useItemInCombat();
				}
			}

			@Override
			public void openSpellScreen() {
				for (InGameEventsListener listener : listeners) {
					listener.openSpellScreenInCombat();
				}
			}
		});
		localizables.add(combatWindow);

		combatWindow.setVisible(false);
		stage.addActor(combatWindow);
		combatWindow.setWidth(0);
		combatWindow.setHeight(0);

		//attributeSkillCheckWindow
		attributeSkillCheckWindow = new AttributeSkillCheckWindow();
		attributeSkillCheckWindow.setWindowBackGround(getModalWindowBackground());
		attributeSkillCheckWindow.setBorder(ShapesHelper.getBorder(attributeSkillCheckWindow));
		attributeSkillCheckWindow.setParentScreen(this);
		attributeSkillCheckWindow.addListener(new AttributeSkillCheckWindowEventsListener() {

			@Override
			public void checkFinished(boolean checkPassed) {
				if (attributeSkillCheckWindow.getLuckPointsUsed() > 0) {
					character.setCurrentLuckPoints(character.getMaximumLuckPoints() - attributeSkillCheckWindow.getLuckPointsUsed());
				}
				useLuckWindow.setLuckPoints(0);

				attributeSkillCheckWindow.hidePopUp(ShowHideWindowAnimationDuration);
				int idPlace = 0;
				if (activePlace instanceof AttributeCheckPlace) {
					idPlace = checkPassed
							? ((AttributeCheckPlace) activePlace).getIdPlaceToGoIfPass()
							: ((AttributeCheckPlace) activePlace).getIdPlaceToGoIfFail();
				} else if (activePlace instanceof SkillCheckPlace) {
					idPlace = checkPassed
							? ((SkillCheckPlace) activePlace).getIdPlaceToGoIfPass()
							: ((SkillCheckPlace) activePlace).getIdPlaceToGoIfFail();
				}
				for (InGameEventsListener listener : listeners) {
					listener.moveToOtherPlace(idPlace);
				}
			}
		});
		localizables.add(attributeSkillCheckWindow);

		attributeSkillCheckWindow.setVisible(false);
		stage.addActor(attributeSkillCheckWindow);
		attributeSkillCheckWindow.setWidth(0);
		attributeSkillCheckWindow.setHeight(0);

		//main menu window
		mainMenu = new MainMenuWindow();
		mainMenu.setWindowBackGround(getModalWindowBackground());
		mainMenu.setBorder(ShapesHelper.getBorder(mainMenu));
		mainMenu.setParentScreen(this);
		mainMenu.addListener(new MainMenuWindowListener() {
			@Override
			public void quitPressed() {
				mainMenu.hidePopUp(ShowHideWindowAnimationDuration);
				Timer.schedule(new Task() {
					@Override
					public void run() {
						if (confirmExit) {
							confirmWindow.showConfirmWindowPopUp(Localization.getInstance().getTranslation("Common", "quitButtonLabel"),
									Localization.getInstance().getTranslation("InGame", "exitConfirmationMessage"), new ConfirmWindowListener() {
									@Override
										public void acceptPressed() {
											for (InGameEventsListener listener : listeners) {
												listener.quitGame();
											}
										}

										@Override
										public void cancelPressed() {

										}
									}, ShowHideWindowAnimationDuration);
						} else {
							for (InGameEventsListener listener : listeners) {
								listener.quitGame();
							}
						}
					}
				}, ShowHideWindowAnimationDuration);
			}

			@Override
			public void optionsPressed() {
				mainMenu.hidePopUp(ShowHideWindowAnimationDuration);
				Timer.schedule(new Task() {
					@Override
					public void run() {
						optionsMenuWindow.refreshControlsFromConfig();
						optionsMenuWindow.showPopUp(ShowHideWindowAnimationDuration, true);
					}
				}, ShowHideWindowAnimationDuration);
			}

			@Override
			public void loadPressed() {
				mainMenu.hidePopUp(ShowHideWindowAnimationDuration);
				Timer.schedule(new Task() {
					@Override
					public void run() {
						for (InGameEventsListener listener : listeners) {
							listener.loadGame();
						}
					}
				}, ShowHideWindowAnimationDuration);
			}

			@Override
			public void continuePressed() {
				mainMenu.hidePopUp(ShowHideWindowAnimationDuration);
			}

			@Override
			public void savePressed() {
				for (InGameEventsListener listener : listeners) {
					listener.saveGame();
				}
				mainMenu.hidePopUp(ShowHideWindowAnimationDuration);
			}
		});
		localizables.add(mainMenu);

		mainMenu.setVisible(false);
		stage.addActor(mainMenu);
		mainMenu.setWidth(0);
		mainMenu.setHeight(0);

		//optionsMenuWindow
		optionsMenuWindow = new OptionsMenuWindow();
		optionsMenuWindow.setWindowBackGround(getModalWindowBackground());
		optionsMenuWindow.setBorder(ShapesHelper.getBorder(optionsMenuWindow));
		optionsMenuWindow.setParentScreen(this);
		optionsMenuWindow.addListener(new OptionsMenuEventsListener() {
			@Override
			public void selectLanguage(Locale locale) {
				for (InGameEventsListener listener : listeners) {
					listener.selectLanguage(locale);
				}
			}

			@Override
			public void exit() {
				optionsMenuWindow.hidePopUp(ShowHideWindowAnimationDuration);
			}
		});
		localizables.add(optionsMenuWindow);

		optionsMenuWindow.setVisible(false);
		stage.addActor(optionsMenuWindow);
		optionsMenuWindow.setWidth(0);
		optionsMenuWindow.setHeight(0);

		//notificationWindow
		notificationWindow = new NotificationWindow();
		notificationWindow.setParentScreen(this);
		notificationWindow.setVisible(false);
		stage.addActor(notificationWindow);

		//newAchievementWindow
		newAchievementWindow = new NewAchievementWindow();
		newAchievementWindow.setParentScreen(this);
		newAchievementWindow.setVisible(false);
		stage.addActor(newAchievementWindow);

		//useLuckWindow
		useLuckWindow = new UseLuckWindow();
		useLuckWindow.setWindowBackGround(getModalWindowBackground());
		useLuckWindow.setBorder(ShapesHelper.getBorder(useLuckWindow));
		useLuckWindow.setParentScreen(this);
		useLuckWindow.addListener(new ExitListener() {
			@Override
			public void exit(Object sender) {
				placeViewer.setAllocatedLuckPoints(useLuckWindow.getLuckPoints());
				useLuckWindow.hidePopUp(ShowHideWindowAnimationDuration);
			}
		});
		localizables.add(useLuckWindow);

		useLuckWindow.setVisible(false);
		stage.addActor(useLuckWindow);
		useLuckWindow.setWidth(0);
		useLuckWindow.setHeight(0);

		//confirm window
		confirmWindow = new ConfirmWindow();
		localizables.add(confirmWindow);
		confirmWindow.setWindowBackGround(getModalWindowBackground());
		confirmWindow.setBorder(ShapesHelper.getBorder(confirmWindow));
		confirmWindow.setParentScreen(this);

		confirmWindow.setVisible(false);
		stage.addActor(confirmWindow);
		confirmWindow.setWidth(0);
		confirmWindow.setHeight(0);
	}
	
	private void showCharacterButton() {
		if(character != null) {
			if(characterSheetButton != null) {
				characterSheetButton.remove();
			}
			//move right button
			TextureRegion portraitTextureRegion = AssetRepository.getInstance().getTextureRegion(
					character.getGender() == Gender.Female ? "woman_portraits" : "man_portraits", character.getPortraitImageName());			
			TextureRegion portraitTextureRegionPressed = ImageManipulationHelper.moveTextureRegionForPressed(portraitTextureRegion);						
			characterSheetButton = new ImageButton(
				new TextureRegionDrawable(portraitTextureRegion),
				new TextureRegionDrawable(portraitTextureRegionPressed));
			StageScreenHelper.addOnClickSound(characterSheetButton, AssetRepository.getInstance().getSound("click_button"));
			characterSheetButton.addListener(new ChangeListener() {
			   	@Override
				public void changed(ChangeEvent event, Actor actor) {		   		
			   		for(InGameEventsListener listener: listeners) {
			   			listener.openCharacterSheet();
			   		}
				}
			});
			characterSheetButton.setWidth(mainMenuButton.getWidth());
			characterSheetButton.setHeight(mainMenuButton.getHeight());			
			stage.addActor(characterSheetButton);
		}
	}
	
	@Override
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);
		updatePanelsSizes(width, height);
		
		//main menu
		PositionerHelper.setPositionCentered(mainMenu, mainPanel);
		setBorderOnActor(mainMenu, mainMenu.getBorder());

		//optionsMenuWindow
		PositionerHelper.setPositionCentered(optionsMenuWindow, mainPanel);
		setBorderOnActor(optionsMenuWindow, optionsMenuWindow.getBorder());
		
		//attributeSkillCheckWindow
		PositionerHelper.setPositionCentered(attributeSkillCheckWindow, mainPanel);
		setBorderOnActor(attributeSkillCheckWindow, attributeSkillCheckWindow.getBorder());
		
		//combatWindow
		PositionerHelper.setPositionCentered(combatWindow, stage);
		setBorderOnActor(combatWindow, combatWindow.getBorder());

		//useLuckWindow
		PositionerHelper.setPositionCentered(useLuckWindow, stage);
		setBorderOnActor(useLuckWindow, useLuckWindow.getBorder());

		//confirmWindow
		PositionerHelper.setPositionCentered(confirmWindow, stage);
		setBorderOnActor(confirmWindow, confirmWindow.getBorder());

		//lower panel buttons
		PositionerHelper.setPositionCenteredVerticalFillingRow(getLowerPanelButtons(), lowerPanel, HorizontalSide.Left);
		pendingItemsControl.setX(inventoryButton.getX() + inventoryButton.getWidth() - pendingItemsControl.getOriginalWidth());
		pendingItemsControl.setY(inventoryButton.getY() + inventoryButton.getHeight() - pendingItemsControl.getOriginalHeight());
		if(characterSheetButton != null) {
			PositionerHelper.setPositionCenterInActor(characterSheetButton, effectLifePointsChangeControl);
			PositionerHelper.setPositionCenterInActor(characterSheetButton, effectPowerPointsChangeControl);
			PositionerHelper.setPositionCenterInActor(characterSheetButton, effectLuckPointsChangeControl);
		}

		//place viewer	and scroll pane
		placeViewer.setPaddingWidth(width * PercentageBorderAroundMainText);
		placeViewer.setMainTextWidth(mainPanel.getWidth() * (1 - PercentageBorderAroundMainText * 2f));		
		mainScrollPane.setWidth(mainPanel.getWidth());
		mainScrollPane.setHeight(mainPanel.getHeight());		
		PositionerHelper.setPositionFromCorner(mainScrollPane, Corner.TopLeft, 0, 0, mainPanel);
		paneIndicator.updateIndicatorsPositions(width * PercentageBorderAroundMainText, width * PercentageBorderAroundMainText / 2.0f);
	}
	
	private Actor [] getLowerPanelButtons() {
		if(character == null) {
			return new Actor[] { mainMenuButton, spellsButton, inventoryButton };
		} else {
			return new Actor[] { mainMenuButton, characterSheetButton ,spellsButton, inventoryButton };
		}
	}
	
	private void updatePanelsSizes(float width, float height) {
		float lowerPanelHeight = (height * PercentageHeightButtonsPanel);
		
		lowerPanel.set(0, 0, width, lowerPanelHeight);
		mainPanel.set(0, lowerPanel.getHeight(), width, height - lowerPanel.getHeight());
	}
	
	public void increasePendingItemsCount(int increment) {
		pendingItemsControl.increaseCount(increment);
	}
	
	public void resetPendingItemsCount() {
		pendingItemsControl.resetCount();
	}

	public int getPendingItemsCount() {
		return pendingItemsControl.getPendingCount();
	}
	
	public void advanceCombatToNextTurn() {
		combatWindow.advanceCombatToNextTurn();	
	}
	
	public boolean isMainMenuVisible() {
		return mainMenu.isVisible();
	}
	
	public void hideMainMenu() {
		if(mainMenu.isVisible()) {
			mainMenu.hidePopUp(ShowHideWindowAnimationDuration);
		}
	}

	public boolean isOptionsMenuWindowVisible() {
		return optionsMenuWindow.isVisible();
	}

	public void hideOptionsMenuWindow() {
		if(optionsMenuWindow.isVisible()) {
			optionsMenuWindow.hidePopUp(ShowHideWindowAnimationDuration);
		}
	}
	
	public void castAttackSpell(AttackSpell attackSpell) {
		if(combatWindow.isVisible()) {
			combatWindow.characterCastAttackSpell(attackSpell);
		}
	}
	
	@Override
	public void render(float delta) {		
		super.render(delta);
		
		if(paneIndicator != null) {
			paneIndicator.updateIndicatorsVisibility();
		}
	}
	
	@Override
	public void hide() {		
		super.hide();
		ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(paneIndicator);
	}	
	
	@Override
	public void show() {		
		super.show();

		sendToFrontIfVisible(effectLifePointsChangeControl);
		sendToFrontIfVisible(effectPowerPointsChangeControl);
		sendToFrontIfVisible(effectLuckPointsChangeControl);
		sendToFrontIfVisible(combatWindow);
		sendToFrontIfVisible(mainMenu);
		sendToFrontIfVisible(optionsMenuWindow);
		sendToFrontIfVisible(attributeSkillCheckWindow);
		sendToFrontIfVisible(useLuckWindow);
		sendToFrontIfVisible(confirmWindow);
		sendToFrontIfVisible(notificationWindow);
		sendToFrontIfVisible(newAchievementWindow);

		if(paneIndicator != null) {
			paneIndicator.refreshContinousRendering();
		}
	}

	@Override
	public void refreshLocalizableItems() {
		for(ILocalizable localizable: localizables) {
			localizable.refreshLocalizableItems();
		}
		recalculateActorsPositions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void showOkNotification(String message) {
		showNotification(message, NotificationWindow.NotificationImage.Ok);
	}

	public void showErrorNotification(String message) {
		showNotification(message, NotificationWindow.NotificationImage.Error);
	}

	private void showNotification(String message, NotificationWindow.NotificationImage notificationImage) {
		notificationWindow.setMessageLabel(message);
		notificationWindow.setNotificationImage(notificationImage);
		notificationWindow.showFromAboveScreenAndReturn(ShowHideWindowAnimationDuration, ShowHideWindowAnimationDuration * 2f, null);
	}

	public void showAchievementNotification(Achievement achievement) {
		newAchievementWindow.enqueueAchivement(achievement);
	}

	public void onBackPressed() {
		if(isMainMenuVisible()) {
			hideMainMenu();
		} else if(isOptionsMenuWindowVisible()) {
			hideOptionsMenuWindow();
		} else if(useLuckWindow.isVisible()) {
			useLuckWindow.setLuckPoints(previousAllocateLuckPoints);
			useLuckWindow.hidePopUp(ShowHideWindowAnimationDuration);
		} else if(confirmWindow.isVisible()) {
			confirmWindow.hidePopUp(ShowHideWindowAnimationDuration);
		}
	}
}
