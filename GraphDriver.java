import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
public class GraphDriver {
	public static int minimum=14;
	public static int maximum=0;
	public static int count=0;
	public static int exception=0;
	public static PrintStream writeToFile;
	public static PrintStream forbiddenGraphs;
	public static Scanner sc;
	public static final int TO_REMOVE = 9;
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

		bruteForce();
		
		String timeStamp = new SimpleDateFormat("MM dd, yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(timeStamp);
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
	//	Deque<ArrayList<Integer>> stack = new ArrayDeque<ArrayList<Integer>>();
	//	makeAllCycles(stack);
	//	int minimum = 14;
	//	PrintStream writeToFile = new PrintStream(new File("inessentials.txt"));
	//	for(ArrayList<Integer> i : stack)
	//	{
			
		//	Graph noCycle = new Graph(myGraph);
		//	Integer[] literally = new Integer[8];
		//	makeAllCubics(i.toArray(literally));
		/*	removeCycle((i.toArray(literallyCannotBelieveThisIsNecessary)), noCycle);
			noCycle.findPath();
			int numC =noCycle.getSpectrum().numberNormalized();
			if(numC == 14)
			{
				for(int j=0; j<8; j++)
				{
					writeToFile.print(i.get(j) + " ");
				}
				writeToFile.println();
		//		writeToFile.println(3);
		//		noCycle.printArray(writeToFile);
		//		noCycle.printSpectrum(false, true);
		//		minimum = numC;
			}*/
	//	}
//		writeToFile.close();
		
	}
	
	public static void pathPowers()
	{
		Deque<ArrayList<Integer>> stack = new ArrayDeque<ArrayList<Integer>>();
		makeAllCycles(stack);
		Integer[] literally = new Integer[8];
		int max = -1;
		int min = 15;
		for(ArrayList<Integer> path : stack)
		{
			Graph myGraph = new Graph(3, false); //make graph
			addPath((path.toArray(literally)), myGraph);
			int power = 0;
			int prev = 0;
			for(int i=1; i<8 && prev!=14; i++)
			{
				Graph temp = myGraph.power(i);
				temp.findPath();
				prev = temp.getSpectrum().numberNormalized();
				power = i;
			}
			if(power>=max)
			{
				max = power;
				System.out.println(max);
				System.out.println(path.toString());
			}
			if(power<=min)
			{
				min = power;
				System.out.println(min);
				System.out.println(path.toString());
			}
		}
	}
	
	public static void bruteForce() throws FileNotFoundException
	{
		try
		{
			File in = new File((TO_REMOVE-1)+"edgesRemoved.txt");
			sc = new Scanner(in);
			writeToFile = new PrintStream(new File(TO_REMOVE+"edgesRemoved.txt"));
			forbiddenGraphs = new PrintStream(new File(TO_REMOVE+"edgeRemovalRejects.txt"));
			while(sc.hasNext())
			{
				Graph g = new Graph(sc);
				int oldRow = sc.nextInt();
				int oldColumn = sc.nextInt();
				bruteForce(g, TO_REMOVE-1, oldRow, oldColumn);
			}
			System.out.println(count+" total accepted.");
			System.out.println(exception+" total excluded.");
		}
		catch (FileNotFoundException e)
		{
			try 
			{
				writeToFile = new PrintStream(new File(TO_REMOVE+"edgesRemoved.txt"));
				forbiddenGraphs = new PrintStream(new File(TO_REMOVE+"edgeRemovalRejects.txt"));
				Graph g = new Graph(3,true);
				bruteForce(g, 0, 0, 0);
				System.out.println(count+" total found.");
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void bruteForce(Graph g, int edgesMissing, int prevRow, int prevColumn)
	{
		if(edgesMissing == TO_REMOVE)
		{
			g.findPath();
			if(g.getSpectrum().numberNormalized() == 14)
			{
				writeToFile.println(3);
				g.printArray(writeToFile);
				writeToFile.print(prevRow + " ");
				writeToFile.println(prevColumn);
				++count;
				if(count%100==0)
					System.out.println(count+" accepted so far.");
				
			}
			else
			{
				++exception;
				forbiddenGraphs.println(3);
				g.printArray(forbiddenGraphs);
				if(exception%100==0)
					System.out.println(exception+" thrown out so far.");
			}
		}
		else
		{
			for(int i = prevRow; i<8; i++)
			{	
				for(int j = (i==prevRow) ? prevColumn+1 : i+1; j<8; j++)
				{
					if(g.removeEdge(i, j))
					{
						bruteForce(g, edgesMissing+1, i, j);
						g.addEdge(i, j);
					}
				}
			}
		}
	}
	
	public static void addPath(Integer[] path, Graph graph)
	{
		for(int i=1; i<path.length; i++)
		{
			graph.addEdge(path[i-1], path[i]);
		}
	}
	//removes the given path from the graph
	public static void removePath(Integer[] path, Graph graph)
	{
		for(int i=1; i<path.length; i++)
		{
			graph.removeEdge(path[i-1], path[i]);
		}
	}
	public static void addCycle(Integer[] cycle, Graph graph)
	{
		graph.addEdge(cycle[0], cycle[cycle.length-1]);
		addPath(cycle, graph);
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
		path.add(1);
		makeAllCycles(stack, path, 2);
		path.clear();
		path.add(0);
		path.add(3);
		makeAllCycles(stack,path,2);
		path.clear();
		path.add(0);
		path.add(7);
		makeAllCycles(stack,path,2);
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
	
	public static void makeAllCubics() throws FileNotFoundException
	{
		File in = new File ("inessentials.txt");
		Scanner sc = new Scanner(in);
		while(sc.hasNext())
		{
			Integer[] cycle = new Integer[8];
			for(int i=0;i<8;i++)
			{
				cycle[i] = sc.nextInt();
			}
			makeAllCubics(cycle);
		}
		
		
		sc.close();
	}
	public static void makeAllCubics(Integer[] cycle)
	{
		Integer[] kind1 = {0,6,1,7,2,4,3,5};
	//	Integer[] kind2 = {0,6,1,3,2,5,4,7}; //kind2==kind3 kinda
		Integer[] kind3 = {0,4,1,7,2,6,3,5};
		Integer[] kind4 = {0,6,1,4,2,5,3,7};
		Integer[] kind5 = {0,5,1,4,2,7,3,6};
		Integer[] kind6 = {0,4,1,5,2,6,3,7}; //kind6==kind7 kinda
	//	Integer[] kind7 = {0,4,1,6,2,5,3,7};
		cubicOfAKind(cycle, kind1, 4);
	//	cubicOfAKind(cycle, kind2, 8);
		cubicOfAKind(cycle, kind3, 4);
		cubicOfAKind(cycle, kind4, 8);
		cubicOfAKind(cycle, kind5, 2);
		cubicOfAKind(cycle, kind6, 1);
	//	cubicOfAKind(cycle, kind7, 4);
	}
	public static void cubicOfAKind(Integer[] cycle, Integer[] kind, int multiplicity)
	{
		for(int i=0; i<multiplicity; i++)
		{
			Graph graph = new Graph(3, false);
			addCycle(cycle, graph);
			for(int j=0; j<4; j++)
			{
				graph.addEdge(cycle[(kind[2*j]+i)%8], cycle[(kind[2*j+1]+i)%8]);
				
			}
			doStuffToGraph(graph, kind);
		}
	}
	public static void doStuffToGraph(Graph g, Integer[] cycle)
	{
		g = g.complement();
		g.findPath();
		
	}
}
