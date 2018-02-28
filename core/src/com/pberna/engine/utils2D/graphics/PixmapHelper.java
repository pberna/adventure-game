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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public class PixmapHelper implements Disposable {
	
	private static final Color BackGroundColorProgressBar1 = new Color(142f / 255, 93f / 255, 202f / 255, 1f);
	private static final Color BackGroundColorProgressBar2 = new Color(26f / 255, 2f / 255, 57f / 255, 1f);
	private static final Color KnobColorProgressBar2 = new Color(0f, 148f / 255f, 1f, 1f);
	private static final Color KnobColorProgressBar1 = new Color(0f, 19f / 255f, 127f / 255f, 1f);
	private static final Color BackGroundColorPendingItems = Color.RED;
	private static final Color BackGroundModalWindow = Color.BLACK;

	private static PixmapHelper instance = null;
	private ArrayList<Disposable> disposables;

	private PixmapHelper() {
		disposables = new ArrayList<Disposable>();
	}

	public static PixmapHelper getInstance()
	{
		if(instance == null) {
			instance = new PixmapHelper();
		}
		return instance;
	}
	
	public Texture createProgressBarBackground(int width, int height) {
		return createGradientRectangle(width, height, BackGroundColorProgressBar1, BackGroundColorProgressBar2, true);
	}

	public Texture createGradientRectangle(int width, int height, Color color1, Color color2, boolean fromBottomToTop) {
		Pixmap pixmap = null;
		Texture texture = null;

		try
		{
			pixmap = new Pixmap(width, height, Format.RGBA8888);

			float incrR = getIncrementByStep(color1.r, color2.r, height);
			float incrG = getIncrementByStep(color1.g, color2.g, height);
			float incrB = getIncrementByStep(color1.b, color2.b, height);
			Color currentColor = new Color(BackGroundColorProgressBar1);

			if(fromBottomToTop) {
				for (int i = 0; i < height; i++) {
					pixmap.setColor(currentColor);
					pixmap.drawLine(0, i, width, i);
					currentColor.set(currentColor.r + incrR, currentColor.g + incrG, currentColor.b + incrB, 1f);
				}
			} else {
				for (int i = height - 1; i >= 0; i--) {
					pixmap.setColor(currentColor);
					pixmap.drawLine(0, i, width, i);
					currentColor.set(currentColor.r + incrR, currentColor.g + incrG, currentColor.b + incrB, 1f);
				}
			}

			texture = new Texture(pixmap);
			disposables.add(texture);
		}
		finally
		{
			if(pixmap != null) {
				pixmap.dispose();
			}
		}

		return texture;
	}

	private float getIncrementByStep(float origin, float destination, int steps) {
		return (destination - origin) / (float) steps;
	}

	public Texture createProgressBarKnob(int width, int height) {
		return createGradientRectangle(width, height, KnobColorProgressBar1, KnobColorProgressBar2, false);
	}

	public Texture createFillRectangle(int width, int height, Color color) {
		Pixmap pixmap = null;
		Texture texture = null;

		try
		{
			pixmap = new Pixmap(width, height, Format.RGBA8888);
			pixmap.setColor(color);
			pixmap.fillRectangle(0, 0, width, height);
			texture = new Texture(pixmap);
			disposables.add(texture);
		}
		finally
		{
			if(pixmap != null) {
				pixmap.dispose();
			}
		}

		return texture;
	}
	
	public Texture createPendingItemsBackground(int width, int height) {
		return createCircleImageWithBorder(width, height, BackGroundColorPendingItems, Color.MAROON);
	}

	public Texture createCircleImageWithBorder(int width, int height, Color fillColor, Color borderColor) {
		Pixmap pixmap = null;
		Texture texture = null;

		try
		{
			pixmap = new Pixmap(width, height, Format.RGBA8888);
			pixmap.setColor(fillColor);
			pixmap.fillCircle(width / 2, height / 2, Math.min(width, height) / 2);
			pixmap.setColor(borderColor);
			pixmap.drawCircle(width / 2, height / 2, Math.min(width, height) / 2);
			texture = new Texture(pixmap);
			disposables.add(texture);
		}
		finally
		{
			if(pixmap != null) {
				pixmap.dispose();
			}
		}

		return texture;
	}

	public Texture createCounterBackground(int width, int height) {
		return createCircleImageWithBorder(width, height, Color.OLIVE, Color.DARK_GRAY);
	}

	public Texture createModalWindowBackground(int width, int height) {
		return createFillRectangle(width, height, BackGroundModalWindow);
	}

	public Texture createProgressBarLeftBorder(int width, int height) {
		return createGradientDemiCircle(width, height, KnobColorProgressBar1, KnobColorProgressBar2, false, true);
	}

	public Texture createProgressBarRightBorder(int width, int height) {
		return createGradientDemiCircle(width, height, KnobColorProgressBar1, KnobColorProgressBar2, false, false);
	}

	public Texture createGradientDemiCircle(int width, int height, Color color1, Color color2, boolean fromBottomToTop, boolean leftSide) {
		Pixmap pixmap = null;
		Texture texture = null;

		try
		{
			pixmap = new Pixmap(width, height, Format.RGBA8888);

			float incrR = getIncrementByStep(color1.r, color2.r, height);
			float incrG = getIncrementByStep(color1.g, color2.g, height);
			float incrB = getIncrementByStep(color1.b, color2.b, height);
			Color currentColor = new Color(BackGroundColorProgressBar1);
			float radius = Math.min(width, height) / 2f;
			int x1;
			int x2;

			if(fromBottomToTop) {
				float angle = (float)( 3f * Math.PI / 2f);
				float incrAngle = getIncrementByStep((float)(3f * Math.PI / 2f),  (float) (Math.PI / 2f), height);

				for (int i = 0; i < height; i++, angle+=incrAngle) {
					pixmap.setColor(currentColor);
					if(leftSide) {
						x1 = (int) (width - Math.abs(radius * Math.cos(angle)));
						x2 = width;
					} else	{
						x1 = 0;
						x2 = (int) Math.abs(radius * Math.cos(angle));
					}
					pixmap.drawLine(x1, i, x2, i);
					currentColor.set(currentColor.r + incrR, currentColor.g + incrG, currentColor.b + incrB, 1f);
				}
			} else {
				float angle = (float) (Math.PI / 2f);
				float incrAngle = getIncrementByStep((float)(Math.PI / 2f),  (float) (3f * Math.PI / 2f), height);

				for (int i = height - 1; i >= 0; i--, angle+=incrAngle) {
					pixmap.setColor(currentColor);
					if(leftSide) {
						x1 = (int) (width - Math.abs(radius * Math.cos(angle)));
						x2 = width;
					} else	{
						x1 = 0;
						x2 = (int)Math.abs(radius * Math.cos(angle));
					}
					pixmap.drawLine(x1, i, x2, i);
					currentColor.set(currentColor.r + incrR, currentColor.g + incrG, currentColor.b + incrB, 1f);
				}
			}

			texture = new Texture(pixmap);
			disposables.add(texture);
		}
		finally
		{
			if(pixmap != null) {
				pixmap.dispose();
			}
		}

		return texture;
	}

	@Override
	public void dispose() {
		for(Disposable disposable: disposables) {
			if (disposable != null) {
				disposable.dispose();
			}
		}
		disposables.clear();
		instance = null;
	}
}
