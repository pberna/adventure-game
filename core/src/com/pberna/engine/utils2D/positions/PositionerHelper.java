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

package com.pberna.engine.utils2D.positions;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class PositionerHelper {
	
	private PositionerHelper()
	{		
	}
	
	public static void setPositionFromCorner(Actor actor, Corner corner,
			float x, float y, Stage stage)
	{
		setPositionFromCorner(actor, corner, x, y , 0, 0, 
				stage.getWidth(), stage.getHeight());
	}
	
	public static void setPositionFromCorner(Actor actor, Corner corner,
			float x, float y, Rectangle container)
	{
		setPositionFromCorner(actor, corner, x, y , container.getX(), container.getY(), 
				container.getWidth(), container.getHeight());
	}
	
	private static void setPositionFromCorner(Actor actor, Corner corner,
			float x, float y, float containerX, float containerY, 
			float containerWidth, float containerHeight)
	{
		switch(corner)
		{
			case BottomLeft:
				actor.setPosition(x + containerX, y + containerY);
				break;
			case BottomRight:
				actor.setPosition(containerWidth - actor.getWidth() - x + containerX, y + containerY);
				break;
			case TopLeft:
				actor.setPosition(x + containerX, containerHeight - actor.getHeight() - y + containerY);
				break;
			case TopRight:
				actor.setPosition(containerWidth - actor.getWidth() - x + containerX, 
						containerHeight - actor.getHeight() - y + containerY);
				break;
			default:
				actor.setPosition(containerX, containerY);
		}
	}
	
	public static float setPositionCenteredHorizontal(Actor actor, VerticalSide side,
			float y, Stage stage)
	{
		return setPositionCenteredHorizontal(actor, side, y, 0, 0, 
				stage.getWidth(), stage.getHeight());
	}
	
	public static float setPositionCenteredHorizontal(Actor actor, VerticalSide side,
			float y, Rectangle container)
	{
		return setPositionCenteredHorizontal(actor, side, y, container.getX(), container.getY(), 
				container.getWidth(), container.getHeight());
	}
	
	private static float setPositionCenteredHorizontal(Actor actor, VerticalSide side,
			float y, float containerX, float containerY, float containerWidth, float containerHeight)
	{
		float x = (containerWidth - actor.getWidth()) / 2;
		switch(side)
		{
			case Bottom:
				actor.setPosition(x + containerX, y + containerY);
				return y + actor.getHeight();
			case Top:				
				actor.setPosition(x + containerX, containerHeight - actor.getHeight() - y + containerY);				
				return containerHeight - actor.getY(); 
			default:
				actor.setPosition(containerX, containerY);
				return actor.getHeight();
		}	
		
	}
	
	public static float setPositionCenteredVertical(Actor actor, HorizontalSide side,
			float x, Stage stage)
	{
		return setPositionCenteredVertical(actor, side, x, 0, 0, 
				stage.getWidth(), stage.getHeight());
	}
	
	public static float setPositionCenteredVertical(Actor actor, HorizontalSide side,
			float x, Rectangle container)
	{
		return setPositionCenteredVertical(actor, side, x, container.getX(), container.getY(), 
				container.getWidth(), container.getHeight());
	}

	public static float setPositionCenteredVertical(Actor actor, HorizontalSide side,
													float x, Actor container)
	{
		return setPositionCenteredVertical(actor, side, x, container.getX(), container.getY(),
				container.getWidth(), container.getHeight());
	}
	
	private static float setPositionCenteredVertical(Actor actor, HorizontalSide side,
			float x, float containerX, float containerY, float containerWidth, float containerHeight)
	{
		float y = (containerHeight - actor.getHeight()) / 2;
		switch(side)
		{
			case Left:
				actor.setPosition(x + containerX, y + containerY);			
				return x + actor.getWidth();
			case Right:
				actor.setPosition(containerWidth - actor.getWidth() - x + containerX, y + containerY);
				return containerWidth - actor.getX();				
			default:
				actor.setPosition(containerX, containerY);
				return actor.getWidth();
		}
	}
	
	public static void setPositionCentered(Actor actor, Stage stage) 			
	{
		setPositionCentered(actor, 0, 0, stage.getWidth(), stage.getHeight());		
	}
	
	public static void setPositionCentered(Actor actor, Rectangle container) 			
	{
		setPositionCentered(actor, container.getX(), container.getY(), 
				container.getWidth(), container.getHeight());		
	}
	public static void setPositionCenteredHorizontalFillingColumn(Actor [] actors, Stage stage, VerticalSide startSide) {
		Rectangle rectangle = new Rectangle(0, 0, stage.getWidth(), stage.getHeight());
		setPositionCenteredHorizontalFillingColumn(actors, rectangle, startSide);
	}

	public static void setPositionCenteredHorizontalFillingColumn(Actor [] actors, Rectangle container, VerticalSide startSide) {
		float gapSize = 0;
		if(actors.length > 0) {
			float totalHeight = 0;
			float totalActors = 0;
			for(Actor actor:actors) {
				totalHeight += actor.getHeight();
				totalActors += 1;
			}
			gapSize = (container.getHeight() - totalHeight) / (totalActors + 1.0f);
		}
		setPositionCenteredHorizontalFillingColumn(actors, container, startSide, gapSize);
	}
	
	public static void setPositionCenteredHorizontalFillingColumn(Actor [] actors, Rectangle container, VerticalSide startSide, float gapSize) {
		float y = gapSize;
		for(int i = 0; i < actors.length; i++) {
			setPositionCenteredHorizontal(actors[i], startSide, y, container);
			y += actors[i].getHeight() + gapSize;
		}
	}
	
	public static void setPositionCenteredVerticalFillingRow(Actor [] actors, Rectangle container, HorizontalSide startSide) {
		float gapSize = 0;
		if(actors.length > 0) {
			float totalWidth = 0;
			float totalActors = 0;
			for(Actor actor:actors) {
				totalWidth += actor.getWidth();
				totalActors += 1;
			}
			gapSize = (container.getWidth() - totalWidth) / (totalActors + 1.0f);
		}
		setPositionCenteredVerticalFillingRow(actors, container, startSide, gapSize);
	}
	
	public static void setPositionCenteredVerticalFillingRow(Actor [] actors, Rectangle container, HorizontalSide startSide, float gapSize) {
		float x = gapSize;
		for(int i = 0; i < actors.length; i++) {
			setPositionCenteredVertical(actors[i], startSide, x, container);
			x += actors[i].getWidth() + gapSize;
		}
	}
	
	public static void setPositionCenterInActor(Actor referenceActor, Actor actorToCenter) {
		actorToCenter.setPosition(referenceActor.getX() + (referenceActor.getWidth() / 2.0f) - (actorToCenter.getWidth() / 2.0f),
				referenceActor.getY() + (referenceActor.getHeight() / 2.0f) - (actorToCenter.getHeight() / 2.0f));
		if(actorToCenter instanceof Label) {
			actorToCenter.setY(actorToCenter.getY() + 1);
		}
	}
	
	public static void setPositionFromCornerInActor(Actor actor, Corner corner,
			float x, float y, Actor container)
	{
		setPositionFromCorner(actor, corner, x, y , container.getX(), container.getY(), 
				container.getWidth(), container.getHeight());
	} 
		
	private static void setPositionCentered(Actor actor, float containerX,
			float containerY, float containerWidth, float containerHeight) 			
	{
		float x = (containerWidth - actor.getWidth()) / 2;
		float y = (containerHeight - actor.getHeight()) / 2;
		actor.setPosition(x + containerX, y + containerY);		
	}
}
