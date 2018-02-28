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

package com.pberna.adventure.pj;

import java.util.ArrayList;

import com.pberna.adventure.screens.controls.IImageName;
import com.pberna.engine.localization.Localization;

public class Skill implements IImageName {
	public static final int MinimumGenericValue = 1;
	public static final int MaximumGenericValue = 8;
	
	public static final int TotalSkillPointsPerPj = 20;
	
	public static final int IdFighting = 1;
	public static final int IdKnowledge = 2;
	public static final int IdStealth = 3;
	public static final int IdMagic = 4;
	public static final int IdSocial = 5;
	
	private int id;
	private String name;
	private int value;
	private String description;
	private String imageName;
	
	public int getId() {
		return id;
	}

	public Skill setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Skill setName(String name) {
		this.name = name;
		return this;
	}

	public int getValue() {
		return value;
	}

	public Skill setValue(int value) {
		this.value = value;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Skill setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getImageName() {
		return imageName;
	}

	public Skill setImageName(String imageName) {
		this.imageName = imageName;
		return this;
	}
	
	public static ArrayList<Skill> getSkills()
	{
		ArrayList<Skill> listSkills = new ArrayList<Skill>();
		
		//Fighting
		listSkills.add(new Skill()
			.setId(IdFighting)
			.setName(Localization.getInstance().getTranslation("PjInformation", "skillFighting"))
			.setValue(MinimumGenericValue)
			.setDescription(Localization.getInstance().getTranslation("PjInformation", "descriptionSkillFighting"))
			.setImageName("combat"));
		
		//Knowledge
		listSkills.add(new Skill()
			.setId(IdKnowledge)
			.setName(Localization.getInstance().getTranslation("PjInformation", "skillKnowledge"))
			.setValue(MinimumGenericValue)
			.setDescription(Localization.getInstance().getTranslation("PjInformation", "descriptionSkillKnowledge"))
			.setImageName("knowledge"));
		
		//Stealth
		listSkills.add(new Skill()
			.setId(IdStealth)
			.setName(Localization.getInstance().getTranslation("PjInformation", "skillStealth"))
			.setValue(MinimumGenericValue)
			.setDescription(Localization.getInstance().getTranslation("PjInformation", "descriptionSkillStealth"))
			.setImageName("stealth"));
				
		//Magic
		listSkills.add(new Skill()
			.setId(IdMagic)
			.setName(Localization.getInstance().getTranslation("PjInformation", "skillMagic"))
			.setValue(MinimumGenericValue)
			.setDescription(Localization.getInstance().getTranslation("PjInformation", "descriptionSkillMagic"))
			.setImageName("magic"));
		
		//Social
		listSkills.add(new Skill()
			.setId(IdSocial)
			.setName(Localization.getInstance().getTranslation("PjInformation", "skillSocial"))
			.setValue(MinimumGenericValue)
			.setDescription(Localization.getInstance().getTranslation("PjInformation", "descriptionSkillSocial"))
			.setImageName("social"));
		
		return listSkills;
	}

	public static Skill findSkill(ArrayList<Skill> listSkills, int idSkill) {
		for(Skill skill: listSkills) {
			if(skill.id == idSkill) {
				return skill;
			}
		}
		
		return null;
	}
}
