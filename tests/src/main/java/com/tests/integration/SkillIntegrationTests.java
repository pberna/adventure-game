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
import com.pberna.adventure.pj.Skill;
import com.pberna.engine.assets.AssetRepository;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SkillIntegrationTests {

    private AssetRepository _assetRepository;

    public SkillIntegrationTests() {

        _assetRepository = AssetRepository.getInstance();
    }

    @Test
    public void getAllSkills_AllSkills_AreWellformed () {

        //Arrange/Act
        ArrayList<Skill> listSkills = Skill.getSkills();


        //Assert
        for(Skill skill: listSkills) {
            Assert.assertNotNull(skill);
            Assert.assertNotNull(skill.getName());
            Assert.assertNotEquals(Constants.NoText, skill.getName());
            Assert.assertNotNull(skill.getDescription());
            Assert.assertNotEquals(Constants.NoText, skill.getDescription());
            try {
                TextureRegion textureRegion = _assetRepository.getTextureRegion("attribute_skills", skill.getImageName());
                Assert.assertNotNull(textureRegion);
            } catch (Exception ex) {
                Assert.fail("Could not get image '" + skill.getImageName() + "' for skill " + skill.getName());
            }
        }
    }
}
