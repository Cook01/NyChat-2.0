import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class ChatServerMain{
	private ArrayList<Socket> clientList;
	private ArrayList<PrintWriter> writerList;
   	private ServerSocket server;

   	private PrintWriter out;

   	public ChatServerMain(int port){
   		clientList = new ArrayList<Socket>();
   		writerList = new ArrayList<PrintWriter>();

   		try{
   			System.out.println("Creation du sever sur le port " + port);
			server = new ServerSocket(port);

			do{
				System.out.println("Attente de client ...");
				Socket client = server.accept();
				System.out.println(client);

				int idClient = open(client);

				System.out.println("Creation du Thread");
				Thread clientThread = new Thread(new ChatServerThread(client, idClient, this));

				System.out.println("Demarage du Thread");
				clientThread.start();

			} while(true);

		} catch(Exception e) {
			System.out.println(e); 
		}
	}

	public int open(Socket client){
		try{
			int idClient = clientList.size();
			clientList.add(client);

			PrintWriter out = new PrintWriter(client.getOutputStream());
			writerList.add(out);

			return idClient;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public void closeId(int id){
		try{
			System.out.println("Client close : " + clientList.get(id));
			clientList.get(id).close();
			clientList.remove(id);

			writerList.get(id).close();
			writerList.remove(id);
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void write(String line){
		for(PrintWriter out : writerList){
			out.println(line);
			out.flush();
		}	
	}

	public static void main(String args[]){
		ChatServerMain server = null;

		if (args.length != 1)
			System.out.println("Usage: java ChatServer port");
		else
			server = new ChatServerMain(Integer.parseInt(args[0]));
	}
}