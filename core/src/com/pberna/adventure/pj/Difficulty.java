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

import com.pberna.engine.localization.Localization;

public class Difficulty {
	
	private static final int [] DifficultiesValues = {2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
	private static final String [] DifficultiesLabel = {
			"difficultyAnyone", "difficultyVeryEasy", "difficultyEasy", "difficultyRoutine", "difficultyComplicatedEnough",
			"difficultyComplicated", "difficultyVeryComplicated", "difficultySomeHard", "difficultyHard", "difficultyVeryHard",
			"difficultyReallyHard", "difficultyTemerity", "difficultyMadness", "difficultyRidiculous", "difficultyImpossible"};

	private int difficulty;
	private String name;
	
	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Difficulty(int difficulty, String name) {
		this.difficulty = difficulty;
		this.name = name;
	}
	
	public static ArrayList<Difficulty> getAllDifficulties(boolean adjustBlankSpaces)
	{
		ArrayList<Difficulty> difficulties = new ArrayList<Difficulty>(DifficultiesValues.length);
		for(int i = 0; i < DifficultiesValues.length && i < DifficultiesLabel.length; i++) {
			difficulties.add(buildDifficulty(DifficultiesValues[i], DifficultiesLabel[i], adjustBlankSpaces));
		}

		return difficulties;
	}

	private static Difficulty buildDifficulty(int difficultyValue, String labelKey, boolean adjustBlankSpaces) {
		return new Difficulty(difficultyValue, Localization.getInstance().getTranslation("PjInformation", labelKey, adjustBlankSpaces));
	}

	public static Difficulty getDifficulty(int difficultyValue, boolean adjustBlankSpaces) {
		for(int i = 0; i < DifficultiesValues.length && i < DifficultiesLabel.length; i++) {
			if(DifficultiesValues[i] == difficultyValue) {
				return buildDifficulty(DifficultiesValues[i], DifficultiesLabel[i], adjustBlankSpaces);
			}
		}
		return null;
	}
	
	public static Difficulty findDifficulty(ArrayList<Difficulty> listDifficulties, int difficultyValue) {
		
		for(Difficulty difficulty: listDifficulties) {
			if(difficulty.getDifficulty() == difficultyValue) {
				return difficulty;
			}
		}
		
		return null;
	}
}
