import java.awt.Point;
public class pubwork {
	private boolean full = true;
	private boolean mapsig = true;
	taxis txs;
	int nummax;
	init ini;
	TaxiGUI gui;
	
	/*
	 * Overview : solve the public problems. 
	 * 
	 * strs != null && x != null && x.length >= 10000 && y.length >= 10000
	 * && map != null && wayans != null && findway != null && map.length >= 100
	 * && map[0].length >= 100 && map[0][0].length >= 10 && wayans.length >= 100 && wayans[0].length >= 100
	 * && findway.length >= 100 && findway[0].length >= 100 
	 * 
	 * 
	 */
	
	public pubwork(int nm,taxis ts)
	 /*
	@REQUIRES:all class not null
	@MODIFIES:this
    @EFFECTS:
    	nummax = nm;
    	txs = ts;
   */
	{
		nummax = nm;
		txs = ts;
	}
	
	 static int x[] = new int [10000];
	 static int y[] = new int [10000];
	 public static boolean map[][][] = new boolean [100][100][10];
	 public static int wayans1[][] = new int [100][100];
	 public static int wayans2[][] = new int [100][100];
	 public static boolean findway[][]  = new boolean [100][100];
	 
	 public boolean repOK()
		/*
		 @EFFECTS:(\all data legal) \result == true;
		 		  (\exists data illegal) \result == false;
		 
		 */
		{
		  try{
			if(x == null)return false;
			if(y == null)return false;
			if(x.length < 10000)return false;
			if(y.length < 10000)return false;
			if(map == null)return false;
			if(wayans1 == null)return false;
			if(findway == null)return false;
			if(txs == null)return false;
			if(nummax < 0)return false;
			
			if(map.length < 100)return false;
			if(map[0].length < 100)return false;
			if(map[0][0].length < 10)return false;
			
			if(wayans1.length < 100)return false;
			if(wayans1[0].length < 100)return false;
			
			if(findway.length < 100)return false;
			if(findway[0].length < 100)return false;
		  }catch (Exception e){return false;}
			return true;
		}
		
	 
	 
	 public void find(int xin,int yin)
	 /*
	    @REQURES: 0 <= xin,yin < 80;
	    @MODIFIES:this
	    @EFFECTS:(\all x,y ;\exists a way from xin,yin to x,y)==>(findway[x][y] = true;wayans[x][y] == the length of the shortest way)
	             (!\exists a way from xin,yin to x,y) ==>(findway[x][y] == false;wayans[x][y] == -1)
	   */
	    {
	    	for(int i = 0;i <= 80;i ++)
	    		for(int j = 0;j <= 80;j ++)
	    		{	findway[i][j] = false;
	    		    wayans1[i][j] = -1;
	    		    wayans2[i][j] = -1;
	    		}
	    	x[1] = xin;
	    	y[1] = yin;
	    	findway[xin][yin] = true;
	    	wayans1[xin][yin] = 0;
	    	wayans2[xin][yin] = 0;
	    	int head = 1,tail = 0;
	    	while(head != tail)
	    	{
	    		tail ++;
	    		int xx = x[tail];
	    		int yy = y[tail];
	    		//System.out.println(xx + " "+ yy);
	    		if(xx != 0)
	    		if(map[xx][yy][0] && !findway[xx-1][yy])
	    		 {
	    			x[++head] = xx-1;
	    			y[head ]  = yy;
	    			findway[xx-1][yy] = true;
	    			wayans1[xx-1][yy] = wayans1[xx][yy] + 1;
	    			
	    		 }
	    		if(map[xx][yy][1] && !findway[xx+1][yy])
	    		{
	    			x[++head] = xx+1;
	    			y[head ]  = yy;
	    			findway[xx+1][yy] = true;
	    			wayans1[xx+1][yy] = wayans1[xx][yy] + 1;
	    		
	   		 	}
	    		if(yy != 0)
	    		if(map[xx][yy][2] && !findway[xx][yy-1])
	   		 	{
	    			x[++head] = xx;
	    			y[head ]  = yy-1;
	    			findway[xx][yy-1] = true;
	    			wayans1[xx][yy-1] = wayans1[xx][yy] + 1;
	   		 	}
	    		if(map[xx][yy][3] && !findway[xx][yy+1])
	   		 	{
	    			x[++head] = xx;
	    			y[head ]  = yy+1;
	    			findway[xx][yy+1] = true;
	    			wayans1[xx][yy+1] = wayans1[xx][yy] + 1;
	   		 	}
	    		
	    	}
	    }
	 
	 
	 
	 public synchronized void modifymap(int x,int y,int type)
	/*
    @REQURES: 0 <= x,y < 80,0<type<4;
    @MODIFIES:this
    @EFFECTS:if(after modify, the map connected) ==>
    (
    ini.map[x][y][type] == !ini.map[x][y][type];
	(type == 0)==>(ini.map[x-1][y][1] = !ini.map[x-1][y][1];p2.x = x-1;p2.y = y;)
	(type == 1)==>(ini.map[x+1][y][0] = !ini.map[x+1][y][0];)
	(type == 2)==>(ini.map[x][y-1][3] = !ini.map[x][y-1][3];)
	(type == 3)==>(ini.map[x][y+1][2] = !ini.map[x][y+1][2];)

    )
    @THREAD EFFECTS:\locked();
   */
	{
		while (full == false)
		  {
			try{  wait();}
			catch (InterruptedException e){}
		  } full = false;
		  
		  int i,j,k;
		  for(i = 0;i < 80;i ++)
			  for(j = 0;j < 80; j++)
			    for(k = 0;k < 4;k ++)
			    	map[i][j][k] = ini.map[i][j][k];
		  boolean tag = true;
		  if(x == 0 && type == 0)tag = false;
		  if(x == 79 && type == 1)tag = false;
		  if(y == 0 && type == 2)tag = false;
		  if(y == 79 && type == 3)tag = false;
		  if(tag)
		  {
			map[x][y][type] = !map[x][y][type];
			if(type == 0)map[x-1][y][1] = !map[x-1][y][1];
			if(type == 1)map[x+1][y][0] = !map[x+1][y][0];
			if(type == 2)map[x][y-1][3] = !map[x][y-1][3];
			if(type == 3)map[x][y+1][2] = !map[x][y+1][2];
			find(0,0);
			for(i = 0;i < 80;i ++)
				for(j = 0;j < 80;j ++)
				{
					if(wayans1[i][j] == -1)tag = false;
				}
		  }
		  if(tag == false)System.out.println("illegal modify!");
		  else
		  {
			  int xxx = x,yyy = y;
			  if(type == 0)xxx --;
			  else if(type == 1)xxx ++;
			  else if(type == 2)yyy --;
			  else if(type == 4)yyy ++;
			  while(true)
			  {
				  boolean tag1 = false;
				  for(i = 0;i < 80;i ++)
				  {
					  if(txs.taxx[i].x == xxx &&txs.taxx[i].y == yyy)tag = true;
					  if(txs.taxx[i].x == x &&txs.taxx[i].y == y)tag = true;
				  }
				  if(!tag1)break;
			  }
			  Point p1 = new Point(),p2 = new Point();
			  ini.map[x][y][type] = !ini.map[x][y][type];
				if(type == 0){ini.map[x-1][y][1] = !ini.map[x-1][y][1];p2.x = x-1;p2.y = y;if(!ini.map[x][y][type])ini.flow[p2.x][p2.y][1] = 0;}
				if(type == 1){ini.map[x+1][y][0] = !ini.map[x+1][y][0];p2.x = x+1;p2.y = y;if(!ini.map[x][y][type])ini.flow[p2.x][p2.y][0] = 0;}
				if(type == 2){ini.map[x][y-1][3] = !ini.map[x][y-1][3];p2.x = x;p2.y = y-1;if(!ini.map[x][y][type])ini.flow[p2.x][p2.y][3] = 0;}
				if(type == 3){ini.map[x][y+1][2] = !ini.map[x][y+1][2];p2.x = x;p2.y = y+1;if(!ini.map[x][y][type])ini.flow[p2.x][p2.y][2] = 0;}
				if(!ini.map[x][y][type])ini.flow[x][y][type] = 0;
				
				p1.x =x;p1.y = y;
				gui.SetRoadStatus(p1,p2,ini.map[x][y][type]?1:0);
				System.out.println("successfully modify!" + x + " " + y + " " + type);
		  }
		  full = true;
		  notifyAll();
	}
	 public synchronized void addflow(int x,int y,int pos,int flow)
	/*
    @REQURES: 0 <= x,y < 80,0<pos<4;
    @MODIFIES:this
    @EFFECTS: ini.flow[x][y][pos] += flow;
		     (pos == 0 && x > 0) ==>
		      (ini.flow[x-1][y][1] += flow;)
		     (pos == 1) ==>
		      (ini.flow[x+1][y][0] += flow;)
		     (pos == 2 && y > 0) ==>
		      (ini.flow[x][y-1][3] += flow;)
		     (pos == 3) ==>
		      ini.flow[x][y+1][2] += flow;)
	@THREAD EFFECTS:\locked();
   */
	{
		while (mapsig == false)
		  {
			try{  wait();}
			catch (InterruptedException e){}
		  } mapsig = false;
		  if(x >= 0 && x < 80 && y >= 0 && y < 80 && pos >= 0 && pos < 4 && ini.map[x][y][pos])
		  {
		      ini.flow[x][y][pos] += flow;
		      if(pos == 0 && x > 0)
		      {ini.flow[x-1][y][1] += flow;}
		      if(pos == 1)
		      {ini.flow[x+1][y][0] += flow;}
		      if(pos == 2 && y > 0)
		      {ini.flow[x][y-1][3] += flow;}
		      if(pos == 3)
		      {ini.flow[x][y+1][2] += flow;}
		  }
		  mapsig = true;
		  notifyAll();
		
	}
	 public synchronized boolean alloc(queue1 q1)
	/*
    @MODIFIES:this
    @EFFECTS:
          (\exists i;q1 contains txs.taxx[i] ; \all j!=i; q1 contains txs.taxx[j] ; ((wayans[txs.taxx[j].x][txs.taxx[j].y] > wayans[txs.taxx[i].x][txs.taxx[i].y]&&(\exists j;txs.taxx[i].credit == txs.taxx[j].credit))||(txs.taxx[i].credit > txs.taxx[j].credit)))
   	@THREAD EFFECTS:\locked();
   */
	{
		while (full == false)
		  {
			try{  wait();}
			catch (InterruptedException e){}
		  } full = false;
		
		int i,j,k;
		for(i = 0;i < 80;i ++)
			  for(j = 0;j < 80; j++)
			    for(k = 0;k < 4;k ++)
			    	map[i][j][k] = ini.map[i][j][k];
		find(q1.sx,q1.sy);
		int chosen = -1;
		System.out.println("Req from("+q1.sx+","+q1.sy+") to ("+q1.tx+","+q1.ty+")");
		for(i = 0;i < q1.sum; i ++)
		{
			if(txs.taxx[q1.taxinum[i]].free)
			{
				System.out.println("taxi number:"+ q1.taxinum[i] +" credit:"+txs.taxx[q1.taxinum[i]].credit + " pos:("+txs.taxx[q1.taxinum[i]].x+","+txs.taxx[q1.taxinum[i]].y+")");
				for(int ii = 0;ii < 80;ii ++)
					for(int jj = 0;jj < 80;jj ++)
						for(int kk = 0;kk < 4;kk ++)
					{
							map[ii][jj][kk] = txs.taxx[q1.taxinum[i]].type1?ini.map[ii][jj][kk]:txs.taxx[q1.taxinum[i]].map[ii][jj][kk];
					}
				System.out.println(wayans1[txs.taxx[q1.taxinum[i]].x][txs.taxx[q1.taxinum[i]].x]);
				find(q1.sx,q1.sy);
				if(chosen != -1)
				{
					if(txs.taxx[chosen].credit < txs.taxx[q1.taxinum[i]].credit)
					 {
						chosen = q1.taxinum[i];
						wayans2[txs.taxx[q1.taxinum[i]].x][txs.taxx[q1.taxinum[i]].y] = wayans1[txs.taxx[q1.taxinum[i]].x][txs.taxx[q1.taxinum[i]].x];
					 }
					else if((txs.taxx[chosen].credit == txs.taxx[q1.taxinum[i]].credit))  ////////need modify
					{
						
						
						if(wayans2[txs.taxx[chosen].x][txs.taxx[chosen].y] > wayans1[txs.taxx[q1.taxinum[i]].x][txs.taxx[q1.taxinum[i]].y])
							{chosen = q1.taxinum[i];
							wayans2[txs.taxx[q1.taxinum[i]].x][txs.taxx[q1.taxinum[i]].y] = wayans1[txs.taxx[q1.taxinum[i]].x][txs.taxx[q1.taxinum[i]].x];
							}
					}
				}
				else
				{chosen = q1.taxinum[i];
				wayans2[txs.taxx[q1.taxinum[i]].x][txs.taxx[q1.taxinum[i]].y] = wayans1[txs.taxx[q1.taxinum[i]].x][txs.taxx[q1.taxinum[i]].x];
				}
			}else System.out.println("taxi number:"+ q1.taxinum[i]+" busy");
		}
		if(chosen == -1)
			{full = true;
			return false;
			}
		txs.taxx[chosen].gx = q1.sx;
		txs.taxx[chosen].gy = q1.sy;
		txs.taxx[chosen].tx = q1.tx;
		txs.taxx[chosen].ty = q1.ty;
		
		txs.taxx[chosen].newguest = true;
		
		full = true;
		notifyAll();
		return true;
	}

}
