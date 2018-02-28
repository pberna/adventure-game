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

package com.pberna.adventure.screens.ingame.places;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.pberna.adventure.adventure.AdventureManager;
import com.pberna.adventure.items.Item;
import com.pberna.adventure.pj.effects.Effect;
import com.pberna.adventure.places.*;
import com.pberna.adventure.screens.FontHelper;
import com.pberna.adventure.screens.controls.CounterControl;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.controls2D.ExtendedImageButton;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.engine.utils2D.graphics.ImageManipulationHelper;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.spells.Spell;

import static com.pberna.adventure.places.PlaceType.*;

public class PlaceViewer extends Table implements ILocalizable {
	
	private Label mainTextLabel;
	private ArrayList<Label> listOptionLabels;	
	private float paddingWidth;
	private Character character;
	private ArrayList<PlaceViewerEventsListener> listeners;
	private CounterControl counterControl;
	private Place currentPlace;
	
	public PlaceViewer() {		
		//main text label
		mainTextLabel = new Label(" ", getMainTextLabelStyle());
		mainTextLabel.setAlignment(Align.top | Align.left);
		mainTextLabel.setWrap(true);
		
		listOptionLabels = new ArrayList<Label>();
		paddingWidth = 10;
		listeners = new ArrayList<PlaceViewerEventsListener>();
		counterControl = new CounterControl();
		currentPlace = null;
	}	

	public void setPaddingWidth(float paddingWidth) {
		this.paddingWidth = paddingWidth;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}
	
	public void addListener(PlaceViewerEventsListener listener) {
		listeners.add(listener);
	}
	
	private void notifyMoveToOtherPlace(int idPlace) {
		for(PlaceViewerEventsListener listener:listeners) {
			listener.moveToOtherPlace(idPlace);
		}	
	}

	private void notifyMoveToOtherPlace(SpellUsePlaceToGo placeToGo) {
		for(PlaceViewerEventsListener listener:listeners) {
			listener.moveToOtherPlace(placeToGo);
		}
	}

	private void notifyMoveToOtherPlace(ItemUsePlaceToGo placeToGo) {
		for(PlaceViewerEventsListener listener:listeners) {
			listener.moveToOtherPlace(placeToGo);
		}
	}

	private void notifyEffectSuffered(Effect effect) {
		for(PlaceViewerEventsListener listener:listeners) {
			listener.effectSuffered(effect);
		}
	}

	public void setMainTextWidth(float width) {
		mainTextLabel.setWidth(width);
		Cell<Label> cell = getCell(mainTextLabel);		
		if(cell != null) {
			cell.width(width);
		}
		
		for(Label label: listOptionLabels) {
			label.setWidth(width * 0.8f);
			cell = getCell(label);
			if(cell != null) {
				cell.width(width * 0.8f);
			}
		}		
	}
	
	public void showPlace(Place place) {
		currentPlace = place;
		clearChildren();
		listOptionLabels.clear();
		
		showMainText(place);		
		
		PlaceType placeType = place.getPlaceType();
		if(placeType != null) {
			switch(placeType.getId()) {
			
				case IdOptionChoosePlace:
					showPlaceInfo((OptionChoosePlace)place);
					break;
				case IdCombatPlace:
					showPlaceInfo((CombatPlace)place);
					break;
				case IdSkillCheckPlace:
					showPlaceInfo((SkillCheckPlace)place);
					break;
				case IdAttributeCheckPlace:
					showPlaceInfo((AttributeCheckPlace)place);
					break;
				case IdItemUsePlace:
					showPlaceInfo((ItemUsePlace)place);
					break;
				case IdSpellUsePlace:
					showPlaceInfo((SpellUsePlace)place);
					break;
				case IdEndAdventurePlace:
					showPlaceInfo((EndAdventurePlace)place);
					break;
				case IdEffectPlace:
					showPlaceInfo((EffectPlace)place);
					break;
			}
		}
		pack();
	}
	
	private void showMainText(Place place) {		
		mainTextLabel.setText(place.getTranslatedText().replace("\n", "\n\n"));
		add(mainTextLabel).colspan(2).expandX().left().top().padLeft(paddingWidth).padRight(paddingWidth)
			.padTop(paddingWidth / 2f).padBottom(paddingWidth / 2f);
		row();
	}

	private void showPlaceInfo(OptionChoosePlace place) {
		if(place.getPlacesToGo() != null) {
			for(final OptionChoosePlaceCanGo optionPlace:place.getPlacesToGo()) {
				addOptionRow( new String[] { optionPlace.getTranslatedText() }, 
						AssetRepository.getInstance().getTextureRegion("widgets", "selectOption"),
						AssetRepository.getInstance().getTextureRegion("widgets", "selectOption_pressed"), 
						false, new PlaceViewerButtonDelegate() {
							
							@Override
							public void doAction() {
								notifyMoveToOtherPlace(optionPlace.getIdPlaceToGo());															
							}
						}, false);
			}
		}
	}
	
	private void showPlaceInfo(final CombatPlace place) {
		TextureRegion startCombatTexture = AssetRepository.getInstance().getTextureRegion("widgets", "start_combat");
		addOptionRow(new String[] { Localization.getInstance().getTranslation("InGame", "startCombat", false) },
				startCombatTexture,	ImageManipulationHelper.moveTextureRegionForPressed(startCombatTexture), false,
				new PlaceViewerButtonDelegate() {					
					@Override
					public void doAction() {
						for(PlaceViewerEventsListener listener: listeners) {
							listener.startCombat(place, false);
						}
					}
				}, false);

		TextureRegion runAwayTexture = AssetRepository.getInstance().getTextureRegion("widgets", "runaway");
		addOptionRow(place.canRunAway() 
				? new String[] { Localization.getInstance().getTranslation("InGame", "runAwayCombat", false),
								  Localization.getInstance().getTranslation("InGame", "enemyAttackOnRunAway", false) }
				: new String[] { Localization.getInstance().getTranslation("InGame", "runAwayCombat", false),
					Localization.getInstance().getTranslation("InGame", "cannotRunAwayCombat", false)},
				runAwayTexture,
				place.canRunAway() ? ImageManipulationHelper.moveTextureRegionForPressed(runAwayTexture)
						: null, !place.canRunAway(), new PlaceViewerButtonDelegate() {							
							@Override
							public void doAction() {
								for(PlaceViewerEventsListener listener: listeners) {
									listener.startCombat(place, true);
								}
							}
						}, false);
	}
	
	private void showPlaceInfo(final SkillCheckPlace place) {
		addOptionRow(getTextsToShow(place), AssetRepository.getInstance().getTextureRegion("widgets", "selectOption"),
				AssetRepository.getInstance().getTextureRegion("widgets", "selectOption_pressed"), false, 
				new PlaceViewerButtonDelegate() {					
					@Override
					public void doAction() {
						for(PlaceViewerEventsListener listener:listeners) {
							listener.checkSkillPressed(place.getSkill(), place.getDifficulty());
						}
					}
				}, false);

		addOptionRow(new String[] { Localization.getInstance().getTranslation("InGame", "useLuckPointsToIncrease", false) },
				AssetRepository.getInstance().getTextureRegion("widgets", "luck_points_button"),
				ImageManipulationHelper.moveTextureRegion(AssetRepository.getInstance().getTextureRegion("widgets", "luck_points_button"), -1, -1),
				false,
				new PlaceViewerButtonDelegate() {
					@Override
					public void doAction() {
						for(PlaceViewerEventsListener listener:listeners) {
							listener.allocateLuckPoints();
						}
					}
				}, true);
	}
	
	private String[] getTextsToShow(SkillCheckPlace place) {
		return new String [] {
			String.format(Localization.getInstance().getTranslation("InGame", "makeSkillAttributeDiceRollText", false),
					place.getSkill().getName()),
			String.format(Localization.getInstance().getTranslation("InGame", "difficultyLabel", false),
					place.getDifficulty().getDifficulty(), place.getDifficulty().getName())
		};
	}
	
	private void showPlaceInfo(final AttributeCheckPlace place) {
		addOptionRow(getTextsToShow(place), AssetRepository.getInstance().getTextureRegion("widgets", "selectOption"),
				AssetRepository.getInstance().getTextureRegion("widgets", "selectOption_pressed"), false, 
				new PlaceViewerButtonDelegate() {					
					@Override
					public void doAction() {
						for(PlaceViewerEventsListener listener:listeners) {
							listener.checkAttributePressed(place.getAttribute(), place.getDifficulty());
						}
					}
				}, false);

		addOptionRow(new String[] { Localization.getInstance().getTranslation("InGame", "useLuckPointsToIncrease") },
				AssetRepository.getInstance().getTextureRegion("widgets", "luck_points_button"),
				ImageManipulationHelper.moveTextureRegion(AssetRepository.getInstance().getTextureRegion("widgets", "luck_points_button"), -1, -1),
				false,
				new PlaceViewerButtonDelegate() {
					@Override
					public void doAction() {
						for(PlaceViewerEventsListener listener:listeners) {
							listener.allocateLuckPoints();
						}
					}
				}, true);
	}
	
	private String[] getTextsToShow(AttributeCheckPlace place) {
		return new String [] {
			String.format(Localization.getInstance().getTranslation("InGame", "makeSkillAttributeDiceRollText", false),
					place.getAttribute().getName()),
			String.format(Localization.getInstance().getTranslation("InGame", "difficultyLabel", false),
					place.getDifficulty().getDifficulty(), place.getDifficulty().getName())
		};
	}
	
	private void showPlaceInfo(final ItemUsePlace place) {
		if(place.getPlacesToGo() != null) {
			for(ItemUsePlaceToGo itemUsePlaceToGo:place.getPlacesToGo()) {
				addItemUseRow(itemUsePlaceToGo);				
			}
		}
		
		addOptionRow(new String[] { Localization.getInstance().getTranslation("InGame", "itemDoNotUseItem", false) }, 
				AssetRepository.getInstance().getTextureRegion("widgets", "selectOption"),
				AssetRepository.getInstance().getTextureRegion("widgets", "selectOption_pressed"), false, 
				new PlaceViewerButtonDelegate() {					
					@Override
					public void doAction() { 
						notifyMoveToOtherPlace(place.getIdPlaceToGoIfNoItem());
					}
				}, false);
	}
	
	private void addItemUseRow(final ItemUsePlaceToGo itemUsePlaceToGo) {
		TextureRegion itemTexture = AssetRepository.getInstance().getTextureRegion("items", itemUsePlaceToGo.getItem().getImageName());
		boolean hasItem = character != null && character.hasItem(itemUsePlaceToGo.getItem());
		TextureRegion itemTexturePressed = character != null &&  hasItem
				? ImageManipulationHelper.moveTextureRegionForPressed(itemTexture) : null;
		
		addOptionRow(getItemTexts(itemUsePlaceToGo.getItem()), itemTexture, itemTexturePressed, !hasItem,  
			new PlaceViewerButtonDelegate() {			
				@Override
				public void doAction() {					
					notifyMoveToOtherPlace(itemUsePlaceToGo);
				}
			}, false);
	}
	
	private String [] getItemTexts(Item item) {
		if(character == null || !character.hasItem(item)) {
			return new String [] {
					String.format(Localization.getInstance().getTranslation("InGame", "itemUseItem", false), item.getName()),
					Localization.getInstance().getTranslation("InGame", "itemDoNotHave", false)
				};
		} 
		else {
			return new String [] {
					String.format(Localization.getInstance().getTranslation("InGame", "itemUseItem", false), item.getName()),
				};
		}		
	}
	
	private void showPlaceInfo(final SpellUsePlace place) {
		if(place.getPlacesToGo() != null) {
			for(SpellUsePlaceToGo spellPlaceToGo:place.getPlacesToGo()) {
				addSpellUseRow(spellPlaceToGo);				
			}
		}
		
		addOptionRow(new String[] { Localization.getInstance().getTranslation("InGame", "spellNoCast", false) }, 
				AssetRepository.getInstance().getTextureRegion("widgets", "selectOption"),
				AssetRepository.getInstance().getTextureRegion("widgets", "selectOption_pressed"), false, 
				new PlaceViewerButtonDelegate() {					
					@Override
					public void doAction() {						
						notifyMoveToOtherPlace(place.getIdPlaceToGoIfNoSpell());
					}
				}, false);
	}
	
	private void addSpellUseRow(final SpellUsePlaceToGo spellPlaceToGo) {
		TextureRegion spellTexture = AssetRepository.getInstance().getTextureRegion("spells", spellPlaceToGo.getSpell().getImageName()); 
		boolean canCastSpell = character != null && character.canCastSpell(spellPlaceToGo.getSpell());
		TextureRegion spellTexturePressed = character != null && canCastSpell
				? ImageManipulationHelper.moveTextureRegionForPressed(spellTexture) : null;
		
		addOptionRow(getSpellTexts(spellPlaceToGo.getSpell()), spellTexture, spellTexturePressed, !canCastSpell, 
				new PlaceViewerButtonDelegate() {					
					@Override
					public void doAction() {
						character.setCurrentPowerPoints(character.getCurrentPowerPoints() - spellPlaceToGo.getSpell().getPowerPointsNeeded());
						notifyMoveToOtherPlace(spellPlaceToGo);
					}
				}, false);
	}
	
	private String [] getSpellTexts(Spell spell) {
		String [] texts = new String [] {
			String.format(Localization.getInstance().getTranslation("InGame", "castSpell", false), spell.getName()), ""
		};
		
		if(character == null) {
			texts[1] = Localization.getInstance().getTranslation("InGame", "spellNotLearnt", false);
			return texts;
		} 
		
		if(!character.hasLearntSpell(spell)) {
			texts[1] = Localization.getInstance().getTranslation("InGame", "spellNotLearnt", false);
		} else if (!character.hasEnoughPowerPointsToCastSpell(spell)) {
			texts[1] = String.format(Localization.getInstance().getTranslation("InGame", "spellNotEnoughPowerPoints", false), 
					spell.getPowerPointsNeeded());
		} else {
			texts[1] = String.format(Localization.getInstance().getTranslation("InGame", "spellPowerPointsCost", false), 
					spell.getPowerPointsNeeded());
		}
		
		return texts;
	}

	@SuppressWarnings("rawtypes")
	private void showPlaceInfo(EndAdventurePlace place) {
		if(place.isPlayerDead() || place.isCompleteVictory()) {
			String imageName = place.isCompleteVictory() ? "chest" : "death";
			Image deathImage = new Image(AssetRepository.getInstance().getTextureRegion("illustrations", imageName));
			add(deathImage).expandX().colspan(2).center().pad(paddingWidth);
			row();
		}

		ExtendedImageButton<Label> endButton =  StageScreenHelper.createLabelImageButton(
				Localization.getInstance().getTranslation("InGame", "endAdventure"), this,
				AssetRepository.getInstance().getSound("click_button"));
		endButton.addListener(new ChangeListener() {
		   	@Override
			public void changed(ChangeEvent event, Actor actor) {		   		
		   		for(PlaceViewerEventsListener listener:listeners) {
					listener.endAdventure();
				}
			}
		});	
		Array<Cell> cells = getCells();
		cells.get(cells.size - 1).expandX().colspan(2).center().pad(paddingWidth);
		
		row();
	}

	private void showPlaceInfo(EffectPlace place) {

		if(place.getEffects().size() > 0) {
			notifyEffectSuffered(place.getEffects().get(0));
		}

		if(character != null && character.getCurrentLifePoints() <= 0 && place.getPlaceToGoIfDie() != null)
		{
			final OptionChoosePlaceCanGo optionPlace = place.getPlaceToGoIfDie();
			addOptionRow( new String[] { optionPlace.getTranslatedText() },
				AssetRepository.getInstance().getTextureRegion("widgets", "selectOption"),
				AssetRepository.getInstance().getTextureRegion("widgets", "selectOption_pressed"),
				false, new PlaceViewerButtonDelegate() {

					@Override
					public void doAction() {
						notifyMoveToOtherPlace(optionPlace.getIdPlaceToGo());
					}
			}, false);
			return;
		}

		if(place.getPlacesToGo() != null) {
			for(final OptionChoosePlaceCanGo optionPlace:place.getPlacesToGo()) {
				addOptionRow( new String[] { optionPlace.getTranslatedText() },
						AssetRepository.getInstance().getTextureRegion("widgets", "selectOption"),
						AssetRepository.getInstance().getTextureRegion("widgets", "selectOption_pressed"),
						false, new PlaceViewerButtonDelegate() {

							@Override
							public void doAction() {
								notifyMoveToOtherPlace(optionPlace.getIdPlaceToGo());
							}
						}, false);
			}
		}
	}
	
	private void addOptionRow(String[] texts, TextureRegion image, TextureRegion imagePressed, boolean imageBlurred, 
			PlaceViewerButtonDelegate buttonDelegate, boolean addCounterControl) {
		Label optionLabel = createOptionLabel(texts);
		listOptionLabels.add(optionLabel);
		ExtendedImageButton<PlaceViewerButtonDelegate> imageButton = createOptionImageButton(image, imagePressed, buttonDelegate);
		if(imageBlurred) {
			ImageManipulationHelper.setActorSemiTransparent(imageButton);
		} else {
			StageScreenHelper.addOnClickSound(imageButton, AssetRepository.getInstance().getSound("pages"));
			imageButton.addListener(new ChangeListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					ExtendedImageButton<PlaceViewerButtonDelegate> imageButton = (ExtendedImageButton<PlaceViewerButtonDelegate>) actor;
					imageButton.getTag().doAction();
				}
			});
		}
				
		float imageButtonCellWidth = imageButton.getWidth() * 1.2f;
		float bottomPadding = imageButton.getHeight() * 0.1f;
				
		add(optionLabel).center().expandX().padBottom(bottomPadding).padLeft(paddingWidth);
		if(addCounterControl) {
			Stack stack = new Stack();
			stack.add(imageButton);

			counterControl.resetCount();
			Table deleteGameButtonTable = new Table();
			deleteGameButtonTable.add(counterControl);
			deleteGameButtonTable.right().top();
			stack.add(deleteGameButtonTable);

			add(stack).center().width(imageButtonCellWidth).padBottom(bottomPadding).padRight(paddingWidth);
		} else {
			add(imageButton).center().width(imageButtonCellWidth).padBottom(bottomPadding).padRight(paddingWidth);
		}
		
		row();
	}
	
	private Label createOptionLabel(String []texts){
		return createOptionLabel(join(texts, "\n"));
	}
	
	static private String join(String [] list, String conjunction)
	{
	   StringBuilder sb = new StringBuilder();
	   boolean first = true;
	   for (String item : list)
	   {
	      if (first)
	         first = false;
	      else
	         sb.append(conjunction);
	      sb.append(item);
	   }
	   return sb.toString();
	}
	
	
	private Label createOptionLabel(String text) {
		Label label = new Label(text, getOptionsLabelStyle());
		label.setAlignment(Align.center);
		label.setWrap(true);
				
		return label;
	}
	
	private ExtendedImageButton<PlaceViewerButtonDelegate> createOptionImageButton(TextureRegion image, TextureRegion imagePressed, PlaceViewerButtonDelegate buttonDelegate) {
		ExtendedImageButton<PlaceViewerButtonDelegate> imageButton = imagePressed == null 
				? new ExtendedImageButton<PlaceViewerButtonDelegate>(new TextureRegionDrawable(image))
				: new ExtendedImageButton<PlaceViewerButtonDelegate>(new TextureRegionDrawable(image), 
						new TextureRegionDrawable(imagePressed));
		imageButton.setTag(buttonDelegate);
		
		return imageButton;
	}
	
	private Label.LabelStyle getMainTextLabelStyle()
	{
		return new Label.LabelStyle(FontHelper.getReadTextFont(), Color.BLACK);
	}
	
	private Label.LabelStyle getOptionsLabelStyle()
	{
		 return new Label.LabelStyle(FontHelper.getOptionsTextFont(), Color.BLACK);
	}

	public void setAllocatedLuckPoints(int luckPoints) {
		counterControl.setCountValue(luckPoints);
	}

	public int getAllocatedLuckPoints() {
		return counterControl.getCountValue();
	}

	@Override
	public void refreshLocalizableItems() {
		if(currentPlace != null) {
			//TODO Replace idAventure by the right idAdventure once more than one adventures implemented
			currentPlace = AdventureManager.getInstance().getPlace(-1, currentPlace.getId());
			showPlace(currentPlace);
		}
	}
}
