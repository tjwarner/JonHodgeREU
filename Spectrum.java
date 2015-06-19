import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
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
	
	public Spectrum() 
	{
	}

	// Updates the normalized list to match the current raw list.
	public void normalizeMap()
	{
		int minHash=0;
		Map<Character,Integer> temp = raw;
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
}
