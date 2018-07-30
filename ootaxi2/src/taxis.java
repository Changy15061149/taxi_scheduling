
public class taxis{
	taxi [] taxx = new taxi[200];
	public boolean repOK()
	/*
	 * Overview : store taxis. 
	 * taxx != null && taxx.length >= 104
	 * 
	 */
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
	  try{
		if(taxx == null)return false;
		if(taxx.length < 104) return false;
	  }catch (Exception e){return false;}
		return true;
	}
	
}
