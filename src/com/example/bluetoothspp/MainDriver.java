package com.example.bluetoothspp;
import java.util.ArrayList;
import java.util.Scanner;

public class MainDriver 
{
	public static void main(String args[])
	{
		Scanner scan = new Scanner(System.in);
		int numPlayers = 0;
		
		System.out.println("Number of players? ");
		numPlayers = scan.nextInt();
		
		PokerGame game = new PokerGame(numPlayers, 10, 10);
		
		ArrayList<Card> cards = new ArrayList<Card>();
		
		boolean stillPlaying = true;
		while(stillPlaying)
		{
			game.SetBlinds();
			game.DealCards(2);
			cards.clear();
			for(int i = 0; i < game.players.size(); i++)
			{
				cards.add(game.players.get(i).GetCards().get(0));
				cards.add(game.players.get(i).GetCards().get(1));
			}
		
			PrintTable(game.table.GetCards().size(), game.players.size(), cards, game.players);
			System.out.println("\n");
			game.Bet();
			
			
			game.BurnCard();
			game.DealTableCards(3);
			cards.clear();
			for(int i = 0; i < 3; i++)
				cards.add(game.table.GetCards().get(i));
			
			for(int i = 0; i < game.players.size(); i++)
			{
				cards.add(game.players.get(i).GetCards().get(0));
				cards.add(game.players.get(i).GetCards().get(1));
			}
		
			PrintTable(game.table.GetCards().size(), game.players.size(), cards, game.players);
			System.out.println("\n");
			game.Bet();
			
			
			game.BurnCard();
			game.DealTableCards(1);
			cards.clear();
			for(int i = 0; i < 4; i++)
				cards.add(game.table.GetCards().get(i));
			
			for(int i = 0; i < game.players.size(); i++)
			{
				cards.add(game.players.get(i).GetCards().get(0));
				cards.add(game.players.get(i).GetCards().get(1));
			}
		
			PrintTable(game.table.GetCards().size(), game.players.size(), cards, game.players);
			System.out.println("\n");
			game.Bet();
			
			
			game.BurnCard();
			game.DealTableCards(1);
			cards.clear();
			for(int i = 0; i < 5; i++)
				cards.add(game.table.GetCards().get(i));
			
			for(int i = 0; i < game.players.size(); i++)
			{
				cards.add(game.players.get(i).GetCards().get(0));
				cards.add(game.players.get(i).GetCards().get(1));
			}
		
			PrintTable(game.table.GetCards().size(), game.players.size(), cards, game.players);
			System.out.println("\n");
			game.Bet();
			
			
			//display score of each player
			game.determineWinner();
			for(int i = 0; i < game.activePlayers.size(); i++)
			{
				System.out.print("Player " + game.activePlayers.get(i).getPlayerNumber() + " has: " + game.activePlayers.get(i).GetScore().toString() + "\t\t Best Hand: ");
				for(int j = 0; j < 5; j++)
				{
					System.out.print("[" + game.activePlayers.get(i).GetBestHand().get(j).rank + game.activePlayers.get(i).GetBestHand().get(j).suit + "]");
				}
				System.out.print("\n");
			}
			
			game.GivePlayersWinnings();
			
			System.out.println("Play another round?\n");
			String response = scan.next().toLowerCase();
			if(response.contains("yes"))
				stillPlaying = true;
			else
				stillPlaying = false;
			game.CleanOldData();
			
		}
		
		scan.close();
	}
	
	public static void PrintTable(int numTableCards, int numPlayers, ArrayList<Card> cards, ArrayList<PokerPlayer> players)
	{
		System.out.println("\t\t\t\t\tTable");
		System.out.print("\t\t\t\t\t");
		int i;
		for(i = 0; i < numTableCards; i++)
			System.out.print("[" + cards.get(i).rank + cards.get(i).suit + "]");
		System.out.println("\n\n\n");
		
		for(int j = 0; j < numPlayers; j++)
			System.out.print("P" + players.get(j).getPlayerNumber() + "\t\t");
		System.out.println("");
		
		for(i = numTableCards; i < cards.size(); i++)
		{
			System.out.print("[" + cards.get(i).rank + cards.get(i).suit + "]");
			i++;
			System.out.print("[" + cards.get(i).rank + cards.get(i).suit + "]\t");
		}
	}
	
}
