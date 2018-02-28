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

package com.pberna.engine.localization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Localization implements Disposable {
	private static final String LocalizationPreferencesName = "localization_settings";
	private static final String LocalizationPreferencesLanguageName = "language";
	public static final String BlankSpaceAdjusted = "   ";
	private static final Localization instance = new Localization();
	
	private HashMap<String, String> bundleMappings;
	private ArrayList<ExtendedLocale> extendedLocaleList;
	private HashMap<String, I18NBundle> bundleFiles;	
	private Locale locale;
	private boolean adjustBlankSpaces;
	
	public Locale getLocale() {
		return locale;
	}

	public ExtendedLocale getExtendedLocale() {
		for(ExtendedLocale extendedLocale: extendedLocaleList) {
			if(extendedLocale.getLocale().toString().equals(locale.toString())) {
				return extendedLocale;
			}
		}
		for(ExtendedLocale extendedLocale: extendedLocaleList) {
			if(locale.toString().startsWith(extendedLocale.getLocale().toString())) {
				return extendedLocale;
			}
		}
		return null;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public boolean isAdjustBlankSpaces() {
		return adjustBlankSpaces;
	}

	public void setAdjustBlankSpaces(boolean adjustBlankSpaces) {
		this.adjustBlankSpaces = adjustBlankSpaces;
	}

	private Localization()
	{
		bundleMappings = new HashMap<String, String>();
		bundleFiles = new HashMap<String, I18NBundle>();
		extendedLocaleList = new ArrayList<ExtendedLocale>();
		locale = Locale.getDefault(); //new Locale("es"); //
		adjustBlankSpaces = true;
	}
	
	public static Localization getInstance() 
	{
		return instance;
	}
	
	public Locale getDefaultLocale()
	{
		 return new Locale(Locale.getDefault().toString()); //new Locale("es");  //
	}
	
	public void readLocalizationDefinitionFromXml(String xmlFilePath)
	{		
		XmlReader xmlReader = new XmlReader();
		try 
		{
			Element root = xmlReader.parse(Gdx.files.internal(xmlFilePath));
			if(root.getName().equals("localization"))
			{				
				loadLocalizationChildrenNodes(root);				
			}			
		} 
		catch (IOException e) 
		{			
			e.printStackTrace();
		}		
	}

	private void loadLocalizationChildrenNodes(Element root) {
		for(int i = 0; i < root.getChildCount(); i++)
		{
			Element childNode = root.getChild(i);
			if(childNode.getName().equals("localeList")){
				loadLocaleList(childNode);
			} 
			else if(childNode.getName().equals("bundlesList")){
				loadBundleList(childNode);
			}			
		}		
	}
	
	private void loadLocaleList(Element node) 
	{				
		for(int i = 0; i < node.getChildCount(); i++)
		{
			Element childNode = node.getChild(i);
			if(childNode.getName().equals("locale")){
				String displayedText = childNode.getAttribute("displayedText", childNode.getText());
				extendedLocaleList.add(new ExtendedLocale(new Locale(childNode.getText()), displayedText));
			}						
		}	
	}

	private void loadBundleList(Element node) {
		bundleMappings.clear();
		bundleMappings = new HashMap<String, String>(node.getChildCount());
		
		for(int i = 0; i < node.getChildCount(); i++)
		{
			Element childNode = node.getChild(i);
			if(childNode.getName().equals("bundle")){
				loadBundle(childNode);
			}		
		}		
	}

	private void loadBundle(Element node) {
		String path = null;
		String alias = null;
		
		for(int i = 0; i < node.getChildCount(); i++)
		{
			Element childNode = node.getChild(i);
			if(childNode.getName().equals("path")){
				path = childNode.getText();				
			}
			else if(childNode.getName().equals("alias")){
				alias = childNode.getText();
			}			
		}
		
		bundleMappings.put(alias, path);				
	}	
	
	public void loadAllBundles()
	{
		bundleFiles.clear();
		for (Map.Entry<String, String> entry : bundleMappings.entrySet()) {
		    String alias = entry.getKey();
		    String path = entry.getValue();
		    		    
		    FileHandle baseFileHandle = Gdx.files.internal(path);		    
		    I18NBundle bundle = I18NBundle.createBundle(baseFileHandle, locale, "UTF-8");
		    bundleFiles.put(alias, bundle);		    
		}
	}
	
	public void loadBundle(String alias)
	{
		if(bundleFiles.containsKey(alias)) {
			bundleFiles.remove(alias);
		}
		
		if(bundleMappings.containsKey(alias)) {
			FileHandle baseFileHandle = Gdx.files.internal(bundleMappings.get(alias));		    
		    I18NBundle bundle = I18NBundle.createBundle(baseFileHandle, locale);
		    bundleFiles.put(alias, bundle);
		}		
	}
	
	public String getTranslation(String bundleName, String key, boolean adjustSpaces)
	{
		if(bundleFiles.containsKey(bundleName)) {
			String bundle = bundleFiles.get(bundleName).get(key);
			return adjustSpaces ? bundle.replace(" ", BlankSpaceAdjusted) : bundle;
		}
		return "<NoText>";
	}
	
	public String getTranslation(String bundleName, String key)
	{
		if(bundleFiles.containsKey(bundleName)) {
			String bundle = bundleFiles.get(bundleName).get(key);
			return this.adjustBlankSpaces ? bundle.replace(" ", BlankSpaceAdjusted) : bundle;
		}
		return "<NoText>";
	}
	
	public String formatTranslation(String bundleName, String key, Object... args)
	{
		if(bundleFiles.containsKey(bundleName)) {
			String bundle = bundleFiles.get(bundleName).format(key, args);
			return this.adjustBlankSpaces ? bundle.replace(" ", BlankSpaceAdjusted) : bundle;
		}
		return "<NoText>";
	}
	
	@Override
	public void dispose() {
		bundleFiles.clear();
		bundleMappings.clear();
		extendedLocaleList.clear();
	}

	public Array<ExtendedLocale> getExtendedLocaleList() {
		Array<ExtendedLocale> extendedLocaleArray = new Array<ExtendedLocale>();
		for(ExtendedLocale extendedLocale: extendedLocaleList) {
			extendedLocaleArray.add(extendedLocale);
		}
		return extendedLocaleArray;
	}

	public void loadSettings() {
		Preferences localizationPreferences = Gdx.app.getPreferences(LocalizationPreferencesName);
		String localeString = localizationPreferences.getString(LocalizationPreferencesLanguageName, null);
		locale = localeString != null ? new Locale(localeString) : getDefaultLocale();
	}

	public void saveSettings() {
		Preferences localizationPreferences = Gdx.app.getPreferences(LocalizationPreferencesName);
		localizationPreferences.putString(LocalizationPreferencesLanguageName, locale.toString());
		localizationPreferences.flush();
	}
}
