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

package com.pberna.adventure.places;

import com.pberna.adventure.pj.Enemy;

public class CombatPlace extends Place{
	private int idPlaceToGoIfWin;
	private int idPlaceToGoIfLose;
	private Integer idPlaceToGoIfRunAway;
	private Enemy enemy;
	
	public int getIdPlaceToGoIfWin() {
		return idPlaceToGoIfWin;
	}
	public void setIdPlaceToGoIfWin(int idPlaceToGoIfWin) {
		this.idPlaceToGoIfWin = idPlaceToGoIfWin;
	}
	
	public int getIdPlaceToGoIfLose() {
		return idPlaceToGoIfLose;
	}
	public void setIdPlaceToGoIfLose(int idPlaceToGoIfLose) {
		this.idPlaceToGoIfLose = idPlaceToGoIfLose;
	}
	
	public Integer getIdPlaceToGoIfRunAway() {
		return idPlaceToGoIfRunAway;
	}
	public void setIdPlaceToGoIfRunAway(Integer idPlaceToGoIfRunAway) {
		this.idPlaceToGoIfRunAway = idPlaceToGoIfRunAway;
	}
	
	public Enemy getEnemy() {
		return enemy;
	}
	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}
	
	public boolean canRunAway() {
		return idPlaceToGoIfRunAway != null && idPlaceToGoIfRunAway > 0; 
	}
}
