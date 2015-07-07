
public class Character implements Comparable<Character> {
	private int numVertices;
	private int numQuestions;
	//private Set<Set<Integer>> powerSet = new HashSet<Set<Integer>>();
	private Boolean[] isSep = new Boolean[32];
	
	public Character()
	{
		numVertices=8;
		numQuestions=3;
	}
	public Character(int numQ, int code)
	{
		numVertices = 1 << numQ;
		numQuestions = numQ;
		for(int i=0; i<numVertices; i++)
		{
			if(1==((code >> i) & 1))
				isSep[i]=true;
			else
				isSep[i]=false;
		}
	}
	//Constructor here
	public Character(Integer[] path, int numQ)
	{
		numVertices = 1 << numQ;
		numQuestions = numQ;
		isSep[0]=true; //represents empty set
		isSep[numVertices-1]=true; //represents full question set
		for(int subset=1; subset<numVertices-1; subset++)
		{
			isSep[subset] = isSeparable(path, subset);
		}
	}
	
	//helper method to decide whether a given set is separable,
	//given a preference matrix
	private Boolean isSeparable(Integer[] path, int subset)
	{
		int fixed = numVertices - 1 - subset;
		int[] count = new int[32]; //this counts how many times we've seen each fixed value
		Integer[] order = new Integer[32]; //keeps track of 'right' order
		for(int i=0; i < numVertices; i++)
		{
			//'place' is how many times we have previously encountered this set
			//of outcomes for the fixed numbers
			int place = count[path[i]&fixed]++;
			if(order[place] == null)
			{
				//there is no 'right' answer yet
				order[place] = (path[i]&subset);
			}
			else if(order[place] != (path[i]&subset))
			{
				//we have the 'wrong' answer; the subset is not separable
				return false;
			}
			
		}
		//we checked the whole path and found no inseparability
		return true;
	}
	
	public String toString()
	{
		String s = "{}";
		for(int i=1; i<numVertices; i++)
		{
			if(isSep[i])
			{
				s+=", {";
				for(int q=1; q<=numQuestions; q++)
					if((i/(1 << (q-1))%2 == 1))
					{
						s+=q;	
					}
				s += "}";
			}
		}
		return s;
	}
	//equals function
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		if(o instanceof Character)
		{
			for(int i=1; i<numVertices-1; i++)
			{
				if(isSep[i]!=((Character)o).isSep[i])
				{	
					return false;
				
				}
			}
			return true;
		}
		else return false;
	}
	
	//hashcode
	
	public int hashCode()
	{
		int code = 1;
		for(int i = 1; i<numVertices; i++)
		{
			if(isSep[i])
			{
				code = code + (1<<i);
			}
		}
		return code;
	}
	
	//helper method for generating permutations of the hashcode
	//pos1 pos2 are zero indexed
	public int bitSwap(int i, int pos1, int pos2) {

	    int bit1 = (i >> pos1) & 1;
	    int bit2 = (i >> pos2) & 1;

	    if (bit1 == bit2)
	        return i; // no need to swap

	    int mask = (1 << pos1) | (1 << pos2);

	    return i ^ mask;
	}
	
	//helper method for generating permutations of the hashcode
	//q1 q2 are zero indexed
	public int permuteQ(int code, int q1, int q2)
	{
		int newCode = (1<<(numVertices-1))+1;
		for(int i=1; i<numVertices-1; i++)
		{
			newCode += (((code >> bitSwap(i,q1,q2)) & 1) << i);
		}
		return newCode;
	}
	public int compareTo(Character newC)
	{
		return hashCode() - ((Character)newC).hashCode();
	}
	
}
