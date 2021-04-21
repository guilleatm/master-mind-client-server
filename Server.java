import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Random;

public class Server {

	public final static int PORT = 1200;
	private static int clients = 0;
	public static int turn = 0;
	public static int numPlayers = 0;

	// Server actions
	public static final String[] actions = {"C_MOVES", "LOSE"};

	public static final char[] colors = {'R', 'Y', 'B', 'G'};
	private static char[][] combinations;


	private static ServerSocket serverSocket;
	
	
	public static void main(String[] a) {

		System.setProperty("line.separador","\r\n");
		

		initServerSocket();
		combinations = generateCombinations();

	

		while (true) {

			
			try { // try-with-resources --> socket is autoclosed.
				
				
				Socket socket = serverSocket.accept();

				Random r = new Random();
				char[] c = combinations[r.nextInt(5)];


				ClientThread clientThread = new ClientThread(socket, numPlayers++, c);
				clientThread.start();

			} catch (IOException e) {
				// problem with one client; don't shut down the server
				System.err.println(e.getMessage());
			}
		}
	}

	public static void updateTurn() {
		turn++;
		turn = turn % numPlayers;
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

	private static void initServerSocket() {
		serverSocket = null;

		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.print("Fail creating the socket at port " + PORT + "\n");
		}
	}


	// JUEGO

	private static char[][] generateCombinations() {
		Random random = new Random();
		

		char[][] combinations = new char[5][4];
	
		for (int n = 0; n < 5; n++) {
	
			char[] l = new char[4];

			for (int i = 0; i < 4; i++) {
				l[i] = colors[random.nextInt(4)];
			}
		
			combinations[n] = l;
		}

		return combinations;
	
	}






}