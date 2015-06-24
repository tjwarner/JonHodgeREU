/** Graph.java
 * 
 * A program to take in a graph file, process it, and output 
 * the character
 * 
 * @author Selene Chew and TJ Warner
 */
import java.util.*;

import java.io.*;

public class Graph {

	public static final int MAX_SIZE = 32;
	
	private Integer[][] adjMatrix = new Integer[MAX_SIZE][MAX_SIZE];
	private int numQuestions;
	private int numRows;
	private Spectrum spectrum;
	/*
	 * The constructor
	 * 
	 */
	public Graph(Graph old)
	{
		numQuestions = old.numQuestions;
		numRows = old.numRows;
		adjMatrix = new Integer[MAX_SIZE][MAX_SIZE];
		for(int i=0; i<numRows; i++)
		{
			for(int j=0; j<numRows; j++)
			{
				adjMatrix[i][j] = old.adjMatrix[i][j];
			}
		}
		spectrum = new Spectrum(numQuestions);
	}
	public Graph(int numQ, boolean complete)
	{
		numQuestions = numQ;
		numRows = 1 << numQ;
		int fill = 0;
		if(complete)
			fill = 1;
		for(int i=0; i<numRows; i++)
		{
			for(int j=0; j<numRows; j++)
			{
				if(i==j)
					adjMatrix[i][j] = 0;
				else
					adjMatrix[i][j] = fill;
			}
		}
	}
	public Graph(String filename) {
		
		try{
			File in = new File (filename);

			Scanner sc = new Scanner(in);

			numQuestions = sc.nextInt();
			numRows = (1<<numQuestions);
			spectrum = new Spectrum(numQuestions);
			for(int i = 0; i < numRows; i++) {
				for(int j = 0; j < numRows; j++) {
					adjMatrix[i][j] = sc.nextInt();
				}
			}

			sc.close();

		} catch(FileNotFoundException exception) {
			System.out.println(filename + " is not a valid file!");
		}

	}
	public Graph(Scanner sc)
	{
		numQuestions = sc.nextInt();
		numRows = (1<<numQuestions);
		spectrum = new Spectrum(numQuestions);
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numRows; j++) {
				adjMatrix[i][j] = sc.nextInt();
			}
		}
	}
	public Graph(Integer[][] adjacency, int numQ)
	{
		adjMatrix = adjacency;
		numQuestions = numQ;
		numRows = (1<<numQuestions);
		spectrum = new Spectrum(numQuestions);
	}

	/*
	 * Print out adjacency matrix
	 */
	public void printArray() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numRows; j++) {
				System.out.print(adjMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void printArray(PrintStream stream)
	{
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numRows; j++) {
				stream.print(adjMatrix[i][j] + " ");
			}
			stream.println();
		}
	}
	
	
	public int getRows()
	{
		return numRows;
	}
	
	

	/*
	 * find all Hamiltonian Paths
	 */
	public void findPath() {
		boolean[] b = new boolean[MAX_SIZE];
		Integer[] p = new Integer[MAX_SIZE];
		spectrum = new Spectrum(numQuestions);
		findPath(b,p,0);
		spectrum.normalizeMap();
	}
	public void findPath(boolean[] b, Integer[] path, int length) {
		if(length == numRows) {
			//call function to determine character
			Character c = new Character(path, numQuestions);
		/*
			Character target = new Character(numQuestions, 0b10010111);
			if(c.equals(target))
			{
				System.out.println();
				for(int k = 0; k < numRows; k++) {
					System.out.print(path[k] + " ");
				}
				System.out.println();
			}
		*/
			
	//		System.out.println(c.toString());
			spectrum.addToRaw(c);
			return;
		}
		//length is less than numRows
		for(int next=0; next<numRows; next++) {
			if(length==0 || (!b[next] && adjMatrix[next][path[length-1]]==1)) {
				b[next]=true;
				path[length]=next;
				findPath(b,path,length+1);
				b[next]=false;
			}
		}
	}



	
	//prints the graph's spectrum to the screen. Normalized version is printed by default.
	public void printSpectrum()
	{
		spectrum.printSpectrum();
	}
	
	//pass true as argument to print the un-normalized spectrum
	public void printSpectrum(boolean wantRaw)
	{
		spectrum.printSpectrum(wantRaw);
	}
	
	public void printSpectrum(boolean wantRaw, boolean wantNumberOfPaths)
	{
		spectrum.printSpectrum(wantRaw, wantNumberOfPaths);
	}
	
	//returns the graph's character spectrum. Will be empty if findPath() has yet to be called
	//on this graph
	public Spectrum getSpectrum()
	{
		return spectrum;
	}
	
	//generates the complement graph of a given graph, in which two vertices are adjacent
	//iff they are not adjacent in the original graph 
	public Graph complement()
	{
		Integer[][] compAdj = new Integer[MAX_SIZE][MAX_SIZE];
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numRows; j++) {
				if(i!=j){
					compAdj[i][j] = 1 - adjMatrix[i][j];
				}
				else
				{
					compAdj[i][j] = 0;
				}
			}
		}
		return new Graph(compAdj, numQuestions);
	}
	
	//Changes the current graph by adding an edge to it.
	//precondition: i and j in bounds
	//postcondition: spectrum set to null if the graph is changed
	public void addEdge(int i, int j)
	{
		if(i!=j && adjMatrix[i][j]==0)
		{
			adjMatrix[i][j] = 1;
			adjMatrix[j][i] = 1;
			spectrum = null;
		}
	}
	//Changes the current graph by adding an edge to it.
	//precondition: i and j in bounds
	//postcondition: spectrum set to null and returns true if the graph is changed
	public boolean removeEdge(int i, int j)
	{
		if(i!=j && adjMatrix[i][j]==1)
		{
			adjMatrix[i][j] = 0;
			adjMatrix[j][i] = 0;
			spectrum = null;
			return true;
		}
		return false;
	}
	
	public Integer[][] distanceMatrix()
	{
		Integer[][] dist = new Integer[MAX_SIZE][MAX_SIZE];
		for(int i=0; i<numRows; i++)
		{
			for(int j=0; j<numRows; j++)
			{
				if(i==j)
					dist[i][j] = 0;
				else if(adjMatrix[i][j]==1)
					dist[i][j] = 1;
				else
					dist[i][j] = numRows;
			}
		}
		for(int k=0; k<numRows; k++)
		{
			for(int i=0; i<numRows; i++)
			{
				for(int j=0; j<numRows; j++)
				{
					if (dist[i][j] > dist[i][k] + dist[k][j])
						dist[i][j] = dist[i][k] + dist[k][j];
				}
			}
		}
		
		
		return dist;
	}
	public Graph power(int power)
	{
		Graph g = new Graph(numQuestions,false);
		Integer [][] d = this.distanceMatrix();
		for(int i=0; i<numRows; i++){
			for(int j=i+1; j<numRows; j++){
				if(d[i][j]<=power)
					g.addEdge(i, j);
			}
		}
		
		
		return g;
	}
}
