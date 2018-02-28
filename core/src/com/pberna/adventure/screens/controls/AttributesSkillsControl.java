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

package com.pberna.adventure.screens.controls;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Skill;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.localization.ILocalizable;
import com.pberna.engine.localization.Localization;
import com.pberna.engine.screens.IModelEditingScreen;
import com.pberna.adventure.screens.StageScreenHelper;
import com.pberna.adventure.pj.Character;

public class AttributesSkillsControl extends Table implements IModelEditingScreen<Character>, ILocalizable{
	
	private Label attributesTitleLabel;
	private Label attributesTitleDecorationLabel;
	private Label skillsTitleLabel;
	private Label skillsTitleDecorationLabel;
	private ArrayList<AttributeSkillInfoControl> attributesControls;
	private ArrayList<AttributeSkillInfoControl> skillsControls;
	private AttributeSkillInfoControl spellsControl;
	private Label combatTitleLabel;
	private AttributeSkillInfoControl attackControl;
	private AttributeSkillInfoControl defenseControl;
	
	private Character character;
	
	public AttributesSkillsControl() {		
		character = null;
		attributesControls = new ArrayList<AttributeSkillInfoControl>();
		skillsControls = new ArrayList<AttributeSkillInfoControl>();
		spellsControl = createSpellCountControl(0);
		createTitlesRow();
		createAttributesSkillsRow();
		createCombatTitleRow();
		createAttackDefenseRow();
	}

	private void createTitlesRow() {
		//attributes label		
		attributesTitleLabel = StageScreenHelper.createCenteredLabel(Localization.getInstance().
				getTranslation("PjInformation", "attributes"), getLabelStyle());
		attributesTitleDecorationLabel = StageScreenHelper.createCenteredLabel(StageScreenHelper.getDecorationText(
				attributesTitleLabel.getText().length + 3), StageScreenHelper.getDecorationLabelStyle());
		Stack stackAttributesLabel = StageScreenHelper.createStackWithActors(attributesTitleLabel, attributesTitleDecorationLabel);
		add(stackAttributesLabel).center().expandX();
				
		//skills title label
		skillsTitleLabel = StageScreenHelper.createCenteredLabel(Localization.getInstance().
				getTranslation("PjInformation", "skills"), getLabelStyle());
		skillsTitleDecorationLabel = StageScreenHelper.createCenteredLabel(StageScreenHelper.getDecorationText(
				skillsTitleLabel.getText().length + 2), StageScreenHelper.getDecorationLabelStyle());
		Stack stackSkillsLabel = StageScreenHelper.createStackWithActors(skillsTitleLabel, skillsTitleDecorationLabel);
		add(stackSkillsLabel).center().top().expandX();
		
		row();
	}	

	private Label.LabelStyle getLabelStyle() {
		return StageScreenHelper.getLabelStyle(Color.BLACK);
	}

	private void createAttributesSkillsRow() {
		ArrayList<Attribute> attributes = Attribute.getAttributes();
		ArrayList<Skill> skills = Skill.getSkills();
		int numRows = Math.max(attributes.size(), skills.size());		
		boolean spellControlAdded = false;
		
		for(int i = 0; i < numRows; i++) {
			if(i < attributes.size()) {
				Attribute attribute = attributes.get(i);
				AttributeSkillInfoControl control = createAttributeSkillControl(attribute);
				attributesControls.add(control);
				add(control).center().expand();
			} else {
				if(!spellControlAdded) {
					add(spellsControl).center().expand();
					spellControlAdded = true;
				} else {
					add(StageScreenHelper.createCenteredLabel("", getLabelStyle())).center().expand();
				}
			}
			if(i < skills.size()) {
				Skill skill = skills.get(i);
				AttributeSkillInfoControl control = createAttributeSkillControl(skill);
				skillsControls.add(control);
				add(control).center().expand();
			} else {
				if(!spellControlAdded) {
					add(spellsControl).center().expand();
					spellControlAdded = true;
				} else {
					add(StageScreenHelper.createCenteredLabel("", getLabelStyle())).center().expand();
				}
			}
			
			row();
		}		
		if(!spellControlAdded) {
			add(spellsControl).center().expand();
			row();
		}
	}
	
	private AttributeSkillInfoControl createAttributeSkillControl(Attribute attribute) {
		AttributeSkillInfoControl control = new AttributeSkillInfoControl(attribute);
		setProperties(control, attribute.getName());
		return control;
	}
	
	private AttributeSkillInfoControl createAttributeSkillControl(Skill skill) {
		AttributeSkillInfoControl control = new AttributeSkillInfoControl(skill);
		setProperties(control, skill.getName());
		return control;
	}
	
	private AttributeSkillInfoControl createSpellCountControl(Integer spellsCount) {
		AttributeSkillInfoControl control = new AttributeSkillInfoControl(new IImageName() {
			@Override
			public String getImageName() {
				return "spells";
			}

			@Override
			public IImageName setImageName(String ImageName) {
				return this;
			}

			@Override
			public int getId() {
				return 0;
			}


		});
		setProperties(control, Localization.getInstance().getTranslation("PjCreation", "spells"));
		return control;
	}
	
	@SuppressWarnings("rawtypes")
	private void setProperties(AttributeSkillInfoControl control, String mainText) {
		control.setMainText(mainText);
		control.setMainValue(0);
		control.setModifierValue(0);
	}
	
	private void createCombatTitleRow() {
		//attributes label		
		combatTitleLabel = StageScreenHelper.createCenteredLabel(Localization.getInstance().
				getTranslation("PjCharacterSheet", "combat"), getLabelStyle());
		add(combatTitleLabel).center().expandX().colspan(2);
		row();
	}
	
	private void createAttackDefenseRow() {
		attackControl = createAttackDefenseControl(Localization.getInstance().
				getTranslation("PjCharacterSheet", "attack"), "attack");
		defenseControl = createAttackDefenseControl(Localization.getInstance().
				getTranslation("PjCharacterSheet", "defense"), "defense");
		
		add(attackControl).center().expand();
		add(defenseControl).center().expand();
		row();
	}

	private AttributeSkillInfoControl createAttackDefenseControl(String text, final String imageName) {
		AttributeSkillInfoControl control = new AttributeSkillInfoControl(new IImageName() {
			@Override
			public String getImageName() {
				return imageName;
			}

			@Override
			public IImageName setImageName(String ImageName) {
				return this;
			}

			@Override
			public int getId() {
				return 0;
			}
		});
		setProperties(control, text);
		return control;
	}

	@Override
	public void setModel(Character model) {
		character = model;
		refreshControls();
	}	

	@Override
	public Character getModel() {
		return character;
	}
	
	private void refreshControls() {
		if(character == null) {
			return;
		}
		
		for(Attribute attribute: character.getAttributes()) {
			for(AttributeSkillInfoControl control: attributesControls) {
				if(control.getTag().getId() == attribute.getId()) {
					control.setMainValue(character.getBaseAttributeValue(attribute.getId()));
					control.setModifierValue(character.getModifierAttributeValue(attribute.getId()));
					break;
				}
			}
		}
		
		for(Skill skill: character.getSkills()) {
			for(AttributeSkillInfoControl control: skillsControls) {
				if(control.getTag().getId() == skill.getId()) {
					control.setMainValue(character.getBaseSkillValue(skill.getId()));
					control.setModifierValue(character.getModifierSkillValue(skill.getId()));
					break;
				}
			}
		}
		
		spellsControl.setMainValue(character.getSpells().size());
		
		attackControl.setMainValue(character.getBaseAttackValue());
		attackControl.setModifierValue(character.getModifierAttackValue());
		defenseControl.setMainValue(character.getBaseDefenseValue());
		defenseControl.setModifierValue(character.getModifierDefenseValue());		
	}

	@Override
	public void refreshLocalizableItems() {
		attributesTitleLabel.setText(Localization.getInstance().getTranslation("PjInformation", "attributes"));
		attributesTitleDecorationLabel.setText(StageScreenHelper.getDecorationText(attributesTitleLabel.getText().length + 3));
		skillsTitleLabel.setText(Localization.getInstance().getTranslation("PjInformation", "skills"));
		skillsTitleDecorationLabel.setText(StageScreenHelper.getDecorationText(skillsTitleLabel.getText().length + 2));
		localizeAttributesControls();
		localizeSkillsControls();
		spellsControl.setMainText(Localization.getInstance().getTranslation("PjCreation", "spells"));
		combatTitleLabel.setText(Localization.getInstance().getTranslation("PjCharacterSheet", "combat"));
		attackControl.setMainText(Localization.getInstance().getTranslation("PjCharacterSheet", "attack"));
		defenseControl.setMainText(Localization.getInstance().getTranslation("PjCharacterSheet", "defense"));
	}

	private void localizeAttributesControls() {
		ArrayList<Attribute> localizedAttributes = Attribute.getAttributes();
		
		for(AttributeSkillInfoControl control: attributesControls) {
			Attribute localizedAttribute = Attribute.findAttribute(localizedAttributes, control.getTag().getId());
			if(localizedAttribute != null) {
				control.setMainText(localizedAttribute.getName());
			}
		}		
	}

	private void localizeSkillsControls() {
		ArrayList<Skill> localizedSkills = Skill.getSkills();
		
		for(AttributeSkillInfoControl control: skillsControls) {
			Skill localizedSkill = Skill.findSkill(localizedSkills, control.getTag().getId());
			if(localizedSkill != null) {
				control.setMainText(localizedSkill.getName());
			}
		}
	}
}
