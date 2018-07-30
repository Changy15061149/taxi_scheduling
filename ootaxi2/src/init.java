import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class init {
	public static boolean map[][][] = new boolean [100][100][10];//
	static String strs[] = new String[100];//
	public static int wayans[][] = new int [100][100];//
	static int nummax;//
	public static boolean findway[][]  = new boolean [100][100];//
	public static int way[][] = new int[100][100];//
	public static int mmm[][] = new int [100][100];//
	public static int flow[][][] = new int [100][100][10];//
	/*
	 * Overview : initialize map and calculate the shortest way. 
	 * 
	 * strs != null && x != null && y != null && strs.length >= 100 && nummax == 80 && x.length >= 10000 && y.length >= 10000
	 * && map != null && wayans != null && findway != null && way != null && mmm != null && flow != null && map.lenth >= 100
	 * && map[0].length >= 100 && map[0][0].length >= 10 && wayans.length >= 100 && wayans[0].length >= 100
	 * && findway.length >= 100 && findway[0].length >= 100 && way.length >= 100 && way[0].length >= 100
	 * && mmm.length >= 100 && mmm[0].length >= 100 && && flow.length >= 100 && flow[0].length >= 100 && flow[0][0].length >= 100 &&
	 * for all 0 <= i < 80 ,map[0][i][0] == false && map[i][0][2]==false && map[79][i][1] && map[i][79][3]
	 */
	public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
	  try{
		if(strs == null) return false;
		if(strs.length < 100) return false;
		if(nummax != 80)return false;
		if(x == null)return false;
		if(y == null)return false;
		if(x.length < 10000)return false;
		if(y.length < 10000)return false;
		if(map == null)return false;
		if(wayans == null)return false;
		if(findway == null)return false;
		if(way == null)return false;
		if(mmm == null)return false;
		if(flow == null) return false;
		
		if(map.length < 100)return false;
		if(map[0].length < 100)return false;
		if(map[0][0].length < 10)return false;
		
		if(wayans.length < 100)return false;
		if(wayans[0].length < 100)return false;
		
		if(findway.length < 100)return false;
		if(findway[0].length < 100)return false;
		
		if(way.length < 100)return false;
		if(way[0].length < 100)return false;
		
		if(mmm.length < 100)return false;
		if(mmm[0].length < 100)return false;
		
		if(flow.length < 100)return false;
		if(flow[0].length < 100)return false;
		if(flow[0][0].length < 10)return false;
	     
		for(int i = 0;i < 80;i ++)
		{
			if(map[0][i][0])return false;
			if(map[i][0][2])return false;
			if(map[79][i][1])return false;
			if(map[i][79][3])return false;
		}
		
	  }catch (Exception e){return false;}
		return true;
	}
	public init(int nm)
	 /*
	@REQUIRES:int nm;
	@MODIFIES:nummax;
    @EFFECTS:nummax == nm;
   */
	{
		nummax = nm;
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
            	if(strs[i].charAt(jjj) != '0' && strs[i].charAt(jjj) != '1' && strs[i].charAt(jjj) != '2' && strs[i].charAt(jjj) != '3')
            		return false;
            }
            if(jjj != nummax)return false;
            line = reader.readLine(); // ��ȡ��һ��
            i ++;
            //System.out.println(i + ":" + strs[i-1]);
            
        }
        if(i != nummax)return false;
        reader.close();
        is.close();
        return true;
    }
	public static void build_map()
    /*@MODIFIES:this
    @EFFECTS:(\all int i,j; 0 <= i,j <= 79; mmm[i][j] == 1) ==> (map[i][j][3] == true;map[i][j+1][2] == true)
             (\all int i,j; 0 <= i,j <= 79; mmm[i][j] == 2) ==> (map[i][j][0] == true;map[i-1][j][1] == true)
             (\all int i,j; 0 <= i,j <= 79; mmm[i][j] == 3) ==> (map[i][j][0] == true;map[i-1][j][1] == true;map[i][j][3] == true;map[i][j+1][2] == true)
             (\exists illegal input) \result == false;
             (\all input legal) \result == true;
   */
    {
    	int i,j;
    	for(i = 0;i < nummax; i ++)
    	   for(j = 0;j < nummax; j++)
    	   {
    		   char s = strs[i].charAt(j);
    		   if(s == '0')
    		   {   mmm[i][j] = 0;
    		   }
    		   if(s == '1')
    		   {   mmm[i][j] = 1;
    			   map[i][j+1][2] = true;
    			   map[i][j][3] = true;
    		   }
    		   if(s == '2')
    		   {   mmm[i][j] = 2;
    			   map[i+1][j][0] = true;
    			   map[i][j][1] = true;
    		   }
    		   if(s == '3')
    		   {   mmm[i][j] = 3;
    			   map[i][j+1][2] = true;
    			   map[i][j][3] = true;
    			   map[i+1][j][0] = true;
    			   map[i][j][1] = true;
    		   }
    		  // System.out.println("x="+i+" y="+j+" s="+s);
    	   }
    }
    static int x[] = new int [10000];
    static int y[] = new int [10000];
    public static void find(int xin,int yin)
    
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
    		    wayans[i][j] = -1;
    		}
    	x[1] = xin;
    	y[1] = yin;
    	findway[xin][yin] = true;
    	wayans[xin][yin] = 0;
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
    			wayans[xx-1][yy] = wayans[xx][yy] + 1;
    			way[xx-1][yy] = 1;
    		 }
    		if(map[xx][yy][1] && !findway[xx+1][yy])
    		{
    			x[++head] = xx+1;
    			y[head ]  = yy;
    			findway[xx+1][yy] = true;
    			wayans[xx+1][yy] = wayans[xx][yy] + 1;
    			way[xx+1][yy] = 0;
   		 	}
    		if(yy != 0)
    		if(map[xx][yy][2] && !findway[xx][yy-1])
   		 	{
    			x[++head] = xx;
    			y[head ]  = yy-1;
    			findway[xx][yy-1] = true;
    			wayans[xx][yy-1] = wayans[xx][yy] + 1;
    			way[xx][yy-1] = 3;
   		 	}
    		if(map[xx][yy][3] && !findway[xx][yy+1])
   		 	{
    			x[++head] = xx;
    			y[head ]  = yy+1;
    			findway[xx][yy+1] = true;
    			wayans[xx][yy+1] = wayans[xx][yy] + 1;
    			way[xx][yy+1] = 2;
   		 	}
    		
    	}
    }
    public boolean initial()
    /*@MODIFIES:this
      @EFFECTS:(\all int i;mmm[i] == file[i])   (把文件里的内容读到mmm数组里面)
               (\all int i,j; 0 <= i,j <= 79; mmm[i][j] == 1) ==> (map[i][j][3] == true;map[i][j+1][2] == true)
               (\all int i,j; 0 <= i,j <= 79; mmm[i][j] == 2) ==> (map[i][j][0] == true;map[i-1][j][1] == true)
               (\all int i,j; 0 <= i,j <= 79; mmm[i][j] == 3) ==> (map[i][j][0] == true;map[i-1][j][1] == true;map[i][j][3] == true;map[i][j+1][2] == true)
               (\exists illegal input) \result = false;
               (\all input legal) \result == true;
     */
    {
    	boolean bb = false;
    	while(bb == false)
    	{
    		try
    		{
    			bb = readstrs("C://ooo/detail.txt");
    	
    		}catch(Exception e){bb = false;}
    	   if(bb == false){System.out.println("Invalid map input! Please modify the input and run again!");return false;}
    		//bb = true;
    	}
    	build_map();
    	for(int i = 0;i < nummax;i ++)
    		for(int j = 0;j < nummax;j ++)
    	{
    			if(i == 0 && map[i][j][0])return false;
    			if(i == nummax -1  && map[i][j][1])return false;
    			if(j == 0 && map[i][j][2])return false;
    			if(j == nummax -1 && map[i][j][3])return false;
    	}
    	find(0,0);
    	for(int i = 0;i < nummax; i ++)
    	{
    		for(int j = 0;j < nummax; j ++)
    		{
    			if(wayans[i][j] == -1)return false;
    			//System.out.print(findway[i][j]);
    		}//System.out.print("\n");
    	}
    	return true;
    }

}
