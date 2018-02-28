package com.pberna.adventure.adventure;
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

import com.pberna.adventure.AdventureBetaHelper;
import com.pberna.adventure.places.Place;

public class AdventureManager {
	private static final AdventureManager instance  = new AdventureManager();
	
	private AdventureManager() {
		
	}
	
	public static AdventureManager getInstance() {
		return instance;
	}
	
	public Place getStartPlace(int idAventure) {
		return AdventureBetaHelper.getStartPlace();
	}
	
	public Place getPlace(int idAventure, int idPlace) {
		switch(idPlace){
			case 1:
				return AdventureBetaHelper.getStartPlace();
			case 2:
				return AdventureBetaHelper.getPlace2();
			case 3:
				return AdventureBetaHelper.getPlace3();
			case 4:
				return AdventureBetaHelper.getPlace4();
			case 5:
				return AdventureBetaHelper.getPlace5();
			case 6:
				return AdventureBetaHelper.getPlace6();
			case 7:
				return AdventureBetaHelper.getPlace7();
			case 8:
				return AdventureBetaHelper.getPlace8();
			case 9:
				return AdventureBetaHelper.getPlace9();
			case 10:
				return AdventureBetaHelper.getPlace10();
			case 11:
				return AdventureBetaHelper.getPlace11();
			case 12:
				return AdventureBetaHelper.getPlace12();
			case 13:
				return AdventureBetaHelper.getPlace13();
			case 14:
				return AdventureBetaHelper.getPlace14();
			case 15:
				return AdventureBetaHelper.getPlace15();
			case 16:
				return AdventureBetaHelper.getPlace16();
			case 17:
				return AdventureBetaHelper.getPlace17();
			case 18:
				return AdventureBetaHelper.getPlace18();
			case 19:
				return AdventureBetaHelper.getPlace19();
			case 20:
				return AdventureBetaHelper.getPlace20();
			case 21:
				return AdventureBetaHelper.getPlace21();
			case 22:
				return AdventureBetaHelper.getPlace22();
			case 23:
				return AdventureBetaHelper.getPlace23();
			case 24:
				return AdventureBetaHelper.getPlace24();
			case 25:
				return AdventureBetaHelper.getPlace25();
			case 26:
				return AdventureBetaHelper.getPlace26();
			case 27:
				return AdventureBetaHelper.getPlace27();
			case 28:
				return AdventureBetaHelper.getPlace28();
			case 29:
				return AdventureBetaHelper.getPlace29();
			case 30:
				return AdventureBetaHelper.getPlace30();
			case 31:
				return AdventureBetaHelper.getPlace31();
			case 32:
				return AdventureBetaHelper.getPlace32();
			case 33:
				return AdventureBetaHelper.getPlace33();
			case 34:
				return AdventureBetaHelper.getPlace34();
			case 35:
				return AdventureBetaHelper.getPlace35();

			default:
				return AdventureBetaHelper.getPlace32();
		}
	}
}
