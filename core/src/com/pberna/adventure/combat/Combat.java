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

package com.pberna.adventure.combat;

import java.util.ArrayList;
import com.pberna.adventure.pj.Character;
import com.pberna.adventure.pj.Enemy;

public class Combat {
	private CombatState combatState;
	private RoundState roundState;
	private int round;	
	private Turn firstTurn;
	private Turn secondTurn;
	private Character character;
	private Enemy enemy;
	private boolean characterUsedMeleeInCombat;
	
	private ArrayList<CombatEventsListener> listeners;
	
	public Combat() {
		combatState = CombatState.NotStarted;
		roundState = RoundState.RollingInitiative;
		round = 0;
		firstTurn = Turn.Character;
		secondTurn = Turn.Enemy;
		character = null;
		enemy = null;
		characterUsedMeleeInCombat = false;
		listeners = new ArrayList<CombatEventsListener>();
	}
	
	public CombatState getCombatState() {
		return combatState;
	}
	
	public RoundState getRoundState() {
		return roundState;
	}
	
	public int getRound() {
		return round;
	}
	
	public Turn firstTurn() {
		return firstTurn;
	}
	
	public Turn secondTurn() {
		return secondTurn;
	}
		
	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public boolean isCharacterUsedMeleeInCombat (){
		return characterUsedMeleeInCombat;
	}
		
	public void addListener(CombatEventsListener listener) {
		listeners.add(listener);
	}

	public void startCombat(boolean getAway) {
		if(getAway) {
			combatState = CombatState.RunAway;
			roundState = RoundState.BeforeAttackRunAway;
		} else {
			combatState = CombatState.NotStarted;
			roundState = RoundState.RollingInitiative;
		}

		startCombat();
	}

	private void startCombat() {
		if( character == null || enemy == null) {
			return;
		}
		
		nextState();
	}
	
	private void nextState() {
		if(character.getCurrentLifePoints() == 0 || enemy.getCurrentLifePoints() == 0) {
			//end combat
			combatState = CombatState.Finished;
			roundState = RoundState.EndRound;
			for(CombatEventsListener listener:listeners) {
				listener.combatFinished();
			}
			return;
		}
		
		if(combatState == CombatState.NotStarted) {
			combatState = CombatState.Fighting;
			roundState = RoundState.RollingInitiative;
			round = 1;
			characterUsedMeleeInCombat = false;
			
			for(CombatEventsListener listener:listeners) {
				listener.rollingInitiative();
			}
			return;
		}
		
		if(combatState == CombatState.Fighting) {
			switch(roundState) {
				case RollingInitiative:
					roundState = RoundState.FirstTurn;
					for(CombatEventsListener listener:listeners) {
						if(firstTurn == Turn.Character) {
							listener.characterTurn();
						} else {
							listener.enemyTurn();
						}
					}
					break;
					
				case FirstTurn:
					roundState = RoundState.SecondTurn;
					for(CombatEventsListener listener:listeners) {
						if(secondTurn == Turn.Character) {
							listener.characterTurn();
						} else {
							listener.enemyTurn();
						}
					}
					break;
					
				case SecondTurn:
					roundState = RoundState.EndRound;
					for(CombatEventsListener listener:listeners) {
						listener.endRound();
					}
					break;
					
				case EndRound:	
					round++;
					roundState = RoundState.RollingInitiative;
					for(CombatEventsListener listener:listeners) {
						listener.rollingInitiative();
					}
					break;
					
				default:
					break;
			}
		}

		if(combatState == CombatState.RunAway) {
			switch (roundState) {
				case BeforeAttackRunAway:
					roundState = RoundState.AfterAttackRunAway;
					for(CombatEventsListener listener:listeners) {
						listener.enemyTurn();
					}
					break;

				case AfterAttackRunAway:
					for(CombatEventsListener listener:listeners) {
						listener.successfulRunAway();
					}
					break;

				default:
					break;
			}
		}
	}
		
	public void applyInitiativeRoll(int characterInitiative, int enemyInitiative) {
		if(combatState != CombatState.Fighting && roundState != RoundState.RollingInitiative) {
			return;
		}

		if(characterInitiative >= enemyInitiative) {
			firstTurn = Turn.Character;
			secondTurn = Turn.Enemy;
		} else {
			firstTurn = Turn.Enemy;
			secondTurn = Turn.Character;
		}
		
		nextState();
	}

	public void applyAttackRoll(int attackValue, int defenseValue, float damageMultiplier, boolean isMeleeAttack) {
		if((combatState != CombatState.Fighting && (roundState != RoundState.FirstTurn && roundState != RoundState.SecondTurn)) &&
				(combatState != CombatState.RunAway && roundState != RoundState.BeforeAttackRunAway	)){
			return;
		}

		int damage = attackValue - defenseValue;
		if(damage > 0) {
			damage = Math.max(1, Math.round(damage * damageMultiplier));
		}
		
		if(damage > 0) {
			applyDamageToDefender(damage);
			for(CombatEventsListener listener: listeners) {
				listener.defenderSuffersDamage(damage);
			}
		}
		if(isMeleeAttack && getCurrentTurn() == Turn.Character) {
			characterUsedMeleeInCombat = true;
		}
		nextState();
	}
	
	private void applyDamageToDefender(int damage) {
		if(combatState == CombatState.RunAway) {
			character.applyDamage(damage);
			return;
		}

		if(roundState == RoundState.FirstTurn) {
			if(firstTurn == Turn.Character) { 
				enemy.applyDamage(damage);
			} else {
				character.applyDamage(damage);
			}
		} else {
			if(secondTurn == Turn.Character) { 
				enemy.applyDamage(damage);
			} else {
				character.applyDamage(damage);
			}
		}		
	}
	
	
	public CombatWinner getCombatWinner() {
		if(character.getCurrentLifePoints() > 0 && enemy.getCurrentLifePoints() <= 0 ) {
			return CombatWinner.Character;
		}
		
		if(character.getCurrentLifePoints() <= 0 && enemy.getCurrentLifePoints() > 0 ) {
			return CombatWinner.Enemy;
		}
		
		return CombatWinner.None;
	}
	
	public Turn getCurrentTurn() {
		if(combatState == CombatState.Fighting && roundState == RoundState.FirstTurn) {
			return firstTurn;
		}
		
		if(combatState == CombatState.Fighting && roundState == RoundState.SecondTurn) {
			return secondTurn;
		}

		if(combatState == CombatState.RunAway) {
			return Turn.Enemy;
		}
		
		return null;
	}
	
	public void nextTurnOrRound() {
		nextState();
	}

	public void resetCombat() {
		combatState = CombatState.NotStarted;
		roundState = RoundState.RollingInitiative;
		round = 0;
		firstTurn = Turn.Character;
		secondTurn = Turn.Enemy;
		characterUsedMeleeInCombat = false;
	}

	public void tryToRunAway() {
		combatState = CombatState.RunAway;
		roundState = RoundState.BeforeAttackRunAway;
		nextState();
	}
}
