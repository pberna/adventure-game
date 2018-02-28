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

package com.pberna.engine.screens.load;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.pberna.adventure.store.PlatformResolver;
import com.pberna.adventure.store.PurchaseManager;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.logging.Logger;
import com.pberna.engine.screens.BaseStageScreen;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.graphics.PixmapHelper;
import com.pberna.engine.utils2D.positions.PositionerHelper;
import com.pberna.engine.utils2D.positions.VerticalSide;

public class LoadScreenInitGame extends BaseStageScreen {
	private static final float PercentageHeightProgressBar = 0.06f;
	private static final float PercentageWidthProgressBar = 0.9f;
	private static final float PercentageStepProgressBar = 0.01f;

	private static final Color BackgroundColor1 = new Color(0 / 255f, 0 / 255f, 0 / 255f, 1f);
	private static final Color BackgroundColor2 = new Color(48f / 255f, 48f / 255f, 48f / 255f, 1f);

	private ArrayList<LoadScreenListener> listeners;
	private BitmapFont font;
	private Image leftBorderProgressBar;
	private Image rightBorderProgressBar;
	private ProgressBar progressBar;
	private Label progressTextLabel;
	private Texture logoTexture;
	private Image logoImage;

	public LoadScreenInitGame() {
		super();
		setShowBackgroundTexture(false);
		listeners = new ArrayList<LoadScreenListener>();
		font = new BitmapFont();

		//leftBorderProgressBar
		int borderSize = (int)(Gdx.graphics.getHeight() * PercentageHeightProgressBar);
		leftBorderProgressBar = new Image(PixmapHelper.getInstance().createProgressBarLeftBorder(borderSize, borderSize));
		stage.addActor(leftBorderProgressBar);

		//rightBorderProgressBar
		rightBorderProgressBar = new Image(PixmapHelper.getInstance().createProgressBarRightBorder(borderSize, borderSize));
		stage.addActor(rightBorderProgressBar);

		//progress bar
		float progressBarWidth = (Gdx.graphics.getWidth() * PercentageWidthProgressBar) - leftBorderProgressBar.getWidth() - rightBorderProgressBar.getWidth();
		progressBar = new ProgressBar(0f, 1f, PercentageStepProgressBar, false, getProgressBarStyle((int)progressBarWidth,
				(int)(Gdx.graphics.getHeight() * PercentageHeightProgressBar)));
		progressBar.setWidth(progressBarWidth);
		stage.addActor(progressBar);

		//progressTextLabel
		progressTextLabel = new Label("  0 %", getProgressTextLabelStyle());
		progressTextLabel.setAlignment(Align.center);
		stage.addActor(progressTextLabel);

		//logo image
		logoTexture = loadLogoTexture();
		logoImage = new Image(logoTexture);
		resizeImageIfNecessary(logoImage);
		stage.addActor(logoImage);
	}

	private Texture loadLogoTexture() {
		int displayWidth = Gdx.graphics.getWidth();
		int displayHeight = Gdx.graphics.getHeight();

		if(displayWidth <= 320 && displayHeight <= 480) {
			return new Texture(Gdx.files.internal("images/logo/company_logo_border_small_no_blur.png"));
		} else if (displayWidth <= 699 && displayHeight <= 1049) {
			return new Texture(Gdx.files.internal("images/logo/company_logo_border_medium_no_blur.png"));
		} else {
			return new Texture(Gdx.files.internal("images/logo/company_logo_border_big_no_blur.png"));
		}
	}

	private void resizeImageIfNecessary(Image logoImage) {
		float initialWidth = logoImage.getWidth();
		if(initialWidth > Gdx.graphics.getWidth()) {
			float finalWidth = Gdx.graphics.getWidth() * PercentageWidthProgressBar;
			float conversionFactor = finalWidth / initialWidth;
			float finalHeight = logoImage.getHeight() * conversionFactor;
			logoImage.setSize(finalWidth, finalHeight);
		}
	}

	private ProgressBarStyle getProgressBarStyle(int width, int height) {
		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = new TextureRegionDrawable(
				new TextureRegion(PixmapHelper.getInstance().createProgressBarBackground(width, height)));
		progressBarStyle.knob = new TextureRegionDrawable(
				new TextureRegion(PixmapHelper.getInstance().createProgressBarKnob((int)(width * PercentageStepProgressBar), height)));
		progressBarStyle.knobBefore = progressBarStyle.knob;
		return progressBarStyle;
	}

	private Label.LabelStyle getProgressTextLabelStyle() {
		return new Label.LabelStyle(font, Color.WHITE);
	}
	
	@Override
	public void render(float delta) {
		if(AssetRepository.getInstance().loadAssetsInBackgroud()) {
			progressBar.setValue(1f);
			progressTextLabel.setText("100 %");
			for(LoadScreenListener listener: listeners) {
				listener.loadFinished();
			}
	    }
		else {
			// display loading information
			float progress = AssetRepository.getInstance().getProgress();
			progressBar.setValue(progress);
			progressTextLabel.setText(String.valueOf((int)(progress * 100f)) + " %");
		}

		super.render(delta);
	}

	@Override
	protected void drawBackGround() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ShapeRenderer shapeRenderer = AssetRepository.getInstance().ShapeRenderer;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight(),
        		BackgroundColor1, BackgroundColor1,
				BackgroundColor2, BackgroundColor2);
        shapeRenderer.end();
	}

	@Override
	public void show() {
		super.show();
		ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(this);
	}

	@Override
	public void hide() {
		super.hide();
		ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(this);
	}

	@Override
	public void dispose() {
		super.dispose();

		disposeIfNotNull(font);
		disposeIfNotNull(logoTexture);
	}
	
	protected void recalculateActorsPositions(float width, float height) {
		super.recalculateActorsPositions(width, height);

		PositionerHelper.setPositionCenteredHorizontalFillingColumn(new Actor []{ logoImage, progressBar }, stage, VerticalSide.Top);
		leftBorderProgressBar.setPosition(progressBar.getX() - leftBorderProgressBar.getWidth(), progressBar.getY());
		rightBorderProgressBar.setPosition(progressBar.getX() + progressBar.getWidth(), progressBar.getY());
		PositionerHelper.setPositionCenteredHorizontal(progressTextLabel, VerticalSide.Bottom, progressBar.getY() - progressTextLabel.getHeight(), stage);
	}
	
	public void addListener(LoadScreenListener listener) {
		listeners.add(listener);
	}
}
