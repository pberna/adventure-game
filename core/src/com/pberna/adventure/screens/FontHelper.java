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

package com.pberna.adventure.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.pberna.engine.assets.AssetRepository;


public class FontHelper {
    private static String ButtonFontAlias = "ButtonFont";
    private static String LabelFontAlias = "LabelFont";
    private static String SmallFontAlias = "SmallFont";
    private static String ReadTextFontAlias = "ReadText";
    private static String OptionsTextFontAlias = "OptionsText";

    public static BitmapFont getButtonsFont() {
        return AssetRepository.getInstance().getFont(ButtonFontAlias);
    }

    public static BitmapFont getLabelsFont() {
        return AssetRepository.getInstance().getFont(LabelFontAlias);
    }

    public static BitmapFont getSmallLabelsFont() {
        return AssetRepository.getInstance().getFont(SmallFontAlias);
    }

    public static BitmapFont getSmallLabelsFont(boolean markupEnabled) {
        return AssetRepository.getInstance().getFont(SmallFontAlias, markupEnabled);
    }

    public static BitmapFont getReadTextForLabelFont() {
        return AssetRepository.getInstance().getFont(ReadTextFontAlias);
    }

    public static BitmapFont getReadTextFont() {
        return AssetRepository.getInstance().getFont(ReadTextFontAlias);
    }

    public static BitmapFont getOptionsTextFont() {
        return AssetRepository.getInstance().getFont(OptionsTextFontAlias);
    }

    public static BitmapFont getOptionsTextForLabelFont() {
        return AssetRepository.getInstance().getFont(OptionsTextFontAlias);
    }

    public static BitmapFont getDecorationTextFont() {
        return AssetRepository.getInstance().getFont(ReadTextFontAlias);
    }
}
