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

package com.pberna.engine.utils2D.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ImageManipulationHelper {

	private ImageManipulationHelper() {
		
	}
	
	public static void setActorSemiTransparent(Actor actor) {
		setActorTransparency(actor, 0.5f);
	}
	
	public static void setActorNonTransparent(Actor actor) {
		setActorTransparency(actor, 1f);
	}
	
	public static void setActorTransparent(Actor actor) {
		setActorTransparency(actor, 0f);
	}
	
	public static void setActorTransparency(Actor actor, float alpha) {
		Color currentColor = actor.getColor();
		actor.setColor(currentColor.r, currentColor.g, currentColor.b, alpha);
	}
	
	public static TextureRegion moveTextureRegionForPressed(TextureRegion texture) {
		return moveTextureRegion(texture, -2, -2);
	}
	
	public static TextureRegion moveTextureRegion(TextureRegion textureRegionToCopy, int incrX, int incrY) {
		TextureRegion textureRegion = new TextureRegion(textureRegionToCopy);
		textureRegion.setRegion(textureRegionToCopy, incrX, incrY, textureRegionToCopy.getRegionWidth(),
				textureRegionToCopy.getRegionHeight());
		
		return textureRegion;
	}
}
