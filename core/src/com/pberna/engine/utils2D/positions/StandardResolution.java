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

public class StandardResolution {

	private int width;
	private int height;
	private String description;
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	private String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}
	
	public StandardResolution(int width, int height, String description)
	{
		setWidth(width);
		setHeight(height);
		setDescription(description);
	}	
	
	public static StandardResolution getPhoneSmall()
	{
		return new StandardResolution(320, 480, "HVGA");
	}	
	
	public static StandardResolution getPhoneMediumAndroid()
	{
		return new StandardResolution(480, 800, "WVGA");
	}
	
	public static StandardResolution getPhoneMediumApple()
	{
		return new StandardResolution(640, 960, "DVGA");
	}
	
	public static StandardResolution getPhoneBigAndroid()
	{
		return new StandardResolution(768, 1280, "XGA");
	}
	
	public static StandardResolution getPhoneBigApple()
	{
		return new StandardResolution(640, 1136, "iPhone 5");
	}
	
	public static StandardResolution getPhoneVeryBigAndroid()
	{
		return new StandardResolution(1080, 1920, "Galaxy S4");
	}
	
	public static StandardResolution getTabletMediumAndroid()
	{
		return new StandardResolution(600, 1024, "Galaxy Tab small");
	}
	
	public static StandardResolution getTabletMediumApple()
	{
		return new StandardResolution(768, 1024, "iPad 1 and 2");
	}
	
	public static StandardResolution getTabletBigAndroid()
	{
		return new StandardResolution(800, 1280, "Galaxy Tab big");
	}
	
	public static StandardResolution getTabletBigApple()
	{
		return new StandardResolution(1536, 2048, "iPad 3 y 4");
	}
	
	public static StandardResolution getTabletVeryBigAndroid()
	{
		return new StandardResolution(1600, 2560, "Nexus 10");
	}


	public static final StandardResolution[] ListStandardResolutions = 
			new  StandardResolution[]
			{
				new StandardResolution(320,480, "HVGA (old iPhones, Small Androids)"),
				new StandardResolution(480, 800, "WVGA (Low-end Windows Phone)"),
				new StandardResolution(640, 960, "DVGA- iPhone"),
				new StandardResolution(768, 1280, "WXGA- High-end Windows phone"),
				new StandardResolution(1024, 768, "XGA - iPad"),				
				new StandardResolution(1366, 768, "WXGA - Tablet"),
				new StandardResolution(1280, 800, "WXGA - Netbook"),
				new StandardResolution(1366, 768 , "WXGA - Ultrabook"),
				new StandardResolution(1280, 1024,  "SXGA - Small desktop")				
			};			
}
