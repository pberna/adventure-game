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

package com.tests.integration;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pberna.adventure.pj.Attribute;
import com.pberna.engine.assets.AssetRepository;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AttributeIntegrationTests {

    private AssetRepository _assetRepository;

    public AttributeIntegrationTests() {

        _assetRepository = AssetRepository.getInstance();
    }

    @Test
    public void getAllAttributes_AllAttributes_AreWellformed () {

        //Arrange/Act
        ArrayList<Attribute> listAttributes = Attribute.getAttributes();

        //Assert
        for(Attribute attribute: listAttributes) {
            Assert.assertNotNull(attribute);
            Assert.assertNotNull(attribute.getName());
            Assert.assertNotEquals(Constants.NoText, attribute.getName());
            Assert.assertNotNull(attribute.getDescription());
            Assert.assertNotEquals(Constants.NoText, attribute.getDescription());
            try {
                TextureRegion textureRegion = _assetRepository.getTextureRegion("attribute_skills", attribute.getImageName());
                Assert.assertNotNull(textureRegion);
            } catch (Exception ex) {
                Assert.fail("Could not get image '" + attribute.getImageName() + "' for attribute " + attribute.getName());
            }
        }
    }
}
