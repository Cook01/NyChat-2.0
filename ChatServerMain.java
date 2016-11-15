import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class ChatServerMain{
	private ArrayList<Socket> clientList;
   	private ServerSocket server;

   	private PrintWriter out;

   	public ChatServerMain(int port){
   		clientList = new ArrayList<Socket>();

   		try{
   			System.out.println("Creation du sever sur le port " + port);
			server = new ServerSocket(port);

			do{
				System.out.println("Attente de client ...");
				Socket socket = server.accept();
				System.out.println(socket);

				int idClient = clientList.size();
				clientList.add(socket);

				System.out.println("Creation du Thread");
				Thread client = new Thread(new ChatServerThread(socket, idClient, this));

				System.out.println("Demarage du Thread");
				client.start();

				Thread.sleep(1000);

			} while(true);

		} catch(Exception e) {
			System.out.println(e); 
		}
	}
	
	public void closeId(int id){
		try{
			System.out.println("Client close : " + clientList.get(id));
			clientList.get(id).close();
			clientList.remove(id);
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void write(String line){
		for(Socket client : clientList){
			try{
				out = new PrintWriter(client.getOutputStream());

				out.println(line);
				out.flush();

				out.close();
			} catch(Exception e){
				e.printStackTrace();
			}
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