package practice;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AuctionCottages 
{
	private static int maxValue;
	private static int H; //maximum total number of places (rooms, villas, ...) offered
	private static List<customer> listOfCustomers; // list to store all customers from a document
	private static List<Integer> bestCustomers; // list to store customers that make the most money
	
	@SuppressWarnings("static-access")
	public AuctionCottages(int capacity)
	{
		this.H = capacity;
		this.listOfCustomers = new ArrayList<>();
	}
	
	static class customer
	{
		int places, euros, customer;
		
		public customer(int places, int euros, int customer)
		{
			this.places = places;
			this.euros = euros;
			this.customer = customer;
		}
	}
	
	public void readAuctioneers() // add all customers to the database
	{
		int index = 0;
		Scanner sc2 = null;
		try 
		{
			sc2 = new Scanner(new File("src/practice/SpringfieldAuction"));
		} catch (FileNotFoundException e) 
		   {
		       e.printStackTrace();
		   }
		   while (sc2.hasNextLine())
		   {
			   Scanner s2 = new Scanner(sc2.nextLine());
			   int places = 0, euros = 0, customer = 0;
		       while (s2.hasNext()) 
		       {
		           String s = s2.next();
		           {
		        	   switch(index)
		        	   {
		        	   case 0:
			        	   places = Integer.parseInt(s);
			        	   break;
		        	   case 1:
		        		   euros = Integer.parseInt(s);
			        	   break;
		        	   case 2:
		        		   customer = Integer.parseInt(s);
			        	   break;
		        	   }
		           }
	        	   ++index;
		       }
		       customer auc = new customer(places, euros, customer);
		       listOfCustomers.add(auc);
		       index = 0;
		   }
	}
	
	public void computeAuctionWithH(int index, int currentCottages, int currentValue, List<Integer> bestCustomerList) 
	{
		if (index == listOfCustomers.size())
		{
			if (currentValue > maxValue)
			{
				maxValue = currentValue;
				bestCustomers = new ArrayList<>();
				for (Integer i : bestCustomerList) 
					bestCustomers.add(i);
			}
		}
		else
		{
			if ((currentCottages + listOfCustomers.get(index).places) <= H)
			{
				List<Integer> listWithNewCottages = new ArrayList<Integer>();
				
				for (Integer i : bestCustomerList)
					listWithNewCottages.add(i);
				listWithNewCottages.add(listOfCustomers.get(index).customer);
				
				computeAuctionWithH(index + 1, currentCottages + listOfCustomers.get(index).places, currentValue + listOfCustomers.get(index).euros, listWithNewCottages);
			}
			
			List<Integer> listWithNoNewCottages = new ArrayList<>();
			for (Integer i : bestCustomerList) 
				listWithNoNewCottages.add(i);
			
			if (index < listOfCustomers.size())
				computeAuctionWithH(index + 1, currentCottages, currentValue, listWithNoNewCottages);
		}
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) 
	{
		int placesAvailable = 10;
		AuctionCottages auction = new AuctionCottages(placesAvailable);
		auction.readAuctioneers(); 
		System.out.println("Places available: " + placesAvailable);
		
		System.out.println("\nPlaces\t" + "Euros\t" + "customerID\t");
		for (customer i : auction.listOfCustomers) 
			System.out.println(i.places + "\t" + i.euros + "\t" + i.customer);
		
		List<Integer> currentList = new ArrayList<>();
		auction.computeAuctionWithH(0, 0, 0, currentList); // find best customers using backtracking
		
		System.out.println("\nCustomers that make the biggest profit: ");
		for (Integer i : auction.bestCustomers) 
			System.out.println("CustomerID: " + i);
		
		int totalMoney = 0;
		for (int i = 0; i < bestCustomers.size(); ++i) 
			for (int j = 0; j < auction.listOfCustomers.size(); ++j)
				if (bestCustomers.get(i) == listOfCustomers.get(j).customer)
					totalMoney += listOfCustomers.get(j).euros;
		
		System.out.println("\nTotal amount of money earned: " + totalMoney + "€");

	}

}
