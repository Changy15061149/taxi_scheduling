import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import test.TaxiGUI;


public class reqinput extends Thread{
	//[CR,src,dst]
	Scanner sos = new Scanner(System.in);
	int sum = 0;
	req rrs[] = new req[350];
	pubwork pw;
	init ini;
	taxis txs;
	int taxisum;
	int nummax;
	TaxiGUI gui;
	/*
	 * Overview : read the requests. 
	 * 
	 * rrs != null && rrs.length >= 350 && 345 > sum >= 0
	 * sos != null
	 */
	public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
		try{
		if(rrs == null)return false;
		if(rrs.length < 350) return false;
		if(sum > 345 || sum <0) return false;
		}catch (Exception e){return false;}
		if(sos == null)return false;
		return true;
	}
	public reqinput(int ts,int ns,pubwork pw1,taxis txx,TaxiGUI gg)
	/*
	@REQUIRES:all class not null
	@MODIFIES:pw,taxisum,ns,txs,gui;
	@EFFECTS:
	    (pw = pw1);
		(taxisum = ts);
		(nummax = ns);
		(txs = txx);
		(gui = gg);
   */
	{
		pw = pw1;
		taxisum = ts;
		nummax = ns;
		txs = txx;
		gui = gg;
	}
	public String input()
	/*
	@EFFECTS:
	      (\result = sos.nextLine().replaceAll("\\s",""));
   */
	{
		String inputs = sos.nextLine();
		inputs = inputs.replaceAll("\\s","");
		return inputs;
	}
	public int match(String input1)///////////////////////need_modify
	/*
	@EFFECTS:
	       (input1 matches s1) ==> \result == 1;
	       (input1 matches s2) ==> \result == 2;
	       (input1 ! matches s1 ;input1 ! matches s2) ==> \result == -1;
   */
	{
		String s1 = "\\[CR,\\(\\+?\\d+,\\+?\\d+\\),\\(\\+?\\d+,\\+?\\d+\\)]";
		String s2 = "\\[CW,\\(\\+?\\d+,\\+?\\d+\\),\\+?\\d+]";
		Pattern p1,p2;
		p1 = Pattern.compile(s1);
		p2 = Pattern.compile(s2);
		Matcher m1,m2;
		m1 = p1.matcher(input1);
		m2 = p2.matcher(input1);
		boolean ans1,ans2,ans;
		ans1 = m1.find();
		ans2 = m2.find();
		ans = ans1 | ans2;
		if(ans1)
		{
			//System.out.println(m1.start()+" "+m1.end()+" "+input1.length());
			if(m1.start() != 0 || m1.end() != input1.length())
				return -1;
		}
		if(ans2)
		{
			if(m2.start() != 0 || m2.end() != input1.length())
				return -1;
		}
		if(ans1)
		   return 1;
		if(ans2)
		   return 2;
		else return -1;
	}
	public void run()
	 /*
	@MODIFIES:this
    @EFFECTS:
    	(input = sos.nextLine().replaceAll("\\s",""));
    	(input1 matches s1) ==>(rrs[sum] = new req(txs,gui);sum ++;initialize all data);
	    (input1 matches s2) ==> pw.modifymap(sx,sy,ty);;
   */
	{
		try{
		while (true)
		{
			boolean bl = false;
			String str = input();
			if(match(str) == 1)
			{
				str = str.replaceAll("\\[", "");
				str = str.replaceAll("\\]", "");
				str = str.replaceAll("\\(", "");
				str = str.replaceAll("\\)", "");
				String a[] = str.split(",");
				int sx = 0,sy = 0,tx = 0,ty = 0;
			 try
			 {
				sx = Integer.parseInt(a[1]);
				sy = Integer.parseInt(a[2]);
				tx = Integer.parseInt(a[3]);
				ty = Integer.parseInt(a[4]);
			 }catch (Exception e){bl = true;}
			 if(!bl)
			 {
				 if(sx < 0 || sx >= nummax)bl = true;
				 if(sy < 0 || sy >= nummax)bl = true;
				 if(tx < 0 || tx >= nummax)bl = true;
				 if(ty < 0 || ty >= nummax)bl = true;
				 if(sx == tx && sy == ty)bl = true;
				 for(int i = 0;i < sum;i ++)
					 if(rrs[i].live && rrs[i].sx == sx-1 && rrs[i].sy == sy&&rrs[i].tx == tx&&rrs[i].ty == ty )
						 bl = true;
			 }
			// System.out.println(bl);
			 if(!bl && sum < 320)
			 {
				// sx--;tx--;
				// sy--;ty--;
				 rrs[sum] = new req(txs,gui);
				 rrs[sum].pw = pw;
				 rrs[sum].sx = sx;
				 rrs[sum].sy = sy;
				 rrs[sum].tx = tx;
				 rrs[sum].ty = ty;
				 rrs[sum].q.sx = sx;
				 rrs[sum].q.sy = sy;
				 rrs[sum].q.tx = tx;
				 rrs[sum].q.ty = ty;
				 rrs[sum].taxisum = taxisum;
				 ini.find(sx,sy);
				 for(int i = 0;i < nummax; i ++)
					 for(int j = 0;j < nummax;j ++)
				{
						 rrs[sum].q.way[i][j] = ini.way[i][j];
						 rrs[sum].q.wayans[i][j] = ini.wayans[i][j];
				}
				 rrs[sum].start();
				 sum ++;
			 }
			 else System.out.println("illegal input!");
			 
			}
			else if(match(str) == 2)
			{
				str = str.replaceAll("\\[", "");
				str = str.replaceAll("\\]", "");
				str = str.replaceAll("\\(", "");
				str = str.replaceAll("\\)", "");
				String a[] = str.split(",");
				int sx = 0,sy = 0,ty = 0;
			 try
			 {
				sx = Integer.parseInt(a[1]);
				sy = Integer.parseInt(a[2]);
				ty = Integer.parseInt(a[3]);
			 }catch (Exception e){bl = true;}
			 
			 if(sx < 0 || sx >= 80 || sx < 0 || sy >= 80 || ty < 0 || ty >= 4)bl = true;
			 if(bl) System.out.println("illegal input!");
			 else
			 {
				 pw.modifymap(sx,sy,ty);
			 }
			}
			else System.out.println("illegal input!");
		}
	  }catch (Exception e){}
	}
}
