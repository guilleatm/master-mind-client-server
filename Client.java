import java.net.*;
import java.util.*;
import java.io.*;

public class Client {


	private final static String SERVER_NAME = "localhost";
	private final static int PORT = 1200;

	private static Socket socket;

	private static PrintWriter out;
	private static BufferedReader in;

	private static final String[] actions = {"MOVE", "MOVE_RESPONSE"};

	public static void main(String[] args) {



	System.setProperty("line.separador","\r\n");



	initSocket();
	
	try {

		OutputStream outputStream = socket.getOutputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
		
		out = new PrintWriter(outputStream, true);
		in = new BufferedReader(inputStreamReader);


		joinGame();

		String[] serverMessage;

		for (int i = 0; i < 15; i++) {
			serverMessage = lookForServerResponse();
			chooseAction(serverMessage);
		}

		socket.close();
		

	} catch(IOException e) {
		System.out.println("No es posible realizar la conexión\n" + e);
	}

	}



	private static void initSocket() {
		try {
			socket = new Socket();
			InetAddress IPDir = InetAddress.getByName(SERVER_NAME);
			SocketAddress address = new InetSocketAddress (IPDir, PORT);

			socket.connect(address);

			System.out.println("Conexión establecida");
			System.out.println("Servidor: " + SERVER_NAME + " " + PORT);

		} catch(UnknownHostException e) {
			System.out.println("Nombre del servidor desconocido\n" + e);
		} catch (IOException e) {
			System.out.println("No es posible realizar la conexión\n" + e);
		} 
	}

	private static void joinGame() {
		out.println("JOIN_GAME");
	}

	private static String[] lookForServerResponse() {
		String action = null;
		String args = null;
		while (action == null) {
			try {
				int i = 0;
				for (String line = in.readLine(); line != null; line = in.readLine()) {
					switch (i) {
						case 0:
							action = line;
							break;
						case 1:
							args = line;
							break;
						default:
							System.out.println("Demasiados argumentos en el mensaje");
							break;
					}
					
					i++;
				}
			} catch (IOException e) {}
		}

		String[] serverMessage = {action, args};
		return serverMessage;
	}

	private static void move() {
		System.out.print("Te toca, introduce tu jugada: ");
		String move = System.console().readLine();
		out.println(move);
	}

	private static void showMoveResponse(String response) {
		System.out.println("Resultado: " + response);
	}

	private static void chooseAction(String[] serverMessage) {

		String action = serverMessage[0];
		String args = serverMessage[1];


		for (int i = 0; i < actions.length; i++) {
			
			if (actions[i].equals(action)) {

				switch (i) {

					case 0: // MOVE
						move();
						break;
					case 1: // MOVE RESPONSE
						showMoveResponse(args);
						break;
					default:
						System.out.println("Invalid action key");
						break;
				}
				return;
			}
		}
	}

}