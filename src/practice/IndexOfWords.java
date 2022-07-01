package practice;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;


public class IndexOfWords 
{
	/**
	 * Returns a new instance of this class, an "Index".
	 * The new Index is associated to the set of the files specified in the
	 * constructor.
	 * This Index has some specific properties.
	 * Read the description of the problem.
	 * N is the maximum number of words that the index may contain
	 */
	private String[] paths = new String[]{};
	private static int N;
	private HashSet<String> usedWords; // list with unique used words so that its easier to find later
	private List<wordsSaved> savedWords = new ArrayList<>();
	
	public IndexOfWords(String[] paths, int N)
	{
		this.paths = paths;
		IndexOfWords.N = N;
		this.usedWords = new HashSet<String>();
	}

	static class wordsSaved
	{
		private String word;
		private List<String> documents = new ArrayList<>();
		private int time;
		
		public wordsSaved() {}
	}
	
	
	
	// find an already found word in the hashmap
	public String[] findWord(String word)
	{
		String[] files = null;
		for (wordsSaved entry : savedWords)
		{
			if (entry.word.equals(word))
			{
				files = new String[entry.documents.size()];
				entry.documents.toArray(files);
				
				for (String i : files)
					if (i != null)
						System.out.println(i);
				
				return entry.documents.toArray(files);
			}
		}
		return null;
	}
	/**
	 * Returns a new array with the names of all files containing
	 * the word specified.
	 * Additionally it prints out by console the result.
	 */
	public String[] whereIs(String word)									 
	{
		String[] words = new String[paths.length];
		wordsSaved wordsaved = new wordsSaved();
		if (savedWords.size() - 1 >= N && !usedWords.contains(word))
		{
			int maxEntry = 0;
			wordsSaved wordToDelete = new wordsSaved();
			
			for (wordsSaved entry : savedWords)
			    if (maxEntry == 0 || entry.time > maxEntry)
			    {
			    	maxEntry = entry.time;
			    	wordToDelete = entry;
			    }
			
			
			usedWords.remove(wordToDelete.word);
			savedWords.remove(wordToDelete);
		}
		
		
		if (usedWords.contains(word))
			return findWord(word);
		
		
		long startTime1 = System.currentTimeMillis();
		
		for (int i = 0; i < paths.length; ++i)
		{
			Scanner sc2 = null;
			try 
			{
				sc2 = new Scanner(new File("src/practice/" + paths[i]));
			} catch (FileNotFoundException e) 
			   {
			       e.printStackTrace();  
			   }
			   while (sc2.hasNextLine())
			   {
				   Scanner s2 = new Scanner(sc2.nextLine());
			       while (s2.hasNext()) 
			       {
			           String s = s2.next();
			           if (s.equals(word))
			           {
			            words[i] = paths[i];
			            usedWords.add(word);
			            if (!savedWords.contains(wordsaved)) // save the word for easier search later
			            {
			            	savedWords.add(wordsaved);
			            	savedWords.get(savedWords.size() - 1).word = word;
			            }
			            savedWords.get(savedWords.size() - 1).documents.add(paths[i]);
			            break;
			           }
			       }
			   }
		}
		long stopTime1 = System.currentTimeMillis();
		savedWords.get(savedWords.size() - 1).time = (int) (stopTime1 - startTime1); // time taken to find the word
		for (String i : words)
		{
			if (i != null)
				System.out.println(i);
		}
		return words;
	}

	@SuppressWarnings({ "unused", "static-access" })
	public static void main(String[] args) throws Exception 
	{

		//		Document1.txt, Document2.txt, Document3.txt are existing text files
		//		The words contained in each file:
		//		Document1 => text1 text2 text3
		//		Document2 => text1 text2
		//		Document3 => text1 text3
		// You may add more documents if you wish 
		IndexOfWords index = new IndexOfWords(new String[]{"Document1", "Document2", "Document3"}, 3); // added the documents to the project
		System.out.println("Size to store words: " + index.N);
		// Query the index: word can refer to any word
		System.out.println("\ntext1 location: ");
		String[] rs1 = index.whereIs("text1"); //returns {"Document1"," Document2 ", "Document3 "}
		
		System.out.println("\ntext2 location: ");
		String[] rs2 = index.whereIs("text2"); // returns {"Document1", "Document2"}
		
		System.out.println("\ntext3 location: ");
		String[] rs3 = index.whereIs("text3"); // returns {"Document1", "Document3"}
		
		System.out.println("\nx location: ");
		String[] rsx = index.whereIs("x"); // Returns an empty array: {}
		
		System.out.println("\ntext1 location: ");
		String[] rs1x = index.whereIs("text1"); // returns {"Document1 ", "Document2 ", "Document3 "}
		
		System.out.println("\ntext2 location: ");
		String[] rs2x = index.whereIs("text2"); // returns {"Document1", "Document2 "}
		
		System.out.println("\ntext3 location: ");
		String[] rs3x = index.whereIs("text3"); // returns {"Document1 ", "Document3 "}

	}

}
