import java.util.ArrayList;
import java.util.ListIterator;
public class information {
	public req rr;
	
	public ArrayList<Integer> getax = new ArrayList<Integer>();
	public ArrayList<Integer> getay = new ArrayList<Integer>();
	public ArrayList<Integer> surax = new ArrayList<Integer>();
	public ArrayList<Integer> suray = new ArrayList<Integer>();
	
	
	
	/*
	 * Overview : store the information for strong taxi. 
	 * 
	 * (getax != null) && (getay != null) && (surax != null) && (suray != null)
	 * 
	 */
	public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
		if(getax == null)return false;
		if(getay == null)return false;
		if(surax == null)return false;
		if(suray == null)return false;
		return true;
	}
	
}
