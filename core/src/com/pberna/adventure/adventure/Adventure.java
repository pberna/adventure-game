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

package com.pberna.adventure.adventure;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.pberna.engine.localization.ExtendedLocale;
import com.pberna.engine.localization.Localization;

public class Adventure {
	private int id;
	private String title;
	private String description;
	private Date creationDate;
	private Author author;
	private Language baseLanguage;
	private ArrayList<Language> languages;
	private ArrayList<AdventureTranslation> translations;
	
	public Adventure() {
		setId(0);
		setTitle("");
		setDescription("");
		setCreationDate(new Date());
		setAuthor(null);
		setBaseLanguage(null);
		setLanguages(new ArrayList<Language>());
		setTranslations(new ArrayList<AdventureTranslation>());
	}

	public int getId() {
		return id;
	}

	public Adventure setId(int id) {
		this.id = id;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Adventure setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Adventure setDescription(String description) {
		this.description = description;
		return this;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Adventure setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	public Author getAuthor() {
		return author;
	}

	public Adventure setAuthor(Author author) {
		this.author = author;
		return this;
	}

	public Language getBaseLanguage() {
		return baseLanguage;
	}

	public Adventure setBaseLanguage(Language baseLanguage) {
		this.baseLanguage = baseLanguage;
		return this;
	}

	public ArrayList<Language> getLanguages() {
		return languages;
	}

	public Adventure setLanguages(ArrayList<Language> languages) {
		this.languages = languages;
		return this;
	}

	public ArrayList<AdventureTranslation> getTranslations() {
		return translations;
	}

	public Adventure setTranslations(ArrayList<AdventureTranslation> translations) {
		this.translations = translations;
		return this;
	}
	
	public String getTranslatedTitle() {
		if(translations != null) {
			Locale locale = Localization.getInstance().getLocale();
			
			for(AdventureTranslation translation: translations){
				if(ExtendedLocale.isTranslationCompatible(translation.getLanguage(),
						locale.getLanguage())) {
					return translation.getTitle();
				}
			}
		}		
		return getTitle();
	}
	
	public String getTranslatedDescription() {
		if(translations != null) {
			Locale locale = Localization.getInstance().getLocale();
			
			for(AdventureTranslation translation: translations){
				if(ExtendedLocale.isTranslationCompatible(translation.getLanguage(),
						locale.getLanguage())) {
					return translation.getDescription();
				}
			}
		}		
		return getDescription();
	}
}
