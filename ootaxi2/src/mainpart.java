//import test.TaxiGUI;

//import test.TaxiGUI;


public class mainpart {
	static init init1;
	static taxis taxs = new taxis();
	static pubwork pw;
	static reqinput ri;
	static int nummax = 80;
	static int taximax = 100;
	static txtoutput tt = new txtoutput();
	static for_tester ft;
	static redgreeninit rg;
	/*
	 * Overview : controlls ans initialize all the threads
	 * nummax == 80 && taximax == 100 && taxs != null && tt != null
	 * 
	 * 
	 */
	static public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
		if(nummax != 80)return false;
		if(taximax != 100)return false;
		if(taxs == null)return false;
		if(tt == null)return false;
		return true;
	}
	
	
	
	public static void set_taxi(TaxiGUI gui)
	/*
	 * @MODIFIES:this
	 * @EFFECTS:taxis set
	 * 
	 */
	{
		java.util.Random r=new java.util.Random(); 
		for(int i = 0;i < 30;i ++)
		{
			int xx = r.nextInt();
			int yy = r.nextInt();
			if(xx < 0)xx = -xx;
			if(yy < 0)yy = -yy;
			taxs.taxx[i] = new taxi_new(xx%nummax,yy%nummax,gui,tt,init1,false);
			
			taxs.taxx[i].self = i;
			taxs.taxx[i].pw = pw;
			
		}
		for(int i = 30;i < taximax;i ++)
		{
			int xx = r.nextInt();
			int yy = r.nextInt();
			if(xx < 0)xx = -xx;
			if(yy < 0)yy = -yy;
			taxs.taxx[i] = new taxi_new(xx%nummax,yy%nummax,gui,tt,init1,true);
			
			taxs.taxx[i].self = i;
			taxs.taxx[i].pw = pw;
			
		}
	}
	
	
	
	public static void main(String[] args)
	/*
    @MODIFIES:this
    @EFFECTS:
        (pw == null) ==>(pw = new pubwork(nummax,taxs);)
        (init1 == null) ==> (init1 = new init(nummax);)
   */
	{
	try{
		
		TaxiGUI gui=new TaxiGUI();
		init1 = new init(nummax);
		rg = new redgreeninit();
		rg.gui = gui;
		rg.ini = init1;
		try
		{
			pw = new pubwork(80,null);
			System.out.println("initial success!");
			
		}catch (Exception e)
		{System.out.println("initial error!");}
		
		pw = new pubwork(nummax,taxs);//System.out.println(pw.repOK());//while(true){if(1 == 2) break;}
		pw.ini = init1;
		pw.gui = gui;
		java.util.Random r=new java.util.Random(); 
		set_taxi(gui);
	//	System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
		if(init1.initial() && rg.readstrs("C://ooo/summary.txt"))
		{
			gui.LoadMap(init1.mmm, 80);
			ri = new reqinput(taximax,nummax,pw,taxs,gui);
			for(int i = 0;i < taximax;i ++)
			{
				taxs.taxx[i].rg = rg;
				for(int j = 0;j < taximax;j ++)
					for(int k = 0;k < taximax;k ++)
					{
						taxs.taxx[i].map[j][k][0] = init1.map[j][k][0];
						taxs.taxx[i].map[j][k][1] = init1.map[j][k][1];
						taxs.taxx[i].map[j][k][2] = init1.map[j][k][2];
						taxs.taxx[i].map[j][k][3] = init1.map[j][k][3];
					}
				taxs.taxx[i].start();
			}
			ft = new for_tester(taxs,ri);
			ft.start();
			ri.start();
			rg.start();
		}
		else
		{System.out.println("Invalid map input! Please modify the input and run again!");
		}
	 }catch (Exception e){}
	}

}
