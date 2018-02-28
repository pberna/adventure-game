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

package com.pberna.adventure;

import java.util.ArrayList;
import java.util.Date;

import com.pberna.adventure.adventure.Adventure;
import com.pberna.adventure.adventure.AdventureTranslation;
import com.pberna.adventure.adventure.Author;
import com.pberna.adventure.adventure.Language;
import com.pberna.adventure.items.manager.ItemManager;
import com.pberna.adventure.pj.Attribute;
import com.pberna.adventure.pj.Difficulty;
import com.pberna.adventure.pj.EnemyManager;
import com.pberna.adventure.pj.Skill;
import com.pberna.adventure.places.AttributeCheckPlace;
import com.pberna.adventure.places.CombatPlace;
import com.pberna.adventure.places.EndAdventurePlace;
import com.pberna.adventure.places.ItemUsePlace;
import com.pberna.adventure.places.ItemUsePlaceToGo;
import com.pberna.adventure.places.OptionChoosePlace;
import com.pberna.adventure.places.OptionChoosePlaceCanGo;
import com.pberna.adventure.places.OptionChoosePlaceCanGoTranslation;
import com.pberna.adventure.places.Place;
import com.pberna.adventure.places.PlaceTranslation;
import com.pberna.adventure.places.PlaceType;
import com.pberna.adventure.places.SkillCheckPlace;
import com.pberna.adventure.places.SpellUsePlace;
import com.pberna.adventure.places.SpellUsePlaceToGo;
import com.pberna.adventure.spells.Spell;

public class AdventureBetaHelper {
	private static ArrayList<PlaceType> listPlacestypes = PlaceType.getAllPlaceTypes();
	
	private AdventureBetaHelper() {
		
	}
	
	public static Adventure createTestingAdventure() {
		Adventure adventure = new Adventure()
			.setId(1)
			.setTitle("One more drink")
			.setDescription("You are an adventurer: a globetrotter who travels the kingdom looking for emotions and rewards in exchange of fulfilling missions. In this fantastic world of sword and sorcery that you inhabit you have seen yourself forced to face all kinds of creatures and dangers. Sometimes alone, but almost always as a member of a larger group of adventurers like you, joined by the same desires and ambitions.\n" +
					"<image>inn</image>" +
					"A couple of days ago, you separated from your last partners. You had been staying at an inn on the road to your next mission, but during the night instead of resting you decided to spend some time playing dice at one of the common room tables. You do not remember exactly how many beers you drank while having fun and playing (neither how much money you lost) but you do remember perfectly the glass of water that the innkeeper threw in your face to wake you up the next day at noon. After a few seconds of astonishment, he told you that your friends had left early in the morning to go on their way without you because they did not find you in the inn. You looked around and saw that you were in the stables. You did not know how you got there.\n" +
					"Embarrassed and in a hurry, you rushed to your room to get your baggage and try to join your party again. Your head was about to explode, but you managed to grab all your belongings and put them in your backpack. When you were about to leave the inn, the innkeeper appeared again panting because of the run to get to you. He told you that there was a group of farmers that were leaving in the same direction as you at that momemt. They wanted to transport some goods to sell in a big city a few days of travel from here. Given that traveling alone is not a good idea, he suggested you to join them. After these last words, he damned and leave.\n" +
					"The farmers were excited to have more protection during the trip and they gladly accepted you. Ten minutes later, and with a little less headache after eating a quick breakfast, you left. All the way through the afternoon of that day, you did not think about anything else but how your collegues might have abandoned you. Of course, you did not know since many time ago, but that was very strange. You had been more than competent in your skills before and you had proved to be a valuable ally. To summarize, a resource with which it was worth to have. Leaving you behind without even inquiring or looking for you a little bit did not make sense. You should have asked the innkeeper more questions before leaving, but the hurry and your own mental numbness caused by the hangover did not let you think of it. Besides, you did not see the man again after his last words in your room...\n" +
					"At dusk you looked for a place to camp. You figured the next morning you would have reached your companions, because in the next stage of your itinerary you had to spend a couple of days in a small village located a little further ahead. A farmer of the caravan said that to right of the road next to a small birch forest there was a good place to camp and spend the night. Apparently he was told about it at the inn. The other farmers agreed to look for the place, and you too even though you were starting to feel pretty restless about it.\n" +
					"You found the place a few hundred steps away. In addition to the trees, there was also a small stream of drinking water. After dinner, and feeling a growing distrust, you offered yourself to be part of the first guard at night. Something was giving you the creeps. It all looked like too much planned...\n" +
					"Nothing happened during the first hour. However, you had all your senses alert. Finally, you heard a slight crackle of branches behind you and you turned around quickly. Despite of your speed you couldn not avoid the blow to your head by a dark figure with a club. You fell to the ground, and some seconds before losing consciousness you were thinking that you already knew who suggested this place for the farmer to camp..."
				)
			.setCreationDate(new Date())
			.setAuthor(getAuthor())
			.setBaseLanguage(getBaseLanguage());
		adventure.getLanguages().add(getBaseLanguage());
		adventure.getLanguages().add(getSpanishLanguage());
		adventure.getTranslations().add(new AdventureTranslation());
		adventure.getTranslations().get(0).setLanguage("es");
		adventure.getTranslations().get(0).setTitle("Una copita de más");
		adventure.getTranslations().get(0).setDescription("Eres un aventurero: un trotamundos que recorre el reino buscando emociones y recompensas a cambio de cumplir misiones. En este mundo fantástico de espada y brujería que habitas te has visto obligado a enfrentarte a todo tipo de criaturas y peligros. A veces solo, pero casi siempre como miembro de un grupo mayor de aventureros como tú unidos por los mismos deseos y ambiciones.\n" +
				"<image>inn</image>" +
				"Hace un par de días te quedaste descolgado de tus últimos compañeros. Os habíais hospedado en un posada en el camino que os conducía a vuestra próxima misión, pero durante la noche en lugar de descansar decidiste pasar el rato jugando a los dados en una de las mesas de la sala común. No recuerdas exactamente cuantas cervezas tomaste mientras te divertías jugando (ni cuando dinero perdiste) pero si recuerdas perfectamente el vaso de agua que el posadero te lanzó al rostro para despertarte al día siguiente al mediodía. Tras unos segundos de estupor, te dijo que tus amigos se habían marchado temprano por la mañana para seguir su camino sin ti, pues no te encontraron por ninguna parte de la posada. Miraste a tu alrededor y comprobaste que te hallabas en los establos. No sabías como habías llegado hasta allí.\n" +
				"Avergonzado y con una súbita premura, corriste a tu habitación a coger tus cosas e intentar alcanzar a tu grupo. La cabeza estaba a punto de estallarte, pero atinaste a coger todas tus pertenencias y meterlas en tu mochila. Cuando ya te disponías a salir de la estancia, el posadero apareció de nuevo jadeando por la carrera que se había dado para alcanzarte y te dijo que había un grupo de granjeros que salían en ese momento en tu misma dirección. Querían transportar en carromatos unas mercancías para venderlas en una gran ciudad a unos días de viaje de aquí. Como viajar sólo no es una buena idea, te recomendó que te unieras a ellos. Tras esas últimas palabras, soltó una maldición y despareció.\n" +
				"Los granjeros se mostraron entusiasmados de contar con algo más de protección durante el camino y te aceptaron de buen grado. Diez minutos más tarde, y con algo menos de dolor de cabeza tras ingerir un rápido desayuno, partisteis. Durante todo el trayecto de la tarde de aquel día no pensabas en otra cosa más que en como te podían haber abandonado tus compañeros. No eráis íntimos, desde luego, pero aquello era muy extraño. Te habías mostrado más que competente en tus habilidades anteriormente y habías demostrado ser un aliado valioso: en definitiva, un recurso con el que valía la pena contar. Que te dejaran atrás sin ni siquiera indagar o buscarte un poco no tenía mucho sentido. Debiste hacer más preguntas al posadero antes de partir, pero la rapidez de los acontecimientos y tu propio embotamiento mental por la resaca no te dejaron caer en ello. Además, no volviste a ver al susodicho otra vez tras sus últimas palabras en tu habitación...\n" +
				"Al anochecer buscasteis un sitio para acampar. Calculabas que al día siguiente por la mañana debías dar alcance a tus compañeros, pues en la siguiente etapa de vuestro itinerario debíais pasar un par de días en una pequeña aldea ubicada un poco más adelante. Un granjero de la caravana comentó que a la derecha del camino junto a un pequeño bosque de abedules había un buen lugar donde montar el campamento y pasar la noche. Por lo visto se lo habían comentado en la posada. El resto de granjeros estuvo de acuerdo en buscar el sitio y tu aceptaste aunque empezabas a sentirte bastante inquieto.\n" +
				"Encontrasteis el lugar a unos centenares de paso de distancia. Además de los árboles también había un pequeño arroyo de agua potable. Tras cenar, y sintiendo una creciente desconfianza, te ofreciste a formar parte del primer turno de guardia durante la noche. Algo te daba mala espina. Todo parecía demasiado planificado...\n" +
				"Durante la primera hora de guardia no pasó nada. No obstante, tú tenías todos los sentidos alerta. Al cabo, oíste un leve crujir de ramas detrás tuyo y te giraste rápidamente. A pesar de tu raudo movimiento no pudiste evitar el golpe directo a tu frente propinado por una figura oscura con un garrote. Caíste al suelo de espaldas y segundos antes de perder el conocimiento no dejabas de pensar que ya sabías quien le había sugerido este lugar para acampar al granjero..."
		);
		return adventure;
	}
	
	public static Author getAuthor() {
		Author author = new Author();
		author.setName("pberna");
		return author;
	}
	
	public static Language getBaseLanguage() {
		Language language = new Language();
		
		language.setId(1);
		language.setCode("en");
		language.setName("English");
		
		return language;
	}

	public static Language getSpanishLanguage() {
		Language language = new Language();

		language.setId(2);
		language.setCode("es");
		language.setName("Spanish");

		return language;
	}
	
	public static Place getStartPlace() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(1);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("When you regain consciousness, you realize that you are no longer in the forest. You are in a small dark room of stone walls, plunged into the gloom. The only source of light comes from outside. It is partially filtered through the bars located on a wall on one side of what seems to be a cell. Because of the humidity in the environment, you guess that you are in an underground place.\n" +
					  "Your head hurts a lot as a result of the wound you received. You touch it with your fingers but you do not see blood on your hand. You stand up and go to the bars, just at the moment when you hear a human, snoring and unpleasant voice that warns you to stay where you are. Immediately, a hand introduces a key into the lock of the cell and a man comes in. He walks ungainly but looks tough. You see what looks like some kind of uniform but in a deplorable state, ragged and filthy. He is armed with a short sword and looks at you with an unpleasant smile full of broken and rotten teeth. Instead of hair he has a tangle of wild black hairs beyond any description. He tells you that he was wishing you to wake up to have a little fun with you before the master arrived, and he goes ahead with the weapon pointing to you...\n" +
					  "However, after his second step his left foot slips on a small puddle of some sort of liquid and he falls face down to the ground. Because of the unnatural shape of his neck, you guess he broke it with the fall. You cannot believe how lucky you have been..\n" +
					  "What are you doing next?");
		place.getTranslations().add(new PlaceTranslation("es",
				"Cuando recobras el conocimiento te das cuenta de que ya no estás en el bosque. Te encuentras en una pequeña habitación oscura de muros de piedra, sumida en la penumbra. La única fuente de luz proviene del exterior, una luz que se filtra parcialmente a través de las rejas que hay en lugar de pared en uno de los lados de lo que parece tu celda. Por la humedad del ambiente deduces que estás un lugar subterráneo.\n" +
				"La frente te duele mucho a consecuencia de la herida recibida. Te la palpas con los dedos de la mano pero no observas restos de sangre. Te incorporas y te acercas a las rejas, momento justo en el cual oyes una voz humana ronca y desagradable que te avisa que te quedes donde estás. Al instante una mano introduce una llave en la cerradura de la celda y entra un hombre de andar desgarbado pero de aspecto fornido. Ves lo que parece algún tipo de uniforme pero en un estado deplorable, andrajoso y lleno de suciedad. Va armado con una espada corta y te mira con una sonrisa desagradable llena de dientes rotos y podridos. En lugar de cabello tiene una maraña indescriptible de pelos salvajes de color negro. Te dice que estaba deseando que despertaras para divertirse un poco contigo antes de que llegue el amo y avanza hacia ti con el arma apuntándote...\n" +
				"Sin embargo, tras su segundo paso resbala con el pie izquierdo sobre un pequeño charco de algún tipo de líquido y cae de bruces al suelo. Por la forma antinatural en la que ha quedado su cuello, deduces que se lo ha roto con la caída. No te puedes creer el golpe de suerte que acabas de tener.\n" +
				"¿Que haces a continuación?"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Register the body", 2));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Registrar el cuerpo"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Exit the jail", 3));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Salir de la celda"));
		return place;
	}
	
	public static Place getPlace2() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(2);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("You register the body of the jailer and find an iron key and the short sword that was hanging on his belt. You keep both items. Given the smell, you think that the puddle was mainly composed by urine, probably from some other captive who was in the cell before you.");
		place.getTranslations().add(new PlaceTranslation("es", "Registras el cuerpo del carcelero y encuentras una llave de hierro y la espada corta que llevaba colgada en el cinto. Te quedas ambos objetos y los guardas. Compruebas por el olor que el charco estaba formado por orines, probablemente de algún otro cautivo que estuvo en la celda antes que tú."));
		place.getItemsCharacterGets().add(ItemManager.getInstance().getItem(3));
		place.getItemsCharacterGets().add(ItemManager.getInstance().getItem(4));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Exit the jail", 3));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Sales de la celda"));
		place.addPlayerAction(AchievementsHelper.ItemSword);
		place.addPlayerAction(AchievementsHelper.ItemKey);
		return place;
	}

	public static Place getPlace3() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(3);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("You come out of the cell and enter into a small room lit up by torches on the walls. There is no window and the humidity in the environment makes you think you are underground. In front of you you see a small moldy oak table with food scattered on it.\n" +
				"To the left, right next to the bars of your own cell, you can see some more bars. To the right there is a corridor with more torches, but you cannot see its end.");
		place.getTranslations().add(new PlaceTranslation("es",
				"Sales de la celda a una pequeña sala iluminada con antorchas en las paredes. No hay ninguna ventana por la que entre luz natural y la humedad en el ambiente te hace pensar que te hallas bajo tierra. Frente a ti ves una pequeña mesa de roble mohosa y con restos de comida desperdigados sobre ella.\n" +
				"A la izquierda, justo al lado de las rejas de tu propia celda, puedes ver otras iguales. A la derecha se abre un pasillo con más antorchas pero no puedes ver el final."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Inspect the adjacent cell", 4));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Inspeccionas la celda contigua"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Walk down the corridor", 11));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Avanzas por el pasillo"));
		place.addPlayerAction(AchievementsHelper.ExitJail);
		return place;
	}
	
	public static Place getPlace4() {
		ItemUsePlace place = new ItemUsePlace();
		place.setId(4);
		place.setPlaceType(getPlaceType(PlaceType.IdItemUsePlace));
		place.setText("You get closer to the other cell and watch it. It looks pretty old but still solid. It has a lock on the right side of the bars that is covered by rust. You try to open it by pulling the bars, but it is locked and it is impossible for you. Suddenly, you hear noise coming from behind, just from the direction of the corridor.");
		place.getTranslations().add(new PlaceTranslation("es",
				"Te acercas a la celda contigua y la observas. Parece bastante vieja pero aún así sólida. Tiene una cerradura en el lado derecho de las rejas que está comida por el óxido. Intentas abrirla las rejas tirando de ellas, pero el cerrojo está echado y te resulta imposible. Al momento, oyes ruidos que vienen de detrás de ti desde la dirección donde se halla el pasillo."));
		place.setIdPlaceToGoIfNoItem(11);
		place.getPlacesToGo().add(new ItemUsePlaceToGo(ItemManager.getInstance().getItem(3), 5, false));
		
		return place;
	}

	public static Place getPlace5() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(5);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("You open the door and enter into the cell. Inside it, on a corner, there is the dead body of an old dwarf. You see signs of torture on it and dry blood stains on the floor. The room smells pretty bad, indicating that the unfortunate one has been dead for several days. You do not see anything else in the room..\n" +
				"The noise you heard before seems to be closer and you see a figure approaching quickly. You cannot identify the being you are dealing with because it is backlit.\n" +
				"What are you doing?");
		place.getTranslations().add(new PlaceTranslation("es",
				"Abres la puerta y entras en la celda. En el interior y en una esquina se encuentra el cuerpo muerto de un enano de avanzada edad. Observas señales de tortura en él y manchas de sangre reseca en el suelo. La estancia huele bastante mal, lo que indica que el desafortunado llevas varios días muerto. No ves nada más en la estancia.\n" +
				"Los ruidos que oíste antes se notan más cerca y atisbas una figura que se acerca andando con cierta rapidez. No puedes identificar el ser del cual se trata pues está a contraluz de las antorchas.\n" +
				"¿Que haces?"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Try to hide in the shadows so you will not be found", 7));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Intentar ocultarte en las sombras para que no te encuentre"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Exit the cell and face it", 6));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Salir de la celda y enfrentarte a él"));
		
		return place;
	}
	
	public static Place getPlace6() {
		CombatPlace place = new CombatPlace();
		place.setId(6);
		place.setPlaceType(getPlaceType(PlaceType.IdCombatPlace));
		place.setText("In front of you there is a skeleton of a human being with the remains of shreds of clothing on it, armed with a sword in one hand and a shield in the other. At once he turns his empty eye sockets pointing towards you and moves forward in attacking position.\n" +
				"There is no possible exit but behind the skeleton. You must face it.");
		place.getTranslations().add(new PlaceTranslation("es",
				"Ante ti ves un esqueleto de un ser humano con restos de jirones de ropa sobre si mismo y armado con una espada en una mano y un escudo en la otra. Al instante gira su cráneo apuntando sus cuencas vacías hacia ti y avanza en posición de ataque.\n" +
				"No hay ninguna salida posible excepto detrás del esqueleto. Debes enfrentarte a él."));
		place.setEnemy(EnemyManager.getInstance().getById(1));
		place.setIdPlaceToGoIfWin(10);
		place.setIdPlaceToGoIfLose(9);

		return place;
	}
	
	public static Place getPlace7() {
		SkillCheckPlace place = new SkillCheckPlace();
		place.setId(7);
		place.setPlaceType(getPlaceType(PlaceType.IdSkillCheckPlace));
		place.setText("By taking advantage of the darkness of the cell, you try to hide in a corner to avoid being seen. You try not to make noise to not be detected..");
		place.getTranslations().add(new PlaceTranslation("es","Aprovechando la oscuridad de la celda, intentas ocultarte en un rincón para no ser visto. Procuras no hacer ruido para no delatar tu presencia."));
		place.setSkill(Skill.findSkill(Skill.getSkills(), Skill.IdStealth));
		place.setDifficulty(Difficulty.getDifficulty(6, false));
		place.setIdPlaceToGoIfPass(8);
		place.setIdPlaceToGoIfFail(6);
		
		return place;
	}

	public static Place getPlace8() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(8);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("Te escondes sin hacer ruido y consigues ocultar tu cuerpo a la vista de cualquiera dentro de la celda. Un instante después, ves aparecer la figura de un  esqueleto de un ser humano con restos de jirones de ropa sobre si mismo y armado con una espada en una mano y un escudo en la otra. Se para en la puerta de la celda, mueve su cráneo de un lado a otro como inspeccionando con sus cuencas vacías pero tras unos segundos se da la vuelta y vuelve por donde ha venido.\n" +
			"Esperas unos minutos a que se haya alejado lo bastante y sales de la celda. Vuelves junto a la mesa de roble y no ves rastro del esqueleto en ningún sitio.");
		place.setText("You hide yourself completely inside the cell. A moment later, The figure of a human skeleton shows up, with the remains of shreds of clothing on itself, armed with a sword in one hand and a shield in the other. He stands at the cell door, moves his skull back and forth like inspecting the place, but after a few seconds he turns around and returns where it came from.\n" +
				"You wait for a few minutes until the skeleton is far enough and then exit the cell. You go back to the oak table and see no sign of the skeleton anywhere.");
		place.getTranslations().add(new PlaceTranslation("es","Te escondes sin hacer ruido y consigues ocultar tu cuerpo a la vista de cualquiera dentro de la celda. Un instante después, ves aparecer la figura de un  esqueleto de un ser humano con restos de jirones de ropa sobre si mismo y armado con una espada en una mano y un escudo en la otra. Se para en la puerta de la celda, mueve su cráneo de un lado a otro como inspeccionando con sus cuencas vacías pero tras unos segundos se da la vuelta y vuelve por donde ha venido.\n" +
				"Esperas unos minutos a que se haya alejado lo bastante y sales de la celda. Vuelves junto a la mesa de roble y no ves rastro del esqueleto en ningún sitio."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Go ahead through the corridor leaving the room behind you", 11));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Avanzas por el pasillo dejando atrás la estancia"));
		place.addPlayerAction(AchievementsHelper.StealthSuccess);
		return place;
	}

	public static Place getPlace9() {
		EndAdventurePlace place = new EndAdventurePlace();
		place.setId(9);
		place.setPlaceType(getPlaceType(PlaceType.IdEndAdventurePlace));
		place.setText("The sword of the skeleton hits you in a vital area and you fall to the ground, without strength and badly wounded. The blow is deadly and you feel a cold that spreads from the wound all over your body quickly. Unable to move, the last thing you see is a slash of your enemy with his weapon straight into your heart.\n" +
				"You died while escaping from the cells.");
		place.getTranslations().add(new PlaceTranslation("es","La espada del esqueleto te alcanza en una zona vital y caes al suelo de espaldas,  sin fuerzas y malherido. El golpe es mortal y sientes un frío que se extiende desde la herida por todo cuerpo rápidamente. Incapaz de moverte, lo último que llegas a ver es un tajo de tu enemigo con su arma directo hacia tu corazón.\n" +
				"Has muerto en tu huida de las celdas."));
		return place;
	}
	
	public static Place getPlace10() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(10);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("You knock down the skeleton with one of your blows and all its bones cover the ground. His sword and his shield break as well, leaving only a heap of scattered bones. The weapons are useless, but you see that it was wearing a pair of green boots that you had not notice before. You pick them up and identify as defense boot, that will help you to protect yourself during the combats. You take them with you. \n" +
				"After going back to the oak table you do not see anyone else.");
		place.getTranslations().add(new PlaceTranslation("es","Derribas al esqueleto con uno de tus golpes y toda su osamenta se desmonta al impactar con el suelo. Su espada y su escudo se quiebran al mismo tiempo, quedando de él solamente un amasijo de huesos desperdigados. Sus armas están inservibles, pero observas que llevaba unas botas de tela verde que antes no habías visto. Las recoges e identificas que son unas botas de defensa que te ayudarán a protegerte durante los combates. Te las llevas. \n" +
				"Vuelves junto a la mesa de roble y no ves a nadie más."));
		place.getItemsCharacterGets().add(ItemManager.getInstance().getItem(1));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Go ahead through the corridor leaving the room behind you", 11));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Avanzas por el pasillo dejando atrás la estancia"));
		place.addPlayerAction(AchievementsHelper.WinSkeleton);
		place.addPlayerAction(AchievementsHelper.ItemBoots);
		return place;
	}
	
	public static Place getPlace11() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(11);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("You go ahead for a few meters through the corridor. From time to time you find more torches hanging on the walls, so you do not have to worry about lighting. \n" +
				"Finally, you come to a crossroads. You can turn right or left, but cannot keep going straight.\n" +
				"Where are you going now?");
		place.getTranslations().add(new PlaceTranslation("es","Avanzas en dirección recta durante unas decenas de metros por el pasillo. Cada cierta distancia encuentras más antorchas colgadas en los muros, por lo que no te tienes que preocupar de la iluminación. \n" +
				"Finalmente llegas a una encrucijada en la que no puedes seguir adelante. El pasillo se bifurca a izquierda y a derecha. \n" +
				"¿Por donde continúas?"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Turn right", 13));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Tuerces a la derecha"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Turn left", 12));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Tuerces a la izquierda"));

		return place;	
	}
	
	public static Place getPlace12() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(12);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("You go a few more steps forward and the corridor turns to the right. You continue through it while observing that this section is in a worse state of conservation. On the ground, there are remains of stones detached from the ceiling and some of the torches are extinguished.\n" +
				"A little further ahead you are forced to stop because of a big hole in the ground. It has to be about 3 meters long and it is as wide as the aisle. You get close to the edge and cannot see the bottom.\n" +
				"What are you doing next?");
		place.getTranslations().add(new PlaceTranslation("es","Avanzas unos cuantos pasos más y el pasillo tuerce a la derecha. Continúas por él mientras observas que esta sección se encuentra en peor estado de conservación. En el suelo te vas encontrando restos de piedras desprendidas del techo y algunas de las antorchas están apagadas.\n" +
				"Un poco más adelante te ves obligado a detenerte por un gran agujero en el suelo. Debe tener un tamaño de unos 3 metros de longitud y tanta anchura como el pasillo. Te acercas al borde y no consigues ver el fondo.\n" +
				"¿Qué haces a continuación?"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Cast a Levitation spell", 14));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Lanzar un sortilegio de Levitación"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Jump over the pit", 15));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Dar un salto para evitar el obstáculo"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo(" Go back to the junction and follow the corridor to the right", 13));
		place.getPlacesToGo().get(2).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Retroceder hasta la encrucijada y seguir por el pasillo de la derecha"));

		return place;	
	}
	
	public static Place getPlace13() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(13);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("You go forward a few more steps and the corridor turns left. You continue through it for some meters and observe a wooden door on the right side. It seems a little rickety and old, and it is mostly rotten. It is unlocked and the knob does not look too bad.");
		place.getTranslations().add(new PlaceTranslation("es","Avanzas unos cuantos pasos más y el pasillo tuerce a la izquierda. Continúas por él durante unas decenas de metros y observas una puerta de madera en el lado derecho. Parece algo desvencijada y vieja y está podrida en su mayor parte. No tiene cerradura y el pomo parece que no está en muy mal estado."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Open the door and see what it is behind", 21));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Si quieres abrir la puerta y ver lo que hay detrás"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Keep on the corridor and ignore the door", 20));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Si quieres continuar por el pasillo e ignorar la puerta"));

		return place;
	}
	
	public static Place getPlace14() {
		SpellUsePlace place = new SpellUsePlace();
		place.setId(14);
		place.setPlaceType(getPlaceType(PlaceType.IdSpellUsePlace));
		place.setText("You review your list of spells in your mind, trying to remember the gestures and magic words needed to cast the spell.");
		place.getTranslations().add(new PlaceTranslation("es","Repasas mentalmente tu compendio de hechizos intentando recordar los gestos y las palabras mágicas necesarias para ejecutar el hechizo."));
		place.getPlacesToGo().add(new SpellUsePlaceToGo(Spell.findSpells(Spell.getSpells(), Spell.IdLevitation), 17));
		place.setIdPlaceToGoIfNoSpell(19);

		return place;	
	}

	public static Place getPlace15() {
		AttributeCheckPlace place = new AttributeCheckPlace();
		place.setId(15);
		place.setPlaceType(getPlaceType(PlaceType.IdAttributeCheckPlace));
		place.setText("You go a few steps back and run to make the jump as long as possible. Once you are closed to the edge, you jump.");
		place.getTranslations().add(new PlaceTranslation("es","Retrocedes unos pasos y coges carrerilla para efectuar el salto con impulso. Avanzas en carrera hacia el agujero y cerca del borde das el salto."));
		place.setAttribute(Attribute.findAttribute(Attribute.getAttributes(), Attribute.IdAgility));
		place.setDifficulty(Difficulty.getDifficulty(10, false));
		place.setIdPlaceToGoIfPass(18);
		place.setIdPlaceToGoIfFail(16);
		
		return place;
	}

	public static Place getPlace16() {
		EndAdventurePlace place = new EndAdventurePlace();
		place.setId(16);
		place.setPlaceType(getPlaceType(PlaceType.IdEndAdventurePlace));
		place.setText("Your jump is too short. You try to grab the other side, but you cannot. You scream in despair while falling to the bottom of the pit about 20 meters down. Your body breaks in dozens of places due to the impact on the hard ground and you die.\n" +
				"You died while escaping from the cells.");
		place.getTranslations().add(new PlaceTranslation("es","Tu salto se queda demasiado corto. Intentas alargar los brazos en el aire para agarrarte al otro borde pero no lo consigues. Lanzas un chillido de desesperación mientras caes hasta el fondo del foso de unos 20 metros de altura. Tu cuerpo se fractura por decenas de sitios a causa del impacto contra el duro suelo y mueres por las heridas.\n" +
				"Has fracasado en tu huida de las celdas."));
		return place;
	}

	public static Place getPlace17() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(17);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("After casting the spell, you are not under the effects of gravity and your body begins to float in the air. You put your hands on the walls and move yourself to the other side of the pit. Once you are safe, you cancel the spell and go your way.");
		place.getTranslations().add(new PlaceTranslation("es","Tras lanzar el sortilegio te liberas de los efectos de la gravedad y tu cuerpo empieza a flotar en el aire. Te apoyas con las manos en la paredes y agarrándote a los salientes te desplazas por encima del foso hasta el otro lado. Cancelas el sortilegio una vez estás a salvo y sigues tu camino."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("You continue down the corridor and turn right again", 20));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Continúas por el pasillo y tuerces a la derecha nuevamente"));

		return place;
	}
	
	public static Place getPlace18() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(18);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("Your jump is powerful and you can fly over the pit. You land hard on your feet on the other side. After taking a look behind you feel safe and continue on your way.");
		place.getTranslations().add(new PlaceTranslation("es","Tu salto es potente y logras sobrevolar el foso. Aterrizas con fuerza sobre tu pies en el otro lado. Echas un vistazo atrás aliviado por el peligro superado y continúas tu camino."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("You continue down the corridor and turn right again", 20));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Continúas por el pasillo y tuerces a la derecha nuevamente"));
		place.addPlayerAction(AchievementsHelper.AgilitySuccess);
		return place;
	}
	
	public static Place getPlace19() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(19);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("The big hole in the ground is still in front of you, preventing you from going down the corridor.\n" +
				"What are you doing?");
		place.getTranslations().add(new PlaceTranslation("es","El gran agujero en el suelo sigue delante de ti, impidiéndote avanzar por el pasillo.\n" +
				"¿Qué haces?"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Make a jump to avoid the obstacle", 15));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Dar un salto para evitar el obstáculo "));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Go back to the junction and follow the corridor to the right", 13));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Retroceder hasta la encrucijada y seguir por el pasillo de la derecha"));

		return place;
	}
	
	public static Place getPlace20() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(20);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("After a few more steps you arrive to a new junction. The only way out is going forward or turn to the new corridor that is on one side. You automatically discard the straight corridor that continues straight because it looks like it turns around and returns to the cells. You can also see a stone plate on  the wall in front of the forking corridor.");
		place.getTranslations().add(new PlaceTranslation("es","Tras avanzar unos pasos más llegas a una nuevo cruce. Las única salidas posibles son seguir adelante o torcer por el nuevo pasillo que se abre a un lado. Descartas automáticamente el pasillo que continúa recto pues parece que más adelante da la vuelta y vuelve hacia las celdas. También observas una placa hecha de piedra que sobresale del muro que hay frente al pasillo que se bifurca, a 1 metro de altura del suelo."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Look at the plate a little closer", 23));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Decides examinar la placa más de cerca"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Turn and keep going ahead", 27));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Tuerces y sigues adelante"));

		return place;
	}
	
	public static Place getPlace21() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(21);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("You open the door and see a small room full of racks and shattered furniture, food scraps and old broken objects are scattered everywhere. The spiderwebs are all along the walls from the floor to the ceiling and there is no torch or other source of light.\n" +
				"What are you doing?");
		place.getTranslations().add(new PlaceTranslation("es","Abres la puerta y ves una pequeña habitación llena de estanterías y muebles destrozados, restos de comida y viejos objetos rotos y desperdigados por todos los rincones. Las telarañas van desde el suelo hasta el techo y no hay ninguna antorcha ni otra fuente de luz en el interior.\n" +
				"¿Que haces?"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Search for something useful in the room", 22));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Rebuscas entre los restos buscando algo útil"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Refuse, exit the room and continue down the aisle", 20));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Desistes y sales de la habitación para continuar por el pasillo"));

		return place;
	}
	
	public static Place getPlace22() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(22);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.getItemsCharacterGets().add(ItemManager.getInstance().getItem(2));
		place.setText("After a while seeking into garbage and rotten wood, you give up and leave the search. However, just as you were leaving, something bright around and caught your attention. In one corner of the room, underneath a pile of spiderwebs, you see some kind of vial. You remove everything around it and find a Healing Potion in perfect state. You take it with yourself.");
		place.getTranslations().add(new PlaceTranslation("es","Tras un rato inspeccionando basura y maderas podridas te das por vencido y dejas la búsqueda. No obstante, justo cuando te dabas la vuelta algo brillante llama tu atención por el rabillo del ojo. En una esquina de la habitación, bajo un montón de telarañas pareces ver una especie de vial. Retiras todo lo que lo rodea y encuentras una Poción de Curación en perfecto estado. Te la guardas."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Finally, exit the room and continue down the aisle", 20));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Finalmente, sales de la habitación para continuar por el pasillo"));
		place.addPlayerAction(AchievementsHelper.ItemLifePot);
		return place;
	}
	
	public static Place getPlace23() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(23);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("The plate is square-shaped and has strange symbols engraved on it, in two sets of horizontal lines. It looks like a sentence written using some kind of code or unusual alphabet. \n" +
				"What are you doing?");
		place.getTranslations().add(new PlaceTranslation("es","La placa es cuadrada y tiene unos símbolos extraños grabados sobre ella en dos series de líneas horizontales. Parece una frase escrita en clave o en un alfabeto poco común. \n" +
				"¿Que haces?"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Try to decrypt what it is written on the plate", 24));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Intentar descifrar lo que hay escrito sobre la placa"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Forget it and turn", 27));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "La olvidas y tuerces siguiendo tu camino"));

		return place;
	}
	
	public static Place getPlace24() {
		SkillCheckPlace place = new SkillCheckPlace();
		place.setId(24);
		place.setPlaceType(getPlaceType(PlaceType.IdSkillCheckPlace));
		place.setText("You try to remember where you have seen similar symbols before...");
		place.getTranslations().add(new PlaceTranslation("es","Haces memoria intentado recordar donde has visto unos símbolos parecidos anteriormente..."));
		place.setSkill(Skill.findSkill(Skill.getSkills(), Skill.IdKnowledge));
		place.setDifficulty(Difficulty.getDifficulty(8, false));
		place.setIdPlaceToGoIfPass(25);
		place.setIdPlaceToGoIfFail(26);
		
		return place;
	}
	
	public static Place getPlace25() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(25);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("The text is written in human letters. The even letters are turned vertically with respect to their usual shape and odd letters are turned horizontally. You also realize that the hidden message has been encrypted in a very simple way: each character has been replaced by the next one in the alphabet. You remember this method from your childhood days. The other kids called it \"The Dwarf's Encryption.\"\n" +
				"The message written in stone once deciphered is:\n" +
				"\"Reminder to the new guards: the password is Nasgorthas.\"");
		place.getTranslations().add(new PlaceTranslation("es","El grabado está escrito con letras humanas. Las letras pares está volteadas verticalmente con respecto a su forma habitual y las impares horizontalmente. Además te das cuenta que el mensaje oculto se ha cifrado de forma muy simple: cada carácter ha sido sustituido por el que le sigue en el alfabeto. Recuerdas este método de tus tiempos de niño. Los otros críos lo llamaban \"El cifrado del Enano\".\n" +
				"El mensaje escrito en piedra una vez descifrado dice lo siguiente:\n" +
				"\"Recordatorio para los guardias nuevos: la clave es Nasgorthas\""));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("After reading it, you leave and keep on your way", 27));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Tras leerlo, tuerces siguiendo tu camino "));
		place.addPlayerAction(AchievementsHelper.KnowledgeSuccess);
		return place;
	}
	
	public static Place getPlace26() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(26);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("You cannot arrive at any conclusion. You have no idea what those symbols mean and they do not look like anything you had seen in your life."); place.getTranslations().add(new PlaceTranslation("es","Por muchas vueltas que le das no consigues sacar nada en claro. No tienes ni idea de lo que significan esos símbolos y no te suena haber visto nada parecido en tu vida."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("A little disappointed, you leave and keep on your way", 27));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Resoplas decepcionado y tuerces siguiendo tu camino"));

		return place;
	}
	
	public static Place getPlace27() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(27);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("After a short walk down the corridor you end up in a narrow underground cavern. The walls seem to be excavated irregularly in the rock and the ceiling is more than 3 meters high. Small fragments of sparkling rocks are embedded everywhere gleaming with a phantasmagoric blue color illuminating the room. \n" +
				"In the the middle there is a humanoid creature with wide limbs and big body. Instead of flesh it appears to be made of stone and instead of eyes it has 2 points of bright blue light inside an irregularly shaped head. It is not wearing any clothes or anything like that, and it is not carrying any objects neither.\n" +
				"The golem immediately points his face at you and talks in a mechanical, powerful voice:\n"
				+ "- What is the password?");
		place.getTranslations().add(new PlaceTranslation("es","Tras recorrer un corto pasillo acabas en una angosta caverna subterránea. Las paredes parecen estar excavadas de forma irregular en la roca y el techo alcanza más de 3 metros de altura. Pequeños fragmentos de rocas brillantes incrustados por todas partes refulgen con un color azulado fantasmagórico iluminando la estancia. \n" +
				"En el centro exacto se halla un criatura humanoide de miembros anchos y cuerpo orondo. En lugar de carne parece estar hecho de piedra y por ojos tiene 2 puntos de luz azul brillante, enmarcados dentro de una cabeza de forma irregular. No lleva ropa ni nada parecido ni porta ningún objeto.\n" +
				"Al instante el golem apunta su rostro hacia ti y te habla con una voz mecánica y potente:\n" +
				"- ¿Cual es la clave?"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Say a password", 28));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Le dices una clave"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Do not know the password or prefer fighting", 31));
		place.getPlacesToGo().get(1).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "No sabes la clave o prefieres abrirte camino combatiendo"));

		return place;
	}
	
	public static Place getPlace28() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(28);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("What is the password you say loudly?");
		place.getTranslations().add(new PlaceTranslation("es","¿Cual es la clave que dices en voz alta?"));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Fugoldiel", 30));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Penraor", 30));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Gorresh", 30));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Zarael", 30));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Ridesc", 30));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Fridhuconn", 30));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Nasgorthas", 29));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Marannlen", 30));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Argar", 30));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Bogrim", 30));

		return place;
	}
	
	public static Place getPlace29() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(29);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("After listening to your words, the golem moves aside and allows you to pass. You run quickly across the cavern and find an exit dug into the rock on the opposite side from where you entered. You walk into it and leave the place behind.");
		place.getTranslations().add(new PlaceTranslation("es","Tras escuchar tus palabras el golem se echa a un lado y te permite el paso. Corres rápidamente al otro lado de la caverna y encuentras una salida excavada en la roca en el lado opuesto por donde entraste. Entras por ella y dejas atrás el lugar."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Go forward through a new stone corridor", 6501));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Sigues adelante por un nuevo pasillo de piedra"));

		return place;
	}
	
	public static Place getPlace30() {
		CombatPlace place = new CombatPlace();
		place.setId(30);
		place.setPlaceType(getPlaceType(PlaceType.IdCombatPlace));
		place.setText("After listening to your words, the golem snarls and goes towards you with hostile intentions. It is a bit slow but seems pretty strong given its stony fists. You have to fight.");
		place.getTranslations().add(new PlaceTranslation("es","Tras escuchar tus palabras el golem emite un leve gruñido y avanza hacia ti con intenciones hostiles. Es algo lento pero parece bastante fuerte combatiendo con sus puños pétreos. Tienes que combatir con él."));
		place.setEnemy(EnemyManager.getInstance().getById(2));
		place.setIdPlaceToGoIfWin(33);
		place.setIdPlaceToGoIfLose(34);
		place.setIdPlaceToGoIfRunAway(35);
		
		return place;
	}

	public static Place getPlace31() {
		CombatPlace place = new CombatPlace();
		place.setId(31);
		place.setPlaceType(getPlaceType(PlaceType.IdCombatPlace));
		place.setText("After watching you preparing for combat, the golem snarls and moves towards you with hostile intentions. It is a bit slow but it looks pretty strong with its stony fists.");
		place.getTranslations().add(new PlaceTranslation("es","Al ver como te preparas para el combate el golem emite un leve gruñido y avanza hacia ti con intenciones hostiles. Es algo lento pero parece bastante fuerte combatiendo con sus puños pétreos."));
		place.setEnemy(EnemyManager.getInstance().getById(2));
		place.setIdPlaceToGoIfWin(33);
		place.setIdPlaceToGoIfLose(34);
		place.setIdPlaceToGoIfRunAway(35);
		
		return place;
	}

	public static Place getPlace32() {
		EndAdventurePlace place = new EndAdventurePlace();
		place.setId(32);
		place.setPlaceType(getPlaceType(PlaceType.IdEndAdventurePlace));
		place.setText("End of the beta.\nThanks for participating and do not forget to provide your feedback.");
		place.getTranslations().add(new PlaceTranslation("es", "Fin de Beta.\nGracias por participar y no te olvides de comunicar tus opiniones."));
		place.setPlayerDead(false);

		return place;
	}

	public static Place getPlace33() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(33);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("After the last strike, you see how the golem staggers. It takes a couple of steps backwards as its entire body disassembles into a small mountain of rubble and makes a loud noise that reverberates in the cave. Its bright eyes fade out. You have  defeated a mighty enemy.\n" +
				"You go across the cavern and find an exit dug into the rock on the opposite side of the cavern. You walk into it and leave the place behind you.");
		place.getTranslations().add(new PlaceTranslation("es","Tras asestar el último golpe, ves como el golem se tambalea. Da un par de pasos atrás mientras todo su cuerpo se desmonta formado una pequeña montaña de cascotes y produciendo un gran ruido que retumba en la cueva. Sus ojos brillantes se apagan. Has vencido a un poderoso enemigo.\n" +
				"Avanzas al otro lado de la caverna y encuentras una salida excavada en la roca en el lado opuesto por donde entraste. Entras por ella y dejas atrás el lugar."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Go forward through a new stone corridor", 6501));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Sigues adelante por un nuevo pasillo de piedra"));
		place.addPlayerAction(AchievementsHelper.WinGolem);
		return place;
	}
	
	public static Place getPlace34() {
		EndAdventurePlace place = new EndAdventurePlace();
		place.setId(34);
		place.setPlaceType(getPlaceType(PlaceType.IdEndAdventurePlace));
		place.setText("With a heavy punch, the golem knocks you down. The blows he has given you have been too strong and you have been unable to resist. With your last breath, you see how the sole of your rocky opponent gets closer to your face, killing you instantly.\n" +
				"You died while escaping from the cells.");
		place.getTranslations().add(new PlaceTranslation("es","Con un fuerte puñetazo el golem te derriba. Los golpes que te ha dado han sido demasiado fuertes y has sido incapaz de resistir. Con tu último aliento, ves como la planta del pie rocoso de tu oponente baja hacia tu rostro quitándote la vida en el acto.\n" +
				"Has muerto en tu huida de las celdas."));

		return place;
	}

	public static Place getPlace35() {
		OptionChoosePlace place = new OptionChoosePlace();
		place.setId(35);
		place.setPlaceType(getPlaceType(PlaceType.IdOptionChoosePlace));
		place.setText("Despite of its impressive appearance the golem is slow moving. You turn around it quickly while keeping some distance with its fists and you can get to the opposite side of the cavern.\n" +
				"There, you find an exit dug into the rock and you enter quickly into it.");
		place.getTranslations().add(new PlaceTranslation("es","A pesar de su aspecto imponente el golem es de movimientos lentos. Giras a su alrededor con rapidez manteniendo la distancia sin que te alcance con sus puños y logras desplazarte hasta el lado opuesto de la caverna.\n" +
				"Allí encuentras una salida excavada en la roca en la que rápidamente penetras."));
		place.getPlacesToGo().add(new OptionChoosePlaceCanGo("Go forward through a new stone corridor", 6501));
		place.getPlacesToGo().get(0).getTranslations().add(new OptionChoosePlaceCanGoTranslation("es", "Sigues adelante por un nuevo pasillo de piedra"));

		return place;
	}

	private static PlaceType getPlaceType(int idPlaceType) {
		return PlaceType.findPlaceType(listPlacestypes, idPlaceType);
	}
}
