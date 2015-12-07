import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;

import netscape.javascript.*;
import javax.swing.JApplet;

import java.util.Arrays;
import java.util.Date;


public class AppletGetData extends JApplet implements Runnable
{
	private int screenX = 1280;
	private int screenY = 1080;
	private int n = 1;
	Thread writeToFile;
	Date date;
	boolean running = true;
	File file = new File("D:\\Desktop\\Medialogy\\P3\\DataFiles\\Test"+n+".txt");
	String homepage;
	private double scrollbarTop;
	private double scrollMax;
	private double scrollPercent;
	String last;
	private int x;
	private int y;
	
	public AppletGetData ()
	{
		
	}
	public void init ()
	{
		//Initiate the writeToFile thread to put the data in a textfile.
		writeToFile = new Thread(this);
		writeToFile.start();
		checkHomepage();
		
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
			try {
				getData();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Get the time.
			date = new Date();
			String time = date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
			
			// See how far down the scrollbar is.
			checkScrollbar();
			
				try {
					// if looking at screen.
					if (!last.equals("heartbeat") && x > 0 && x < screenX && y > 0 && y < screenY)
					{
						//printout x, y, page, the location of the scrollbar in pixels from the top, max amount you can scroll, how much is scrolled in percent, and time, to file.
						printOut.write(x +";"+y+";" + homepage + ";"+scrollbarTop+";"+scrollMax+";"+scrollPercent+";" + time);
						printOut.newLine();
						printOut.flush();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
	               {
	                     // Wait 50 milliseconds before continuing
	                    writeToFile.sleep(50);
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
	
	//this checks what the URL the applet is on and saves it into a variable.
	public void checkHomepage ()
	{
		String location = getDocumentBase().toString();
		homepage = location;
	}
	
	public void getData() throws IOException
	{
		
		BufferedReader input = new BufferedReader(new FileReader("D:\\Desktop\\Test.txt"));
		String line;

	    while ((line = input.readLine()) != null) 
	    {
	        last = line;
	    }
	    
	    if (last.contains("tracker"))
        {
        	last = last.replaceAll("[^-?0-9]+", " ");
        	String [] num = last.split(" ");
        	x = Integer.parseInt(num[2]);
        	y = Integer.parseInt(num [4]);
        }
        else
        {
        	last = "heartbeat";
        }
	}
	
	public void checkScrollbar ()
	{
		try
		{
			//Get the window the applet is in
			JSObject win = (JSObject) JSObject.getWindow(this);
			
			//Getting the position of the scrollbar and the max scrollbar, and the percent.
			scrollbarTop =  (int) win.getMember ("scrollY");
			scrollMax = (int) win.getMember("scrollMaxY");
			scrollPercent = ((double)scrollbarTop/(double)scrollMax) * 100;
		}
		catch (JSException jse) {
			jse.printStackTrace();
			
		}
	}
}
