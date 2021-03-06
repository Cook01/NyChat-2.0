import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class ChatServerReadingThread implements Runnable{
	private int id;

	private Socket client = null;
	private ChatServerMain server = null;	
	private Scanner in;

	public ChatServerReadingThread(Socket client, int id, ChatServerMain server){
		this.server = server;
		this.client = client;

		try{
			open();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void run(){
		boolean done = false;
		while(!done){
			if(in.hasNext()){
				String line = in.nextLine();
				System.out.println(line);
				done = line.equals("/quit");
			}
		}
		try{
			close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void open() throws IOException{
		in = new Scanner(client.getInputStream());
	}

	public void close() throws IOException{
		server.closeId(id);

		if (in != null)
			in.close();
	}
}