import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class ChatServerMain{
	private ArrayList<Socket> clientList;
   	private ServerSocket server;
   

   	public ChatServerMain(int port){
   		clientList = new ArrayList<Thread>();

   		try{
   			System.out.println("Creation du sever sur le port " + port);
			server = new ServerSocket(port);

			do{
				System.out.println("Attente de client ...");
				Socket socket = server.accept();

				int idClient = clientList.size();
				clientList.add(socket);

				System.out.println("Creation du Thread");
				Thread client = new Thread(new ChatServerThread(socket, id, this));

				System.out.println("Demarage du Thread");
				client.start();

				Thread.sleep(1000);

				

			} while(clientList.size() > 0);


		} catch(Exception e) {
			System.out.println(e); 
		}
	}

	public ArrayList<Thread> getClientList(){
		return this.clientList;
	}
	
	public static void main(String args[]){
		ChatServerMain server = null;

		if (args.length != 1)
			System.out.println("Usage: java ChatServer port");
		else
			server = new ChatServerMain(Integer.parseInt(args[0]));
	}
}