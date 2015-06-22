import java.util.*;
import java.io.*;
public class GraphDriver {
	/* Main method
	 * 
	 * takes in a input file representing a graph and determines all possible characters
	 */
	public static void main(String[] args) throws FileNotFoundException {
		if(args[0]==null)
		{
			System.out.println("Put a file in the arguments!");
			return;
		}
		Graph myGraph = new Graph(args[0]); //make graph
	/*	Graph oneLess = new Graph(myGraph);

		oneLess.removeEdge(0, 2);
		oneLess.removeEdge(0, 3);
		oneLess.removeEdge(0, 4);
		oneLess.removeEdge(0, 5);
		oneLess.removeEdge(0, 6);
		oneLess.removeEdge(0, 7);
		myGraph.printArray(); //print graph
		
		myGraph.findPath(); //construct spectrum
		
		
		System.out.println("===Graph 1===");
		myGraph.printSpectrum(false, true);
		System.out.println();
		
		oneLess.printArray();
		oneLess.findPath();
		System.out.println("===Graph 2===");
		oneLess.printSpectrum(false, true);
		System.out.println();
	/*
		Graph compy = myGraph.complement();
		compy.findPath();
		compy.printArray();
		System.out.println("===Complement===");
		compy.printSpectrum();
	
		myGraph.getSpectrum().compareWith(oneLess.getSpectrum()); */
		Deque<ArrayList<Integer>> stack = new ArrayDeque<ArrayList<Integer>>();
		makeAllCycles(stack);
	//	int minimum = 14;
		PrintStream writeToFile = new PrintStream(new File("maximallyEssentialCycles.txt"));
		for(ArrayList<Integer> i : stack)
		{
			Graph noCycle = new Graph(myGraph);
			Integer[] literallyCannotBelieveThisIsNecessary = new Integer[1];
			removeCycle((i.toArray(literallyCannotBelieveThisIsNecessary)), noCycle);
			noCycle.findPath();
			int numC =noCycle.getSpectrum().numberNormalized();
			if(numC == 8)
			{
				writeToFile.println(i.toString());
				noCycle.printArray(writeToFile);
		//		noCycle.printSpectrum(false, true);
		//		minimum = numC;
			}
		}
		writeToFile.close();
	}
	
	
	//removes the given path from the graph
	public static void removePath(Integer[] path, Graph graph)
	{
		for(int i=1; i<path.length; i++)
		{
			graph.removeEdge(path[i-1], path[i]);
		}
	}
	//removes the given cycle from the graph
	public static void removeCycle(Integer[] cycle, Graph graph)
	{
		graph.removeEdge(cycle[0], cycle[cycle.length-1]);
		removePath(cycle, graph);
	}
	
	public static void makeAllCycles(Deque<ArrayList<Integer>> stack)
	{
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(0);
		makeAllCycles(stack, path, 1);
		
	}
	
	public static void makeAllCycles(Deque<ArrayList<Integer>> stack, ArrayList<Integer> path, int length)
	{
		if(path.size()==8)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>(path);
			stack.addLast(temp);
			return;
		}
		else
		{
			for(Integer i=1; i<8; i++)
			{
				if(!path.contains(i))
				{
					path.add(i);
					makeAllCycles(stack,path, length+1);
					path.remove(i);
				}
			}
		}
	}
	
	
	
}
