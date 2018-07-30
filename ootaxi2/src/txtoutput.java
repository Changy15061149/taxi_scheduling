import java.io.File;
import java.io.FileOutputStream;


public class txtoutput {
	FileOutputStream oo1,oo2;
	boolean cnt = true;
	/*
	 * Overview : do the work related to output into files. 
	 * 
	 * oo1 != null && oo2 != null
	 */
	 public boolean repOK()
	/*
	 @EFFECTS:(\all data legal) \result == true;
	 		  (\exists data illegal) \result == false;
	 
	 */
	{
		if(oo1 == null)return false;
		if(oo2 == null)return false;
		return true;
	}
	public txtoutput()
	/*
	@MODIFIES:oo1,oo2;
    @EFFECTS: 
      oo1 = new FileOutputStream("taxi_customer_survice.txt");
      oo2 = new FileOutputStream("taxi_information.txt");
   */
	{
		String st1,st2;
		st1 = "taxi_customer_survice.txt";
		st2 = "taxi_information.txt";
		//C://result.txt
		try
		{
			File file1,file2;
			file1 = new File(st1);
			file2 = new File(st2);
			if(!file1.exists())
			{
				boolean gg = file1.createNewFile();
			}
			if(!file2.exists())
			{
				boolean gg = file2.createNewFile();
			}
		oo1 = new FileOutputStream(st1);
		oo2 = new FileOutputStream(st2);
		}catch (Exception e){System.out.println("Wrong???");
		}
	}
	public synchronized void outputt(String str,boolean bb)
	/*
    @EFFECTS:
    	(bb) ==>( oo2.write(buff,0,buff.length);  )
    	(!bb) ==>( oo1.write(buff,0,buff.length);  )
   */
	{
		while (cnt == false)
		  {
			try{  wait();}
			catch (InterruptedException e){}
		  } 
		cnt = false;
		byte[] buff=new byte[]{}; 
		buff=str.getBytes();  
		if(bb)
		{
			try{
				 oo2.write(buff,0,buff.length);  
			 }
			 catch(Exception e)
			 {} 	
		}
		else
		{
			try{
				 oo1.write(buff,0,buff.length);  
			 }
			 catch(Exception e)
			 {} 	
		}
		cnt = true;
		notifyAll();
	}

}
