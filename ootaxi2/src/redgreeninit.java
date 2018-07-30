import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class redgreeninit extends Thread{
	
	
	static String strs[] = new String[100];
	static int sum_of_red = 0;
	static boolean trafficmap[][] = new boolean [100][100];
	static int waittime[][] = new int [100][100];
	static long lasttime[][] = new long [100][100];
	static TaxiGUI gui; 
	init ini;
	/*
	 * Overview : initialize the information of traffic light and modifies the state of the lights. 
	 * 
	 * strs != null  && strs.length >= 100 
	 * && trafficmap != null && waittime != null && lasttime != null  && lasttime.lenth >= 100
	 * && lasttime[0].length >= 100  && waittime.length >= 100 && waittime[0].length >= 100
	 * && trafficmap.length >= 100 && trafficmap[0].length >= 100
	 * 
	 */
	public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
		if(strs == null)return false;
		if(strs.length < 100) return false;
		
		if(waittime == null)return false;
		if(waittime.length < 100)return false;
		if(waittime[0].length < 100)return false;
		
		if(lasttime == null)return false;
		if(lasttime.length < 100)return false;
		if(lasttime[0].length < 100)return false;
		
		if(trafficmap == null)return false;
		if(trafficmap.length < 100)return false;
		if(trafficmap[0].length < 100)return false;
		return true;
	}
	public static boolean readstrs(String filePath) throws IOException 
	 /*
		@REQUIRES:filepath exists;
		@MODIFIES:this
	    @EFFECTS:
	    	(\exists illegal input) \result == false;
           (\all input legal) \result == true;
	   */
	{
		
   	
       InputStream is = new FileInputStream(filePath);
       String line; // ��������ÿ�ж�ȡ������
       BufferedReader reader = new BufferedReader(new InputStreamReader(is));
       int i = 0;
       line = reader.readLine(); // ��ȡ��һ��
       while (line != null) { // ��� line Ϊ��˵��������
       	if(i> 80)return false;
           strs[i] = line.replaceAll("\\s","");
           int jjj;
           for(jjj = 0;jjj < strs[i].length() ;jjj ++)
           {
           	if(strs[i].charAt(jjj) != '0' && strs[i].charAt(jjj) != '1')
           		return false;
           	if(strs[i].charAt(jjj) == '1')sum_of_red ++;
           }
           if(jjj != 80)return false;
           line = reader.readLine(); // ��ȡ��һ��
           i ++;
           //System.out.println(i + ":" + strs[i-1]);
           
       }
       if(sum_of_red < 6400 * 0.3)System.out.println("WARNING! Sum of traffic light is less than 6400 x 30%!");
       if(i != 80)return false;
       reader.close();
       is.close();
       return true;
   }
	public void pre_solve()
	/*
	@MODIFIES:this
    @EFFECTS:
    	(\all i,j; 0 <= i,j < 80; (strs[i].charAt(j) == '1') && (ini.map[i][j][0]?1:0) + (ini.map[i][j][1]?1:0) + (ini.map[i][j][2]?1:0) + (ini.map[i][j][3]?1:0) >= 3)
    ==> (waittime[i][j] == rand() % 50 + 50;trafficmap[i][j] == (rand()%2 == 1))
    	(!(\all i,j; 0 <= i,j < 80; (strs[i].charAt(j) == '1') && (ini.map[i][j][0]?1:0) + (ini.map[i][j][1]?1:0) + (ini.map[i][j][2]?1:0) + (ini.map[i][j][3]?1:0) > 3))
    ==> (waittime[i][j] == -1)
   */
	{
		int i,j,k;
		int  aa;
		java.util.Random r=new java.util.Random();
		for(i = 0;i < 80;i ++)
			for(j = 0;j < 80;j ++)
				if(strs[i].charAt(j) == '1')
				{
			    if((ini.map[i][j][0]?1:0) + (ini.map[i][j][1]?1:0) + (ini.map[i][j][2]?1:0) + (ini.map[i][j][3]?1:0) < 3)
			    {
			    	System.out.println("Warning! Point (" + i + "," + j + ") is not a crossroad!");
			    	waittime[i][j] = -1;
			    }
				else{
					aa = r.nextInt();
					if(aa < 0) aa = 0 - aa;
					aa = aa % 2;
					trafficmap[i][j] = (aa==1);
					
					Point p = new Point();
					p.x = i;
					p.y = j;
					gui.SetLightStatus(p,trafficmap[i][j]?1:2);
					
					aa = r.nextInt();
					if(aa < 0) aa = 0 - aa;
					aa = aa % 300;
					waittime[i][j] = aa + 200; 
				}
					//waittime[i][j] = 20000;
				}
				else 
				{
					waittime[i][j] = -1;
				}
	}
	public void solve(int i,int j)
	 /*
	@REQUIRES: 0 <= i,j <= 79,waittime[i][j] != -1;
	@MODIFIES: this
    @EFFECTS:
    	(nowtime >= \old(lasttime[i][j]) + waittime[i][j]) ==> (\new(lasttime[i][j]) == \old(lasttime[i][j]) + waittime[i][j];
    															\new(trafficmap[i][j]) == !\old(trafficmap[i][j]))
   */
	{
		Point p = new Point();
		p.x = i;
		p.y = j;
		long nowtime = System.currentTimeMillis();
		
		if(nowtime >= lasttime[i][j] + waittime[i][j])
		{
			lasttime[i][j] = lasttime[i][j] + waittime[i][j];
			trafficmap[i][j] = !trafficmap[i][j];
			gui.SetLightStatus(p,trafficmap[i][j]?1:2);
		}
	}
	public void run()
	 /*
		@MODIFIES: this
	    @EFFECTS:
	    	(nowtime >= \old(lasttime[i][j]) + waittime[i][j] && waittime[i][j] != -1) ==> (\new(lasttime[i][j]) == \old(lasttime[i][j]) + waittime[i][j];
	    															\new(trafficmap[i][j]) == !\old(trafficmap[i][j]))
	    															call functions
	   */
	{
		pre_solve();
		long lt = System.currentTimeMillis();
		for(int i = 0;i < 80;i ++)
			for(int j = 0;j < 80;j ++)
			{
				lasttime[i][j] = lt;
			}
		while(true)
		{
			
			for(int i = 0;i < 80;i ++)
				for(int j = 0;j < 80;j ++)
			{
					if(waittime[i][j] != -1)
						solve(i,j);
			}
			try{
				  sleep(5);
				}catch(Exception e){};
		}
	}

}
