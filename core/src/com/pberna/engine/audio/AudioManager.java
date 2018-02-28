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

package com.pberna.engine.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager implements IAudioManager {
    private static final AudioManager instance = new AudioManager();
    public static final float MinVolume = 0f;
    public static final float MaxVolume = 1f;
    private static final long SoundIdNull = -1;
    private static final String AudioPreferencesName = "audio_settings";
    private static final String AudioPreferencesSoundVolume = "SoundVolume";
    private static final String AudioPreferencesMusicVolume = "MusicVolume";

    private float soundVolume;
    private float musicVolume;
    private Music currentMusic;

    private AudioManager(){
        soundVolume = MaxVolume;
        musicVolume = MaxVolume;
        currentMusic = null;
    }

    public static AudioManager getInstance() {
        return instance;
    }

    @Override
    public float getSoundVolume() {
        return soundVolume;
    }

    @Override
    public void setSoundVolume(float volume) {
        soundVolume = Math.min(MaxVolume, Math.max(MinVolume, volume));
    }

    @Override
    public float getMusicVolume() {
        return musicVolume;
    }

    @Override
    public void setMusicVolume(float volume) {
        musicVolume = Math.min(MaxVolume, Math.max(MinVolume, volume));
        if(currentMusic != null) {
            currentMusic.setVolume(musicVolume);
            if(musicVolume <= MinVolume && currentMusic.isPlaying()) {
                currentMusic.pause();
            }
            if(musicVolume > MinVolume && !currentMusic.isPlaying()) {
                currentMusic.play();
            }
        }
    }

    @Override
    public void loadSettings() {
        Preferences audioPreferences = Gdx.app.getPreferences(AudioPreferencesName);
        soundVolume = audioPreferences.getFloat(AudioPreferencesSoundVolume, MaxVolume);
        musicVolume = audioPreferences.getFloat(AudioPreferencesMusicVolume, MaxVolume);
    }

    @Override
    public void saveSettings() {
        Preferences audioPreferences = Gdx.app.getPreferences(AudioPreferencesName);
        audioPreferences.putFloat(AudioPreferencesSoundVolume, soundVolume);
        audioPreferences.putFloat(AudioPreferencesMusicVolume, musicVolume);
        audioPreferences.flush();
    }

    @Override
    public Music getCurrentMusic() {
        return currentMusic;
    }

    @Override
    public void setCurrentMusic(Music music) {
        if(currentMusic == music) {
            return;
        }

        if(currentMusic != null) {
            currentMusic.stop();
        }
        currentMusic = music;
        if(currentMusic != null) {
            currentMusic.setLooping(true);
            currentMusic.setVolume(musicVolume);
        }
    }

    @Override
    public void playMusic() {
        if(currentMusic != null) {
            currentMusic.setVolume(musicVolume);
            if(musicVolume > MinVolume) {
                currentMusic.play();
            }
        }
    }

    @Override
    public void stopMusic() {
        if(currentMusic != null) {
            currentMusic.stop();
        }
    }

    @Override
    public void pauseMusic() {
        if(currentMusic != null) {
            currentMusic.pause();
        }
    }

    @Override
    public long playSound(Sound sound) {
        if(sound != null && soundVolume > MinVolume) {
            return sound.play(soundVolume);
        }
        return SoundIdNull;
    }
}
