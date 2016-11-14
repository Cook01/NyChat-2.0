import java.net.*;
import java.io.*;

public class ChatClient implements Runnable 
{

	public static Boolean done			= false;

	private Boolean lecture				= false;
	private Socket socket 				= null;
	private DataInputStream console		= null;
	private DataOutputStream streamOut 	= null;
	private	DataInputStream streamIn	= null;

	private ChatClient(String serverName, int serverPort, Boolean lecture)
	{
		if(!lecture) System.out.println("Connexion au serveur...");

		try {

			socket = new Socket(serverName, serverPort);
			if(!lecture) System.out.println("Connecté : " + socket);


			console			= new DataInputStream(System.in);
			streamOut		= new DataOutputStream(socket.getOutputStream());

			this.lecture	= lecture;

		} catch(UnknownHostException uhe) {

			if(!lecture) System.out.println("Serveur inconnu : " + uhe.getMessage());

		} catch (IOException ioe) {

			if(!lecture) System.out.println("Erreur inconnue : " + ioe.getMessage());

		}

	}


	public void run() 
	{

		String line = "";

		if( lecture ) {


			try {

				streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			} catch(IOException ioe) {

				System.out.println("Erreur serveur : " + ioe.getMessage());

				return;

			}

			while(!ChatClient.done) {

				try {

					line = streamIn.readUTF();
					System.out.println("message recu : " + line);

				} catch(IOException ioe) {

					System.out.println("Erreur serveur : " + ioe.getMessage());

				}

			}

		} else {

			

			while(!line.equals("/quit")) {

				try {

					line = console.readLine();
					streamOut.writeUTF(line);
					streamOut.flush();

				} catch(IOException ioe) {

					System.out.println("Erreur serveur : " + ioe.getMessage());

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

		}
	}

	public static void main(String args[])
	{  


		ChatClient client = null;
      	if (args.length != 2)
         	System.out.println("Usage: java ChatClient host port");
      	else {

          	Thread ecriture = new Thread( new ChatClient(args[0], Integer.parseInt(args[1]), false) );
          	Thread lecture  = new Thread( new ChatClient(args[0], Integer.parseInt(args[1]), true) );

    		ecriture.start();
    		lecture.start();

      	}
         	
   }

}