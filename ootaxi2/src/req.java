import java.awt.Point;

//import test.TaxiGUI;

//import test.TaxiGUI;


public class req extends Thread{
	TaxiGUI gui;
	taxis tt;
	long lastime = 0;
	long basetime;
	pubwork pw;
	int taxisum;
	int sx,sy,tx,ty;
	queue1 q = new queue1();
	boolean live;
	/*
	 * Overview : store the information of the requires. 
	 * 
	 * 0 <= sx < 80 &&0 <= sy < 80 &&0 <= tx < 80 &&0 <= ty < 80 && q != null && tt != null
	 * 
	 */
	public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
		if(sx >= 80)return false;
		if(sy >= 100)return false;
		if(tx >= 80)return false;
		if(ty >= 100)return false;
		
		if(sx < 0)return false;
		if(sy < 0)return false;
		if(tx < 0)return false;
		if(ty < 0)return false;
		
		if(q == null)return false;
		if(tt == null)return false;
		return true;
	}
	public req(taxis txs,TaxiGUI gg)
	/*
	@MODIFIES:this
    @EFFECTS:
    	(tt = txs);
		(basetime = tt.taxx[0].basetime);
		(gui = gg);
   */
	{
		tt = txs;
		basetime = tt.taxx[0].basetime;
		gui = gg;
	}
	public req(long time,long bstime)
	/*
	@MODIFIES:this
    @EFFECTS:
    	(lastime == time);
   */
	{
		lastime = time;
		basetime = bstime;
	}
	public void addtaxiqueue(int i)
	/*
	@MODIFIES:this
    @EFFECTS:
    	(q.taxinum[q.sum] = i);
		(q.sum++);
		(tt.taxx[i].credit ++);
   */
	{
		q.taxinum[q.sum] = i;
		q.sum++;
		tt.taxx[i].credit ++;
	}
	public boolean findtaxiqueue(int i)
	/*
	@EFFECTS:
    	(\exists ii;q.taxinum[ii] == i)\result == true;
    	(\all ii;q.taxinum[ii] != i)\result == false;
   */
	{

		for(int ii = 0;ii < q.sum;ii ++)
			if(q.taxinum[ii] == i)return true;
		return false;
	}
    public void run()
    /*
    @MODIFIES:this
	@EFFECTS:
    	(\exists i;-2 <= tt.taxx[i].x - sx && tt.taxx[i].x - sx <= 2 && -2 <= tt.taxx[i].y - sy && tt.taxx[i].y - sy <= 2 && tt.taxx[i].free;
    	 ! findtaxiqueue(i))
    		==> addtaxiqueue(i);
   */
    {
     try{
    	live = true;
    	lastime  = System.currentTimeMillis();
    	gui.RequestTaxi(new Point(sx,sy), new Point(tx,ty));
    	System.out.println("req start:" +( lastime - basetime ) / 1000.0);
    	while(true)
    	{
    		long nowtime = System.currentTimeMillis();
    		if(nowtime - lastime >= 3000)
    		{
    			System.out.println("shortest way:" + q.wayans[tx][ty]);
    			boolean sus = pw.alloc(q);
    			System.out.println("req end:" +( lastime + 3000 - basetime ) / 1000.0);
    			if(sus)System.out.println("Successfully alloced!");
    			else System.out.println("Unuccessfully alloced!");
    			break;
    		}
    		else
    		{
    			for(int i = 0;i < taxisum; i ++)
    			{
    				int xxx = tt.taxx[i].x - sx;
    				int yyy = tt.taxx[i].y - sy;
    				if(-2 <= xxx && xxx <= 2 && -2 <= yyy && yyy <= 2 && tt.taxx[i].free)
    				{
    					if(!findtaxiqueue(i))
    					{
    						addtaxiqueue(i);
    					}
    				}
    			}
    		}try{
  			  sleep(20);
  			}catch(Exception e){};
    	}live = false;
      }catch (Exception e){}
    }

}
