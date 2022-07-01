package practice;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import practice.BFSGraph.Graph;

import java.util.Set;

public class MineMyPaintBFS 
{
	private int[][] img;
	private Graph graph;

	public MineMyPaintBFS(int[][] img) 
	{
		this.img = img;
		graph = new Graph(img.length * img[0].length);
		graph.BFSmatrix(this.img); // transform matrix to disconnected graph
		graph.BFS(); // traverse graph and add regions
	}

	/// transforms a number to a letter using ASCII representation
	private Character getCharForNumber(int i)
	{
	    return i > -1 && i < 26 ? (char)(i + 65) : null; // 65 in ASCII is A, 66 is B and so on
	}
	
	/// returns the number of regions in img
	public int regions() 
	{
		return graph.regions.size();
	}

	/// Precondition: row and column are valid coordinates of a pixed in the image
	/// this method returns the region that the pixel belongs to
	public char regionOf(int row, int column) 
	{
		int pos = graph.newMatrix[row][column];
		int whatRegion = 0;
		
		for (Entry<Integer, ArrayList<Integer>> ee : graph.regions.entrySet())
		{
			Integer key = ee.getKey();
			ArrayList<Integer> values = ee.getValue();			
			for (int i = 0; i < values.size(); ++i)
				if (pos == values.get(i))
					whatRegion = key;
		}
		
		char region = getCharForNumber(whatRegion); // typecast to character
		return region;
	}
	
	///  This method returns the list of regions connected to the region 
	///  that the pixel is located 
	public List<Character> connectedRegionsTo(int row, int column) 
	{
		Set<Character> conRegions = new HashSet<>(); // no repeated regions will be added to the list this way
		boolean checkUp = (row - 1) > 0, checkRight = (column + 1) < (img[0].length - 1), checkLeft = (column - 1) > 0, checkDown = (row + 1) < (img.length - 1); // check limits of the matrix
		
		// depending on the position of the matrix add adjacent regions
		if (checkUp)
			conRegions.add(regionOf(row - 1, column));
		if (checkRight)
			conRegions.add(regionOf(row, column + 1));
		if (checkLeft)
			conRegions.add(regionOf(row, column - 1));
		if (checkDown)
			conRegions.add(regionOf(row + 1, column));
		
		List<Character> conRegionsList = new ArrayList<>();
		conRegionsList.addAll(conRegions);
		return conRegionsList;
	}

	/// Precondition: rgn is the char representing one of the regions of the image
	/// This method returns the list of pixels in a region
	public List<Coordinates> pixelsIn(char rgn) 
	{
		List<Coordinates> allCoords = new ArrayList<>();
		ArrayList<Integer> coords = new ArrayList<>();
		
		for (int key : graph.regions.keySet()) // iterate through every key to get the region and store the array of elements
			if (key == (int)rgn - 65)
				coords = graph.regions.get(key);
		
		
		for (int i = 0; i < coords.size(); ++i) // get the coordinates of the elements in the old matrix
		{
			int row = coords.get(i) / graph.newMatrix.length; // row of the element in the original matrix
			int col = coords.get(i) % graph.newMatrix[0].length; // column of the element in the original matrix
			Coordinates coordinates = new Coordinates(row, col);
			allCoords.add(coordinates);
		}
		return allCoords;
	}

	public static class Coordinates 
	{
		public final int row;
		public final int col;

		public Coordinates(int row, int column) 
		{
			this.row = row;
			this.col = column;
		}
	}

	public static void main(String[] args) 
	{

		int[][] image = 
			{
				{1, 1, 1, 1, 3},
				{1, 2, 1, 3, 3},
				{1, 1, 1, 3, 3},
				{1, 2, 2, 3, 3},
				{3, 3, 3, 2, 2}
			};
		
		// print the matrix
		System.out.println("Matrix:");
		for (int i = 0; i < image.length; ++i)
		{
			for (int j = 0; j < image[0].length; ++j)
				System.out.print(image[i][j] + " ");
			System.out.println();
		}
		// Some tests
		MineMyPaintBFS mmpb = new MineMyPaintBFS(image);
		
		int nregions = mmpb.regions();
		System.out.println("\nNumber of regions: " + nregions);
		
		// print matrix with regions
		for (int i = 0; i < image.length; ++i)
		{
			for (int j = 0; j < image[0].length; ++j)
				System.out.print(mmpb.regionOf(i, j) + " ");
			System.out.println();
		}
		
		char whatRegion = mmpb.regionOf(0, 0);
		System.out.println("\nRegion of coordinate (0,0): " + whatRegion);
		
		char whatRegion2 = mmpb.regionOf(4, 4);
		System.out.println("\nRegion of coordinate (4,4): " + whatRegion2);
		
		List<Coordinates> newlist = mmpb.pixelsIn('A');
		System.out.println("\nCoordinates of the region A: ");		
		for (int i = 0; i < newlist.size(); ++i)
			System.out.println("(" + newlist.get(i).row + "," + newlist.get(i).col + ")");
		
		List<Coordinates> newlist2 = mmpb.pixelsIn('F');
		System.out.println("\nCoordinates of the region F: ");		
		for (int i = 0; i < newlist2.size(); ++i)
			System.out.println("(" + newlist2.get(i).row + "," + newlist2.get(i).col + ")");
		
		System.out.println("\nRegions connected to coordinate (0,0): ");
		List<Character> conList = mmpb.connectedRegionsTo(0, 0);
		for (int i = 0; i < conList.size(); ++i)						
			System.out.println(conList.get(i));
		
		System.out.println("\nRegions connected to coordinate (4,4): ");
		List<Character> conList2 = mmpb.connectedRegionsTo(4, 4);
		for (int i = 0; i < conList2.size(); ++i)						
			System.out.println(conList2.get(i));
	}
}
