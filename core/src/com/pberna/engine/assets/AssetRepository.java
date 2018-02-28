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

package com.pberna.engine.assets;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class AssetRepository implements Disposable {
	private static AssetRepository instance = null;
	private AssetManager assetManager;
	private HashMap<String, String> fontsMappings;
	private HashMap<String, String> textureMappings;
	private HashMap<String, String> musicMappings;
	private HashMap<String, String> textureAtlasMappings;
	private HashMap<String, TextureRegion> textureRegionCache;
	private HashMap<String, String> soundMappings;
		
	public ShapeRenderer ShapeRenderer;
	public SpriteBatch SpriteBatch;
	
	private int displayWidth = Gdx.graphics.getWidth();
	private int displayHeight = Gdx.graphics.getHeight();

	private static final int DefaultConfIndex = 1;
	
	private AssetRepository()
	{
		assetManager = new AssetManager();
		Texture.setAssetManager(assetManager);
		fontsMappings = new HashMap<String, String>();
		textureMappings = new HashMap<String, String>();
		musicMappings = new HashMap<String, String>();
		textureAtlasMappings = new HashMap<String, String>();
		textureRegionCache = new HashMap<String, TextureRegion>();
		soundMappings = new HashMap<String, String>();
		
		ShapeRenderer = new ShapeRenderer();	
		SpriteBatch = new SpriteBatch();
	}
	
	public static AssetRepository getInstance() 
	{
		if(instance == null) {
			instance = new AssetRepository();
		}
		return instance;
	}
	
	public void readAssetsDefinitionFromXml(String xmlFilePath)
	{		
		XmlReader xmlReader = new XmlReader();
		try 
		{
			Element root = xmlReader.parse(Gdx.files.internal(xmlFilePath));
			if(root.getName().equals("assets"))
			{				
				loadAssetsChildrenNodes(root);				
			}			
		} 
		catch (IOException e) 
		{			
			e.printStackTrace();
		}		
	}
	
	private void loadAssetsChildrenNodes(Element root) {
		for(int i = 0; i < root.getChildCount(); i++)
		{
			Element childNode = root.getChild(i);
			if(childNode.getName().equals("bitmapFontList")){
				loadBitmapFontList(childNode);
			} 
			else if(childNode.getName().equals("textureList")){
				loadTextureList(childNode);
			}
			else if(childNode.getName().equals("musicList")){
				loadMusicList(childNode);
			}
			else if(childNode.getName().equals("textureAtlasList")){
				loadTextureAtlasList(childNode);
			}
			else if(childNode.getName().equals("soundList")){
				loadSoundList(childNode);
			}
		}		
	}

	private void loadBitmapFontList(Element node) {
		fontsMappings.clear();
		fontsMappings = new HashMap<String, String>(node.getChildCount());
		
		for(int i = 0; i < node.getChildCount(); i++)
		{
			Element childNode = node.getChild(i);
			if(childNode.getName().equals("bitmapFont")){
				loadAsset(childNode, BitmapFont.class, fontsMappings);
			}		
		}
	}
	
	private void loadTextureList(Element node) {
		textureMappings.clear();
		textureMappings = new HashMap<String, String>(node.getChildCount());
		
		for(int i = 0; i < node.getChildCount(); i++)
		{
			Element childNode = node.getChild(i);
			if(childNode.getName().equals("texture")){
				loadAsset(childNode, Texture.class, textureMappings);
			}		
		}		
	}
	
	private void loadMusicList(Element node) {
		musicMappings.clear();
		musicMappings = new HashMap<String, String>(node.getChildCount());
		
		for(int i = 0; i < node.getChildCount(); i++)
		{
			Element childNode = node.getChild(i);
			if(childNode.getName().equals("music")){
				loadAsset(childNode, Music.class, musicMappings);
			}		
		}		
	}
	
	private void loadTextureAtlasList(Element node) {
		textureAtlasMappings.clear();
		textureAtlasMappings = new HashMap<String, String>(node.getChildCount());
		
		for(int i = 0; i < node.getChildCount(); i++)
		{
			Element childNode = node.getChild(i);
			if(childNode.getName().equals("textureAtlas")){
				loadAsset(childNode, TextureAtlas.class, textureAtlasMappings);
			}		
		}		
	}

	private void loadSoundList(Element node) {
		soundMappings.clear();
		soundMappings = new HashMap<String, String>(node.getChildCount());

		for(int i = 0; i < node.getChildCount(); i++)
		{
			Element childNode = node.getChild(i);
			if(childNode.getName().equals("sound")){
				loadAsset(childNode, Sound.class, soundMappings);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void loadAsset(Element node, @SuppressWarnings("rawtypes") Class classType, 
			HashMap<String, String> hashMap) {
		
		String path = null;
		String alias = null;
		
		for(int i = 0; i < node.getChildCount(); i++)
		{
			Element childNode = node.getChild(i);
			if(childNode.getName().equals("path")){
				if(isAssetWidthAllowed(childNode) && isAssetHeightAllowed(childNode)) {
					path = childNode.getText();
				}
			}
			else if(childNode.getName().equals("alias")){
				if(isAssetWidthAllowed(childNode) && isAssetHeightAllowed(childNode)) {
					alias = childNode.getText();
				}
			}			
		}

		int effectiveIndex = getEffectiveIndex(node);
		if(path == null && effectiveIndex < node.getChildCount()) {
			path = node.getChild(effectiveIndex).getText();
		}
		
		hashMap.put(alias, path);
		assetManager.load(path, classType);		
	}

	private int getEffectiveIndex(Element node) {
		for(int i = DefaultConfIndex; i > 0; i--) {
			if(i < node.getChildCount() - 1) {
				return i;
			}
		}
		return 0;
	}

	private boolean isAssetWidthAllowed(Element node) {
		String maxWidth = node.getAttribute("maxWidth", String.valueOf(Integer.MAX_VALUE));
		String minWidth = node.getAttribute("minWidth", String.valueOf(Integer.MIN_VALUE));
		return displayWidth >= Integer.parseInt(minWidth) && 
				displayWidth <= Integer.parseInt(maxWidth);		
	}
	
	private boolean isAssetHeightAllowed(Element node) {
		String maxHeight = node.getAttribute("maxHeight", String.valueOf(Integer.MAX_VALUE));
		String minHeight = node.getAttribute("minHeight", String.valueOf(Integer.MIN_VALUE));
		return displayHeight >= Integer.parseInt(minHeight) &&
				displayHeight <= Integer.parseInt(maxHeight);
	}

	public void loadAssetsSynchronized()
	{
		assetManager.finishLoading();
	}
	
	public boolean loadAssetsInBackgroud()
	{
		return assetManager.update();
	}
	
	public float getProgress() {
		return assetManager.getProgress();
	}
	
	public BitmapFont getFont(String fontAlias)
	{
		return assetManager.get(fontsMappings.get(fontAlias), BitmapFont.class);
	}

	public BitmapFont getFont(String fontAlias, boolean markupEnabled)
	{
		BitmapFont font = assetManager.get(fontsMappings.get(fontAlias), BitmapFont.class);
		font.getData().markupEnabled = markupEnabled;
		return font;
	}
	
	public Texture getTexture(String textureAlias)
	{
		if(textureMappings.containsKey(textureAlias)){
			return assetManager.get(textureMappings.get(textureAlias), Texture.class);
		}
		return null;
	}
	
	public Music getMusic(String musicAlias)
	{
		return assetManager.get(musicMappings.get(musicAlias), Music.class);
	}

	public Sound getSound(String soundAlias)
	{
		return assetManager.get(soundMappings.get(soundAlias), Sound.class);
	}
	
	public TextureRegion getTextureRegion(String textureAtlasAlias, String regionName)
	{
		//check if the texture is in the cache
		String cacheKey = textureAtlasAlias + "." + regionName;
		if(!textureRegionCache.containsKey(cacheKey))
		{		
			if(!textureAtlasMappings.containsKey(textureAtlasAlias))
			{
				return null;
			}
			TextureAtlas textureAtlas = assetManager.get(textureAtlasMappings.get(textureAtlasAlias), 
				TextureAtlas.class);
		
			TextureRegion textureRegion = textureAtlas.findRegion(regionName);
			textureRegionCache.put(cacheKey, textureRegion);
			
			return textureRegion;
		}
		else
		{
			return textureRegionCache.get(cacheKey);
		}
	}

	@Override
	public void dispose() {		
		disposeIfNotNull(assetManager);
		disposeIfNotNull(ShapeRenderer);
		disposeIfNotNull(SpriteBatch);
		instance = null;
	}
	
	private static void disposeIfNotNull(Disposable object)
	{
		if(object != null)
		{
			object.dispose();
		}
	}
}