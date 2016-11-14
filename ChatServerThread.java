import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class ChatServerThread implements Runnable{
	private Socket client = null;
	private DataInputStream streamIn =  null;
	private ChatServerMain server = null;
	private int id;


	public ChatServerThread(Socket client, int id, ChatServerMain server){
		this.server = server;
		this.client = client;

		try{
			open();
		}catch(Exception e){
			System.out.println(e);
		}
	}


	public void run(){
		boolean done = false;
		while(!done){
			try{
				String line = streamIn.readUTF();
				System.out.println(line);
				done = line.equals("/quit");
			} catch(IOException ioe) {
				System.out.println(ioe);
				done = true;
			}

			try{
				close();
			}catch(Exception e){
				System.out.println(e);
			}
		}
	}

	public void open() throws IOException{
		streamIn = new DataInputStream(new BufferedInputStream(client.getInputStream()));
	}

	public void close() throws IOException{
		if (client != null)
			client.close();

		server.getClientList().remove(this);

		if (streamIn != null)
			streamIn.close();
	}
}