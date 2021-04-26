import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Random;

public class Server {

	public final static int PORT = 1200;
	private static int clients = 0;
	public static int turn = 0;
	public static int numPlayers = 0;

	public static int someoneWon = -1;

	// Server actions
	public static final String[] actions = {"C_MOVES"};

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

// CLASE CLIENT_THREAD

class ClientThread extends Thread {


	private Socket socket;
	private int id;
	private char[] combination;

	private Writer out;
	private BufferedReader in;


	public ClientThread(Socket socket, int id, char[] combination) {
		this.socket = socket;
		this.id = id;
		this.combination = combination;
	}

	public void run() {
		try {
			manageRequest();
		} catch(UnknownHostException e) {
			System.out.println("Host desconocido");
		} catch(IOException e) {

			System.out.println("Juagador desconectado " + id);
		}
	}

	private void manageRequest() throws UnknownHostException, IOException {


		System.out.println("Tengo que adivinar: " + String.valueOf(combination));


		OutputStream outputStream = socket.getOutputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());

		
		out = new OutputStreamWriter(outputStream, "ASCII");
		in = new BufferedReader(inputStreamReader);


		if (!in.readLine().equals("JOIN_GAME")) {
			socket.close();
		}


		for (int i = 0; i < 15; i++) {

			// Espero turno
			while (Server.turn != id) {
				try {
					sleep(2000);
				} catch (InterruptedException e) {}
			}

			if (Server.someoneWon >= 0) {
				String msg = "LOSE\n" + Server.someoneWon + "\n";
				out.write(msg);
				out.flush();
				break;
			}
			
			String msg = "MOVE\nnull\n";
			out.write(msg);
			out.flush();


			String[] message = readClientRequest();
			chooseAction(message);


			Server.updateTurn();
		}
	}

	private String[] readClientRequest() {

		String action = null;
		String args = "null";

		try {
			
			while (action == null || action.length() < 1) {
				action = in.readLine();
			}
			args = in.readLine();
			
		} catch (IOException e) {
			System.out.println("No se ha podido leer");
		}		

		return new String[] {action, args};
	}

	private void chooseAction(String[] message) {
		String action = message[0];
		String args = message[1];

		for (int i = 0; i < Server.actions.length; i++) {
			if (Server.actions[i].equals(action)) {

				switch (i) {
					
					case 0: // C_MOVES
						processClientMove(args);
						break;
					default:
						System.out.println("Invalid action");
						break;

				}
				return;
			}
		}
		System.out.println("Invalid action: " + action);
	}


	private void processClientMove(String move) {
		
		int black = 0;
		int white = 0;		

		for (int i = 0; i < 4; i++) {
			if (move.charAt(i) == combination[i]) {
				black++;
			} else if (String.valueOf(combination).indexOf(move.charAt(i)) != -1) {
				white++;
			}
		}

		if (black == 4) { // CLIENTE GANA
			send("WIN\nGANASTE!!\n");
			Server.someoneWon = id;

		} else { // CLIENTE NO GANA
			String result = "B" + black + "W" + white;
			send("MOVE_RESPONSE\n" + result + "\n");
		}
	}

	private void send(String message) {
		try {
			out.write(message);
			out.flush();
		} catch (IOException e) {
			System.out.println("Error al enviar");
		}
	}	
}