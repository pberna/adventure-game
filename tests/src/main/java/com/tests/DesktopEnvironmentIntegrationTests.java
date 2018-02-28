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

package com.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pberna.adventure.dependencies.DependenciesContainer;
import com.pberna.adventure.desktop.persistence.DatabaseDesktop;
import com.pberna.engine.logging.Logger;
import com.tests.integration.AttributeIntegrationTests;
import com.tests.integration.Constants;
import com.tests.integration.EnemyManagerIntegrationTests;
import com.tests.integration.ItemManagerIntegrationTests;
import com.tests.integration.SkillIntegrationTests;
import com.tests.integration.SpellIntegrationTests;
import com.tests.integration.TranslationIntegrationTests;
import com.tests.integration.achievements.AchievementActionTests;
import com.tests.integration.achievements.AchievementTests;
import com.tests.integration.achievements.PlayerActionTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.File;

public class DesktopEnvironmentIntegrationTests {

    private static final String databaseName = "adventure_tests";

    private static Class [] listTestClasses = {
            TranslationIntegrationTests.class,
            EnemyManagerIntegrationTests.class,
            ItemManagerIntegrationTests.class,
            AttributeIntegrationTests.class,
            SkillIntegrationTests.class,
            SpellIntegrationTests.class,
            PlayerActionTests.class,
            AchievementActionTests.class,
            AchievementTests.class };

    public static void main (String[] arg) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 480;
        config.height = 600;
        new LwjglApplication(new SetupEnvironment(buildDependenciesContainer()), config);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runTests();
        exit();
    }

    private static DependenciesContainer buildDependenciesContainer() {
        DependenciesContainer container = new DependenciesContainer();
        container.setDatabase(new DatabaseDesktop(databaseName));
        container.getSettings().setStoreAvailable(true);
        return container;
    }

    private static void runTests() {
        JUnitCore junit = new JUnitCore();
        Logger.getInstance().setLogEnabled(true);

        for(Class testClass: listTestClasses) {
            Result testResult = junit.run(testClass);
            printTestResult(testResult, testClass);
        }
    }

    private static void printTestResult(Result testResult, Class testClass) {
        StringBuilder sb = new StringBuilder();
        sb.append("Test: ").append(testClass.getName()).append("\n");
        sb.append("Run count: ").append(testResult.getRunCount()).append("\n");
        sb.append("Failure count: ").append(testResult.getFailureCount()).append("\n");
        for(Failure failure: testResult.getFailures()) {
            sb.append("Failure: ").append(failure.toString()).append("\n");
            sb.append("\t").append(failure.getDescription()).append("\n");
            sb.append("\t").append(failure.getException()).append("\n");
            sb.append("\t").append(failure.getTrace()).append("\n");
        }
        sb.append("Successful: ").append(testResult.wasSuccessful()).append("\n");

        Logger.getInstance().addLogInfo(Constants.TagTests, sb.toString());
    }

    private static void exit() {
        doCleanUp();
        Gdx.app.exit();
    }

    private static void doCleanUp() {
        try {
            File file = new File(databaseName + ".db");
            if(!file.delete()) {
              Logger.getInstance().addLogInfo(Constants.TagTests,"Error deleting test database");
            }
        } catch (Exception ex) {
            Logger.getInstance().addLogInfo(Constants.TagTests,"Error deleting test database: " + ex.getMessage());
        }
    }

}
