import java.awt.Point;

//import test.TaxiGUI;


public class taxi  extends Thread{
	long lasttime;
	long lastguest;
	long basetime;
	int x,y;
	public boolean type1;
	int summ = 0;
	int lastx = 80,lasty  = 80,lasttype = 4,lltype = 4,llx = 80,lly = 80;
	int tx,ty;
	int gx,gy;
	int credit = 0;
	int state;
	boolean free = true;
	boolean newguest = false;
	
	

	
	
	int self;
	boolean map[][][] = new boolean[100][100][10];
	boolean way1[][] = new boolean [100][100];
	int way[][] = new int[100][100];
	TaxiGUI gui;
	txtoutput tt;
	init ini;
	int wayans[][] = new int [100][100];
	int flowans[][] = new int[100][100];
	int father[][][] = new int [100][100][10];
	int wayqx [] = new int[100000];
	int wayqy [] = new int[100000];
	boolean inqueue[][] = new boolean [100][100];
	pubwork pw;
	boolean helpmodify = false;
	redgreeninit rg;
	/*
	 * Overview : imitate the behavior of the taxi. 
	 * 
	 * wayqy != null && wayqx != null && 0 <= lasttype <= 4 && 0 <= lltype <= 4 && 0 <= x <80 && 0 <= y <80 
	 * && 0 <= lastx <80 && 0 <= lasty <80 && 0 <= llx <80 && 0 <= lly <80  
	 * && nummax == 80 && wayqx.length >= 10000 && wayqy.length >= 10000 
	 * 
	 * && map != null && wayans != null && way1 != null && way != null && inqueue != null && flowans != null && father != null
	 * && map.lenth >= 100
	 * && map[0].length >= 100 && map[0][0].length >= 10 && wayans.length >= 100 && wayans[0].length >= 100
	 * && way1.length >= 100 && way1[0].length >= 100 && way.length >= 100 && way[0].length >= 100
	 * && inqueue.length >= 100 && inqueue[0].length >= 100 && && flowans.length >= 100 && flowans[0].length >= 100 && flow[0][0].length >= 100 &&
	 * for all 0 <= i < 80 ,map[0][i][0] == false && map[i][0][2]==false && map[79][i][1] && map[i][79][3]
	 * && father.length >= 100 && father[0].length >= 100 && father[0][0].length >= 10 
	 */
	public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
	  try{
		if(wayqx == null)return false;
		if(wayqx.length < 10000)return false;
		if(wayqy == null)return false;
		if(wayqy.length < 10000)return false;
		if(lasttype < 0 || lasttype > 4)return false;
		if(lltype < 0 || lltype > 4)return false;
		if(x >= 80 || x < 0)return false;
		if(y >= 80 || y < 0)return false;
		if(lastx >= 80 || lastx < 0)return false;
		if(lasty >= 80 || lasty < 0)return false;
		if(llx >= 80 || llx < 0)return false;
		if(lly >= 80 || lly < 0)return false;
		if(map == null)return false;
		if(wayans == null)return false;
		if(way1 == null)return false;
		if(way == null)return false;
		if(flowans == null)return false;
		if(father == null) return false;
		if(inqueue == null)return false;
		
		if(map.length < 100)return false;
		if(map[0].length < 100)return false;
		if(map[0][0].length < 10)return false;
		
		if(wayans.length < 100)return false;
		if(wayans[0].length < 100)return false;
		
		if(way1.length < 100)return false;
		if(way1[0].length < 100)return false;
		
		if(way.length < 100)return false;
		if(way[0].length < 100)return false;
		
		if(flowans.length < 100)return false;
		if(flowans[0].length < 100)return false;
		
		if(father.length < 100)return false;
		if(father[0].length < 100)return false;
		if(father[0][0].length < 10)return false;
		
		if(inqueue.length < 100)return false;
		if(inqueue[0].length < 100)return false;
		
		for(int i = 0;i < 80;i ++)
		{
			if(map[0][i][0])return false;
			if(map[i][0][2])return false;
			if(map[79][i][1])return false;
			if(map[i][79][3])return false;
		}
	  }catch (Exception e) {return false;}
		return true;
	}
	public taxi(int xy,int yy,TaxiGUI gg,txtoutput tt1,init ini1)
	/*
    @REQUIRES:all class not null
    @MODIFIES:this
    @EFFECTS:
        x == max(xy,-xy) % 80;
		y == max(yy,-yy) & 80;
		gui == gg;
		tt == tt1;
		ini == ini1;
   */
	{
		if(xy < 0)xy = -xy;
		if(yy < 0)yy = -yy;
		x = xy % 80;
		y = yy % 80;
		gui = gg;
		tt = tt1;
		ini = ini1;
	}
	public void pre_work()
	/*
    @MODIFIES:this
    @EFFECTS:
    	llx == lastx;
		lly == lasty;
		lltype == lasttype;
		ini.map[llx][lly][lltype] --;
		ini.map[llx + (lltype == 1?+1:lltype ==0?-1:0)][lly + (lltype == 3?+1:lltype ==2?-1:0)][lltype > 1?5 - lltype:1 - lltype ] --;
   */
	{
		pw.addflow(llx, lly, lltype, -1);
		llx = lastx;
		lly = lasty;
		lltype = lasttype;
	}
	public void traffic_light()
	/*
	 @MODIFIES: this
	 @EFFECTS: 
	 		(lltype == 0 && lasttype == 0) ==> (lasttime == currenttime);
		    (lltype == 0 && lasttype == 2) ==> (lasttime == currenttime);
		    (lltype == 1 && lasttype == 1) ==> (lasttime == currenttime);
		    (lltype == 1 && lasttype == 3) ==> (lasttime == currenttime);
		    (lltype == 2 && lasttype == 1) ==> (lasttime == currenttime);
		    (lltype == 2 && lasttype == 2) ==> (lasttime == currenttime);
		    (lltype == 3 && lasttype == 0) ==> (lasttime == currenttime);
		    (lltype == 3 && lasttype == 3) ==> (lasttime == currenttime);
	 */
	{
		boolean tyty = false;
		if(lltype == 0 && lasttype == 0)tyty = false;
		else if(lltype == 0 && lasttype == 1)return;
		else if(lltype == 0 && lasttype == 2)tyty = true;
		else if(lltype == 0 && lasttype == 3)return;
		
		
		else if(lltype == 1 && lasttype == 0)return;
		else if(lltype == 1 && lasttype == 1)tyty = false;
		else if(lltype == 1 && lasttype == 2)return;
		else if(lltype == 1 && lasttype == 3)tyty = true;
		
		else if(lltype == 2 && lasttype == 0)return;
		else if(lltype == 2 && lasttype == 1)tyty = false;
		else if(lltype == 2 && lasttype == 2)tyty = true;
		else if(lltype == 2 && lasttype == 3)return;
		
		else if(lltype == 3 && lasttype == 0)tyty = false;
		else if(lltype == 3 && lasttype == 1)return;
		else if(lltype == 3 && lasttype == 2)return;
		else if(lltype == 3 && lasttype == 3)tyty = true;
		else return;
		if(free == false){System.out.println(rg.trafficmap[lastx][lasty]?1:2);}	
		if(rg.trafficmap[lastx][lasty] == tyty || rg.waittime[lastx][lasty] == -1)return;
		if(free == false){System.out.println("wait at (" + lastx + "," + lasty + ")");}
		gui.SetTaxiStatus(self, new Point(lastx,lasty), 0);
		if(free == false)System.out.println("success!");
		while(rg.trafficmap[lastx][lasty] != tyty)
		{try{
			  sleep(2);
			}catch(Exception e){};}
		lasttime = System.currentTimeMillis();
	}
	public void bfs(int fromx,int fromy)
	/*
    @MODIFIES:this
    @EFFECTS:(\all x,y ;\exists a way from fromx,fromy to x,y)
    			==>(flowans[x][y] = the min sum flow from fromx,fromy to x,y;wayans[x][y] == the length of the shortest way;
    			father[x][y][0] = x of the point from which we come from ;father[x][y][1] = y of the point from which we come from )
             (!\exists a way from xin,yin to x,y) ==>(flowans[x][y] == 200;wayans[x][y] == -1)
             
   */
	{
		for(int i = 0;i <= 90;i ++)
			for(int j = 0;j <= 90;j ++)
		{
				wayans[i][j] = -1;
				inqueue[i][j] = false;
				flowans[i][j] = 200;
		}
		int head = 1,tail = 0;
		wayans[fromx][fromy] = 0;
		wayqx[1] = fromx;
		wayqy[1] = fromy;
		while(head != tail)
		{
			tail ++;
			int xx = wayqx[tail];
			int yy = wayqy[tail];
			    if(xx != 0)
	    		if(ini.map[xx][yy][0] && wayans[xx-1][yy] == -1)
	    		 {
	    			wayqx[++head] = xx-1;
	    			wayqy[head ]  = yy;
	    			wayans[xx-1][yy] = wayans[xx][yy] + 1;
	    		 }
	    		if(ini.map[xx][yy][1] && wayans[xx+1][yy] == -1)
	    		{
	    			wayqx[++head] = xx+1;
	    			wayqy[head ]  = yy;
	    			wayans[xx+1][yy] = wayans[xx][yy] + 1;
	   		 	}
	    		if(yy != 0)
	    		if(ini.map[xx][yy][2] && wayans[xx][yy-1] == -1)
	   		 	{
	    			wayqx[++head] = xx;
	    			wayqy[head ]  = yy-1;
	    			wayans[xx][yy-1] = wayans[xx][yy] + 1;
	   		 	}
	    		if(ini.map[xx][yy][3] && wayans[xx][yy+1] == -1)
	   		 	{
	    			wayqx[++head] = xx;
	    			wayqy[head ]  = yy+1;
	    			wayans[xx][yy+1] = wayans[xx][yy] + 1;
	   		 	}
		}head = 1; tail = 0;
		inqueue[fromx][fromy] = true;
		flowans[fromx][fromy] = 0;
		while(head != tail)
		{
			tail ++;
			int xx = wayqx[tail];
			int yy = wayqy[tail];
			inqueue[xx][yy] = false;
			if(xx != 0)
	    		if(ini.map[xx][yy][0] && wayans[xx-1][yy] == wayans[xx][yy]+1)
	    		 {
	    			if(flowans[xx][yy] + ini.flow[xx][yy][0] < flowans[xx-1][yy])
	    			{
	    			flowans[xx-1][yy] = flowans[xx][yy] + ini.flow[xx][yy][0];
	    			father[xx-1][yy][0] = xx;
	    			father[xx-1][yy][1] = yy;
	    			if(!inqueue[xx-1][yy])
	    			 {
	    			 inqueue[xx-1][yy] = true;
	    			 wayqx[++head] = xx-1;
	    			 wayqy[head ]  = yy;
	    			 }
	    			}
	    		 }
	    		if(ini.map[xx][yy][1] && wayans[xx+1][yy] == wayans[xx][yy]+1)
	    		{
	    			if(flowans[xx][yy] + ini.flow[xx][yy][1] < flowans[xx+1][yy])
	    			{
	    			flowans[xx+1][yy] = flowans[xx][yy] + ini.flow[xx][yy][1];
	    			father[xx+1][yy][0] = xx;
	    			father[xx+1][yy][1] = yy;
	    			if(!inqueue[xx+1][yy])
	    			 {
	    			 inqueue[xx+1][yy] = true;
	    			 wayqx[++head] = xx+1;
	    			 wayqy[head ]  = yy;
	    			 }
	    			}
	   		 	}
	    		if(yy != 0)
	    		if(ini.map[xx][yy][2] && wayans[xx][yy-1] == wayans[xx][yy]+1)
	   		 	{
	    			if(flowans[xx][yy] + ini.flow[xx][yy][2] < flowans[xx][yy-1])
	    			{
	    			flowans[xx][yy-1] = flowans[xx][yy] + ini.flow[xx][yy][2];
	    			father[xx][yy-1][0] = xx;
	    			father[xx][yy-1][1] = yy;
	    			if(!inqueue[xx][yy-1])
	    			 {
	    			 inqueue[xx][yy-1] = true;
	    			 wayqx[++head] = xx;
	    			 wayqy[head ]  = yy-1;
	    			 }
	    			}
	   		 	}
	    		if(ini.map[xx][yy][3] && wayans[xx][yy+1] == wayans[xx][yy]+1)
	   		 	{
	    			if(flowans[xx][yy] + ini.flow[xx][yy][3] < flowans[xx][yy + 1])
	    			{
	    			flowans[xx][yy + 1] = flowans[xx][yy] + ini.flow[xx][yy][3];
	    			father[xx][yy+1][0] = xx;
	    			father[xx][yy+1][1] = yy;
	    			if(!inqueue[xx][yy +1])
	    			 {
	    			 inqueue[xx][yy +1] = true;
	    			 wayqx[++head] = xx;
	    			 wayqy[head ]  = yy+1;
	    			 }
	    			}
	   		 	}
		}
	}
	public void service()
	/*
    @MODIFIES:this
    @EFFECTS:
    	(\all x,y ;\exists a way from tx,ty to xx,yy)
    			==>(flowans[x][y] = the min sum flow from tx,ty to x,y;wayans[x][y] == the length of the shortest way;
    			father[x][y][0] = x of the point from which we come from ;father[x][y][1] = y of the point from which we come from )
             (!\exists a way from tx,ty to x,y) ==>(flowans[x][y] == 200;wayans[x][y] == -1)
    	x = father[x][y][0];
		y = father[x][y][1];
		(nowtime - old(lasttime) >= 200) ==> (lasttime += 200);
		print taxi information on screen;
   */
	{
		bfs(tx,ty);
		while(x != tx || y != ty)
		{
			
			long nowtime = System.currentTimeMillis();
			if(!helpmodify && nowtime >= lasttime + 196)
			{
				helpmodify = true;
				
				pw.addflow(lastx, lasty, lasttype, 1);
				pre_work();
				lastx = x;
				lasty = y;
			}
			if(nowtime - lasttime >= 200)
			{
				//bfs(tx,ty);
				//pre_work();
				int wantedx = father[x][y][0],wantedy = father[x][y][1];
				int floww = 200;
				helpmodify = false;
				/*if(wayans[x+1][y] == wayans[x][y]-1 && ini.flow[x][y][1] < floww && ini.map[x][y][1]){wantedx = x+1;wantedy = y;floww = ini.flow[x][y][1];lasttype = 1;}
				//if(x > 0 && wayans[x-1][y] == wayans[x][y]-1 && ini.flow[x][y][0] < floww && ini.map[x][y][0]){wantedx = x-1;wantedy = y;floww = ini.flow[x][y][0];lasttype = 0;}
				if(wayans[x][y+1] == wayans[x][y]-1 && ini.flow[x][y][3] < floww && ini.map[x][y][3]){wantedx = x;wantedy = y+1;floww = ini.flow[x][y][3];lasttype = 3;}
				if(y > 0 && wayans[x][y-1] == wayans[x][y]-1 && ini.flow[x][y][2] < floww && ini.map[x][y][2]){wantedx = x;wantedy = y-1;floww = ini.flow[x][y][2];lasttype = 2;}
				*/
				if(wantedx == x-1 && wantedy == y){lasttype = 0;if(!ini.map[x][y][0])bfs(tx,ty);}
				else if(wantedx == x+1 && wantedy == y){lasttype = 1;if(!ini.map[x][y][1])bfs(tx,ty);}
				else if(wantedx == x && wantedy == y-1){lasttype = 2;if(!ini.map[x][y][2])bfs(tx,ty);}
				else if(wantedx == x && wantedy == y+1){lasttype = 3;if(!ini.map[x][y][3])bfs(tx,ty);}
				wantedx = father[x][y][0];wantedy = father[x][y][1];
				x = wantedx;
				y = wantedy;
				traffic_light();lasttime += 200;
				//pw.addflow(x, y, lasttype, 1);
				gui.SetTaxiStatus(self, new Point(x,y), 1);
    			System.out.println("taxi number:" + self + " at position:" + (x)+ " " + (y)+" time:" +(lasttime - basetime)/1000.00 +" now servicing!"+ wayans[x][y]);
    			String ssss = "taxi number:" + self + " at position:" + (x)+ " " + (y)+" time:" +(lasttime - basetime)/1000.00 +" now servicing!"+ " credit = "+credit+"\n";
				tt.outputt(ssss,false);
			}try{
				  sleep(20);
				}catch(Exception e){};
			
		}
	}
	public void getguest()
	/*
    @MODIFIES:this
    @EFFECTS:
    	(\all x,y ;\exists a way from gx,gy to xx,yy)
    			==>(flowans[x][y] = the min sum flow from gx,gy to x,y;wayans[x][y] == the length of the shortest way;
    			father[x][y][0] = x of the point from which we come from ;father[x][y][1] = y of the point from which we come from )
             (!\exists a way from tx,ty to x,y) ==>(flowans[x][y] == 200;wayans[x][y] == -1)
        x = father[x][y][0];
		y = father[x][y][1];
		(nowtime - old(lasttime) >= 200) ==> (lasttime += 200)
		print taxi information on screen
   */
	{
		bfs(gx,gy);
		
		while(x != gx || y != gy)
		{
			long nowtime = System.currentTimeMillis();
			if(!helpmodify && nowtime >= lasttime + 196)
			{
				helpmodify = true;
				System.out.println("ww");
				pw.addflow(lastx, lasty, lasttype, 1);
				//if(self == 1)System.out.println(lastx + " " + lasty + " "+ x + " " + y + " " + ini.flow[x][y][lasttype]);
				pre_work();
				lastx = x;
				lasty = y;
			}
			
			if(nowtime - lasttime >= 200)
			{
				
				//bfs(gx,gy);
				//pre_work();
				int wantedx = father[x][y][0],wantedy = father[x][y][1];
				int floww = 200;
				//lasttime += 200;
				/*if(wayans[x+1][y] == wayans[x][y]-1 && ini.flow[x][y][1] < floww && ini.map[x][y][1]){wantedx = x+1;wantedy = y;floww = ini.flow[x][y][1];lasttype = 1;}
				if(x > 0 && wayans[x-1][y] == wayans[x][y]-1 && ini.flow[x][y][0] < floww && ini.map[x][y][0]){wantedx = x-1;wantedy = y;floww = ini.flow[x][y][0];lasttype = 0;}
				if(wayans[x][y+1] == wayans[x][y]-1 && ini.flow[x][y][3] < floww && ini.map[x][y][3]){wantedx = x;wantedy = y+1;floww = ini.flow[x][y][3];lasttype = 3;}
				if(y > 0 && wayans[x][y-1] == wayans[x][y]-1 && ini.flow[x][y][2] < floww && ini.map[x][y][2]){wantedx = x;wantedy = y-1;floww = ini.flow[x][y][2];lasttype = 2;}
				*/
				if(wantedx == x-1 && wantedy == y){lasttype = 0;if(!ini.map[x][y][0])bfs(gx,gy);}
				else if(wantedx == x+1 && wantedy == y){lasttype = 1;if(!ini.map[x][y][1])bfs(gx,gy);}
				else if(wantedx == x && wantedy == y-1){lasttype = 2;if(!ini.map[x][y][2])bfs(gx,gy);}
				else if(wantedx == x && wantedy == y+1){lasttype = 3;if(!ini.map[x][y][3])bfs(gx,gy);}
				wantedx = father[x][y][0];wantedy = father[x][y][1];
				x = wantedx;
				y = wantedy;
				helpmodify = false;
				//pw.addflow(x,y,lasttype,1);
				//gui.SetTaxiStatus(self, new Point(x,y), 1);
				traffic_light();lasttime += 200;
				gui.SetTaxiStatus(self, new Point(x,y), 3);
				System.out.println(lastx + " " + lasty + " " + llx + " " + lly);
    			System.out.println("taxi number:" + self + " at position:" + (x)+ " " + (y)+" time:" +(lasttime - basetime)/1000.00 + "pos" + wayans[x][y] +" now going to pick up guest");
    			String ssss = "taxi number:" + self + " at position:" + (x)+ " " + (y)+" time:" +(lasttime - basetime)/1000.00 +" now going to pick up guest\n";
				tt.outputt(ssss,false);
			}try{
				  sleep(20);
				}catch(Exception e){};
			
		}
	}
	public void twait()
	/*
    @MODIFIES:this
    @EFFECTS:
		(nowtime - old(lasttime) >= 100) ==>lasttime += 100;
   */
	{
		while(true)
		{
			long nowtime = System.currentTimeMillis();
			if(nowtime >= lasttime + 1000)
			{
				lasttime += 1000;
				break;
			}
			try{
				  sleep(20);
				}catch(Exception e){};
		}
	}
	public void solve()
	/*
    @MODIFIES:this
    @EFFECTS:
    	(x = wantedx);
		(y = wantedy);
		(nowtime - old(lasttime) >= 200) ==> (lasttime += 200)
   */
	{
	//	System.out.println("QQWWQQ");
		while(true)
		{
			state = 0;
			if(newguest == true)
			{
				free = false;
				newguest = false;
				state = 1;
				getguest();
				gui.SetTaxiStatus(self, new Point(x,y), 0);
				state = 3;
				twait();
				state = 2;
				service();
				gui.SetTaxiStatus(self, new Point(x,y), 0);
				state = 3;
				twait();
				state = 0;
				credit += 4;
				free = true;
				lastguest = lasttime;
				summ = 0;
			}
			long nowtime = System.currentTimeMillis();
			java.util.Random r=new java.util.Random();
			if(!helpmodify && nowtime >= lasttime + 196)
			{
				helpmodify = true;
				
				pw.addflow(lastx, lasty, lasttype, 1);
				//if(self == 1)System.out.println(lastx + " " + lasty + " "+ x + " " + y + " " + ini.flow[x][y][lasttype]);
				pre_work();
				lastx = x;
				lasty = y;
			}
			if(nowtime >= lasttime + 200)
			{
				
				helpmodify = false;
				int floww = 200;
				if(ini.map[x][y][0] && ini.flow[x][y][0] < floww)floww = ini.flow[x][y][0];
				if(ini.map[x][y][1] && ini.flow[x][y][1] < floww)floww = ini.flow[x][y][1];
				if(ini.map[x][y][2] && ini.flow[x][y][2] < floww)floww = ini.flow[x][y][2];
				if(ini.map[x][y][3] && ini.flow[x][y][3] < floww)floww = ini.flow[x][y][3];
				
				if(self == -1)
				{
					System.out.println("x=" + x + ",y=" + y);
					System.out.println("laxtx=" + lastx + ",lasty=" + lasty  + " lastinformation" + ini.flow[lastx][lasty][lasttype]);
					if(ini.map[x][y][0])System.out.println("UP:"+ini.flow[x][y][0]);
					if(ini.map[x][y][1])System.out.println("DOWN:"+ini.flow[x][y][1]);
					if(ini.map[x][y][2])System.out.println("LEFT:"+ini.flow[x][y][2]);
					if(ini.map[x][y][3])System.out.println("RIGHT:"+ini.flow[x][y][3]);
				}
				
				
				int  aa;
				//pre_work();
				while(true)
				{
				aa = r.nextInt();
				if(aa < 0) aa = 0 - aa;
				aa = aa % 4;
				if(ini.flow[x][y][aa] == floww && ini.map[x][y][aa])break;
				}
				if(ini.map[x][y][0] && aa == 0 && ini.flow[x][y][0] == floww) {x = x-1;lasttype = 0;}
				else if(ini.map[x][y][1] && aa == 1 && ini.flow[x][y][1] == floww){x = x+1;lasttype = 1;}
				else if(ini.map[x][y][2] && aa == 2 && ini.flow[x][y][2] == floww){y = y-1;lasttype = 2;}
				else if(ini.map[x][y][3] && aa == 3 && ini.flow[x][y][3] == floww){y = y+1;lasttype = 3;}
				else if(ini.map[x][y][0] && ini.flow[x][y][0] == floww){x = x-1;lasttype = 0;}
				else if(ini.map[x][y][1] && ini.flow[x][y][1] == floww){x = x+1;lasttype = 1;}
				else if(ini.map[x][y][2] && ini.flow[x][y][2] == floww){y = y-1;lasttype = 2;}
				else if(ini.map[x][y][3] && ini.flow[x][y][3] == floww){y = y+1;lasttype = 3;}
			//	else System.out.println("?????????happend!");
				traffic_light();
				summ ++;
				lasttime += 200;
				//pw.addflow(x, y, lasttype, 1);
				gui.SetTaxiStatus(self, new Point(x,y), 2);
				String ssss = "taxi number:" + self + " at position:" + (x)+ " " + (y) +" time:" +(lasttime - basetime)/1000.00 +" credit = "+ credit + "  now empty\n";
				tt.outputt(ssss,true);
			}
			if(summ >= 100)
			{
				free = false;
				summ = 0;
				gui.SetTaxiStatus(self, new Point(x,y), 3);
				state = 3;
				twait();
				free = true;
				state = 0;
				lastguest = lasttime;
			}
			try{
			  sleep(5);
			}catch(Exception e){};
		}
	}
	public void run()
	/*
    @MODIFIES:this
    @EFFECTS:lasttime = System.currentTimeMillis();
		basetime = lasttime;
		lastguest = lasttime;
   */
	{
	 try{
		lasttime = System.currentTimeMillis();
		basetime = lasttime;
		lastguest = lasttime;
		solve();
	 }catch (Exception e){};
	}
	
}
