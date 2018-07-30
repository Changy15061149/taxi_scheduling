
public class queue1 {
	
	int taxinum[] = new int[200];
	int sum = 0;
	int sx,sy,tx,ty;
	int way[][] = new int[100][100];
	int wayans[][] = new int[100][100];
	/*
	 * Overview : store the information of the chosen taxis. 
	 * 0 <= sx < 80 && 0 <= sy < 80 && 0 <= tx < 80 && 0 <= ty < 80 && 0 <= sum < 100 && wayans.length >= 100 && wayans[0].length >= 100 &&
	 * wayans != null && way.length >= 100 && way[0].length >= 100 && way != null
	 * 
	 */
	public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
		if(sx >= 80)return false;
		if(sy >= 80)return false;
		if(tx >= 80)return false;
		if(ty >= 80)return false;
		
		if(sx < 0)return false;
		if(sy < 0)return false;
		if(tx < 0)return false;
		if(ty < 0)return false;
		try{
		if(sum > 100 || sum < 0)return false;
		if(way == null)return false;
		if(way.length < 100)return false;
		if(way[0].length < 100)return false;
		
		if(wayans == null)return false;
		if(wayans.length < 100)return false;
		if(wayans[0].length < 100)return false;
		}catch (Exception e){return false;}
		return true;
	}

}
