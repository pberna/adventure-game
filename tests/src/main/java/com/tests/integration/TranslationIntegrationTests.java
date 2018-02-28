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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.pberna.adventure.adventure.AdventureManager;
import com.pberna.adventure.places.EffectPlace;
import com.pberna.adventure.places.OptionChoosePlace;
import com.pberna.adventure.places.OptionChoosePlaceCanGo;
import com.pberna.adventure.places.OptionChoosePlaceCanGoTranslation;
import com.pberna.adventure.places.Place;
import com.pberna.adventure.places.PlaceTranslation;
import com.pberna.adventure.places.PlaceType;
import com.pberna.engine.localization.Localization;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import java.util.ArrayList;
import java.util.Locale;

public class TranslationIntegrationTests {

    private static final int MaximumIterations = 10000;

    private AdventureManager _adventureManager;

    public TranslationIntegrationTests() {
        _adventureManager = AdventureManager.getInstance();
    }

    @Parameter(1)
    public int adventureId;

    @Test
    public void ensureAllPlacesText_AreTranslated() {
        //Arrange
        int countTotalPlaces = 0;
        int countTranslatedPlaces = 0;
        Place place;
        Place lastPlaceWithError = new OptionChoosePlace();

        //Act
        for(int i = 0; i < MaximumIterations; i++) {
            place = _adventureManager.getPlace(adventureId, i);
            if(place == null) {
                continue;
            }
            countTotalPlaces++;
            PlaceTranslation translation = place.getTranslations().size() > 0 ? place.getTranslations().get(0) : null;
            if(translation != null && !place.getText().equals(translation.getText()) && translation.getLanguage().equals("es")) {
                countTranslatedPlaces++;
            } else {
                lastPlaceWithError = place;
            }
        }

        //Assert
        Assert.assertEquals("Not all places are translated: " + ((float)countTranslatedPlaces / countTotalPlaces) * 100f +"%" +
                ".Id=" + String.valueOf(lastPlaceWithError.getId()), countTotalPlaces, countTranslatedPlaces);
    }

    @Test
    public void ensureAllOptionsPlacesTexts_AreWellTranslated() {
        //Arrange
        int countTotalPlaces = 0;
        int countTranslatedPlaces = 0;
        Place place;
        Place lastPlaceWithNotAllOptionsTranslated = new OptionChoosePlace();

        //Act
        for(int i = 0; i < MaximumIterations; i++) {
            place = _adventureManager.getPlace(adventureId, i);
            if(place == null) {
                continue;
            }
            countTotalPlaces++;
            if(placeOptionsAreWellTranslated(place) || place.getId() == 28) {
                countTranslatedPlaces++;
            } else {
                lastPlaceWithNotAllOptionsTranslated = place;
            }
        }

        //Assert
        Assert.assertEquals("Not all options places are translated: " + ((float)countTranslatedPlaces / countTotalPlaces) * 100f +"%" +
                ".Last place id=" + String.valueOf(lastPlaceWithNotAllOptionsTranslated.getId()), countTotalPlaces, countTranslatedPlaces);
    }

    private static boolean placeOptionsAreWellTranslated(Place place) {
        switch (place.getPlaceType().getId()) {
            case PlaceType.IdAttributeCheckPlace:
            case PlaceType.IdSkillCheckPlace:
            case PlaceType.IdCombatPlace:
            case PlaceType.IdItemUsePlace:
            case PlaceType.IdSpellUsePlace:
            case PlaceType.IdEndAdventurePlace:
                return true;

            case PlaceType.IdOptionChoosePlace:
            case PlaceType.IdEffectPlace:
                OptionChoosePlace optionChoosePlace = (OptionChoosePlace)place;

                if(!areAllOptionsDifferent(optionChoosePlace.getPlacesToGo())) {
                    return false;
                }

                for (OptionChoosePlaceCanGo placeCanGo: optionChoosePlace.getPlacesToGo()) {
                    if(!isOptionChoosePlaceCanGoWellTranslated(placeCanGo)) {
                        return false;
                    }
                }

                if(place instanceof EffectPlace) {
                    EffectPlace effectPlace = (EffectPlace) place;
                    if(effectPlace.getPlaceToGoIfDie() != null &&
                            !isOptionChoosePlaceCanGoWellTranslated(effectPlace.getPlaceToGoIfDie())) {
                        return false;
                    }
                }

                return true;
        }

        return false;
    }

    private static boolean areAllOptionsDifferent(ArrayList<OptionChoosePlaceCanGo> placesToGo) {
        for(int i = 0; i < placesToGo.size(); i++) {
            for(int j = i + 1; j < placesToGo.size(); j++) {
                if(placesToGo.get(i).getText().equals(placesToGo.get(j).getText())) {
                    return false;
                }
                OptionChoosePlaceCanGoTranslation translation1 = placesToGo.get(i).getTranslations().size() > 0
                        ? placesToGo.get(i).getTranslations().get(0) : null;
                OptionChoosePlaceCanGoTranslation translation2 = placesToGo.get(j).getTranslations().size() > 0
                        ? placesToGo.get(j).getTranslations().get(0) : null;
                if(translation1 != null && translation2 != null && translation1.equals(translation2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isOptionChoosePlaceCanGoWellTranslated(OptionChoosePlaceCanGo placeCanGo ) {
        OptionChoosePlaceCanGoTranslation placeCanGoTranslation = placeCanGo.getTranslations().size() > 0 ?
                placeCanGo.getTranslations().get(0) : null;
        return placeCanGoTranslation != null && !placeCanGoTranslation.getText().equals(placeCanGo.getText()) &&
                placeCanGoTranslation.getLanguage().equals("es");
    }

    @Test
    @Ignore
    public void exportAllTextAndOptionsEnglish() {
        //Arrange
        Place place;
        Locale previousLocale = Localization.getInstance().getLocale();
        Localization.getInstance().setLocale(new Locale("en"));
        FileHandle file = Gdx.files.local("export/adventure.txt");
        file.writeString("", false);

        //Act
        for(int i = 0; i < MaximumIterations; i++) {
            place = _adventureManager.getPlace(adventureId, i);
            if(place == null) {
                continue;
            }
            printTextAndOptionsToFile(file, place);
        }

        //restore
        Localization.getInstance().setLocale(previousLocale);

    }

    private void printTextAndOptionsToFile(FileHandle file, Place place) {
        file.writeString("Place: " + String.valueOf(place.getId()) + "\n", true);
        file.writeString(place.getTranslatedText() + "\n", true);

        if(place.getPlaceType().getId() != PlaceType.IdOptionChoosePlace &&
                place.getPlaceType().getId() != PlaceType.IdEffectPlace) {
            return;
        }

        OptionChoosePlace optionChoosePlace = (OptionChoosePlace)place;
        int count = 0;
        for (OptionChoosePlaceCanGo placeCanGo: optionChoosePlace.getPlacesToGo()) {
            file.writeString("Option " + String.valueOf(count) + ": " + placeCanGo.getTranslatedText() + "\n", true);
            count++;
        }

        if(place instanceof EffectPlace) {
            EffectPlace effectPlace = (EffectPlace) place;
            if(effectPlace.getPlaceToGoIfDie() != null) {
                file.writeString("If died: " + effectPlace.getPlaceToGoIfDie().getTranslatedText() + "\n", true);
            }
        }
    }

    @Test
    public void ensureAllPlacesText_AreWellFormed() {
        //Arrange
        int countTotalPlaces = 0;
        int countWellFormedPlaces = 0;
        Place place;
        Place lastPlaceWithError = new OptionChoosePlace();
        Locale previousLocale = Localization.getInstance().getLocale();
        Localization.getInstance().setLocale(new Locale("en"));

        //Act
        for(int i = 0; i < MaximumIterations; i++) {
            place = _adventureManager.getPlace(adventureId, i);
            if(place == null) {
                continue;
            }
            countTotalPlaces++;
            PlaceTranslation translation = place.getTranslations().size() > 0 ? place.getTranslations().get(0) : null;
            if(translation != null && (translation.getText().endsWith(".") || translation.getText().endsWith("?")
                    || translation.getText().endsWith("\"") || translation.getText().endsWith("!")) &&
                    translation.getText().substring(0, 1).equals(translation.getText().substring(0, 1).toUpperCase())  ) {
                countWellFormedPlaces++;
            } else {
                lastPlaceWithError = place;
            }
        }

        //Assert
        Assert.assertEquals("Not all places are well-formed: " + ((float)countWellFormedPlaces / countTotalPlaces) * 100f +"%" +
                ".Id=" + String.valueOf(lastPlaceWithError.getId()), countTotalPlaces, countWellFormedPlaces);

        //restore
        Localization.getInstance().setLocale(previousLocale);
    }

    @Test
    public void ensureAllOptionsPlacesTexts_AreWellFormed() {
        //Arrange
        int countTotalPlaces = 0;
        int countTranslatedPlaces = 0;
        Place place;
        Place lastPlaceWithNotAllOptionsTranslated = new OptionChoosePlace();
        Locale previousLocale = Localization.getInstance().getLocale();
        Localization.getInstance().setLocale(new Locale("en"));

        //Act
        for(int i = 0; i < MaximumIterations; i++) {
            place = _adventureManager.getPlace(adventureId, i);
            if(place == null) {
                continue;
            }
            countTotalPlaces++;
            if(placeOptionsAreWellFormed(place)) {
                countTranslatedPlaces++;
            } else {
                lastPlaceWithNotAllOptionsTranslated = place;
            }
        }

        //Assert
        Assert.assertEquals("Not all options places are translated: " + ((float)countTranslatedPlaces / countTotalPlaces) * 100f +"%" +
                ".Last place id=" + String.valueOf(lastPlaceWithNotAllOptionsTranslated.getId()), countTotalPlaces, countTranslatedPlaces);

        //restore
        Localization.getInstance().setLocale(previousLocale);
    }

    private static boolean placeOptionsAreWellFormed(Place place) {
        switch (place.getPlaceType().getId()) {
            case PlaceType.IdAttributeCheckPlace:
            case PlaceType.IdSkillCheckPlace:
            case PlaceType.IdCombatPlace:
            case PlaceType.IdItemUsePlace:
            case PlaceType.IdSpellUsePlace:
            case PlaceType.IdEndAdventurePlace:
                return true;

            case PlaceType.IdOptionChoosePlace:
            case PlaceType.IdEffectPlace:
                OptionChoosePlace optionChoosePlace = (OptionChoosePlace)place;

                for (OptionChoosePlaceCanGo placeCanGo: optionChoosePlace.getPlacesToGo()) {
                    if(placeCanGo.getTranslatedText().endsWith(".") || !placeCanGo.getTranslatedText().substring(0, 1).
                            equals(placeCanGo.getTranslatedText().substring(0, 1).toUpperCase())) {
                        return false;
                    }
                }

                if(place instanceof EffectPlace) {
                    EffectPlace effectPlace = (EffectPlace) place;
                    if(effectPlace.getPlaceToGoIfDie() != null && (effectPlace.getPlaceToGoIfDie().getTranslatedText().endsWith(".")
                            || !effectPlace.getPlaceToGoIfDie().getTranslatedText().substring(0, 1).
                            equals(effectPlace.getPlaceToGoIfDie().getTranslatedText().substring(0, 1).toUpperCase()))
                            ) {
                        return false;
                    }
                }

                return true;
        }

        return false;
    }
}