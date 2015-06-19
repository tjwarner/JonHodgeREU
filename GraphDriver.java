
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
		
		myGraph.printArray(); //print graph
		
		myGraph.findPath(); //construct spectrum
		System.out.println("\n"); 
		
		System.out.println("===Cube Graph===");
		myGraph.printSpectrum();
		System.out.println();
		
	
		Graph compy = myGraph.complement();
		compy.findPath();
		compy.printArray();
		System.out.println("===Cube Complement===");
		compy.printSpectrum();
	}
}
