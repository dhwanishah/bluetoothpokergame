package com.example.bluetoothspp;
import java.util.ArrayList;


public class PokerPlayer 
{
	//betting variables
	private int totalFunds;
	private int currentBet;
	private int playerNumber;
	public ArrayList<Integer> availablePots;
	private boolean allIn;
	private String betType;
	private boolean folded;
	//game logic variables
	private ArrayList<Card> hand;
	private ArrayList<Card> bestHand;
	private Score handScore;
	
	public PokerPlayer(int totalFunds, int playerNumber)
	{//initialize variables
		folded = false;
		allIn = false;
		this.totalFunds = totalFunds;
		currentBet = 0;
		betType = "";
		this.playerNumber = playerNumber;
		
		handScore = Score.High_Card;
		hand = new ArrayList<Card>();
		availablePots = new ArrayList<Integer>();
		availablePots.add(0);
	}
	
	public void ReceiveCard(Card card)
	{
		hand.add(card);
	}
	
	public ArrayList<Card> GetCards()
	{
		return hand;
	}
	
	public void SetFolded(boolean foldValue)
	{
		folded = foldValue;
	}
	
	public boolean Folded()
	{
		return folded;
	}
	
	public void SetScore(Score score)
	{
		handScore = score;
	}
	
	public Score GetScore()
	{
		return handScore;
	}
	
	public ArrayList<Card> GetBestHand()
	{
		return bestHand;
	}
	public void SetBestHand(ArrayList<Card> possibleHand, Score score)
	{
		//first check to see if the incoming hand is better than the current best hand in terms of score
		if(score.ordinal() < handScore.ordinal())
			return;
		int cardsAlreadySorted = 0;
		//arrange the cards in a certain order based on hand ranking
		
		switch(score)
		{
			case High_Card:
			{
				//no special sorting required
				cardsAlreadySorted = 0;
				break;
			}
			case One_Pair:
			{
				//first determine which rank has a pair
				int rankToMatch = 0;
				for(int i = 0; i < 4; i++)
				{
					int matchCount = 1;
					rankToMatch = possibleHand.get(i).rank;
					for(int j = 0; j < possibleHand.size(); j++)
					{
						if(j == i)
							continue; //we don't want to compare the card to itself
						if(possibleHand.get(j).rank == rankToMatch)
							matchCount++;
					}
					
					if(matchCount == 2)
					{
						break;
					}
				}
				//prioritize the pair
				//because the hand has a ranking of one pair, there should be exactly 2 cards of the same rank in the hand
				int rankCount = 0;
				for(int i = 0; i < possibleHand.size(); i++)
				{
					if(possibleHand.get(i).rank == rankToMatch)
					{
						//simple swap
						Card tempCard = possibleHand.get(rankCount);
						possibleHand.set(rankCount, possibleHand.get(i));
						possibleHand.set(i, tempCard);
						rankCount++;
					}
				}
				cardsAlreadySorted = 2;
				break;
			}
			case Two_Pair:
			{
				int highPairRank;
				int lowPairRank;
				
				//get the first pair
				int firstPairRank = 0;
				for(int i = 0; i < possibleHand.size(); i++)
				{
					int matchCount = 1;
					int rankToMatch = possibleHand.get(i).rank;
					for(int j = 0; j < possibleHand.size(); j++)
					{
						if(j == i)
							continue; //we don't want to compare the card to itself
						if(possibleHand.get(j).rank == rankToMatch)
							matchCount++;
					}
					
					if(matchCount == 2)
					{
						firstPairRank = rankToMatch;
						break;
					}
				}
				
				//get the second pair
				int secondPairRank = 0;
				for(int i = 0; i < possibleHand.size(); i++)
				{
					int matchCount = 1;
					int rankToMatch = possibleHand.get(i).rank;
					for(int j = 0; j < possibleHand.size(); j++)
					{
						if(j == i)
							continue; //we don't want to compare the card to itself
						if(possibleHand.get(j).rank == rankToMatch)
							matchCount++;
					}
					
					if(matchCount == 2 && rankToMatch != firstPairRank)
					{
						secondPairRank = rankToMatch;
						break;
					}
				}
				
				//determine which of the ranks is higher
				if(firstPairRank > secondPairRank)
				{
					highPairRank = firstPairRank;
					lowPairRank = secondPairRank;
				}
				else
				{
					highPairRank = secondPairRank;
					lowPairRank = firstPairRank;
				}
				
				//prioritize the hand in the following order:
				//[high pair] [low pair] [remaining card]
				//first move each type of card to its own arraylist
				ArrayList<Card> highRankList = new ArrayList<Card>();
				ArrayList<Card> lowRankList = new ArrayList<Card>();
				ArrayList<Card> remainingCardsList = new ArrayList<Card>();
				
				for(int i = 0; i < possibleHand.size(); i++)
				{
					if(possibleHand.get(i).rank == highPairRank)
						highRankList.add(possibleHand.get(i));
					else if(possibleHand.get(i).rank == lowPairRank)
						lowRankList.add(possibleHand.get(i));
					else
						remainingCardsList.add(possibleHand.get(i));
				}
				
				//clear out the original list
				possibleHand.clear();
				//and add the cards in the required order
				possibleHand.add(highRankList.get(0));
				possibleHand.add(highRankList.get(1));
				possibleHand.add(lowRankList.get(0));
				possibleHand.add(lowRankList.get(1));
				possibleHand.add(remainingCardsList.get(0));
				
				cardsAlreadySorted = 4;
				break;
			}
			case Three_Of_A_Kind:
			{
				//first determine which rank has three matches
				int rankToMatch = 0;
				for(int i = 0; i < 3; i++)
				{
					int matchCount = 1;
					rankToMatch = possibleHand.get(i).rank;
					for(int j = 0; j < possibleHand.size(); j++)
					{
						if(j == i)
							continue; //we don't want to compare the card to itself
						if(possibleHand.get(j).rank == rankToMatch)
							matchCount++;
					}
					
					if(matchCount == 3)
					{
						break;
					}
				}
				//prioritize the three matching ranks
				int rankCount = 0;
				for(int i = 0; i < possibleHand.size(); i++)
				{
					if(possibleHand.get(i).rank == rankToMatch)
					{
						//simple swap
						Card tempCard = possibleHand.get(rankCount);
						possibleHand.set(rankCount, possibleHand.get(i));
						possibleHand.set(i, tempCard);
						rankCount++;
					}
				}
				cardsAlreadySorted = 3;
				break;
			}
			case Straight:
			{
				//no special sorting required
				cardsAlreadySorted = 0;
				break;
			}
			case Flush:
			{
				//no special sorting required
				cardsAlreadySorted = 0;
				break;
			}
			case Full_House:
			{
				//we first have to determine which rank is 3 of a kind and which is just a pair
				int threeRank = 0;
				
				//start by finding the rank with 3 matches
				for(int i = 0; i < possibleHand.size(); i++)
				{
					int matchCount = 1;
					int rankToMatch = possibleHand.get(i).rank;
					for(int j = 0; j < possibleHand.size(); j++)
					{
						if(j == i)
							continue; //we don't want to compare the card to itself
						if(possibleHand.get(j).rank == rankToMatch)
							matchCount++;
					}
					
					if(matchCount == 3)
					{
						threeRank = rankToMatch;
						break;
					}
				}
				//prioritize the hand in the following order:
				//[matching 3] [matching pair]
				//first move each type of card to its own arraylist
				ArrayList<Card> threeRankList = new ArrayList<Card>();
				ArrayList<Card> twoRankList = new ArrayList<Card>();
				//the only rank left in the hand besides the matching 3 will be the pair
				for(int i = 0; i < possibleHand.size(); i++)
				{
					if(possibleHand.get(i).rank == threeRank)
						threeRankList.add(possibleHand.get(i));
					else
						twoRankList.add(possibleHand.get(i));
				}
				
				//clear out the original list
				possibleHand.clear();
				//and add the cards in the required order
				possibleHand.add(threeRankList.get(0));
				possibleHand.add(threeRankList.get(1));
				possibleHand.add(threeRankList.get(2));
				possibleHand.add(twoRankList.get(0));
				possibleHand.add(twoRankList.get(1));
				
				cardsAlreadySorted = 5;
				break;
			}
			case Four_Of_A_Kind:
			{
				//first determine which rank has four matches
				int rankToMatch = 0;
				for(int i = 0; i < 2; i++)
				{
					int matchCount = 1;
					rankToMatch = possibleHand.get(i).rank;
					for(int j = 0; j < possibleHand.size(); j++)
					{
						if(j == i)
							continue; //we don't want to compare the card to itself
						if(possibleHand.get(j).rank == rankToMatch)
							matchCount++;
					}
					
					if(matchCount == 4)
					{
						break;
					}
				}
				//prioritize the four matching ranks
				int rankCount = 0;
				for(int i = 0; i < possibleHand.size(); i++)
				{
					if(possibleHand.get(i).rank == rankToMatch)
					{
						//simple swap
						Card tempCard = possibleHand.get(rankCount);
						possibleHand.set(rankCount, possibleHand.get(i));
						possibleHand.set(i, tempCard);
						rankCount++;
					}
				}
				cardsAlreadySorted = 4;
				break;
			}
			case Straight_Flush:
			{
				//no special sorting required
				cardsAlreadySorted = 0;
				break;
			}
		}
		//simple bubble sort the remaining cards in the hand
		boolean swap = true;
		Card temp;
		
		while(swap)
		{
			swap = false;
			for(int i = cardsAlreadySorted; i < possibleHand.size() - 1; i++)
			{
				if(possibleHand.get(i).rank < possibleHand.get(i + 1).rank)
				{
					temp = possibleHand.get(i);
					possibleHand.set(i, possibleHand.get(i + 1));
					possibleHand.set(i + 1, temp);
					swap = true;
				}
			}
		}
		
		//compare the current best hand with the incoming hand
		//first check to see if the best hand is still null
		if(bestHand == null)
		{
			bestHand = new ArrayList<Card>();
			for(Card tempCard: possibleHand)
				  bestHand.add((Card)tempCard.clone());
			handScore = score;
		}
		else if(score.ordinal() > handScore.ordinal())
		{
			bestHand.clear();
			for(Card card: possibleHand)
				  bestHand.add((Card)card.clone());
			handScore = score;
		}
		else
		{
			for(int i = 0; i < bestHand.size(); i++)
			{
				if(bestHand.get(i).rank < possibleHand.get(i).rank)
				{
					bestHand.clear();
					for(Card card: possibleHand)
						  bestHand.add((Card)card.clone());
					break;
				}
			}
		}
	}
	
	//function used to bet and call/check
	public int BetChips(int numberBet)
	{
		//check to see if the number bet was valid
		if(numberBet <= getTotalFunds())
		{
			setTotalFunds(getTotalFunds() - numberBet);
			return 0;
		}
		else
			return -1;
	}
	
	public void Fold()
	{
		setFolded(true);
	}
	
	public void AllIn()
	{
		setAllIn(true);
		setCurrentBetType("ALL IN");
		System.out.println("Player went ALL IN!");
	}
	
	public String getCurrentBetType() {
		return betType;
	}

	public void setCurrentBetType(String currentBetType) {
		betType = currentBetType;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNum) {
		playerNumber = playerNum;
	}

	public int getTotalFunds() {
		return totalFunds;
	}

	public void setTotalFunds(int numChips) {
		totalFunds = numChips;
	}

	public boolean isFolded() {
		return folded;
	}

	public void setFolded(boolean isFold) {
		folded = isFold;
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
		
		if(getTotalFunds() == 0 && currentBet != 0)
			AllIn();
	}

	public boolean isAllIn() {
		return allIn;
	}

	public void setAllIn(boolean allIn) {
		this.allIn = allIn;
	}
	
	public void AddPot(int potIndex)
	{
		availablePots.add(potIndex);
	}
	
	public void ClearPots()
	{
		availablePots.clear();
	}
	
	public void AddFunds(int funds)
	{
		totalFunds += funds;
	}
	
	public void NewHand()
	{
		hand.clear();
		bestHand = null;
		handScore = Score.High_Card;
		availablePots.clear();
		availablePots.add(0);
	}
}
