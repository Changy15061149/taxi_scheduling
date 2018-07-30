
public class for_tester extends Thread{
	taxis taxs;
	reqinput rqs;
	/*
	 * Overview : enable tester to test my code. 
	 * 
	 * rqs != null && taxs != null;
	 * 
	 * 
	 */
	public for_tester(taxis taxs1,reqinput rqs1)
	/*
	 @MODIFIES:rqs,taxs
	    @EFFECTS:
	    rqs == rqs1;
		taxs == taxs1;
	*/
	{
		rqs = rqs1;
		taxs = taxs1;
	}
	public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
		if(rqs == null)return false;
		if(taxs == null)return false;
		return true;
	}
	public void printtaxi(int pos)
	/*
	    @REQUIRES:0<pos<100
	    @MODIFIES:
	    @EFFECTS:
	    print information of taxs.taxx[pos]
	*/
	{
		if(pos < 0 || pos >= 100){ System.out.println("number limit exceed!");return;}
		else System.out.println("taxinumber:" + pos + " now at ("+ taxs.taxx[pos].x +","+ taxs.taxx[pos].y +") credit = "+ taxs.taxx[pos].credit + " state = " +taxs.taxx[pos].state);
			
	}
	public void printreq(int pos)
	/*
    @REQUIRES:0<pos<rqs.sum
    @MODIFIES:
    @EFFECTS:
    print information of rqs.rrs[pos]
*/
	{
		if(pos < 0 || pos >= rqs.sum) {System.out.println("number limit exceed!");return;}
		System.out.println("req :"+ pos + "starttime:" +( rqs.rrs[pos].lastime - rqs.rrs[pos].basetime ) / 1000.0);
		
		if(rqs.rrs[pos].live)System.out.println("now live!");
		else System.out.println("now end!");
		System.out.println("from: (" + rqs.rrs[pos].sx +","+ rqs.rrs[pos].sy + ") to (" + rqs.rrs[pos].tx + "," + rqs.rrs[pos].ty+")");
		
	}
	/*
	void test1()
	{
		taxi_new tt = null;
		System.out.println(" ---------------------------------------------- ");
		while(true)
		{
			for(int i = 1;i < 100;i ++)
			{
				if(taxs.taxx[i-1].sum_of_information != 0)
				{
					tt = taxs.taxx[i-1];
					//System.out.println(i-1);
					break;
				}
			}
			if(tt == null)
			{
				try
				{sleep(30);}catch(Exception e){}
			}
			else break;
		}
		System.out.println(" ---------------------------------------------- ");
		while(tt.sum_of_information != 2)
		{
			try
			{sleep(30);}catch(Exception e){}
		}
		if(tt.sum_of_information == 2)
		{
			System.out.println("_________________________________________________");
			information info;
			while(tt.hasnext())
			{
				info = tt.next();
				System.out.println(info.rr.sx + " " + info.rr.sy + " " + info.rr.tx + " "+ info.rr.ty);
				System.out.println((info.rr.lastime - info.rr.basetime)/1000);
				int i = 0;
				while(i < info.getax.size())
				{
					System.out.println(info.getax.get(i) + " " + info.getay.get(i));
					
					i ++;
				}
				i = 0;
				while(i < info.surax.size())
				{
					System.out.println(info.surax.get(i) + " " + info.suray.get(i));
					i ++;
				}
				i = 0;
			}
			System.out.println("------------------------------------------------------");
			while(tt.hasprevious())
			{
				info = tt.previous();
				System.out.println(info.rr.sx + " " + info.rr.sy + " " + info.rr.tx + " "+ info.rr.ty);
				System.out.println((info.rr.lastime - info.rr.basetime)/1000);
				int i = 0;
				while(i < info.getax.size())
				{
					System.out.println(info.getax.get(i) + " " + info.getay.get(i));
					
					i ++;
				}
				i = 0;
				while(i < info.surax.size())
				{
					System.out.println(info.surax.get(i) + " " + info.suray.get(i));
					i ++;
				}
				i = 0;
			}
			
		}
	}*/
	public void run()
	{
		System.out.println("for_tester start!");
		try
		{
		/*
			
			please add your code here!
			
		*/	
			/*while(true)
			{
				printreq(0);
				printtaxi(1);
				try
				{sleep(1000);}
				catch (Exception e){}
			}*///these are examples
		}catch(Exception e)
		{}
		
	}

}
