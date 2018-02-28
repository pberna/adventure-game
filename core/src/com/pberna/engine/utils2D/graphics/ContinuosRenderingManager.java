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

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.pberna.engine.logging.Logger;

public class ContinuosRenderingManager implements Disposable {
	private static ContinuosRenderingManager instance;
	private final ArrayList<Object> objectsNeedContinuosRendering;
	
	private ContinuosRenderingManager() {
		objectsNeedContinuosRendering = new ArrayList<Object>();
	}
	
	public static ContinuosRenderingManager getInstance() {
		if(instance == null) {
			instance = new ContinuosRenderingManager();
		}
		return instance;
	}
	
	public void addObjectNeedsContinuosRendering(Object object) {
		synchronized(objectsNeedContinuosRendering) {
			if(!objectsNeedContinuosRendering.contains(object)) {
				objectsNeedContinuosRendering.add(object);
			}
			updateContinousRendering();
		}
	}
	
	public void removeObjectNeedsContinuosRendering(Object object) {
		synchronized(objectsNeedContinuosRendering) {
			if(objectsNeedContinuosRendering.contains(object)) {
				objectsNeedContinuosRendering.remove(object);
			}
			updateContinousRendering();
		}
	}
	
	private void updateContinousRendering() {
		boolean continousRendering = objectsNeedContinuosRendering.size() > 0;
		if(continousRendering != Gdx.graphics.isContinuousRendering()) {
			Gdx.graphics.setContinuousRendering(continousRendering);
			//Logger.getInstance().addLogInfo(Logger.TagRendering, String.valueOf(Gdx.graphics.isContinuousRendering()));
		}
	}

	public int countObjectsNeedContinuosRendering() {
		return objectsNeedContinuosRendering.size();
	}

	@Override
	public void dispose() {
		objectsNeedContinuosRendering.clear();
		instance = null;
	}
}
