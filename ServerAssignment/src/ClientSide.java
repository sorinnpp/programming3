import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;


public class ClientSide {
	
	public ClientSide() throws UnknownHostException, IOException, InterruptedException{	
		
	}
	
	public Socket Connect() throws UnknownHostException, IOException{
		Socket MyClient = null;
	    try {
	           MyClient = new Socket("127.0.0.1", 13333);
	           
	    }
	    catch (IOException e) {
	        System.out.println("Did not connects!");
	    }
		return MyClient;
	}
	
	
	public void Send(Socket socket) throws IOException, InterruptedException{
		
		
		String fp = "C:\\Users\\Peter\\Desktop\\Giraffe.jpg";
		
		OutputStream outputStream = socket.getOutputStream();
		BufferedImage image = ImageIO.read(new File(fp));
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        
        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
        outputStream.write(size);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.flush();
        System.out.println("Flushed: " + System.currentTimeMillis());

        Thread.sleep(120000);
        System.out.println("Closing: " + System.currentTimeMillis());
        socket.close();

	}

}

