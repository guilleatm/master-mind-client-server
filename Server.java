import java.net.*;
import java.io.*;
import java.util.Date;


public class Server {

	public final static int PORT = 1200;
	private static int clients = 0;

	// Server actions
	private static final String[] actions = {"C_MOVES"};


	private static ServerSocket serverSocket;
	
	
	public static void main(String[] a) {

		System.setProperty("line.separador","\r\n");
		

		initServerSocket();

		
		while (true) {
			
			try (Socket socket = serverSocket.accept()) { // try-with-resources --> socket is autoclosed.
				

				ClientThread clientThread = new ClientThread(socket);
				clientThread.start();



				// OutputStream outputStream = socket.getOutputStream();
				// InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());

				// //Writer out = new OutputStreamWriter(outputStream, "ASCII");
				// PrintWriter out = new PrintWriter(outputStream, true);
				// BufferedReader in = new BufferedReader(inputStreamReader);






				// if (!in.readLine().equals("JOIN_GAME")) {
				// 	socket.close();
				// }

				// System.out.println("Client " + socket.getLocalAddress() + ":" + socket.getPort() + " joined the game!!");



				// String msg = "MOVE\nArguments: 0, 1, 2, etc...";
				// out.println(msg);
				// //out.flush();


				// String action = in.readLine();
				// //String args = in.readLine();

				// System.out.println(action);

				// if (action != null) {
				// 	chooseAction(new String[] {action, null});
				// }






				
			
				// socket.close(); // try () {}

			} catch (IOException e) {
				// problem with one client; don't shut down the server
				System.err.println(e.getMessage());
			}
		}
	}


	private static void chooseAction(String[] clientMessage) {
		String action = clientMessage[0];
		String args = clientMessage[1];


		for (int i = 0; i < actions.length; i++) {
			
			if (actions[i].equals(action)) {

				switch (i) {

					case 0: // CLIENT_MOVE
						System.out.println("Client has moved " + args);
						//move();
						break;
					case 1: // MOVE RESPONSE
						//showMoveResponse(args);
						break;
					default:
						System.out.println("Invalid action key");
						break;
				}
				return;
			}
		}
	}


	// private static String[] lookForServerResponse() {
	// 	String action = null;
	// 	String args = null;
	// 	while (action == null) {
	// 		try {
	// 			int i = 0;
	// 			for (String line = in.readLine(); line != null; line = in.readLine()) {
	// 				switch (i) {
	// 					case 0:
	// 						action = line;
	// 						break;
	// 					case 1:
	// 						args = line;
	// 						break;
	// 					default:
	// 						System.out.println("Demasiados argumentos en el mensaje");
	// 						break;
	// 				}
					
	// 				i++;
	// 			}
	// 		} catch (IOException e) {}
	// 	}

	// 	String[] serverMessage = {action, args};
	// 	return serverMessage;
	// }



	private static void initServerSocket() {
		serverSocket = null;

		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.print("Fail creating the socket at port " + PORT + "\n");
		}
	}

}