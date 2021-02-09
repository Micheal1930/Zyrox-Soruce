package com.zyrox.world.content;

import com.zyrox.world.entity.impl.player.Player;

public class PointsHandler {

	private Player p;

	public PointsHandler(Player p) {
		this.p = p;
	}

	public void reset() {
		dungTokens = commendations = (int) (loyaltyPoints = votingPoints = slayerPoints = pkPoints = 0);
		p.getPlayerKillingAttributes().setKillstreak(0);
		p.getPlayerKillingAttributes().setPlayerKills(0);
		p.getPlayerKillingAttributes().setPlayerDeaths(0);
		p.getDueling().arenaStats[0] = p.getDueling().arenaStats[1] = 0;
	}

	private int prestigePoints;
	private int triviaPoints;
	private int slayerPoints;
	private int commendations;
	private int dungTokens;
	private int pkPoints;
	private double loyaltyPoints;
	private int votingPoints;
	private int achievementPoints;
	private int donationPoints;

	public int getPrestigePoints() {
		return prestigePoints;
	}

	public void setPrestigePoints(int points, boolean add) {
		if (add)
			this.prestigePoints += points;
		else
			this.prestigePoints = points;
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}

	public int getDonationPoints() {
		return donationPoints;
	}

	public void setDonationPoints(int donationPoints, boolean add) {
		if (add)
			this.donationPoints += donationPoints;
		else
			this.donationPoints = donationPoints;
	}

	public void incrementDonationPoints(double amount) {
		this.donationPoints += amount;
	}

	public int getTriviaPoints() {
		return triviaPoints;
	}

	public void setTriviaPoints(int triviaPoints, boolean add) {
		if (add)
			this.triviaPoints += triviaPoints;
		else
			this.triviaPoints = triviaPoints;
	}

	public void incrementTriviaPoints(double amount) {
		this.triviaPoints += amount;
	}

	public void setSlayerPoints(int slayerPoints, boolean add) {
		if (add)
			this.slayerPoints += slayerPoints;
		else
			this.slayerPoints = slayerPoints;
	}

	public int getCommendations() {
		return this.commendations;
	}

	public void setCommendations(int commendations, boolean add) {
		if (add)
			this.commendations += commendations;
		else
			this.commendations = commendations;
	}

	public int getLoyaltyPoints() {
		return (int) this.loyaltyPoints;
	}

	public void setLoyaltyPoints(int points, boolean add) {
		if (add)
			this.loyaltyPoints += points;
		else
			this.loyaltyPoints = points;
	}

	public void incrementLoyaltyPoints(double amount) {
		this.loyaltyPoints += amount;
	}

	public int getPkPoints() {
		return this.pkPoints;
	}

	public void setPkPoints(int points, boolean add) {
		if (add)
			this.pkPoints += points;
		else
			this.pkPoints = points;
	}

	public int getDungeoneeringTokens() {
		return dungTokens;
	}

	public void setDungeoneeringTokens(int dungTokens, boolean add) {
		if (add)
			this.dungTokens += dungTokens;
		else
			this.dungTokens = dungTokens;
	}

	public int getVotingPoints() {
		return votingPoints;
	}

	public void setVotingPoints(int votingPoints) {
		this.votingPoints = votingPoints;
	}

	public void incrementVotingPoints() {
		this.votingPoints++;
	}

	public void incrementVotingPoints(int amt) {
		this.votingPoints += amt;
	}

	public void setVotingPoints(int points, boolean add) {
		if (add)
			this.votingPoints += points;
		else
			this.votingPoints = points;
	}

	public int getAchievementPoints() {
		return achievementPoints;
	}

	public void setAchievementPoints(int points, boolean add) {
		if (add)
			this.achievementPoints += points;
		else
			this.achievementPoints = points;
	}
}
