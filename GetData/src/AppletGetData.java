import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import netscape.javascript.*;
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
	String homepage;
	private int SBPpercent;
	
	public AppletGetData ()
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
			
			// See which page we are on and how far down the scrollbar is.
			checkHomepage();
			checkScrollbar();
			
				try {
					// if looking at screen.
					if (GetData.gX > 0 && GetData.gY > 0 && GetData.gX < screenX && GetData.gY < screenY)
					{
						//printout x, y, page, location of scrollbar, and time, to file.
						printOut.write("x = " + GetData.gX+ " y = "+ GetData.gY + " on page: " + homepage + "with scrollbar "+SBPpercent+"% from the top"+" at time: " + time);
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
	
	//this checks what the URL the applet is on and saves it into a variable.
	public void checkHomepage ()
	{
		String location = getDocumentBase().toString();
		String home = "http://asgarddk.wix.com/msm1";
		String films = "http://asgarddk.wix.com/msm1#!films/cwzt";
		String series = "http://asgarddk.wix.com/msm1#!series/chrj";
		
		if (location.equals(home))
		{
			homepage = "home";
		}
		if (location.equals(films))
		{
			homepage = "films";
		}
		if (location.equals(series))
		{
			homepage = "series";
		}
		else
		{
			homepage = null;
		}
	}
	public void checkScrollbar ()
	{
		JSObject win = (JSObject) JSObject.getWindow(this);
		//JSObject doc = (JSObject) win.getMember("document");
		//JSObject loc = (JSObject) doc.getMember("location");
		int scrollbarTop = (int) win.getMember("pageYOffset");
		int scrollHeight = (int) win.getMember("scrollHeight");
		int clientHeight = (int) win.getMember("clientHeight");
		
		int scrollbarPosition = scrollHeight - clientHeight;
		//how far down the scrollbar is in percent
		SBPpercent = (scrollbarTop / scrollbarPosition) * 100;
	}
}
