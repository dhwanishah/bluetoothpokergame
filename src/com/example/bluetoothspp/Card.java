package com.example.bluetoothspp;

public class Card implements Cloneable
{
	public int rank;
	public char suit;
	
	public Card(int cardRank, char cardSuit)
	{
		rank = cardRank;
		suit = cardSuit;
	}
	
	public Object clone()
	{  
	    try
	    {  
	        return super.clone();  
	    }
	    catch(Exception e)
	    { 
	        return null; 
	    }
	}
}
