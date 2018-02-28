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

package com.pberna.adventure.screens.windows;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pberna.adventure.combat.Combat;
import com.pberna.adventure.combat.CombatEventsListener;
import com.pberna.adventure.combat.CombatState;
import com.pberna.adventure.combat.CombatWinner;
import com.pberna.adventure.combat.RoundState;
import com.pberna.adventure.combat.Turn;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Enemy;
import com.pberna.adventure.pj.EnemyNextAction;
import com.pberna.adventure.pj.Gender;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.places.CombatPlace;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.adventure.screens.controls.CombatStatsControl;
import com.pberna.adventure.spells.AttackSpell;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.audio.AudioManager;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.screens.controls.LifePowerPointsControl;
import com.pberna.adventure.screens.controls.PortraitsListControl;
import com.pberna.adventure.screens.controls.RollDiceControl;
import com.pberna.adventure.screens.controls.RollDiceControlListener;
import com.pberna.engine.screens.windows.AnimableWindow;
import com.pberna.engine.utils2D.graphics.AnimationsHelper;
import com.pberna.engine.utils2D.graphics.ImageManipulationHelper;

public class CombatWindow extends AnimableWindow implements ILocalizable {
	private static final int UndefinedValue = -1000;
	private static final float TransitionTimeDelay = 2f;
	private static final float FadeAnimationDuration = 0.5f;
	private static final float SplatAnimationDuration1 = 0.75f;
	private static final float SplatAnimationDuration2 = 1.25f;
	private static final float SplatAnimationDurationStay = 0.75f;
	
	private Label titleLabel;	
	private Image characterPortrait;
	private Stack characterPortraitStack;
	private CombatStatsControl characterStatsControl;
	private LifePowerPointsControl characterLifePower;
	private RollDiceControl characterDiceControl;
	private Image characterInitiativingImage;
	private Image characterAttackingImage;
	private Image characterDefendingImage;
	private Image characterAttackingWithMagicImage;
	private Image currentCharacterPortraitImage;
	private Image characterDamageImage;
	private Label characterDamageText;
	
	private Image enemyPortrait;
	private Stack enemyPortraitStack;
	private CombatStatsControl enemyStatsControl;
	private LifePowerPointsControl enemyLifePower;
	private RollDiceControl enemyDiceControl;
	private Image enemyInitiativingImage;
	private Image enemyAttackingImage;
	private Image enemyDefendingImage;
	private Image enemyAttackingWithMagicImage;
	private Image currentEnemyPortraitImage;
	private Image enemyDamageImage;
	private Label enemyDamageLabel;
	
	private Label mainMessageLabel;
	private HorizontalGroup combatWonGroup;
	private Label combatWonLabel;
	private HorizontalGroup combatLostGroup;
	private Label combatLostLabel;
	
	private ImageButton runAwayButton;
	private ImageButton inventoryButton;
	private ImageButton spellsButton;
	private ImageButton attackButton;
	private ExtendedImageButton<Label> continueButton;
	
	@SuppressWarnings("rawtypes")
	private ArrayList<Cell> characterActionsButtonsCells;
	@SuppressWarnings("rawtypes")
	private Cell continueButtonCell;
	private float continueButtonHeight;
	private float continueButtonWidth;
	private float buttonsRowHeight;
		
	private int paddingSize;
	private int halfPaddingSize;
	private Character character;
	private CombatPlace combatPlace;
	
	private Combat combat;
	private int characterRollValue;
	private int enemyRollValue;
	private AttackSpell characterAttackingSpell;
	private AttackSpell enemyAttackingSpell;
	
	private ArrayList<CombatWindowEventsListener> listeners;
	
	public CombatWindow() {
		super();
		paddingSize = (int)(Gdx.graphics.getHeight() * 0.03f);
		halfPaddingSize = (int)(paddingSize / 2.0f);
		character = null;
		combatPlace = null;
		combat = createCombat();
		characterRollValue = UndefinedValue;
		enemyRollValue = UndefinedValue;
		characterAttackingSpell = null;
		enemyAttackingSpell = null;
		listeners = new ArrayList<CombatWindowEventsListener>();		
		
		buildWindow();
		pack();
				
		setModal(true);
		setMovable(false);
		setResizable(false);
		setBackground(new TextureRegionDrawable(new TextureRegion(AssetRepository.getInstance().getTexture("old_page_dark"))));
		
		originalWidth = getWidth();
		originalHeight = getHeight();
	}	
	
	public void addListener(CombatWindowEventsListener listener) {
		listeners.add(listener);
	}
	
	private Combat createCombat() {
		Combat combat = new Combat();
		combat.addListener(new CombatEventsListener() {
			
			@Override
			public void rollingInitiative() {
				startRollingInitiative();				
			}
			
			@Override
			public void enemyTurn() {
				startEnemyTurn();				
			}
			
			@Override
			public void endRound() {
				startEndRound();				
			}
			
			@Override
			public void defenderSuffersDamage(int damage) {
				startDefenderSuffersDamage(damage);
			}
			
			@Override
			public void combatFinished() {
				endOfCombat();				
			}

			@Override
			public void characterTurn() {
				startCharacterTurn();				
			}

			@Override
			public void successfulRunAway() {
				endOfCombat();
			}
		});
		
		return combat;
	}	

	private void setMainMessage(String message) {
		mainMessageLabel.setText(message);
	}

	private void buildWindow() {
		addTitleRow();
		addCharacterNameRow();
		addCharacterInfoRow();
		addCharacterLifePowerRow();
		addEnemyNameRow();
		addEnemyInfoRow();
		addEnemyLifePowerRow();
		addMainMessageRow();
		addButtonsRow();
		addContinueButtonRow();
	}	

	private void addTitleRow() {
		titleLabel = new Label(String.format(Localization.getInstance().getTranslation("InGame", 
				"combatWindowTitle"), 0), getTitleLabelStyle());
		titleLabel.setAlignment(Align.center);
		add(titleLabel).expandX().pad(paddingSize).colspan(4).padBottom(halfPaddingSize);
		row();		
	}
	
	private void updateMainTitleLabel() {
		titleLabel.setText(String.format(Localization.getInstance().getTranslation("InGame", 
				"combatWindowTitle"), combat.getRound()));
	}
	
	private void addCharacterNameRow() {
		characterStatsControl = new CombatStatsControl();
		add(characterStatsControl).center().colspan(4).padLeft(paddingSize).padRight(paddingSize).padBottom(halfPaddingSize);
		row();
	}
	
	private void addCharacterInfoRow() {
		characterPortrait = new Image(AssetRepository.getInstance().getTextureRegion("man_portraits", "man1"));
		characterInitiativingImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "initiativing_portrait"));		
		characterAttackingImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "attacking_portrait"));
		characterDefendingImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "defending_portrait"));
		characterAttackingWithMagicImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "magic_portrait"));
		characterDamageImage = new Image(AssetRepository.getInstance().getTextureRegion("animations", "blood_splat1"));
		characterDamageText = new Label("-00", getDamageLabelStyle());
		characterDamageText.setAlignment(Align.center);
		characterPortraitStack = StageScreenHelper.createStackWithActors(characterPortrait, characterInitiativingImage,
				characterAttackingImage, characterDefendingImage, characterAttackingWithMagicImage, characterDamageImage, characterDamageText);
		
		characterDiceControl = new RollDiceControl();		
		characterDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame", "initiative"));
		characterDiceControl.addListener(new RollDiceControlListener() {			
			@Override
			public void rollFinished(int totalRollResult) {
				characterRollFinished(totalRollResult);				
			}
		});
		
		add(characterPortraitStack).center().padLeft(paddingSize).padBottom(halfPaddingSize).
			width(characterPortrait.getWidth()).height(characterPortrait.getHeight()).colspan(2);
		add(characterDiceControl).center().padRight(paddingSize).padBottom(halfPaddingSize).colspan(2);
		row();
	}	

	private void addCharacterLifePowerRow() {
		characterLifePower = new LifePowerPointsControl();
		characterLifePower.resetPointsLabel();
		
		add(characterLifePower).center().colspan(4).padLeft(paddingSize).padRight(paddingSize).padBottom(halfPaddingSize);
		row();
	}
	
	private void addEnemyNameRow() {
		enemyStatsControl = new CombatStatsControl();
		add(enemyStatsControl).center().colspan(4).padLeft(paddingSize).padRight(paddingSize).padBottom(halfPaddingSize);
		row();		
	}
	
	private void addEnemyInfoRow() {
		enemyPortrait = new Image(AssetRepository.getInstance().getTextureRegion("man_portraits", "man1"));
		enemyInitiativingImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "initiativing_portrait"));
		enemyAttackingImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "attacking_portrait"));
		enemyDefendingImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "defending_portrait"));
		enemyAttackingWithMagicImage = new Image(AssetRepository.getInstance().getTextureRegion("widgets", "magic_portrait"));
		enemyDamageImage = new Image(AssetRepository.getInstance().getTextureRegion("animations", "blood_splat1"));
		enemyDamageLabel = new Label("-00", getDamageLabelStyle());
		enemyDamageLabel.setAlignment(Align.center);
		enemyPortraitStack = StageScreenHelper.createStackWithActors(enemyPortrait, enemyInitiativingImage, 
				enemyAttackingImage, enemyDefendingImage, enemyAttackingWithMagicImage, enemyDamageImage, enemyDamageLabel);
				
		enemyDiceControl = new RollDiceControl();		
		enemyDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame", "initiative"));
		enemyDiceControl.addListener(new RollDiceControlListener() {			
			@Override
			public void rollFinished(int totalRollResult) {
				enemyRollFinished(totalRollResult);				
			}
		});
		
		add(enemyPortraitStack).center().padLeft(paddingSize).padBottom(halfPaddingSize).
			width(enemyPortrait.getWidth()).height(enemyPortrait.getHeight()).colspan(2);
		add(enemyDiceControl).center().padRight(paddingSize).padBottom(halfPaddingSize).colspan(2);
		row();		
	}	

	private void addEnemyLifePowerRow() {
		enemyLifePower = new LifePowerPointsControl();
		enemyLifePower.resetPointsLabel();
		
		add(enemyLifePower).center().colspan(4).padLeft(paddingSize).padRight(paddingSize).padBottom(halfPaddingSize);
		row();
	}
	
	private void addMainMessageRow() {
		Image combatWonImage = new Image(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "success")));
		combatWonLabel = new Label("   " + Localization.getInstance().getTranslation("InGame", "combatResultWin", false),
				getMainMessageLabelStyle());
		combatWonGroup = StageScreenHelper.createHorizontalGroupWithActors(combatWonImage, combatWonLabel);
				
		Image combatLostImage = new Image(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion("widgets", "fail")));
		combatLostLabel = new Label("   " + Localization.getInstance().getTranslation("InGame", "combatResultLost", false),
				getMainMessageLabelStyle());
		combatLostGroup = StageScreenHelper.createHorizontalGroupWithActors(combatLostImage, combatLostLabel);
				
		mainMessageLabel = new Label("    ", getMainMessageLabelStyle());
		mainMessageLabel.setAlignment(Align.center);		
		Stack stack = StageScreenHelper.createStackWithActors(combatWonGroup, combatLostGroup,mainMessageLabel);
				
		add(stack).center().colspan(4).padLeft(paddingSize).padRight(paddingSize).padBottom(halfPaddingSize);
		
		row();
	}
	
	@SuppressWarnings("rawtypes")
	private void addButtonsRow() {
		characterActionsButtonsCells = new ArrayList<Cell>();
		TextureRegion runAwayTexture = AssetRepository.getInstance().getTextureRegion("widgets", "runaway");
		runAwayButton = new ImageButton(
				new TextureRegionDrawable(runAwayTexture),
				new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegionForPressed(runAwayTexture)));
		StageScreenHelper.addOnClickSound(runAwayButton, AssetRepository.getInstance().getSound("click_button"));
		runAwayButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				characterRunsAway();				
			}
		});

		TextureRegion inventoryButtonTexture = AssetRepository.getInstance().getTextureRegion("widgets", "backpack");
		inventoryButton = new ImageButton(
				new TextureRegionDrawable(inventoryButtonTexture),
				new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegionForPressed(inventoryButtonTexture)));
		StageScreenHelper.addOnClickSound(inventoryButton, AssetRepository.getInstance().getSound("click_button"));
		inventoryButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for(CombatWindowEventsListener listener:listeners) {
					listener.useItemFromInventory();
				}
			}			
		});

		TextureRegion spellButtonTexture = AssetRepository.getInstance().getTextureRegion("widgets", "spell_button");
		spellsButton = new ImageButton(
				new TextureRegionDrawable(spellButtonTexture),
				new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegionForPressed(spellButtonTexture)));
		StageScreenHelper.addOnClickSound(spellsButton, AssetRepository.getInstance().getSound("click_button"));
		spellsButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for(CombatWindowEventsListener listener:listeners) {
					listener.openSpellScreen();
				}
			}			
		});
		
		TextureRegion attackButtonTextureRegion = AssetRepository.getInstance().getTextureRegion("widgets", "attack_button");
		attackButton = new ImageButton(
				new TextureRegionDrawable(attackButtonTextureRegion),
				new TextureRegionDrawable(ImageManipulationHelper.moveTextureRegion(attackButtonTextureRegion, -1, -1)));
		StageScreenHelper.addOnClickSound(attackButton, AssetRepository.getInstance().getSound("click_button"));
		attackButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				characterAttacks();	
			}
		});
		buttonsRowHeight = Math.max(runAwayButton.getHeight(), Math.max(inventoryButton.getHeight(), 
				Math.max(spellsButton.getHeight(), attackButton.getHeight())));
				
		add(runAwayButton).center().padLeft(paddingSize).padBottom(halfPaddingSize);
		add(spellsButton).center().padLeft(halfPaddingSize).padBottom(halfPaddingSize);
		add(inventoryButton).center().padLeft(halfPaddingSize).padBottom(halfPaddingSize);
		add(attackButton).center().padLeft(halfPaddingSize).padBottom(halfPaddingSize).padRight(paddingSize);
		row();
		
		//add cells of the four buttons
		for(int i = this.getCells().size - 4; i < this.getCells().size; i++)
		{
			characterActionsButtonsCells.add(this.getCells().get(i));
		}
	}	

	private void addContinueButtonRow() {
		continueButton = StageScreenHelper.createLabelImageButton(Localization.getInstance().getTranslation(
				"InGame", "continue"), this, AssetRepository.getInstance().getSound("click_button"));
		continueButton.addListener(new ChangeListener() {			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(combat.getCombatState() == CombatState.Fighting) {
					combat.nextTurnOrRound();
				} else if (combat.getCombatState() == CombatState.Finished || combat.getCombatState() == CombatState.RunAway) {
					exitCombatWindow();
				}
			}
		});
		
		continueButtonHeight = continueButton.getHeight();
		continueButtonWidth = continueButton.getWidth();
		
		this.getCells().peek().center().colspan(4).height(0);
		StageScreenHelper.setImageButtonVisible(continueButton, false);
		row();
		
		continueButtonCell = this.getCells().peek();
	}	

	private LabelStyle getTitleLabelStyle() {
		return StageScreenHelper.getLabelStyle(Color.BLACK);
	}

	private LabelStyle getDamageLabelStyle() {
		return StageScreenHelper.getLabelButtonStyle();
	}
	
	private LabelStyle getMainMessageLabelStyle() {
		return new LabelStyle(FontHelper.getOptionsTextForLabelFont(), Color.BLACK);
	}
	
	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
		combat.setCharacter(this.character);
		updateCharacterInfo(true);		
	}
	
	private void updateCharacterInfo(boolean updatePortrait) {
		if(character != null) {
			if(updatePortrait) {
				updateCharacterPortrait();
			}
			characterStatsControl.update(character);
			characterLifePower.updatePointsLabel(character);
		}
	}
	
	private void updateCharacterPortrait() {
		if(characterPortrait != null) {
			characterPortraitStack.removeActor(characterPortrait);
		}
		String portraitAtlas = character.getGender() == Gender.Male 
				? PortraitsListControl.ManTextureAtlas 
				: PortraitsListControl.WomanTextureAtlas;
		characterPortrait = new Image(AssetRepository.getInstance().getTextureRegion(
				portraitAtlas, character.getPortraitImageName()));
		characterPortraitStack.addActorAt(0, characterPortrait);
		Cell<Stack> cell = getCell(characterPortraitStack);
		if(cell != null) {
			cell.width(characterPortrait.getWidth()).height(characterPortrait.getHeight());
		}
	}

	public CombatPlace getCombatPlace() {
		return combatPlace;
	}

	public void setCombatPlace(CombatPlace combatPlace) {
		this.combatPlace = combatPlace;		
		combat.setEnemy(this.combatPlace.getEnemy());
		updateEnemyInfo(true);
		if(this.combatPlace.canRunAway()) {
			ImageManipulationHelper.setActorNonTransparent(runAwayButton);
			runAwayButton.getStyle().imageDown = new TextureRegionDrawable(
					AssetRepository.getInstance().getTextureRegion("widgets", "selectOption_pressed"));
			runAwayButton.setTouchable(Touchable.enabled);
		} else {
			ImageManipulationHelper.setActorSemiTransparent(runAwayButton);
			runAwayButton.getStyle().imageDown = null;
			runAwayButton.setTouchable(Touchable.disabled);
		}
	}

	private void updateEnemyInfo(boolean updatePortrait) {
		if(combatPlace == null) {
			return;
		}
		
		Enemy enemy = combatPlace.getEnemy();
		
		if(enemy != null) {
			if(updatePortrait) {
				updateEnemyPortrait(enemy);
			}
			enemyStatsControl.update(enemy);
			enemyLifePower.updatePointsLabel(enemy);
		}
	}
	
	private void updateEnemyPortrait(Enemy enemy) {
		if(enemyPortrait != null) {
			enemyPortraitStack.removeActor(enemyPortrait);
		}
		enemyPortrait =  enemy.getTextureImage() == null
				? new Image(AssetRepository.getInstance().getTextureRegion("enemies", enemy.getImageName()))
				: new Image(enemy.getTextureImage());
		enemyPortraitStack.addActorAt(0, enemyPortrait);
		Cell<Stack> cell = getCell(enemyPortraitStack);
		if(cell != null) {
			cell.width(enemyPortrait.getWidth()).height(enemyPortrait.getHeight());
		}
	}
		
	public void startCombat(float delay, boolean runAway) {
		if(character == null || combatPlace == null) {
			return;
		}
		
		combat.resetCombat();
		setButtonsVisible(false);
		showMenuButtonsRow();		
		StageScreenHelper.setImageButtonVisible(continueButton, false);
		hideContinueButtonRow();
		hideAllPortraitsImages();
		setMessagesVisible(true, false, false);		
		
		resetRollValues();

		final boolean finalRunAway = runAway;
		if(delay > 0) {
			Timer.schedule(new Task() {				
				@Override
				public void run() {
					combat.startCombat(finalRunAway);
					
				}
			}, delay);
		} else {
			combat.startCombat(finalRunAway);
		}
	}
	
	protected void startRollingInitiative() {
		resetRollValues();
		
		setButtonsVisible(false);
		showMenuButtonsRow();		
		StageScreenHelper.setImageButtonVisible(continueButton, false);
		hideContinueButtonRow();
				
		characterDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame", "initiative"));
		characterDiceControl.setBaseValue(character.getTotalInitiativeValue());
		
		enemyDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame", "initiative"));
		enemyDiceControl.setBaseValue(combatPlace.getEnemy().getInitiativeValue());
		
		setMainMessage(Localization.getInstance().getTranslation("InGame", "combatRollingInitiatives", false));
		updateMainTitleLabel();
				
		currentCharacterPortraitImage = characterInitiativingImage;
		currentCharacterPortraitImage.addAction(AnimationsHelper.getFadeInAction(FadeAnimationDuration));
		currentEnemyPortraitImage = enemyInitiativingImage;
		currentEnemyPortraitImage.addAction(AnimationsHelper.getFadeInAction(FadeAnimationDuration));
		
		characterDiceControl.startRoll();
		enemyDiceControl.startRoll();
	}
	
	private void resetRollValues() {
		characterRollValue = UndefinedValue;
		enemyRollValue = UndefinedValue;
	}

	protected void startCharacterTurn() {
		resetRollValues();
		setButtonsVisible(true);
				
		setMainMessage(Localization.getInstance().getTranslation("InGame", "characterTurnMessage", false));
		
		currentCharacterPortraitImage.addAction(AnimationsHelper.getFadeOutAction(FadeAnimationDuration));
		currentEnemyPortraitImage.addAction(AnimationsHelper.getFadeOutAction(FadeAnimationDuration));		
	}

	protected void startEnemyTurn() {
		resetRollValues();
		setButtonsVisible(false);

		if(combat.getCombatState() != CombatState.RunAway) {
			setMainMessage(String.format(Localization.getInstance().getTranslation("InGame", "enemyTurnToAct", false),
					removeBlankAdjusted(combatPlace.getEnemy().getName())));
		} else {
			setMainMessage(String.format(Localization.getInstance().getTranslation("InGame", "enemyAttackRunAway", false),
					removeBlankAdjusted(combatPlace.getEnemy().getName())));
		}
		if(currentCharacterPortraitImage != null) {
			currentCharacterPortraitImage.addAction(AnimationsHelper.getFadeOutAction(FadeAnimationDuration));
		}
		if(currentEnemyPortraitImage != null) {
			currentEnemyPortraitImage.addAction(AnimationsHelper.getFadeOutAction(FadeAnimationDuration));
		}
		
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		        enemyAttacks(false);
		    }
		}, TransitionTimeDelay);
	}

	private static String removeBlankAdjusted(String text) {
		return text.replace(Localization.BlankSpaceAdjusted, " ");
	}
	
	protected void enemyAttacks(boolean characterIsRunningAway) {
		resetRollValues();
		setButtonsVisible(false);

		Enemy enemy = combatPlace.getEnemy();
		if(enemy.getNextAction() == EnemyNextAction.MagicAttack) {
			enemyAttackingSpell = enemy.getAttackSpellToCast();
			if(enemyAttackingSpell != null) {
				enemy.setCurrentPowerPoints(enemy.getCurrentPowerPoints() - enemyAttackingSpell.getPowerPointsNeeded());
				updateEnemyInfo(false);
			}
		} else {
			enemyAttackingSpell = null;
		}
		
		characterDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame",
				enemyAttackingSpell == null ? "defense" : "magicDefense"));
		characterDiceControl.setBaseValue(enemyAttackingSpell == null ? character.getTotalDefenseValue()
				: character.getTotalSkillValue(Skill.IdMagic));
		
		enemyDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame",
				enemyAttackingSpell == null ? "attack" : "magicAttack"));
		enemyDiceControl.setBaseValue(enemyAttackingSpell == null ? enemy.getAttackValue() : enemy.getMagicValue());
				
		if(characterIsRunningAway) {
			setMainMessage(String.format(Localization.getInstance().getTranslation("InGame", "enemyAttackOnRunAway", false),
					removeBlankAdjusted(enemy.getName()), removeBlankAdjusted(character.getName())));
		} else {
			setMainMessage(String.format(Localization.getInstance().getTranslation("InGame", "attackMessage", false),
					removeBlankAdjusted(enemy.getName()), removeBlankAdjusted(character.getName())));
		}
		
		currentCharacterPortraitImage = characterDefendingImage;
		currentCharacterPortraitImage.addAction(AnimationsHelper.getFadeInAction(FadeAnimationDuration));
		currentEnemyPortraitImage = enemyAttackingSpell == null ? enemyAttackingImage : enemyAttackingWithMagicImage;
		currentEnemyPortraitImage.addAction(AnimationsHelper.getFadeInAction(FadeAnimationDuration));
		
		characterDiceControl.startRoll();
		enemyDiceControl.startRoll();
	}
	
	protected void characterAttacks () {
		characterAttackingSpell = null;
		resetRollValues();
		setButtonsVisible(false);
		characterDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame", "attack"));
		characterDiceControl.setBaseValue(character.getTotalAttackValue());
		
		enemyDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame", "defense"));
		enemyDiceControl.setBaseValue(combatPlace.getEnemy().getDefenseValue());
				
		setMainMessage(String.format(Localization.getInstance().getTranslation("InGame", "attackMessage", false),
				removeBlankAdjusted(character.getName()), removeBlankAdjusted(combatPlace.getEnemy().getName())));
		
		currentCharacterPortraitImage = characterAttackingImage;
		currentCharacterPortraitImage.addAction(AnimationsHelper.getFadeInAction(FadeAnimationDuration));
		currentEnemyPortraitImage = enemyDefendingImage;
		currentEnemyPortraitImage.addAction(AnimationsHelper.getFadeInAction(FadeAnimationDuration));
			
		characterDiceControl.startRoll();
		enemyDiceControl.startRoll();
	}
	
	public void characterCastAttackSpell(AttackSpell attackSpell) {
		characterAttackingSpell = attackSpell;
		resetRollValues();
		setButtonsVisible(false);
		characterDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame", "magicAttack"));
		characterDiceControl.setBaseValue(character.getTotalSkillValue(Skill.IdMagic));
		
		enemyDiceControl.setTitleText(Localization.getInstance().getTranslation("InGame", "magicDefense"));
		enemyDiceControl.setBaseValue(combatPlace.getEnemy().getMagicValue());
				
		setMainMessage(String.format(Localization.getInstance().getTranslation("InGame", "attackMessage", false),
				removeBlankAdjusted(character.getName()), removeBlankAdjusted(combatPlace.getEnemy().getName())));
		
		currentCharacterPortraitImage = characterAttackingWithMagicImage;
		currentCharacterPortraitImage.addAction(AnimationsHelper.getFadeInAction(FadeAnimationDuration));
		currentEnemyPortraitImage = enemyDefendingImage;
		currentEnemyPortraitImage.addAction(AnimationsHelper.getFadeInAction(FadeAnimationDuration));
			
		characterDiceControl.startRoll();
		enemyDiceControl.startRoll();	
	}
	
	protected void characterRollFinished(int totalRollResult) {
		
		if(combat.getCombatState() == CombatState.Fighting && combat.getRoundState() == RoundState.RollingInitiative) {
			synchronized(this) {
				characterRollValue = totalRollResult;
				if(enemyRollValue != UndefinedValue) {
					//end initiative
					combat.applyInitiativeRoll(characterRollValue, enemyRollValue);
				}
			}			
		}
		
		if((combat.getCombatState() == CombatState.Fighting &&
				(combat.getRoundState() == RoundState.FirstTurn || combat.getRoundState() == RoundState.SecondTurn)) ||
				(combat.getCombatState() == CombatState.RunAway && combat.getRoundState() == RoundState.AfterAttackRunAway)) {
			synchronized(this) {
				characterRollValue = totalRollResult;
				if(enemyRollValue != UndefinedValue) {
					//end attack
					if(combat.getCurrentTurn() == Turn.Character) {
						combat.applyAttackRoll(characterRollValue, enemyRollValue, characterAttackingSpell != null ?
								characterAttackingSpell.getDamageMultiplier() : 1f, characterAttackingSpell == null);
					} else {
						combat.applyAttackRoll(enemyRollValue, characterRollValue, enemyAttackingSpell != null ?
								enemyAttackingSpell.getDamageMultiplier() : 1f, enemyAttackingSpell == null);
					}
				}
			}
		}		
	}
	
	protected void enemyRollFinished(int totalRollResult) {
		
		if(combat.getCombatState() == CombatState.Fighting && combat.getRoundState() == RoundState.RollingInitiative) {
			synchronized(this) {
				enemyRollValue = totalRollResult;				
				if(characterRollValue != UndefinedValue) {
					//end initiative
					combat.applyInitiativeRoll(characterRollValue, enemyRollValue);
				}
			}			
		}
		
		if((combat.getCombatState() == CombatState.Fighting &&
				(combat.getRoundState() == RoundState.FirstTurn || combat.getRoundState() == RoundState.SecondTurn)) ||
				(combat.getCombatState() == CombatState.RunAway && combat.getRoundState() == RoundState.AfterAttackRunAway)) {
			synchronized(this) {
				enemyRollValue = totalRollResult;
				if(characterRollValue != UndefinedValue) {
					//end attack
					if(combat.getCurrentTurn() == Turn.Character) {
						combat.applyAttackRoll(characterRollValue, enemyRollValue, characterAttackingSpell != null ?
								characterAttackingSpell.getDamageMultiplier() : 1f, characterAttackingSpell == null);
					} else {
						combat.applyAttackRoll(enemyRollValue, characterRollValue, enemyAttackingSpell != null ?
								enemyAttackingSpell.getDamageMultiplier() : 1f, enemyAttackingSpell == null);
					}
				}
			}
		}
	}
	
	protected void startDefenderSuffersDamage(int damage) {
		characterLifePower.updatePointsLabel(character);
		enemyLifePower.updatePointsLabel(combatPlace.getEnemy());
		
		if(combat.getCurrentTurn() == Turn.Character) {
			enemyDamageImage.addAction(new SequenceAction(AnimationsHelper.getFadeInAction(SplatAnimationDuration1),
					AnimationsHelper.getDelayAction(SplatAnimationDurationStay),
					AnimationsHelper.getFadeOutAction(SplatAnimationDuration2)));
			enemyDamageLabel.setText(String.valueOf(damage));
			enemyDamageLabel.addAction(new SequenceAction(AnimationsHelper.getFadeInAction(SplatAnimationDuration1),
					AnimationsHelper.getDelayAction(SplatAnimationDurationStay),
					AnimationsHelper.getFadeOutAction(SplatAnimationDuration2)));
		} else {
			characterDamageImage.addAction(new SequenceAction(AnimationsHelper.getFadeInAction(SplatAnimationDuration1),
					AnimationsHelper.getDelayAction(SplatAnimationDurationStay),
					AnimationsHelper.getFadeOutAction(SplatAnimationDuration2)));
			characterDamageText.setText(String.valueOf(damage));
			characterDamageText.addAction(new SequenceAction(AnimationsHelper.getFadeInAction(SplatAnimationDuration1),
					AnimationsHelper.getDelayAction(SplatAnimationDurationStay),
					AnimationsHelper.getFadeOutAction(SplatAnimationDuration2)));
		}
		AudioManager.getInstance().playSound(AssetRepository.getInstance().getSound("combat_impact"));
	}
	
	protected void characterRunsAway() {
		if(combatPlace != null && combatPlace.canRunAway()) {
			combat.tryToRunAway();
		}
	}
	
	protected void startEndRound() {
		resetRollValues();
		setButtonsVisible(false);
		hideMenuButtonsRow();
		StageScreenHelper.setImageButtonVisible(continueButton, true);
		showContinueButtonRow();
		
		setMainMessage(Localization.getInstance().getTranslation("InGame", "roundFinished", false));
		
		currentCharacterPortraitImage.addAction(AnimationsHelper.getFadeOutAction(FadeAnimationDuration));
		currentEnemyPortraitImage.addAction(AnimationsHelper.getFadeOutAction(FadeAnimationDuration));
	}
	
	protected void endOfCombat() {
		resetRollValues();
		if(combat.getCombatWinner() == CombatWinner.Character) {
			combatWonLabel.setText("   " + Localization.getInstance().getTranslation("InGame", "combatResultWin", false));
			setMessagesVisible(false, true, false);			
		} else if(combat.getCombatWinner() == CombatWinner.Enemy) {
			setMessagesVisible(false, false, true);			
		} else {
			combatWonLabel.setText("   " + Localization.getInstance().getTranslation("InGame", "combatResultRunAway", false));
			setMessagesVisible(false, true, false);
		}
		
		setButtonsVisible(false);
		hideMenuButtonsRow();
		StageScreenHelper.setImageButtonVisible(continueButton, true);
		showContinueButtonRow();
		
		currentCharacterPortraitImage.addAction(AnimationsHelper.getFadeOutAction(FadeAnimationDuration));
		currentEnemyPortraitImage.addAction(AnimationsHelper.getFadeOutAction(FadeAnimationDuration));
	}
	
	protected void exitCombatWindow() {
		int idPlaceToGo;
		
		if(combat.getCombatWinner() == CombatWinner.Character) {
			idPlaceToGo = combatPlace.getIdPlaceToGoIfWin();
		} else if(combat.getCombatWinner() == CombatWinner.Enemy) {
			idPlaceToGo = combatPlace.getIdPlaceToGoIfLose();
		} else {
			idPlaceToGo = combatPlace.getIdPlaceToGoIfRunAway();
		}
		
		for(CombatWindowEventsListener listener:listeners) {
			listener.combatFinished(idPlaceToGo);
		}
	}	
	
	private void setButtonsVisible(boolean visible) {
		runAwayButton.setVisible(visible);
		inventoryButton.setVisible(visible);
		spellsButton.setVisible(visible);
		attackButton.setVisible(visible);
	}	
		
	@SuppressWarnings("rawtypes")
	private void hideMenuButtonsRow() {
		for(Cell cell: characterActionsButtonsCells) {
			cell.pad(0);
			cell.height(0);
		}
	}
			
	@SuppressWarnings("rawtypes")
	private void showMenuButtonsRow() {
		float height = buttonsRowHeight;
				
		for(int i = 0; i < characterActionsButtonsCells.size(); i++) {
			Cell cell = characterActionsButtonsCells.get(i);
			cell.center().padBottom(halfPaddingSize).height(height);
			if(i == 0) {
				cell.padLeft(paddingSize);
			} else {
				cell.padLeft(halfPaddingSize);
			}
			if(i == 3) {
				cell.padRight(paddingSize);
			}
		}
	}
	
	private void showContinueButtonRow() {
		continueButtonCell.center().height(buttonsRowHeight).padLeft(paddingSize).padBottom(halfPaddingSize).padRight(paddingSize);
		continueButton.setHeight(continueButtonHeight);
		continueButton.setWidth(continueButtonWidth);
	}
	
	private void hideContinueButtonRow() {
		continueButtonCell.pad(0).height(0);
	}
	
	private void hideAllPortraitsImages() {
		AnimationsHelper.hideActorByAlpha(characterInitiativingImage);
		AnimationsHelper.hideActorByAlpha(characterAttackingImage);
		AnimationsHelper.hideActorByAlpha(characterDefendingImage);
		AnimationsHelper.hideActorByAlpha(characterAttackingWithMagicImage);
		AnimationsHelper.hideActorByAlpha(characterDamageImage);
		AnimationsHelper.hideActorByAlpha(characterDamageText);
		
		AnimationsHelper.hideActorByAlpha(enemyInitiativingImage);
		AnimationsHelper.hideActorByAlpha(enemyAttackingImage);
		AnimationsHelper.hideActorByAlpha(enemyDefendingImage);
		AnimationsHelper.hideActorByAlpha(enemyAttackingWithMagicImage);
		AnimationsHelper.hideActorByAlpha(enemyDamageImage);
		AnimationsHelper.hideActorByAlpha(enemyDamageLabel);
	}
	
	private void setMessagesVisible(boolean mainMessageVisible, boolean combatWonMessageVisible, boolean combatLostMessageVisible) {
		mainMessageLabel.setVisible(mainMessageVisible);
		combatWonGroup.setVisible(combatWonMessageVisible);
		combatLostGroup.setVisible(combatLostMessageVisible);
	}

	public void advanceCombatToNextTurn() {
		updateCharacterInfo(false);
		updateEnemyInfo(false);
		combat.nextTurnOrRound();
	}

	@Override
	public void refreshLocalizableItems() {
		updateMainTitleLabel();
		updateCharacterInfo(false);
		updateEnemyInfo(false);
		combatWonLabel.setText("   " + Localization.getInstance().getTranslation("InGame", "combatResultWin", false));
		combatLostLabel.setText("   " + Localization.getInstance().getTranslation("InGame", "combatResultLost", false));
		continueButton.getTag().setText(Localization.getInstance().getTranslation("InGame", "continue"));
	}

	public Combat getCombat() {
		return combat;
	}
}
