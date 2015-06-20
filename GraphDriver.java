
public class GraphDriver {
	/* Main method
	 * 
	 * takes in a input file representing a graph and determines all possible characters
	 */
	public static void main(String[] args) {
		if(args[0]==null)
		{
			System.out.println("Put a file in the arguments!");
			return;
		}
		Graph myGraph = new Graph(args[0]); //make graph
		Graph oneLess = new Graph(myGraph);

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
	*/
		myGraph.getSpectrum().compareWith(oneLess.getSpectrum()); 
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
		graph.removeEdge(0, cycle.length-1);
		removePath(cycle, graph);
	}
	
	
	
}
