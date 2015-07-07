import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.util.Map.Entry;

public class GraphDriver {
	public static int minimum=14;
	public static int maximum=0;
	public static int count=0;
	public static int exception=0;
	public static PrintStream writeToFile;
	public static PrintStream forbiddenGraphs;
	public static Scanner sc;
	public static final int TO_REMOVE = 4;
	public static int[] spectrumSize = new int[15];
	/* Main method
	 * 
	 * takes in a input file representing a graph and determines all possible characters
	 */
	public static void main(String[] args) throws FileNotFoundException {
		/*Graph g = new Graph(3, true);
		g.removeEdge(0b000, 0b011);
		g.removeEdge(0b001, 0b010);
		g.removeEdge(0b100, 0b111);
		g.removeEdge(0b101, 0b110);
		g.findPath();
		g.getSpectrum().compareWith(complete.getSpectrum());
		complete.getSpectrum().compareWith(g.getSpectrum());
		
	*/
		classifyUniques(5);
		
		
		String timeStamp = new SimpleDateFormat("MM dd, yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(timeStamp);
		
	}
	
	public static void classifyUniques(int k)
	{
		Map<Map<Character,Integer>, Graph> spectra = new HashMap<Map<Character,Integer>,Graph>();
		Map<Map<Character,Integer>, Integer> specCount = new HashMap<Map<Character,Integer>,Integer>();
		Graph completeGraph = new Graph(3,true);
		completeGraph.findPath();
		Spectrum complete = completeGraph.getSpectrum();
		
		File in = new File(k + "edgeUniqueRejects.txt");
		try {
			sc = new Scanner(in);
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
		while(sc.hasNext())
		{
			Graph reject = new Graph(sc);
			reject.findPath();
			Map<Character,Integer> rejectSpec = reject.getSpectrum().getNormal();
			if(spectra.containsKey(rejectSpec))
			{
				specCount.put(rejectSpec, specCount.get(rejectSpec)+1);
			}
			else
			{
				spectra.put(rejectSpec, reject);
				specCount.put(rejectSpec, 1);
			}
			
		}
		sc.close();
		for(Map<Character,Integer> key : spectra.keySet())
		{
			System.out.println(specCount.get(key));
			spectra.get(key).printArray();
			Spectrum keySpec = new Spectrum(3,key);
			keySpec.isMissing(complete);
			System.out.println();
		}
	}
	
	
	public static void findProblem(Graph g)
	{
		Graph complete = new Graph(3,true);
		complete.findPath();
		
		for(int k=3; k<15; ++k)
		{
			File in = new File(k + "edgeUniqueRejects.txt");
			try {
				sc = new Scanner(in);
			} catch (FileNotFoundException e) {
		
				e.printStackTrace();
			}
			while(sc.hasNext())
			{
				Graph reject = new Graph(sc);
				if(g.isSubgraphOf(reject))
				{
					System.out.println(k);
					reject.printArray();
					if(k<=16)
					{
						reject.findPath();
						reject.isMissing(complete);
					}
				}
			}
			sc.close();
		}
	}
	
	public static void checkForCommon()
	{
		Deque<ArrayList<Integer>> stack = new ArrayDeque<ArrayList<Integer>>();
		makeAllCycles(stack);
		Integer[] path = new Integer[8];
		Character c = new Character(3,129);
		for(ArrayList<Integer> cycle : stack)
		{
			Graph cycleGraph = new Graph(3,false);
			addCycle(cycle.toArray(path), cycleGraph);
			cycleGraph.findPath();
			if(cycleGraph.getSpectrum().getCount(c)==6)
			{
				for(int i=0;i<8;i++)
				{
					System.out.print(cycle.get(i) + " ");
				}
				System.out.println();
			}
		}
		
		
	}

	public static void combRejects(int edgeNum, boolean goFast)
	{
		String name = "edgeRemovalRejects.txt";
		String newName = "edgeUniqueRejects.txt";

		try 
		{
			writeToFile = new PrintStream(new File(edgeNum + newName));
			ArrayList<Graph> rejects = new ArrayList<Graph>();

			if(goFast)
			{
				for(int k=3; k<edgeNum; ++k)
				{
					File in = new File(k + newName);
					sc = new Scanner(in);
					while(sc.hasNext())
					{
						Graph reject = new Graph(sc);
						rejects.add(reject);
					}
					sc.close();
				}
				System.out.println("Done loading.");
			}
			else
			{
				File in = new File((edgeNum-1) + name);
				sc = new Scanner(in);
				while(sc.hasNext())
				{
					Graph reject = new Graph(sc);
					rejects.add(reject);
				}
				sc.close();
			}
			File in2 = new File(edgeNum + name);
			sc = new Scanner(in2);

			while(sc.hasNext())
			{
				Graph toCheck = new Graph(sc);
				boolean isSub = false;
				for(int i=0; i<rejects.size() && !isSub ; i++)
				{
					isSub = toCheck.isSubgraphOf(rejects.get(i));
				}
				if(!isSub)
				{
					writeToFile.println(3);
					toCheck.printArray(writeToFile);
					++count;
				}
			}
			sc.close();

			System.out.println(count+" uniquely "+edgeNum+" edge rejects");
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

	}

	public static void testPrefix(String prefix)
	{
		Integer[] path = new Integer[8];
		Deque<ArrayList<Integer>> stack = new ArrayDeque<ArrayList<Integer>>();
		ArrayList<Integer> p = new ArrayList<Integer>();
		
		
		for(int i=0; i<prefix.length(); i++)
		{
			p.add(java.lang.Character.getNumericValue(prefix.charAt(i)));
		}
		
		makeAllCycles(stack, p,prefix.length());
		Spectrum s = new Spectrum(3);
		for(ArrayList<Integer> perm : stack)
		{
			Character c = new Character(perm.toArray(path),3);
			if(c.hashCode()==0b11010101)
				System.out.println(Arrays.toString(perm.toArray(path)));
			s.addToRaw(c);
		}
		s.normalizeMap();
	//	s.printSpectrum(true);
	}
	
	public static void weightEdge()
	{
		Integer[] path = new Integer[8];
		Deque<ArrayList<Integer>> stack = new ArrayDeque<ArrayList<Integer>>();
		ArrayList<Integer> p = new ArrayList<Integer>();
		makeAllCycles(stack, p,0);
		Map<String, Spectrum> mappy = new TreeMap<String, Spectrum>();
		
		for(ArrayList<Integer> perm : stack)
			{
				Character c = new Character(perm.toArray(path),3);
				String w = getWeight(path);
				if(w.indexOf('3')>=0 && (w.indexOf('3') < 2 || w.lastIndexOf('3') > 4))
					w = "starts with 3";
				else
					w = "does not start with 3";
				if(!mappy.containsKey(w))
				{
					Spectrum s = new Spectrum(3);
					s.addToRaw(c);
					mappy.put(w, s);
				}
				else
				{
					mappy.get(w).addToRaw(c);
				}				
			}
		//yay, mappy is whole!
		for(Entry<String, Spectrum> entry : mappy.entrySet())
		{
			entry.getValue().normalizeMap();
			System.out.println(entry.getKey()+": ");
			entry.getValue().printSpectrum();
		}
	}
	public static String getWeight(Integer[] path)
	{
		String weight = "";
		for(int i=0; i<7; ++i)
		{
			weight += Integer.bitCount(((int)path[i]) ^ ((int)path[i+1]));
		}
		return weight;
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
				System.out.println(count+" total accepted.");
				System.out.println(exception+" total excluded.");
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
		makeAllCycles(stack,path,0);
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
			for(Integer i=0; i<8; i++)
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

	public static void bruteForceAdd(int toAdd) throws FileNotFoundException
	{
		String partial = "edgesPartialSpectrum.txt";
		String full = "edgesFullSpectrum.txt";
		try 
		{
			writeToFile = new PrintStream(new File(toAdd+full));
			forbiddenGraphs = new PrintStream(new File(toAdd+partial));
			Graph g = new Graph(3,false);
			bruteForceAdd(g, 0, 0, 0, toAdd);
			System.out.println(count+" graphs with full spectrum.");
			System.out.println(exception+" graphs with partial non-empty spectrum.");
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		System.out.println(Arrays.toString(spectrumSize));
	}

	public static void bruteForceAdd(Graph g, int edges, int prevRow, int prevColumn, int toAdd)
	{
		if(edges == toAdd)
		{
			if(g.findPath())
			{
				int mySize = g.getSpectrum().numberNormalized();
				++spectrumSize[mySize];
				if(mySize == 14)
				{
					writeToFile.println(3);
					g.printArray(writeToFile);
				
					++count;
					System.out.println(count+" full so far. WE DID IT!");
				}
				else
				{
					++exception;
					forbiddenGraphs.println(3);
					g.printArray(forbiddenGraphs);
					if(exception%10000==0)
						System.out.println(exception+" partial so far.");
					if(mySize==1){
						g.printArray();
						System.out.println();
					}
				}
			}
		}
		else
		{
			for(int i = prevRow; i<8; i++)
			{	
				for(int j = (i==prevRow) ? prevColumn+1 : i+1; j<8; j++)
				{
					if(g.addEdge(i, j))
					{
						bruteForceAdd(g, edges+1, i, j, toAdd);
						g.removeEdge(i, j);
					}
				}
			}
		}
	}
	
	public static void bruteForceAddClever(int toAdd)
	{
		String partial = "edgesWithFragile.txt";
		String full = "edgesWithFragileFULL.txt";
		try 
		{
			writeToFile = new PrintStream(new File(toAdd+full));
			forbiddenGraphs = new PrintStream(new File(toAdd+partial));
			Graph g = new Graph(3,false);
			Integer[] path = {7,6,5,3,1,2,4,0};
			addPath(path, g);
			bruteForceAdd(g, 7, 0, 0, toAdd);
			System.out.println(count+" graphs with full spectrum.");
			System.out.println(exception+" graphs with partial non-empty spectrum.");
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		System.out.println(Arrays.toString(spectrumSize));
	}
	public static void scratchPaper(String s)
	{
/*		Integer[] path = {7,6,3,5,1,2,4,0};
		Character c = new Character(path, 3);
		System.out.println(c.toString());
		Integer[] path2 = {7,6,5,4,0,1,2,3};
		Character c2 = new Character(path2, 3);
		System.out.println(c2.toString());*/
	/*	Integer[] path2 = {0b11111,
						   0b11110,
						   0b11101,
						   0b11011,
						   0b10111,
						   0b01111,
						   0b11100,
						   0b11010,
						   0b11001,
						   0b10110,
						   0b10101,
						   0b10011,
						   0b01110,
						   0b01101,
						   0b01011,
						   0b00111,
						   0b11000,
						   0b10100,
						   0b10010,
						   0b10001,
						   0b01100,
						   0b01010,
						   0b01001,
						   0b00110,
						   0b00101,
						   0b00011,
						   0b00001,
						   0b00010,
						   0b00100,
						   0b01000,
						   0b10000,
						   0b00000};
	
	
	
		Character c2 = new Character(path2,5);
		System.out.println(c2.toString());
		*/
		
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
//			writeToFile.close();
		/*
		Graph thirteen = new Graph(s);
		thirteen.findPath();
		Graph other = new Graph(thirteen);
		other.removeEdge(2, 3);
		other.findPath();
		other.getSpectrum().compareWith(thirteen.getSpectrum());
		Graph complete = new Graph(3,true);
		complete.findPath();
		complete.printSpectrum();
		*/
		
		testPrefix("75");
		testPrefix("74");
		testPrefix("765");
		testPrefix("764");
		testPrefix("715");
		testPrefix("714");
		testPrefix("7614");
		testPrefix("7615");
		testPrefix("7604");
		testPrefix("7605");
		testPrefix("7105");
		testPrefix("7104");
		testPrefix("76104");
		testPrefix("76105");
		testPrefix("76014");
		testPrefix("76015");
		testPrefix("71604");
		testPrefix("71605");
		testPrefix("71065");
		testPrefix("71064");
	}
	
}
