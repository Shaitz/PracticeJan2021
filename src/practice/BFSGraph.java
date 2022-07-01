package practice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BFSGraph 
{
	static class Graph
	{
		private int V;
		private LinkedList<Integer> adj[];
		public int[][] newMatrix;
		public HashMap<Integer, ArrayList<Integer>> regions = new HashMap<>();
		
		@SuppressWarnings("unchecked")
		public Graph(int V)
		{
			this.V = V;
			adj = new LinkedList[V];
			for (int i = 0; i < V; ++i)
				adj[i] = new LinkedList<Integer>();
		}
		
		public void addEdge(int V, int V2)
		{
			adj[V].add(V2);
			adj[V2].add(V);
		}
		
		public void BFS() // bfs traversal for disconnected graph
		{
			boolean[] marked = new boolean[V];
			Queue<Integer> q = new LinkedList<>();
			int regionCount = 0; // variable to keep region in order, otherwise I would have regions like X or Q because I would take large numbers
			
			for (int i = 0; i < V; ++i)	// iterate through every vertice (the ones visited will be skipped) necessary if graph is disconnected
			{
				if (!marked[i])
				{
					q.add(i);
					regions.putIfAbsent(regionCount, new ArrayList<Integer>()); // create new region if null
					regions.get(regionCount).add(i);	// add the starting point to the region
					while (!q.isEmpty())
					{
						int v = q.remove();
						marked[v] = true;
						
						for (int j = 0; j < adj[v].size(); ++j)
						{
							int adjV = adj[v].get(j);
							if (!marked[adjV])
							{
								marked[adjV] = true;				
								q.add(adjV);
								regions.get(regionCount).add(adjV); // add the adjacent to the same region as the starting point
							}
						}
					}
					++regionCount; // no more vertices in this region so a new region will be created in the next iteration.
				}
			}
		}
	
	    public void BFSmatrix(int[][] grid) // create a graph (connected or disconnected) from a matrix
	    {

	    	int k = 0;
	        int h = grid.length;
	        if (h == 0)
	            return;
	        
	        int l = grid[0].length;

	        newMatrix = new int[h][l];
	        for (int i = 0; i < h; ++i)
	        	for (int j = 0; j < l; ++j)
	        	{
	        		newMatrix[i][j] = k;
	        		++k;
	        	}
	        
	        boolean[][] visited = new boolean[h][l];

	        Queue<String> q = new LinkedList<>();

	        q.add(0 + "," + 0);

	        while (!q.isEmpty())
	        {

	            String x = q.remove();
	            int row = Integer.parseInt(x.split(",")[0]);
	            int col = Integer.parseInt(x.split(",")[1]);

	            if (row < 0 || col < 0 || row >= h || col >= l || visited[row][col]) // if row or col are out of matrix or if the vertice is visited skip all the code ahead
	                continue;

	            visited[row][col] = true;
	            q.add(row + "," + (col - 1)); //go left
	            q.add(row + "," + (col + 1)); //go right
	            q.add((row - 1) + "," + col); //go up
	            q.add((row + 1) + "," + col); //go down
	            
	            // some checkings to compare elements (if they have the same color/number add an edge between them)
	            if (col > 0)
	            	if (grid[row][col] == grid[row][col - 1])
	            		addEdge(newMatrix[row][col], newMatrix[row][col - 1]);
	            if (col < l - 1)
	            	if (grid[row][col] == grid[row][col + 1])
	            		addEdge(newMatrix[row][col], newMatrix[row][col + 1]);	            
	            if (row > 0)
	            	if (grid[row][col] == grid[row - 1][col])
	            		addEdge(newMatrix[row][col], newMatrix[row - 1][col]);	            
	            if (row < h - 1)
	            	if (grid[row][col] == grid[row + 1][col])
	            		addEdge(newMatrix[row][col], newMatrix[row + 1][col]);
	        }
	    }
	
	}
}
