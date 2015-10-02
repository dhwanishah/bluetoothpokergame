package com.example.bluetoothspp;
import java.util.ArrayList;

public class Table 
{
	private ArrayList<Card> board;
	private ArrayList<Card> discardPile;
	
	public Table()
	{
		board = new ArrayList<Card>();
		discardPile = new ArrayList<Card>();
	}
	
	public void ReceiveCard(Card card)
	{
		board.add(card);
	}
	
	public ArrayList<Card> GetCards()
	{
		return board;
	}
	
	public void Discard(Card card)
	{
		discardPile.add(card);
	}
	
	public void NewHand()
	{
		board.clear();
		discardPile.clear();
	}
}

