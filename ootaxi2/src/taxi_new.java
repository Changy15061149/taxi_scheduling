import java.awt.Point;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.ListIterator;
public class taxi_new extends taxi {
	
	public boolean type1;
	public boolean map_ini[][][] = new boolean [100][100][10];
	
	int sum_of_information = 0;
	ArrayList <information> info = new ArrayList<information>();
	int nowpo = -1;
	/*
	 * Overview : imitate the behavior of the taxi. 
	 * 
	 * wayqy != null && wayqx != null && 0 <= lasttype <= 4 && 0 <= lltype <= 4 && 0 <= x <80 && 0 <= y <80 
	 * && 0 <= lastx <80 && 0 <= lasty <80 && 0 <= llx <80 && 0 <= lly <80  
	 * && nummax == 80 && wayqx.length >= 10000 && wayqy.length >= 10000 
	 * && map != null && wayans != null && way1 != null && way != null && inqueue != null && flowans != null && father != null
	 * && map.lenth >= 100
	 * && map[0].length >= 100 && map[0][0].length >= 10 && wayans.length >= 100 && wayans[0].length >= 100
	 * && way1.length >= 100 && way1[0].length >= 100 && way.length >= 100 && way[0].length >= 100
	 * && inqueue.length >= 100 && inqueue[0].length >= 100 && && flowans.length >= 100 && flowans[0].length >= 100 && flow[0][0].length >= 100 &&
	 * for all 0 <= i < 80 ,map[0][i][0] == false && map[i][0][2]==false && map[79][i][1] && map[i][79][3]
	 * && father.length >= 100 && father[0].length >= 100 && father[0][0].length >= 10 && map_ini.length < 100 && map_ini[0].length < 100 &&
	 * map_ini[0][0].length < 10
	 */
	public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
		if(!super.repOK())return false;
		if(map_ini.length < 100)return false;
		if(map_ini[0].length < 100)return false;
		if(map_ini[0][0].length < 10)return false;
		return true;
	}
	public taxi_new(int xy,int yy,TaxiGUI gg,txtoutput tt1,init ini1,boolean type11)
	/*
    @MODIFIES:this
    @EFFECTS:
        x == xy;
		y == yy;
		gui == gg;
		tt == tt1;
		ini == ini1;
		type1 = type11;
   */
	{
		super(xy,yy,gg,tt1,ini1);
		type1 = type11;
	}
	
	public information next()
	/*
    @MODIFIES:this
    @EFFECTS:
        (hasnext() == true) ==> (\new(nowpo) == \old(nowpo) + 1 && \result == info.get(nowpo))
        (hasnext() == false) ==> (\result == null)
   */
	{
		information ret = null;
		if(hasnext())
		{
			nowpo ++;
			if(nowpo < 0 || nowpo > sum_of_information -1)
				return null;
			/*System.out.println(info.get(nowpo).rr.sx + " " + info.get(nowpo).rr.sy + " " + info.get(nowpo).rr.tx + " "+ info.get(nowpo).rr.ty);
			System.out.println((info.get(nowpo).rr.lastime - basetime)/1000);
			int i = 0;
			while(i < info.get(nowpo).getax.size())
			{
				System.out.println(info.get(nowpo).getax.get(i) + " " + info.get(nowpo).getay.get(i));
				
				i ++;
			}
			i = 0;
			while(i < info.get(nowpo).surax.size())
			{
				System.out.println(info.get(nowpo).surax.get(i) + " " + info.get(nowpo).suray.get(i));
				i ++;
			}
			i = 0;*/
			return info.get(nowpo);
		}
		return ret;
	}
	
	public information previous()
	/*
    @MODIFIES:this
    @EFFECTS:
        (hasprevious() == true) ==> (\new(nowpo) == \old(nowpo) - 1 && \result == info.get(nowpo))
        (hasprevious() == false) ==> (\result == null)
   */
	{
		information ret = null;
		if(hasprevious())
		{
			nowpo --;
			if(nowpo < 0 || nowpo > sum_of_information -1)
				return null;
			return info.get(nowpo);
		}
		return ret;
	}
	
	public boolean hasnext()
	/*
    @EFFECTS:
        (nowpo >= sum_of_information -1) ==> (\result == false)
        (nowpo < sum_of_information -1) ==> (\result == true)
   */
	{
		if(nowpo >= sum_of_information -1)return false;
		return true;
	}
	
	public boolean hasprevious()
	/*
    @EFFECTS:
        (nowpo <= 0) ==> (\result == false)
        (nowpo > 0) ==> (\result == true)
   */
	{
		if(nowpo > 0)return true;
		return false;
	}
	public void update()
	/*
	@MODIFIES:this
    @EFFECTS:
        (\all i,j,k; 0 <= i,j < 80; 0 < k < 4) ==>(\new(map[i][j][k]) == \old(map[i][j][k])|ini.map[i][j][k] 
        && \new(map_ini[i][j][k]) == \old(map_ini[i][j][k])|ini.map[i][j][k] );
   */
	{
		for(int i = 0;i < 80;i ++)
			for(int j = 0;j < 80;j ++)
				for(int k = 0;k < 4 ; k ++)
				{
					map[i][j][k] = map[i][j][k] | ini.map[i][j][k];
					map_ini[i][j][k] = map_ini[i][j][k] | ini.map[i][j][k];
				}
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
	    		if((type1?(ini.map[xx][yy][0]):(ini.map[xx][yy][0] || map_ini[xx][yy][0]))&& wayans[xx-1][yy] == -1)
	    		 {
	    			wayqx[++head] = xx-1;
	    			wayqy[head ]  = yy;
	    			wayans[xx-1][yy] = wayans[xx][yy] + 1;
	    		 }
	    		if((type1?(ini.map[xx][yy][1]):(ini.map[xx][yy][1] || map_ini[xx][yy][1])) && wayans[xx+1][yy] == -1)
	    		{
	    			wayqx[++head] = xx+1;
	    			wayqy[head ]  = yy;
	    			wayans[xx+1][yy] = wayans[xx][yy] + 1;
	   		 	}
	    		if(yy != 0)
	    		if((type1?(ini.map[xx][yy][2]):(ini.map[xx][yy][2] || map_ini[xx][yy][2])) && wayans[xx][yy-1] == -1)
	   		 	{
	    			wayqx[++head] = xx;
	    			wayqy[head ]  = yy-1;
	    			wayans[xx][yy-1] = wayans[xx][yy] + 1;
	   		 	}
	    		if((type1?(ini.map[xx][yy][3]):(ini.map[xx][yy][3] || map_ini[xx][yy][3]))&& wayans[xx][yy+1] == -1)
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
	    		if((ini.map[xx][yy][0]|| map_ini[xx][yy][0]) && wayans[xx-1][yy] == wayans[xx][yy]+1)
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
	    		if((ini.map[xx][yy][1]|| map_ini[xx][yy][1]) && wayans[xx+1][yy] == wayans[xx][yy]+1)
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
	    		if((ini.map[xx][yy][2]|| map_ini[xx][yy][2]) && wayans[xx][yy-1] == wayans[xx][yy]+1)
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
	    		if((ini.map[xx][yy][3]|| map_ini[xx][yy][3]) && wayans[xx][yy+1] == wayans[xx][yy]+1)
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
	public void new_service()
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
			update();
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
			info.get(sum_of_information - 1).surax.add(x);
			info.get(sum_of_information - 1).suray.add(y);
				int wantedx = father[x][y][0],wantedy = father[x][y][1];
				int floww = 200;
				helpmodify = false;
				/*if(wayans[x+1][y] == wayans[x][y]-1 && ini.flow[x][y][1] < floww && ini.map[x][y][1]){wantedx = x+1;wantedy = y;floww = ini.flow[x][y][1];lasttype = 1;}
				//if(x > 0 && wayans[x-1][y] == wayans[x][y]-1 && ini.flow[x][y][0] < floww && ini.map[x][y][0]){wantedx = x-1;wantedy = y;floww = ini.flow[x][y][0];lasttype = 0;}
				if(wayans[x][y+1] == wayans[x][y]-1 && ini.flow[x][y][3] < floww && ini.map[x][y][3]){wantedx = x;wantedy = y+1;floww = ini.flow[x][y][3];lasttype = 3;}
				if(y > 0 && wayans[x][y-1] == wayans[x][y]-1 && ini.flow[x][y][2] < floww && ini.map[x][y][2]){wantedx = x;wantedy = y-1;floww = ini.flow[x][y][2];lasttype = 2;}
				*/
				if(wantedx == x-1 && wantedy == y){lasttype = 0;}
				else if(wantedx == x+1 && wantedy == y){lasttype = 1;}
				else if(wantedx == x && wantedy == y-1){lasttype = 2;}
				else if(wantedx == x && wantedy == y+1){lasttype = 3;}
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
	public void new_getguest()
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
			update();
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
				info.get(sum_of_information - 1).getax.add(x);
				info.get(sum_of_information - 1).getay.add(y);
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
				if(wantedx == x-1 && wantedy == y){lasttype = 0;}
				else if(wantedx == x+1 && wantedy == y){lasttype = 1;}
				else if(wantedx == x && wantedy == y-1){lasttype = 2;}
				else if(wantedx == x && wantedy == y+1){lasttype = 3;}
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
	public void new_solve()
	/*
    @MODIFIES:this
    @EFFECTS:
    	(x = wantedx);
		(y = wantedy);
		(nowtime - old(lasttime) >= 200) ==> (lasttime += 200)
   */
	{
		while(true)
		{	update();
			state = 0;
			if(newguest == true)
			{
				free = false;
				newguest = false;
				state = 1;
				information inff = new information();
				
				req rrr = new req(System.currentTimeMillis() - 3000,basetime);
				rrr.sx = gx;
				rrr.sy = gy;
				rrr.tx = tx;
				rrr.ty = ty;
				sum_of_information ++;
				inff.rr = rrr;
				info.add(inff);
				
				new_getguest();
				gui.SetTaxiStatus(self, new Point(x,y), 0);
				state = 3;
				twait();
				state = 2;
				new_service();
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
			//next();
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
				if((map_ini[x][y][0] || ini.map[x][y][0])&& ini.flow[x][y][0] < floww)floww = ini.flow[x][y][0];
				if((map_ini[x][y][1] || ini.map[x][y][1])&& ini.flow[x][y][1] < floww)floww = ini.flow[x][y][1];
				if((map_ini[x][y][2] || ini.map[x][y][2])&& ini.flow[x][y][2] < floww)floww = ini.flow[x][y][2];
				if((map_ini[x][y][3] || ini.map[x][y][3])&& ini.flow[x][y][3] < floww)floww = ini.flow[x][y][3];
				
				if(self == -1)
				{
					System.out.println("x=" + x + ",y=" + y);
					System.out.println("laxtx=" + lastx + ",lasty=" + lasty  + " lastinformation" + ini.flow[lastx][lasty][lasttype]);
					if(map_ini[x][y][0])System.out.println("UP:"+ini.flow[x][y][0]);
					if(map_ini[x][y][1])System.out.println("DOWN:"+ini.flow[x][y][1]);
					if(map_ini[x][y][2])System.out.println("LEFT:"+ini.flow[x][y][2]);
					if(map_ini[x][y][3])System.out.println("RIGHT:"+ini.flow[x][y][3]);
				}
				
				
				int  aa;
				//pre_work();
				while(true)
				{
				aa = r.nextInt();
				if(aa < 0) aa = 0 - aa;
				aa = aa % 4;
				if(ini.flow[x][y][aa] == floww && map_ini[x][y][aa])break;
				}
				if((map_ini[x][y][0] || ini.map[x][y][0]) && aa == 0 && ini.flow[x][y][0] == floww) {x = x-1;lasttype = 0;}
				else if((map_ini[x][y][1] || ini.map[x][y][1]) && aa == 1 && ini.flow[x][y][1] == floww){x = x+1;lasttype = 1;}
				else if((map_ini[x][y][2] || ini.map[x][y][2]) && aa == 2 && ini.flow[x][y][2] == floww){y = y-1;lasttype = 2;}
				else if((map_ini[x][y][3] || ini.map[x][y][3]) && aa == 3 && ini.flow[x][y][3] == floww){y = y+1;lasttype = 3;}
				else if((map_ini[x][y][0] || ini.map[x][y][0]) && ini.flow[x][y][0] == floww){x = x-1;lasttype = 0;}
				else if((map_ini[x][y][1] || ini.map[x][y][1]) && ini.flow[x][y][1] == floww){x = x+1;lasttype = 1;}
				else if((map_ini[x][y][2] || ini.map[x][y][2]) && ini.flow[x][y][2] == floww){y = y-1;lasttype = 2;}
				else if((map_ini[x][y][3] || ini.map[x][y][3]) && ini.flow[x][y][3] == floww){y = y+1;lasttype = 3;}
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
		call function;
   */
	{
		lasttime = System.currentTimeMillis();
		basetime = lasttime;
		lastguest = lasttime;
		//System.out.println("sad!");
		if(type1)
		{
			gui.SetTaxiType(self,0);
			solve();
		}
		else
		{
			gui.SetTaxiType(self,1);
			for(int j = 0;j < 80;j ++)
				for(int k = 0;k < 80;k ++)
				{
					map_ini[j][k][0] = map[j][k][0];
					map_ini[j][k][1] = map[j][k][1];
					map_ini[j][k][2] = map[j][k][2];
					map_ini[j][k][3] = map[j][k][3];
				}
			new_solve();
		}
	}

}
