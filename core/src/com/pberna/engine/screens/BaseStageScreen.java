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

package com.pberna.engine.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.screens.shapes.LineRectangle;
import com.pberna.engine.screens.shapes.LineShape;
import com.pberna.engine.screens.windows.AnimableWindow;
import com.pberna.engine.utils2D.graphics.ImageManipulationHelper;
import com.pberna.engine.utils2D.graphics.PixmapHelper;

public abstract class BaseStageScreen implements Screen, Disposable {
	
	protected static final float LowerPanelPercentageHeight = 0.19f;
	
	protected Stage stage;
	private String backgroundTextureName;
	private ArrayList<TextureRegionToDraw> listTextureRegionToDraw;
	private ArrayList<LineShape> listLineShapes;
	private float initialWidth;
	private float initialHeight;
	private boolean showBackgroundTexture;
	private Image modalWindowBackground;
	private Image darkBackground;
	private boolean lineShapesVisible;
	
	public BaseStageScreen()
	{
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		setBackgroundTextureName("old_page_dark");
		listTextureRegionToDraw = new ArrayList<TextureRegionToDraw>();
		listLineShapes = new ArrayList<LineShape>();
		initialWidth = Gdx.graphics.getWidth();
		initialHeight = Gdx.graphics.getHeight();
		setShowBackgroundTexture(true);
		modalWindowBackground = createDarkBackground();
		darkBackground = createDarkBackground();
		lineShapesVisible = true;
	}

	public String getBackgroundTextureName() {
		return backgroundTextureName;
	}

	public void setBackgroundTextureName(String backgroundTextureName) {
		this.backgroundTextureName = backgroundTextureName;
	}

	protected boolean isShowBackgroundTexture() {
		return showBackgroundTexture;
	}

	protected void setShowBackgroundTexture(boolean showBackgroundTexture) {
		this.showBackgroundTexture = showBackgroundTexture;
	}

	private Image createDarkBackground() {
		Image background = new Image(PixmapHelper.getInstance().createModalWindowBackground(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		ImageManipulationHelper.setActorTransparent(background);
		background.setTouchable(Touchable.disabled);
		stage.addActor(background);
		return background;
	}

	public Image getDarkBackground() {
		return darkBackground;
	}

	public Image getModalWindowBackground() {
		return modalWindowBackground;
	}
	
	protected void addTextureRegionToDraw(TextureRegionToDraw textureRegionToDraw) {
		listTextureRegionToDraw.add(textureRegionToDraw);
	}
	
	protected void removeTextureRegionToDraw(TextureRegionToDraw textureRegionToDraw) {
		listTextureRegionToDraw.remove(textureRegionToDraw);
	}
	
	public void addLineShape(LineShape lineShape) {
		listLineShapes.add(lineShape);
	}
	
	public void removeLineShape(LineShape lineShape) {
		listLineShapes.remove(lineShape);
	}	

	@Override
	public void render(float delta) {
		drawBackGround();
		drawShapes();
		
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();		
	}
	
	protected void drawBackGround()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        /*ShapeRenderer shapeRenderer = AssetRepository.getInstance().ShapeRenderer;        
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight(), 
        		new Color(142f / 255, 93f / 255, 202f / 255, 1f),
        		new Color(142f / 255, 93f / 255, 202f / 255, 1f),        		
        		new Color(26f / 255, 2f / 255, 57f / 255, 1f),
        		new Color(26f / 255, 2f / 255, 57f / 255, 1f)        		
        );        	
        shapeRenderer.end();*/
        
        if(showBackgroundTexture)
        {        
	        SpriteBatch spriteBatch = AssetRepository.getInstance().SpriteBatch;
	        spriteBatch.begin();
	        Texture backgroundTexture = AssetRepository.getInstance().getTexture(getBackgroundTextureName());
	        spriteBatch.draw(backgroundTexture, 0, 0, initialWidth, initialHeight);
	        drawListTextureRegions(spriteBatch);
	        spriteBatch.end();
        }
        
	}

	private void drawListTextureRegions(SpriteBatch spriteBatch) {
		for(TextureRegionToDraw toDraw: listTextureRegionToDraw) {
			spriteBatch.draw(toDraw.getTextureRegion(), toDraw.getX(), toDraw.getY());
		}
	}
	
	private void drawShapes() {
		if(lineShapesVisible) {
			drawLineShapes();
		}
	}

	private void drawLineShapes() {
		if(listLineShapes.size() > 0) {
			ShapeRenderer shapeRenderer = AssetRepository.getInstance().ShapeRenderer;
			shapeRenderer.begin(listLineShapes.get(0).getShapeType());
			for(LineShape lineShape: listLineShapes) {
				lineShape.draw(shapeRenderer);
			}
			shapeRenderer.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		if(hasChangedAvailableSize(modalWindowBackground, width, height)) {
			modalWindowBackground.remove();
			modalWindowBackground = createDarkBackground();
			lineShapesVisible = false;
		}
		if(hasChangedAvailableSize(darkBackground, width, height)) {
			darkBackground.remove();
			darkBackground = createDarkBackground();
			lineShapesVisible = false;
		}
		recalculateActorsPositions(stage.getWidth(), stage.getHeight());		
	}

	private boolean hasChangedAvailableSize(Image image, int width, int height) {
		if(image.getWidth() != width || image.getHeight() != height) {
			return true;
		}
		return false;
	}

	protected void recalculateActorsPositions(float width, float height)
	{
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		sendToFrontIfVisible(modalWindowBackground);
		sendToFrontIfVisible(darkBackground);
		Gdx.graphics.requestRendering();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		disposeIfNotNull(stage);		
	}
	
	protected void disposeIfNotNull(Disposable object)
	{
		if(object != null)
		{
			object.dispose();
		}
	}

	protected void sendToFrontIfVisible(Actor actor) {
		if(actor.isVisible()) {
			actor.toFront();
		}
	}

	protected void setBorderOnActor(AnimableWindow animableWindow, LineRectangle border) {
		if(animableWindow.getWidth() == 0 || animableWindow.getHeight() == 0) {
			border.getRectangle().setX(animableWindow.getX() - (animableWindow.getOriginalWidth() / 2f));
			border.getRectangle().setY(animableWindow.getY() - 1 - (animableWindow.getOriginalHeight() / 2f));
		} else {
			border.getRectangle().setX(animableWindow.getX());
			border.getRectangle().setY(animableWindow.getY());
		}
	}
}
