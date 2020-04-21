package com.varrock.world.content;

import java.util.ArrayList;
import java.util.List;

import com.varrock.util.Misc;
import com.varrock.world.World;
import com.varrock.world.entity.impl.player.Player;

public class TriviaBot {

	public static final int TIMER = 1800;

	public static int botTimer = TIMER;

	public static int answerCount;

	public static String currentQuestion;

	private static String currentAnswer;

	private static List<String> winners = new ArrayList<String>(3);
	
	public static boolean didSend = false;

	public static void sequence() {
		
		if (--botTimer <= 1) {
			botTimer = TIMER;
			didSend = false;
			askQuestion();
		}
	}

	public static List<String> getWinners() {
		return winners;
	}
	
	public static void resetForNextQuestion() {
		if (!winners.isEmpty()) {
		    winners.clear();
		}
		answerCount = 0;
	}

	public static void attemptAnswer(Player p, String attempt) {

		if (hasAnswered(p)) {
			p.sendMessage("You have already answered the trivia question!");
			return;
		}

		if (!currentQuestion.equals("") && attempt.replaceAll("_", " ").equalsIgnoreCase(currentAnswer)) {

			if (answerCount == 0) {

				answerCount++;
				p.getPointsHandler().incrementTriviaPoints(10);
				p.getPacketSender().sendMessage("You received 10 trivia points for </col>1st Place.");
				winners.add(p.getUsername());
				return;
			}
			if (answerCount == 1) {

				answerCount++;
				p.getPointsHandler().incrementTriviaPoints(6);
				p.getPacketSender().sendMessage("You received 6 trivia points for </col>2nd Place.");
				winners.add(p.getUsername());
				return;

			}
			if (answerCount == 2) {
                p.getPointsHandler().incrementTriviaPoints(4);
				p.getPacketSender().sendMessage("You received 4 trivia points for </col>3rd Place.");
				winners.add(p.getUsername());
				
				
				
				World.sendMessage("<img=462><col=0037B1>[Trivia]: @bla@[1st:" + 
				(winners.size() > 0 ? winners.get(0) : "Nobody!") + "</col> (10 pts)</col>] </col>[2nd:" + 
						(winners.size() > 1 ? winners.get(1) : "Nobody!") + "</col> (6 pts)</col>] [3rd:" + 
				(winners.size() > 2 ? winners.get(2) : "Nobody!") + "</col>  (4 pts)</col>]");
				resetForNextQuestion();
				currentQuestion = "";
				didSend = false;
				botTimer = TIMER;
				answerCount = 0;
				return;
			}

		} else {
			if (attempt.contains("question") || attempt.contains("repeat")) {
				p.getPacketSender().sendMessage("<col=800000>" + (currentQuestion));
				return;
			}
			p.getPacketSender().sendMessage("<img=462><col=0037B1> [Trivia]: Sorry! Wrong answer! " + (currentQuestion));
			return;
		}

	}

	private static boolean hasAnswered(Player p) {
		for (int i = 0; i < winners.size(); i++) {
			if (winners.get(i).equalsIgnoreCase(p.getUsername())) {
				return true;
			}
		}
		return false;
	}

	public static boolean acceptingQuestion() {
		return currentQuestion != null && !currentQuestion.equals("");
	}

	private static void askQuestion() {

		for (int i = 0; i < TRIVIA_DATA.length; i++) {
			if (Misc.getRandom(TRIVIA_DATA.length - 1) == i) {
				if (!didSend) {
					didSend = true;
					currentQuestion = TRIVIA_DATA[i][0];
					currentAnswer = TRIVIA_DATA[i][1];
					resetForNextQuestion();
					World.sendMessage(currentQuestion);

				}
			}
		}
	}

	public static final String[][] TRIVIA_DATA = {
			{ "<img=462><col=0037B1> [Trivia]:</col> How many thieving stalls are there at the home area?", "5" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the name of the server?", "varrock" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What attack level do you need to wield an abyssal whip?", "70" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Where is the home area located?", "edgeville" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What mining level do you need to mine shooting stars?", "80" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the bandos boss called?", "General graardor" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the name of the clan chat everyone is in?", "help" },
			//{ "<img=462><col=0037B1> [Trivia]:</col> Which boss drops dragon kiteshields?", "king black dragon" },
			//{ "<img=462><col=0037B1> [Trivia]:</col> Which boss drops dark bows?", "phoenix" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Which monster drops whips?", "abyssal demon" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Which beast drops dark bows?", "dark beast" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Which boss drops dragon claws?", "tormented demon" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the level requirement to wear skillcapes?", "99" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the maximum combat level in varrock?", "138" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What defence level is required to wear barrows?", "70" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Where can you get void armour in varrock?", "pest control" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What weapon hits with melee, but it's special attack hits with magic?", "korasi" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the most powerful crossbow in the game?", "armadyl crossbow" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Who is the owner of varrock?", "Harrison" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Who is the developer of varrock?", "Harrison" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Where can you get dharoks armour?", "barrows" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What miniquest grants access to barrows gloves?", "recipe for disaster" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What combat level are tormented demons?", "450" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What combat level is nex?", "1001" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What combat level is corporeal beast", "785" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What combat level are rock crabs?", "13" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What combat level is kree'arra?", "580" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What boss drops dragon warhammer?", "lizardman shaman" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What boss drops divine sigils?", "corporeal beast" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the best offensive mage prayer in the normal prayer book?", "augury" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many skills are there in varrock", "25" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the best offensive range prayer in the normal prayer book?", "rigour" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is your total level if you have 99 in every skill in varrock?", "2475" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What trees do you cut for magic logs?", "Magic" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the highest level rock to mine", "runite" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Where can you fight other players for their loot?", "wilderness" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the cape for complete players?", "completionist cape" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the cape for max players?", "max cape" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What skill makes potions?", "herblore" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What skill lets you make weapons and armour?", "smithing" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Where can you store money other than the bank", "money pouch" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Where do you store all of your items?", "bank" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What points do you get for killing bosses?", "boss points" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many free slots does each bank tab have?", "352" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What skill advances your combat level past 126?", "summoning" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What food heals the most in varrock", "rocktail" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What should I do every day to help the server?", "vote" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What skill do I use when crafting runes", "runecrafting" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many elite achievement tasks are there?", "13" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What boss drops toxic staff of the dead?", "Skotizo" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many dungeoneering tokens is an arcane stream necklace?", "75000" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many dungeoneering tokens are chaotics?", "200000" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the cube root of 216?", "6" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many time can you vote a day?", "2" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What boss drops the dragon kiteshield?", "King Black Dragon" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What slayer level does the master Sumona require?", "92" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What slayer level does the master Kuradel require?", "80" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What slayer level does the master Duradel require?", "50" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Who is the default slayer master?", "Vannaka" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What level herblore is required to make overloads?", "96" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What NPC will help you with your account security?", "Town Crier" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What summoning level is required to make a Talon Beast?", "77" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What summoning level is required to make a Ravenous Locust?", "70" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What summoning level is required to make an Iron Minotaur?", "46" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What summoning level is required to make a Spirit Larupia?", "57" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What summoning level is required to make an Moss Titan?", "79" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How much xp do you need to reach 99?", "13034431" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the maximum amount of cash you can hold in your inventory?", "2147483647" },
			{ "<img=462><col=0037B1> [Trivia]:</col> At what level prayer can you use Hawk Eye?", "26" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Chickens are there at the Chicken Pen?", "8" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many bales of Hay are there south east of Draynor Manor?", "27" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Which NPC will be able to give you a title?", "Sir Vyvin" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What was Herblore's name originally called?", "Herblaw" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many coins does a monkfish alch for?", "3200" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the killcount requirement to enter a lair in Godwars?", "20" },
			{ "<img=462><col=0037B1> [Trivia]:</col> In what month was varrock released?", "October" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "1" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "2" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "3" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "4" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "5" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "6" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "7" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "8" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "9" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Guess a number 1-10?", "10" },

			{ "<img=462><col=0037B1> [Trivia]:</col> What smithing level is required to smith a Steel Plateskirt?", "46" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What monster gives the examine 'A vicious thief'?", "bandit" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the second boss to kill in the Recipe for Disaster quest?", "karamel" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many points does a Void Knight Deflector cost?", "350" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What construction level is required to create Hangman?", "59" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the 40th spell on the regular spell book?", "vulnerability" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the 20th item in the Tzhaar shop?", "toktz-mej-tal" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the 21st item in the Farming shop?", "potato seed" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many boss points does a Zamorakian Spear cost?", "80" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the 17th emote?", "cry" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the 24th emote?", "goblin salute" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Bankers are there in Seers' Village bank?", "6" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Runes are on the Magic thieving stall?", "3" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the 35th Achievement?", "fish 25 rocktails" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Green Dragons are there at West Dragons?", "4" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Inventory Spaces does a Pack Yak have?", "30" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Inventory Spaces does a Spirit Terrorbird have?", "12" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Inventory Spaces does a War Tortoise have?", "18" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What monster gives the examine 'The tongue of evil'?", "bloodveld" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What monster gives the examine 'An evil magic user'?", "infernal mage" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Agility courses are there?", "3" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Easy Tasks are there?", "30" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Medium Tasks are there?", "31" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Hard Tasks are there?", "32" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Elite Tasks are there?", "12" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How much money can the Well of Goodwill hold?", "400m" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the 8th item in the Runecrafting Shop?", "chaos talisman" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Name the NPC that is wearing Torva", "Max" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the name of the skilling pet you get from Farming?", "tangleroot" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the name of the skilling pet you get from Woodcutting?", "beaver" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the name of the skilling pet you get from Mining?", "rock golem" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the name of the skilling pet you get from Fishing?", "Heron" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the name of the skilling pet you get from Thieving?", "Rocky" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the name of the skilling pet you get from Agility?", "giant squirrel" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What is the name of the skilling pet you get from Runecrafting?",
					"rift guardian" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Name the NPC that is holding a Bell?", "town crier" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Which NPC sells Skill Capes?", "wise old man" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many Crates are there in the Varrock general store?", "10" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How much gold do you need to pay to get through the gate to Al Kharid?", "10" },
			{ "<img=462><col=0037B1> [Trivia]:</col> How many thieving stalls are there at Ardougne Market place?", "16" },
			{ "<img=462><col=0037B1> [Trivia]:</col> What object gives the option to Dump-weeds", "compost bin" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Which skill shows an image of a Fist", "strength" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Which skill shows an image of a Wolf?", "summoning" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Which skill shows an image of a Ring?", "dungeoneering" },
			{ "<img=462><col=0037B1> [Trivia]:</col> Which skill shows an image of Paw?", "hunter" },
			{ "<img=462><col=0037B1> [Trivia]:</col> type the following ::answer jdj49a39ru357cnf", "jdj49a39ru357cnf" },
			{ "<img=462><col=0037B1> [Trivia]:</col> type the following ::answer qpal29djeifh58cjid", "qpal29djeifh58cjid" },
			{ "<img=462><col=0037B1> [Trivia]:</col> type the following ::answer qd85d4r0md42u2mssd", "qd85d4r0md42u2mssd" },
			{ "<img=462><col=0037B1> [Trivia]:</col> type the following ::answer loski4893dhncbv7539", "loski4893dhncbv7539" },
			{ "<img=462><col=0037B1> [Trivia]:</col> type the following ::answer 9esmf03na9admieutapdz9", "9esmf03na9admieutapdz9" },
			{ "<img=462><col=0037B1> [Trivia]:</col> type the following ::answer djs83adm39s88s84masl", "djs83adm39s88s84masl" },
			{ "<img=462><col=0037B1> [Trivia]:</col> type the following ::answer alskpwru39020dmsa3aeamap",
					"alskpwru39020dmsa3aeamap" } };
}