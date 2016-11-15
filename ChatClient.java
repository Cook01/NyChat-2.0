import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient implements Runnable 
{

	public static Boolean done			= false;

	private Boolean lecture				= false;
	private Socket socket 				= null;
	private DataInputStream console		= null;
	private PrintWriter streamOut 	= null;
	private	Scanner streamIn	= null;

	private ChatClient(String serverName, int serverPort, Boolean lecture, Socket socket)
	{
		if(!lecture) System.out.println("Connexion au serveur...");



		this.socket = socket;
		if(!lecture) System.out.println("Connecté : " + this.socket);


		console			= new DataInputStream(System.in);
		

		this.lecture	= lecture;



	}


	public void run() 
	{

		String line = "";

		if( lecture ) {


			try {

				streamIn = new Scanner(this.socket.getInputStream());
			} catch(IOException ioe) {

				System.out.println("Erreur serveur : " + ioe.getMessage());

				ChatClient.done = true;

			}

			while(!ChatClient.done ) {



					if(streamIn.hasNext()) {
						line = streamIn.nextLine();
					System.out.println("message recu : " + line);
					}
					

				

			}

		} else {

			

			while(!line.equals("/quit")) {

				try {

					streamOut		= new PrintWriter(this.socket.getOutputStream());


					line = console.readLine();

					System.out.println("(test)" + line);

					streamOut.println(line);
					streamOut.flush();

				} catch(IOException ioe) {

					System.out.println("Erreur serveur (read) : " + ioe.getMessage());

					ChatClient.done = true; 

				}

			}

			ChatClient.done = true; 

			stop();

		}

		
		

	} 

	public void stop()
	{
		try {

			if( console 	!= null ) console.close();
			if( streamOut	!= null ) streamOut.close();
			if( socket 		!= null ) socket.close();

		} catch (IOException ioe) {

			System.out.println("Erreur lors de la fermeture du serveur");
			ChatClient.done = true;

		}
	}

	public static void main(String args[])
	{  


		ChatClient client = null;
      	if (args.length != 2)
         	System.out.println("Usage: java ChatClient host port");
      	else {


			try {

				Socket socket = new Socket(args[0], Integer.parseInt(args[1]));

	          	Thread ecriture = new Thread( new ChatClient(args[0], Integer.parseInt(args[1]), false, socket) );
	          	Thread lecture  = new Thread( new ChatClient(args[0], Integer.parseInt(args[1]), true, socket) );

	    		ecriture.start();
	    		lecture.start();

	    	} catch(UnknownHostException uhe) {

				System.out.println("Serveur inconnu : " + uhe.getMessage());
				ChatClient.done = true;

			} catch (IOException ioe) {

				System.out.println("Erreur inconnue : " + ioe.getMessage());
				ChatClient.done = true;

			}

      	}
         	
   }

}