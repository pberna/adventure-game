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

package com.pberna.engine.screens.controls;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pberna.engine.assets.AssetRepository;
import com.pberna.engine.utils2D.graphics.ContinuosRenderingManager;
import com.pberna.engine.utils2D.positions.Corner;
import com.pberna.engine.utils2D.positions.HorizontalSide;
import com.pberna.engine.utils2D.positions.PositionerHelper;

public class ScrollPaneIndicator {

	private boolean orientationHorizontal;
	private float scrollIncrement;
	private boolean scrollIncrementInPercentage;
	private ScrollPane scrollPane;
	private Stage stage;
	private ImageButton scrollBack;
	private ImageButton scrollForward;
	
	public ScrollPaneIndicator(ScrollPane scrollPane, boolean horizontal) {
		orientationHorizontal = horizontal;
		scrollIncrement = 0.8f;
		scrollIncrementInPercentage = true;
		this.scrollPane = scrollPane;
		this.scrollPane.addListener(new EventListener() {
			
			@Override
			public boolean handle(Event event) {
				updateIndicatorsVisibility();
				return false;
			}
		});
		
		scrollBack = new ImageButton(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion(
				"widgets", orientationHorizontal ? "scroll_left" : "scroll_up")));
		scrollBack.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				moveScrollBack();
			}
		});
		scrollBack.setVisible(false);
		
		scrollForward = new ImageButton(new TextureRegionDrawable(AssetRepository.getInstance().getTextureRegion(
				"widgets",  orientationHorizontal ? "scroll_right" : "scroll_down")));
		scrollForward.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				moveScrollForward();
			}
		});
		scrollForward.setVisible(false);
		
		if(this.scrollPane.getStage() != null) {
			stage = scrollPane.getStage();
			stage.addActor(scrollBack);
			stage.addActor(scrollForward);
		}	
	}	
	
	public void updateIndicatorsVisibility() {	
		boolean scrollBackVisible = scrollBack.isVisible();
		boolean scrollForwardVisible = scrollForward.isVisible();
		
		scrollBack.setVisible(orientationHorizontal ? !scrollPane.isLeftEdge() : !scrollPane.isTopEdge());
		scrollForward.setVisible(orientationHorizontal ? !scrollPane.isRightEdge() : !scrollPane.isBottomEdge());
		
		if(scrollBack.isVisible() || scrollForward.isVisible()) {
			if(scrollBackVisible != scrollBack.isVisible() || scrollForwardVisible !=  scrollForward.isVisible()) {
				ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(this);
			}
		} else {
			if(scrollBackVisible != scrollBack.isVisible() || scrollForwardVisible !=  scrollForward.isVisible()) {
				ContinuosRenderingManager.getInstance().removeObjectNeedsContinuosRendering(this);
			}
		}
	}

	public void refreshContinousRendering() {
		if(scrollBack.isVisible() || scrollForward.isVisible()) {
			ContinuosRenderingManager.getInstance().addObjectNeedsContinuosRendering(this);
		}
	}
	
	protected void moveScrollBack() {
		float scrollIncrement = getScrollIncrement();
		if(orientationHorizontal) {
			scrollPane.setScrollX(scrollPane.getScrollX() - scrollIncrement);
		} else {
			scrollPane.setScrollY(scrollPane.getScrollY() - scrollIncrement);
		}
		updateIndicatorsVisibility();
	}

	protected void moveScrollForward() {
		float scrollIncrement = getScrollIncrement();
		if(orientationHorizontal) {
			scrollPane.setScrollX(scrollPane.getScrollX() + scrollIncrement);
		} else {
			scrollPane.setScrollY(scrollPane.getScrollY() + scrollIncrement);
		}
		updateIndicatorsVisibility();
	}

	public void setScrollIncrement(float scrollIncrement, boolean scrollIncrementInPercentage) {
		this.scrollIncrement = scrollIncrement;
		this.scrollIncrementInPercentage = scrollIncrementInPercentage;
	}
	
	private float getScrollIncrement() {
		if(scrollIncrementInPercentage) {
			return scrollPane.getScrollHeight() * scrollIncrement;
		}

		return scrollIncrement;
	}

	public void updateIndicatorsPositions(float paddingX, float paddingY) {
		updateIndicatorsVisibility();
		if(orientationHorizontal) {
			PositionerHelper.setPositionCenteredVertical(scrollBack, HorizontalSide.Left, -scrollBack.getWidth() / 2f, scrollPane);
			PositionerHelper.setPositionCenteredVertical(scrollForward, HorizontalSide.Right, -scrollBack.getWidth() / 2f, scrollPane);
		} else {
			PositionerHelper.setPositionFromCornerInActor(scrollBack, Corner.TopRight, paddingX, paddingY, scrollPane);
			PositionerHelper.setPositionFromCornerInActor(scrollForward, Corner.BottomRight, paddingX, paddingY, scrollPane);
		}
	}
}
