package com.example.bluetoothspp;
import java.io.Serializable;
import java.util.*;


@SuppressWarnings("serial")
public class PokerGame implements Serializable
{
	
	public static String readMessage;
	BluetoothService bs = MainActivity.mChatService;
	public static String currentPot;
	
	private ArrayList<Card> cardDeck;
	private int numPlayers;
	public Table table;
	public ArrayList<PokerPlayer> players;
	public ArrayList<PokerPlayer> activePlayers;
	public static ArrayList<Integer> pots;
	private Scanner scan;
	private int numFolded;
	private int numAllIn;
	public static int currentPotIndex;
	private int currentBet;
	private int bigBlind;
	private int ante;
	
	private static final int startingFunds = 1000;
	private static final boolean playingAnte = false;
	private static final int INVALID_PLAYER = -2;
	
	public PokerGame(int numberPlayers, int bigBlind, int ante)
	{
		numPlayers = numberPlayers;
		//initialize the player array
		players = new ArrayList<PokerPlayer>();
		for(int i = 0; i < numPlayers; i++)
			players.add(new PokerPlayer(startingFunds, i+1));
		
		pots = new ArrayList<Integer>();
		currentPotIndex = 0;
		
		//initialize the deck
		cardDeck = new ArrayList<Card>();
		
		//initialize the table
		table = new Table();
		
		//initialize the string array representing the deck and fill it with all 52 cards
		ResetDeck();
		
		numFolded = 0;
		numAllIn = 0;
		
		this.bigBlind = bigBlind;
		this.ante = ante;
		
		scan = new Scanner(System.in);
	}
	
	public void DealCards(int numCards)
	{//deal the passed in number of cards to each player and remove the corresponding number of cards from the deck
		for(int i = 0; i < numCards; i++)
			for(int j = 0; j < numPlayers; j++)
			{
				if(!players.get(j).Folded())
				{//deal the player the top card of the deck
					players.get(j).ReceiveCard(cardDeck.get(0));
					//then remove that card from the deck array
					cardDeck.remove(0);
				}
			}
	}
	public void DealTableCards(int numCards)
	{//deal the passed in number of cards to the table
		for(int i = 0; i < numCards; i++)
		{//deal the table the top card of the deck
			table.ReceiveCard(cardDeck.get(0));
			//then remove that card from the deck array
			cardDeck.remove(0);
		}
	}
	public void BurnCard()
	{
		table.Discard(cardDeck.get(0));
		cardDeck.remove(0);
	}
	
	public void ResetDeck()
	{
		cardDeck.clear();
		//add each card to the deck
		//ace is considered a high card so mark it as 14
		for(int i = 2; i < 15; i++)
			cardDeck.add(new Card(i, 'h'));			//hearts suit
		for(int i = 2; i < 15; i++)
			cardDeck.add(new Card(i, 'd'));			//diamonds suit
		for(int i = 2; i < 15; i++)
			cardDeck.add(new Card(i, 's'));			//spades suit
		for(int i = 2; i < 15; i++)
			cardDeck.add(new Card(i, 'c'));			//clubs suit
		
		//give the deck an initial shuffle
		ShuffleDeck();
	}
	public void ShuffleDeck()
	{
		Random rand = new Random();
		for(int i = 0; i < cardDeck.size(); i++)
		{
			int randIndex = rand.nextInt(i + 1);
			//simple swap
			Card temp = cardDeck.get(randIndex);
			cardDeck.set(randIndex, cardDeck.get(i));
			cardDeck.set(i, temp);
		}
	}
	
	public void PrintDeck()
	{
		for(int i = 0; i < cardDeck.size(); i++)
			System.out.print(cardDeck.get(i) + " ");
	}
	
	public void determineWinner()
	{
		for(int i = 0; i < numPlayers; i++)
		{			
			ArrayList<Card> arr = new ArrayList<Card>();
			arr.addAll(players.get(i).GetCards());
			arr.addAll(table.GetCards());
		    ArrayList<Card> data = new ArrayList<Card>();
		    //create blank cards for the array list
		    for(int cardIndex = 0; cardIndex < 5; cardIndex++)
		    	data.add(new Card(0, 'Z'));

			//manually generate all 21 possible combinations
		    //1
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(4));
			rankHand(data, i);
			//2
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(5));
			rankHand(data, i);
			//3
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(6));
			rankHand(data, i);
			//4
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(4));
			data.add(arr.get(5));
			rankHand(data, i);
			//5
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(4));
			data.add(arr.get(6));
			rankHand(data, i);
			//6
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
			//7
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(3));
			data.add(arr.get(4));
			data.add(arr.get(5));
			rankHand(data, i);
			//8
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(3));
			data.add(arr.get(4));
			data.add(arr.get(6));
			rankHand(data, i);
			//9
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(3));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
			//10
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(1));
			data.add(arr.get(4));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
			//11
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(4));
			data.add(arr.get(5));
			rankHand(data, i);
			//12
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(4));
			data.add(arr.get(6));
			rankHand(data, i);
			//13
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
			//14
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(2));
			data.add(arr.get(4));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
			//15
		    data.clear();
			data.add(arr.get(0));
			data.add(arr.get(3));
			data.add(arr.get(4));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
			//16
		    data.clear();
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(4));
			data.add(arr.get(5));
			rankHand(data, i);
			//17
		    data.clear();
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(4));
			data.add(arr.get(6));
			rankHand(data, i);
			//18
		    data.clear();
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
			//19
		    data.clear();
			data.add(arr.get(1));
			data.add(arr.get(2));
			data.add(arr.get(4));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
			//20
		    data.clear();
			data.add(arr.get(1));
			data.add(arr.get(3));
			data.add(arr.get(4));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
			//21
		    data.clear();
			data.add(arr.get(2));
			data.add(arr.get(3));
			data.add(arr.get(4));
			data.add(arr.get(5));
			data.add(arr.get(6));
			rankHand(data, i);
		}
		
		reorderPlayers();
		/*
		//determine the highest score among players
		Score highestScore = Score.High_Card;
		for(int i = 0; i < numPlayers; i++)
		{
			if(players.get(i).GetScore().ordinal() > highestScore.ordinal())
				highestScore = players.get(i).GetScore();
		}
		
		//now check to see if the top score occurs more than once
		//create a temp arraylist to hold on tying winners
		ArrayList<PokerPlayer> winningPlayers = new ArrayList<PokerPlayer>();
		for(int i = 0; i < numPlayers; i++)
		{
			if(players.get(i).GetScore() == highestScore)
				winningPlayers.add(players.get(i));
		}
		
		
		if(winningPlayers.size() > 1)
		{
			//we have a tie. determine who had the highest card of the winning score to determine the actual winner
			winningPlayers = determineTie(winningPlayers);
		}
		
		for(int i = 0; i < winningPlayers.size(); i++)
			winningPlayers.get(i).winner = true;
		*/
	}
	
	private void rankHand(ArrayList<Card> playerHand, int playerId)
	{		
		//determine if the player has a straight
		boolean straight = checkStraight(playerHand);
		//determine if the player has a flush
		boolean flush = checkFlush(playerHand);
		//determine if the player has 4 of a kind
		boolean fourOfAKind = checkXOfAKind(playerHand, 4);
		//determine if the player has 3 of a kind
		boolean threeOfAKind = checkXOfAKind(playerHand, 3);
		//determine if the player has a pair
		boolean onePair = checkXOfAKind(playerHand, 2);
		//determine if the player has two pair
		boolean twoPair = checkTwoPair(playerHand);
		
		if(straight && flush)
			players.get(playerId).SetBestHand(playerHand, Score.Straight_Flush);
		else if(fourOfAKind)
			players.get(playerId).SetBestHand(playerHand, Score.Four_Of_A_Kind);
		else if(threeOfAKind && onePair)
			players.get(playerId).SetBestHand(playerHand, Score.Full_House);
		else if(flush)
			players.get(playerId).SetBestHand(playerHand, Score.Flush);
		else if(straight)
			players.get(playerId).SetBestHand(playerHand, Score.Straight);
		else if(threeOfAKind)
			players.get(playerId).SetBestHand(playerHand, Score.Three_Of_A_Kind);
		else if(twoPair)
			players.get(playerId).SetBestHand(playerHand, Score.Two_Pair);
		else if(onePair)
			players.get(playerId).SetBestHand(playerHand, Score.One_Pair);
		else
			players.get(playerId).SetBestHand(playerHand, Score.High_Card);
	}
	
	private boolean checkStraight(ArrayList<Card> hand)
	{
		boolean response = true;
		int lowestCard = 99; //initialize. 99 ensures that it will always be higher than what can possibly be in the hand
		for(int i = 0; i < hand.size(); i++)
		{
			if(lowestCard > hand.get(i).rank)
				lowestCard = hand.get(i).rank;
		}
		
		//now that we have the lowest card in the hand, we can determine if the other four cards form a straight
		//suit does not affect whether the hand forms a straight or not so translate the hand to an integer arraylist
		ArrayList<Integer> cardRanks = new ArrayList<Integer>();
		for(int i = 0; i < hand.size(); i++)
			cardRanks.add(hand.get(i).rank);
		
		
		//first check the specific case of an ace low straight (A, 2, 3, 4, 5)
		if(cardRanks.contains(14) && cardRanks.contains(2) && cardRanks.contains(3) && cardRanks.contains(4) && cardRanks.contains(5))
			response = true;
		else if(cardRanks.contains(lowestCard + 1) && 
				cardRanks.contains(lowestCard + 2) && 
				cardRanks.contains(lowestCard + 3) && 
				cardRanks.contains(lowestCard + 4))
			response = true;
		else
			response = false;
		
		return response;
	}
	
	private boolean checkXOfAKind(ArrayList<Card> hand, int x)
	{
		boolean response = false;
		int matchCount;
		
		//create an integer arraylist of the cards
		ArrayList<Integer>tempHand = new ArrayList<Integer>();
		for(int i = 0; i < hand.size(); i++)
			tempHand.add(hand.get(i).rank);
		
		//loop through the arraylist to see if a card is matched x times
		//because this is checking to see if the hand contains x of a kind, the loop only needs to be done 6 - x
		for(int i = 0; i < 6 - x; i++)
		{
			matchCount = 1;
			int rankToMatch = tempHand.get(i);
			for(int j = 0; j < tempHand.size(); j++)
			{
				if(j == i)
					continue; //we don't want to compare the card to itself
				if(tempHand.get(j) == rankToMatch)
					matchCount++;
			}
			
			if(matchCount == x)
			{
				response = true;
				break;
			}
		}
		return response;
	}
	
	private boolean checkFlush(ArrayList<Card> hand)
	{
		boolean response = true;
		char firstSuit = hand.get(0).suit; //start by grabbing the suit of the first card
		
		//a flush has occurred if all the cards in the hand have the same suit
		for(int i = 1; i < hand.size(); i++)
		{
			if(hand.get(i).suit != firstSuit)
			{
				response = false;
				break;
			}
		}
		
		return response;
	}
	
	private boolean checkTwoPair(ArrayList<Card> hand)
	{
		boolean response = false;
		
		int matchCount;
		int pairCount = 0;
		
		//create an integer arraylist of the cards
		ArrayList<Integer>tempHand = new ArrayList<Integer>();
		for(int i = 0; i < hand.size(); i++)
			tempHand.add(hand.get(i).rank);
		
		//loop through the arraylist to see if 2 of the cards have a separate matching card ie. 2 of clubs and 2 of diamonds and 4 of hearts and 4 of diamonds
		//would be two pair
		//because this is checking to see if the hand contains two pair, the loop needs to be done 4 times
		for(int i = 0; i < tempHand.size() - 1; i++)
		{
			matchCount = 1;
			int rankToMatch = tempHand.get(i);
			for(int j = 0; j < tempHand.size(); j++)
			{
				if(j == i)
					continue; //we don't want to compare the card to itself
				if(tempHand.get(j) == rankToMatch)
					matchCount++;
			}
			
			if(matchCount == 2)
			{
				//we don't want to compare the same pair again, otherwise it would give us a false positive
				//remove the two matching cards
				int index = tempHand.indexOf(rankToMatch);
				tempHand.remove(index);
				index = tempHand.indexOf(rankToMatch);
				tempHand.remove(index);
				//reset i to account for shifting the items of the list
				i = -1;
				pairCount++;
			}
		}
		
		if(pairCount == 2)
			response = true;
		
		return response;
	}
	
	
	public boolean Bet()
	{
		int lastBet = INVALID_PLAYER;
		int previousPlayer = 0;
		String commPlay;
		boolean playerBet = false;
		
		if(playingAnte) {SetAnte();}
		
		lastBet = -2;

		previousPlayer = INVALID_PLAYER + 1;
		playerBet = true;
		while((lastBet != (DetermineNextPlayer(previousPlayer))) && ((numAllIn+numFolded) < (numPlayers-1)) && playerBet)
		{
			//loop through each player 
			for(int x = 0; x < numPlayers; x++)
			{	
				if(lastBet == x)
					break;
				
				//skip players who have folded or went all in
				if((players.get(x).isFolded() == false) && (players.get(x).isAllIn() == false))
				{
					//this will be used for the next player. it keeps track of which player went last
					previousPlayer = x;
					
					System.out.println("Previous player is " + previousPlayer);
					System.out.println("last bet was by player" + lastBet);
					System.out.println("num folded " + numFolded);
					System.out.println("num all in " + numAllIn);
					
					if(((numFolded + numAllIn) == (numPlayers)) || (numFolded == (numPlayers-1)))
						break;
					
					System.out.println("\nPlayer " + players.get(x).getPlayerNumber());
					System.out.println("Players Funds:\t" + players.get(x).getTotalFunds());
					System.out.println("Players Bet:\t" + players.get(x).getCurrentBet());
					System.out.println("Total Pot:\t" + pots.get(currentPotIndex));
					System.out.println("Current Bet:\t" + currentBet);
					
					/*readMessage = MainActivity.readMessage;
					String test = MainActivity.test;
					System.out.println("|" + readMessage + "|");
					System.out.println("|" + test + "|");
					while(!readMessage.startsWith("p" + previousPlayer)) {
						try {
							Thread.sleep(1000);
							System.out.println("|" + readMessage + "|");
							System.out.println("|" + test + "|");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}*/
					//String test = ServerActivity.test;
					//System.out.println("|" + test + "|");
					String readMessage3 = MainActivity.waitForBluetooth(previousPlayer);
					System.out.println("/" + readMessage3 + "/");
					System.out.println("out of while");

					//String readMessage3 = "RAISE=100";
					int indexOfEqual = readMessage3.indexOf("=");
					String comm = readMessage3.substring(3);
					int newCurrentBet = 0;
					if (!comm.startsWith("RAISE")) {
						commPlay = comm;
						System.out.println(comm);
					} else {
						commPlay = comm.substring(0, 5);
						System.out.println(comm);
						newCurrentBet = Integer.parseInt(comm.substring((indexOfEqual)+1));
					}
					System.out.println(commPlay + "*");
					//commPlay = readMessage.substring(1, readMessage.length());
					players.get(x).setCurrentBetType(commPlay);
					
						//this will take care of check and call. the bet will either be zero or the current bet amount
						if (commPlay.equals("CALL")) {
						} else if (commPlay.equals("CALL") || commPlay.equals("CHECK")) {	
							//get the player from the array list and bet the currentBet amount
							if(currentBet != players.get(x).getCurrentBet())
							{
								int betDifference = currentBet - players.get(x).getCurrentBet();
								
								if(players.get(x).BetChips(betDifference) == 0)
								{
									pots.set(currentPotIndex, pots.get(currentPotIndex) + betDifference);
									players.get(x).setCurrentBet(currentBet);
									if(players.get(x).getTotalFunds() == 0)
									{
										//create a side pot and add pot access to players who are still active
										currentPotIndex++;
										pots.add(0);
										numAllIn++;
										
										for(int count = 0; count < players.size(); count++)
											if((players.get(count).isFolded() == false) && (players.get(count).isAllIn() == false))
												players.get(count).AddPot(currentPotIndex);
									}
								}
								//we called for more chips than we have so we should notify the user and either
								//cancel the bet or go all in
								else
								{
									System.out.println("You bet more chips than you have...");
									System.out.println("Let's all cry because we now have to make a side pot...");
									
									pots.set(currentPotIndex, pots.get(currentPotIndex) + betDifference);
									players.get(x).setCurrentBet(currentBet);
									if(players.get(x).getTotalFunds() == 0)
									{
										//create a side pot and add pot access to players who are still active
										currentPotIndex++;
										pots.add(0);
										numAllIn++;
										
										for(int count = 0; count < players.size(); count++)
											if((players.get(count).isFolded() == false) && (players.get(count).isAllIn() == false))
												players.get(count).AddPot(currentPotIndex);
									}
								}
							}
							//this means all players past this one have already called (current bet already met)
							else
								players.get(x).setCurrentBetType("CHECK");
							break;
						} else if (commPlay.equals("FOLD")) {
							//flag the player as folded so we can ignore him/her for the rest of the hand
							players.get(x).Fold();
							numFolded++;
							break;
						} else if (commPlay.equals("RAISE")) {
							//int newCurrentBet = scan.nextInt();
							newCurrentBet = newCurrentBet + currentBet;
							
							lastBet = x;
							
							if(players.get(x).BetChips(newCurrentBet - players.get(x).getCurrentBet()) == 0)
							{
								currentBet = newCurrentBet;
								pots.set(currentPotIndex, pots.get(currentPotIndex) + (currentBet - players.get(x).getCurrentBet()));
								players.get(x).setCurrentBet(newCurrentBet);
								
								if(players.get(x).getTotalFunds() == 0)
								{
									lastBet = INVALID_PLAYER;
									numAllIn++;
								}
							}
							else
							{
								System.out.println("You bet more chips than you have...");
								//we need to repeat this player's turn instead of moving to the next player
								//this should not be possible in our final game because we are checking
								//when the user is doing GUI things with betting
							}
							break;
						} else {
							System.out.println("Turn was unsuccessful...but who cares?");
							break;
						}
					
					
					System.out.println("Players Funds:\t" + players.get(x).getTotalFunds());
					System.out.println("Players Bet:\t" + players.get(x).getCurrentBet());
					System.out.println("Total Pot:\t" + pots.get(currentPotIndex));
					System.out.println("Current Bet:\t" + currentBet + "\n");
				}
			}
			
			if(lastBet != INVALID_PLAYER)
				playerBet = true;
			else
				playerBet = false;
		}
		
		
		//reset the current bet back to 0 for players and table after the round ends
		currentBet = 0;
		for(int i = 0; i < numPlayers; i++){players.get(i).setCurrentBet(0);}
		
		return true;
		/**************************************************************************************************************
		 * This will all need to be moved
		 
		//there has to be a more elegant way of doing this, but we're using the same check we did earlier
		if(numFolded == (numPlayers - 1))
		{
			int winner = DetermineWinnerByFold(ps);
			System.out.println("Player " + ps.get(winner).getPlayerNumber() + " wins " + bm.getTotalPot() + "!");
			GivePlayerWinnings(ps, winner, bm.getTotalPot());
		}
		//determine winner by best hand
		else
			System.out.println("Game ends with " + bm.getTotalPot() + " in the pot!");
		
		//this is where the game ends. we will need to loop this until the users quit
		//we can make this when numPlayers = 1 or if the server presses the quit button	

		
		if(numPlayers > 1)
		{
			System.out.print("Play another hand? Type anything but QUIT: ");
			continuePlaying = scan.next().toUpperCase();
		}
		else
		{
			System.out.println("Game Over.");
			continuePlaying = "QUIT";
		}
		
		*/
	}
	
	public int DetermineNextPlayer(int lastPlayer)
	{
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int x = 0; x < numPlayers; x++)
		{
			if((players.get(x).isFolded() == false) && (players.get(x).isAllIn() == false))
			{
				//add all players still in this game into the array in order
				temp.add(x);
			}
		}
		
		for(int x = 0; x < temp.size(); x++)
		{
			if(temp.get(x) == lastPlayer)
				return(temp.get((x+1)%temp.size()));
		}
			
		return ((lastPlayer+1) % numPlayers);
	}
		
	
	public int DetermineWinnerByFold()
	{
		int playerNum = 0;
		
		for(int x = 0; x < numPlayers; x++)
		{
			if((players.get(x).isFolded() == false) && (players.get(x).isAllIn() == false))
			{
				//add all players still in this game into the array in order
				playerNum = x;
			}
		}
			
		return playerNum;
	}
	
	//swap the order of the player array list so that the person who just bet
	//will now be last in the list. this is to cycle the dealer chip and
	//give blinds to different players after each round (if playing with blinds)
	private void ShuffleOrder()
	{
		for(int x = 0; x <  players.size() - 1; x++)
			Collections.swap(players, x, x+1);
	}
	
	//function to set blinds on the last two players to bet
	//blinds count towards first round betting for those two players
	public void SetBlinds()
	{
		int lastPlayer = numPlayers - 1;
		players.get(lastPlayer).BetChips(bigBlind);
		players.get(lastPlayer).setCurrentBet(bigBlind);
		if(players.get(lastPlayer).getTotalFunds() == 0)
			numAllIn++;
		
		players.get(lastPlayer - 1).BetChips(bigBlind/2);
		players.get(lastPlayer - 1).setCurrentBet(bigBlind/2);
		if(players.get(lastPlayer - 1).getTotalFunds() == 0)
			numAllIn++;
		
		currentBet = bigBlind;
		currentPotIndex = 0;
		pots.add((int)(bigBlind * 1.5));
	}
	
	//function to set antes on all players
	//antes do not count toward first round betting
	//function to set ante on all players
	//ante does not count toward first round betting
	public void SetAnte()
	{
		for(int i = 0; i < numPlayers; i++)
			players.get(i).BetChips(ante);
		
		pots.add(ante * numPlayers);
	}
	
	//cleanup from the previous play
	public void CleanOldData()
	{
		for(int i = players.size() - 1; i >= 0; i--)
		{
			players.get(i).setAllIn(false);
			players.get(i).setFolded(false);
			players.get(i).setCurrentBet(0);
			players.get(i).setCurrentBetType("");
			players.get(i).ClearPots();
			players.get(i).NewHand();
			
			if(players.get(i).getTotalFunds() == 0)
			{
				players.remove(i);
				System.out.println("Player removed.");
				numPlayers--;
			}
		}
		
		currentBet = 0;
		pots.clear();
		
		numFolded = 0;
		numAllIn = 0;
		
		table.NewHand();
		ResetDeck();
		ShuffleOrder();
		
	}
	
	
	public void GivePlayersWinnings()
	{//loop through all active players, distributing side pots as necessary
		//debug variables
		int totalNumPots = 0;
		for(int i = 0; i < activePlayers.size(); i++)
		{
			int playerWinnings = 0;
			for(int potIndex = 0; potIndex < activePlayers.get(i).availablePots.size(); potIndex++)
			{
				int thisPot = 0;
				thisPot = pots.get(activePlayers.get(i).availablePots.get(potIndex));
				if(thisPot != 0)
				{
					activePlayers.get(i).AddFunds(thisPot);
					playerWinnings += thisPot;
					//since the pot money has been distributed, set it to 0
					pots.set(activePlayers.get(i).availablePots.get(potIndex), 0);
					totalNumPots++;
				}
			}
			System.out.println("Player " + activePlayers.get(i).getPlayerNumber() + " wins " + playerWinnings);
		}
		System.out.println("Number of pots: " + totalNumPots);
	}
	
	private void reorderPlayers()
	{//reorders the player arraylist in order of winning hand
	//if two or more people both have the same score, the winner will be the one with the highest set of cards
	//the hand is sorted in order of relevant score and then highest to lowest
	//ie a hand with a pair of 4's would be sorted as follows:
	//[4H][4D][8C][5S][2S]
		
		//start by creating a separate list of players
		activePlayers = new ArrayList<PokerPlayer>();
		activePlayers.clear();
		for(int i = 0; i < players.size(); i++)
			if(!players.get(i).Folded())
				activePlayers.add(players.get(i));
		
		boolean swap = true;
		while(swap)
		{
			swap = false;	
			for(int i = 0; i < activePlayers.size() - 1; i++)	
			{
				//compare scores, swap if next player has better
				if(activePlayers.get(i).GetScore().ordinal() < activePlayers.get(i + 1).GetScore().ordinal())
				{
					Collections.swap(activePlayers, i, i + 1);
					swap = true;
				}
				else if(activePlayers.get(i).GetScore().ordinal() > activePlayers.get(i + 1).GetScore().ordinal())
					continue;
				else if(activePlayers.get(i).GetScore().ordinal() == activePlayers.get(i + 1).GetScore().ordinal())
				{//scores are the same, we have to compare individual hands
					for(int j = 0; j < 5; j++)
					{
						if(activePlayers.get(i).GetBestHand().get(j).rank > activePlayers.get(i + 1).GetBestHand().get(j).rank)
							break;
						else if(activePlayers.get(i).GetBestHand().get(j).rank < activePlayers.get(i + 1).GetBestHand().get(j).rank)
						{
							Collections.swap(activePlayers, i, i + 1);
							swap = true;
							break;
						}
						
					}
				}
			}
		}
	}
}

