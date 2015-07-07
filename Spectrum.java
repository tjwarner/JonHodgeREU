import java.util.*;
import java.util.Map.Entry;


/**This class is a specialized container which holds a set of Characters 
 * with associated counts. The 'raw' map contains all characters, whereas 
 * the 'normal' map contains at most one representative from each character 
 * equivalence classes
 */
public class Spectrum {

	private int numQuestions;
	private Map<Character, Integer> raw = new HashMap<Character, Integer>();
	private Map<Character, Integer> normal = new TreeMap<Character, Integer>();
	
	public Spectrum(int numQ)
	{
		numQuestions = numQ;
	}
	
	public Spectrum(int numQ, Map<Character,Integer> normalIn) 
	{
		numQuestions = numQ;
		normal = normalIn;
		raw = null;
	}

	// Updates the normalized list to match the current raw list.
	public void normalizeMap()
	{
		int minHash=0;
		Map<Character,Integer> temp = new HashMap<Character,Integer>();
		for (Entry<Character, Integer> entry : raw.entrySet()) 
		{
			temp.put(entry.getKey(), entry.getValue());
		}
		
		for (Entry<Character, Integer> entry : (temp).entrySet()) 
		{
			if(entry.getValue()!=0)
			{
				minHash = entry.getKey().hashCode();
				String str = "";
				for(int i=0;i<numQuestions; i++)
					str+='a';
				StringBuffer strBuf = new StringBuffer(str);
				Deque<Character> stack = new ArrayDeque<Character>();
				stack = getPerms(strBuf,str.length(), entry.getKey(), (entry.getKey().hashCode()), stack);
				entry.getKey();
				while(stack.size()!=0)
				{
					Character top = stack.pop();
					if(temp.containsKey(top) && !top.equals(entry.getKey()))
					{
						temp.put(entry.getKey(), (temp.get(top) + temp.get(entry.getKey())));
						temp.put(top, 0);
					}
					if(top.hashCode()<minHash)
						minHash=top.hashCode();
				}
				Character n = new Character(numQuestions,minHash);
				normal.put(n, temp.get(entry.getKey()));
			
			}
		}
	
	}
	
	// returns a map of the raw, unordered set of characters as keys,
	// with the 'times seen' count as the value.
	public Map<Character, Integer> getRaw()
	{
		return raw;
	}
	
	// returns an ordered map of the equivalence classes of characters as keys,
	// with the 'times seen' count as value
	public Map<Character, Integer> getNormal()
	{
		return normal;
	}
	
	//returns true if this spectrum contains the character
	//by default, assumes the argument is in "standard form"
	public boolean contains(Character c)
	{
		return normal.containsKey(c);
	}
	
	public int getCount(Character c)
	{
		if(!normal.containsKey(c))
			return 0;
		else
			return normal.get(c);
	}
	
	public boolean equals(Spectrum s)
	{
		return normal.keySet().equals(s.normal.keySet());
		
/*		if(normal.size()==s.normal.size())
		{
			for(Character key : normal.keySet())
			{
				if(!s.normal.containsKey(key))
				{
					System.out.println("what the hey");
					return false;
				}
			}
			System.out.println("whats goin on");
			return true;
		}
		System.out.println("wrong size");
		return false;
*/	}
	
	//
	//
	public void compareWith(Spectrum s)
	{
		Spectrum common = new Spectrum(numQuestions);
		Spectrum spec1 = new Spectrum(numQuestions);
		Spectrum spec2 = new Spectrum(numQuestions);
		for(Entry<Character, Integer> entry : normal.entrySet())
		{
			Character c = entry.getKey();
			Integer count = entry.getValue();
			if(s.contains(c)) //this and s both contain c
			{
				common.normal.put(c, s.normal.get(c)  - count);
			}
			else //this contains c and s does not
			{
				spec1.normal.put(c,count);
			}	
		}
		for(Entry<Character, Integer> entry : s.normal.entrySet())
		{
			Character c = entry.getKey();
			Integer count = entry.getValue();
			if(!this.contains(c)) //s contains c and this does not 
			{
				spec2.normal.put(c, count);
			}
		}
		System.out.println("The two graphs have these characters in common, where each count is");
		System.out.println("the difference of its appearances in each graph (second - first)");
		common.printSpectrum();
		System.out.println("The following characters appear in the first graph but not the second:");
		spec1.printSpectrum();
		System.out.println("The following characters appear in the second graph but not the first:");
		spec2.printSpectrum();
	}
	
	
	
	// adds a character to the raw list.
	public void addToRaw(Character c)
	{
		if(raw.containsKey(c))
		{
			raw.put(c, raw.get(c)+1);
		}
		else
		{
			raw.put(c, 1);
		}
	}

	private Deque<Character> getPerms(StringBuffer str, int index, Character c, int code, Deque<Character> stack)
	{
		if(index <= 0)
		{ 
			Character newC = new Character(numQuestions,code);
			stack.push(newC);
		}          
		else 
		{ //recursively solve this by placing all other chars at current first pos
			stack = getPerms(str, index-1, c, code, stack);
			int currPos = str.length()-index;
			for (int i = currPos+1; i < str.length(); i++) 
			{//start swapping all other chars with current first char
				code = swap(str,currPos, i, c, code);
				stack = getPerms(str, index-1, c, code, stack);
				code = swap(str,i, currPos, c, code);//restore back my string buffer
			}

		}
		return stack;
	}

	private int swap(StringBuffer str, int pos1, int pos2, Character c, int code)
	{
		code = c.permuteQ(code, pos1, pos2);
		char t1 = str.charAt(pos1);
		str.setCharAt(pos1, str.charAt(pos2));
		str.setCharAt(pos2, t1);

		return code;

	}
	
	//prints the graph's spectrum to the screen. 
	//Normalized version is printed without total number of paths by default.
	public void printSpectrum()
	{
		printSpectrum(false, false);
	}
	
	//pass true as argument to print the un-normalized spectrum
	public void printSpectrum(boolean wantRaw, boolean wantNumberOfPaths)
	{
		int count = 0;
		Map<Character, Integer> temp;
		String phrase;
		if(wantRaw)
		{
			temp = raw;
			phrase = " characters ";
		}
		else
		{
			temp = normal;
			phrase = " equivalence classes ";
		}
		for(Entry<Character, Integer> entry : (temp).entrySet()) 
		{
			System.out.println(entry.getKey().toString() + " || " + entry.getValue());
			count+=entry.getValue();
		}
		System.out.println(temp.size() + " total" + phrase + "found.");
		if(wantNumberOfPaths)
		{
			System.out.println(count + " total paths found.");
		}
	}
	
	//pass true to print the raw spectrum.
	//by default, number of paths not printed.
	public void printSpectrum(boolean wantRaw)
	{
		printSpectrum(wantRaw, false);
	}
	
	public int numberNormalized()
	{
		return normal.size();
	}
	
	public int hashCode()
	{
		System.out.println(normal.keySet().hashCode());
		return normal.keySet().hashCode();
	}
	public void isMissing(Spectrum complete)
	{
		Spectrum missing = new Spectrum(numQuestions);
		for(Entry<Character, Integer> entry : complete.getNormal().entrySet())
		{
			Character c = entry.getKey();
			Integer count = entry.getValue();
			if(!contains(c))
			{
				missing.getNormal().put(c,count);
			}	
		}
		
		missing.printSpectrum();
		
		
	}
}
