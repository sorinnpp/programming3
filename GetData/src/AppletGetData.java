import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import java.util.Date;


public class AppletGetData extends JApplet implements Runnable
{
	private int screenX = 1920;
	private int screenY = 1080;
	private int n = 1;
	Thread writeToFile;
	Date date;
	boolean running = true;
	File file = new File("D:\\Desktop\\Medialogy\\P3\\DataFiles\\Test"+n+".txt");
	
	public void AppletGetData ()
	{
		
	}
	public void init ()
	{
		//Initiate the GetData class to gather data and the writeToFile thread to put the data in a textfile.
		GetData getData = new GetData();
		writeToFile = new Thread(this);
		writeToFile.start();

		
		
	}
	public void destroy ()
	{
		running = false;
		writeToFile = null;
	}
	
	public void run()
	{
		BufferedWriter printOut = null;
		
		// If the filename already exists, it calls the method to do some recursion.
		if (file.exists())
		{
			checkFile(file);
		}
		
		if (!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			printOut = new BufferedWriter(new FileWriter(file));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(running)
		{
			//Get the time.
			date = new Date();
			String time = date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
			
				try {
					// if looking at screen.
					if (GetData.gX > 0 && GetData.gY > 0 && GetData.gX < screenX && GetData.gY < screenY)
					{
						//printout x and y locations to file.
						printOut.write("x = " + GetData.gX+ " y = "+ GetData.gY +" at time: " + time);
						printOut.newLine();
						printOut.flush();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
	               {
	                     // Wait 250 milliseconds before continuing
	                    writeToFile.sleep(250);
	               }
	               catch (InterruptedException e)
	               {
	                    System.out.println(e);
	                }
			
			
		}
		
	}
	
	//a recursive method that keeps adding 1 to n until the filename is free.
	public void checkFile(File file)
	{
		if (file.exists())
		{
			n++;
			file = new File("D:\\Desktop\\Medialogy\\P3\\DataFiles\\Test"+n+".txt");
			this.file = file;
			checkFile(file);
		}
	}
}
